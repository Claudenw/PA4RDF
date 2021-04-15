package org.xenei.jena.entities.impl.parser;

import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.impl.ObjectHandler;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.handlers.EntityHandler;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.testing.iface.TestInterface;

public abstract class AbstractMultiValueTest extends BaseAbstractParserTest
{
	private static final String NS = "http://localhost/test#";

	protected AbstractMultiValueTest( final Class<?> classUnderTest )
	{
		super(classUnderTest);
	}

	@Test
	public void testBoolean() throws Exception
	{
		Method m = classUnderTest.getMethod("addBool", Boolean.class);
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Boolean.class));
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals("addBool", pi.getMethodName());
		Assert.assertEquals(Boolean.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "bool",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeBool", Boolean.class);
		pi = (PredicateInfoImpl) parser.parse(m);

		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals("removeBool", pi.getMethodName());
		Assert.assertEquals(Boolean.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "bool",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

	}

	@Test
	public void testChar() throws Exception
	{
		Method m = classUnderTest.getMethod("getChar");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Character.class));
		Assert.assertEquals("getChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addChar", Character.class);
		pi = (PredicateInfoImpl) parser.parse(m);

		Assert.assertEquals("addChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeChar", Character.class);
		pi = (PredicateInfoImpl) parser.parse(m);

		Assert.assertEquals("removeChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasChar", Character.class);
		pi = (PredicateInfoImpl) parser.parse(m);

		Assert.assertEquals("hasChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

	}

	@Test
	public void testDbl() throws Exception
	{
		Method m = classUnderTest.getMethod("getDbl");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Double.class));
		Assert.assertEquals("getDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addDbl", Double.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("addDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeDbl", Double.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasDbl", Double.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("hasDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

	}

	@Test
	public void testEntity() throws Exception
	{
		Method m = classUnderTest.getMethod("getEnt");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		Assert.assertEquals("getEnt", pi.getMethodName());
		final ObjectHandler handler = new EntityHandler(
				EntityManagerFactory.getEntityManager(), TestInterface.class);

		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "ent",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addEnt", TestInterface.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("addEnt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(TestInterface.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "ent",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeEnt", TestInterface.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeEnt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(TestInterface.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "ent",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasEnt", TestInterface.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("hasEnt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(TestInterface.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "ent",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());
	}

	@Test
	public void testFlt() throws Exception
	{
		Method m = classUnderTest.getMethod("getFlt");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Float.class));

		Assert.assertEquals("getFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addFlt", Float.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("addFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeFlt", Float.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasFlt", Float.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("hasFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());
	}

	@Test
	public void testInt() throws Exception
	{
		Method m = classUnderTest.getMethod("getInt");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Integer.class));

		Assert.assertEquals("getInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addInt", Integer.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("addInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeInt", Integer.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasInt", Integer.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("hasInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());
	}

	@Test
	public void testLng() throws Exception
	{
		Method m = classUnderTest.getMethod("getLng");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Long.class));

		Assert.assertEquals("getLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addLng", Long.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("addLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeLng", Long.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasLng", Long.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("hasLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());
	}

	@Test
	public void testRdf() throws Exception
	{
		Method m = classUnderTest.getMethod("getRDF");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler handler = new ResourceHandler();

		Assert.assertEquals("getRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "rDF",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addRDF", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("addRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "rDF",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeRDF", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "rDF",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasRDF", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("hasRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "rDF",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());
	}

	@Test
	public void testStr() throws Exception
	{
		Method m = classUnderTest.getMethod("getStr");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(String.class));

		Assert.assertEquals("getStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "str",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addStr", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("addStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "str",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeStr", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "str",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasStr", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("hasStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "str",
				pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());
	}

	@Test
	public void testURI() throws Exception
	{
		Method m = classUnderTest.getMethod("getU");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler resHandler = new ResourceHandler();
		final ObjectHandler uriHandler = new UriHandler();

		Assert.assertEquals("getU", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addU", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("addU", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addU", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("addU", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeU", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeU", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeU", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeU", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasU", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("hasU", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasU", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("hasU", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("getU2");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("getU2", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());
	}

	@Test
	public void testURIOrdering() throws Exception
	{
		Method m = classUnderTest.getMethod("getU3");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler resHandler = new ResourceHandler();
		final ObjectHandler uriHandler = new UriHandler();

		Assert.assertEquals("getU3", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u3", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addU3", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("addU3", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u3", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addU3", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("addU3", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u3", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeU3", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeU3", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u3", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeU3", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeU3", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u3", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasU3", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("hasU3", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u3", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasU3", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("hasU3", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u3", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("getU4");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("getU4", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(AbstractMultiValueTest.NS + "u3", pi.getUriString());
		Assert.assertEquals(AbstractMultiValueTest.NS, pi.getNamespace());
	}
}
