package org.xenei.jena.entities.impl.manager;

import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.RDFNode;

import java.util.List;
import java.util.Queue;
import java.util.Set;

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
import org.xenei.jena.entities.testing.iface.TestInterface;

public abstract class AbstractCollectionValueTest extends BaseAbstractManagerTest {

    protected AbstractCollectionValueTest(final Class<?> classUnderTest) {
        super( classUnderTest );
        // TODO Auto-generated constructor stub
    }

    @Test
    public void testBoolean()
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addBool", Boolean.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Boolean.class ) );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( "addBool", pi.getMethodName() );
        Assertions.assertEquals( Boolean.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "bool", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeBool", Boolean.class );

        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( "removeBool", pi.getMethodName() );
        Assertions.assertEquals( Boolean.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "bool", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        final Class<?> c = (Class<?>) Boolean.class.getField( "TYPE" ).get( null );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addBool", c );

        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( "addBool", pi.getMethodName() );
        Assertions.assertEquals( Boolean.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "bool", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeBool", c );

        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( "removeBool", pi.getMethodName() );
        Assertions.assertEquals( Boolean.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "bool", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

    }

    @Test
    public void testChar()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getChar", List.class );
        final ObjectHandler handler = new LiteralHandler( CharacterDatatype.INSTANCE );
        Assertions.assertEquals( "getChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( List.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addChar", Character.class );
        Assertions.assertEquals( "addChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeChar", Character.class );
        Assertions.assertEquals( "removeChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasChar", Character.class );
        Assertions.assertEquals( "hasChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        final Class<?> c = (Class<?>) Character.class.getField( "TYPE" ).get( null );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addChar", c );
        Assertions.assertEquals( "addChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeChar", c );
        Assertions.assertEquals( "removeChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasChar", c );
        Assertions.assertEquals( "hasChar", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Character.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "char", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );
    }

    @Test
    public void testDbl()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getDbl", Queue.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Double.class ) );
        Assertions.assertEquals( "getDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Queue.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addDbl", Double.class );
        Assertions.assertEquals( "addDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeDbl", Double.class );
        Assertions.assertEquals( "removeDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasDbl", Double.class );
        Assertions.assertEquals( "hasDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        final Class<?> c = (Class<?>) Double.class.getField( "TYPE" ).get( null );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addDbl", c );
        Assertions.assertEquals( "addDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeDbl", c );
        Assertions.assertEquals( "removeDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasDbl", c );
        Assertions.assertEquals( "hasDbl", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Double.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "dbl", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

    }

    @Test
    public void testEntity() {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getEnt", Queue.class );
        Assertions.assertEquals( "getEnt", pi.getMethodName() );
        final ObjectHandler handler = new EntityHandler( TestInterface.class );

        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Queue.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "ent", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addEnt", TestInterface.class );
        Assertions.assertEquals( "addEnt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( TestInterface.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "ent", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeEnt", TestInterface.class );
        Assertions.assertEquals( "removeEnt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( TestInterface.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "ent", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasEnt", TestInterface.class );
        Assertions.assertEquals( "hasEnt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( TestInterface.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "ent", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );
    }

    @Test
    public void testFlt()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getFlt", Set.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Float.class ) );

        Assertions.assertEquals( "getFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Set.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addFlt", Float.class );
        Assertions.assertEquals( "addFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeFlt", Float.class );
        Assertions.assertEquals( "removeFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasFlt", Float.class );
        Assertions.assertEquals( "hasFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        final Class<?> c = (Class<?>) Float.class.getField( "TYPE" ).get( null );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addFlt", c );
        Assertions.assertEquals( "addFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeFlt", c );
        Assertions.assertEquals( "removeFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasFlt", c );
        Assertions.assertEquals( "hasFlt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Float.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "flt", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );
    }

    @Test
    public void testInt()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {

        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getInt", Queue.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( Integer.class ) );

        Assertions.assertEquals( "getInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Queue.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addInt", Integer.class );
        Assertions.assertEquals( "addInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeInt", Integer.class );
        Assertions.assertEquals( "removeInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasInt", Integer.class );
        Assertions.assertEquals( "hasInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        final Class<?> c = (Class<?>) Integer.class.getField( "TYPE" ).get( null );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addInt", c );
        Assertions.assertEquals( "addInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeInt", c );
        Assertions.assertEquals( "removeInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasInt", c );
        Assertions.assertEquals( "hasInt", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Integer.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "int", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

    }

    @Test
    public void testLng()
            throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getLng", List.class );
        final ObjectHandler handler = new LiteralHandler( LongDatatype.INSTANCE );

        Assertions.assertEquals( "getLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( List.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addLng", Long.class );
        Assertions.assertEquals( "addLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeLng", Long.class );
        Assertions.assertEquals( "removeLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasLng", Long.class );
        Assertions.assertEquals( "hasLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        final Class<?> c = (Class<?>) Long.class.getField( "TYPE" ).get( null );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addLng", c );
        Assertions.assertEquals( "addLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeLng", c );
        Assertions.assertEquals( "removeLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasLng", c );
        Assertions.assertEquals( "hasLng", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Long.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "lng", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

    }

    @Test
    public void testRdf() {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getRDF", List.class );
        final ObjectHandler handler = new ResourceHandler();

        Assertions.assertEquals( "getRDF", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( List.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "rDF", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addRDF", RDFNode.class );
        Assertions.assertEquals( "addRDF", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "rDF", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeRDF", RDFNode.class );
        Assertions.assertEquals( "removeRDF", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "rDF", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasRDF", RDFNode.class );
        Assertions.assertEquals( "hasRDF", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "rDF", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );
    }

    @Test
    public void testStr() {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getStr", Set.class );
        final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( String.class ) );

        Assertions.assertEquals( "getStr", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( Set.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "str", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addStr", String.class );
        Assertions.assertEquals( "addStr", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "str", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeStr", String.class );
        Assertions.assertEquals( "removeStr", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "str", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasStr", String.class );
        Assertions.assertEquals( "hasStr", pi.getMethodName() );
        Assertions.assertEquals( handler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "str", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );
    }

    @Test
    public void testURI() {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getU", Set.class );
        final ObjectHandler resHandler = new ResourceHandler();
        final ObjectHandler uriHandler = new UriHandler();

        Assertions.assertEquals( "getU", pi.getMethodName() );
        Assertions.assertEquals( resHandler, pi.getObjectHandler() );
        Assertions.assertEquals( Set.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addU", String.class );
        Assertions.assertEquals( "addU", pi.getMethodName() );
        Assertions.assertEquals( uriHandler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addU", RDFNode.class );
        Assertions.assertEquals( "addU", pi.getMethodName() );
        Assertions.assertEquals( resHandler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeU", RDFNode.class );
        Assertions.assertEquals( "removeU", pi.getMethodName() );
        Assertions.assertEquals( resHandler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeU", String.class );
        Assertions.assertEquals( "removeU", pi.getMethodName() );
        Assertions.assertEquals( uriHandler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasU", String.class );
        Assertions.assertEquals( "hasU", pi.getMethodName() );
        Assertions.assertEquals( uriHandler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasU", RDFNode.class );
        Assertions.assertEquals( "hasU", pi.getMethodName() );
        Assertions.assertEquals( resHandler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getU2", List.class );
        Assertions.assertEquals( "getU2", pi.getMethodName() );
        Assertions.assertEquals( uriHandler, pi.getObjectHandler() );
        Assertions.assertEquals( List.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );
    }

    @Test
    public void testURIOrdering() {
        PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getU3", Queue.class );
        final ObjectHandler resHandler = new ResourceHandler();
        final ObjectHandler uriHandler = new UriHandler();

        Assertions.assertEquals( "getU3", pi.getMethodName() );
        Assertions.assertEquals( resHandler, pi.getObjectHandler() );
        Assertions.assertEquals( Queue.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u3", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addU3", String.class );
        Assertions.assertEquals( "addU3", pi.getMethodName() );
        Assertions.assertEquals( uriHandler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u3", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "addU3", RDFNode.class );
        Assertions.assertEquals( "addU3", pi.getMethodName() );
        Assertions.assertEquals( resHandler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u3", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeU3", RDFNode.class );
        Assertions.assertEquals( "removeU3", pi.getMethodName() );
        Assertions.assertEquals( resHandler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u3", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeU3", String.class );
        Assertions.assertEquals( "removeU3", pi.getMethodName() );
        Assertions.assertEquals( uriHandler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u3", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasU3", String.class );
        Assertions.assertEquals( "hasU3", pi.getMethodName() );
        Assertions.assertEquals( uriHandler, pi.getObjectHandler() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u3", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasU3", RDFNode.class );
        Assertions.assertEquals( "hasU3", pi.getMethodName() );
        Assertions.assertEquals( resHandler, pi.getObjectHandler() );
        Assertions.assertEquals( RDFNode.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u3", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getU4", Set.class );
        Assertions.assertEquals( "getU4", pi.getMethodName() );
        Assertions.assertEquals( uriHandler, pi.getObjectHandler() );
        Assertions.assertEquals( Set.class, pi.getValueClass() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS + "u3", pi.getUriString() );
        Assertions.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );
    }

}
