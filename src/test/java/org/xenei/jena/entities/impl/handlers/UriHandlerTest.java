package org.xenei.jena.entities.impl.handlers;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UriHandlerTest implements HandlerTestInterface
{
	UriHandler handler;
	RDFNode node;
	Integer instance;

	@Before
	public void setup()
	{
		handler = new UriHandler();
		node = ResourceFactory.createResource("http://example.com");
		instance = 5;
	}

	@Override
	@Test
	public void testCreateRDFNode()
	{
		final RDFNode n = handler.createRDFNode("http://example.com");
		Assert.assertNotNull(n);
		Assert.assertEquals(node, n);
	}
	
	@Override
	public void testCreateRDFNode_Null() {
		final RDFNode n = handler.createRDFNode( null );
		Assert.assertNull(n);
	}

	@Override
	@Test
	public void testIsEmpty()
	{
		Assert.assertTrue(handler.isEmpty(null));
		Assert.assertFalse(handler.isEmpty(node));
		Assert.assertTrue(handler.isEmpty(""));
		Assert.assertTrue(handler.isEmpty(" "));
		Assert.assertFalse(handler.isEmpty("foo"));

	}

	@Override
	@Test
	public void testParseObject()
	{
		final Object o = handler.parseObject(node);
		Assert.assertNotNull(o);
		Assert.assertEquals("http://example.com", o);
	}
}
