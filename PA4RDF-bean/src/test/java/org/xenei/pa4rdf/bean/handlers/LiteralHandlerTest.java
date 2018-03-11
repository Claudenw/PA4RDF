package org.xenei.pa4rdf.bean.handlers;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LiteralHandlerTest extends AbstractObjectHandlerTest
{
	RDFNode node;
	Integer instance;

	@Before
	public void setup()
	{
		underTest = new LiteralHandler(XSDDatatype.XSDinteger);
		node = ResourceFactory.createPlainLiteral("5");
		instance = 5;
	}

	@Override
	@Test
	public void testCreateRDFNode()
	{
		final RDFNode n = underTest.createRDFNode(Integer.valueOf(5));
		Assert.assertNotNull(n);
		final Literal l = ResourceFactory.createTypedLiteral("5",
				XSDDatatype.XSDinteger);
		Assert.assertEquals(l, n);
	}

	@Override
	@Test
	public void testIsEmpty()
	{
		Assert.assertTrue(underTest.isEmpty(null));
		Assert.assertFalse(underTest.isEmpty(instance));
		underTest = new LiteralHandler(XSDDatatype.XSDstring);
		Assert.assertTrue(underTest.isEmpty(null));
		Assert.assertTrue(underTest.isEmpty(""));
		Assert.assertTrue(underTest.isEmpty(" "));
		Assert.assertFalse(underTest.isEmpty(instance));
		Assert.assertFalse(underTest.isEmpty("foo"));

	}

	@Override
	@Test
	public void testParseObject()
	{
		final Object o = underTest.parseObject(node);
		Assert.assertNotNull(o);
		Assert.assertTrue(o instanceof Integer);
		final Integer a2 = (Integer) o;
		Assert.assertEquals(instance, a2);

	}

	@Override
	@Test
	public void testCreateRDFNode_Null()
	{
		final RDFNode n = underTest.createRDFNode(null);
		Assert.assertNull(n);
	}
}
