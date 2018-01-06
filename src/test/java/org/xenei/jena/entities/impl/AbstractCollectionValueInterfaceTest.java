package org.xenei.jena.entities.impl;

import java.lang.reflect.Method;
import java.util.Queue;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.testing.iface.CollectionValueInterface;
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

    protected Method addEnt;// (TestInterface b);
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

    protected AbstractCollectionValueInterfaceTest(Class<? extends CollectionValueInterface> underTest)
            throws NoSuchMethodException, SecurityException {
        super( underTest );
        NS = "http://localhost/test#";

        addBool = underTest.getMethod( "addBool", Boolean.class );
        getBool = underTest.getMethod( "getBool" );
        hasBool = underTest.getMethod( "hasBool", Boolean.class );
        removeBool = underTest.getMethod( "removeBool", Boolean.class );

        addChar = underTest.getMethod( "addChar", Character.class );
        getChar = underTest.getMethod( "getChar" );
        hasChar = underTest.getMethod( "hasChar", Character.class );
        removeChar = underTest.getMethod( "removeChar", Character.class );

        addDbl = underTest.getMethod( "addDbl", Double.class );
        PIMap.put( addDbl, mockPredicateInfo( addDbl, "dbl", ActionType.SETTER, Double.class, 1, 0 ) );
        OMMap.put( addDbl, new LiteralHandler( XSDDatatype.XSDdouble ) );
        addCount.put( "addDbl", Integer.valueOf( 1 ) );

        getDbl = underTest.getMethod( "getDbl" );
        PIMap.put( getDbl, mockPredicateInfo( getDbl, "dbl", ActionType.GETTER, Queue.class, 0, 0 ) );
        OMMap.put( getDbl, new LiteralHandler( XSDDatatype.XSDdouble ) );

        hasDbl = underTest.getMethod( "hasDbl", Double.class );
        PIMap.put( hasDbl, mockPredicateInfo( hasDbl, "dbl", ActionType.EXISTENTIAL, Boolean.class, 0, 0 ) );
        OMMap.put( hasDbl, new LiteralHandler( XSDDatatype.XSDdouble ) );

        removeDbl = underTest.getMethod( "removeDbl", Double.class );
        PIMap.put( removeDbl, mockPredicateInfo( removeDbl, "dbl", ActionType.REMOVER, Double.class, 0, 0 ) );
        OMMap.put( removeDbl, new LiteralHandler( XSDDatatype.XSDdouble ) );

        addEnt = underTest.getMethod( "addEnt", TestInterface.class );
        getEnt = underTest.getMethod( "getEnt" );
        hasEnt = underTest.getMethod( "hasEnt", TestInterface.class );
        removeEnt = underTest.getMethod( "removeEnt", TestInterface.class );

        addFlt = underTest.getMethod( "addFlt", Float.class );
        getFlt = underTest.getMethod( "getFlt" );
        hasFlt = underTest.getMethod( "hasFlt", Float.class );
        removeFlt = underTest.getMethod( "removeFlt", Float.class );

        addInt = underTest.getMethod( "addInt", Integer.class );
        getInt = underTest.getMethod( "getInt" );
        hasInt = underTest.getMethod( "hasInt", Integer.class );
        removeInt = underTest.getMethod( "removeInt", Integer.class );

        addLng = underTest.getMethod( "addLng", Long.class );
        getLng = underTest.getMethod( "getLng" );
        hasLng = underTest.getMethod( "hasLng", Long.class );
        removeLng = underTest.getMethod( "removeLng", Long.class );

        addRDF = underTest.getMethod( "addRDF", RDFNode.class );
        getRDF = underTest.getMethod( "getRDF" );
        hasRDF = underTest.getMethod( "hasRDF", RDFNode.class );
        removeRDF = underTest.getMethod( "removeRDF", RDFNode.class );

        addStr = underTest.getMethod( "addStr", String.class );
        getStr = underTest.getMethod( "getStr" );
        hasStr = underTest.getMethod( "hasStr", String.class );
        removeStr = underTest.getMethod( "removeStr", String.class );

        addU_R = underTest.getMethod( "addU", RDFNode.class );
        addU_S = underTest.getMethod( "addU", String.class );
        getU = underTest.getMethod( "getU" );
        hasU_R = underTest.getMethod( "hasU", RDFNode.class );
        hasU_S = underTest.getMethod( "hasU", String.class );
        removeU_R = underTest.getMethod( "removeU", RDFNode.class );
        removeU_S = underTest.getMethod( "removeU", RDFNode.class );

        addU3_R = underTest.getMethod( "addU3", RDFNode.class );
        addU3_S = underTest.getMethod( "addU3", String.class );
        getU3 = underTest.getMethod( "getU3" );
        hasU3_R = underTest.getMethod( "hasU3", RDFNode.class );
        hasU3_S = underTest.getMethod( "hasU3", String.class );
        removeU3_R = underTest.getMethod( "removeU3", RDFNode.class );
        removeU3_S = underTest.getMethod( "removeU3", String.class );

        getU4 = underTest.getMethod( "getU4" );

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
}
