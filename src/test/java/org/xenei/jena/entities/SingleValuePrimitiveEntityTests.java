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
import org.xenei.jena.entities.testing.iface.SingleValuePrimitiveInterface;

public class SingleValuePrimitiveEntityTests {

    private SingleValuePrimitiveInterface tc;
    private Model model;

    @BeforeEach
    public void setup() throws Exception {
        model = ModelFactory.createDefaultModel();
        final Resource r = model.createResource( "http://localhost/SingleValuePrimitiveEntityTests" );
        final EntityManager manager = EntityManagerFactory.getEntityManager();
        tc = manager.read( r, SingleValuePrimitiveInterface.class );
    }

    @AfterEach
    public void teardown() {
        model.close();
    }

    @Test
    public void testBoolean() {
        Assertions.assertFalse( tc.isBool() );
        tc.setBool( true );
        Assertions.assertTrue( tc.isBool() );
        tc.setBool( false );
        Assertions.assertFalse( tc.isBool() );
        tc.setBool( Boolean.TRUE );
        Assertions.assertTrue( tc.isBool() );
        tc.setBool( Boolean.FALSE );
        Assertions.assertFalse( tc.isBool() );
        tc.removeBool();
        Assertions.assertFalse( tc.isBool() );
    }

    @Test
    public void testChar() {
        final char c = 'a';
        final Character cc = Character.valueOf( c );
        tc.setChar( c );
        Assertions.assertEquals( cc, Character.valueOf( tc.getChar() ) );
        tc.setChar( 'x' );
        Assertions.assertTrue( !cc.equals( tc.getChar() ) );
        tc.setChar( cc );
        Assertions.assertEquals( cc, Character.valueOf( tc.getChar() ) );
        tc.removeChar();
        try {
            tc.getChar();
            Assertions.fail( "Should have thrown NullPointerException" );
        } catch (final NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testDbl() {
        final double c = 3.14;
        final Double cc = Double.valueOf( c );
        tc.setDbl( c );
        Assertions.assertEquals( cc, Double.valueOf( tc.getDbl() ) );
        tc.setDbl( 0.0 );
        Assertions.assertTrue( !cc.equals( tc.getDbl() ) );
        tc.setDbl( cc );
        Assertions.assertEquals( cc, Double.valueOf( tc.getDbl() ) );
        tc.removeDbl();
        try {
            tc.getDbl();
            Assertions.fail( "Should have thrown NullPointerException" );
        } catch (final NullPointerException e) {
            // expected
        }
        ;
    }

    @Test
    public void testFlt() {
        final float c = 3.14F;
        final Float cc = Float.valueOf( c );
        tc.setFlt( c );
        Assertions.assertEquals( cc, Float.valueOf( tc.getFlt() ) );
        tc.setFlt( 0.0F );
        Assertions.assertTrue( !cc.equals( tc.getFlt() ) );
        tc.setFlt( cc );
        Assertions.assertEquals( cc, Float.valueOf( tc.getFlt() ) );
        tc.removeFlt();
        try {
            tc.getFlt();
            Assertions.fail( "Should have thrown NullPointerException" );
        } catch (final NullPointerException e) {
            // expected
        }
        ;
    }

    @Test
    public void testInt() {
        final int c = 3;
        final Integer cc = Integer.valueOf( c );
        tc.setInt( c );
        Assertions.assertEquals( cc, Integer.valueOf( tc.getInt() ) );
        tc.setInt( 0 );
        Assertions.assertTrue( !cc.equals( tc.getInt() ) );
        tc.setInt( cc );
        Assertions.assertEquals( cc, Integer.valueOf( tc.getInt() ) );
        tc.removeInt();
        try {
            tc.getInt();
            Assertions.fail( "Should have thrown NullPointerException" );
        } catch (final NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testLng() {
        final long c = 3;
        final Long cc = Long.valueOf( c );
        tc.setLng( c );
        Assertions.assertEquals( cc, Long.valueOf( tc.getLng() ) );
        tc.setLng( 0L );
        Assertions.assertTrue( !cc.equals( tc.getLng() ) );
        tc.setLng( cc );
        Assertions.assertEquals( cc, Long.valueOf( tc.getLng() ) );
        tc.removeLng();
        try {
            tc.getLng();
            Assertions.fail( "Should have thrown NullPointerException" );
        } catch (final NullPointerException e) {
            // expected
        }
        ;
    }

}
