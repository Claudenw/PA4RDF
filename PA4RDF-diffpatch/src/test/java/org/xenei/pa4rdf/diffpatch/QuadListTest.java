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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.test.helpers.ModelHelper;
import org.apache.jena.sparql.core.Quad;
import org.junit.Test;

public class QuadListTest {
    final Model orig = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), "x P a; x P b; x R c" );
    final Model revised = ModelHelper.modelAdd( ModelFactory.createDefaultModel(), "x P a; x P b; x R C" );

    @Test
    public void standard() {
        final Node g = NodeFactory.createURI( "g" );
        final Node s = NodeFactory.createURI( "s" );
        final Node p = NodeFactory.createURI( "p" );
        final Node o = NodeFactory.createURI( "o" );
        final Node o2 = NodeFactory.createURI( "o2" );
        final Node o3 = NodeFactory.createURI( "o3" );

        final Quad q1 = new Quad( g, s, p, o );
        final QuadList ql = new QuadList();
        ql.add( q1 );
        final Quad q2 = new Quad( g, s, p, o2 );
        ql.add( q2 );

        assertEquals( 2, ql.size() );
        assertTrue( ql.contains( q1 ) );
        assertTrue( ql.contains( q2 ) );

        final List<Quad> lst = new ArrayList<Quad>();
        lst.add( q1 );
        lst.add( q2 );
        assertTrue( ql.containsAll( lst ) );

        lst.add( new Quad( g, s, p, o3 ) );
        assertFalse( ql.containsAll( lst ) );

        assertEquals( q1, ql.get( 0 ) );
        assertEquals( q2, ql.get( 1 ) );

    }

    @Test
    public void subList() {
        final Node g = NodeFactory.createURI( "g" );
        final Node g2 = NodeFactory.createURI( "g2" );
        final Node g3 = NodeFactory.createURI( "g3" );
        final Node s = NodeFactory.createURI( "s" );
        final Node p = NodeFactory.createURI( "p" );
        final Node o = NodeFactory.createURI( "o" );
        final Node o2 = NodeFactory.createURI( "o2" );
        NodeFactory.createURI( "o3" );

        final QuadList ql = new QuadList();
        List<Quad> qlst = new ArrayList<Quad>();
        qlst.add( new Quad( g, s, p, o ) );
        qlst.add( new Quad( g, s, p, o2 ) );
        ql.addAll( qlst );

        qlst = new ArrayList<Quad>();
        qlst.add( new Quad( g2, s, p, o ) );
        qlst.add( new Quad( g2, s, p, o2 ) );
        ql.addAll( qlst );

        qlst = new ArrayList<Quad>();
        qlst.add( new Quad( g3, s, p, o ) );
        qlst.add( new Quad( g3, s, p, o2 ) );
        ql.addAll( qlst );

        qlst = new ArrayList<Quad>();
        qlst.add( new Quad( g, s, p, o2 ) );
        qlst.add( new Quad( g2, s, p, o ) );
        qlst.add( new Quad( g2, s, p, o2 ) );
        qlst.add( new Quad( g3, s, p, o ) );

        final List<Quad> subList = ql.subList( 1, 5 );

        assertEquals( 4, subList.size() );
        assertTrue( subList.containsAll( qlst ) );

        assertFalse( subList.contains( new Quad( g, s, p, o ) ) );
    }

}
