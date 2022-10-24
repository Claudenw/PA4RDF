package org.xenei.jena.entities.impl.handlers;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VoidHandlerTest implements HandlerTestInterface
{
	VoidHandler handler;
	RDFNode node;
	Integer instance;

	@BeforeEach
	public void setup()
	{
		handler = new VoidHandler();
		node = ResourceFactory.createPlainLiteral("5");
		instance = 5;
	}

	@Override
	@Test
	public void testCreateRDFNode()
	{
		final RDFNode n = handler.createRDFNode(Integer.valueOf(5));
		Assertions.assertNull(n);
	}

	@Override
	@Test
	public void testIsEmpty()
	{
		Assertions.assertTrue(handler.isEmpty(null));
		Assertions.assertTrue(handler.isEmpty(instance));
		Assertions.assertTrue(handler.isEmpty(""));
		Assertions.assertTrue(handler.isEmpty(" "));
		Assertions.assertTrue(handler.isEmpty("foo"));

	}

	@Override
	@Test
	public void testParseObject()
	{
		final Object o = handler.parseObject(node);
		Assertions.assertNull(o);
	}
}
