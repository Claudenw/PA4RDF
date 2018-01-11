package org.xenei.jena.entities.impl.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;

import org.junit.Test;
import org.xenei.jena.entities.impl.ObjectHandler;

public abstract class AbstractObjectHandlerTest {

    protected ObjectHandler underTest;

    /**
     * Create an rdf node with a normal value
     */
    public abstract void testCreateRDFNode();

    /**
     * Create an rdf node with a null argument
     */
    public abstract void testCreateRDFNode_Null();

    /**
     * test values are empty
     */
    public abstract void testIsEmpty();

    /**
     * Test that parse object works.
     */
    public abstract void testParseObject();

    @Test
    public final void testEqualsOverride() throws NoSuchMethodException, SecurityException {
        assertNotNull( "underTest must be set", underTest);
        final Method m = underTest.getClass().getMethod( "equals", Object.class );
        assertEquals( String.format(  "%s must override %s", underTest.getClass(), m ),
                underTest.getClass(), m.getDeclaringClass());
    }

    @Test
    public final void testHashCodeOverride() throws NoSuchMethodException, SecurityException {
        assertNotNull( "underTest must be set", underTest);
        final Method m = underTest.getClass().getMethod( "hashCode" );
        assertEquals( String.format(  "%s must override %s", underTest.getClass(), m ),
                underTest.getClass(), m.getDeclaringClass());
    }

    @Test
    public final void testToStringOverride() throws NoSuchMethodException, SecurityException {
        assertNotNull( "underTest must be set", underTest);
        final Method m = underTest.getClass().getMethod( "toString" );
        assertEquals( String.format(  "%s must override %s", underTest.getClass(), m ),
                underTest.getClass(), m.getDeclaringClass());
    }
}
