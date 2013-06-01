package org.xenei.jena.entities.impl.parser;

import com.hp.hpl.jena.rdf.model.RDFNode;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.EffectivePredicate;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;

public abstract class AbstractSimpleURITest extends BaseAbstractParserTest
{
	protected AbstractSimpleURITest( final Class<?> classUnderTest )
	{
		super(classUnderTest);
	}

	@Test
	public void testStandardGetter() throws Exception
	{
		final Method m = classUnderTest.getMethod("getU");
		final PredicateInfo pi = parser.parse(m);
		Assert.assertNotNull("getU not parsed", pi);
		Assert.assertEquals(ActionType.GETTER, pi.getActionType());
		Assert.assertEquals("getU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
	}

	@Test
	public void testStandardHas() throws Exception
	{
		final Method m = classUnderTest.getMethod("hasU");
		final PredicateInfo pi = parser.parse(m);
		Assert.assertNotNull("hasU not parsed", pi);
		Assert.assertEquals(ActionType.EXISTENTIAL, pi.getActionType());
		Assert.assertEquals("hasU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(boolean.class, pi.getValueClass());

	}

	@Test
	public void testStandardRemove() throws Exception
	{
		final Method m = classUnderTest.getMethod("removeU");
		final PredicateInfo pi = parser.parse(m);
		Assert.assertNotNull("removeU not parsed", pi);
		Assert.assertEquals(ActionType.REMOVER, pi.getActionType());
		Assert.assertEquals("removeU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(null, pi.getValueClass());

	}

	@Test
	public void testStandardSetter() throws Exception
	{
		final Method m = classUnderTest.getMethod("setU", String.class);
		PredicateInfo pi = parser.parse(m);
		Assert.assertNotNull("setU( String ) not parsed", pi);
		Assert.assertEquals(ActionType.SETTER, pi.getActionType());
		Assert.assertEquals("setU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(UriHandler.class, ((PredicateInfoImpl) pi)
				.getObjectHandler().getClass());
		Assert.assertEquals(String.class, pi.getValueClass());
		/*
		 * pi = subjectInfo.getPredicateInfo( classUnderTest.getMethod( "setU",
		 * RDFNode.class ) );
		 * assertNotNull( "setU( RDFNode ) not parsed", pi );
		 * assertEquals( ActionType.SETTER, pi.getActionType());
		 * assertEquals( "setU", pi.getMethodName());
		 * assertEquals( "http://example.com/", pi.getNamespace());
		 * assertEquals( "http://example.com/u", pi.getUriString());
		 * assertEquals( RDFNode.class, pi.getValueClass());
		 */
		pi = subjectInfo.getPredicateInfo(classUnderTest.getMethod("getU"));
		Assert.assertNotNull("getU not parsed", pi);
		Assert.assertEquals(ActionType.GETTER, pi.getActionType());
		Assert.assertEquals("getU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(ResourceHandler.class, ((PredicateInfoImpl) pi)
				.getObjectHandler().getClass());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());

		pi = subjectInfo.getPredicateInfo(classUnderTest.getMethod("hasU"));
		Assert.assertNotNull("hasU not parsed", pi);
		Assert.assertEquals(ActionType.EXISTENTIAL, pi.getActionType());
		Assert.assertEquals("hasU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(boolean.class, pi.getValueClass());

		pi = subjectInfo.getPredicateInfo(classUnderTest.getMethod("removeU"));
		Assert.assertNotNull("removeU not parsed", pi);
		Assert.assertEquals(ActionType.REMOVER, pi.getActionType());
		Assert.assertEquals("removeU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(null, pi.getValueClass());
	}
}
