package org.xenei.jena.entities.impl.manager;

import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.RDFNode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.ObjectHandler;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.datatype.CharacterDatatype;
import org.xenei.jena.entities.impl.datatype.LongDatatype;
import org.xenei.jena.entities.impl.handlers.EntityHandler;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.impl.handlers.VoidHandler;
import org.xenei.jena.entities.testing.iface.TestInterface;

public abstract class AbstractSingleValueTest extends BaseAbstractManagerTest {

    protected AbstractSingleValueTest(final Class<?> classUnderTest) {
        super( classUnderTest );
    }

    @Test
    public void testBoolean()
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "isBool", Boolean.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Boolean.class ) );

        Assertions.assertEquals( "isBool", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Boolean.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "bool", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setBool", Boolean.class );
        Assertions.assertEquals( "setBool", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Boolean.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "bool", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeBool", null );
        Assertions.assertEquals( "removeBool", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "bool", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        final Class<?> c = (Class<?>) Boolean.class.getField( "TYPE" ).get( null );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "isBool", c );
        Assertions.assertEquals( "isBool", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Boolean.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "bool", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setBool", c );
        Assertions.assertEquals( "setBool", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Boolean.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "bool", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

    }

    @Test
    public void testChar()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getChar", Character.class );
        final ObjectHandler handler = new LiteralHandler( CharacterDatatype.INSTANCE );

        Assertions.assertEquals( "getChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setChar", Character.class );
        Assertions.assertEquals( "setChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeChar", null );
        Assertions.assertEquals( "removeChar", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        final Class<?> c = (Class<?>) Character.class.getField( "TYPE" ).get( null );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getChar", c );
        Assertions.assertEquals( "getChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setChar", c );
        Assertions.assertEquals( "setChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

    }

    @Test
    public void testDbl()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getDbl", Double.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Double.class ) );

        Assertions.assertEquals( "getDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setDbl", Double.class );
        Assertions.assertEquals( "setDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeDbl", null );
        Assertions.assertEquals( "removeDbl", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        final Class<?> c = (Class<?>) Double.class.getField( "TYPE" ).get( null );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getDbl", c );
        Assertions.assertEquals( "getDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setDbl", c );
        Assertions.assertEquals( "setDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

    }

    @Test
    public void testEntity() {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getEnt", TestInterface.class );
        final ObjectHandler handler = new EntityHandler( manager, TestInterface.class );

        Assertions.assertEquals( "getEnt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( TestInterface.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "ent", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setEnt", TestInterface.class );
        Assertions.assertEquals( "setEnt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( TestInterface.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "ent", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeEnt", null );
        Assertions.assertEquals( "removeEnt", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "ent", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );
    }

    @Test
    public void testFlt()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getFlt", Float.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Float.class ) );

        Assertions.assertEquals( "getFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setFlt", Float.class );
        Assertions.assertEquals( "setFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeFlt", null );
        Assertions.assertEquals( "removeFlt", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        final Class<?> c = (Class<?>) Float.class.getField( "TYPE" ).get( null );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getFlt", c );
        Assertions.assertEquals( "getFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setFlt", c );
        Assertions.assertEquals( "setFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

    }

    @Test
    public void testInt()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getInt", Integer.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Integer.class ) );

        Assertions.assertEquals( "getInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setInt", Integer.class );
        Assertions.assertEquals( "setInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeInt", null );
        Assertions.assertEquals( "removeInt", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        final Class<?> c = (Class<?>) Integer.class.getField( "TYPE" ).get( null );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getInt", c );
        Assertions.assertEquals( "getInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setInt", c );
        Assertions.assertEquals( "setInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

    }

    @Test
    public void testLng()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getLng", Long.class );
        final ObjectHandler handler = new LiteralHandler( LongDatatype.INSTANCE );

        Assertions.assertEquals( "getLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setLng", Long.class );
        Assertions.assertEquals( "setLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeLng", null );
        Assertions.assertEquals( "removeLng", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        final Class<?> c = (Class<?>) Long.class.getField( "TYPE" ).get( null );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getLng", c );
        Assertions.assertEquals( "getLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setLng", c );
        Assertions.assertEquals( "setLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );
    }

    @Test
    public void testRdf() {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getRDF", RDFNode.class );
        final ObjectHandler handler = new ResourceHandler();

        Assertions.assertEquals( "getRDF", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "rDF", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setRDF", RDFNode.class );
        Assertions.assertEquals( "setRDF", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "rDF", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeRDF", null );
        Assertions.assertEquals( "removeRDF", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "rDF", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );
    }

    @Test
    public void testStr() {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getStr", String.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( String.class ) );

        Assertions.assertEquals( "getStr", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "str", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setStr", String.class );
        Assertions.assertEquals( "setStr", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "str", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeStr", null );
        Assertions.assertEquals( "removeStr", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "str", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );
    }

    @Test
    public void testURI() {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getU", RDFNode.class );

        final ObjectHandler uriHandler = new UriHandler();
        final ObjectHandler rdfHandler = new ResourceHandler();

        Assertions.assertEquals( "getU", pi.getMethodName() );
        Assertions.assertEquals( rdfHandler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setU", String.class );
        Assertions.assertEquals( "setU", pi.getMethodName() );
        Assertions.assertEquals( uriHandler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setU", RDFNode.class );
        Assertions.assertEquals( "setU", pi.getMethodName() );
        Assertions.assertEquals( rdfHandler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeU", null );
        Assertions.assertEquals( "removeU", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getU2", String.class );
        Assertions.assertEquals( "getU2", pi.getMethodName() );
        Assertions.assertEquals( uriHandler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );
    }

}
