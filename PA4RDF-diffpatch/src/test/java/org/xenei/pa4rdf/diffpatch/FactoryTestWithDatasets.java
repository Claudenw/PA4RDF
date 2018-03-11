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

import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.test.helpers.ModelHelper;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.update.UpdateRequest;
import org.junit.Test;

import difflib.Patch;

public class FactoryTestWithDatasets {

    protected void testPatch(Dataset orig, Dataset revised) {
        final RDFConnection origC = RDFConnectionFactory.connect( orig );
        final RDFConnection revC = RDFConnectionFactory.connect( revised );

        final Patch<Quad> patch = PatchFactory.patch( origC, revC );
        final UpdateRequest req = UpdateFactory.asUpdate( patch );

        // System.out.println( req );

        origC.update( req );

        final Iterator<String> names = orig.listNames();
        while (names.hasNext()) {
            final String uri = names.next();

            // System.out.println( "\n\nREVISED "+uri+"\n\n");
            // revised.getNamedModel(uri).write( System.out, "TURTLE");
            // System.out.println( "\n\nORIG "+uri+"\n\n");
            // orig.getNamedModel(uri).write( System.out, "TURTLE");

            assertTrue( uri + " not isomorphic",
                    revised.getNamedModel( uri ).isIsomorphicWith( orig.getNamedModel( uri ) ) );
        }
        revised.getDefaultModel();
        assertTrue( "default not isomporphi", revised.getDefaultModel().isIsomorphicWith( orig.getDefaultModel() ) );
    }

    @Test
    public void testSimplePatch() {
        final String initModelA1 = "x P a; x P b; x R c";
        final String initModelA2 = "x P a; x P b; x R c";

        final String initModelB1 = "x P a; x P b; x R C";
        final String initModelB2 = "x P a; x P b; x R c";

        final Model A1 = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelA1 );
        final Model A2 = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelA2 );
        final Model B1 = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelB1 );
        final Model B2 = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelB2 );

        final Dataset orig = DatasetFactory.create();
        orig.addNamedModel( "Graph1", A1 );
        orig.addNamedModel( "Graph2", A2 );

        final Dataset revised = DatasetFactory.create();
        revised.addNamedModel( "Graph1", B1 );
        revised.addNamedModel( "Graph2", B2 );

        testPatch( orig, revised );

        assertTrue( orig.getNamedModel( "Graph1" ).contains( ModelHelper.statement( "x R C" ) ) );
        assertTrue( orig.getNamedModel( "Graph2" ).contains( ModelHelper.statement( "x R c" ) ) );
    }

    /**
     * Test that a simple blank node change works
     */
    @Test
    public void testSimplePatchWithBlankNode() {
        final String initModelA1 = "s p _:a ; _:a p o; _:a p2 o2 ";
        final String initModelA2 = "s p _:a ; _:a p o; _:a p2 o2 ";

        final String initModelB1 = "s p _:b ; _:b p o; _:b p2 o3 ";
        final String initModelB2 = "s p _:a ; _:a p o; _:a p2 o2 ";

        final Model A1 = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelA1 );
        final Model A2 = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelA2 );
        final Model B1 = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelB1 );
        final Model B2 = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelB2 );

        final Dataset orig = DatasetFactory.create();
        orig.addNamedModel( "Graph1", A1 );
        orig.addNamedModel( "Graph2", A2 );

        final Dataset revised = DatasetFactory.create();
        revised.addNamedModel( "Graph1", B1 );
        revised.addNamedModel( "Graph2", B2 );

        testPatch( orig, revised );

    }

    @Test
    public void testPatchWithBlankNodeAndExtra() {

        final String initModelA1 = "s p o4;s p _:a ; _:a p o; _:a p2 o2 ";
        final String initModelA2 = "s p o4;s p _:a ; _:a p o; _:a p2 o2 ";

        final String initModelB1 = "s2 p o4;s p _:b ; _:b p o; _:b p2 o3 ";
        final String initModelB2 = "s p o4;s p _:b ; _:b p o; _:b p2 o2 ";

        final Model A1 = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelA1 );
        final Model A2 = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelA2 );
        final Model B1 = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelB1 );
        final Model B2 = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelB2 );

        final Dataset orig = DatasetFactory.create();
        orig.addNamedModel( "Graph1", A1 );
        orig.addNamedModel( "Graph2", A2 );

        final Dataset revised = DatasetFactory.create();
        revised.addNamedModel( "Graph1", B1 );
        revised.addNamedModel( "Graph2", B2 );

        testPatch( orig, revised );

    }
}
