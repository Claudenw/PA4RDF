package org.xenei.jena.entities.impl.parser;

import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.RDFNode;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.impl.ObjectHandler;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.handlers.EntityHandler;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.testing.iface.TestInterface;

public abstract class AbstractCollectionValueTest extends
		BaseAbstractParserTest
{

	private static final String NS = "http://localhost/test#";

	protected AbstractCollectionValueTest( final Class<?> classUnderTest )
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
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals("addBool", pi.getMethodName());
		Assertions.assertEquals(Boolean.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "bool",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeBool", Boolean.class);
		pi = (PredicateInfoImpl) parser.parse(m);

		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals("removeBool", pi.getMethodName());
		Assertions.assertEquals(Boolean.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "bool",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

	}

	@Test
	public void testChar() throws Exception
	{
		Method m = classUnderTest.getMethod("getChar");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Character.class));
		Assertions.assertEquals("getChar", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(List.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "char",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addChar", Character.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("addChar", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Character.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "char",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeChar", Character.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("removeChar", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Character.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "char",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasChar", Character.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("hasChar", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Character.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "char",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

	}

	@Test
	public void testDbl() throws Exception
	{
		Method m = classUnderTest.getMethod("getDbl");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Double.class));
		Assertions.assertEquals("getDbl", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Queue.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "dbl",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addDbl", Double.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("addDbl", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Double.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "dbl",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeDbl", Double.class);
		pi = (PredicateInfoImpl) parser.parse(m);

		Assertions.assertEquals("removeDbl", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Double.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "dbl",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasDbl", Double.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("hasDbl", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Double.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "dbl",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

	}

	@Test
	public void testEntity() throws Exception
	{
		Method m = classUnderTest.getMethod("getEnt");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);

		Assertions.assertEquals("getEnt", pi.getMethodName());
		final ObjectHandler handler = new EntityHandler(
				EntityManagerFactory.getEntityManager(), TestInterface.class);

		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Queue.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "ent",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addEnt", TestInterface.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("addEnt", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(TestInterface.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "ent",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeEnt", TestInterface.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("removeEnt", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(TestInterface.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "ent",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasEnt", TestInterface.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("hasEnt", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(TestInterface.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "ent",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());
	}

	@Test
	public void testFlt() throws Exception
	{
		Method m = classUnderTest.getMethod("getFlt");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Float.class));

		Assertions.assertEquals("getFlt", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Set.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "flt",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addFlt", Float.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("addFlt", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Float.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "flt",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeFlt", Float.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("removeFlt", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Float.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "flt",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasFlt", Float.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("hasFlt", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Float.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "flt",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());
	}

	@Test
	public void testInt() throws Exception
	{
		Method m = classUnderTest.getMethod("getInt");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Integer.class));

		Assertions.assertEquals("getInt", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Queue.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "int",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addInt", Integer.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("addInt", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Integer.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "int",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeInt", Integer.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("removeInt", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Integer.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "int",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasInt", Integer.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("hasInt", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Integer.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "int",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());
	}

	@Test
	public void testLng() throws Exception
	{
		Method m = classUnderTest.getMethod("getLng");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Long.class));

		Assertions.assertEquals("getLng", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(List.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "lng",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addLng", Long.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("addLng", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Long.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "lng",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeLng", Long.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("removeLng", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Long.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "lng",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasLng", Long.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("hasLng", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Long.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "lng",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

	}

	@Test
	public void testRdf() throws Exception
	{
		Method m = classUnderTest.getMethod("getRDF");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler handler = new ResourceHandler();

		Assertions.assertEquals("getRDF", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(List.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "rDF",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addRDF", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("addRDF", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(RDFNode.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "rDF",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeRDF", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("removeRDF", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(RDFNode.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "rDF",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasRDF", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("hasRDF", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(RDFNode.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "rDF",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());
	}

	@Test
	public void testStr() throws Exception
	{
		Method m = classUnderTest.getMethod("getStr");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(String.class));

		Assertions.assertEquals("getStr", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(Set.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "str",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addStr", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("addStr", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(String.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "str",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeStr", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("removeStr", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(String.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "str",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasStr", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("hasStr", pi.getMethodName());
		Assertions.assertEquals(handler, pi.getObjectHandler());
		Assertions.assertEquals(String.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "str",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());
	}

	@Test
	public void testURI() throws Exception
	{
		Method m = classUnderTest.getMethod("getU");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler resHandler = new ResourceHandler();
		final ObjectHandler uriHandler = new UriHandler();

		Assertions.assertEquals("getU", pi.getMethodName());
		Assertions.assertEquals(resHandler, pi.getObjectHandler());
		Assertions.assertEquals(Set.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addU", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("addU", pi.getMethodName());
		Assertions.assertEquals(uriHandler, pi.getObjectHandler());
		Assertions.assertEquals(String.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addU", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("addU", pi.getMethodName());
		Assertions.assertEquals(resHandler, pi.getObjectHandler());
		Assertions.assertEquals(RDFNode.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeU", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("removeU", pi.getMethodName());
		Assertions.assertEquals(resHandler, pi.getObjectHandler());
		Assertions.assertEquals(RDFNode.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeU", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("removeU", pi.getMethodName());
		Assertions.assertEquals(uriHandler, pi.getObjectHandler());
		Assertions.assertEquals(String.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasU", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("hasU", pi.getMethodName());
		Assertions.assertEquals(uriHandler, pi.getObjectHandler());
		Assertions.assertEquals(String.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasU", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("hasU", pi.getMethodName());
		Assertions.assertEquals(resHandler, pi.getObjectHandler());
		Assertions.assertEquals(RDFNode.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("getU2");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("getU2", pi.getMethodName());
		Assertions.assertEquals(uriHandler, pi.getObjectHandler());
		Assertions.assertEquals(List.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());
	}

	@Test
	public void testURIOrdering() throws Exception
	{
		Method m = classUnderTest.getMethod("getU3");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m);
		final ObjectHandler resHandler = new ResourceHandler();
		final ObjectHandler uriHandler = new UriHandler();

		Assertions.assertEquals("getU3", pi.getMethodName());
		Assertions.assertEquals(resHandler, pi.getObjectHandler());
		Assertions.assertEquals(Queue.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u3",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addU3", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("addU3", pi.getMethodName());
		Assertions.assertEquals(uriHandler, pi.getObjectHandler());
		Assertions.assertEquals(String.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u3",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("addU3", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("addU3", pi.getMethodName());
		Assertions.assertEquals(resHandler, pi.getObjectHandler());
		Assertions.assertEquals(RDFNode.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u3",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeU3", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("removeU3", pi.getMethodName());
		Assertions.assertEquals(resHandler, pi.getObjectHandler());
		Assertions.assertEquals(RDFNode.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u3",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("removeU3", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("removeU3", pi.getMethodName());
		Assertions.assertEquals(uriHandler, pi.getObjectHandler());
		Assertions.assertEquals(String.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u3",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasU3", String.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("hasU3", pi.getMethodName());
		Assertions.assertEquals(uriHandler, pi.getObjectHandler());
		Assertions.assertEquals(String.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u3",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("hasU3", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("hasU3", pi.getMethodName());
		Assertions.assertEquals(resHandler, pi.getObjectHandler());
		Assertions.assertEquals(RDFNode.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u3",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());

		m = classUnderTest.getMethod("getU4");
		pi = (PredicateInfoImpl) parser.parse(m);
		Assertions.assertEquals("getU4", pi.getMethodName());
		Assertions.assertEquals(uriHandler, pi.getObjectHandler());
		Assertions.assertEquals(Set.class, pi.getValueClass());
		Assertions.assertEquals(AbstractCollectionValueTest.NS + "u3",
				pi.getUriString());
		Assertions.assertEquals(AbstractCollectionValueTest.NS, pi.getNamespace());
	}

}
