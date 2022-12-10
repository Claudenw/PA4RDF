package org.xenei.jena.entities.impl;

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
    private PredicateInfo setter;
    private PredicateInfo getter;
    private PredicateInfo remover;
    private PredicateInfo removerNoArg;
    private PredicateInfo exist;
    private PredicateInfo existNoArg;

    private void setupMock() {
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
        subjectInfo.add( setter );

        getter = Mockito.mock( PredicateInfo.class );
        Mockito.when( getter.getMethodName() ).thenReturn( "getMockGetter" );
        Mockito.doReturn( Integer.class ).when( getter ).getReturnType();
        Mockito.when( getter.getActionType() ).thenReturn( ActionType.GETTER );
        Mockito.when( getter.getObjectHandler() ).thenReturn( mockHandler );
        Mockito.when( getter.getProperty() )
                .thenReturn( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockGetter" ) );
        Mockito.when( getter.getUriString() ).thenReturn( SubjectInfoImplTest.namespace + "mockGetter" );
        subjectInfo.add( getter );

        remover = Mockito.mock( PredicateInfo.class );
        Mockito.when( remover.getMethodName() ).thenReturn( "removeMockRemover" );
        Mockito.doReturn( Integer.class ).when( remover ).getArgumentType();
        Mockito.when( remover.getActionType() ).thenReturn( ActionType.REMOVER );
        Mockito.when( remover.getObjectHandler() ).thenReturn( mockHandler );
        Mockito.when( remover.getProperty() )
                .thenReturn( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockRemover" ) );
        Mockito.when( remover.getUriString() ).thenReturn( SubjectInfoImplTest.namespace + "mockRemover" );
        subjectInfo.add( remover );

        removerNoArg = Mockito.mock( PredicateInfo.class );
        Mockito.when( removerNoArg.getMethodName() ).thenReturn( "removeMockRemover" );
        Mockito.doReturn( void.class ).when( removerNoArg ).getArgumentType();
        Mockito.when( removerNoArg.getActionType() ).thenReturn( ActionType.REMOVER );
        Mockito.when( removerNoArg.getObjectHandler() ).thenReturn( noArgHandler );
        Mockito.when( removerNoArg.getProperty() )
                .thenReturn( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockRemover" ) );
        Mockito.when( removerNoArg.getUriString() ).thenReturn( SubjectInfoImplTest.namespace + "mockRemover" );
        subjectInfo.add( removerNoArg );

        exist = Mockito.mock( PredicateInfo.class );
        Mockito.when( exist.getMethodName() ).thenReturn( "hasMockExist" );
        Mockito.doReturn( Integer.class ).when( exist ).getArgumentType();
        Mockito.when( exist.getActionType() ).thenReturn( ActionType.EXISTENTIAL );
        Mockito.when( exist.getObjectHandler() ).thenReturn( mockHandler );
        Mockito.when( exist.getProperty() )
                .thenReturn( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockExist" ) );
        Mockito.when( exist.getUriString() ).thenReturn( SubjectInfoImplTest.namespace + "mockExist" );
        subjectInfo.add( exist );

        existNoArg = Mockito.mock( PredicateInfo.class );
        Mockito.when( existNoArg.getMethodName() ).thenReturn( "hasMockExist" );
        Mockito.doReturn( void.class ).when( existNoArg ).getArgumentType();
        Mockito.when( existNoArg.getActionType() ).thenReturn( ActionType.EXISTENTIAL );
        Mockito.when( existNoArg.getObjectHandler() ).thenReturn( noArgHandler );
        Mockito.when( existNoArg.getProperty() )
                .thenReturn( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockExist" ) );
        Mockito.when( existNoArg.getUriString() ).thenReturn( SubjectInfoImplTest.namespace + "mockExist" );
        subjectInfo.add( existNoArg );
    }

    @Test
    public void addTest() {
        subjectInfo = new SubjectInfoImpl( SimpleInterface.class );
        Assertions.assertThrows( IllegalArgumentException.class, () -> subjectInfo.add( null ) );
        final PredicateInfo pi = Mockito.mock( PredicateInfo.class );
        Mockito.when( pi.getMethodName() ).thenReturn( "mockMethod" );
        subjectInfo.add( pi );
    }

    @Test
    public void getPredicateInfoByNameTest() {
        setupMock();
        Assertions.assertEquals( setter, subjectInfo.getPredicateInfo( "setMockSetter", Integer.class ) );
        Assertions.assertNull( subjectInfo.getPredicateInfo( "setMockSetter", null ) );
        Assertions.assertEquals( getter, subjectInfo.getPredicateInfo( "getMockGetter", Integer.class ) );
        Assertions.assertNull( subjectInfo.getPredicateInfo( "getMockGetter", null ) );
        Assertions.assertEquals( remover, subjectInfo.getPredicateInfo( "removeMockRemover", Integer.class ) );
        Assertions.assertEquals( removerNoArg, subjectInfo.getPredicateInfo( "removeMockRemover", null ) );
        Assertions.assertEquals( exist, subjectInfo.getPredicateInfo( "hasMockExist", Integer.class ) );
        Assertions.assertEquals( existNoArg, subjectInfo.getPredicateInfo( "hasMockExist", null ) );
        Assertions.assertNull( subjectInfo.getPredicateInfo( "fakeMethod", null ) );
    }

    @Test
    public void getPredicateInfoByMethodTest() throws Exception {
        setupMock();
        Assertions.assertEquals( setter,
                subjectInfo.getPredicateInfo( MockInterface.class.getMethod( "setMockSetter", Integer.class ) ) );
        Assertions.assertEquals( getter,
                subjectInfo.getPredicateInfo( MockInterface.class.getMethod( "getMockGetter" ) ) );
        Assertions.assertEquals( remover,
                subjectInfo.getPredicateInfo( MockInterface.class.getMethod( "removeMockRemover", Integer.class ) ) );
        Assertions.assertEquals( removerNoArg,
                subjectInfo.getPredicateInfo( MockInterface.class.getMethod( "removeMockRemover" ) ) );
        Assertions.assertEquals( exist,
                subjectInfo.getPredicateInfo( MockInterface.class.getMethod( "hasMockExist", Integer.class ) ) );
        Assertions.assertEquals( existNoArg,
                subjectInfo.getPredicateInfo( MockInterface.class.getMethod( "hasMockExist" ) ) );
        Assertions.assertNull( subjectInfo.getPredicateInfo( MockInterface.class.getMethod( "fakeMethod" ) ) );
        Assertions.assertNull(
                subjectInfo.getPredicateInfo( MockInterface.class.getMethod( "setMockSetter", Integer[].class ) ) );
        Assertions.assertNull( subjectInfo
                .getPredicateInfo( MockInterface.class.getMethod( "setMockSetter", Integer.class, long.class ) ) );
    }

    @Test
    public void getPredicatePropertyFromMethodTest() throws Exception {
        setupMock();
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockSetter" ),
                subjectInfo.getPredicateProperty( MockInterface.class.getMethod( "setMockSetter", Integer.class ) ) );
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockGetter" ),
                subjectInfo.getPredicateProperty( MockInterface.class.getMethod( "getMockGetter" ) ) );
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockRemover" ),
                subjectInfo
                        .getPredicateProperty( MockInterface.class.getMethod( "removeMockRemover", Integer.class ) ) );
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockRemover" ),
                subjectInfo.getPredicateProperty( MockInterface.class.getMethod( "removeMockRemover" ) ) );
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockExist" ),
                subjectInfo.getPredicateProperty( MockInterface.class.getMethod( "hasMockExist", Integer.class ) ) );
        Assertions.assertEquals( ResourceFactory.createProperty( SubjectInfoImplTest.namespace, "mockExist" ),
                subjectInfo.getPredicateProperty( MockInterface.class.getMethod( "hasMockExist" ) ) );
        Assertions.assertNull( subjectInfo.getPredicateProperty( MockInterface.class.getMethod( "fakeMethod" ) ) );
        Assertions.assertNull(
                subjectInfo.getPredicateProperty( MockInterface.class.getMethod( "setMockSetter", Integer[].class ) ) );
        Assertions.assertNull( subjectInfo
                .getPredicateProperty( MockInterface.class.getMethod( "setMockSetter", Integer.class, long.class ) ) );
    }

    @Test
    public void getPredicatePropertyFromNameTest() {
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
                subjectInfo.getPredicateUriStr( MockInterface.class.getMethod( "setMockSetter", Integer.class ) ) );
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockGetter",
                subjectInfo.getPredicateUriStr( MockInterface.class.getMethod( "getMockGetter" ) ) );
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockRemover",
                subjectInfo.getPredicateUriStr( MockInterface.class.getMethod( "removeMockRemover", Integer.class ) ) );
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockRemover",
                subjectInfo.getPredicateUriStr( MockInterface.class.getMethod( "removeMockRemover" ) ) );
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockExist",
                subjectInfo.getPredicateUriStr( MockInterface.class.getMethod( "hasMockExist", Integer.class ) ) );
        Assertions.assertEquals( SubjectInfoImplTest.namespace + "mockExist",
                subjectInfo.getPredicateUriStr( MockInterface.class.getMethod( "hasMockExist" ) ) );
        Assertions.assertNull( subjectInfo.getPredicateUriStr( MockInterface.class.getMethod( "fakeMethod" ) ) );
        Assertions.assertNull(
                subjectInfo.getPredicateUriStr( MockInterface.class.getMethod( "setMockSetter", Integer[].class ) ) );
        Assertions.assertNull( subjectInfo
                .getPredicateUriStr( MockInterface.class.getMethod( "setMockSetter", Integer.class, long.class ) ) );
    }

    @Test
    public void getPredicateUriStrFromNameTest() {
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

    interface MockInterface {
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
}
