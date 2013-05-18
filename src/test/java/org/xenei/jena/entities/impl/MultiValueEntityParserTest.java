package org.xenei.jena.entities.impl;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.impl.handlers.EntityHandler;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.testing.abst.MultiValueObjectTestClass;
import org.xenei.jena.entities.testing.abst.TestClass;

public class MultiValueEntityParserTest
{

	private Model m;
	private SubjectInfo subjectInfo;
	private Class<?> clazz;
	private static String namespaceStr = "http://localhost/test#";
	private final EntityManager manager = new EntityManagerImpl();

	@Before
	public void setup() throws MissingAnnotation
	{
		m = ModelFactory.createDefaultModel();
		clazz = MultiValueObjectTestClass.class;
		final Resource r = m
				.createResource("http://localhost/MultiValueEntityTests");
		manager.read(r, clazz);
		subjectInfo = manager.getSubjectInfo(clazz);
	}

	@After
	public void teardown()
	{
		m.close();
	}

	@Test
	public void testBoolean() throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException
	{

		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("addBool", Boolean.class);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Boolean.class));
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals("addBool", pi.getMethodName());
		Assert.assertEquals(Boolean.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "bool",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeBool",
				Boolean.class);

		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals("removeBool", pi.getMethodName());
		Assert.assertEquals(Boolean.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "bool",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		final Class<?> c = (Class<?>) Boolean.class.getField("TYPE").get(null);

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addBool", c);

		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals("addBool", pi.getMethodName());
		Assert.assertEquals(Boolean.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "bool",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeBool", c);

		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals("removeBool", pi.getMethodName());
		Assert.assertEquals(Boolean.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "bool",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

	}

	@Test
	public void testChar() throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getChar", ExtendedIterator.class);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Character.class));
		Assert.assertEquals("getChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "char",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addChar",
				Character.class);
		Assert.assertEquals("addChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "char",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeChar",
				Character.class);
		Assert.assertEquals("removeChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "char",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasChar",
				Character.class);
		Assert.assertEquals("hasChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "char",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		final Class<?> c = (Class<?>) Character.class.getField("TYPE")
				.get(null);

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addChar", c);
		Assert.assertEquals("addChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "char",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeChar", c);
		Assert.assertEquals("removeChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "char",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasChar", c);
		Assert.assertEquals("hasChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "char",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());
	}

	@Test
	public void testDbl() throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getDbl", ExtendedIterator.class);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Double.class));
		Assert.assertEquals("getDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "dbl",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addDbl",
				Double.class);
		Assert.assertEquals("addDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "dbl",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeDbl",
				Double.class);
		Assert.assertEquals("removeDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "dbl",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasDbl",
				Double.class);
		Assert.assertEquals("hasDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "dbl",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		final Class<?> c = (Class<?>) Double.class.getField("TYPE").get(null);

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addDbl", c);
		Assert.assertEquals("addDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "dbl",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeDbl", c);
		Assert.assertEquals("removeDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "dbl",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasDbl", c);
		Assert.assertEquals("hasDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "dbl",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

	}

	@Test
	public void testEntity()
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getEnt", ExtendedIterator.class);
		Assert.assertEquals("getEnt", pi.getMethodName());
		final ObjectHandler handler = new EntityHandler(manager,
				TestClass.class);

		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "ent",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addEnt",
				TestClass.class);
		Assert.assertEquals("addEnt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(TestClass.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "ent",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeEnt",
				TestClass.class);
		Assert.assertEquals("removeEnt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(TestClass.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "ent",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasEnt",
				TestClass.class);
		Assert.assertEquals("hasEnt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(TestClass.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "ent",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());
	}

	@Test
	public void testFlt() throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException
	{

		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getFlt", ExtendedIterator.class);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Float.class));

		Assert.assertEquals("getFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "flt",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addFlt",
				Float.class);
		Assert.assertEquals("addFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "flt",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeFlt",
				Float.class);
		Assert.assertEquals("removeFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "flt",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasFlt",
				Float.class);
		Assert.assertEquals("hasFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "flt",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		final Class<?> c = (Class<?>) Float.class.getField("TYPE").get(null);

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addFlt", c);
		Assert.assertEquals("addFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "flt",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeFlt", c);
		Assert.assertEquals("removeFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "flt",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasFlt", c);
		Assert.assertEquals("hasFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "flt",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());
	}

	@Test
	public void testInt() throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException
	{

		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getInt", ExtendedIterator.class);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Integer.class));

		Assert.assertEquals("getInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "int",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addInt",
				Integer.class);
		Assert.assertEquals("addInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "int",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeInt",
				Integer.class);
		Assert.assertEquals("removeInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "int",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasInt",
				Integer.class);
		Assert.assertEquals("hasInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "int",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		final Class<?> c = (Class<?>) Integer.class.getField("TYPE").get(null);

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addInt", c);
		Assert.assertEquals("addInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "int",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeInt", c);
		Assert.assertEquals("removeInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "int",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasInt", c);
		Assert.assertEquals("hasInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "int",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

	}

	@Test
	public void testLng() throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getLng", ExtendedIterator.class);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Long.class));

		Assert.assertEquals("getLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "lng",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addLng",
				Long.class);
		Assert.assertEquals("addLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "lng",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeLng",
				Long.class);
		Assert.assertEquals("removeLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "lng",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasLng",
				Long.class);
		Assert.assertEquals("hasLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "lng",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		final Class<?> c = (Class<?>) Long.class.getField("TYPE").get(null);

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addLng", c);
		Assert.assertEquals("addLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "lng",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeLng", c);
		Assert.assertEquals("removeLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "lng",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasLng", c);
		Assert.assertEquals("hasLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "lng",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

	}

	@Test
	public void testRdf()
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getRDF", ExtendedIterator.class);
		final ObjectHandler handler = new ResourceHandler();

		Assert.assertEquals("getRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "rDF",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addRDF",
				RDFNode.class);
		Assert.assertEquals("addRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "rDF",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeRDF",
				RDFNode.class);
		Assert.assertEquals("removeRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "rDF",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasRDF",
				RDFNode.class);
		Assert.assertEquals("hasRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "rDF",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());
	}

	@Test
	public void testStr()
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getStr", ExtendedIterator.class);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(String.class));

		Assert.assertEquals("getStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "str",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addStr",
				String.class);
		Assert.assertEquals("addStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "str",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeStr",
				String.class);
		Assert.assertEquals("removeStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "str",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasStr",
				String.class);
		Assert.assertEquals("hasStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "str",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());
	}

	@Test
	public void testURI()
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getU", ExtendedIterator.class);
		final ObjectHandler resHandler = new ResourceHandler();
		final ObjectHandler uriHandler = new UriHandler();

		Assert.assertEquals("getU", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addU",
				String.class);
		Assert.assertEquals("addU", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addU",
				RDFNode.class);
		Assert.assertEquals("addU", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeU",
				RDFNode.class);
		Assert.assertEquals("removeU", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeU",
				String.class);
		Assert.assertEquals("removeU", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasU",
				String.class);
		Assert.assertEquals("hasU", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasU",
				RDFNode.class);
		Assert.assertEquals("hasU", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("getU2",
				ExtendedIterator.class);
		Assert.assertEquals("getU2", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());
	}

	@Test
	public void testURIOrdering()
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getU3", ExtendedIterator.class);
		final ObjectHandler resHandler = new ResourceHandler();
		final ObjectHandler uriHandler = new UriHandler();

		Assert.assertEquals("getU3", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u3",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addU3",
				String.class);
		Assert.assertEquals("addU3", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u3",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("addU3",
				RDFNode.class);
		Assert.assertEquals("addU3", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u3",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeU3",
				RDFNode.class);
		Assert.assertEquals("removeU3", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u3",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeU3",
				String.class);
		Assert.assertEquals("removeU3", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u3",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasU3",
				String.class);
		Assert.assertEquals("hasU3", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u3",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("hasU3",
				RDFNode.class);
		Assert.assertEquals("hasU3", pi.getMethodName());
		Assert.assertEquals(resHandler, pi.getObjectHandler());
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u3",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("getU4",
				ExtendedIterator.class);
		Assert.assertEquals("getU4", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr + "u3",
				pi.getUriString());
		Assert.assertEquals(MultiValueEntityParserTest.namespaceStr,
				pi.getNamespace());
	}

}
