package org.xenei.jena.entities.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.ObjectHandler;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public class SubjectInfoImplTest {

    private static String namespace= "http://example.com/";
    private SubjectInfoImpl subjectInfo;
    private PredicateInfo setter;
    private PredicateInfo getter;
    private PredicateInfo remover;
    private PredicateInfo removerNoArg;
    private PredicateInfo exist;
    private PredicateInfo existNoArg;
    
    
    private void setupMock() {
        subjectInfo = new SubjectInfoImpl( SimpleInterface.class );
        ObjectHandler mockHandler = mock(ObjectHandler.class);
        ObjectHandler noArgHandler = mock(ObjectHandler.class);
        
        setter = mock( PredicateInfo.class );
        when( setter.getMethodName() ).thenReturn( "setMockSetter");
        doReturn(Integer.class).when(setter).getValueClass();
        when( setter.getActionType() ).thenReturn( ActionType.SETTER );
        when( setter.getObjectHandler()).thenReturn( mockHandler );
        when( setter.getProperty()).thenReturn( ResourceFactory.createProperty( namespace, "mockSetter" ));
        when( setter.getUriString()).thenReturn( namespace+"mockSetter" );
        subjectInfo.add( setter );
        
        getter = mock( PredicateInfo.class );
        when( getter.getMethodName() ).thenReturn( "getMockGetter");
        doReturn(Integer.class).when(getter).getValueClass();
        when( getter.getActionType() ).thenReturn( ActionType.GETTER );
        when( getter.getObjectHandler()).thenReturn( mockHandler );
        when( getter.getProperty()).thenReturn( ResourceFactory.createProperty( namespace, "mockGetter" ));
        when( getter.getUriString()).thenReturn( namespace+"mockGetter" );
        subjectInfo.add( getter );
        
        remover = mock( PredicateInfo.class );
        when( remover.getMethodName() ).thenReturn( "removeMockRemover");
        doReturn(Integer.class).when(remover).getValueClass();
        when( remover.getActionType() ).thenReturn( ActionType.REMOVER );
        when( remover.getObjectHandler()).thenReturn( mockHandler );
        when( remover.getProperty()).thenReturn( ResourceFactory.createProperty( namespace, "mockRemover" ));
        when( remover.getUriString()).thenReturn( namespace+"mockRemover" );
        subjectInfo.add( remover );
        
        removerNoArg = mock( PredicateInfo.class );
        when( removerNoArg.getMethodName() ).thenReturn( "removeMockRemover");
        when( removerNoArg.getValueClass() ).thenReturn( null );
        when( removerNoArg.getActionType() ).thenReturn( ActionType.REMOVER );
        when( removerNoArg.getObjectHandler()).thenReturn( noArgHandler );
        when( removerNoArg.getProperty()).thenReturn( ResourceFactory.createProperty( namespace, "mockRemover" ));
        when( removerNoArg.getUriString()).thenReturn( namespace+"mockRemover" );
        subjectInfo.add( removerNoArg );
        
        exist = mock( PredicateInfo.class );
        when( exist.getMethodName() ).thenReturn( "hasMockExist");
        doReturn(Integer.class).when(exist).getValueClass();
        when( exist.getActionType() ).thenReturn( ActionType.EXISTENTIAL );
        when( exist.getObjectHandler()).thenReturn( mockHandler );
        when( exist.getProperty()).thenReturn( ResourceFactory.createProperty( namespace, "mockExist" ));
        when( exist.getUriString()).thenReturn( namespace+"mockExist" );
        subjectInfo.add( exist );
        
        existNoArg = mock( PredicateInfo.class );
        when( existNoArg.getMethodName() ).thenReturn( "hasMockExist");
        doReturn(null).when(existNoArg).getValueClass();
        when( existNoArg.getActionType() ).thenReturn( ActionType.EXISTENTIAL );
        when( existNoArg.getObjectHandler()).thenReturn( noArgHandler );
        when( existNoArg.getProperty()).thenReturn( ResourceFactory.createProperty( namespace, "mockExist" ));
        when( existNoArg.getUriString()).thenReturn( namespace+"mockExist" );
        subjectInfo.add( existNoArg );
    }
    
    @Test
    public void addTest() {
        subjectInfo = new SubjectInfoImpl( SimpleInterface.class );
        Assertions.assertThrows( IllegalArgumentException.class, () -> subjectInfo.add( null ) );
        PredicateInfo pi = mock( PredicateInfo.class );
        when( pi.getMethodName() ).thenReturn( "mockMethod");
        subjectInfo.add( pi );
    }
    
    @Test
    public void getPredicateInfoByNameTest() {
        setupMock();
        assertEquals( setter, subjectInfo.getPredicateInfo( "setMockSetter", Integer.class ) );
        assertNull( subjectInfo.getPredicateInfo( "setMockSetter", null ) );
        assertEquals( getter, subjectInfo.getPredicateInfo( "getMockGetter", Integer.class ) );
        assertNull( subjectInfo.getPredicateInfo( "getMockGetter", null ) );
        assertEquals( remover, subjectInfo.getPredicateInfo( "removeMockRemover", Integer.class ) );
        assertEquals( removerNoArg, subjectInfo.getPredicateInfo( "removeMockRemover", null ) );
        assertEquals( exist, subjectInfo.getPredicateInfo( "hasMockExist", Integer.class ) );
        assertEquals( existNoArg, subjectInfo.getPredicateInfo( "hasMockExist", null ) );
        assertNull( subjectInfo.getPredicateInfo( "fakeMethod", null ));
    }
    
    
    @Test
    public void getPredicateInfoByMethodTest() throws Exception {
        setupMock();
        assertEquals( setter, subjectInfo.getPredicateInfo( MockInterface.class.getMethod( "setMockSetter", Integer.class )) );
        assertEquals( getter, subjectInfo.getPredicateInfo( MockInterface.class.getMethod( "getMockGetter")) );
        assertEquals( remover, subjectInfo.getPredicateInfo( MockInterface.class.getMethod( "removeMockRemover", Integer.class )) );
        assertEquals( removerNoArg, subjectInfo.getPredicateInfo( MockInterface.class.getMethod( "removeMockRemover") ) );
        assertEquals( exist, subjectInfo.getPredicateInfo( MockInterface.class.getMethod( "hasMockExist", Integer.class ) ));
        assertEquals( existNoArg, subjectInfo.getPredicateInfo( MockInterface.class.getMethod( "hasMockExist") ) );
        assertNull( subjectInfo.getPredicateInfo( MockInterface.class.getMethod("fakeMethod") ));
        assertNull( subjectInfo.getPredicateInfo( MockInterface.class.getMethod("setMockSetter", Integer[].class)) );
        assertNull( subjectInfo.getPredicateInfo( MockInterface.class.getMethod("setMockSetter", Integer.class, long.class) ));
    }
    
    @Test
    public void getPredicatePropertyFromMethodTest() throws Exception {
        setupMock();
        assertEquals( ResourceFactory.createProperty( namespace, "mockSetter" ), subjectInfo.getPredicateProperty( MockInterface.class.getMethod( "setMockSetter", Integer.class )) );
        assertEquals( ResourceFactory.createProperty( namespace, "mockGetter" ), subjectInfo.getPredicateProperty( MockInterface.class.getMethod( "getMockGetter")) );
        assertEquals( ResourceFactory.createProperty( namespace, "mockRemover" ), subjectInfo.getPredicateProperty( MockInterface.class.getMethod( "removeMockRemover", Integer.class )) );
        assertEquals( ResourceFactory.createProperty( namespace, "mockRemover" ), subjectInfo.getPredicateProperty( MockInterface.class.getMethod( "removeMockRemover") ) );
        assertEquals( ResourceFactory.createProperty( namespace, "mockExist" ), subjectInfo.getPredicateProperty( MockInterface.class.getMethod( "hasMockExist", Integer.class ) ));
        assertEquals( ResourceFactory.createProperty( namespace, "mockExist" ), subjectInfo.getPredicateProperty( MockInterface.class.getMethod( "hasMockExist") ) );
        assertNull( subjectInfo.getPredicateProperty( MockInterface.class.getMethod("fakeMethod") ));
        assertNull( subjectInfo.getPredicateProperty( MockInterface.class.getMethod("setMockSetter", Integer[].class)) );
        assertNull( subjectInfo.getPredicateProperty( MockInterface.class.getMethod("setMockSetter", Integer.class, long.class) ));
    }
    
    @Test
    public void getPredicatePropertyFromNameTest() {
        setupMock();
        assertEquals( ResourceFactory.createProperty( namespace, "mockSetter" ), subjectInfo.getPredicateProperty( "setMockSetter" ));
        assertEquals( ResourceFactory.createProperty( namespace, "mockGetter" ), subjectInfo.getPredicateProperty( "getMockGetter") );
        assertEquals( ResourceFactory.createProperty( namespace, "mockRemover" ), subjectInfo.getPredicateProperty( "removeMockRemover") );
        assertEquals( ResourceFactory.createProperty( namespace, "mockExist" ), subjectInfo.getPredicateProperty( "hasMockExist" ));
        assertNull( subjectInfo.getPredicateProperty( "fakeMethod") );
    }
    
    @Test
    public void getPredicateUriStrFromMethodTest() throws Exception {
        setupMock();
        assertEquals( namespace+"mockSetter", subjectInfo.getPredicateUriStr( MockInterface.class.getMethod( "setMockSetter", Integer.class )) );
        assertEquals( namespace+"mockGetter", subjectInfo.getPredicateUriStr( MockInterface.class.getMethod( "getMockGetter")) );
        assertEquals( namespace+"mockRemover", subjectInfo.getPredicateUriStr( MockInterface.class.getMethod( "removeMockRemover", Integer.class )) );
        assertEquals( namespace+"mockRemover", subjectInfo.getPredicateUriStr( MockInterface.class.getMethod( "removeMockRemover") ) );
        assertEquals( namespace+"mockExist", subjectInfo.getPredicateUriStr( MockInterface.class.getMethod( "hasMockExist", Integer.class ) ));
        assertEquals( namespace+"mockExist", subjectInfo.getPredicateUriStr( MockInterface.class.getMethod( "hasMockExist") ) );
        assertNull( subjectInfo.getPredicateUriStr( MockInterface.class.getMethod("fakeMethod") ));
        assertNull( subjectInfo.getPredicateUriStr( MockInterface.class.getMethod("setMockSetter", Integer[].class)) );
        assertNull( subjectInfo.getPredicateUriStr( MockInterface.class.getMethod("setMockSetter", Integer.class, long.class) ));
    }
   
    @Test
    public void getPredicateUriStrFromNameTest() {
        setupMock();
        assertEquals( namespace+"mockSetter", subjectInfo.getPredicateUriStr( "setMockSetter" ));
        assertEquals( namespace+"mockGetter", subjectInfo.getPredicateUriStr( "getMockGetter") );
        assertEquals( namespace+"mockRemover", subjectInfo.getPredicateUriStr( "removeMockRemover") );
        assertEquals( namespace+"mockExist", subjectInfo.getPredicateUriStr( "hasMockExist" ));
        assertNull( subjectInfo.getPredicateProperty( "fakeMethod") );
    }
    
    
    @Test
    public void getSubjectTest() {
        subjectInfo = new SubjectInfoImpl( SimpleInterface.class );
        Subject s = subjectInfo.getSubject();
        assertEquals( namespace, s.namespace());
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
