package org.xenei.jena.entities.impl;

import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.testing.iface.SingleValueMixedTypeTestInterface;

public class SingleValueMixedTypeParserTest {

    private Model model;
    private SubjectInfo subjectInfo;

    private static String namespaceStr = "http://localhost/test#";

    @BeforeEach
    public void setup() throws Exception {
        model = ModelFactory.createDefaultModel();
        final EntityManager manager = EntityManagerFactory.getEntityManager();
        manager.reset();
        manager.read( model.createResource( "http://localhost/SingleValueEntityTests" ),
                SingleValueMixedTypeTestInterface.class );
        subjectInfo = manager.getSubjectInfo( SingleValueMixedTypeTestInterface.class );
    }

    @AfterEach
    public void teardown() {
        model.close();
    }

    @Test
    public void testB() throws Exception {
        final Class<?> c = (Class<?>) Boolean.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "isB", Boolean.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Boolean.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assertions.assertEquals( "isB", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "b", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setB", Boolean.class );
        Assertions.assertEquals( "setB", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Boolean.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "b", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "isB", c );
        Assertions.assertEquals( "isB", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "b", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setB", c );
        Assertions.assertEquals( "setB", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Boolean.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "b", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testBool() throws Exception {
        final Class<?> c = (Class<?>) Boolean.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "isBool", Boolean.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Boolean.class ) );

        Assertions.assertEquals( "isBool", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Boolean.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "bool", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setBool", Boolean.class );
        Assertions.assertEquals( "setBool", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "bool", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "isBool", c );
        Assertions.assertEquals( "isBool", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Boolean.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "bool", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setBool", c );
        Assertions.assertEquals( "setBool", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "bool", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testC()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Character.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getC", Character.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Character.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assertions.assertEquals( "getC", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "c", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setC", Character.class );
        Assertions.assertEquals( "setC", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "c", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getC", c );
        Assertions.assertEquals( "getC", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "c", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setC", c );
        Assertions.assertEquals( "setC", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "c", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testChar()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Character.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getChar", Character.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Character.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assertions.assertEquals( "getChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "char", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setChar", Character.class );
        Assertions.assertEquals( "setChar", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "char", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getChar", c );
        Assertions.assertEquals( "getChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "char", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setChar", c );
        Assertions.assertEquals( "setChar", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "char", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testD()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Double.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getD", Double.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Double.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assertions.assertEquals( "getD", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "d", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setD", Double.class );
        Assertions.assertEquals( "setD", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "d", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getD", c );
        Assertions.assertEquals( "getD", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "d", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setD", c );
        Assertions.assertEquals( "setD", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "d", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testDbl()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Double.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getDbl", Double.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Double.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assertions.assertEquals( "getDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "dbl", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setDbl", Double.class );
        Assertions.assertEquals( "setDbl", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "dbl", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getDbl", c );
        Assertions.assertEquals( "getDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "dbl", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setDbl", c );
        Assertions.assertEquals( "setDbl", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "dbl", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testF()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Float.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getF", Float.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Float.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assertions.assertEquals( "getF", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "f", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setF", Float.class );
        Assertions.assertEquals( "setF", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "f", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getF", c );
        Assertions.assertEquals( "getF", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "f", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setF", c );
        Assertions.assertEquals( "setF", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "f", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testFlt()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Float.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getFlt", Float.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Float.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assertions.assertEquals( "getFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "flt", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setFlt", Float.class );
        Assertions.assertEquals( "setFlt", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "flt", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getFlt", c );
        Assertions.assertEquals( "getFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "flt", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setFlt", c );
        Assertions.assertEquals( "setFlt", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "flt", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testI()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Integer.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getI", Integer.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Integer.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assertions.assertEquals( "getI", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "i", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setI", Integer.class );
        Assertions.assertEquals( "setI", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "i", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getI", c );
        Assertions.assertEquals( "getI", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "i", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setI", c );
        Assertions.assertEquals( "setI", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "i", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testInt()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Integer.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getInt", Integer.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Integer.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assertions.assertEquals( "getInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "int", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setInt", Integer.class );
        Assertions.assertEquals( "setInt", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "int", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getInt", c );
        Assertions.assertEquals( "getInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "int", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setInt", c );
        Assertions.assertEquals( "setInt", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "int", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testL()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Long.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getL", Long.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Long.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assertions.assertEquals( "getL", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "l", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setL", Long.class );
        Assertions.assertEquals( "setL", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "l", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getL", c );
        Assertions.assertEquals( "getL", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "l", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setL", c );
        Assertions.assertEquals( "setL", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "l", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );
    }

    @Test
    public void testLng()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        final Class<?> c = (Class<?>) Long.class.getField( "TYPE" ).get( null );

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getLng", Long.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Long.class ) );
        final ObjectHandler phandler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( c ) );

        Assertions.assertEquals( "getLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "lng", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setLng", Long.class );
        Assertions.assertEquals( "setLng", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "lng", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getLng", c );
        Assertions.assertEquals( "getLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "lng", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setLng", c );
        Assertions.assertEquals( "setLng", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "lng", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );
    }

    @Test
    public void testU() throws IllegalArgumentException, SecurityException {

        final Class<?> c = String.class;

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getU", RDFNode.class );
        final ObjectHandler handler = new UriHandler();
        final ResourceHandler rHandler = new ResourceHandler();

        Assertions.assertEquals( "getU", pi.getMethodName() );
        Assertions.assertEquals( rHandler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "u", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setU", String.class );
        Assertions.assertEquals( "setU", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( c, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "u", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }

    @Test
    public void testU2() throws IllegalArgumentException, SecurityException {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getU2", String.class );
        final ObjectHandler handler = new ResourceHandler();
        final ObjectHandler phandler = new UriHandler();

        Assertions.assertEquals( "getU2", pi.getMethodName() );
        Assertions.assertEquals( phandler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "u2", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setU2", RDFNode.class );
        Assertions.assertEquals( "setU2", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr + "u2", pi.getUriString() );
        Assertions.assertEquals( SingleValueMixedTypeParserTest.namespaceStr, pi.getNamespace() );

    }
}
