package org.xenei.jena.entities.impl;

import java.lang.reflect.Method;

import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.xenei.jena.entities.ObjectHandler;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public class SubjectInfoImplTest {

    private static String namespace = "http://example.com/";
    private SubjectInfoImpl subjectInfo;
    private Method setterMethod;
    private PredicateInfo setter;
    private Method getterMethod;
    private PredicateInfo getter;
    private Method removerMethod;
    private PredicateInfo remover;
    private Method removerNoArgMethod;
    private PredicateInfo removerNoArg;
    private Method existMethod;
    private PredicateInfo exist;
    private Method existNoArgMethod;
    private PredicateInfo existNoArg;

    interface TestInterface {
        void setMockSetter(Integer i);

        void setMockSetter(Integer... i);

        void setMockSetter(Integer i, long j);

        Integer getMockGetter();

        void removeMockRemover(Integer i);

        void removeMockRemover();

        boolean hasMockExist(Integer i);

        boolean hasMockExist();

        void fakeMethod();
    }

    private void setupMethods() throws Exception {
        setterMethod = TestInterface.class.getMethod( "setMockSetter", Integer.class );
        getterMethod = TestInterface.class.getMethod( "getMockGetter" );
        removerMethod = TestInterface.class.getMethod( "removeMockRemover", Integer.class );
        removerNoArgMethod = TestInterface.class.getMethod( "removeMockRemover" );
        existMethod = TestInterface.class.getMethod( "hasMockExist", Integer.class );
        existNoArgMethod = TestInterface.class.getMethod( "hasMockExist" );

    }

    private void setupMock() throws Exception {
        setupMethods();
        subjectInfo = new SubjectInfoImpl( SimpleInterface.class );
        final ObjectHandler mockHandler = Mockito.mock( ObjectHandler.class );
        final ObjectHandler noArgHandler = Mockito.mock( ObjectHandler.class );

        setter = Mockito.mock( PredicateInfo.class );
        Mockito.when( setter.getMethodName() ).thenReturn( "setMockSetter" );
        Mockito.doReturn( Integer.class ).when( setter ).getArgumentType();
        Mockito.when( setter.getActionType() ).thenReturn( ActionType.SETTER );
        Mockito.when( setter.getObjectHandler() ).thenReturn( mockHandler );
        Mockito.when( setter.getProperty() )
                .thenReturn( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockSetter" ) );
        Mockito.when( setter.getUriString() ).thenReturn( SubjectInfoImplTest.namespace + "mockSetter" );
        subjectInfo.add( setterMethod, setter );

        getter = Mockito.mock( PredicateInfo.class );
        Mockito.when( getter.getMethodName() ).thenReturn( "getMockGetter" );
        Mockito.doReturn( Integer.class ).when( getter ).getReturnType();
        Mockito.when( getter.getActionType() ).thenReturn( ActionType.GETTER );
        Mockito.when( getter.getObjectHandler() ).thenReturn( mockHandler );
        Mockito.when( getter.getProperty() )
                .thenReturn( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockGetter" ) );
        Mockito.when( getter.getUriString() ).thenReturn( SubjectInfoImplTest.namespace + "mockGetter" );
        subjectInfo.add( getterMethod, getter );

        remover = Mockito.mock( PredicateInfo.class );
        Mockito.when( remover.getMethodName() ).thenReturn( "removeMockRemover" );
        Mockito.doReturn( Integer.class ).when( remover ).getArgumentType();
        Mockito.when( remover.getActionType() ).thenReturn( ActionType.REMOVER );
        Mockito.when( remover.getObjectHandler() ).thenReturn( mockHandler );
        Mockito.when( remover.getProperty() )
                .thenReturn( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockRemover" ) );
        Mockito.when( remover.getUriString() ).thenReturn( SubjectInfoImplTest.namespace + "mockRemover" );
        subjectInfo.add( removerMethod, remover );

        removerNoArg = Mockito.mock( PredicateInfo.class );
        Mockito.when( removerNoArg.getMethodName() ).thenReturn( "removeMockRemover" );
        Mockito.doReturn( void.class ).when( removerNoArg ).getArgumentType();
        Mockito.when( removerNoArg.getActionType() ).thenReturn( ActionType.REMOVER );
        Mockito.when( removerNoArg.getObjectHandler() ).thenReturn( noArgHandler );
        Mockito.when( removerNoArg.getProperty() )
                .thenReturn( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockRemover" ) );
        Mockito.when( removerNoArg.getUriString() ).thenReturn( SubjectInfoImplTest.namespace + "mockRemover" );
        subjectInfo.add( removerNoArgMethod, removerNoArg );

        exist = Mockito.mock( PredicateInfo.class );
        Mockito.when( exist.getMethodName() ).thenReturn( "hasMockExist" );
        Mockito.doReturn( Integer.class ).when( exist ).getArgumentType();
        Mockito.when( exist.getActionType() ).thenReturn( ActionType.EXISTENTIAL );
        Mockito.when( exist.getObjectHandler() ).thenReturn( mockHandler );
        Mockito.when( exist.getProperty() )
                .thenReturn( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockExist" ) );
        Mockito.when( exist.getUriString() ).thenReturn( SubjectInfoImplTest.namespace + "mockExist" );
        subjectInfo.add( existMethod, exist );

        existNoArg = Mockito.mock( PredicateInfo.class );
        Mockito.when( existNoArg.getMethodName() ).thenReturn( "hasMockExist" );
        Mockito.doReturn( void.class ).when( existNoArg ).getArgumentType();
        Mockito.when( existNoArg.getActionType() ).thenReturn( ActionType.EXISTENTIAL );
        Mockito.when( existNoArg.getObjectHandler() ).thenReturn( noArgHandler );
        Mockito.when( existNoArg.getProperty() )
                .thenReturn( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockExist" ) );
        Mockito.when( existNoArg.getUriString() ).thenReturn( SubjectInfoImplTest.namespace + "mockExist" );
        subjectInfo.add( existNoArgMethod, existNoArg );
    }

    @Test
    public void addTest() throws Exception {
        setupMethods();
        subjectInfo = new SubjectInfoImpl( SimpleInterface.class );
        Assertions.assertThrows( IllegalArgumentException.class, () -> subjectInfo.add( existMethod, null ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> subjectInfo.add( null, exist ) );
        final PredicateInfo pi = Mockito.mock( PredicateInfo.class );
        Mockito.when( pi.getMethodName() ).thenReturn( "mockMethod" );
        Mockito.when( pi.getActionType() ).thenReturn( ActionType.EXISTENTIAL );
        subjectInfo.add( existMethod, pi );
    }

    @Test
    public void getPredicateInfoByNameTest() throws Exception {
        setupMock();
        Assertions.assertEquals( setter, subjectInfo.getPredicateInfo( "setMockSetter", Integer.class ) );
        Assertions.assertNull( subjectInfo.getPredicateInfo( "setMockSetter", null ) );
        Assertions.assertEquals( getter, subjectInfo.getPredicateInfo( "getMockGetter", Integer.class ) );
        Assertions.assertNull( subjectInfo.getPredicateInfo( "getMockGetter", null ) );
        Assertions.assertEquals( remover, subjectInfo.getPredicateInfo( "removeMockRemover", Integer.class ) );
        Assertions.assertEquals( removerNoArg, subjectInfo.getPredicateInfo( "removeMockRemover", void.class ) );
        Assertions.assertEquals( exist, subjectInfo.getPredicateInfo( "hasMockExist", Integer.class ) );
        Assertions.assertEquals( existNoArg, subjectInfo.getPredicateInfo( "hasMockExist", void.class ) );
        Assertions.assertNull( subjectInfo.getPredicateInfo( "fakeMethod", null ) );
    }

    @Test
    public void getPredicateInfoByMethodTest() throws Exception {
        setupMock();
        Assertions.assertEquals( setter, subjectInfo.getPredicateInfo( setterMethod ) );
        Assertions.assertEquals( getter, subjectInfo.getPredicateInfo( getterMethod ) );
        Assertions.assertEquals( remover, subjectInfo.getPredicateInfo( removerMethod ) );
        Assertions.assertEquals( removerNoArg, subjectInfo.getPredicateInfo( removerNoArgMethod ) );
        Assertions.assertEquals( exist, subjectInfo.getPredicateInfo( existMethod ) );
        Assertions.assertEquals( existNoArg, subjectInfo.getPredicateInfo( existNoArgMethod ) );
        Assertions.assertNull( subjectInfo.getPredicateInfo( TestInterface.class.getMethod( "fakeMethod" ) ) );
        Assertions.assertNull(
                subjectInfo.getPredicateInfo( TestInterface.class.getMethod( "setMockSetter", Integer[].class ) ) );
        Assertions.assertNull( subjectInfo
                .getPredicateInfo( TestInterface.class.getMethod( "setMockSetter", Integer.class, long.class ) ) );
    }

    @Test
    public void getPredicatePropertyFromMethodTest() throws Exception {
        setupMock();
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockSetter" ),
                subjectInfo.getPredicateProperty( TestInterface.class.getMethod( "setMockSetter", Integer.class ) ) );
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockGetter" ),
                subjectInfo.getPredicateProperty( TestInterface.class.getMethod( "getMockGetter" ) ) );
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockRemover" ),
                subjectInfo
                        .getPredicateProperty( TestInterface.class.getMethod( "removeMockRemover", Integer.class ) ) );
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockRemover" ),
                subjectInfo.getPredicateProperty( TestInterface.class.getMethod( "removeMockRemover" ) ) );
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockExist" ),
                subjectInfo.getPredicateProperty( TestInterface.class.getMethod( "hasMockExist", Integer.class ) ) );
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockExist" ),
                subjectInfo.getPredicateProperty( TestInterface.class.getMethod( "hasMockExist" ) ) );
        Assertions.assertNull( subjectInfo.getPredicateProperty( TestInterface.class.getMethod( "fakeMethod" ) ) );
        Assertions.assertNull(
                subjectInfo.getPredicateProperty( TestInterface.class.getMethod( "setMockSetter", Integer[].class ) ) );
        Assertions.assertNull( subjectInfo
                .getPredicateProperty( TestInterface.class.getMethod( "setMockSetter", Integer.class, long.class ) ) );
    }

    @Test
    public void getPredicatePropertyFromNameTest() throws Exception {
        setupMock();
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockSetter" ),
                subjectInfo.getPredicateProperty( "setMockSetter" ) );
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockGetter" ),
                subjectInfo.getPredicateProperty( "getMockGetter" ) );
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockRemover" ),
                subjectInfo.getPredicateProperty( "removeMockRemover" ) );
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockExist" ),
                subjectInfo.getPredicateProperty( "hasMockExist" ) );
        Assertions.assertNull( subjectInfo.getPredicateProperty( "fakeMethod" ) );
    }

    @Test
    public void getPredicateUriStrFromMethodTest() throws Exception {
        setupMock();
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockSetter",
                subjectInfo.getPredicateUriStr( TestInterface.class.getMethod( "setMockSetter", Integer.class ) ) );
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockGetter",
                subjectInfo.getPredicateUriStr( TestInterface.class.getMethod( "getMockGetter" ) ) );
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockRemover",
                subjectInfo.getPredicateUriStr( TestInterface.class.getMethod( "removeMockRemover", Integer.class ) ) );
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockRemover",
                subjectInfo.getPredicateUriStr( TestInterface.class.getMethod( "removeMockRemover" ) ) );
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockExist",
                subjectInfo.getPredicateUriStr( TestInterface.class.getMethod( "hasMockExist", Integer.class ) ) );
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockExist",
                subjectInfo.getPredicateUriStr( TestInterface.class.getMethod( "hasMockExist" ) ) );
        Assertions.assertNull( subjectInfo.getPredicateUriStr( TestInterface.class.getMethod( "fakeMethod" ) ) );
        Assertions.assertNull(
                subjectInfo.getPredicateUriStr( TestInterface.class.getMethod( "setMockSetter", Integer[].class ) ) );
        Assertions.assertNull( subjectInfo
                .getPredicateUriStr( TestInterface.class.getMethod( "setMockSetter", Integer.class, long.class ) ) );
    }

    @Test
    public void getPredicateUriStrFromNameTest() throws Exception {
        setupMock();
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockSetter",
                subjectInfo.getPredicateUriStr( "setMockSetter" ) );
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockGetter",
                subjectInfo.getPredicateUriStr( "getMockGetter" ) );
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockRemover",
                subjectInfo.getPredicateUriStr( "removeMockRemover" ) );
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockExist",
                subjectInfo.getPredicateUriStr( "hasMockExist" ) );
        Assertions.assertNull( subjectInfo.getPredicateProperty( "fakeMethod" ) );
    }

    @Test
    public void getSubjectTest() {
        subjectInfo = new SubjectInfoImpl( SimpleInterface.class );
        final Subject s = subjectInfo.getSubject();
        Assertions.assertEquals( SubjectInfoImplTest.namespace, s.namespace() );
    }

}
