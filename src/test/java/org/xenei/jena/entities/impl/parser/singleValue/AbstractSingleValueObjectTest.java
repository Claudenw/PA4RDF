package org.xenei.jena.entities.impl.parser.singleValue;

import java.lang.reflect.Method;

import org.apache.jena.rdf.model.RDFNode;
import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.parser.AbstractMethodParserTest;
import org.xenei.jena.entities.testing.iface.TestInterface;

public abstract class AbstractSingleValueObjectTest extends AbstractMethodParserTest {

    protected final Method isBool;
    protected final Method setBool;
    protected final Method removeBool;

    protected final Method getChar;
    protected final Method setChar;
    protected final Method removeChar;

    protected final Method getDbl;
    protected final Method setDbl;
    protected final Method removeDbl;

    protected final Method getEnt;
    protected final Method setEnt;
    protected final Method removeEnt;

    protected final Method getFlt;
    protected final Method setFlt;
    protected final Method removeFlt;

    protected final Method getInt;
    protected final Method setInt;
    protected final Method removeInt;

    protected final Method getLng;
    protected final Method setLng;
    protected final Method removeLng;

    protected final Method getRDF;
    protected final Method setRDF;
    protected final Method removeRDF;

    protected final Method getStr;
    protected final Method setStr;
    protected final Method removeStr;

    protected final Method getU;
    protected final Method setU_S;
    protected final Method setU_R;
    protected final Method removeU;
    protected final Method getU2;

