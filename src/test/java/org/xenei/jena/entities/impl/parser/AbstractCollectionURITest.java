package org.xenei.jena.entities.impl.parser;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;

public abstract class AbstractCollectionURITest extends BaseAbstractParserTest
{

	protected AbstractCollectionURITest( final Class<?> classUnderTest )
	{
		super(classUnderTest);
	}

	@Test
	public void testStandardGetter() throws Exception
	{
		final Method m = classUnderTest.getMethod("getU");
		final PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertNotNull("getU not parsed", pi);
		Assert.assertEquals(ResourceHandler.class, pi.getObjectHandler()
				.getClass());
		Assert.assertEquals(ActionType.GETTER, pi.getActionType());
		Assert.assertEquals("getU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(List.class, pi.getValueClass());
	}

	@Test
	public void testStandardGetter2() throws Exception
	{
		final Method m = classUnderTest.getMethod("getU2");
		final PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertNotNull("getU2 not parsed", pi);
		Assert.assertEquals(ResourceHandler.class, pi.getObjectHandler()
				.getClass());
		Assert.assertEquals(ActionType.GETTER, pi.getActionType());
		Assert.assertEquals("getU2", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u2", pi.getUriString());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
	}

	@Test
	public void testStandardHas() throws Exception
	{
		final Method m = classUnderTest.getMethod("hasU", String.class);
		final PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertNotNull("hasU not parsed", pi);
		Assert.assertEquals(UriHandler.class, pi.getObjectHandler().getClass());
		Assert.assertEquals(ActionType.EXISTENTIAL, pi.getActionType());
		Assert.assertEquals("hasU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

	}

	@Test
	public void testStandardHas2() throws Exception
	{
		final Method m = classUnderTest.getMethod("hasU2", String.class);
		final PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertNotNull("hasU2 not parsed", pi);
		Assert.assertEquals(UriHandler.class, pi.getObjectHandler().getClass());
		Assert.assertEquals(ActionType.EXISTENTIAL, pi.getActionType());
		Assert.assertEquals("hasU2", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u2", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

	}

	@Test
	public void testStandardRemove() throws Exception
	{
		final Method m = classUnderTest.getMethod("removeU", String.class);
		final PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertNotNull("removeU not parsed", pi);
		Assert.assertEquals(UriHandler.class, pi.getObjectHandler().getClass());
		Assert.assertEquals(ActionType.REMOVER, pi.getActionType());
		Assert.assertEquals("removeU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

	}

	@Test
	public void testStandardRemove2() throws Exception
	{
		final Method m = classUnderTest.getMethod("removeU2", String.class);
		final PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertNotNull("removeU2 not parsed", pi);
		Assert.assertEquals(UriHandler.class, pi.getObjectHandler().getClass());
		Assert.assertEquals(ActionType.REMOVER, pi.getActionType());
		Assert.assertEquals("removeU2", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u2", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

	}

	@Test
	public void testStandardSetter() throws Exception
	{
		final Method m = classUnderTest.getMethod("addU", String.class);
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertNotNull("addU( String ) not parsed", pi);
		Assert.assertEquals(UriHandler.class, pi.getObjectHandler().getClass());
		Assert.assertEquals(ActionType.SETTER, pi.getActionType());
		Assert.assertEquals("addU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(UriHandler.class, pi.getObjectHandler().getClass());
		Assert.assertEquals(String.class, pi.getValueClass());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo(classUnderTest
				.getMethod("addU", RDFNode.class));
		Assert.assertNotNull("addU( RDFNode ) not parsed", pi);
		Assert.assertEquals(ActionType.SETTER, pi.getActionType());
		Assert.assertEquals("addU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo(classUnderTest
				.getMethod("getU"));
		Assert.assertNotNull("getU not parsed", pi);
		Assert.assertEquals(ResourceHandler.class, pi.getObjectHandler()
				.getClass());
		Assert.assertEquals(ActionType.GETTER, pi.getActionType());
		Assert.assertEquals("getU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(List.class, pi.getValueClass());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo(classUnderTest
				.getMethod("hasU", String.class));
		Assert.assertNotNull("hasU not parsed", pi);
		Assert.assertEquals(UriHandler.class, pi.getObjectHandler().getClass());
		Assert.assertEquals(ActionType.EXISTENTIAL, pi.getActionType());
		Assert.assertEquals("hasU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo(classUnderTest
				.getMethod("removeU", String.class));
		Assert.assertNotNull("removeU not parsed", pi);
		Assert.assertEquals(UriHandler.class, pi.getObjectHandler().getClass());
		Assert.assertEquals(ActionType.REMOVER, pi.getActionType());
		Assert.assertEquals("removeU", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());
	}

	@Test
	public void testStandardSetter2() throws Exception
	{
		final Method m = classUnderTest.getMethod("addU2", RDFNode.class);
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		Assert.assertNotNull("addU2( RDFNode ) not parsed", pi);
		Assert.assertEquals(ResourceHandler.class, pi.getObjectHandler()
				.getClass());
		Assert.assertEquals(ActionType.SETTER, pi.getActionType());
		Assert.assertEquals("addU2", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u2", pi.getUriString());
		Assert.assertEquals(ResourceHandler.class, pi.getObjectHandler()
				.getClass());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo(classUnderTest
				.getMethod("addU2", String.class));
		Assert.assertNotNull("addU2( String ) not parsed", pi);
		Assert.assertEquals(ActionType.SETTER, pi.getActionType());
		Assert.assertEquals("addU2", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u2", pi.getUriString());
		Assert.assertEquals(UriHandler.class, pi.getObjectHandler().getClass());
		Assert.assertEquals(String.class, pi.getValueClass());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo(classUnderTest
				.getMethod("getU2"));
		Assert.assertNotNull("getU2 not parsed", pi);
		Assert.assertEquals(ActionType.GETTER, pi.getActionType());
		Assert.assertEquals(ResourceHandler.class, pi.getObjectHandler()
				.getClass());
		Assert.assertEquals("getU2", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u2", pi.getUriString());
		Assert.assertEquals(ResourceHandler.class, pi.getObjectHandler()
				.getClass());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo(classUnderTest
				.getMethod("hasU2", String.class));
		Assert.assertNotNull("hasU2 not parsed", pi);
		Assert.assertEquals(UriHandler.class, pi.getObjectHandler().getClass());
		Assert.assertEquals(ActionType.EXISTENTIAL, pi.getActionType());
		Assert.assertEquals("hasU2", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u2", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo(classUnderTest
				.getMethod("removeU2", RDFNode.class));
		Assert.assertNotNull("removeU2( RDFNode ) not parsed", pi);
		Assert.assertEquals(ResourceHandler.class, pi.getObjectHandler()
				.getClass());
		Assert.assertEquals(ActionType.REMOVER, pi.getActionType());
		Assert.assertEquals("removeU2", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u2", pi.getUriString());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo(classUnderTest
				.getMethod("removeU2", String.class));
		Assert.assertNotNull("removeU2( String ) not parsed", pi);
		Assert.assertEquals(UriHandler.class, pi.getObjectHandler().getClass());
		Assert.assertEquals(ActionType.REMOVER, pi.getActionType());
		Assert.assertEquals("removeU2", pi.getMethodName());
		Assert.assertEquals("http://example.com/", pi.getNamespace());
		Assert.assertEquals("http://example.com/u2", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());
	}
}
