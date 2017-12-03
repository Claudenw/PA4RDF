package org.xenei.jena.entities.nested;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

/**
 * This is a test for the case where a concrete class annotates an implemented
 * method.
 * The annotation should override the method.
 */
public class ImplementedAnnotationTest
{

	@Subject( namespace = "http://example.com/testing#" )
	public interface AnnotatedInterface
	{
		@Predicate
		public String getValue();

	}

	public static class AnnotationImplementation implements AnnotatedInterface
	{

		public String getName()
		{
			return "name";
		}

		@Override
		@Predicate
		public String getValue()
		{
			throw new IllegalArgumentException("NOT A VALID METHOD");
		}

	}

	public static class NoAnnotationImplementation implements
			AnnotatedInterface
	{

		public String getName()
		{
			return "name";
		}

		@Override
		public String getValue()
		{
			throw new IllegalArgumentException("NOT A VALID METHOD");
		}

	}

	private final Model model = ModelFactory.createDefaultModel();

	private Resource resource;

	private final EntityManager em = EntityManagerFactory.create();

	@Before
	public void setup()
	{
		model.removeAll();
		resource = model.createResource("http://example.com/testResource");
		final Property v = model.createProperty("http://example.com/testing#",
				"value");
		final Property n = model.createProperty("http://example.com/testing#",
				"name");
		resource.addProperty(v, "modelValue");
		resource.addProperty(n, "modelName");
	}

	@Test
	public void testReadNoAnnotation()
	{
		try
		{
			em.read(resource, NoAnnotationImplementation.class);
		}
		catch (final MissingAnnotation e)
		{
			// expected
		}
	}

	@Test
	public void testReadWithAnnotation() throws Exception
	{
		try
		{
			final AnnotationImplementation ai = em.read(resource,
					AnnotationImplementation.class);
			final String name = ai.getName();
			Assert.assertEquals("name", name);
			final String value = ai.getValue();
			Assert.assertEquals("modelValue", value);
		}
		catch (final RuntimeException e)
		{
			Assert.assertEquals("Not IMPLEMENTED", e.getMessage());
		}
	}

	@Test
	public void testUseNoReadAnnotation()
	{
		try
		{
			final AnnotationImplementation nai = new AnnotationImplementation();
			final String name = nai.getName();
			Assert.assertEquals("name", name);
			nai.getValue();
			Assert.fail("Should have thrown an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			Assert.assertEquals("NOT A VALID METHOD", e.getMessage());
		}
	}

	@Test
	public void testUseNoReadNoAnnotation()
	{
		try
		{
			final NoAnnotationImplementation nai = new NoAnnotationImplementation();
			final String name = nai.getName();
			Assert.assertEquals("name", name);
			nai.getValue();
			Assert.fail("Should have thrown an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			Assert.assertEquals("NOT A VALID METHOD", e.getMessage());
		}
	}

}