    protected AbstractSingleValueObjectTest(final Class<?> interfaceClass)
            throws NoSuchMethodException, SecurityException {
        super( interfaceClass );
        NS = "http://localhost/test#";

        isBool = interfaceClass.getMethod( "isBool" );
        PIMap.put( isBool, mockPredicateInfo( isBool, "bool", ActionType.GETTER, Boolean.class, 0 ) );

        setBool = interfaceClass.getMethod( "setBool", Boolean.class );
        PIMap.put( setBool, mockPredicateInfo( setBool, "bool", ActionType.SETTER, Boolean.class, 0 ) );

        removeBool = interfaceClass.getMethod( "removeBool" );
        PIMap.put( removeBool, mockPredicateInfo( removeBool, "bool", ActionType.REMOVER, Predicate.UNSET.class, 0 ) );

        addCount.put( "setBool", Integer.valueOf( 1 ) );

        getChar = interfaceClass.getMethod( "getChar" );
        PIMap.put( getChar, mockPredicateInfo( getChar, "char", ActionType.GETTER, Character.class, 0 ) );

        setChar = interfaceClass.getMethod( "setChar", Character.class );
        PIMap.put( setChar, mockPredicateInfo( setChar, "char", ActionType.SETTER, Character.class, 0 ) );

        removeChar = interfaceClass.getMethod( "removeChar" );
        PIMap.put( removeChar, mockPredicateInfo( removeChar, "char", ActionType.REMOVER, Predicate.UNSET.class, 0 ) );

        addCount.put( "setChar", Integer.valueOf( 1 ) );

        getDbl = interfaceClass.getMethod( "getDbl" );
        PIMap.put( getDbl, mockPredicateInfo( getDbl, "dbl", ActionType.GETTER, Double.class, 0 ) );

        setDbl = interfaceClass.getMethod( "setDbl", Double.class );
        PIMap.put( setDbl, mockPredicateInfo( setDbl, "dbl", ActionType.SETTER, Double.class, 0 ) );

        removeDbl = interfaceClass.getMethod( "removeDbl" );
        PIMap.put( removeDbl, mockPredicateInfo( removeDbl, "dbl", ActionType.REMOVER, Predicate.UNSET.class, 0 ) );

        addCount.put( "setDbl", Integer.valueOf( 1 ) );

        getEnt = interfaceClass.getMethod( "getEnt" );
        PIMap.put( getEnt, mockPredicateInfo( getEnt, "ent", ActionType.GETTER, TestInterface.class, 0 ) );

        setEnt = interfaceClass.getMethod( "setEnt", TestInterface.class );
        PIMap.put( setEnt, mockPredicateInfo( setEnt, "ent", ActionType.SETTER, TestInterface.class, 0 ) );

        removeEnt = interfaceClass.getMethod( "removeEnt" );
        PIMap.put( removeEnt, mockPredicateInfo( removeEnt, "ent", ActionType.REMOVER, Predicate.UNSET.class, 0 ) );

        addCount.put( "setEnt", Integer.valueOf( 1 ) );

        getFlt = interfaceClass.getMethod( "getFlt" );
        PIMap.put( getFlt, mockPredicateInfo( getFlt, "flt", ActionType.GETTER, Float.class, 0 ) );

        setFlt = interfaceClass.getMethod( "setFlt", Float.class );
        PIMap.put( setFlt, mockPredicateInfo( setFlt, "flt", ActionType.SETTER, Float.class, 0 ) );

        removeFlt = interfaceClass.getMethod( "removeFlt" );
        PIMap.put( removeFlt, mockPredicateInfo( removeFlt, "flt", ActionType.REMOVER, Predicate.UNSET.class, 0 ) );

        addCount.put( "setFlt", Integer.valueOf( 1 ) );

        getInt = interfaceClass.getMethod( "getInt" );
        PIMap.put( getInt, mockPredicateInfo( getInt, "int", ActionType.GETTER, Integer.class, 0 ) );

        setInt = interfaceClass.getMethod( "setInt", Integer.class );
        PIMap.put( setInt, mockPredicateInfo( setInt, "int", ActionType.SETTER, Integer.class, 0 ) );

        removeInt = interfaceClass.getMethod( "removeInt" );
        PIMap.put( removeInt, mockPredicateInfo( removeInt, "int", ActionType.REMOVER, Predicate.UNSET.class, 0 ) );

        addCount.put( "setInt", Integer.valueOf( 1 ) );

        getLng = interfaceClass.getMethod( "getLng" );
        PIMap.put( getLng, mockPredicateInfo( getLng, "lng", ActionType.GETTER, Long.class, 0 ) );

        setLng = interfaceClass.getMethod( "setLng", Long.class );
        PIMap.put( setLng, mockPredicateInfo( setLng, "lng", ActionType.SETTER, Long.class, 0 ) );

        removeLng = interfaceClass.getMethod( "removeLng" );
        PIMap.put( removeLng, mockPredicateInfo( removeLng, "lng", ActionType.REMOVER, Predicate.UNSET.class, 0 ) );

        addCount.put( "setLng", Integer.valueOf( 1 ) );

        getRDF = interfaceClass.getMethod( "getRDF" );
        PIMap.put( getRDF, mockPredicateInfo( getRDF, "rDF", ActionType.GETTER, RDFNode.class, 0 ) );

        setRDF = interfaceClass.getMethod( "setRDF", RDFNode.class );
        PIMap.put( setRDF, mockPredicateInfo( setRDF, "rDF", ActionType.SETTER, RDFNode.class, 0 ) );

        removeRDF = interfaceClass.getMethod( "removeRDF" );
        PIMap.put( removeRDF, mockPredicateInfo( removeRDF, "rDF", ActionType.REMOVER, Predicate.UNSET.class, 0 ) );

        addCount.put( "setRDF", Integer.valueOf( 1 ) );

        getStr = interfaceClass.getMethod( "getStr" );
        PIMap.put( getStr, mockPredicateInfo( getStr, "str", ActionType.GETTER, String.class, 0 ) );

        setStr = interfaceClass.getMethod( "setStr", String.class );
        PIMap.put( setStr, mockPredicateInfo( setStr, "str", ActionType.SETTER, String.class, 0 ) );

        removeStr = interfaceClass.getMethod( "removeStr" );
        PIMap.put( removeStr, mockPredicateInfo( removeStr, "str", ActionType.REMOVER, Predicate.UNSET.class, 0 ) );

        addCount.put( "setStr", Integer.valueOf( 1 ) );

        getU = interfaceClass.getMethod( "getU" );
        PIMap.put( getU, mockPredicateInfo( getU, "u", ActionType.GETTER, RDFNode.class, 0 ) );

        setU_S = interfaceClass.getMethod( "setU", String.class );
        PIMap.put( setU_S, mockPredicateInfo( setU_S, "u", ActionType.SETTER, String.class, 0 ) );

        setU_R = interfaceClass.getMethod( "setU", RDFNode.class );
        PIMap.put( setU_R, mockPredicateInfo( setU_R, "u", ActionType.SETTER, RDFNode.class, 0 ) );

        removeU = interfaceClass.getMethod( "removeU" );
        PIMap.put( removeU, mockPredicateInfo( removeU, "u", ActionType.REMOVER, Predicate.UNSET.class, 0 ) );

        getU2 = interfaceClass.getMethod( "getU2" );
        PIMap.put( getU2, mockPredicateInfo( getU2, "u", ActionType.GETTER, String.class, 0 ) );

        addCount.put( "setU", Integer.valueOf( 1 ) );
        addCount.put( "setU2", Integer.valueOf( 1 ) );

    }

