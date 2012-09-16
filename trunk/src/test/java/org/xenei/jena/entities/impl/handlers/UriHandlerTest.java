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

public class UriHandlerTest implements HandlerTestInterface
{
	UriHandler handler;
	RDFNode node;
	Integer instance;
	
	@Before
	public void setup()
	{
		handler = new UriHandler();
		node = ResourceFactory.createResource( "http://example.com");
		instance = 5;
	}
	
	@Test
	public void testCreateRDFNode()
	{
		RDFNode n = handler.createRDFNode( "http://example.com" );
		Assert.assertNotNull( n );
		Assert.assertEquals( node, n );
	}

	@Test
	public void testParseObject()
	{
		Object o = handler.parseObject( node );
		Assert.assertNotNull( o );	
		Assert.assertEquals( "http://example.com", o );
	}

	@Test
	public void testIsEmpty()
	{
		Assert.assertTrue( handler.isEmpty( null ));
		Assert.assertFalse( handler.isEmpty( node ));
		Assert.assertTrue( handler.isEmpty( "" ));
		Assert.assertFalse( handler.isEmpty( "foo" ));
		
	}
}
