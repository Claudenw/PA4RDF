package org.xenei.jena.entities;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import org.apache.commons.proxy.exception.InvokerException;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.bad_entities.B;

public class AbstractEntityTest
{
	private Model m;
	private EntityManager manager = EntityManagerFactory.getEntityManager();
	private AbstractEntity theInstance;
	private Resource r;
	
	@Before
	public void setup() throws MissingAnnotation
	{
		PropertyConfigurator.configure("./src/test/resources/log4j.properties");
		m = ModelFactory.createDefaultModel();
		r = m.createResource( "http://localhost/DirectInheritanceTest");
		theInstance = manager.read(r, AbstractEntity.class);
	}
	
	
	@Test
	public void testSetRetrieve()
	{
		String x = theInstance.getX();
		Assert.assertNull( x );
		theInstance.setX( "foo" );
		x = theInstance.getX();
		Assert.assertEquals( "foo", theInstance.getX());
	}
	
	@Test
	public void testConcreteMethod()
	{
		Assert.assertEquals( "Y", theInstance.getY());
	}
	
	

}
