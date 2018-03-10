package org.xenei.pa4rdf.bean;

import static org.junit.Assert.*;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Test;
import org.xenei.pa4rdf.bean.EntityFactory;
import org.xenei.pa4rdf.bean.ResourceWrapper;
import org.xenei.pa4rdf.bean.exceptions.MissingAnnotation;
import org.xenei.pa4rdf.bean.test.fullMagic.FullMagic;
import org.xenei.pa4rdf.bean.test.partialMagic.PartialMagic;

public class EntityFactoryTest
{
 
	
	/**
	 * Test case where all interfaces are annotated with magicbean annotation.
	 * @throws Exception on error
	 */
	@Test
	public void testFullyMagic() throws Exception
	{
		FullMagic magic = EntityFactory.read( ResourceFactory.createResource( "http://example.com/Magic"), FullMagic.class);

		assertFalse( magic.hasA());
		assertFalse( magic.hasB());
		
		magic.setA(1);
		magic.setB("hello");
		
		assertTrue( magic.hasA());
		assertTrue( magic.hasB());
		
		assertEquals( 1, magic.getA());
		assertEquals( "hello", magic.getB());
		
		Resource r = ((ResourceWrapper) magic).getResource();
		assertTrue( r.hasLiteral( ResourceFactory.createProperty("http://example.com/test/a"), 1));
		assertTrue( r.hasLiteral( ResourceFactory.createProperty("http://example.com/test/b"), "hello"));
	}
	
	/**
	 * Test case where only base interface is annotated with magicbean annotation.
	 * @throws Exception on error
	 */
	@Test
	public void testPartialMagic() throws Exception
	{
		PartialMagic magic = EntityFactory.read( ResourceFactory.createResource( "http://example.com/Magic2"), PartialMagic.class);

		assertFalse( magic.hasA());
		assertFalse( magic.hasB());
		
		magic.setA(1);
		magic.setB("hello");
		
		assertTrue( magic.hasA());
		assertTrue( magic.hasB());
		
		assertEquals( 1, magic.getA());
		assertEquals( "hello", magic.getB());
		
		Resource r = ((ResourceWrapper) magic).getResource();
		assertTrue( r.hasLiteral( ResourceFactory.createProperty("a"), 1));
		assertTrue( r.hasLiteral( ResourceFactory.createProperty("b"), "hello"));
	}

}


