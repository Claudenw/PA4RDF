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

import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.testing.iface.CollectionValueInterface;
import org.xenei.jena.entities.testing.iface.SingleValuePrimitiveInterface;

public class CollectionValueEntityTests {

    private CollectionValueInterface tc;
    private Model model;

    @BeforeEach
    public void setup() throws Exception {
        model = ModelFactory.createDefaultModel();
        final Resource r = model.createResource( "http://localhost/CollectionValueEntityTests" );
        final EntityManager manager = EntityManagerFactory.getEntityManager();
        tc = manager.read( r, CollectionValueInterface.class );
    }

    @AfterEach
    public void teardown() {
        model.close();
    }

    @Test
    public void testBoolean() {
        Assertions.assertTrue( tc.getBool().isEmpty() );
        Assertions.assertTrue( tc.getBool() instanceof Set );
        tc.addBool( true );
        Assertions.assertEquals(1,  tc.getBool().size() );
        Assertions.assertTrue(tc.getBool().iterator().next());
        tc.addBool( false );
        Assertions.assertEquals(2,  tc.getBool().size() );
        Assertions.assertTrue(tc.getBool().contains(true));
        Assertions.assertTrue(tc.getBool().contains(false));
        
        tc.removeBool(Boolean.FALSE);
        Assertions.assertEquals(1,  tc.getBool().size() );
        Assertions.assertTrue(tc.getBool().iterator().next());
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

    /*
     * /*
 
public interface CollectionValueInterface {

    @Predicate
    void addBool(Boolean b);

    @Predicate
    void addChar(Character b);

    @Predicate
    void addDbl(Double b);

    @Predicate
    void addEnt(TestInterface b);

    @Predicate
    void addFlt(Float b);

    @Predicate
    void addInt(Integer b);

    @Predicate
    void addLng(Long b);

    @Predicate
    void addRDF(RDFNode b);

    @Predicate
    void addStr(String b);

    void addU(RDFNode b);

    @Predicate
    void addU(@URI String b);

    @Predicate
    void addU3(RDFNode b);

    void addU3(@URI String b);

    Set<Boolean> getBool();

    List<Character> getChar();

    Queue<Double> getDbl();

    Queue<TestInterface> getEnt();

    Set<Float> getFlt();

    Queue<Integer> getInt();

    List<Long> getLng();

    List<RDFNode> getRDF();

    Set<String> getStr();

    @Predicate(type = RDFNode.class)
    Set<RDFNode> getU();

    @Predicate(type = URI.class, name = "u")
    List<String> getU2();

    @Predicate(type = RDFNode.class)
    Queue<RDFNode> getU3();

    @Predicate(type = URI.class, name = "u3")
    Set<String> getU4();

    Boolean hasBool(Boolean b);

    Boolean hasChar(Character b);

    Boolean hasDbl(Double b);

    Boolean hasEnt(TestInterface b);

    Boolean hasFlt(Float b);

    Boolean hasInt(Integer b);

    Boolean hasLng(Long b);

    Boolean hasRDF(RDFNode b);

    Boolean hasStr(String b);

    Boolean hasU(RDFNode b);

    Boolean hasU(@URI String b);

    Boolean hasU3(RDFNode b);

    Boolean hasU3(@URI String b);

    void removeBool(Boolean b);

    void removeChar(Character b);

    void removeDbl(Double b);

    void removeEnt(TestInterface b);

    void removeFlt(Float b);

    void removeInt(Integer b);

    void removeLng(Long b);

    void removeRDF(RDFNode b);

    void removeStr(String b);

    void removeU(RDFNode b);

    void removeU(@URI String b);

    void removeU3(RDFNode b);

    void removeU3(@URI String b);

}

     */
}
