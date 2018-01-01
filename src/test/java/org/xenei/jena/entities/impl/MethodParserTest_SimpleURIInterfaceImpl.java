package org.xenei.jena.entities.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.testing.impl.SimpleURIInterfaceImpl;

public class MethodParserTest_SimpleURIInterfaceImpl {

    private final MethodParser parser;
    private final EntityManagerImpl entityManager = (EntityManagerImpl) EntityManagerFactory.create();
    private final Map<String, Integer> addCount = new HashMap<String, Integer>();
    private final SubjectInfoImpl subjectInfo;
    private final Method getter;
    private final Method setterR;
    private final Method setterS;
    private final Method remover;
    private final Method existential;

    public MethodParserTest_SimpleURIInterfaceImpl() throws Exception {
        getter = SimpleURIInterfaceImpl.class.getMethod( "getU" );
        setterR = SimpleURIInterfaceImpl.class.getMethod( "setU", RDFNode.class );
        setterS = SimpleURIInterfaceImpl.class.getMethod( "setU", String.class );
        remover = SimpleURIInterfaceImpl.class.getMethod( "removeU" );
        existential = SimpleURIInterfaceImpl.class.getMethod( "hasU" );
        subjectInfo = new SubjectInfoImpl( SimpleURIInterfaceImpl.class );
        addCount.clear();
        addCount.put( "setU", Integer.valueOf( 2 ) );
        parser = new MethodParser( entityManager, subjectInfo, addCount );
    }

    @Test
    public void testParseGetter() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getter );
        assertGetX( predicateInfo );
        assertGetX( subjectInfo.getPredicateInfo( getter ) );
        assertSetX( subjectInfo.getPredicateInfo( setterR ), RDFNode.class );
        assertSetX( subjectInfo.getPredicateInfo( setterS ), String.class );
        assertHasX( subjectInfo.getPredicateInfo( existential ) );
        assertRemoveX( subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseSetterR() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( setterR );
        assertSetX( predicateInfo, RDFNode.class );
        assertGetX( subjectInfo.getPredicateInfo( getter ) );
        assertSetX( subjectInfo.getPredicateInfo( setterR ), RDFNode.class );
        assertSetX( subjectInfo.getPredicateInfo( setterS ), String.class );
        assertHasX( subjectInfo.getPredicateInfo( existential ) );
        assertRemoveX( subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseSetterS() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( setterS );
        assertSetX( predicateInfo, String.class );
        assertGetX( subjectInfo.getPredicateInfo( getter ) );
        assertSetX( subjectInfo.getPredicateInfo( setterR ), RDFNode.class );
        assertSetX( subjectInfo.getPredicateInfo( setterS ), String.class );
        assertHasX( subjectInfo.getPredicateInfo( existential ) );
        assertRemoveX( subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseExistential() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( existential );
        assertHasX( predicateInfo );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterR ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterS ) );
        assertHasX( subjectInfo.getPredicateInfo( existential ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseRemover() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( remover );
        assertRemoveX( predicateInfo );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterR ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterS ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( existential ) );
        assertRemoveX( subjectInfo.getPredicateInfo( remover ) );
    }

    private void assertSetX(PredicateInfo predicateInfo, Class<?> valueClass) {
        Assert.assertNotNull( "Missing predicate info " + ActionType.SETTER, predicateInfo );
        Assert.assertEquals( ActionType.SETTER, predicateInfo.getActionType() );
        Assert.assertEquals( "setU", predicateInfo.getMethodName() );
        Assert.assertEquals( "found unexpected annotations", 1, predicateInfo.getAnnotations().size() );
        Assert.assertEquals( "http://example.com/", predicateInfo.getNamespace() );
        Assert.assertTrue( "found unexpected postExec", predicateInfo.getPostExec().isEmpty() );
        Assert.assertEquals( ResourceFactory.createProperty( "http://example.com/u" ), predicateInfo.getProperty() );
        Assert.assertEquals( "http://example.com/u", predicateInfo.getUriString() );
        Assert.assertEquals( valueClass, predicateInfo.getValueClass() );
    }

    private void assertRemoveX(PredicateInfo predicateInfo) {
        Assert.assertNotNull( "Missing predicate info " + ActionType.REMOVER, predicateInfo );
        Assert.assertEquals( ActionType.REMOVER, predicateInfo.getActionType() );
        Assert.assertEquals( "removeU", predicateInfo.getMethodName() );
        Assert.assertEquals( "found unexpected annotations", 1, predicateInfo.getAnnotations().size() );
        Assert.assertEquals( "http://example.com/", predicateInfo.getNamespace() );
        Assert.assertTrue( "found unexpected postExec", predicateInfo.getPostExec().isEmpty() );
        Assert.assertEquals( ResourceFactory.createProperty( "http://example.com/u" ), predicateInfo.getProperty() );
        Assert.assertEquals( "http://example.com/u", predicateInfo.getUriString() );
        Assert.assertEquals( null, predicateInfo.getValueClass() );
    }

    private void assertHasX(PredicateInfo predicateInfo) {
        Assert.assertNotNull( "Missing predicate info " + ActionType.EXISTENTIAL, predicateInfo );
        Assert.assertEquals( ActionType.EXISTENTIAL, predicateInfo.getActionType() );
        Assert.assertEquals( "hasU", predicateInfo.getMethodName() );
        Assert.assertEquals( "found unexpected annotations", 1, predicateInfo.getAnnotations().size() );
        Assert.assertEquals( "http://example.com/", predicateInfo.getNamespace() );
        Assert.assertTrue( "found unexpected postExec", predicateInfo.getPostExec().isEmpty() );
        Assert.assertEquals( ResourceFactory.createProperty( "http://example.com/u" ), predicateInfo.getProperty() );
        Assert.assertEquals( "http://example.com/u", predicateInfo.getUriString() );
        Assert.assertEquals( TypeChecker.getPrimitiveClass( Boolean.class ), predicateInfo.getValueClass() );
    }

    private void assertGetX(PredicateInfo predicateInfo) {
        Assert.assertNotNull( "Missing predicate info " + ActionType.GETTER, predicateInfo );
        Assert.assertEquals( ActionType.GETTER, predicateInfo.getActionType() );
        Assert.assertEquals( "getU", predicateInfo.getMethodName() );
        Assert.assertEquals( "found unexpected annotations", 1, predicateInfo.getAnnotations().size() );
        Assert.assertEquals( "http://example.com/", predicateInfo.getNamespace() );
        Assert.assertTrue( "found unexpected postExec", predicateInfo.getPostExec().isEmpty() );
        Assert.assertEquals( ResourceFactory.createProperty( "http://example.com/u" ), predicateInfo.getProperty() );
        Assert.assertEquals( "http://example.com/u", predicateInfo.getUriString() );
        Assert.assertEquals( RDFNode.class, predicateInfo.getValueClass() );
    }

}
