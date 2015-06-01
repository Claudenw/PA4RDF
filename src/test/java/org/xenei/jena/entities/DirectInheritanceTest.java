package org.xenei.jena.entities;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.testing.bad.UnannotatedInterface;
import org.xenei.jena.entities.testing.iface.TwoValueSimpleInterface;

public class DirectInheritanceTest
{
	private Model m;
	private final EntityManager manager = EntityManagerFactory
			.getEntityManager();
	private TwoValueSimpleInterface theInstance;
	private Resource r;

	@Before
	public void setup() throws MissingAnnotation
	{
		PropertyConfigurator.configure("./src/test/resources/log4j.properties");
		m = ModelFactory.createDefaultModel();
		r = m.createResource("http://localhost/DirectInheritanceTest");
		theInstance = manager.read(r, TwoValueSimpleInterface.class);
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
			manager.read(r, UnannotatedInterface.class);
			Assert.fail("Should have thrown MissingAnnotation");
		}
		catch (final MissingAnnotation e)
		{
			// expected
		}

	}

	/*
	 * @Test
	 * public void testResourceAncestorMethods()
	 * {
	 * 
	 * Assert.assertEquals(r.getModel(), theInstance.getModel());
	 * 
	 * }
	 * 
	 * @Test
	 * public void testResourceMethods()
	 * {
	 * Assert.assertEquals(r.getURI(), theInstance.getURI());
	 * }
	 */
}
