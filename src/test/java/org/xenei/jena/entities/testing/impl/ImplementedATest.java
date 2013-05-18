package org.xenei.jena.entities.testing.impl;

import static org.junit.Assert.*;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;

public class ImplementedATest
{
	private Model model;
	private Resource resource;
	private SimpleInterface wrap;
	private EntityManager em;
	@Before
	public void setup()
	{
		model = ModelFactory.createDefaultModel();
		resource = model.createResource("http://example.com/testResource");
		em = EntityManagerFactory.getEntityManager();
		wrap = em.read( resource, SimpleInterfaceImpl.class );
	}
	
	@After
	public void teardown() {
		model.close();
	}
	
	@Test
	public void testGetResource()
	{
		assertEquals( resource, wrap.getResource()  );
	}
	
	@Test
	public void testSetGetX()
	{
		wrap.setX( "ex");
		assertEquals( "ex", wrap.getX() );
	}
	
	@Test
	public void testSetGetZ()
	{
		assertNull( wrap.getZ() );
		wrap.setZ( "zee");
		assertEquals( "zee", wrap.getZ() );
	}
}
