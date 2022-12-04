package org.xenei.jena.entities.impl.parser;

import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.RDFNode;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.CharacterData;
import org.xenei.jena.entities.EntityManagerFactory;
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

public abstract class AbstractSingleValueObjectTest extends BaseAbstractParserTest {

    private static String NS = "http://localhost/test#";

    protected AbstractSingleValueObjectTest(final Class<?> classUnderTest) {
        super( classUnderTest );
    }

    @Test
    public void testBoolean() throws Exception {
        Method m = classUnderTest.getMethod( "isBool" );
        PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );

        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Boolean.class ) );

        Assertions.assertEquals( "isBool", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Boolean.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "bool", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "setBool", Boolean.class );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "setBool", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Boolean.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "bool", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "removeBool" );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "removeBool", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "bool", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );
    }

    @Test
    public void testChar() throws Exception {
        Method m = classUnderTest.getMethod( "getChar" );
        PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );

        final ObjectHandler handler = new LiteralHandler( CharacterDatatype.INSTANCE );

        Assertions.assertEquals( "getChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "setChar", Character.class );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "setChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "removeChar" );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "removeChar", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );
    }

    @Test
    public void testDbl() throws Exception {
        Method m = classUnderTest.getMethod( "getDbl" );
        PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );

        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Double.class ) );

        Assertions.assertEquals( "getDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "setDbl", Double.class );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "setDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "removeDbl" );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "removeDbl", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );
    }

    @Test
    public void testEntity() throws Exception {
        Method m = classUnderTest.getMethod( "getEnt" );
        PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );

        final ObjectHandler handler = new EntityHandler( EntityManagerFactory.getEntityManager(), TestInterface.class );

        Assertions.assertEquals( "getEnt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( TestInterface.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "ent", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "setEnt", TestInterface.class );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "setEnt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( TestInterface.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "ent", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "removeEnt" );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "removeEnt", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "ent", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );
    }

    @Test
    public void testFlt() throws Exception {
        Method m = classUnderTest.getMethod( "getFlt" );
        PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Float.class ) );

        Assertions.assertEquals( "getFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "setFlt", Float.class );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "setFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "removeFlt" );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "removeFlt", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

    }

    @Test
    public void testInt() throws Exception {

        Method m = classUnderTest.getMethod( "getInt" );
        PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Integer.class ) );

        Assertions.assertEquals( "getInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "setInt", Integer.class );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "setInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "removeInt" );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "removeInt", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

    }

    @Test
    public void testLng() throws Exception {
        Method m = classUnderTest.getMethod( "getLng" );
        PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );
        final ObjectHandler handler = new LiteralHandler( LongDatatype.INSTANCE );

        Assertions.assertEquals( "getLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "setLng", Long.class );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "setLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "removeLng" );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "removeLng", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

    }

    @Test
    public void testRdf() throws Exception {
        Method m = classUnderTest.getMethod( "getRDF" );
        PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );
        final ObjectHandler handler = new ResourceHandler();

        Assertions.assertEquals( "getRDF", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "rDF", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "setRDF", RDFNode.class );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "setRDF", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "rDF", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "removeRDF" );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "removeRDF", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "rDF", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );
    }

    @Test
    public void testStr() throws Exception {
        Method m = classUnderTest.getMethod( "getStr" );
        PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( String.class ) );

        Assertions.assertEquals( "getStr", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "str", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "setStr", String.class );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "setStr", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "str", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "removeStr" );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "removeStr", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "str", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );
    }

    @Test
    public void testURI() throws Exception {
        Method m = classUnderTest.getMethod( "getU" );
        PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );

        final ObjectHandler uriHandler = new UriHandler();
        final ObjectHandler rdfHandler = new ResourceHandler();

        Assertions.assertEquals( "getU", pi.getMethodName() );
        Assertions.assertEquals( rdfHandler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "setU", String.class );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "setU", pi.getMethodName() );
        Assertions.assertEquals( uriHandler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "setU", RDFNode.class );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "setU", pi.getMethodName() );
        Assertions.assertEquals( rdfHandler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "removeU" );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "removeU", pi.getMethodName() );
        Assertions.assertEquals( new VoidHandler(), pi.getObjectHandler() );
        Assertions.assertEquals( null, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );

        m = classUnderTest.getMethod( "getU2" );
        pi = (PredicateInfoImpl) parser.parse( m );
        Assertions.assertEquals( "getU2", pi.getMethodName() );
        Assertions.assertEquals( uriHandler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( AbstractSingleValueObjectTest.NS, pi.getNamespace() );
    }

}
