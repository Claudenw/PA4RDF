package org.xenei.pa4rdf.bean.handlers;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResourceHandlerTest extends AbstractObjectHandlerTest
{

	RDFNode node;
	Integer instance;

	@Before
	public void setup()
	{
		underTest = ResourceHandler.INSTANCE;
		node = ResourceFactory.createResource();
		instance = 5;
	}

	@Override
	@Test
	public void testCreateRDFNode()
	{
		final RDFNode n = underTest.createRDFNode(node);
		Assert.assertNotNull(n);
		Assert.assertEquals(node, n);
	}

	@Override
	@Test
	public void testCreateRDFNode_Null()
	{
		final RDFNode n = underTest.createRDFNode(null);
		Assert.assertNull(n);
	}

	@Override
	@Test
	public void testIsEmpty()
	{
		Assert.assertTrue(underTest.isEmpty(null));
		Assert.assertFalse(underTest.isEmpty(node));
		Assert.assertTrue(underTest.isEmpty("foo"));
		Assert.assertTrue(underTest.isEmpty(""));
		Assert.assertTrue(underTest.isEmpty(" "));
		Assert.assertTrue(
				underTest.isEmpty(ResourceFactory.createResource("")));
		Assert.assertTrue(
				underTest.isEmpty(ResourceFactory.createResource(" ")));
		Assert.assertFalse(underTest.isEmpty(ResourceFactory.createResource()));
		Assert.assertTrue(underTest.isEmpty(
				ResourceFactory.createTypedLiteral("", XSDDatatype.XSDstring)));
		Assert.assertTrue(underTest.isEmpty(ResourceFactory
				.createTypedLiteral(" ", XSDDatatype.XSDstring)));
	}

	@Override
	@Test
	public void testParseObject()
	{
		final Object o = underTest.parseObject(node);
		Assert.assertNotNull(o);
		Assert.assertEquals(node, o);
	}
}
