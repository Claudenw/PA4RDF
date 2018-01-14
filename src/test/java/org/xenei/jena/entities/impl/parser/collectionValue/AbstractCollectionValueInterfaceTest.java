package org.xenei.jena.entities.impl.parser.collectionValue;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.jena.entities.impl.datatype.CharacterDatatype;
import org.xenei.jena.entities.impl.handlers.CollectionHandler;
import org.xenei.jena.entities.impl.handlers.EntityHandler;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.impl.parser.AbstractMethodParserTest;
import org.xenei.jena.entities.testing.iface.TestInterface;

public abstract class AbstractCollectionValueInterfaceTest extends AbstractMethodParserTest {

    protected Method addBool;
    protected Method getBool;
    protected Method hasBool;
    protected Method removeBool;

    protected Method addChar;
    protected Method getChar;
    protected Method hasChar;
    protected Method removeChar;

    protected Method addDbl;
    protected Method getDbl;
    protected Method hasDbl;
    protected Method removeDbl;

    protected Method addEnt;// (TestLngerface b);
    protected Method getEnt;
    protected Method hasEnt;
    protected Method removeEnt;

    protected Method addFlt;
    protected Method getFlt;
    protected Method hasFlt;
    protected Method removeFlt;

    protected Method addInt;
    protected Method getInt;
    protected Method hasInt;
    protected Method removeInt;

    protected Method addLng;
    protected Method getLng;
    protected Method hasLng;
    protected Method removeLng;

    protected Method addRDF;
    protected Method getRDF;
    protected Method hasRDF;
    protected Method removeRDF;

    protected Method addStr;
    protected Method getStr;
    protected Method hasStr;
    protected Method removeStr;

    protected Method addU_R;
    protected Method addU_S;
    protected Method getU;
    protected Method hasU_R;
    protected Method hasU_S;
    protected Method removeU_R;
    protected Method removeU_S;

    protected Method addU3_R;
    protected Method addU3_S;
    protected Method getU3;
    protected Method hasU3_R;
    protected Method hasU3_S;
    protected Method removeU3_R;
    protected Method removeU3_S;

    protected Method getU4;

    static {
        EntityManagerImpl.registerTypes();
    }

