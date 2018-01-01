package org.xenei.jena.entities.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.testing.impl.SimpleInterfaceImpl;

public class MethodParserTest_SimpleInterfaceImpl {

    private final MethodParser parser;
    private final EntityManagerImpl entityManager = (EntityManagerImpl) EntityManagerFactory.create();
    private final Map<String, Integer> addCount = new HashMap<String, Integer>();
    private final SubjectInfoImpl subjectInfo;
    private final Method getter;
    private final Method setter;
    private final Method remover;
    private final Method existential;

    public MethodParserTest_SimpleInterfaceImpl() throws Exception {
        getter = SimpleInterfaceImpl.class.getMethod( "getX" );
        setter = SimpleInterfaceImpl.class.getMethod( "setX", String.class );
        remover = SimpleInterfaceImpl.class.getMethod( "removeX" );
        existential = SimpleInterfaceImpl.class.getMethod( "hasX" );
        subjectInfo = new SubjectInfoImpl( SimpleInterfaceImpl.class );
        addCount.clear();
        addCount.put( "setX", Integer.valueOf( 1 ) );
        parser = new MethodParser( entityManager, subjectInfo, addCount );
    }

    @Test
    public void testParseGetter() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getter );
        assertGetX( predicateInfo );
        assertGetX( subjectInfo.getPredicateInfo( getter ) );
        assertSetX( subjectInfo.getPredicateInfo( setter ) );
        assertHasX( subjectInfo.getPredicateInfo( existential ) );
        assertRemoveX( subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseSetter() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( setter );
        assertSetX( predicateInfo );
        assertGetX( subjectInfo.getPredicateInfo( getter ) );
        assertSetX( subjectInfo.getPredicateInfo( setter ) );
        assertHasX( subjectInfo.getPredicateInfo( existential ) );
        assertRemoveX( subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseExistential() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( existential );
        assertHasX( predicateInfo );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setter ) );
        assertHasX( subjectInfo.getPredicateInfo( existential ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseRemover() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( remover );
        assertRemoveX( predicateInfo );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( existential ) );
        assertRemoveX( subjectInfo.getPredicateInfo( remover ) );
    }
    /*
     * @Subject(namespace = "http://example.com/") public interface
     * SimpleInterface { String getX();
     * 
     * boolean hasX();
     * 
     * void removeX();
     * 
     * @Predicate void setX(String x);
     * 
     * }
     */

    private void assertSetX(PredicateInfo predicateInfo) {
        Assert.assertNotNull( "Missing predicate info " + ActionType.SETTER, predicateInfo );
        Assert.assertEquals( ActionType.SETTER, predicateInfo.getActionType() );
        Assert.assertEquals( "setX", predicateInfo.getMethodName() );
        Assert.assertEquals( "found unexpected annotations", 1, predicateInfo.getAnnotations().size() );
        Assert.assertEquals( "http://example.com/", predicateInfo.getNamespace() );
        Assert.assertTrue( "found unexpected postExec", predicateInfo.getPostExec().isEmpty() );
        Assert.assertEquals( ResourceFactory.createProperty( "http://example.com/x" ), predicateInfo.getProperty() );
        Assert.assertEquals( "http://example.com/x", predicateInfo.getUriString() );
        Assert.assertEquals( String.class, predicateInfo.getValueClass() );
    }

    private void assertRemoveX(PredicateInfo predicateInfo) {
        Assert.assertNotNull( "Missing predicate info " + ActionType.REMOVER, predicateInfo );
        Assert.assertEquals( ActionType.REMOVER, predicateInfo.getActionType() );
        Assert.assertEquals( "removeX", predicateInfo.getMethodName() );
        Assert.assertEquals( "found unexpected annotations", 1, predicateInfo.getAnnotations().size() );
        Assert.assertEquals( "http://example.com/", predicateInfo.getNamespace() );
        Assert.assertTrue( "found unexpected postExec", predicateInfo.getPostExec().isEmpty() );
        Assert.assertEquals( ResourceFactory.createProperty( "http://example.com/x" ), predicateInfo.getProperty() );
        Assert.assertEquals( "http://example.com/x", predicateInfo.getUriString() );
        Assert.assertEquals( null, predicateInfo.getValueClass() );
    }

    private void assertHasX(PredicateInfo predicateInfo) {
        Assert.assertNotNull( "Missing predicate info " + ActionType.EXISTENTIAL, predicateInfo );
        Assert.assertEquals( ActionType.EXISTENTIAL, predicateInfo.getActionType() );
        Assert.assertEquals( "hasX", predicateInfo.getMethodName() );
        Assert.assertEquals( "found unexpected annotations", 1, predicateInfo.getAnnotations().size() );
        Assert.assertEquals( "http://example.com/", predicateInfo.getNamespace() );
        Assert.assertTrue( "found unexpected postExec", predicateInfo.getPostExec().isEmpty() );
        Assert.assertEquals( ResourceFactory.createProperty( "http://example.com/x" ), predicateInfo.getProperty() );
        Assert.assertEquals( "http://example.com/x", predicateInfo.getUriString() );
        Assert.assertEquals( TypeChecker.getPrimitiveClass( Boolean.class ), predicateInfo.getValueClass() );
    }

    private void assertGetX(PredicateInfo predicateInfo) {
        Assert.assertNotNull( "Missing predicate info " + ActionType.GETTER, predicateInfo );
        Assert.assertEquals( ActionType.GETTER, predicateInfo.getActionType() );
        Assert.assertEquals( "getX", predicateInfo.getMethodName() );
        Assert.assertEquals( "found unexpected annotations", 1, predicateInfo.getAnnotations().size() );
        Assert.assertEquals( "http://example.com/", predicateInfo.getNamespace() );
        Assert.assertEquals( "found unexpected postExec", 1, predicateInfo.getPostExec().size() );
        Assert.assertEquals( ResourceFactory.createProperty( "http://example.com/x" ), predicateInfo.getProperty() );
        Assert.assertEquals( "http://example.com/x", predicateInfo.getUriString() );
        Assert.assertEquals( String.class, predicateInfo.getValueClass() );
    }

}
