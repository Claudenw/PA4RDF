/*
 * Copyright 2017, XENEI.com
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xenei.pa4rdf.diffpatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.apache.jena.arq.querybuilder.UpdateBuilder;
import org.apache.jena.arq.querybuilder.WhereBuilder;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.modify.request.QuadDataAcc;
import org.apache.jena.sparql.modify.request.UpdateDataDelete;
import org.apache.jena.sparql.modify.request.UpdateDataInsert;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.iterator.WrappedIterator;

import difflib.Chunk;
import difflib.Delta;
import difflib.Patch;

/**
 * A factory to create SPARQL UpdateRequests from Patches.
 *
 */
public class UpdateFactory {
    /**
     * Create an UpdateRequest from a Quad patch.
     * 
     * @param patch
     *            the patch
     * @return an UpdateRequest.
     */
    public static UpdateRequest asUpdate(Patch<Quad> patch) {
        final QuadList del = new QuadList();
        final QuadList add = new QuadList();
        final UpdateRequest req = new UpdateRequest();
        for (final Delta<Quad> delta : patch.getDeltas()) {
            switch (delta.getType()) {
            case CHANGE:
                delReq( req, del, delta.getOriginal() );
                addReq( req, add, delta.getRevised() );
                break;
            case DELETE:
                del.addAll( delta.getOriginal().getLines() );
                break;
            case INSERT:
                add.addAll( delta.getRevised().getLines() );
                break;
            }
        }
        if (!del.isEmpty()) {
            req.add( new UpdateDataDelete( new QuadDataAcc( del ) ) );
        }
        if (!add.isEmpty()) {
            req.add( new UpdateDataInsert( new QuadDataAcc( add ) ) );
        }
        return req;
    }

    /**
     * Check if a triple contains a blank node.
     * 
     * @param t
     *            The triple to check.
     * @return True if the triple has a blank node.
     */
    private static boolean hasBlank(Triple t) {
        return t.getSubject().isBlank() || t.getPredicate().isBlank() || t.getObject().isBlank();
    }

    /**
     * Build the delete update for the quads being deleted.
     * 
     * If no where clause is required add the quad to the lst. If a where clause
     * is required (e.g. blank nodes) create the complete update request and add
     * it to the req.
     * 
     * @param req
     *            the request to add the delete with where clause to.
     * @param lst
     *            the list of quads to delete without using where clause
     * @param chunk
     *            the chunk to delete.
     */
    private static void delReq(UpdateRequest req, QuadList lst, Chunk<Quad> chunk) {

        final List<Node> blanks = new ArrayList<Node>( extractBlanks( chunk ) );
        if (blanks.isEmpty()) {
            lst.addAll( chunk.getLines() );
        } else {
            for (final List<Quad> qLst : byGraph( chunk )) {
                final DeleteBlankMapper mapper = new DeleteBlankMapper( blanks );
                final List<Triple> tLst = new TransformingList<Quad, Triple>( qLst, q -> q.asTriple() );

                final UpdateBuilder mod = new UpdateBuilder();

                /*
                 * TODO if anonymous exists as subject for multiple clauses that
                 * we are not deleting then we need to delete the entire block.
                 * Here we are only deleting the initial link.
                 */

                final WhereBuilder wb = new WhereBuilder();

                WrappedIterator.create( tLst.iterator() ).filterKeep( t -> hasBlank( t ) ).mapWith( mapper )
                .forEachRemaining( t -> wb.addWhere( t ) );

                mod.addGraph( qLst.get( 0 ).getGraph(), wb );

                WrappedIterator.create( qLst.iterator() )
                .mapWith( q -> new Quad( q.getGraph(), mapper.apply( q.asTriple() ) ) )
                .forEachRemaining( q -> mod.addDelete( q ) );

                req.add( mod.build() );
            }
        }
    }

    /**
     * Build the insert update for the quads being added.
     * 
     * If no where clause is required add the quad to the lst. If a where clause
     * is required (e.g. blank nodes) create the complete update request and add
     * it to the req.
     * 
     * @param req
     *            the request to add the insert with where clause to.
     * @param lst
     *            the list of quads to inserted without using where clause
     * @param chunk
     *            the chunk to add.
     */
    private static void addReq(UpdateRequest req, QuadList lst, Chunk<Quad> chunk) {

        final List<Node> blanks = new ArrayList<Node>( extractBlanks( chunk ) );
        if (blanks.isEmpty()) {
            lst.addAll( chunk.getLines() );
        } else {

            for (final List<Quad> qLst : byGraph( chunk )) {
                final UpdateBuilder mod = new UpdateBuilder();

                WrappedIterator.create( qLst.iterator() ).forEachRemaining( t -> mod.addInsert( t ) );

                req.add( mod.build() );
            }
        }
    }

    /**
     * Extract blank nodes from a chunk.
     * 
     * @param chunk
     *            the chunk of quads.
     * @return the set of blank nodes in the chunk.
     */
    private static Set<Node> extractBlanks(Chunk<Quad> chunk) {
        final Set<Node> blanks = new HashSet<Node>();
        for (final Quad q : chunk.getLines()) {
            if (q.getSubject().isBlank()) {
                blanks.add( q.getSubject() );
            }
            if (q.getPredicate().isBlank()) {
                blanks.add( q.getPredicate() );
            }
            if (q.getObject().isBlank()) {
                blanks.add( q.getObject() );
            }
        }
        return blanks;

    }

    /**
     * Converts blank nodes into variables for delete operations.
     *
     */
    private static class DeleteBlankMapper implements Function<Triple, Triple> {
        /**
         * The list of all blanks in the delete operation.
         */
        protected List<Node> blanks;

        /**
         * Constructor.
         * 
         * @param blanks
         *            the list of blank nodes in the operation.
         */
        DeleteBlankMapper(List<Node> blanks) {
            this.blanks = blanks;
        }

        /**
         * Map the node to the variable.
         * 
         * Returns variables indexed to the blank node position in the list.
         * 
         * @param node
         *            the node to map.
         * @return if node is not blank return it otherwise return a variable.
         */
        Node map(Node node) {
            if (node.isBlank()) {
                return NodeFactory.createVariable( "v" + blanks.indexOf( node ) );
            }
            return node;
        }

        @Override
        public Triple apply(Triple t) {
            return new Triple( map( t.getSubject() ), map( t.getPredicate() ), map( t.getObject() ) );
        }
    }

    /**
     * Extract a list of lists of quads where each inner list is a separate
     * graph.
     * 
     * @param chunk
     *            the chunk to split by graph.
     * @return the list of lists of quads.
     */
    private static List<List<Quad>> byGraph(Chunk<Quad> chunk) {
        /* chunk lists are always in order */
        if (chunk.size() == 0) {
            return Collections.emptyList();
        }
        final List<List<Quad>> lst = new ArrayList<List<Quad>>();
        final int limit = chunk.size() - 1;
        int i = 0;
        final List<Quad> lines = chunk.getLines();

        Node g = lines.get( i ).getGraph();
        if (g.equals( lines.get( limit ).getGraph() )) {
            lst.add( lines.subList( i, limit + 1 ) );
            i = limit + 1;
        }
        for (int j = i + 1; j <= limit; j++) {
            if (!g.equals( lines.get( j ).getGraph() )) {
                lst.add( lines.subList( i, j ) );
                g = lines.get( j ).getGraph();
                i = j;
            }
        }
        if (i < limit) {
            lst.add( lines.subList( i, limit + 1 ) );
        }
        return lst;
    }

}
