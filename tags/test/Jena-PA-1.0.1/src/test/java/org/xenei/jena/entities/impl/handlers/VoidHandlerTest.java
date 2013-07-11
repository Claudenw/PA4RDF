package org.xenei.jena.entities.impl.handlers;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VoidHandlerTest implements HandlerTestInterface
{
	VoidHandler handler;
	RDFNode node;
	Integer instance;

	@Before
	public void setup()
	{
		handler = new VoidHandler();
		node = ResourceFactory.createPlainLiteral("5");
		instance = 5;
	}

	@Test
	public void testCreateRDFNode()
	{
		RDFNode n = handler.createRDFNode(Integer.valueOf(5));
		Assert.assertNull(n);
	}

	@Test
	public void testParseObject()
	{
		Object o = handler.parseObject(node);
		Assert.assertNull(o);
	}

	@Test
	public void testIsEmpty()
	{
		Assert.assertTrue(handler.isEmpty(null));
		Assert.assertTrue(handler.isEmpty(instance));
		Assert.assertTrue(handler.isEmpty(""));
		Assert.assertTrue(handler.isEmpty(" "));
		Assert.assertTrue(handler.isEmpty("foo"));

	}
}
