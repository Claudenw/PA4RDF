/*
 * Copyright 2012, XENEI.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xenei.jena.entities;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.xenei.jena.entities.testing.abst.CollectionValueAnnoatedAbst;
import org.xenei.jena.entities.testing.iface.TestInterface;

public class CollectionValueEntityTests {

    // BOOLEAN, CHAR, DOUBLE, FLOAT, LONG, INTEGER, STRING, RDFNODE, ENTITY,
    // URI, VOID

    private CollectionValueAnnoatedAbst tc;
    private Model m;
    private final EntityManager manager = EntityManagerFactory.getEntityManager();

    @BeforeEach
    public void setup() throws MissingAnnotation {
        m = ModelFactory.createDefaultModel();
        final Resource r = m.createResource( "http://localhost/CollectionValueEntityTests" );
        tc = manager.read( r, CollectionValueAnnoatedAbst.class );
    }

    @AfterEach
    public void teardown() {
        m.close();
    }

    @Test
    public void testBoolean() {
        tc.addBool( true );
        Assertions.assertTrue( tc.hasBool( true ) );
        Assertions.assertTrue( !tc.hasBool( false ) );
        Assertions.assertEquals( 1, tc.getBool().size() );

        tc.addBool( false );
        Assertions.assertTrue( tc.hasBool( true ) );
        Assertions.assertTrue( tc.hasBool( false ) );
        Assertions.assertEquals( 2, tc.getBool().size() );

        tc.addBool( false );
        Assertions.assertEquals( 2, tc.getBool().size() );

        tc.removeBool( false );
        Assertions.assertTrue( tc.hasBool( true ) );
        Assertions.assertTrue( !tc.hasBool( false ) );
        Assertions.assertEquals( 1, tc.getBool().size() );

    }

    @Test
    public void testChar() {
        final char c = 'a';
        final Character cc = Character.valueOf( c );
        final char d = 'b';
        final Character dd = Character.valueOf( d );

        tc.addChar( c );
        Assertions.assertTrue( tc.hasChar( c ) );
        Assertions.assertTrue( tc.hasChar( cc ) );
        Assertions.assertTrue( !tc.hasChar( d ) );
        Assertions.assertTrue( !tc.hasChar( dd ) );
        Assertions.assertEquals( 1, tc.getChar().size() );

        tc.addChar( dd );
        Assertions.assertTrue( tc.hasChar( c ) );
        Assertions.assertTrue( tc.hasChar( cc ) );
        Assertions.assertTrue( tc.hasChar( d ) );
        Assertions.assertTrue( tc.hasChar( dd ) );
        Assertions.assertEquals( 2, tc.getChar().size() );

        tc.removeChar( cc );
        Assertions.assertTrue( !tc.hasChar( c ) );
        Assertions.assertTrue( !tc.hasChar( cc ) );
        Assertions.assertTrue( tc.hasChar( d ) );
        Assertions.assertTrue( tc.hasChar( dd ) );
        Assertions.assertEquals( 1, tc.getChar().size() );

        tc.removeChar( d );
        Assertions.assertTrue( !tc.hasChar( c ) );
        Assertions.assertTrue( !tc.hasChar( cc ) );
        Assertions.assertTrue( !tc.hasChar( d ) );
        Assertions.assertTrue( !tc.hasChar( dd ) );
        Assertions.assertEquals( 0, tc.getChar().size() );

    }

    @Test
    public void testDbl() {
        final double c = 3.14;
        final Double cc = Double.valueOf( c );

        final double d = 2.157;
        final Double dd = Double.valueOf( d );

        tc.addDbl( c );
        Assertions.assertTrue( tc.hasDbl( c ) );
        Assertions.assertTrue( tc.hasDbl( cc ) );
        Assertions.assertTrue( !tc.hasDbl( d ) );
        Assertions.assertTrue( !tc.hasDbl( dd ) );
        Assertions.assertEquals( 1, tc.getDbl().size() );

        tc.addDbl( dd );
        Assertions.assertTrue( tc.hasDbl( c ) );
        Assertions.assertTrue( tc.hasDbl( cc ) );
        Assertions.assertTrue( tc.hasDbl( d ) );
        Assertions.assertTrue( tc.hasDbl( dd ) );
        Assertions.assertEquals( 2, tc.getDbl().size() );

        tc.removeDbl( cc );
        Assertions.assertTrue( !tc.hasDbl( c ) );
        Assertions.assertTrue( !tc.hasDbl( cc ) );
        Assertions.assertTrue( tc.hasDbl( d ) );
        Assertions.assertTrue( tc.hasDbl( dd ) );
        Assertions.assertEquals( 1, tc.getDbl().size() );

        tc.removeDbl( d );
        Assertions.assertTrue( !tc.hasDbl( c ) );
        Assertions.assertTrue( !tc.hasDbl( cc ) );
        Assertions.assertTrue( !tc.hasDbl( d ) );
        Assertions.assertTrue( !tc.hasDbl( dd ) );
        Assertions.assertEquals( 0, tc.getDbl().size() );

    }

    @Test
    public void testEntity() throws MissingAnnotation {
        Resource r = m.createResource( "cc" );
        final TestInterface cc = manager.read( r, TestInterface.class );
        r = m.createResource( "dd" );
        final TestInterface dd = manager.read( r, TestInterface.class );

        tc.addEnt( cc );
        Assertions.assertTrue( tc.hasEnt( cc ) );
        Assertions.assertTrue( !tc.hasEnt( dd ) );
        Assertions.assertEquals( 1, tc.getEnt().size() );

        tc.addEnt( dd );
        Assertions.assertTrue( tc.hasEnt( cc ) );
        Assertions.assertTrue( tc.hasEnt( dd ) );
        Assertions.assertEquals( 2, tc.getEnt().size() );

        tc.removeEnt( cc );
        Assertions.assertTrue( !tc.hasEnt( cc ) );
        Assertions.assertTrue( tc.hasEnt( dd ) );
        Assertions.assertEquals( 1, tc.getEnt().size() );

        tc.removeEnt( dd );
        Assertions.assertTrue( !tc.hasEnt( cc ) );
        Assertions.assertTrue( !tc.hasEnt( dd ) );
        Assertions.assertEquals( 0, tc.getEnt().size() );

    }

    @Test
    public void testFlt() {
        final float c = 3.14F;
        final Float cc = Float.valueOf( c );

        final float d = 2.157F;
        final Float dd = Float.valueOf( d );

        tc.addFlt( c );
        Assertions.assertTrue( tc.hasFlt( c ) );
        Assertions.assertTrue( tc.hasFlt( cc ) );
        Assertions.assertTrue( !tc.hasFlt( d ) );
        Assertions.assertTrue( !tc.hasFlt( dd ) );
        Assertions.assertEquals( 1, tc.getFlt().size() );

        tc.addFlt( dd );
        Assertions.assertTrue( tc.hasFlt( c ) );
        Assertions.assertTrue( tc.hasFlt( cc ) );
        Assertions.assertTrue( tc.hasFlt( d ) );
        Assertions.assertTrue( tc.hasFlt( dd ) );
        Assertions.assertEquals( 2, tc.getFlt().size() );

        tc.removeFlt( cc );
        Assertions.assertTrue( !tc.hasFlt( c ) );
        Assertions.assertTrue( !tc.hasFlt( cc ) );
        Assertions.assertTrue( tc.hasFlt( d ) );
        Assertions.assertTrue( tc.hasFlt( dd ) );
        Assertions.assertEquals( 1, tc.getFlt().size() );

        tc.removeFlt( d );
        Assertions.assertTrue( !tc.hasFlt( c ) );
        Assertions.assertTrue( !tc.hasFlt( cc ) );
        Assertions.assertTrue( !tc.hasFlt( d ) );
        Assertions.assertTrue( !tc.hasFlt( dd ) );
        Assertions.assertEquals( 0, tc.getFlt().size() );
    }

    @Test
    public void testInt() {
        final int c = 3;
        final Integer cc = Integer.valueOf( c );

        final int d = 42;
        final Integer dd = Integer.valueOf( d );

        tc.addInt( c );
        Assertions.assertTrue( tc.hasInt( c ) );
        Assertions.assertTrue( tc.hasInt( cc ) );
        Assertions.assertTrue( !tc.hasInt( d ) );
        Assertions.assertTrue( !tc.hasInt( dd ) );
        Assertions.assertEquals( 1, tc.getInt().size() );

        tc.addInt( dd );
        Assertions.assertTrue( tc.hasInt( c ) );
        Assertions.assertTrue( tc.hasInt( cc ) );
        Assertions.assertTrue( tc.hasInt( d ) );
        Assertions.assertTrue( tc.hasInt( dd ) );
        Assertions.assertEquals( 2, tc.getInt().size() );

        tc.removeInt( cc );
        Assertions.assertTrue( !tc.hasInt( c ) );
        Assertions.assertTrue( !tc.hasInt( cc ) );
        Assertions.assertTrue( tc.hasInt( d ) );
        Assertions.assertTrue( tc.hasInt( dd ) );
        Assertions.assertEquals( 1, tc.getInt().size() );

        tc.removeInt( d );
        Assertions.assertTrue( !tc.hasInt( c ) );
        Assertions.assertTrue( !tc.hasInt( cc ) );
        Assertions.assertTrue( !tc.hasInt( d ) );
        Assertions.assertTrue( !tc.hasInt( dd ) );
        Assertions.assertEquals( 0, tc.getInt().size() );
    }

    @Test
    public void testLng() {
        final long c = 3;
        final Long cc = Long.valueOf( c );

        final long d = 42;
        final Long dd = Long.valueOf( d );

        tc.addLng( c );
        Assertions.assertTrue( tc.hasLng( c ) );
        Assertions.assertTrue( tc.hasLng( cc ) );
        Assertions.assertTrue( !tc.hasLng( d ) );
        Assertions.assertTrue( !tc.hasLng( dd ) );
        Assertions.assertEquals( 1, tc.getLng().size() );

        tc.addLng( dd );
        Assertions.assertTrue( tc.hasLng( c ) );
        Assertions.assertTrue( tc.hasLng( cc ) );
        Assertions.assertTrue( tc.hasLng( d ) );
        Assertions.assertTrue( tc.hasLng( dd ) );
        Assertions.assertEquals( 2, tc.getLng().size() );

        tc.removeLng( cc );
        Assertions.assertTrue( !tc.hasLng( c ) );
        Assertions.assertTrue( !tc.hasLng( cc ) );
        Assertions.assertTrue( tc.hasLng( d ) );
        Assertions.assertTrue( tc.hasLng( dd ) );
        Assertions.assertEquals( 1, tc.getLng().size() );

        tc.removeLng( d );
        Assertions.assertTrue( !tc.hasLng( c ) );
        Assertions.assertTrue( !tc.hasLng( cc ) );
        Assertions.assertTrue( !tc.hasLng( d ) );
        Assertions.assertTrue( !tc.hasLng( dd ) );
        Assertions.assertEquals( 0, tc.getLng().size() );

    }

    @Test
    public void testRdf() {
        final RDFNode cc = ResourceFactory.createResource( "cc" );

        final RDFNode dd = ResourceFactory.createResource( "dd" );
        ;

        tc.addRDF( cc );
        Assertions.assertTrue( tc.hasRDF( cc ) );
        Assertions.assertTrue( !tc.hasRDF( dd ) );
        Assertions.assertEquals( 1, tc.getRDF().size() );

        tc.addRDF( dd );
        Assertions.assertTrue( tc.hasRDF( cc ) );
        Assertions.assertTrue( tc.hasRDF( dd ) );
        Assertions.assertEquals( 2, tc.getRDF().size() );

        tc.removeRDF( cc );
        Assertions.assertTrue( !tc.hasRDF( cc ) );
        Assertions.assertTrue( tc.hasRDF( dd ) );
        Assertions.assertEquals( 1, tc.getRDF().size() );

        tc.removeRDF( dd );
        Assertions.assertTrue( !tc.hasRDF( cc ) );
        Assertions.assertTrue( !tc.hasRDF( dd ) );
        Assertions.assertEquals( 0, tc.getRDF().size() );
    }

    @Test
    public void testStr() {
        final String cc = "c";

        final String dd = "d";

        tc.addStr( cc );
        Assertions.assertTrue( tc.hasStr( cc ) );
        Assertions.assertTrue( !tc.hasStr( dd ) );
        Assertions.assertEquals( 1, tc.getStr().size() );

        tc.addStr( dd );
        Assertions.assertTrue( tc.hasStr( cc ) );
        Assertions.assertTrue( tc.hasStr( dd ) );
        Assertions.assertEquals( 2, tc.getStr().size() );

        tc.removeStr( cc );
        Assertions.assertTrue( !tc.hasStr( cc ) );
        Assertions.assertTrue( tc.hasStr( dd ) );
        Assertions.assertEquals( 1, tc.getStr().size() );

        tc.removeStr( dd );
        Assertions.assertTrue( !tc.hasStr( cc ) );
        Assertions.assertTrue( !tc.hasStr( dd ) );
        Assertions.assertEquals( 0, tc.getStr().size() );
    }

    @Test
    public void testURI() {

        final Resource cc = ResourceFactory.createResource( "cc" );
        final Resource dd = ResourceFactory.createResource( "dd" );

        tc.addU( "cc" );
        Assertions.assertTrue( tc.hasU( cc ) );
        Assertions.assertTrue( tc.hasU( "cc" ) );
        Assertions.assertTrue( !tc.hasU( dd ) );
        Assertions.assertTrue( !tc.hasU( "dd" ) );
        Assertions.assertEquals( 1, tc.getU().size() );
        Assertions.assertEquals( 1, tc.getU2().size() );

        tc.addU( "dd" );
        Assertions.assertTrue( tc.hasU( cc ) );
        Assertions.assertTrue( tc.hasU( "cc" ) );
        Assertions.assertTrue( tc.hasU( dd ) );
        Assertions.assertTrue( tc.hasU( "dd" ) );
        Assertions.assertEquals( 2, tc.getU().size() );
        Assertions.assertEquals( 2, tc.getU2().size() );

        tc.removeU( cc );
        Assertions.assertTrue( !tc.hasU( cc ) );
        Assertions.assertTrue( !tc.hasU( "cc" ) );
        Assertions.assertTrue( tc.hasU( dd ) );
        Assertions.assertTrue( tc.hasU( "dd" ) );
        Assertions.assertEquals( 1, tc.getU().size() );
        Assertions.assertEquals( 1, tc.getU2().size() );

        tc.removeU( "dd" );
        Assertions.assertTrue( !tc.hasU( cc ) );
        Assertions.assertTrue( !tc.hasU( "cc" ) );
        Assertions.assertTrue( !tc.hasU( dd ) );
        Assertions.assertTrue( !tc.hasU( "dd" ) );
        Assertions.assertEquals( 0, tc.getU().size() );
        Assertions.assertEquals( 0, tc.getU2().size() );

    }
}
