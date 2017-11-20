package org.xenei.jena.entities.impl.parser;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.testing.impl.SimpleInterfaceImpl;

public class SimpleInterfaceImplTest extends AbstractSimpleTest
{
	public SimpleInterfaceImplTest()
	{
		super(SimpleInterfaceImpl.class);
	}

	@Test
	public void testPostExec() throws Exception
	{
		final Method m = classUnderTest.getMethod("getX");
		final PredicateInfo pi = parser.parse(m);
		Assert.assertNotNull("getX not parsed", pi);
		Assert.assertEquals(ActionType.GETTER, pi.getActionType());
		Assert.assertEquals("getX", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/x", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertFalse(pi.getPostExec().isEmpty());

	}

	@Override
	protected Class<?>[] getGetAnnotations()
	{
		return new Class<?>[] { Predicate.class };
	}

	@Override
	protected Class<?>[] getHasAnnotations()
	{
		return new Class<?>[] { Predicate.class };
	}

	@Override
	protected Class<?>[] getRemoveAnnotations()
	{
		return new Class<?>[] { Predicate.class };
	}

	@Override
	protected Class<?>[] getSetAnnotations()
	{
		return new Class<?>[] { Predicate.class };
	}
}
