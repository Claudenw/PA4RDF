package org.xenei.jena.entities.impl.parser;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.EffectivePredicate;

abstract public class AbstractSimpleTest extends BaseAbstractParserTest
{

	protected AbstractSimpleTest( final Class<?> classUnderTest )
	{
		super(classUnderTest);
	}

	@Test
	public void testStandardGetter() throws Exception
	{
		final Method m = classUnderTest.getMethod("getX");
		final PredicateInfo pi = parser.parse(m);
		Assert.assertNotNull("getX not parsed", pi);
		Assert.assertEquals(ActionType.GETTER, pi.getActionType());
		Assert.assertEquals("getX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

	}

	@Test
	public void testStandardHas() throws Exception
	{
		final Method m = classUnderTest.getMethod("hasX");
		final PredicateInfo pi = parser.parse(m);
		Assert.assertNotNull("hasX not parsed", pi);
		Assert.assertEquals(ActionType.EXISTENTIAL, pi.getActionType());
		Assert.assertEquals("hasX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(boolean.class, pi.getValueClass());

	}

	@Test
	public void testStandardRemove() throws Exception
	{
		final Method m = classUnderTest.getMethod("removeX");
		final PredicateInfo pi = parser.parse(m);
		Assert.assertNotNull("removeX not parsed", pi);
		Assert.assertEquals(ActionType.REMOVER, pi.getActionType());
		Assert.assertEquals("removeX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(null, pi.getValueClass());

	}

	@Test
	public void testStandardSetter() throws Exception
	{
		final Method m = classUnderTest.getMethod("setX", String.class);
		PredicateInfo pi = parser.parse(m);
		Assert.assertNotNull("setX not parsed", pi);
		Assert.assertEquals(ActionType.SETTER, pi.getActionType());
		Assert.assertEquals("setX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

		pi = subjectInfo.getPredicateInfo(classUnderTest.getMethod("getX"));
		Assert.assertNotNull("getX not parsed", pi);
		Assert.assertEquals(ActionType.GETTER, pi.getActionType());
		Assert.assertEquals("getX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

		pi = subjectInfo.getPredicateInfo(classUnderTest.getMethod("hasX"));
		Assert.assertNotNull("hasX not parsed", pi);
		Assert.assertEquals(ActionType.EXISTENTIAL, pi.getActionType());
		Assert.assertEquals("hasX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(boolean.class, pi.getValueClass());

		pi = subjectInfo.getPredicateInfo(classUnderTest.getMethod("removeX"));
		Assert.assertNotNull("removeX not parsed", pi);
		Assert.assertEquals(ActionType.REMOVER, pi.getActionType());
		Assert.assertEquals("removeX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(null, pi.getValueClass());
	}
}
