package org.xenei.jena.entities.impl.handlers;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

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

	@Test
	public void testCreateRDFNode()
	{
		RDFNode n = handler.createRDFNode(Integer.valueOf(5));
		Assert.assertNotNull(n);
		Literal l = ResourceFactory.createTypedLiteral("5",
				XSDDatatype.XSDinteger);
		Assert.assertEquals(l, n);
	}

	@Test
	public void testParseObject()
	{
		Object o = handler.parseObject(node);
		Assert.assertNotNull(o);
		Assert.assertTrue(o instanceof Integer);
		Integer a2 = (Integer) o;
		Assert.assertEquals(instance, a2);

	}

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
}
