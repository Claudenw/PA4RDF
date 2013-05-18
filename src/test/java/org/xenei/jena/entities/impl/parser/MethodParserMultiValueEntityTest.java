package org.xenei.jena.entities.impl.parser;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.impl.EffectivePredicate;
import org.xenei.jena.entities.impl.ObjectHandler;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.handlers.EntityHandler;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.testing.abst.MultiValueObjectTestClass;
import org.xenei.jena.entities.testing.abst.TestClass;

public class MethodParserMultiValueEntityTest extends BaseAbstractParserTest
{
	private static final String NS = "http://localhost/test#";

	protected MethodParserMultiValueEntityTest( final Class<?> classUnderTest )
	{
		super(MultiValueObjectTestClass.class);
	}

	@Test
	public void testBoolean() throws Exception
	{
		Method m = MultiValueObjectTestClass.class.getMethod("addBool",
				Boolean.class);
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m,
				new EffectivePredicate(m));

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Boolean.class));
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals("addBool", pi.getMethodName());
		Assert.assertEquals(Boolean.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "bool",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("removeBool",
				Boolean.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));

		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals("removeBool", pi.getMethodName());
		Assert.assertEquals(Boolean.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "bool",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

	}

	@Test
	public void testChar() throws Exception
	{
		Method m = MultiValueObjectTestClass.class.getMethod("getChar");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m,
				new EffectivePredicate(m));

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Character.class));
		Assert.assertEquals("getChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("addChar",
				Character.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));

		Assert.assertEquals("addChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("removeChar",
				Character.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));

		Assert.assertEquals("removeChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("hasChar",
				Character.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));

		Assert.assertEquals("hasChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

	}

	@Test
	public void testDbl() throws Exception
	{
		Method m = MultiValueObjectTestClass.class.getMethod("getDbl");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m,
				new EffectivePredicate(m));

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Double.class));
		Assert.assertEquals("getDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("addDbl", Double.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("addDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class
				.getMethod("removeDbl", Double.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("removeDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("hasDbl", Double.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("hasDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

	}

	@Test
	public void testEntity() throws Exception
	{
		Method m = MultiValueObjectTestClass.class.getMethod("getEnt");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m,
				new EffectivePredicate(m));

		Assert.assertEquals("getEnt", pi.getMethodName());
		final ObjectHandler handler = new EntityHandler(
				EntityManagerFactory.getEntityManager(), TestClass.class);

		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "ent",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class
				.getMethod("addEnt", TestClass.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("addEnt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(TestClass.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "ent",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("removeEnt",
				TestClass.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("removeEnt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(TestClass.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "ent",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class
				.getMethod("hasEnt", TestClass.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("hasEnt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(TestClass.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "ent",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());
	}

	@Test
	public void testFlt() throws Exception
	{
		Method m = MultiValueObjectTestClass.class.getMethod("getFlt");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m,
				new EffectivePredicate(m));

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Float.class));

		Assert.assertEquals("getFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("addFlt", Float.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("addFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("removeFlt", Float.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("removeFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("hasFlt", Float.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("hasFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());
	}

	@Test
	public void testInt() throws Exception
	{
		Method m = MultiValueObjectTestClass.class.getMethod("getInt");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m,
				new EffectivePredicate(m));

		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Integer.class));

		Assert.assertEquals("getInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("addInt", Integer.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("addInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("removeInt",
				Integer.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("removeInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("hasInt", Integer.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("hasInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());
	}

	@Test
	public void testLng() throws Exception
	{
		Method m = MultiValueObjectTestClass.class.getMethod("getLng");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m,
				new EffectivePredicate(m));
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Long.class));

		Assert.assertEquals("getLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("addLng", Long.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("addLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("removeLng", Long.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("removeLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("hasLng", Long.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("hasLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());
	}

	@Test
	public void testRdf() throws Exception
	{
		Method m = MultiValueObjectTestClass.class.getMethod("getRDF");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m,
				new EffectivePredicate(m));
		final ObjectHandler handler = new ResourceHandler();

		Assert.assertEquals("getRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "rDF",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("addRDF", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("addRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "rDF",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("removeRDF",
				RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("removeRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "rDF",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("hasRDF", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("hasRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "rDF",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());
	}

	@Test
	public void testStr() throws Exception
	{
		Method m = MultiValueObjectTestClass.class.getMethod("getStr");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m,
				new EffectivePredicate(m));
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(String.class));

		Assert.assertEquals("getStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "str",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("addStr", String.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("addStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "str",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class
				.getMethod("removeStr", String.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("removeStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "str",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("hasStr", String.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("hasStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "str",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());
	}

	@Test
	public void testURI() throws Exception
	{
		Method m = MultiValueObjectTestClass.class.getMethod("getU");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m,
				new EffectivePredicate(m));
		final ObjectHandler resHandler = new ResourceHandler();
		final ObjectHandler uriHandler = new UriHandler();

		Assert.assertEquals("getU", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("addU", String.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("addU", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("addU", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("addU", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("removeU", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("removeU", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("removeU", String.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("removeU", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("hasU", String.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("hasU", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("hasU", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("hasU", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("getU2");
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("getU2", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());
	}

	@Test
	public void testURIOrdering() throws Exception
	{
		Method m = MultiValueObjectTestClass.class.getMethod("getU3");
		PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse(m,
				new EffectivePredicate(m));
		final ObjectHandler resHandler = new ResourceHandler();
		final ObjectHandler uriHandler = new UriHandler();

		Assert.assertEquals("getU3", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u3",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("addU3", String.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("addU3", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u3",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("addU3", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("addU3", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u3",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class
				.getMethod("removeU3", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("removeU3", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u3",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("removeU3", String.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("removeU3", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u3",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("hasU3", String.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("hasU3", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u3",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("hasU3", RDFNode.class);
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("hasU3", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u3",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());

		m = MultiValueObjectTestClass.class.getMethod("getU4");
		pi = (PredicateInfoImpl) parser.parse(m, new EffectivePredicate(m));
		Assert.assertEquals("getU4", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS + "u3",
				pi.getUriString());
		Assert.assertEquals(MethodParserMultiValueEntityTest.NS,
				pi.getNamespace());
	}
}
