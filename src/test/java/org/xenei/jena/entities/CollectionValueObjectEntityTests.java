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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.testing.iface.CollectionValueInterface;
import org.xenei.jena.entities.testing.iface.TestInterface;


public class CollectionValueObjectEntityTests {
    
    private static final String NAMESPACE = "http://localhost/test#";

    private CollectionValueInterface underTest;
    private Model model;
    private EntityManager manager;

    @BeforeEach
    public void setup() throws Exception {
        manager = EntityManagerFactory.getEntityManager();
        model = ModelFactory.createDefaultModel();
        final Resource r = model.createResource( "http://localhost/CollectionValueObjectEntityTest" );
        underTest = manager.read( r, CollectionValueInterface.class );
    }

    @AfterEach
    public void teardown() {
        model.close();
    }
    /*
     * @Test public void testBoolean() { Assertions.assertFalse( tc.hasBool(
     * true ) ); tc.addBool( true ); Assertions.assertTrue( tc.hasBool( true )
     * ); Assertions.assertFalse( tc.hasBool( false ) ); tc.addBool( false );
     * Assertions.assertTrue( tc.hasBool( false ) ); Assertions.assertEquals( 2,
     * tc.getBool().size() ); tc.addBool( Boolean.TRUE ); Assertions.assertTrue(
     * tc.hasBool( Boolean.TRUE ) ); Assertions.assertEquals( 2,
     * tc.getBool().size() ); tc.addBool( Boolean.FALSE );
     * Assertions.assertTrue( tc.hasBool( Boolean.FALSE ) );
     * Assertions.assertEquals( 2, tc.getBool().size() ); tc.removeBool(
     * Boolean.FALSE ); Assertions.assertFalse( tc.hasBool( Boolean.FALSE ) );
     * Assertions.assertEquals( 1, tc.getBool().size() ); }
     */

//    @Test
//    public void testChar() {
//        final char c = 'a';
//        final Character cc = Character.valueOf( c );
//        tc.addChar( c );
//        Assertions.assertTrue( tc.getChar().contains( c ) );
//        tc.addChar( 'x' );
//        Assertions.assertTrue( tc.getChar().contains( 'x' ) );
//        tc.addChar( cc );
//        Assertions.assertTrue( tc.getChar().contains( cc ) );
//        tc.removeChar( cc );
//        Assertions.assertFalse( tc.getChar().contains( cc ) );
//        Assertions.assertFalse( tc.getChar().contains( c ) );
//        Assertions.assertTrue( tc.getChar().contains( 'x' ) );
//    }
    /*
     * @Test public void testDbl() { final double c = 3.14; final Double cc =
     * Double.valueOf( c ); tc.setDbl( c ); Assertions.assertEquals( cc,
     * tc.getDbl() ); tc.setDbl( 0.0 ); Assertions.assertTrue( !cc.equals(
     * tc.getDbl() ) ); tc.setDbl( cc ); Assertions.assertEquals( cc,
     * tc.getDbl() ); tc.removeDbl(); Assertions.assertNull( tc.getDbl() ); }
     *
     * @Test public void testEntity() throws Exception { Resource r =
     * model.createResource( "testclass" ); final TestInterface c =
     * manager.read( r, TestInterface.class ); tc.setEnt( c );
     * Assertions.assertEquals( c, tc.getEnt() ); r = model.createResource(
     * "testclass2" ); final TestInterface cc = manager.read( r,
     * TestInterface.class ); tc.setEnt( cc ); Assertions.assertTrue( !c.equals(
     * tc.getEnt() ) ); tc.removeEnt(); Assertions.assertNull( tc.getEnt() );
     *
     * }
     *
     * @Test public void testFlt() { final float c = 3.14F; final Float cc =
     * Float.valueOf( c ); tc.setFlt( c ); Assertions.assertEquals( cc,
     * tc.getFlt() ); tc.setFlt( 0.0F ); Assertions.assertTrue( !cc.equals(
     * tc.getFlt() ) ); tc.setFlt( cc ); Assertions.assertEquals( cc,
     * tc.getFlt() ); tc.removeFlt(); Assertions.assertNull( tc.getFlt() ); }
     *
     * @Test public void testInt() { final int c = 3; final Integer cc =
     * Integer.valueOf( c ); tc.setInt( c ); Assertions.assertEquals( cc,
     * tc.getInt() ); tc.setInt( 0 ); Assertions.assertTrue( !cc.equals(
     * tc.getInt() ) ); tc.setInt( cc ); Assertions.assertEquals( cc,
     * tc.getInt() ); tc.removeInt(); Assertions.assertNull( tc.getInt() ); }
     *
     * @Test public void testLng() { final long c = 3; final Long cc =
     * Long.valueOf( c ); tc.setLng( c ); Assertions.assertEquals( cc,
     * tc.getLng() ); tc.setLng( 0L ); Assertions.assertTrue( !cc.equals(
     * tc.getLng() ) ); tc.setLng( cc ); Assertions.assertEquals( cc,
     * tc.getLng() ); tc.removeLng(); Assertions.assertNull( tc.getLng() ); }
     *
     * @Test public void testRdf() { final RDFNode n = model.createResource(
     * "rdfNode" ); tc.setRDF( n ); Assertions.assertEquals( n, tc.getRDF() );
     * tc.setRDF( model.createResource( "anotherRdfNode" ) );
     * Assertions.assertTrue( !n.equals( tc.getStr() ) ); tc.removeRDF();
     * Assertions.assertNull( tc.getRDF() );
     *
     * }
     *
     * @Test public void testStr() { final String cc = "string"; tc.setStr(
     * "string" ); Assertions.assertEquals( cc, tc.getStr() ); tc.setStr( "foo"
     * ); Assertions.assertTrue( !cc.equals( tc.getStr() ) ); tc.setStr( cc );
     * Assertions.assertEquals( cc, tc.getStr() ); tc.removeStr();
     * Assertions.assertNull( tc.getStr() ); }
     *
     * @Test public void testSubPredicate() throws Exception { Resource r =
     * model.createResource( "http://localhost/SubPredicateTest1" ); final
     * SubPredicate sp = manager.read( r, SubPredicate.class ); sp.setName(
     * "spTest" );
     *
     * r = model.createResource( "http://localhost/SubPredicateTest2" ); final
     * SubPredicate sp2 = manager.read( r, SubPredicate.class ); sp2.setName(
     * "spTest2" );
     *
     * tc.setSubPredicate( sp ); Assertions.assertNotNull( tc.getSubPredicate()
     * ); Assertions.assertEquals( sp.getName(), tc.getSubPredicate().getName()
     * );
     *
     * tc.setSubPredicate( sp2 ); Assertions.assertNotNull( tc.getSubPredicate()
     * ); Assertions.assertEquals( sp2.getName(), tc.getSubPredicate().getName()
     * );
     *
     * tc.removeSubPredicate(); Assertions.assertNull( tc.getSubPredicate() );
     *
     * }
     *
     * @Test public void testURI() { final Resource r =
     * ResourceFactory.createResource( "uriTest" ); tc.setU( r );
     * Assertions.assertEquals( r, tc.getU() ); Assertions.assertEquals(
     * "uriTest", tc.getU2() ); tc.setU( "uriTest2" ); Assertions.assertTrue(
     * !r.equals( tc.getU() ) ); tc.removeU(); Assertions.assertNull( tc.getU()
     * ); Assertions.assertNull( tc.getU2() ); }
     */
    