    protected AbstractCollectionValueInterfaceTest(Class<?> underTest) throws NoSuchMethodException, SecurityException {
        super( underTest );
        NS = "http://localhost/test#";

        addBool = underTest.getMethod( "addBool", Boolean.class );
        PIMap.put( addBool, mockPredicateInfo( addBool, "bool", ActionType.SETTER, Boolean.class, 0 ) );
        OMMap.put( addBool, new LiteralHandler( XSDDatatype.XSDboolean ) );
        getBool = underTest.getMethod( "getBool" );
        PIMap.put( getBool, mockPredicateInfo( getBool, "bool", ActionType.GETTER, Set.class, 0 ) );
        OMMap.put( getBool, new LiteralHandler( XSDDatatype.XSDboolean ) );
        hasBool = underTest.getMethod( "hasBool", Boolean.class );
        PIMap.put( hasBool, mockPredicateInfo( hasBool, "bool", ActionType.EXISTENTIAL, Boolean.class, 0 ) );
        OMMap.put( hasBool, new LiteralHandler( XSDDatatype.XSDboolean ) );
        removeBool = underTest.getMethod( "removeBool", Boolean.class );
        PIMap.put( removeBool, mockPredicateInfo( removeBool, "bool", ActionType.REMOVER, Boolean.class, 0 ) );
        OMMap.put( removeBool, new LiteralHandler( XSDDatatype.XSDboolean ) );
        addCount.put( "addBool", Integer.valueOf( 1 ) );

        addChar = underTest.getMethod( "addChar", Character.class );
        PIMap.put( addChar, mockPredicateInfo( addChar, "char", ActionType.SETTER, Character.class, 0 ) );
        OMMap.put( addChar, new LiteralHandler( CharacterDatatype.INSTANCE ) );
        getChar = underTest.getMethod( "getChar" );
        PIMap.put( getChar, mockPredicateInfo( getChar, "char", ActionType.GETTER, List.class, 0 ) );
        OMMap.put( getChar, new LiteralHandler( CharacterDatatype.INSTANCE ) );
        hasChar = underTest.getMethod( "hasChar", Character.class );
        PIMap.put( hasChar, mockPredicateInfo( hasChar, "char", ActionType.EXISTENTIAL, Boolean.class, 0 ) );
        OMMap.put( hasChar, new LiteralHandler( CharacterDatatype.INSTANCE ) );
        removeChar = underTest.getMethod( "removeChar", Character.class );
        PIMap.put( removeChar, mockPredicateInfo( removeChar, "char", ActionType.REMOVER, Character.class, 0 ) );
        OMMap.put( removeChar, new LiteralHandler( CharacterDatatype.INSTANCE ) );
        addCount.put( "addChar", Integer.valueOf( 1 ) );

        addDbl = underTest.getMethod( "addDbl", Double.class );
        PIMap.put( addDbl, mockPredicateInfo( addDbl, "dbl", ActionType.SETTER, Double.class, 0 ) );
        OMMap.put( addDbl, new LiteralHandler( XSDDatatype.XSDdouble ) );
        addCount.put( "addDbl", Integer.valueOf( 1 ) );
        getDbl = underTest.getMethod( "getDbl" );
        PIMap.put( getDbl, mockPredicateInfo( getDbl, "dbl", ActionType.GETTER, Queue.class, 0 ) );
        OMMap.put( getDbl, new LiteralHandler( XSDDatatype.XSDdouble ) );
        hasDbl = underTest.getMethod( "hasDbl", Double.class );
        PIMap.put( hasDbl, mockPredicateInfo( hasDbl, "dbl", ActionType.EXISTENTIAL, Boolean.class, 0 ) );
        OMMap.put( hasDbl, new LiteralHandler( XSDDatatype.XSDdouble ) );
        removeDbl = underTest.getMethod( "removeDbl", Double.class );
        PIMap.put( removeDbl, mockPredicateInfo( removeDbl, "dbl", ActionType.REMOVER, Double.class, 0 ) );
        OMMap.put( removeDbl, new LiteralHandler( XSDDatatype.XSDdouble ) );
        addCount.put( "addDbl", Integer.valueOf( 1 ) );

        addEnt = underTest.getMethod( "addEnt", TestInterface.class );
        PIMap.put( addEnt, mockPredicateInfo( addEnt, "ent", ActionType.SETTER, TestInterface.class, 0 ) );
        OMMap.put( addEnt, new EntityHandler( null, TestInterface.class ) );
        getEnt = underTest.getMethod( "getEnt" );
        PIMap.put( getEnt, mockPredicateInfo( getEnt, "ent", ActionType.GETTER, Queue.class, 0 ) );
        OMMap.put( getEnt, new CollectionHandler( new EntityHandler( null, TestInterface.class ), Queue.class ) );
        hasEnt = underTest.getMethod( "hasEnt", TestInterface.class );
        PIMap.put( hasEnt, mockPredicateInfo( hasEnt, "ent", ActionType.EXISTENTIAL, Boolean.class, 0 ) );
        OMMap.put( hasEnt, new EntityHandler( null, Boolean.class ) );
        removeEnt = underTest.getMethod( "removeEnt", TestInterface.class );
        PIMap.put( removeEnt, mockPredicateInfo( removeEnt, "ent", ActionType.REMOVER, TestInterface.class, 0 ) );
        OMMap.put( removeEnt, new EntityHandler( null, TestInterface.class ) );
        addCount.put( "addEnt", Integer.valueOf( 1 ) );

        addFlt = underTest.getMethod( "addFlt", Float.class );
        PIMap.put( addFlt, mockPredicateInfo( addFlt, "flt", ActionType.SETTER, Float.class, 0 ) );
        OMMap.put( addFlt, new LiteralHandler( XSDDatatype.XSDfloat ) );
        getFlt = underTest.getMethod( "getFlt" );
        PIMap.put( getFlt, mockPredicateInfo( getFlt, "flt", ActionType.GETTER, Set.class, 0 ) );
        OMMap.put( getFlt, new LiteralHandler( XSDDatatype.XSDfloat ) );
        hasFlt = underTest.getMethod( "hasFlt", Float.class );
        PIMap.put( hasFlt, mockPredicateInfo( hasFlt, "flt", ActionType.EXISTENTIAL, Boolean.class, 0 ) );
        OMMap.put( hasFlt, new LiteralHandler( XSDDatatype.XSDfloat ) );
        removeFlt = underTest.getMethod( "removeFlt", Float.class );
        PIMap.put( removeFlt, mockPredicateInfo( removeFlt, "flt", ActionType.REMOVER, Float.class, 0 ) );
        OMMap.put( removeFlt, new LiteralHandler( XSDDatatype.XSDfloat ) );
        addCount.put( "addFlt", Integer.valueOf( 1 ) );

        addInt = underTest.getMethod( "addInt", Integer.class );
        PIMap.put( addInt, mockPredicateInfo( addInt, "int", ActionType.SETTER, Integer.class, 0 ) );
        OMMap.put( addInt, new LiteralHandler( XSDDatatype.XSDint ) );
        getInt = underTest.getMethod( "getInt" );
        PIMap.put( getInt, mockPredicateInfo( getInt, "int", ActionType.GETTER, Queue.class, 0 ) );
        OMMap.put( getInt, new LiteralHandler( XSDDatatype.XSDint ) );
        hasInt = underTest.getMethod( "hasInt", Integer.class );
        PIMap.put( hasInt, mockPredicateInfo( hasInt, "int", ActionType.EXISTENTIAL, Boolean.class, 0 ) );
        OMMap.put( hasInt, new LiteralHandler( XSDDatatype.XSDint ) );
        removeInt = underTest.getMethod( "removeInt", Integer.class );
        PIMap.put( removeInt, mockPredicateInfo( removeInt, "int", ActionType.REMOVER, Integer.class, 0 ) );
        OMMap.put( removeInt, new LiteralHandler( XSDDatatype.XSDint ) );
        addCount.put( "addInt", Integer.valueOf( 1 ) );

        addLng = underTest.getMethod( "addLng", Long.class );
        PIMap.put( addLng, mockPredicateInfo( addLng, "lng", ActionType.SETTER, Long.class, 0 ) );
        OMMap.put( addLng, new LiteralHandler( XSDDatatype.XSDlong ) );
        getLng = underTest.getMethod( "getLng" );
        PIMap.put( getLng, mockPredicateInfo( getLng, "lng", ActionType.GETTER, List.class, 0 ) );
        OMMap.put( getLng, new LiteralHandler( XSDDatatype.XSDlong ) );
        hasLng = underTest.getMethod( "hasLng", Long.class );
        PIMap.put( hasLng, mockPredicateInfo( hasLng, "lng", ActionType.EXISTENTIAL, Boolean.class, 0 ) );
        OMMap.put( hasLng, new LiteralHandler( XSDDatatype.XSDlong ) );
        removeLng = underTest.getMethod( "removeLng", Long.class );
        PIMap.put( removeLng, mockPredicateInfo( removeLng, "lng", ActionType.REMOVER, Long.class, 0 ) );
        OMMap.put( removeLng, new LiteralHandler( XSDDatatype.XSDlong ) );
        addCount.put( "addLng", Integer.valueOf( 1 ) );

        addRDF = underTest.getMethod( "addRDF", RDFNode.class );
        PIMap.put( addRDF, mockPredicateInfo( addRDF, "rDF", ActionType.SETTER, RDFNode.class, 0 ) );
        OMMap.put( addRDF, ResourceHandler.INSTANCE );
        getRDF = underTest.getMethod( "getRDF" );
        PIMap.put( getRDF, mockPredicateInfo( getRDF, "rDF", ActionType.GETTER, List.class, 0 ) );
        OMMap.put( getRDF, new CollectionHandler( ResourceHandler.INSTANCE, List.class ) );
        hasRDF = underTest.getMethod( "hasRDF", RDFNode.class );
        PIMap.put( hasRDF, mockPredicateInfo( hasRDF, "rDF", ActionType.EXISTENTIAL, Boolean.class, 0 ) );
        OMMap.put( hasRDF, ResourceHandler.INSTANCE );
        removeRDF = underTest.getMethod( "removeRDF", RDFNode.class );
        PIMap.put( removeRDF, mockPredicateInfo( removeRDF, "rDF", ActionType.REMOVER, RDFNode.class, 0 ) );
        OMMap.put( removeRDF, ResourceHandler.INSTANCE );
        addCount.put( "addRDF", Integer.valueOf( 1 ) );

        addStr = underTest.getMethod( "addStr", String.class );
        PIMap.put( addStr, mockPredicateInfo( addStr, "str", ActionType.SETTER, String.class, 0 ) );
        OMMap.put( addStr, new LiteralHandler( XSDDatatype.XSDstring ) );
        getStr = underTest.getMethod( "getStr" );
        PIMap.put( getStr, mockPredicateInfo( getStr, "str", ActionType.GETTER, Set.class, 0 ) );
        OMMap.put( getStr, new LiteralHandler( XSDDatatype.XSDstring ) );
        hasStr = underTest.getMethod( "hasStr", String.class );
        PIMap.put( hasStr, mockPredicateInfo( hasStr, "str", ActionType.EXISTENTIAL, Boolean.class, 0 ) );
        OMMap.put( hasStr, new LiteralHandler( XSDDatatype.XSDstring ) );
        removeStr = underTest.getMethod( "removeStr", String.class );
        PIMap.put( removeStr, mockPredicateInfo( removeStr, "str", ActionType.REMOVER, String.class, 0 ) );
        OMMap.put( removeStr, new LiteralHandler( XSDDatatype.XSDstring ) );
        addCount.put( "addStr", Integer.valueOf( 1 ) );

        addU_R = underTest.getMethod( "addU", RDFNode.class );
        PIMap.put( addU_R, mockPredicateInfo( addU_R, "u", ActionType.SETTER, RDFNode.class, 0 ) );
        OMMap.put( addU_R, ResourceHandler.INSTANCE );
        addU_S = underTest.getMethod( "addU", String.class );
        PIMap.put( addU_S, mockPredicateInfo( addU_S, "u", ActionType.SETTER, String.class, 0 ) );
        OMMap.put( addU_S, UriHandler.INSTANCE );
        getU = underTest.getMethod( "getU" );
        PIMap.put( getU, mockPredicateInfo( getU, "u", ActionType.GETTER, Set.class, 0 ) );
        OMMap.put( getU, new CollectionHandler( ResourceHandler.INSTANCE, Set.class ) );
        hasU_R = underTest.getMethod( "hasU", RDFNode.class );
        PIMap.put( hasU_R, mockPredicateInfo( hasU_R, "u", ActionType.EXISTENTIAL, Boolean.class, 0 ) );
        OMMap.put( hasU_R, ResourceHandler.INSTANCE );
        hasU_S = underTest.getMethod( "hasU", String.class );
        PIMap.put( hasU_S, mockPredicateInfo( hasU_S, "u", ActionType.EXISTENTIAL, Boolean.class, 0 ) );
        OMMap.put( hasU_S, UriHandler.INSTANCE );
        removeU_R = underTest.getMethod( "removeU", RDFNode.class );
        PIMap.put( removeU_R, mockPredicateInfo( removeU_R, "u", ActionType.REMOVER, RDFNode.class, 0 ) );
        OMMap.put( removeU_R, ResourceHandler.INSTANCE );
        removeU_S = underTest.getMethod( "removeU", String.class );
        PIMap.put( removeU_S, mockPredicateInfo( removeU_S, "u", ActionType.REMOVER, URI.class, 0 ) );
        OMMap.put( removeU_S, UriHandler.INSTANCE );
        addCount.put( "addU", Integer.valueOf( 2 ) );

        addU3_R = underTest.getMethod( "addU3", RDFNode.class );
        PIMap.put( addU3_R, mockPredicateInfo( addU3_R, "u3", ActionType.SETTER, RDFNode.class, 0 ) );
        OMMap.put( addU3_R, ResourceHandler.INSTANCE );
        addU3_S = underTest.getMethod( "addU3", String.class );
        PIMap.put( addU3_S, mockPredicateInfo( addU3_S, "u3", ActionType.SETTER, String.class, 0 ) );
        OMMap.put( addU3_S, UriHandler.INSTANCE );
        getU3 = underTest.getMethod( "getU3" );
        PIMap.put( getU3, mockPredicateInfo( getU3, "u3", ActionType.GETTER, Queue.class, 0 ) );
        OMMap.put( getU3, new CollectionHandler( ResourceHandler.INSTANCE, Queue.class ) );
        hasU3_R = underTest.getMethod( "hasU3", RDFNode.class );
        PIMap.put( hasU3_R, mockPredicateInfo( hasU3_R, "u3", ActionType.EXISTENTIAL, Boolean.class, 0 ) );
        OMMap.put( hasU3_R, ResourceHandler.INSTANCE );
        hasU3_S = underTest.getMethod( "hasU3", String.class );
        PIMap.put( hasU3_S, mockPredicateInfo( hasU3_S, "u3", ActionType.EXISTENTIAL, Boolean.class, 0 ) );
        OMMap.put( hasU3_S, UriHandler.INSTANCE );
        removeU3_R = underTest.getMethod( "removeU3", RDFNode.class );
        PIMap.put( removeU3_R, mockPredicateInfo( removeU3_R, "u3", ActionType.REMOVER, RDFNode.class, 0 ) );
        OMMap.put( removeU3_R, ResourceHandler.INSTANCE );
        removeU3_S = underTest.getMethod( "removeU3", String.class );
        PIMap.put( removeU3_S, mockPredicateInfo( removeU3_R, "u3", ActionType.REMOVER, URI.class, 0 ) );
        OMMap.put( removeU3_S, UriHandler.INSTANCE );
        addCount.put( "addU3", Integer.valueOf( 2 ) );

        getU4 = underTest.getMethod( "getU4" );
        PIMap.put( getU4, mockPredicateInfo( getU4, "u3", ActionType.GETTER, Set.class, 0 ) );
        OMMap.put( getU4, new CollectionHandler( UriHandler.INSTANCE, Set.class ) );

    }

