package org.xenei.jena.entities.impl.manager;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.jena.entities.impl.SubjectInfoImpl;

abstract public class BaseAbstractManagerTest
{
	protected SubjectInfoImpl subjectInfo;
	protected final Class<?> classUnderTest;
	protected static String NS = "http://localhost/test#";
	protected EntityManager manager;
	protected Model model;

	protected BaseAbstractManagerTest( final Class<?> classUnderTest )
	{
		this.classUnderTest = classUnderTest;
	}

	@Before
	public void setup() throws MissingAnnotation
	{
		model = ModelFactory.createDefaultModel();
		 manager = EntityManagerFactory.getEntityManager(model);
		subjectInfo = (SubjectInfoImpl) manager.getSubjectInfo(classUnderTest);
	}

	@After
	public void teardown()
	{
		model.close();
	}

	@Test
	public void testGetResource() throws Exception
	{
		final Resource r = model.createResource();
		final Object o = manager.read(r, classUnderTest);
		Resource r2 = r;
		if (o instanceof ResourceWrapper)
		{
			final ResourceWrapper rw = (ResourceWrapper) o;
			r2 = rw.getResource();

		}
		else
		{
			r2 = (Resource) o;
		}
		Assert.assertEquals(r, r2);
	}

}
