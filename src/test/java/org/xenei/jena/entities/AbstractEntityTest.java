package org.xenei.jena.entities;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.testing.abst.AbstractEntity;

public class AbstractEntityTest {
    private Model m;
    private final EntityManager manager = EntityManagerFactory.create();
    private AbstractEntity theInstance;
    private Resource r;

    @Before
    public void setup() throws MissingAnnotation {
        PropertyConfigurator.configure( "./src/test/resources/log4j.properties" );
        m = ModelFactory.createDefaultModel();
        r = m.createResource( "http://localhost/DirectInheritanceTest" );
        theInstance = manager.read( r, AbstractEntity.class );
    }

    @Test
    public void testConcreteMethod() {
        Assert.assertEquals( "Y", theInstance.getY() );
    }

    @Test
    public void testSetRetrieve() {
        String x = theInstance.getX();
        Assert.assertNull( x );
        theInstance.setX( "foo" );
        x = theInstance.getX();
        Assert.assertEquals( "foo", theInstance.getX() );
    }

}
