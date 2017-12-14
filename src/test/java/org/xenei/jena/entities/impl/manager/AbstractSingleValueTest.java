package org.xenei.jena.entities.impl.manager;

import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.RDFNode;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.impl.ObjectHandler;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.handlers.EntityHandler;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.impl.handlers.VoidHandler;
import org.xenei.jena.entities.testing.iface.TestInterface;

public abstract class AbstractSingleValueTest extends BaseAbstractManagerTest
{

	protected AbstractSingleValueTest( final Class<?> classUnderTest )
	{
		super(classUnderTest);
	}

	@Test
	public void testBoolean() throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("isBool", Boolean.class);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Boolean.class));

		Assert.assertEquals("isBool", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Boolean.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "bool",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setBool",
				Boolean.class);
		Assert.assertEquals("setBool", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Boolean.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "bool",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeBool",
				null);
		Assert.assertEquals("removeBool", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( manager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "bool",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		final Class<?> c = (Class<?>) Boolean.class.getField("TYPE").get(null);

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("isBool", c);
		Assert.assertEquals("isBool", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Boolean.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "bool",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setBool", c);
		Assert.assertEquals("setBool", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Boolean.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "bool",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

	}

	@Test
	public void testChar() throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getChar", Character.class);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Character.class));

		Assert.assertEquals("getChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setChar",
				Character.class);
		Assert.assertEquals("setChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeChar",
				null);
		Assert.assertEquals("removeChar", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( manager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		final Class<?> c = (Class<?>) Character.class.getField("TYPE")
				.get(null);

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("getChar", c);
		Assert.assertEquals("getChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setChar", c);
		Assert.assertEquals("setChar", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Character.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "char",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

	}

	@Test
	public void testDbl() throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getDbl", Double.class);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Double.class));

		Assert.assertEquals("getDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setDbl",
				Double.class);
		Assert.assertEquals("setDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("removeDbl", null);
		Assert.assertEquals("removeDbl", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( manager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		final Class<?> c = (Class<?>) Double.class.getField("TYPE").get(null);

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("getDbl", c);
		Assert.assertEquals("getDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setDbl", c);
		Assert.assertEquals("setDbl", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Double.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "dbl",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

	}

	@Test
	public void testEntity()
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getEnt", TestInterface.class);
		final ObjectHandler handler = new EntityHandler(manager,
				TestInterface.class);

		Assert.assertEquals("getEnt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(TestInterface.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "ent",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setEnt",
				TestInterface.class);
		Assert.assertEquals("setEnt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(TestInterface.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "ent",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("removeEnt", null);
		Assert.assertEquals("removeEnt", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( manager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "ent",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());
	}

	@Test
	public void testFlt() throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getFlt", Float.class);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Float.class));

		Assert.assertEquals("getFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setFlt",
				Float.class);
		Assert.assertEquals("setFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("removeFlt", null);
		Assert.assertEquals("removeFlt", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( manager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		final Class<?> c = (Class<?>) Float.class.getField("TYPE").get(null);

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("getFlt", c);
		Assert.assertEquals("getFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setFlt", c);
		Assert.assertEquals("setFlt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Float.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "flt",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

	}

	@Test
	public void testInt() throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException
	{

		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getInt", Integer.class);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Integer.class));

		Assert.assertEquals("getInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setInt",
				Integer.class);
		Assert.assertEquals("setInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("removeInt", null);
		Assert.assertEquals("removeInt", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( manager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		final Class<?> c = (Class<?>) Integer.class.getField("TYPE").get(null);

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("getInt", c);
		Assert.assertEquals("getInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setInt", c);
		Assert.assertEquals("setInt", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Integer.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "int",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

	}

	@Test
	public void testLng() throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getLng", Long.class);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(Long.class));

		Assert.assertEquals("getLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setLng",
				Long.class);
		Assert.assertEquals("setLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("removeLng", null);
		Assert.assertEquals("removeLng", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( manager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		final Class<?> c = (Class<?>) Long.class.getField("TYPE").get(null);

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("getLng", c);
		Assert.assertEquals("getLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setLng", c);
		Assert.assertEquals("setLng", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(Long.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "lng",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());
	}

	@Test
	public void testRdf()
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getRDF", RDFNode.class);
		final ObjectHandler handler = new ResourceHandler();

		Assert.assertEquals("getRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "rDF",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setRDF",
				RDFNode.class);
		Assert.assertEquals("setRDF", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "rDF",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("removeRDF", null);
		Assert.assertEquals("removeRDF", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( manager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "rDF",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());
	}

	@Test
	public void testStr()
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getStr", String.class);
		final ObjectHandler handler = new LiteralHandler(TypeMapper
				.getInstance().getTypeByClass(String.class));

		Assert.assertEquals("getStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "str",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setStr",
				String.class);
		Assert.assertEquals("setStr", pi.getMethodName());
		Assert.assertEquals(handler, pi.getObjectHandler( manager ));
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "str",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("removeStr", null);
		Assert.assertEquals("removeStr", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( manager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "str",
				pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());
	}

	@Test
	public void testURI()
	{
		PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo
				.getPredicateInfo("getU", RDFNode.class);

		final ObjectHandler uriHandler = new UriHandler();
		final ObjectHandler rdfHandler = new ResourceHandler();

		Assert.assertEquals("getU", pi.getMethodName());
		Assert.assertEquals(rdfHandler, pi.getObjectHandler( manager ));
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "u", pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setU",
				String.class);
		Assert.assertEquals("setU", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler( manager ));
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "u", pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("setU",
				RDFNode.class);
		Assert.assertEquals("setU", pi.getMethodName());
		Assert.assertEquals(rdfHandler, pi.getObjectHandler( manager ));
		Assert.assertEquals(RDFNode.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "u", pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("removeU", null);
		Assert.assertEquals("removeU", pi.getMethodName());
		Assert.assertEquals(new VoidHandler(), pi.getObjectHandler( manager ));
		Assert.assertEquals(null, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "u", pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());

		pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo("getU2",
				String.class);
		Assert.assertEquals("getU2", pi.getMethodName());
		Assert.assertEquals(uriHandler, pi.getObjectHandler( manager ));
		Assert.assertEquals(String.class, pi.getValueClass());
		Assert.assertEquals(BaseAbstractManagerTest.NS + "u", pi.getUriString());
		Assert.assertEquals(BaseAbstractManagerTest.NS, pi.getNamespace());
	}

}
