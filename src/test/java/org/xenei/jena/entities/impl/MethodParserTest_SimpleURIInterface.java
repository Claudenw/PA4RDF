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
import org.xenei.jena.entities.testing.iface.SimpleURIInterface;

public class MethodParserTest_SimpleURIInterface {

    private final MethodParser parser;
    private final EntityManagerImpl entityManager = (EntityManagerImpl) EntityManagerFactory.create();
    private final Map<String, Integer> addCount = new HashMap<String, Integer>();
    private final SubjectInfoImpl subjectInfo;
    private final Method getter;
    private final Method setterR;
    private final Method setterS;
    private final Method remover;
    private final Method existential;

    public MethodParserTest_SimpleURIInterface() throws Exception {
        getter = SimpleURIInterface.class.getMethod( "getU" );
        setterR = SimpleURIInterface.class.getMethod( "setU", RDFNode.class );
        setterS = SimpleURIInterface.class.getMethod( "setU", String.class );
        remover = SimpleURIInterface.class.getMethod( "removeU" );
        existential = SimpleURIInterface.class.getMethod( "hasU" );
        subjectInfo = new SubjectInfoImpl( SimpleURIInterface.class );
        addCount.clear();
        addCount.put( "setU", Integer.valueOf( 2 ) );
        parser = new MethodParser( entityManager, subjectInfo, addCount );
    }

    @Test
    public void testParseGetter() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getter );
        assertGetX( predicateInfo );
        assertGetX( subjectInfo.getPredicateInfo( getter ) );
        assertSetX( subjectInfo.getPredicateInfo( setterR ), RDFNode.class, 0 );
        assertSetX( subjectInfo.getPredicateInfo( setterS ), String.class, 1 );
        assertHasX( subjectInfo.getPredicateInfo( existential ) );
        assertRemoveX( subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseSetterR() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( setterR );
        assertSetX( predicateInfo, RDFNode.class, 0 );
        assertGetX( subjectInfo.getPredicateInfo( getter ) );
        assertSetX( subjectInfo.getPredicateInfo( setterR ), RDFNode.class, 0 );
        assertSetX( subjectInfo.getPredicateInfo( setterS ), String.class, 1 );
        assertHasX( subjectInfo.getPredicateInfo( existential ) );
        assertRemoveX( subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseSetterS() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( setterS );
        assertSetX( predicateInfo, String.class, 1 );
        assertGetX( subjectInfo.getPredicateInfo( getter ) );
        assertSetX( subjectInfo.getPredicateInfo( setterR ), RDFNode.class, 0 );
        assertSetX( subjectInfo.getPredicateInfo( setterS ), String.class, 1 );
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

    private void assertSetX(PredicateInfo predicateInfo, Class<?> valueClass, int annotationCount) {
        Assert.assertNotNull( "Missing predicate info " + ActionType.SETTER, predicateInfo );
        Assert.assertEquals( ActionType.SETTER, predicateInfo.getActionType() );
        Assert.assertEquals( "setU", predicateInfo.getMethodName() );
        Assert.assertEquals( "missing annotations", annotationCount, predicateInfo.getAnnotations().size() );
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
        Assert.assertTrue( "found unexpected annotations", predicateInfo.getAnnotations().isEmpty() );
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
        Assert.assertTrue( "found unexpected annotations", predicateInfo.getAnnotations().isEmpty() );
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
        Assert.assertTrue( "found unexpected annotations", predicateInfo.getAnnotations().isEmpty() );
        Assert.assertEquals( "http://example.com/", predicateInfo.getNamespace() );
        Assert.assertTrue( "found unexpected postExec", predicateInfo.getPostExec().isEmpty() );
        Assert.assertEquals( ResourceFactory.createProperty( "http://example.com/u" ), predicateInfo.getProperty() );
        Assert.assertEquals( "http://example.com/u", predicateInfo.getUriString() );
        Assert.assertEquals( RDFNode.class, predicateInfo.getValueClass() );
    }

}
