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
import org.apache.jena.rdf.model.Resource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.testing.abst.SingleValueMixedTypeTestClass;

public class SingleValueMixedObjectEntityTests {

    private SingleValueMixedTypeTestClass tc;
    private Model model;

    @BeforeEach
    public void setup() throws Exception {
        final EntityManager manager = EntityManagerFactory.getEntityManager();
        model = ModelFactory.createDefaultModel();
        final Resource r = model.createResource( "http://localhost/SingleValueMixedTypeEntityTests" );
        tc = manager.read( r, SingleValueMixedTypeTestClass.class );
    }

    @AfterEach
    public void teardown() {
        model.close();
    }

    @Test
    public void testB() {
        tc.setB( true );
        Assertions.assertTrue( tc.isB() );
        tc.setB( false );
        Assertions.assertTrue( !tc.isB() );
        tc.setB( Boolean.TRUE );
        Assertions.assertTrue( tc.isB() );
        tc.setB( Boolean.FALSE );
        Assertions.assertTrue( !tc.isB() );

    }

    @Test
    public void testBoolean() {
        Assertions.assertNull( tc.isBool() );
        tc.setBool( true );
        Assertions.assertTrue( tc.isBool() );
        tc.setBool( false );
        Assertions.assertTrue( !tc.isBool() );
        tc.setBool( Boolean.TRUE );
        Assertions.assertTrue( tc.isBool() );
        tc.setBool( Boolean.FALSE );
        Assertions.assertTrue( !tc.isBool() );

    }

    @Test
    public void testC() {
        final char c = 'a';
        final Character cc = Character.valueOf( c );
        tc.setC( c );
        Assertions.assertEquals( cc, Character.valueOf( tc.getC() ) );
        tc.setC( 'x' );
        Assertions.assertTrue( !cc.equals( tc.getC() ) );
        tc.setC( cc );
        Assertions.assertEquals( cc, Character.valueOf( tc.getC() ) );

    }

    @Test
    public void testChar() {
        final char c = 'a';
        final Character cc = Character.valueOf( c );
        Assertions.assertNull( tc.getChar() );
        tc.setChar( c );
        Assertions.assertEquals( cc, Character.valueOf( tc.getChar() ) );
        tc.setChar( 'x' );
        Assertions.assertTrue( !cc.equals( tc.getChar() ) );
        tc.setChar( cc );
        Assertions.assertEquals( cc, Character.valueOf( tc.getChar() ) );
    }

    @Test
    public void testD() {
        final double c = 3.14;
        final Double cc = Double.valueOf( c );
        tc.setD( c );
        Assertions.assertEquals( cc, Double.valueOf( tc.getD() ) );
        tc.setD( 0.0 );
        Assertions.assertTrue( !cc.equals( tc.getD() ) );
        tc.setD( cc );
        Assertions.assertEquals( cc, Double.valueOf( tc.getD() ) );
    }

    @Test
    public void testDbl() {
        final double c = 3.14;
        final Double cc = Double.valueOf( c );
        Assertions.assertNull( tc.getDbl() );
        tc.setDbl( c );
        Assertions.assertEquals( cc, Double.valueOf( tc.getDbl() ) );
        tc.setDbl( 0.0 );
        Assertions.assertTrue( !cc.equals( tc.getDbl() ) );
        tc.setDbl( cc );
        Assertions.assertEquals( cc, Double.valueOf( tc.getDbl() ) );
    }

    @Test
    public void testF() {
        final float c = 3.14F;
        final Float cc = Float.valueOf( c );
        tc.setF( c );
        Assertions.assertEquals( cc, Float.valueOf( tc.getF() ) );
        tc.setF( 0.0F );
        Assertions.assertTrue( !cc.equals( tc.getF() ) );
        tc.setF( cc );
        Assertions.assertEquals( cc, Float.valueOf( tc.getF() ) );
    }

    @Test
    public void testFlt() {
        final float c = 3.14F;
        final Float cc = Float.valueOf( c );
        Assertions.assertNull( tc.getFlt() );
        tc.setFlt( c );
        Assertions.assertEquals( cc, Float.valueOf( tc.getFlt() ) );
        tc.setFlt( 0.0F );
        Assertions.assertTrue( !cc.equals( tc.getFlt() ) );
        tc.setFlt( cc );
        Assertions.assertEquals( cc, Float.valueOf( tc.getFlt() ) );

    }

    @Test
    public void testI() {
        final int c = 3;
        final Integer cc = Integer.valueOf( c );
        tc.setI( c );
        Assertions.assertEquals( cc, Integer.valueOf( tc.getI() ) );
        tc.setI( 0 );
        Assertions.assertTrue( !cc.equals( tc.getI() ) );
        tc.setI( cc );
        Assertions.assertEquals( cc, Integer.valueOf( tc.getI() ) );
    }

    @Test
    public void testInt() {
        final int c = 3;
        final Integer cc = Integer.valueOf( c );
        Assertions.assertNull( tc.getInt() );
        tc.setInt( c );
        Assertions.assertEquals( cc, Integer.valueOf( tc.getInt() ) );
        tc.setInt( 0 );
        Assertions.assertTrue( !cc.equals( tc.getInt() ) );
        tc.setInt( cc );
        Assertions.assertEquals( cc, Integer.valueOf( tc.getInt() ) );
    }

    @Test
    public void testL() {
        final long c = 3;
        final Long cc = Long.valueOf( c );
        tc.setL( c );
        Assertions.assertEquals( cc, Long.valueOf( tc.getL() ) );
        tc.setL( 0L );
        Assertions.assertTrue( !cc.equals( tc.getL() ) );
        tc.setL( cc );
        Assertions.assertEquals( cc, Long.valueOf( tc.getL() ) );
    }

    @Test
    public void testLng() {
        final long c = 3;
        final Long cc = Long.valueOf( c );
        Assertions.assertNull( tc.getLng() );
        tc.setLng( c );
        Assertions.assertEquals( cc, Long.valueOf( tc.getLng() ) );
        tc.setLng( 0L );
        Assertions.assertTrue( !cc.equals( tc.getLng() ) );
        tc.setLng( cc );
        Assertions.assertEquals( cc, Long.valueOf( tc.getLng() ) );
    }

}
