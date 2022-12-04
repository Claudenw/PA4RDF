package org.xenei.jena.entities.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TypeCheckerTest {

    @Test
    public void canBeSetFromTest() {
        assertFalse( TypeChecker.canBeSetFrom( null, String.class ));
        assertFalse( TypeChecker.canBeSetFrom( String.class, null ));
        assertTrue( TypeChecker.canBeSetFrom(  Number.class,  Integer.class ));
        assertTrue( TypeChecker.canBeSetFrom(  Integer.class,  int.class ));
        assertTrue( TypeChecker.canBeSetFrom(  int.class, Integer.class ));
        assertFalse( TypeChecker.canBeSetFrom(  int.class, String.class ));
        assertFalse( TypeChecker.canBeSetFrom(  String.class, int.class ));
    }
    
    @Test
    public void getPrimitiveClassTest() {
        assertNull( TypeChecker.getPrimitiveClass( String.class ));
        assertEquals( int.class, TypeChecker.getPrimitiveClass( Integer.class ));
        assertEquals( int.class, TypeChecker.getPrimitiveClass( int.class ));
        assertNull( TypeChecker.getPrimitiveClass( TypeCheckerTest.class ));
    }
}
