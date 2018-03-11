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

import java.util.List;
import java.util.function.Function;

import org.apache.jena.arq.querybuilder.ExprFactory;
import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.graph.Graph;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.util.iterator.WrappedIterator;

import difflib.DiffUtils;
import difflib.Patch;
import difflib.myers.MyersDiff;

/**
 * Factory to create diff and patch files.
 *
 */
public class PatchFactory {
    private static final SelectBuilder PATCH_QUERY;
    private static final Function<QuerySolution, Quad> TO_QUAD_FUNC;
    private final static Var G = Var.alloc( "g" );
    private final static Var S = Var.alloc( "s" );
    private final static Var P = Var.alloc( "p" );
    private final static Var O = Var.alloc( "o" );

    static {
        final SelectBuilder inner = new SelectBuilder();
        final ExprFactory factory = inner.getExprFactory();
        inner.addVar( G ).addVar( S ).addVar( P ).addVar( O ).addWhere( S, P, O )
        .addBind( factory.asExpr( Quad.defaultGraphIRI ), G );
        PATCH_QUERY = new SelectBuilder().addVar( G ).addVar( S ).addVar( P ).addVar( O ).addGraph( G, S, P, O )
                .addUnion( inner ).addOrderBy( G ).addOrderBy( S ).addOrderBy( P ).addOrderBy( O );

        TO_QUAD_FUNC = new Function<QuerySolution, Quad>() {

            @Override
            public Quad apply(QuerySolution qs) {
                return new Quad( qs.get( G.getName() ).asNode(), qs.get( S.getName() ).asNode(),
                        qs.get( P.getName() ).asNode(), qs.get( O.getName() ).asNode() );
            }
        };
    }

    /**
     * Create a patch from RDFConnections.
     * 
     * @param orig
     *            the RDFConnection that contains the original data.
     * @param revised
     *            the RDFConnection that contains the revised data.
     * @return the Patch for the difference.
     */
    public static Patch<Quad> patch(RDFConnection orig, RDFConnection revised) {

        final List<Quad> origQ = createQuadList( orig );

        final List<Quad> revQ = createQuadList( revised );

        return DiffUtils.diff( origQ, revQ, new MyersDiff<Quad>() );
    }

    /**
     * Create a patch from Dataset.
     * 
     * @param orig
     *            the Dataset that contains the original data.
     * @param revised
     *            the Dataset that contains the revised data.
     * @return the Patch for the difference.
     */
    public static Patch<Quad> patch(Dataset orig, Dataset revised) {
        return patch( RDFConnectionFactory.connect( orig ), RDFConnectionFactory.connect( revised ) );
    }

    /**
     * Create a patch from Model.
     * 
     * @param orig
     *            the Model that contains the original data.
     * @param revised
     *            the Model that contains the revised data.
     * @return the Patch for the difference.
     */
    public static Patch<Quad> patch(Model orig, Model revised) {
        return patch( DatasetFactory.create( orig ), DatasetFactory.create( revised ) );
    }

    /**
     * Create a patch from Graph.
     * 
     * @param orig
     *            the Graph that contains the original data.
     * @param revised
     *            the Graph that contains the revised data.
     * @return the Patch for the difference.
     */
    public static Patch<Quad> patch(Graph orig, Graph revised) {
        return patch( ModelFactory.createModelForGraph( orig ), ModelFactory.createModelForGraph( revised ) );
    }

    /**
     * Generate a sorted list of Quads from an RDFConnection.
     * 
     * @param conn
     *            the connection to use.
     * @return the List of quads.
     */
    public static List<Quad> createQuadList(RDFConnection conn) {
        return WrappedIterator.create( conn.query( PATCH_QUERY.build() ).execSelect() ).mapWith( TO_QUAD_FUNC )
                .toList();
    }

    /**
     * Generate a sorted list of Quads from a Model.
     * 
     * @param model
     *            the model to use.
     * @return the List of quads.
     */
    public static List<Quad> createQuadList(Model model) {
        return createQuadList( RDFConnectionFactory.connect( DatasetFactory.create( model ) ) );
    }
}
