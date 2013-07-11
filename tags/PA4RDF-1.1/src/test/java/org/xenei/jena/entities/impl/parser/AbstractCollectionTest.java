package org.xenei.jena.entities.impl.parser;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.impl.ActionType;

public abstract class AbstractCollectionTest extends BaseAbstractParserTest
{

	protected AbstractCollectionTest( final Class<?> classUnderTest )
	{
		super(classUnderTest);
	}

	@Test
	public void testCollectionGetter() throws Exception
	{
		final Method m = classUnderTest.getMethod("getX");
		final PredicateInfo pi = parser.parse(m);
		Assert.assertNotNull("getX not parsed", pi);
		Assert.assertEquals(ActionType.GETTER, pi.getActionType());
		Assert.assertEquals("getX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(List.class, pi.getValueClass());

	}

	@Test
	public void testCollectionHas() throws Exception
	{
		final Method m = classUnderTest.getMethod("hasX", String.class);
		final PredicateInfo pi = parser.parse(m);
		Assert.assertNotNull("hasX not parsed", pi);
		Assert.assertEquals(ActionType.EXISTENTIAL, pi.getActionType());
		Assert.assertEquals("hasX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

	}

	@Test
	public void testCollectionRemove() throws Exception
	{
		final Method m = classUnderTest.getMethod("removeX", String.class);
		final PredicateInfo pi = parser.parse(m);
		Assert.assertNotNull("removeX not parsed", pi);
		Assert.assertEquals(ActionType.REMOVER, pi.getActionType());
		Assert.assertEquals("removeX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

	}

	@Test
	public void testCollectionSetter() throws Exception
	{
		final Method m = classUnderTest.getMethod("addX", String.class);
		PredicateInfo pi = parser.parse(m);
		Assert.assertNotNull("addX not parsed", pi);
		Assert.assertEquals(ActionType.SETTER, pi.getActionType());
		Assert.assertEquals("addX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

		pi = subjectInfo.getPredicateInfo(classUnderTest.getMethod("getX"));
		Assert.assertNotNull("getX not parsed", pi);
		Assert.assertEquals(ActionType.GETTER, pi.getActionType());
		Assert.assertEquals("getX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(List.class, pi.getValueClass());

		pi = subjectInfo.getPredicateInfo(classUnderTest.getMethod("hasX",
				String.class));
		Assert.assertNotNull("hasX not parsed", pi);
		Assert.assertEquals(ActionType.EXISTENTIAL, pi.getActionType());
		Assert.assertEquals("hasX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

		pi = subjectInfo.getPredicateInfo(classUnderTest.getMethod("removeX",
				String.class));
		Assert.assertNotNull("removeX not parsed", pi);
		Assert.assertEquals(ActionType.REMOVER, pi.getActionType());
		Assert.assertEquals("removeX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());
	}
}