    @Test
    public void testParseAddBool() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( addBool );
        assertSame( PIMap.get( addBool ), predicateInfo, addBool );
        assertSame( addBool );
        assertSame( getBool );
        assertSame( hasBool );
        assertSame( removeBool );
    }

    @Test
    public void testParseGetBool() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getBool );
        assertSame( PIMap.get( getBool ), predicateInfo, getBool );
        assertSame( addBool );
        assertSame( getBool );
        assertSame( hasBool );
        assertSame( removeBool );
    }

    @Test
    public void testParseHasBool() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( hasBool );
        assertSame( PIMap.get( hasBool ), predicateInfo, hasBool );
        Assert.assertNull( subjectInfo.getPredicateInfo( addBool ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getBool ) );
        assertSame( hasBool );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeBool ) );
    }

    @Test
    public void testParseRemoveBool() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removeBool );
        assertSame( PIMap.get( removeBool ), predicateInfo, removeBool );
        Assert.assertNull( subjectInfo.getPredicateInfo( addBool ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getBool ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasBool ) );
        assertSame( removeBool );

    }

    @Test
    public void testParseAddChar() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( addChar );
        assertSame( PIMap.get( addChar ), predicateInfo, addChar );
        assertSame( addChar );
        assertSame( getChar );
        assertSame( hasChar );
        assertSame( removeChar );
    }

    @Test
    public void testParseGetChar() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getChar );
        assertSame( PIMap.get( getChar ), predicateInfo, getChar );
        assertSame( addChar );
        assertSame( getChar );
        assertSame( hasChar );
        assertSame( removeChar );
    }

    @Test
    public void testParseHasChar() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( hasChar );
        assertSame( PIMap.get( hasChar ), predicateInfo, hasChar );
        Assert.assertNull( subjectInfo.getPredicateInfo( addChar ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getChar ) );
        assertSame( hasChar );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeChar ) );
    }

    @Test
    public void testParseRemoveChar() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removeChar );
        assertSame( PIMap.get( removeChar ), predicateInfo, removeChar );
        Assert.assertNull( subjectInfo.getPredicateInfo( addChar ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getChar ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasChar ) );
        assertSame( removeChar );
    }

    @Test
    public void testParseAddDbl() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( addDbl );
        assertSame( PIMap.get( addDbl ), predicateInfo, addDbl );
        assertSame( addDbl );
        assertSame( getDbl );
        assertSame( hasDbl );
        assertSame( removeDbl );
    }

    @Test
    public void testParseGetDbl() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getDbl );
        assertSame( PIMap.get( getDbl ), predicateInfo, getDbl );
        assertSame( addDbl );
        assertSame( getDbl );
        assertSame( hasDbl );
        assertSame( removeDbl );
    }

    @Test
    public void testParseHasDbl() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( hasDbl );
        assertSame( PIMap.get( hasDbl ), predicateInfo, hasDbl );
        Assert.assertNull( subjectInfo.getPredicateInfo( addDbl ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getDbl ) );
        assertSame( hasDbl );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeDbl ) );
    }

    @Test
    public void testParseRemoveDbl() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removeDbl );
        assertSame( PIMap.get( removeDbl ), predicateInfo, removeDbl );
        Assert.assertNull( subjectInfo.getPredicateInfo( addDbl ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getDbl ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasDbl ) );
        assertSame( removeDbl );
    }

    @Test
    public void testParseAddEnt() throws Exception {
        OMMap.put( hasEnt, new CollectionHandler( OMMap.get( hasEnt ), Queue.class ) );
        OMMap.put( removeEnt, new CollectionHandler( OMMap.get( removeEnt ), Queue.class ) );
        final PredicateInfo predicateInfo = parser.parse( addEnt );
        assertSame( PIMap.get( addEnt ), predicateInfo, addEnt );
        assertSame( addEnt );
        assertSame( getEnt );
        assertSame( hasEnt );
        assertSame( removeEnt );
    }

    @Test
    public void testParseGetEnt() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getEnt );
        assertSame( PIMap.get( getEnt ), predicateInfo, getEnt );
        assertSame( addEnt );
        assertSame( getEnt );
        assertSame( hasEnt );
        assertSame( removeEnt );
    }

    @Test
    public void testParseHasEnt() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( hasEnt );
        assertSame( PIMap.get( hasEnt ), predicateInfo, hasEnt );
        Assert.assertNull( subjectInfo.getPredicateInfo( addEnt ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getEnt ) );
        assertSame( hasEnt );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeEnt ) );
    }

    @Test
    public void testParseRemoveEnt() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removeEnt );
        assertSame( PIMap.get( removeEnt ), predicateInfo, removeEnt );
        Assert.assertNull( subjectInfo.getPredicateInfo( addEnt ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getEnt ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasEnt ) );
        assertSame( removeEnt );
    }

    @Test
    public void testParseAddFlt() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( addFlt );
        assertSame( PIMap.get( addFlt ), predicateInfo, addFlt );
        assertSame( addFlt );
        assertSame( getFlt );
        assertSame( hasFlt );
        assertSame( removeFlt );
    }

    @Test
    public void testParseGetFlt() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getFlt );
        assertSame( PIMap.get( getFlt ), predicateInfo, getFlt );
        assertSame( addFlt );
        assertSame( getFlt );
        assertSame( hasFlt );
        assertSame( removeFlt );
    }

    @Test
    public void testParseHasFlt() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( hasFlt );
        assertSame( PIMap.get( hasFlt ), predicateInfo, hasFlt );
        Assert.assertNull( subjectInfo.getPredicateInfo( addFlt ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getFlt ) );
        assertSame( hasFlt );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeFlt ) );
    }

    @Test
    public void testParseRemoveFlt() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removeFlt );
        assertSame( PIMap.get( removeFlt ), predicateInfo, removeFlt );
        Assert.assertNull( subjectInfo.getPredicateInfo( addFlt ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getFlt ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasFlt ) );
        assertSame( removeFlt );
    }

    @Test
    public void testParseAddInt() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( addInt );
        assertSame( PIMap.get( addInt ), predicateInfo, addInt );
        assertSame( addInt );
        assertSame( getInt );
        assertSame( hasInt );
        assertSame( removeInt );
    }

    @Test
    public void testParseGetInt() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getInt );
        assertSame( PIMap.get( getInt ), predicateInfo, getInt );
        assertSame( addInt );
        assertSame( getInt );
        assertSame( hasInt );
        assertSame( removeInt );
    }

    @Test
    public void testParseHasInt() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( hasInt );
        assertSame( PIMap.get( hasInt ), predicateInfo, hasInt );
        Assert.assertNull( subjectInfo.getPredicateInfo( addInt ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getInt ) );
        assertSame( hasInt );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeInt ) );
    }

    @Test
    public void testParseRemoveInt() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removeInt );
        assertSame( PIMap.get( removeInt ), predicateInfo, removeInt );
        Assert.assertNull( subjectInfo.getPredicateInfo( addInt ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getInt ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasInt ) );
        assertSame( removeInt );
    }

    @Test
    public void testParseAddLng() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( addLng );
        assertSame( PIMap.get( addLng ), predicateInfo, addLng );
        assertSame( addLng );
        assertSame( getLng );
        assertSame( hasLng );
        assertSame( removeLng );
    }

    @Test
    public void testParseGetLng() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getLng );
        assertSame( PIMap.get( getLng ), predicateInfo, getLng );
        assertSame( addLng );
        assertSame( getLng );
        assertSame( hasLng );
        assertSame( removeLng );
    }

    @Test
    public void testParseHasLng() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( hasLng );
        assertSame( PIMap.get( hasLng ), predicateInfo, hasLng );
        Assert.assertNull( subjectInfo.getPredicateInfo( addLng ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getLng ) );
        assertSame( hasLng );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeLng ) );
    }

    @Test
    public void testParseRemoveLng() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removeLng );
        assertSame( PIMap.get( removeLng ), predicateInfo, removeLng );
        Assert.assertNull( subjectInfo.getPredicateInfo( addLng ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getLng ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasLng ) );
        assertSame( removeLng );
    }

    @Test
    public void testParseAddRDF() throws Exception {
        OMMap.put( hasRDF, new CollectionHandler( OMMap.get( hasRDF ), List.class ) );
        OMMap.put( removeRDF, new CollectionHandler( OMMap.get( removeRDF ), List.class ) );
        final PredicateInfo predicateInfo = parser.parse( addRDF );
        assertSame( PIMap.get( addRDF ), predicateInfo, addRDF );
        assertSame( addRDF );
        assertSame( getRDF );
        assertSame( hasRDF );
        assertSame( removeRDF );
    }

    @Test
    public void testParseGetRDF() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getRDF );
        assertSame( PIMap.get( getRDF ), predicateInfo, getRDF );
        assertSame( addRDF );
        assertSame( getRDF );
        assertSame( hasRDF );
        assertSame( removeRDF );
    }

    @Test
    public void testParseHasRDF() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( hasRDF );
        assertSame( PIMap.get( hasRDF ), predicateInfo, hasRDF );
        Assert.assertNull( subjectInfo.getPredicateInfo( addRDF ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getRDF ) );
        assertSame( hasRDF );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeRDF ) );
    }

    @Test
    public void testParseRemoveRDF() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removeRDF );
        assertSame( PIMap.get( removeRDF ), predicateInfo, removeRDF );
        Assert.assertNull( subjectInfo.getPredicateInfo( addRDF ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getRDF ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasRDF ) );
        assertSame( removeRDF );
    }

    @Test
    public void testParseAddStr() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( addStr );
        assertSame( PIMap.get( addStr ), predicateInfo, addStr );
        assertSame( addStr );
        assertSame( getStr );
        assertSame( hasStr );
        assertSame( removeStr );
    }

    @Test
    public void testParseGetStr() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getStr );
        assertSame( PIMap.get( getStr ), predicateInfo, getStr );
        assertSame( addStr );
        assertSame( getStr );
        assertSame( hasStr );
        assertSame( removeStr );
    }

    @Test
    public void testParseHasStr() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( hasStr );
        assertSame( PIMap.get( hasStr ), predicateInfo, hasStr );
        Assert.assertNull( subjectInfo.getPredicateInfo( addStr ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getStr ) );
        assertSame( hasStr );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeStr ) );
    }

    @Test
    public void testParseRemoveStr() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removeStr );
        assertSame( PIMap.get( removeStr ), predicateInfo, removeStr );
        Assert.assertNull( subjectInfo.getPredicateInfo( addStr ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getStr ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasStr ) );
        assertSame( removeStr );
    }

    @Test
    public void testParseAddU_R() throws Exception {
        OMMap.put( addU_S, new CollectionHandler( OMMap.get( addU_S ), Set.class ) );
        OMMap.put( hasU_R, new CollectionHandler( OMMap.get( hasU_R ), Set.class ) );
        OMMap.put( hasU_S, new CollectionHandler( OMMap.get( hasU_S ), Set.class ) );
        OMMap.put( removeU_R, new CollectionHandler( OMMap.get( removeU_R ), Set.class ) );
        OMMap.put( removeU_S, new CollectionHandler( OMMap.get( removeU_S ), Set.class ) );
        final PredicateInfo predicateInfo = parser.parse( addU_R );
        assertSame( PIMap.get( addU_R ), predicateInfo, addU_R );
        assertSame( addU_R );
        assertSame( addU_S );
        assertSame( getU );
        assertSame( hasU_R );
        assertSame( hasU_S );
        assertSame( removeU_R );
        assertSame( removeU_S );
    }

    @Test
    public void testParseAddU_S() throws Exception {
        OMMap.put( addU_R, new CollectionHandler( OMMap.get( addU_R ), Set.class ) );
        OMMap.put( hasU_R, new CollectionHandler( OMMap.get( hasU_R ), Set.class ) );
        OMMap.put( hasU_S, new CollectionHandler( OMMap.get( hasU_S ), Set.class ) );
        OMMap.put( removeU_R, new CollectionHandler( OMMap.get( removeU_R ), Set.class ) );
        OMMap.put( removeU_S, new CollectionHandler( OMMap.get( removeU_S ), Set.class ) );
        final PredicateInfo predicateInfo = parser.parse( addU_S );
        assertSame( PIMap.get( addU_S ), predicateInfo, addU_S );
        assertSame( addU_R );
        assertSame( addU_S );
        assertSame( getU );
        assertSame( hasU_R );
        assertSame( hasU_S );
        assertSame( removeU_R );
        assertSame( removeU_S );
    }

    @Test
    public void testParseGetU() throws Exception {
        OMMap.put( addU_S, new CollectionHandler( OMMap.get( addU_S ), Set.class ) );
        OMMap.put( addU_R, new CollectionHandler( OMMap.get( addU_R ), Set.class ) );
        OMMap.put( hasU_R, new CollectionHandler( OMMap.get( hasU_R ), Set.class ) );
        OMMap.put( hasU_S, new CollectionHandler( OMMap.get( hasU_S ), Set.class ) );
        OMMap.put( removeU_R, new CollectionHandler( OMMap.get( removeU_R ), Set.class ) );
        OMMap.put( removeU_S, new CollectionHandler( OMMap.get( removeU_S ), Set.class ) );
        final PredicateInfo predicateInfo = parser.parse( getU );
        assertSame( PIMap.get( getU ), predicateInfo, getU );
        assertSame( addU_R );
        assertSame( addU_S );
        assertSame( getU );
        assertSame( hasU_R );
        assertSame( hasU_S );
        assertSame( removeU_R );
        assertSame( removeU_S );
    }

    @Test
    public void testParseHasU_S() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( hasU_S );
        assertSame( PIMap.get( hasU_S ), predicateInfo, hasU_S );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU_S ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getU ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU_R ) );
        assertSame( hasU_S );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU_S ) );
    }

    @Test
    public void testParseHasU_R() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( hasU_R );
        assertSame( PIMap.get( hasU_R ), predicateInfo, hasU_R );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU_S ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getU ) );
        assertSame( hasU_R );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU_S ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU_S ) );
    }

    @Test
    public void testParseRemoveU_R() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removeU_R );
        assertSame( PIMap.get( removeU_R ), predicateInfo, removeU_R );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU_S ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getU ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU_S ) );
        assertSame( removeU_R );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU_S ) );
    }

    @Test
    public void testParseRemoveU_S() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removeU_S );
        assertSame( PIMap.get( removeU_S ), predicateInfo, removeU_S );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU_S ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getU ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU_S ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU_R ) );
        assertSame( removeU_S );
    }

    @Test
    public void testParseAddU3_R() throws Exception {
        OMMap.put( addU3_S, new CollectionHandler( OMMap.get( addU3_S ), Queue.class ) );
        OMMap.put( hasU3_R, new CollectionHandler( OMMap.get( hasU3_R ), Queue.class ) );
        OMMap.put( hasU3_S, new CollectionHandler( OMMap.get( hasU3_S ), Queue.class ) );
        OMMap.put( removeU3_R, new CollectionHandler( OMMap.get( removeU3_R ), Queue.class ) );
        OMMap.put( removeU3_S, new CollectionHandler( OMMap.get( removeU3_S ), Queue.class ) );
        final PredicateInfo predicateInfo = parser.parse( addU3_R );
        assertSame( PIMap.get( addU3_R ), predicateInfo, addU3_R );
        assertSame( addU3_R );
        assertSame( addU3_S );
        assertSame( getU3 );
        assertSame( hasU3_R );
        assertSame( hasU3_S );
        assertSame( removeU3_R );
        assertSame( removeU3_S );
    }

    @Test
    public void testParseAddU3_S() throws Exception {
        OMMap.put( addU3_R, new CollectionHandler( OMMap.get( addU3_R ), Queue.class ) );
        OMMap.put( hasU3_R, new CollectionHandler( OMMap.get( hasU3_R ), Queue.class ) );
        OMMap.put( hasU3_S, new CollectionHandler( OMMap.get( hasU3_S ), Queue.class ) );
        OMMap.put( removeU3_R, new CollectionHandler( OMMap.get( removeU3_R ), Queue.class ) );
        OMMap.put( removeU3_S, new CollectionHandler( OMMap.get( removeU3_S ), Queue.class ) );
        final PredicateInfo predicateInfo = parser.parse( addU3_S );
        assertSame( PIMap.get( addU3_S ), predicateInfo, addU3_S );
        assertSame( addU3_R );
        assertSame( addU3_S );
        assertSame( getU3 );
        assertSame( hasU3_R );
        assertSame( hasU3_S );
        assertSame( removeU3_R );
        assertSame( removeU3_S );
    }

    @Test
    public void testParseGetU3() throws Exception {
        OMMap.put( addU3_S, new CollectionHandler( OMMap.get( addU3_S ), Queue.class ) );
        OMMap.put( addU3_R, new CollectionHandler( OMMap.get( addU3_R ), Queue.class ) );
        OMMap.put( hasU3_S, new CollectionHandler( OMMap.get( hasU3_S ), Queue.class ) );
        OMMap.put( hasU3_R, new CollectionHandler( OMMap.get( hasU3_R ), Queue.class ) );
        OMMap.put( removeU3_R, new CollectionHandler( OMMap.get( removeU3_R ), Queue.class ) );
        OMMap.put( removeU3_S, new CollectionHandler( OMMap.get( removeU3_S ), Queue.class ) );
        final PredicateInfo predicateInfo = parser.parse( getU3 );
        assertSame( PIMap.get( getU3 ), predicateInfo, getU3 );
        assertSame( addU3_R );
        assertSame( addU3_S );
        assertSame( getU3 );
        assertSame( hasU3_R );
        assertSame( hasU3_S );
        assertSame( removeU3_R );
        assertSame( removeU3_S );
    }

    @Test
    public void testParseHasU3_R() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( hasU3_R );
        assertSame( PIMap.get( hasU3_R ), predicateInfo, hasU_R );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU3_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU3_S ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getU3 ) );
        assertSame( hasU3_R );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU3_S ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU3_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU3_S ) );
    }

    @Test
    public void testParseHasU3_S() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( hasU3_S );
        assertSame( PIMap.get( hasU3_S ), predicateInfo, hasU_S );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU3_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU3_S ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getU3 ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU3_R ) );
        assertSame( hasU3_S );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU3_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU3_S ) );
    }

    @Test
    public void testParseRemoveU3_R() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removeU3_R );
        assertSame( PIMap.get( removeU3_R ), predicateInfo, removeU3_R );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU3_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU3_S ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getU3 ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU3_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU3_S ) );
        assertSame( removeU3_R );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU3_S ) );
    }

    @Test
    public void testParseRemoveU3_S() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removeU3_S );
        assertSame( PIMap.get( removeU3_S ), predicateInfo, removeU3_S );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU3_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU3_S ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( getU3 ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU3_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU3_S ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU3_R ) );
        assertSame( removeU3_S );
    }

    @Test
    public void testParseGetU4() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getU4 );
        assertSame( PIMap.get( getU4 ), predicateInfo, getU4 );
        assertSame( getU4 );
    }

}
