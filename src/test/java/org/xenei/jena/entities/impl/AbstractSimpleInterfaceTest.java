package org.xenei.jena.entities.impl;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public abstract class AbstractSimpleInterfaceTest extends AbstractMethodParserTest {

    protected final MethodParser parser;
    protected final SubjectInfoImpl subjectInfo;
    protected final Method getter;
    protected final Method setter;
    protected final Method remover;
    protected final Method existential;

    protected AbstractSimpleInterfaceTest(Class<? extends SimpleInterface> interfaceClass)
            throws NoSuchMethodException, SecurityException {

        getter = interfaceClass.getMethod( "getX" );
        PIMap.put( getter, mockPredicateInfo( getter, "x", ActionType.GETTER, String.class, 0, 0 ) );

        setter = interfaceClass.getMethod( "setX", String.class );
        PIMap.put( setter, mockPredicateInfo( setter, "x", ActionType.SETTER, String.class, 1, 0 ) );

        remover = interfaceClass.getMethod( "removeX" );
        PIMap.put( remover, mockPredicateInfo( remover, "x", ActionType.REMOVER, null, 0, 0 ) );

        existential = interfaceClass.getMethod( "hasX" );
        PIMap.put( existential, mockPredicateInfo( existential, "x", ActionType.EXISTENTIAL,
                TypeChecker.getPrimitiveClass( Boolean.class ), 0, 0 ) );

        subjectInfo = new SubjectInfoImpl( interfaceClass );

        addCount.put( "setX", Integer.valueOf( 1 ) );
        parser = new MethodParser( entityManager, subjectInfo, addCount );
    }

    @Test
    public void testParseGetter() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getter );
        assertSame( PIMap.get( getter ), predicateInfo );
        assertSame( PIMap.get( getter ), subjectInfo.getPredicateInfo( getter ) );
        assertSame( PIMap.get( setter ), subjectInfo.getPredicateInfo( setter ) );
        assertSame( PIMap.get( existential ), subjectInfo.getPredicateInfo( existential ) );
        assertSame( PIMap.get( remover ), subjectInfo.getPredicateInfo( remover ) );

    }

    @Test
    public void testParseSetter() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( setter );
        assertSame( PIMap.get( setter ), predicateInfo );
        assertSame( PIMap.get( getter ), subjectInfo.getPredicateInfo( getter ) );
        assertSame( PIMap.get( setter ), subjectInfo.getPredicateInfo( setter ) );
        assertSame( PIMap.get( existential ), subjectInfo.getPredicateInfo( existential ) );
        assertSame( PIMap.get( remover ), subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseExistential() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( existential );
        assertSame( PIMap.get( existential ), predicateInfo );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setter ) );
        assertSame( PIMap.get( existential ), subjectInfo.getPredicateInfo( existential ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseRemover() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( remover );
        assertSame( PIMap.get( remover ), predicateInfo );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( existential ) );
        assertSame( PIMap.get( remover ), subjectInfo.getPredicateInfo( remover ) );
    }

}
