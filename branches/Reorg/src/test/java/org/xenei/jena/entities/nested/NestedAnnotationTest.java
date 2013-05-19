package org.xenei.jena.entities.nested;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import org.junit.Test;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

/**
 * This is a test for the case where an abstract class implements a method that
 * is not
 * annotated in the interface and an annotated method causes the interface to be
 * parsed.
 * This throws exception because the parsing of the annotation believes that the
 * annotation should
 * be complete when it should be handled by the implementation parsing.
 */
public class NestedAnnotationTest
{

	@Subject( namespace = "http://example.com/testing#" )
	public interface AnnotatedInterface
	{

		public String getName();

		@Predicate
		public String getValue();

	}

	public static abstract class Implementation implements AnnotatedInterface
	{

		@Override
		public String getName()
		{
			return "name";
		}

	}

	@Test
	public void testLoad()
	{
		final EntityManager em = EntityManagerFactory.getEntityManager();
		final Model m = ModelFactory.createDefaultModel();
		em.read(m.createResource("http://example.com/test"),
				Implementation.class);

	}

}
