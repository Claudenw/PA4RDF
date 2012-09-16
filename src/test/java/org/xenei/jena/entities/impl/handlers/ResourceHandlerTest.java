package org.xenei.jena.entities.impl.handlers;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.A;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;

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
	
	@Test
	public void testCreateRDFNode()
	{
		RDFNode n = handler.createRDFNode( node );
		Assert.assertNotNull( n );
		Assert.assertEquals( node, n );
	}

	@Test
	public void testParseObject()
	{
		Object o = handler.parseObject(node);
		Assert.assertNotNull( o );	
		Assert.assertEquals( node, o );
	}

	@Test
	public void testIsEmpty()
	{
		Assert.assertTrue( handler.isEmpty( null ));
		Assert.assertFalse( handler.isEmpty( node ));
		Assert.assertTrue( handler.isEmpty( "" ));
		Assert.assertTrue( handler.isEmpty( "foo" ));
		
	}
}
