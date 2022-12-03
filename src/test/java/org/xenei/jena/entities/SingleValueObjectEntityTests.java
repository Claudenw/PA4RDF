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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.testing.iface.SingleValueObjectInterface.SubPredicate;
import org.xenei.jena.entities.testing.iface.SingleValueObjectInterfaceDefault;
import org.xenei.jena.entities.testing.iface.TestInterface;

public class SingleValueObjectEntityTests {

    private SingleValueObjectInterfaceDefault tc;
    private Model model;
    private EntityManager manager;

    @BeforeEach
    public void setup() throws Exception {
        manager = EntityManagerFactory.getEntityManager();
        model = ModelFactory.createDefaultModel();
        final Resource r = model.createResource( "http://localhost/SingleValueObjectEntityTests" );
        tc = manager.read( r, SingleValueObjectInterfaceDefault.class );
    }

    @AfterEach
    public void teardown() {
        model.close();
    }

    @Test
    public void testBoolean() {
        tc.setBool( true );
        Assertions.assertTrue( tc.isBool() );
        tc.setBool( false );
        Assertions.assertTrue( !tc.isBool() );
        tc.setBool( Boolean.TRUE );
        Assertions.assertTrue( tc.isBool() );
        tc.setBool( Boolean.FALSE );
        Assertions.assertTrue( !tc.isBool() );
        tc.removeBool();
        Assertions.assertNull( tc.isBool() );
    }

    @Test
    public void testChar() {
        final char c = 'a';
        final Character cc = Character.valueOf( c );
        tc.setChar( c );
        Assertions.assertEquals( cc, tc.getChar() );
        tc.setChar( 'x' );
        Assertions.assertTrue( !cc.equals( tc.getChar() ) );
        tc.setChar( cc );
        Assertions.assertEquals( cc, tc.getChar() );
        tc.removeChar();
        Assertions.assertNull( tc.getChar() );
    }

    @Test
    public void testDbl() {
        final double c = 3.14;
        final Double cc = Double.valueOf( c );
        tc.setDbl( c );
        Assertions.assertEquals( cc, tc.getDbl() );
        tc.setDbl( 0.0 );
        Assertions.assertTrue( !cc.equals( tc.getDbl() ) );
        tc.setDbl( cc );
        Assertions.assertEquals( cc, tc.getDbl() );
        tc.removeDbl();
        Assertions.assertNull( tc.getDbl() );
    }

    @Test
    public void testEntity() throws Exception {
        Resource r = model.createResource( "testclass" );
        final TestInterface c = manager.read( r, TestInterface.class );
        tc.setEnt( c );
        Assertions.assertEquals( c, tc.getEnt() );
        r = model.createResource( "testclass2" );
        final TestInterface cc = manager.read( r, TestInterface.class );
        tc.setEnt( cc );
        Assertions.assertTrue( !c.equals( tc.getEnt() ) );
        tc.removeEnt();
        Assertions.assertNull( tc.getEnt() );

    }

    @Test
    public void testFlt() {
        final float c = 3.14F;
        final Float cc = Float.valueOf( c );
        tc.setFlt( c );
        Assertions.assertEquals( cc, tc.getFlt() );
        tc.setFlt( 0.0F );
        Assertions.assertTrue( !cc.equals( tc.getFlt() ) );
        tc.setFlt( cc );
        Assertions.assertEquals( cc, tc.getFlt() );
        tc.removeFlt();
        Assertions.assertNull( tc.getFlt() );
    }

    @Test
    public void testInt() {
        final int c = 3;
        final Integer cc = Integer.valueOf( c );
        tc.setInt( c );
        Assertions.assertEquals( cc, tc.getInt() );
        tc.setInt( 0 );
        Assertions.assertTrue( !cc.equals( tc.getInt() ) );
        tc.setInt( cc );
        Assertions.assertEquals( cc, tc.getInt() );
        tc.removeInt();
        Assertions.assertNull( tc.getInt() );
    }

    @Test
    public void testLng() {
        final long c = 3;
        final Long cc = Long.valueOf( c );
        tc.setLng( c );
        Assertions.assertEquals( cc, tc.getLng() );
        tc.setLng( 0L );
        Assertions.assertTrue( !cc.equals( tc.getLng() ) );
        tc.setLng( cc );
        Assertions.assertEquals( cc, tc.getLng() );
        tc.removeLng();
        Assertions.assertNull( tc.getLng() );
    }

    @Test
    public void testRdf() {
        final RDFNode n = model.createResource( "rdfNode" );
        tc.setRDF( n );
        Assertions.assertEquals( n, tc.getRDF() );
        tc.setRDF( model.createResource( "anotherRdfNode" ) );
        Assertions.assertTrue( !n.equals( tc.getStr() ) );
        tc.removeRDF();
        Assertions.assertNull( tc.getRDF() );

    }

    @Test
    public void testStr() {
        final String cc = "string";
        tc.setStr( "string" );
        Assertions.assertEquals( cc, tc.getStr() );
        tc.setStr( "foo" );
        Assertions.assertTrue( !cc.equals( tc.getStr() ) );
        tc.setStr( cc );
        Assertions.assertEquals( cc, tc.getStr() );
        tc.removeStr();
        Assertions.assertNull( tc.getStr() );
    }

    @Test
    public void testSubPredicate() throws Exception {
        Resource r = model.createResource( "http://localhost/SubPredicateTest1" );
        final SubPredicate sp = manager.read( r, SubPredicate.class );
        sp.setName( "spTest" );

        r = model.createResource( "http://localhost/SubPredicateTest2" );
        final SubPredicate sp2 = manager.read( r, SubPredicate.class );
        sp2.setName( "spTest2" );

        tc.setSubPredicate( sp );
        Assertions.assertNotNull( tc.getSubPredicate() );
        Assertions.assertEquals( sp.getName(), tc.getSubPredicate().getName() );

        tc.setSubPredicate( sp2 );
        Assertions.assertNotNull( tc.getSubPredicate() );
        Assertions.assertEquals( sp2.getName(), tc.getSubPredicate().getName() );

        tc.removeSubPredicate();
        Assertions.assertNull( tc.getSubPredicate() );

    }

    @Test
    public void testURI() {
        final Resource r = ResourceFactory.createResource( "uriTest" );
        tc.setU( r );
        Assertions.assertEquals( r, tc.getU() );
        Assertions.assertEquals( "uriTest", tc.getU2() );
        tc.setU( "uriTest2" );
        Assertions.assertTrue( !r.equals( tc.getU() ) );
        tc.removeU();
        Assertions.assertNull( tc.getU() );
        Assertions.assertNull( tc.getU2() );
    }
}
