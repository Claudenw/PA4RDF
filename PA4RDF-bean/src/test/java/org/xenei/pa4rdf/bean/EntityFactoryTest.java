package org.xenei.pa4rdf.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Test;
import org.xenei.pa4rdf.bean.test.fullMagic.FullMagic;
import org.xenei.pa4rdf.bean.test.impl.SimpleInterfaceImpl;
import org.xenei.pa4rdf.bean.test.partialMagic.PartialMagic;

public class EntityFactoryTest
{

	/**
	 * Test case where all interfaces are annotated with magicbean annotation.
	 * 
	 * @throws Exception
	 *             on error
	 */
	@Test
	public void testFullyMagic() throws Exception
	{
		final FullMagic magic = EntityFactory.read(
				ResourceFactory.createResource("http://example.com/Magic"),
				FullMagic.class);

		assertFalse(magic.hasA());
		assertFalse(magic.hasB());

		magic.setA(1);
		magic.setB("hello");

		assertTrue(magic.hasA());
		assertTrue(magic.hasB());

		assertEquals(1, magic.getA());
		assertEquals("hello", magic.getB());

		final Resource r = ((ResourceWrapper) magic).getResource();
		assertTrue(r.hasLiteral(
				ResourceFactory.createProperty("http://example.com/test/a"),
				1));
		assertTrue(r.hasLiteral(
				ResourceFactory.createProperty("http://example.com/test/b"),
				"hello"));
	}

	/**
	 * Test case where only base interface is annotated with magicbean
	 * annotation.
	 * 
	 * @throws Exception
	 *             on error
	 */
	@Test
	public void testPartialMagic() throws Exception
	{
		final PartialMagic magic = EntityFactory.read(
				ResourceFactory.createResource("http://example.com/Magic2"),
				PartialMagic.class);

		assertFalse(magic.hasA());
		assertFalse(magic.hasB());

		magic.setA(1);
		magic.setB("hello");

		assertTrue(magic.hasA());
		assertTrue(magic.hasB());

		assertEquals(1, magic.getA());
		assertEquals("hello", magic.getB());

		final Resource r = ((ResourceWrapper) magic).getResource();
		assertTrue(r.hasLiteral(ResourceFactory.createProperty("a"), 1));
		assertTrue(r.hasLiteral(ResourceFactory.createProperty("b"), "hello"));
	}

	@Test
	public void testPostExecRuns() throws Exception
	{

		final SimpleInterfaceImpl o = EntityFactory.read(
				ResourceFactory.createResource("http://example.com/execTest"),
				SimpleInterfaceImpl.class);

		o.setX("foo");
		o.getX();
		assertEquals("foo", o.lastGetX);

	}
}
