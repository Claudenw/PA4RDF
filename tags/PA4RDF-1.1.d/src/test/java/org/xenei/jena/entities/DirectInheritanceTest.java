package org.xenei.jena.entities;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import org.apache.commons.proxy.exception.InvokerException;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.bad.B;

public class DirectInheritanceTest
{
	private Model m;
	private final EntityManager manager = EntityManagerFactory
			.getEntityManager();
	private A theInstance;
	private Resource r;

	@Before
	public void setup() throws MissingAnnotation
	{
		PropertyConfigurator.configure("./src/test/resources/log4j.properties");
		m = ModelFactory.createDefaultModel();
		r = m.createResource("http://localhost/DirectInheritanceTest");
		theInstance = manager.read(r, A.class);
	}

	@Test
	public void testEmptyIsNull()
	{
		theInstance.setZ("foo");
		Assert.assertNotNull(theInstance.getZ());
		Assert.assertEquals("foo", theInstance.getZ());

		theInstance.setZ("");
		Assert.assertNull(theInstance.getZ());

	}

	@Test
	public void testNullMethods()
	{
		try
		{
			manager.read(r, B.class);
			Assert.fail("Should have thrown InvokerException");
		}
		catch (final InvokerException e)
		{
			// expected
		}

	}

	@Test
	public void testResourceAncestorMethods()
	{

		Assert.assertEquals(r.getModel(), theInstance.getModel());

	}

	@Test
	public void testResourceMethods()
	{
		Assert.assertEquals(r.getURI(), theInstance.getURI());
	}

}
