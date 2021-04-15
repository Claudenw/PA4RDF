package org.xenei.jena.entities.impl.handlers;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResourceHandlerTest implements HandlerTestInterface
{
	ResourceHandler handler;
	RDFNode node;
	Integer instance;

	@Before
	public void setup()
	{
		handler = new ResourceHandler();
		node = ResourceFactory.createResource();
		instance = 5;
	}

	@Override
	@Test
	public void testCreateRDFNode()
	{
		final RDFNode n = handler.createRDFNode(node);
		Assert.assertNotNull(n);
		Assert.assertEquals(node, n);
	}

	@Override
	@Test
	public void testIsEmpty()
	{
		Assert.assertTrue(handler.isEmpty(null));
		Assert.assertFalse(handler.isEmpty(node));
		Assert.assertTrue(handler.isEmpty("foo"));
		Assert.assertTrue(handler.isEmpty(""));
		Assert.assertTrue(handler.isEmpty(" "));
		Assert.assertTrue(handler.isEmpty(ResourceFactory.createResource("")));
		Assert.assertTrue(handler.isEmpty(ResourceFactory.createResource(" ")));
		Assert.assertFalse(handler.isEmpty(ResourceFactory.createResource()));
		Assert.assertTrue(handler.isEmpty(ResourceFactory.createTypedLiteral(
				"", XSDDatatype.XSDstring)));
		Assert.assertTrue(handler.isEmpty(ResourceFactory.createTypedLiteral(
				" ", XSDDatatype.XSDstring)));
	}

	@Override
	@Test
	public void testParseObject()
	{
		final Object o = handler.parseObject(node);
		Assert.assertNotNull(o);
		Assert.assertEquals(node, o);
	}
}