    @Test
    public void testIsBool() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( isBool );
        assertSame( PIMap.get( isBool ), predicateInfo, isBool );
        assertSame( isBool );
        assertSame( setBool );
        assertSame( removeBool );
    }

    @Test
    public void testSetBool() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( setBool );
        assertSame( PIMap.get( setBool ), predicateInfo, setBool );
        assertSame( isBool );
        assertSame( setBool );
        assertSame( removeBool );
    }

    @Test
    public void testRemoveBool() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( removeBool );
        assertSame( PIMap.get( removeBool ), predicateInfo, removeBool );
        Assert.assertNull( subjectInfo.getPredicateInfo( isBool ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setBool ) );
        assertSame( removeBool );
    }

    @Test
    public void testGetChar() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( getChar );
        assertSame( PIMap.get( getChar ), predicateInfo, getChar );
        assertSame( getChar );
        assertSame( setChar );
        assertSame( removeChar );
    }

    @Test
    public void testSetChar() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( setChar );
        assertSame( PIMap.get( setChar ), predicateInfo, setChar );
        assertSame( getChar );
        assertSame( setChar );
        assertSame( removeChar );
    }

    @Test
    public void testRemoveChar() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( removeChar );
        assertSame( PIMap.get( removeChar ), predicateInfo, removeChar );
        Assert.assertNull( subjectInfo.getPredicateInfo( getChar ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setChar ) );
        assertSame( removeChar );
    }

    @Test
    public void testGetDbl() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( getDbl );
        assertSame( PIMap.get( getDbl ), predicateInfo, getDbl );
        assertSame( getDbl );
        assertSame( setDbl );
        assertSame( removeDbl );
    }

    @Test
    public void testSetDbl() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( setDbl );
        assertSame( PIMap.get( setDbl ), predicateInfo, setDbl );
        assertSame( getDbl );
        assertSame( setDbl );
        assertSame( removeDbl );
    }

    @Test
    public void testRemoveDbl() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( removeDbl );
        assertSame( PIMap.get( removeDbl ), predicateInfo, removeDbl );
        Assert.assertNull( subjectInfo.getPredicateInfo( getDbl ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setDbl ) );
        assertSame( removeDbl );
    }

    @Test
    public void testGetEnt() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( getEnt );
        assertSame( PIMap.get( getEnt ), predicateInfo, getEnt );
        assertSame( getEnt );
        assertSame( setEnt );
        assertSame( removeEnt );
    }

    @Test
    public void testSetEnt() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( setEnt );
        assertSame( PIMap.get( setEnt ), predicateInfo, setEnt );
        assertSame( getEnt );
        assertSame( setEnt );
        assertSame( removeEnt );
    }

    @Test
    public void testRemoveEnt() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( removeEnt );
        assertSame( PIMap.get( removeEnt ), predicateInfo, removeEnt );
        Assert.assertNull( subjectInfo.getPredicateInfo( getEnt ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setEnt ) );
        assertSame( removeEnt );
    }

    @Test
    public void testGetFlt() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( getFlt );
        assertSame( PIMap.get( getFlt ), predicateInfo, getFlt );
        assertSame( getFlt );
        assertSame( setFlt );
        assertSame( removeFlt );
    }

    @Test
    public void testSetFlt() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( setFlt );
        assertSame( PIMap.get( setFlt ), predicateInfo, setFlt );
        assertSame( getFlt );
        assertSame( setFlt );
        assertSame( removeFlt );
    }

    @Test
    public void testRemoveFlt() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( removeFlt );
        assertSame( PIMap.get( removeFlt ), predicateInfo, removeFlt );
        Assert.assertNull( subjectInfo.getPredicateInfo( getFlt ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setFlt ) );
        assertSame( removeFlt );
    }

    @Test
    public void testGetInt() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( getInt );
        assertSame( PIMap.get( getInt ), predicateInfo, getInt );
        assertSame( getInt );
        assertSame( setInt );
        assertSame( removeInt );
    }

    @Test
    public void testSetInt() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( setInt );
        assertSame( PIMap.get( setInt ), predicateInfo, setInt );
        assertSame( getInt );
        assertSame( setInt );
        assertSame( removeInt );
    }

    @Test
    public void testRemoveInt() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( removeInt );
        assertSame( PIMap.get( removeInt ), predicateInfo, removeInt );
        Assert.assertNull( subjectInfo.getPredicateInfo( getInt ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setInt ) );
        assertSame( removeInt );
    }

    @Test
    public void testGetLng() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( getLng );
        assertSame( PIMap.get( getLng ), predicateInfo, getLng );
        assertSame( getLng );
        assertSame( setLng );
        assertSame( removeLng );
    }

    @Test
    public void testSetLng() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( setLng );
        assertSame( PIMap.get( setLng ), predicateInfo, setLng );
        assertSame( getLng );
        assertSame( setLng );
        assertSame( removeLng );
    }

    @Test
    public void testRemoveLng() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( removeLng );
        assertSame( PIMap.get( removeLng ), predicateInfo, removeLng );
        Assert.assertNull( subjectInfo.getPredicateInfo( getLng ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setLng ) );
        assertSame( removeLng );
    }

    @Test
    public void testGetRDF() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( getRDF );
        assertSame( PIMap.get( getRDF ), predicateInfo, getRDF );
        assertSame( getRDF );
        assertSame( setRDF );
        assertSame( removeRDF );
    }

    @Test
    public void testSetRDF() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( setRDF );
        assertSame( PIMap.get( setRDF ), predicateInfo, setRDF );
        assertSame( getRDF );
        assertSame( setRDF );
        assertSame( removeRDF );
    }

    @Test
    public void testRemoveRDF() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( removeRDF );
        assertSame( PIMap.get( removeRDF ), predicateInfo, removeRDF );
        Assert.assertNull( subjectInfo.getPredicateInfo( getRDF ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setRDF ) );
        assertSame( removeRDF );
    }

    @Test
    public void testGetStr() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( getStr );
        assertSame( PIMap.get( getStr ), predicateInfo, getStr );
        assertSame( getStr );
        assertSame( setStr );
        assertSame( removeStr );
    }

    @Test
    public void testSetStr() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( setStr );
        assertSame( PIMap.get( setStr ), predicateInfo, setStr );
        assertSame( getStr );
        assertSame( setStr );
        assertSame( removeStr );
    }

    @Test
    public void testRemoveStr() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( removeStr );
        assertSame( PIMap.get( removeStr ), predicateInfo, removeStr );
        Assert.assertNull( subjectInfo.getPredicateInfo( getStr ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setStr ) );
        assertSame( removeStr );
    }

    @Test
    public void testGetU() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( getU );
        assertSame( PIMap.get( getU ), predicateInfo, getU );
        assertSame( getU );
        assertSame( setU_R );
        assertSame( setU_S );
        assertSame( removeU );
        Assert.assertNull( subjectInfo.getPredicateInfo( getU2 ) );
    }

    @Test
    public void testSetU_S() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( setU_S );
        assertSame( PIMap.get( setU_S ), predicateInfo, setU_S );
        assertSame( getU );
        assertSame( setU_R );
        assertSame( setU_S );
        assertSame( removeU );
        Assert.assertNull( subjectInfo.getPredicateInfo( getU2 ) );
    }

    @Test
    public void testSetU_R() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( setU_R );
        assertSame( PIMap.get( setU_R ), predicateInfo, setU_R );
        assertSame( getU );
        assertSame( setU_R );
        assertSame( setU_S );
        assertSame( removeU );
        Assert.assertNull( subjectInfo.getPredicateInfo( getU2 ) );
    }

    @Test
    public void testRemoveU() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( removeU );
        assertSame( PIMap.get( removeU ), predicateInfo, removeU );
        Assert.assertNull( subjectInfo.getPredicateInfo( getU ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setU_S ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setU_R ) );
        assertSame( removeU );
        Assert.assertNull( subjectInfo.getPredicateInfo( getU2 ) );
    }

    @Test
    public void testGetU2() throws MissingAnnotation {
        final PredicateInfo predicateInfo = parser.parse( getU2 );
        assertSame( PIMap.get( getU2 ), predicateInfo, getU2 );
        Assert.assertNull( subjectInfo.getPredicateInfo( getU ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setU_S ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setU_R ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU ) );
        assertSame( getU2 );
    }

}
