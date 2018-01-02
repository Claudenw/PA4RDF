package org.xenei.jena.entities.impl;

import java.lang.reflect.Method;

import org.apache.jena.rdf.model.RDFNode;
import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.testing.iface.SimpleURIInterface;

public abstract class AbstractSimpleURIInterfaceTest extends AbstractMethodParserTest {

    protected final MethodParser parser;
    protected final SubjectInfoImpl subjectInfo;
    protected final Method getter;
    protected final Method setterS;
    protected final Method setterR;
    protected final Method remover;
    protected final Method existential;

    protected AbstractSimpleURIInterfaceTest(Class<? extends SimpleURIInterface> interfaceClass)
            throws NoSuchMethodException, SecurityException {

        getter = interfaceClass.getMethod( "getU" );
        PIMap.put( getter, mockPredicateInfo( getter, "u", ActionType.GETTER, RDFNode.class, 0, 0 ) );

        setterR = interfaceClass.getMethod( "setU", RDFNode.class );
        PIMap.put( setterR, mockPredicateInfo( setterR, "u", ActionType.SETTER, RDFNode.class, 0, 0 ) );

        setterS = interfaceClass.getMethod( "setU", String.class );
        PIMap.put( setterS, mockPredicateInfo( setterS, "u", ActionType.SETTER, String.class, 1, 0 ) );

        remover = interfaceClass.getMethod( "removeU" );
        PIMap.put( remover, mockPredicateInfo( remover, "u", ActionType.REMOVER, null, 0, 0 ) );

        existential = interfaceClass.getMethod( "hasU" );
        PIMap.put( existential, mockPredicateInfo( existential, "u", ActionType.EXISTENTIAL,
                TypeChecker.getPrimitiveClass( Boolean.class ), 0, 0 ) );

        subjectInfo = new SubjectInfoImpl( interfaceClass );

        addCount.put( "setU", Integer.valueOf( 2 ) );
        parser = new MethodParser( entityManager, subjectInfo, addCount );
    }

    @Test
    public void testParseGetter() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getter );
        assertSame( PIMap.get( getter ), predicateInfo );
        assertSame( PIMap.get( getter ), subjectInfo.getPredicateInfo( getter ) );
        assertSame( PIMap.get( setterS ), subjectInfo.getPredicateInfo( setterS ) );
        assertSame( PIMap.get( setterR ), subjectInfo.getPredicateInfo( setterR ) );
        assertSame( PIMap.get( existential ), subjectInfo.getPredicateInfo( existential ) );
        assertSame( PIMap.get( remover ), subjectInfo.getPredicateInfo( remover ) );

    }

    @Test
    public void testParseSetterS() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( setterS );
        assertSame( PIMap.get( setterS ), predicateInfo );
        assertSame( PIMap.get( getter ), subjectInfo.getPredicateInfo( getter ) );
        assertSame( PIMap.get( setterS ), subjectInfo.getPredicateInfo( setterS ) );
        assertSame( PIMap.get( setterR ), subjectInfo.getPredicateInfo( setterR ) );
        assertSame( PIMap.get( existential ), subjectInfo.getPredicateInfo( existential ) );
        assertSame( PIMap.get( remover ), subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseSetterR() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( setterR );
        assertSame( PIMap.get( setterR ), predicateInfo );
        assertSame( PIMap.get( getter ), subjectInfo.getPredicateInfo( getter ) );
        assertSame( PIMap.get( setterS ), subjectInfo.getPredicateInfo( setterS ) );
        assertSame( PIMap.get( setterR ), subjectInfo.getPredicateInfo( setterR ) );
        assertSame( PIMap.get( existential ), subjectInfo.getPredicateInfo( existential ) );
        assertSame( PIMap.get( remover ), subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseExistential() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( existential );
        assertSame( PIMap.get( existential ), predicateInfo );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterS ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterR ) );
        assertSame( PIMap.get( existential ), subjectInfo.getPredicateInfo( existential ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseRemover() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( remover );
        assertSame( PIMap.get( remover ), predicateInfo );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterS ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterR ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( existential ) );
        assertSame( PIMap.get( remover ), subjectInfo.getPredicateInfo( remover ) );
    }

}
