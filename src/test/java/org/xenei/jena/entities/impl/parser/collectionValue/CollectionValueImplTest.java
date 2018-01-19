package org.xenei.jena.entities.impl.parser.collectionValue;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.testing.impl.CollectionValueImpl;

public class CollectionValueImplTest extends AbstractCollectionValueInterfaceTest {

    public CollectionValueImplTest() throws NoSuchMethodException, SecurityException {
        super( CollectionValueImpl.class );
    }

    @Override
    @Test
    public void testParseAddU3_S() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( addU3_S );
        assertSame( PIMap.get( addU3_S ), predicateInfo, addU3_S );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU3_R ) );
        assertSame( addU3_S );
        assertSame( getU3  );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU3_R ));
        assertSame( hasU3_S );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU3_R ));
        assertSame( removeU3_S );
    }

    @Override
    @Test
    public void testParseAddU_S() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( addU_S );
        assertSame( PIMap.get( addU_S ), predicateInfo, addU_S );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU_R ));
        assertSame( addU_S );
        assertSame( getU );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU_R ));
        assertSame( hasU_S );
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU_R ));
        assertSame( removeU_S );
    }

    @Override
    @Test
    public void testParseGetU3() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getU3 );
        assertSame( PIMap.get( getU3 ), predicateInfo, getU3 );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU3_R ));
        Assert.assertNull( subjectInfo.getPredicateInfo( addU3_S ));
        assertSame( getU3 );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU3_R ));
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU3_S ));
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU3_R ));
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU3_S ));
    }

    @Override
    @Test
    public void testParseGetDbl() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getDbl );
        assertSame( PIMap.get( getDbl ), predicateInfo, getDbl );
        Assert.assertNull( subjectInfo.getPredicateInfo( addDbl ));
        assertSame( getDbl );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasDbl ));
        Assert.assertNull( subjectInfo.getPredicateInfo( removeDbl ));
    }

    @Override
    @Test
    public void testParseGetEnt() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getEnt );
        assertSame( PIMap.get( getEnt ), predicateInfo, getEnt );
        Assert.assertNull( subjectInfo.getPredicateInfo( addEnt ));
        assertSame( getEnt );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasEnt ));
        Assert.assertNull( subjectInfo.getPredicateInfo( removeEnt ));
    }

    @Override
    @Test
    public void testParseGetFlt() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getFlt );
        assertSame( PIMap.get( getFlt ), predicateInfo, getFlt );
        Assert.assertNull( subjectInfo.getPredicateInfo( addFlt ));
        assertSame( getFlt );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasFlt ));
        Assert.assertNull( subjectInfo.getPredicateInfo( removeFlt ));
    }

    @Override
    @Test
    public void testParseGetInt() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getInt );
        assertSame( PIMap.get( getInt ), predicateInfo, getInt );
        Assert.assertNull( subjectInfo.getPredicateInfo( addInt ));
        assertSame( getInt );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasInt ));
        Assert.assertNull( subjectInfo.getPredicateInfo( removeInt ));
    }

    @Override
    @Test
    public void testParseGetLng() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getLng );
        assertSame( PIMap.get( getLng ), predicateInfo, getLng );
        Assert.assertNull( subjectInfo.getPredicateInfo( addLng ));
        assertSame( getLng );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasLng ));
        Assert.assertNull( subjectInfo.getPredicateInfo( removeLng ));
    }

    @Override
    @Test
    public void testParseGetRDF() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getRDF );
        assertSame( PIMap.get( getRDF ), predicateInfo, getRDF );
        Assert.assertNull( subjectInfo.getPredicateInfo( addRDF ));
        assertSame( getRDF );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasRDF ));
        Assert.assertNull( subjectInfo.getPredicateInfo( removeRDF ));
    }

    @Override
    @Test
    public void testParseGetStr() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getStr );
        assertSame( PIMap.get( getStr ), predicateInfo, getStr );
        Assert.assertNull( subjectInfo.getPredicateInfo( addStr ));
        assertSame( getStr );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasStr ));
        Assert.assertNull( subjectInfo.getPredicateInfo( removeStr ));
    }

    @Override
    @Test
    public void testParseGetU() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getU );
        assertSame( PIMap.get( getU ), predicateInfo, getU );
        Assert.assertNull( subjectInfo.getPredicateInfo( addU_R ));
        Assert.assertNull( subjectInfo.getPredicateInfo( addU_S ));
        assertSame( getU );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU_R ));
        Assert.assertNull( subjectInfo.getPredicateInfo( hasU_S ));
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU_R ));
        Assert.assertNull( subjectInfo.getPredicateInfo( removeU_S ));
    }

    @Override
    @Test
    public void testParseGetBool() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getBool );
        assertSame( PIMap.get( getBool ), predicateInfo, getBool );
        Assert.assertNull( subjectInfo.getPredicateInfo( addBool ));
        assertSame( getBool );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasBool ));
        Assert.assertNull( subjectInfo.getPredicateInfo( removeBool ));
    }

    @Override
    @Test
    public void testParseGetChar() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getChar );
        assertSame( PIMap.get( getChar ), predicateInfo, getChar );
        Assert.assertNull( subjectInfo.getPredicateInfo( addChar ));
        assertSame( getChar );
        Assert.assertNull( subjectInfo.getPredicateInfo( hasChar ));
        Assert.assertNull( subjectInfo.getPredicateInfo( removeChar ));
    }
}
