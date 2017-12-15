package org.xenei.jena.entities.impl.handlers;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LiteralHandlerTest implements HandlerTestInterface
{
	LiteralHandler handler;
	RDFNode node;
	Integer instance;

	@Before
	public void setup()
	{
		handler = new LiteralHandler(XSDDatatype.XSDinteger);
		node = ResourceFactory.createPlainLiteral("5");
		instance = 5;
	}

	@Override
	@Test
	public void testCreateRDFNode()
	{
		final RDFNode n = handler.createRDFNode(Integer.valueOf(5));
		Assert.assertNotNull(n);
		final Literal l = ResourceFactory.createTypedLiteral("5",
				XSDDatatype.XSDinteger);
		Assert.assertEquals(l, n);
	}

	@Override
	@Test
	public void testIsEmpty()
	{
		Assert.assertTrue(handler.isEmpty(null));
		Assert.assertFalse(handler.isEmpty(instance));
		handler = new LiteralHandler(XSDDatatype.XSDstring);
		Assert.assertTrue(handler.isEmpty(null));
		Assert.assertTrue(handler.isEmpty(""));
		Assert.assertTrue(handler.isEmpty(" "));
		Assert.assertFalse(handler.isEmpty(instance));
		Assert.assertFalse(handler.isEmpty("foo"));

	}

	@Override
	@Test
	public void testParseObject()
	{
		final Object o = handler.parseObject(node);
		Assert.assertNotNull(o);
		Assert.assertTrue(o instanceof Integer);
		final Integer a2 = (Integer) o;
		Assert.assertEquals(instance, a2);

	}

	@Override
	@Test
	public void testCreateRDFNode_Null() {
		final RDFNode n = handler.createRDFNode( null );
		Assert.assertNull(n);
	}
}
