package org.xenei.jena.entities.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.PrimitiveIterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

public class ActionTypeTest {

    @BeforeEach
    public void setUp() throws Exception {
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void testExistentialTypes() {
        final ActionType type = ActionType.parse( "hasX" );
        Assertions.assertTrue( type == ActionType.EXISTENTIAL, "hasX should be EXISTENTIAL" );
    }

    @Test
    public void testGetterTypes() {
        ActionType type = ActionType.parse( "getX" );
        Assertions.assertTrue( type == ActionType.GETTER, "Should be GETTER" );
        type = ActionType.parse( "getIs" );
        Assertions.assertTrue( type == ActionType.GETTER, "Should be GETTER" );
    }

    @Test
    public void testRemoverTypes() {
        final ActionType type = ActionType.parse( "removeX" );
        Assertions.assertTrue( type == ActionType.REMOVER, "hasX should be REMOVER" );
    }

    @Test
    public void testSetterTypes() {
        ActionType type = ActionType.parse( "setX" );
        Assertions.assertTrue( type == ActionType.SETTER, "setX should be SETTER" );
        type = ActionType.parse( "addX" );
        Assertions.assertTrue( type == ActionType.SETTER, "setX should be SETTER" );
    }

    @Test
    public void isATest() {
        Assertions.assertTrue( ActionType.EXISTENTIAL.isA( "hasX" ) );
        Assertions.assertFalse( ActionType.EXISTENTIAL.isA( "getX" ) );
        Assertions.assertFalse( ActionType.EXISTENTIAL.isA( "removeX" ) );
        Assertions.assertFalse( ActionType.EXISTENTIAL.isA( "setX" ) );
        Assertions.assertFalse( ActionType.EXISTENTIAL.isA( "addX" ) );
        Assertions.assertFalse( ActionType.EXISTENTIAL.isA( "random" ) );
        Assertions.assertFalse( ActionType.EXISTENTIAL.isA( null ) );
        Assertions.assertFalse( ActionType.EXISTENTIAL.isA( "" ) );
        Assertions.assertFalse( ActionType.EXISTENTIAL.isA( " " ) );

        Assertions.assertFalse( ActionType.GETTER.isA( "hasX" ) );
        Assertions.assertTrue( ActionType.GETTER.isA( "getX" ) );
        Assertions.assertFalse( ActionType.GETTER.isA( "removeX" ) );
        Assertions.assertFalse( ActionType.GETTER.isA( "setX" ) );
        Assertions.assertFalse( ActionType.GETTER.isA( "addX" ) );
        Assertions.assertFalse( ActionType.GETTER.isA( "random" ) );
        Assertions.assertFalse( ActionType.GETTER.isA( null ) );
        Assertions.assertFalse( ActionType.GETTER.isA( "" ) );
        Assertions.assertFalse( ActionType.GETTER.isA( " " ) );

        Assertions.assertFalse( ActionType.REMOVER.isA( "hasX" ) );
        Assertions.assertFalse( ActionType.REMOVER.isA( "getX" ) );
        Assertions.assertTrue( ActionType.REMOVER.isA( "removeX" ) );
        Assertions.assertFalse( ActionType.REMOVER.isA( "setX" ) );
        Assertions.assertFalse( ActionType.REMOVER.isA( "addX" ) );
        Assertions.assertFalse( ActionType.REMOVER.isA( "random" ) );
        Assertions.assertFalse( ActionType.REMOVER.isA( null ) );
        Assertions.assertFalse( ActionType.REMOVER.isA( "" ) );
        Assertions.assertFalse( ActionType.REMOVER.isA( " " ) );

        Assertions.assertFalse( ActionType.SETTER.isA( "hasX" ) );
        Assertions.assertFalse( ActionType.SETTER.isA( "getX" ) );
        Assertions.assertFalse( ActionType.SETTER.isA( "removeX" ) );
        Assertions.assertTrue( ActionType.SETTER.isA( "setX" ) );
        Assertions.assertTrue( ActionType.SETTER.isA( "addX" ) );
        Assertions.assertFalse( ActionType.SETTER.isA( "random" ) );
        Assertions.assertFalse( ActionType.SETTER.isA( null ) );
        Assertions.assertFalse( ActionType.SETTER.isA( "" ) );
        Assertions.assertFalse( ActionType.SETTER.isA( " " ) );
    }

    @Test
    public void isMultipleTest() throws Exception {
        Assertions.assertFalse( ActionType.GETTER.isMultiple( MultipleTest.class.getMethod( "getSingle" ) ) );
        Assertions.assertTrue( ActionType.GETTER.isMultiple( MultipleTest.class.getMethod( "getArray" ) ) );
        Assertions.assertTrue( ActionType.GETTER.isMultiple( MultipleTest.class.getMethod( "getCollection" ) ) );
        Assertions.assertTrue( ActionType.GETTER.isMultiple( MultipleTest.class.getMethod( "getIterator" ) ) );
        Assertions.assertTrue( ActionType.GETTER.isMultiple( MultipleTest.class.getMethod( "getPrimitive" ) ) );
        Assertions.assertFalse(
                ActionType.SETTER.isMultiple( MultipleTest.class.getMethod( "setSingle", Integer.class ) ) );
        Assertions.assertTrue(
                ActionType.SETTER.isMultiple( MultipleTest.class.getMethod( "addMultiple", Integer.class ) ) );
        Assertions.assertFalse( ActionType.EXISTENTIAL.isMultiple( MultipleTest.class.getMethod( "hasSingle" ) ) );
        Assertions.assertTrue(
                ActionType.EXISTENTIAL.isMultiple( MultipleTest.class.getMethod( "hasMultiple", Integer.class ) ) );
        Assertions.assertFalse( ActionType.REMOVER.isMultiple( MultipleTest.class.getMethod( "removeSingle" ) ) );
        Assertions.assertTrue(
                ActionType.REMOVER.isMultiple( MultipleTest.class.getMethod( "removeMultiple", Integer.class ) ) );

        Assertions.assertFalse( ActionType.GETTER.isMultiple( MultipleTest.class.getMethod( "notAnActionMethod" ) ) );
        Assertions.assertFalse( ActionType.SETTER.isMultiple( MultipleTest.class.getMethod( "notAnActionMethod" ) ) );
        Assertions.assertFalse(
                ActionType.EXISTENTIAL.isMultiple( MultipleTest.class.getMethod( "notAnActionMethod" ) ) );
        Assertions.assertFalse( ActionType.REMOVER.isMultiple( MultipleTest.class.getMethod( "notAnActionMethod" ) ) );
    }

    @Test
    public void extractNameTest() {
        Assertions.assertEquals( "X", ActionType.EXISTENTIAL.extractName( "hasX" ) );
        Assertions.assertEquals( "X", ActionType.EXISTENTIAL.extractName( "isX" ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.EXISTENTIAL.extractName( "random" ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.EXISTENTIAL.extractName( null ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.EXISTENTIAL.extractName( "" ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.EXISTENTIAL.extractName( " " ) );

        Assertions.assertEquals( "X", ActionType.GETTER.extractName( "getX" ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.GETTER.extractName( "random" ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.GETTER.extractName( null ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.GETTER.extractName( "" ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.GETTER.extractName( " " ) );

        Assertions.assertEquals( "X", ActionType.REMOVER.extractName( "removeX" ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.REMOVER.extractName( "random" ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.REMOVER.extractName( null ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.REMOVER.extractName( "" ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.REMOVER.extractName( " " ) );

        Assertions.assertEquals( "X", ActionType.SETTER.extractName( "setX" ) );
        Assertions.assertEquals( "X", ActionType.SETTER.extractName( "addX" ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.REMOVER.extractName( "random" ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.REMOVER.extractName( null ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.REMOVER.extractName( "" ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ActionType.REMOVER.extractName( " " ) );
    }

    @Subject(namespace = "http://example.com/")
    interface MultipleTest {
        @Predicate
        Integer getSingle();

        @Predicate
        Integer[] getArray();

        @Predicate
        Collection<Integer> getCollection();

        @Predicate
        Iterator<Integer> getIterator();

        @Predicate
        PrimitiveIterator.OfInt getPrimitive();

        @Predicate
        void setSingle(Integer i);

        @Predicate
        void addMultiple(Integer i);

        @Predicate
        boolean hasSingle();

        @Predicate
        boolean hasMultiple(Integer a);

        @Predicate
        boolean removeSingle();

        @Predicate
        boolean removeMultiple(Integer a);

        void notAnActionMethod();
    }
}
