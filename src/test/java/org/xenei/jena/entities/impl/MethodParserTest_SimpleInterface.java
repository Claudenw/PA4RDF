package org.xenei.jena.entities.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public class MethodParserTest_SimpleInterface extends AbstractMethodParserTest {

    private final MethodParser parser;
    private final EntityManagerImpl entityManager = (EntityManagerImpl) EntityManagerFactory.create();
    private final Map<String, Integer> addCount = new HashMap<String, Integer>();
    private final Map<Method, PredicateInfo> PIMap = new HashMap<Method, PredicateInfo>();
    private final SubjectInfoImpl subjectInfo;
    private final Method getter;
    private final Method setter;
    private final Method remover;
    private final Method existential;

    public MethodParserTest_SimpleInterface() throws Exception {
        getter = SimpleInterface.class.getMethod( "getX" );
        PIMap.put( getter, mockPredicateInfo( getter, "x", ActionType.GETTER, String.class, 0, 0 ) );

        setter = SimpleInterface.class.getMethod( "setX", String.class );
        PIMap.put( setter, mockPredicateInfo( setter, "x", ActionType.SETTER, String.class, 1, 0 ) );

        remover = SimpleInterface.class.getMethod( "removeX" );
        PIMap.put( remover, mockPredicateInfo( remover, "x", ActionType.REMOVER, null, 0, 0 ) );

        existential = SimpleInterface.class.getMethod( "hasX" );
        PIMap.put( existential, mockPredicateInfo( existential, "x", ActionType.EXISTENTIAL, TypeChecker.getPrimitiveClass( Boolean.class ), 0, 0 ) );

        subjectInfo = new SubjectInfoImpl( SimpleInterface.class );
        
        addCount.clear();
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
