package org.xenei.jena.entities.impl.handlers;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LiteralHandlerTest implements HandlerTestInterface
{
	LiteralHandler handler;
	RDFNode node;
	Integer instance;

	@BeforeEach
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
		Assertions.assertNotNull(n);
		final Literal l = ResourceFactory.createTypedLiteral("5",
				XSDDatatype.XSDinteger);
		Assertions.assertEquals(l, n);
	}

	@Override
	@Test
	public void testIsEmpty()
	{
		Assertions.assertTrue(handler.isEmpty(null));
		Assertions.assertFalse(handler.isEmpty(instance));
		handler = new LiteralHandler(XSDDatatype.XSDstring);
		Assertions.assertTrue(handler.isEmpty(null));
		Assertions.assertTrue(handler.isEmpty(""));
		Assertions.assertTrue(handler.isEmpty(" "));
		Assertions.assertFalse(handler.isEmpty(instance));
		Assertions.assertFalse(handler.isEmpty("foo"));

	}

	@Override
	@Test
	public void testParseObject()
	{
		final Object o = handler.parseObject(node);
		Assertions.assertNotNull(o);
		Assertions.assertTrue(o instanceof Integer);
		final Integer a2 = (Integer) o;
		Assertions.assertEquals(instance, a2);

	}
}