    Method setter(String shortName, Class<?> dataType) throws NoSuchMethodException, SecurityException {
        return CollectionValueInterface.class.getMethod("add"+shortName, dataType);
    }
    
    Method getter(String shortName) throws NoSuchMethodException, SecurityException {
        Method m = CollectionValueInterface.class.getMethod("get"+shortName);
        assertTrue( Collection.class.isAssignableFrom( m.getReturnType() ));
        return m;
     }
    
    Method exist(String shortName, Class<?> dataType) throws NoSuchMethodException, SecurityException {
        Method m =  CollectionValueInterface.class.getMethod("has"+shortName, dataType);
        assertEquals( Boolean.class, m.getReturnType());
        return m;
    }
    
    Method remover(String shortName, Class<?> dataType) throws NoSuchMethodException, SecurityException {
        return CollectionValueInterface.class.getMethod("remove"+shortName, dataType);
    }
    
    @ParameterizedTest
    @MethodSource("testFullSetParams")
    void testFullSet(String shortName, Class<?> dataType, Object first, Object second ) throws Exception {
        Property p = model.createProperty( NAMESPACE, shortName.substring( 0,1 ).toLowerCase()+shortName.substring(1));
        Method setter = setter(shortName, dataType);
        Method getter = getter(shortName);
        Method exist = exist(shortName, dataType);
        Method remover = remover(shortName, dataType);
        
        assertTrue(model.isEmpty());        
        assertFalse((Boolean) exist.invoke(underTest, first));
        assertFalse((Boolean) exist.invoke(underTest, second));
        
        setter.invoke(underTest, first);
        assertEquals( 1, model.listObjectsOfProperty(p).toList().size());
        assertTrue((Boolean) exist.invoke(underTest, first));
        assertFalse((Boolean) exist.invoke(underTest, second));
        
        setter.invoke(underTest, second);
        assertEquals( 2, model.listObjectsOfProperty(p).toList().size());
        assertTrue((Boolean) exist.invoke(underTest, first));
        assertTrue((Boolean) exist.invoke(underTest, second));
        
        Collection<?> collection = (Collection<?>) getter.invoke(underTest);
        assertEquals(2, collection.size());
        assertTrue(collection.remove(first));
        assertTrue(collection.remove(second));

        remover.invoke(underTest, first);
        assertEquals( 1, model.listObjectsOfProperty(p).toList().size());
        assertFalse((Boolean) exist.invoke(underTest, first));
        assertTrue((Boolean) exist.invoke(underTest, second));

        remover.invoke(underTest,second);
        assertEquals( 0, model.listObjectsOfProperty(p).toList().size());
        assertFalse((Boolean) exist.invoke(underTest, first));
        assertFalse((Boolean) exist.invoke(underTest, second));
    }
    
