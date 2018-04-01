package org.xenei.pa4rdf.bean.handlers;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LiteralHandlerTest extends AbstractObjectHandlerTest
{
	private RDFNode node;
	private Integer instance;

	@Before
	public void setup()
	{
		underTest = new LiteralHandler(XSDDatatype.XSDint);
		node = ResourceFactory.createPlainLiteral("5");
		instance = 5;
	}
	
	@Test
	public void testBigIntegerRoundTrip() {
		RDFDatatype dt = TypeMapper.getInstance().getTypeByClass( BigInteger.class );
		LiteralHandler underTest = new LiteralHandler( dt );
		RDFNode value = underTest.createRDFNode( BigInteger.TEN );
		assertEquals( "Did not return proper BigInteger", BigInteger.TEN, underTest.parseObject(value));
	}
	
	@Test
	public void testBigDecimalRoundTrip() {
		RDFDatatype dt = TypeMapper.getInstance().getTypeByClass( BigDecimal.class );
		LiteralHandler underTest = new LiteralHandler( dt );
		RDFNode value = underTest.createRDFNode( BigDecimal.TEN );
		assertEquals( "Did not return proper BigDecimal", BigDecimal.TEN, underTest.parseObject(value));
	}

	@Override
	@Test
	public void testCreateRDFNode()
	{
		final RDFNode n = underTest.createRDFNode(Integer.valueOf(5));
		Assert.assertNotNull(n);
		final Literal l = ResourceFactory.createTypedLiteral("5",
				XSDDatatype.XSDint);
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
		Assert.assertNotNull("Should not be null", o);
		Assert.assertTrue("Should be an instance of Integer", o instanceof Integer);
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
