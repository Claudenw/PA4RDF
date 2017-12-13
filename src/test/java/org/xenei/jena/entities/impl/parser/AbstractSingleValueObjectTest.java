package org.xenei.jena.entities.impl.parser;

import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.RDFNode;

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
import org.xenei.jena.entities.impl.handlers.VoidHandler;
import org.xenei.jena.entities.testing.iface.TestInterface;

public abstract class AbstractSingleValueObjectTest extends
		BaseAbstractParserTest
{

	private static String NS = "http://localhost/test#";

	protected AbstractSingleValueObjectTest( final Class<?> classUnderTest )
	{
		super(classUnderTest);
	}

	@Test
	public void testBoolean() throws Exception
	{
		Method m = classUnderTest.getMethod("isBool");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Boolean.class));

		Assert.assertEquals("isBool", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(Boolean.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "bool",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("setBool", Boolean.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("setBool", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(Boolean.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "bool",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeBool");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeBool", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( entityManager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "bool",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());
	}

	@Test
	public void testChar() throws Exception
	{
		Method m = classUnderTest.getMethod("getChar");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Character.class));

		Assert.assertEquals("getChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("setChar", Character.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("setChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeChar");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeChar", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( entityManager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());
	}

	@Test
	public void testDbl() throws Exception
	{
		Method m = classUnderTest.getMethod("getDbl");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Double.class));

		Assert.assertEquals("getDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("setDbl", Double.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("setDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeDbl");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeDbl", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( entityManager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());
	}

	@Test
	public void testEntity() throws Exception
	{
		Method m = classUnderTest.getMethod("getEnt");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		final ObjectHandler handler = new EntityHandler(
				EntityManagerFactory.create(), TestInterface.class);

		Assert.assertEquals("getEnt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(TestInterface.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "ent",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("setEnt", TestInterface.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("setEnt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(TestInterface.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "ent",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeEnt");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeEnt", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( entityManager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "ent",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());
	}

	@Test
	public void testFlt() throws Exception
	{
		Method m = classUnderTest.getMethod("getFlt");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Float.class));

		Assert.assertEquals("getFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("setFlt", Float.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("setFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeFlt");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeFlt", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( entityManager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

	}

	@Test
	public void testInt() throws Exception
	{

		Method m = classUnderTest.getMethod("getInt");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Integer.class));

		Assert.assertEquals("getInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("setInt", Integer.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("setInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeInt");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeInt", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( entityManager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

	}

	@Test
	public void testLng() throws Exception
	{
		Method m = classUnderTest.getMethod("getLng");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Long.class));

		Assert.assertEquals("getLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("setLng", Long.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("setLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeLng");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeLng", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( entityManager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

	}

	@Test
	public void testRdf() throws Exception
	{
		Method m = classUnderTest.getMethod("getRDF");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler handler = new ResourceHandler();

		Assert.assertEquals("getRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "rDF",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("setRDF", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("setRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "rDF",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeRDF");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeRDF", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( entityManager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "rDF",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());
	}

	@Test
	public void testStr() throws Exception
	{
		Method m = classUnderTest.getMethod("getStr");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(String.class));

		Assert.assertEquals("getStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "str",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("setStr", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("setStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "str",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeStr");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeStr", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( entityManager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "str",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());
	}

	@Test
	public void testURI() throws Exception
	{
		Method m = classUnderTest.getMethod("getU");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		final ObjectHandler uriHandler = new UriHandler();
		final ObjectHandler rdfHandler = new ResourceHandler();

		Assert.assertEquals("getU", pi.getMethodName());
		Assert.assertEquals(rdfHandler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "u",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("setU", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("setU", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "u",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("setU", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("setU", pi.getMethodName());
		Assert.assertEquals(rdfHandler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "u",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeU");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("removeU", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( entityManager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "u",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("getU2");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assert.assertEquals("getU2", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler( entityManager ));
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS + "u",
				pi.getUriString());
		Assert.assertEquals(AbstractSingleValueObjectTest.NS, pi.getNamespace());
	}

}
