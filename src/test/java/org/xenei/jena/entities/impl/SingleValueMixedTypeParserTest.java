package org.xenei.jena.entities.impl;

import static org.mockito.Mockito.mock;

import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.testing.abst.SingleValueMixedTypeTestClass;

public class SingleValueMixedTypeParserTest {

    private Model model;
    private SubjectInfo subjectInfo;
    private EntityManager entityManager;
    private static String namespaceStr = "http://localhost/test#";

    @Before
    public void setup() throws Exception {
        model = ModelFactory.createDefaultModel();
        final EntityManager manager = EntityManagerFactory.create();
        manager.reset();
        manager.read( model.createResource( "http://localhost/SingleValueEntityTests" ),
                SingleValueMixedTypeTestClass.class );
        subjectInfo = manager.getSubjectInfo( SingleValueMixedTypeTestClass.class );
        entityManager = mock( EntityManager.class );
    }

    @After
    public void teardown() {
        model.close();
    }

    @Test
    public void testB() throws Exception {
        final Class<?> c = (Class<?>) Boolean.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "isB", Boolean.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Boolean.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assert.assertEquals( "isB", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "b", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setB", Boolean.class );
        Assert.assertEquals( "setB", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Boolean.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "b", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "isB", c );
        Assert.assertEquals( "isB", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "b", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setB", c );
        Assert.assertEquals( "setB", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Boolean.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "b", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testBool() throws Exception {
        final Class<?> c = (Class<?>) Boolean.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "isBool", Boolean.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Boolean.class ) );

        Assert.assertEquals( "isBool", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Boolean.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "bool", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setBool", Boolean.class );
        Assert.assertEquals( "setBool", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "bool", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "isBool", c );
        Assert.assertEquals( "isBool", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Boolean.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "bool", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setBool", c );
        Assert.assertEquals( "setBool", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "bool", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testC()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Character.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getC", Character.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Character.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assert.assertEquals( "getC", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "c", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setC", Character.class );
        Assert.assertEquals( "setC", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Character.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "c", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getC", c );
        Assert.assertEquals( "getC", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "c", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setC", c );
        Assert.assertEquals( "setC", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Character.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "c", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testChar()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Character.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getChar", Character.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Character.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assert.assertEquals( "getChar", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Character.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "char", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setChar", Character.class );
        Assert.assertEquals( "setChar", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "char", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getChar", c );
        Assert.assertEquals( "getChar", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Character.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "char", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setChar", c );
        Assert.assertEquals( "setChar", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "char", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testD()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Double.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getD", Double.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Double.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assert.assertEquals( "getD", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "d", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setD", Double.class );
        Assert.assertEquals( "setD", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Double.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "d", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getD", c );
        Assert.assertEquals( "getD", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "d", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setD", c );
        Assert.assertEquals( "setD", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Double.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "d", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testDbl()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Double.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getDbl", Double.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Double.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assert.assertEquals( "getDbl", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Double.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "dbl", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setDbl", Double.class );
        Assert.assertEquals( "setDbl", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "dbl", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getDbl", c );
        Assert.assertEquals( "getDbl", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Double.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "dbl", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setDbl", c );
        Assert.assertEquals( "setDbl", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "dbl", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testF()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Float.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getF", Float.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Float.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assert.assertEquals( "getF", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "f", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setF", Float.class );
        Assert.assertEquals( "setF", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Float.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "f", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getF", c );
        Assert.assertEquals( "getF", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "f", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setF", c );
        Assert.assertEquals( "setF", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Float.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "f", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testFlt()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Float.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getFlt", Float.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Float.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assert.assertEquals( "getFlt", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Float.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "flt", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setFlt", Float.class );
        Assert.assertEquals( "setFlt", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "flt", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getFlt", c );
        Assert.assertEquals( "getFlt", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Float.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "flt", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setFlt", c );
        Assert.assertEquals( "setFlt", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "flt", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testI()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Integer.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getI", Integer.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Integer.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assert.assertEquals( "getI", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "i", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setI", Integer.class );
        Assert.assertEquals( "setI", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Integer.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "i", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getI", c );
        Assert.assertEquals( "getI", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "i", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setI", c );
        Assert.assertEquals( "setI", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Integer.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "i", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testInt()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Integer.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getInt", Integer.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Integer.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assert.assertEquals( "getInt", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Integer.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "int", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setInt", Integer.class );
        Assert.assertEquals( "setInt", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "int", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getInt", c );
        Assert.assertEquals( "getInt", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Integer.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "int", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setInt", c );
        Assert.assertEquals( "setInt", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "int", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testL()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Long.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getL", Long.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Long.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assert.assertEquals( "getL", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "l", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setL", Long.class );
        Assert.assertEquals( "setL", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Long.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "l", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getL", c );
        Assert.assertEquals( "getL", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "l", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setL", c );
        Assert.assertEquals( "setL", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Long.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "l", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );
    }

    @Test
    public void testLng()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Long.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getLng", Long.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Long.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assert.assertEquals( "getLng", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Long.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "lng", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setLng", Long.class );
        Assert.assertEquals( "setLng", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "lng", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getLng", c );
        Assert.assertEquals( "getLng", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( Long.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "lng", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setLng", c );
        Assert.assertEquals( "setLng", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "lng", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );
    }

    @Test
    public void testU()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = String.class;

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getU", RDFNode.class );
        final ObjectHandler handler = new UriHandler();
        final ResourceHandler rHandler = new ResourceHandler();

        Assert.assertEquals( "getU", pi.getMethodName() );
        Assert.assertEquals( rHandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( RDFNode.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "u", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setU", String.class );
        Assert.assertEquals( "setU", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( c, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "u", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testU2()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getU2", String.class );
        final ObjectHandler handler = new ResourceHandler();
        final ObjectHandler phandler = new UriHandler();

        Assert.assertEquals( "getU2", pi.getMethodName() );
        Assert.assertEquals( phandler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( String.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "u2", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setU2", RDFNode.class );
        Assert.assertEquals( "setU2", pi.getMethodName() );
        Assert.assertEquals( handler, pi.getObjectHandler( entityManager ) );
        Assert.assertEquals( RDFNode.class, pi.getValueClass() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "u2", pi.getUriString() );
        Assert.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }
}
