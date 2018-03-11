package org.xenei.pa4rdf.bean.impl.parser.singleValue;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.pa4rdf.bean.PredicateInfo;
import org.xenei.pa4rdf.bean.exceptions.MissingAnnotation;
import org.xenei.pa4rdf.bean.test.impl.SingleValueObjectImpl;

public class SingleValueObjectImplTest extends AbstractSingleValueObjectTest
{

	public SingleValueObjectImplTest()
			throws NoSuchMethodException, SecurityException
	{
		super(SingleValueObjectImpl.class);
	}

	@Override
	@Test
	public void testGetChar() throws MissingAnnotation
	{
		final PredicateInfo predicateInfo = parser.parse(getChar);
		assertSame(PIMap.get(getChar), predicateInfo, getChar);
		assertSame(getChar);
		Assert.assertNull(subjectInfo.getPredicateInfo(setChar));
		Assert.assertNull(subjectInfo.getPredicateInfo(removeChar));
	}

	@Override
	@Test
	public void testGetU() throws MissingAnnotation
	{
		final PredicateInfo predicateInfo = parser.parse(getU);
		assertSame(PIMap.get(getU), predicateInfo, getU);
		assertSame(getU);
		Assert.assertNull(subjectInfo.getPredicateInfo(setU_R));
		Assert.assertNull(subjectInfo.getPredicateInfo(setU_S));
		Assert.assertNull(subjectInfo.getPredicateInfo(removeU));
		Assert.assertNull(subjectInfo.getPredicateInfo(getU2));
	}

	@Override
	@Test
	public void testGetDbl() throws MissingAnnotation
	{
		final PredicateInfo predicateInfo = parser.parse(getDbl);
		assertSame(PIMap.get(getDbl), predicateInfo, getDbl);
		assertSame(getDbl);
		Assert.assertNull(subjectInfo.getPredicateInfo(setDbl));
		Assert.assertNull(subjectInfo.getPredicateInfo(removeDbl));
	}

	@Override
	@Test
	public void testGetEnt() throws MissingAnnotation
	{
		final PredicateInfo predicateInfo = parser.parse(getEnt);
		assertSame(PIMap.get(getEnt), predicateInfo, getEnt);
		assertSame(getEnt);
		Assert.assertNull(subjectInfo.getPredicateInfo(setEnt));
		Assert.assertNull(subjectInfo.getPredicateInfo(removeEnt));
	}

	@Override
	@Test
	public void testGetFlt() throws MissingAnnotation
	{
		final PredicateInfo predicateInfo = parser.parse(getFlt);
		assertSame(PIMap.get(getFlt), predicateInfo, getFlt);
		assertSame(getFlt);
		Assert.assertNull(subjectInfo.getPredicateInfo(setFlt));
		Assert.assertNull(subjectInfo.getPredicateInfo(removeFlt));
	}

	@Override
	@Test
	public void testGetInt() throws MissingAnnotation
	{
		final PredicateInfo predicateInfo = parser.parse(getInt);
		assertSame(PIMap.get(getInt), predicateInfo, getInt);
		assertSame(getInt);
		Assert.assertNull(subjectInfo.getPredicateInfo(setInt));
		Assert.assertNull(subjectInfo.getPredicateInfo(removeInt));
	}

	@Override
	@Test
	public void testGetLng() throws MissingAnnotation
	{
		final PredicateInfo predicateInfo = parser.parse(getLng);
		assertSame(PIMap.get(getLng), predicateInfo, getLng);
		assertSame(getLng);
		Assert.assertNull(subjectInfo.getPredicateInfo(setLng));
		Assert.assertNull(subjectInfo.getPredicateInfo(removeLng));
	}

	@Override
	@Test
	public void testGetRDF() throws MissingAnnotation
	{
		final PredicateInfo predicateInfo = parser.parse(getRDF);
		assertSame(PIMap.get(getRDF), predicateInfo, getRDF);
		assertSame(getRDF);
		Assert.assertNull(subjectInfo.getPredicateInfo(setRDF));
		Assert.assertNull(subjectInfo.getPredicateInfo(removeRDF));
	}

	@Override
	@Test
	public void testGetStr() throws MissingAnnotation
	{
		final PredicateInfo predicateInfo = parser.parse(getStr);
		assertSame(PIMap.get(getStr), predicateInfo, getStr);
		assertSame(getStr);
		Assert.assertNull(subjectInfo.getPredicateInfo(setStr));
		Assert.assertNull(subjectInfo.getPredicateInfo(removeStr));
	}

	@Override
	@Test
	public void testIsBool() throws MissingAnnotation
	{
		final PredicateInfo predicateInfo = parser.parse(isBool);
		assertSame(PIMap.get(isBool), predicateInfo, isBool);
		assertSame(isBool);
		Assert.assertNull(subjectInfo.getPredicateInfo(setBool));
		Assert.assertNull(subjectInfo.getPredicateInfo(removeBool));
	}

	@Override
	@Test
	public void testSetU_R() throws MissingAnnotation
	{
		final PredicateInfo predicateInfo = parser.parse(setU_R);
		assertSame(PIMap.get(setU_R), predicateInfo, setU_R);
		assertSame(getU);
		assertSame(setU_R);
		Assert.assertNull(subjectInfo.getPredicateInfo(setU_S));
		assertSame(removeU);
		Assert.assertNull(subjectInfo.getPredicateInfo(getU2));
	}

	@Override
	@Test
	public void testSetU_S() throws MissingAnnotation
	{
		final PredicateInfo predicateInfo = parser.parse(setU_S);
		assertSame(PIMap.get(setU_S), predicateInfo, setU_S);
		assertSame(getU);
		Assert.assertNull(subjectInfo.getPredicateInfo(setU_R));
		assertSame(setU_S);
		assertSame(removeU);
		Assert.assertNull(subjectInfo.getPredicateInfo(getU2));
	}
}