    public static final Stream<Arguments> testFullSetParams() {
        List<Arguments> args = new ArrayList<>();
        args.add( Arguments.of( "Bool", Boolean.class, true, Boolean.FALSE ));
        args.add( Arguments.of( "Char", Character.class, Character.valueOf('A'), Character.valueOf( 'b' ) ));
        args.add( Arguments.of( "Dbl", Double.class, Double.MAX_VALUE, 3.14d ));
        //args.add( Arguments.of( "Ent", TestInterface.class, new TestInterface, 3.14d ));
        args.add( Arguments.of( "Flt", Float.class, Float.MAX_VALUE, 3.14f ));
        args.add( Arguments.of( "Int", Integer.class, Integer.MAX_VALUE, 314 ));
        args.add( Arguments.of( "Lng", Long.class, Long.MAX_VALUE, 314l));
        args.add( Arguments.of( "RDF", RDFNode.class, ResourceFactory.createStringLiteral( "foo" ), ResourceFactory.createResource()));
        args.add( Arguments.of( "Str", String.class, "first", "second"));
        args.add( Arguments.of( "U", String.class, "http://example.com#first", "http://example.com#second"));
/*9*/        args.add( Arguments.of( "U", RDFNode.class, ResourceFactory.createStringLiteral( "foo" ), ResourceFactory.createResource()));
        args.add( Arguments.of( "U3", String.class, "http://example.com#first", "http://example.com#second"));
/*11*/        args.add( Arguments.of( "U3", RDFNode.class, ResourceFactory.createStringLiteral( "foo" ), ResourceFactory.createResource()));

        /*
   

    @Predicate
    void addEnt(TestInterface b);

    void addU(RDFNode b);

    @Predicate
    void addU(@URI String b);

    @Predicate
    void addU3(RDFNode b);

    void addU3(@URI String b);

         */
        
        return args.stream();
    }
}
