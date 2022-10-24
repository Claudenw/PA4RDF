package org.xenei.jena.entities.impl.parser;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.impl.ActionType;

abstract public class AbstractSimpleTest extends BaseAbstractParserTest {

    protected AbstractSimpleTest(final Class<?> classUnderTest) {
        super( classUnderTest );
    }

    @Test
    public void testStandardGetter() throws Exception {
        final Method m = classUnderTest.getMethod( "getX" );
        final PredicateInfo pi = parser.parse( m );
        validatePredicateInfo( pi, "getX", ActionType.GETTER, "x", String.class );
    }

    @Test
    public void testStandardHas() throws Exception {
        final Method m = classUnderTest.getMethod( "hasX" );
        final PredicateInfo pi = parser.parse( m );
        validatePredicateInfo( pi, "hasX", ActionType.EXISTENTIAL, "x", boolean.class );
    }

    @Test
    public void testStandardRemove() throws Exception {
        final Method m = classUnderTest.getMethod( "removeX" );
        final PredicateInfo pi = parser.parse( m );
        validatePredicateInfo( pi, "removeX", ActionType.REMOVER, "x", null );
    }

    @Test
    public void testStandardSetter() throws Exception {
        final Method m = classUnderTest.getMethod( "setX", String.class );
        PredicateInfo pi = parser.parse( m );
        validatePredicateInfo( pi, "setX", ActionType.SETTER, "x", String.class );

        pi = subjectInfo.getPredicateInfo( classUnderTest.getMethod( "getX" ) );
        validatePredicateInfo( pi, "getX", ActionType.GETTER, "x", String.class );

        pi = subjectInfo.getPredicateInfo( classUnderTest.getMethod( "hasX" ) );
        validatePredicateInfo( pi, "hasX", ActionType.EXISTENTIAL, "x", boolean.class );

        pi = subjectInfo.getPredicateInfo( classUnderTest.getMethod( "removeX" ) );
        validatePredicateInfo( pi, "removeX", ActionType.REMOVER, "x", null );
    }
}
