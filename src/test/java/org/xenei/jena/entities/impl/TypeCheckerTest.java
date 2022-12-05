package org.xenei.jena.entities.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TypeCheckerTest {

    @Test
    public void canBeSetFromTest() {
        Assertions.assertFalse( TypeChecker.canBeSetFrom( null, String.class ) );
        Assertions.assertFalse( TypeChecker.canBeSetFrom( String.class, null ) );
        Assertions.assertTrue( TypeChecker.canBeSetFrom( Number.class, Integer.class ) );
        Assertions.assertTrue( TypeChecker.canBeSetFrom( Integer.class, int.class ) );
        Assertions.assertTrue( TypeChecker.canBeSetFrom( int.class, Integer.class ) );
        Assertions.assertFalse( TypeChecker.canBeSetFrom( int.class, String.class ) );
        Assertions.assertFalse( TypeChecker.canBeSetFrom( String.class, int.class ) );
    }

    @Test
    public void getPrimitiveClassTest() {
        Assertions.assertNull( TypeChecker.getPrimitiveClass( String.class ) );
        Assertions.assertEquals( int.class, TypeChecker.getPrimitiveClass( Integer.class ) );
        Assertions.assertEquals( int.class, TypeChecker.getPrimitiveClass( int.class ) );
        Assertions.assertNull( TypeChecker.getPrimitiveClass( TypeCheckerTest.class ) );
    }
}
