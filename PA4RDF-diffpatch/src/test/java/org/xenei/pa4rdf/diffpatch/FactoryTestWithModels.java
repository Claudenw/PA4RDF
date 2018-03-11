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

public class FactoryTestWithModels {

    protected void testPatch(Model orig, Model revised) {
        final RDFConnection origC = RDFConnectionFactory.connect( DatasetFactory.create( orig ) );
        final RDFConnection revC = RDFConnectionFactory.connect( DatasetFactory.create( revised ) );

        final Patch<Quad> patch = PatchFactory.patch( origC, revC );
        final UpdateRequest req = UpdateFactory.asUpdate( patch );

        origC.update( req );

        assertTrue( revised.isIsomorphicWith( orig ) );
    }

    @Test
    public void testSimplePatch() {
        final String initModelA = "x P a; x P b; x R c";
        final String initModelB = "x P a; x P b; x R C";

        final Model orig = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelA );
        final Model revised = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelB );

        testPatch( orig, revised );
        assertTrue( orig.contains( ModelHelper.statement( "x R C" ) ) );
    }

    /**
     * Test that a simple blank node change works
     */
    @Test
    public void testSimplePatchWithBlankNode() {
        final String initModelA = "s p _:a ; _:a p o; _:a p2 o2 ";
        final String initModelB = "s p _:b ; _:b p o; _:b p2 o3 ";

        final Model orig = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelA );
        final Model revised = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelB );

        testPatch( orig, revised );

    }

    @Test
    public void testPatchWithBlankNodeAndExtra() {
        final String initModelA = "s p o4;s p _:a ; _:a p o; _:a p2 o2 ";
        final String initModelB = "s2 p o4;s p _:b ; _:b p o; _:b p2 o3 ";

        final Model orig = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelA );
        final Model revised = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), initModelB );

        testPatch( orig, revised );

    }
}
