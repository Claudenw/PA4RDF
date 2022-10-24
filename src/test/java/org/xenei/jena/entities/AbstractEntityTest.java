package org.xenei.jena.entities;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.xenei.jena.entities.testing.abst.AbstractEntity;

public class AbstractEntityTest {
    private Model m;
    private final EntityManager manager = EntityManagerFactory.getEntityManager();
    private AbstractEntity theInstance;
    private Resource r;

    @BeforeEach
    public void setup() throws MissingAnnotation {
        m = ModelFactory.createDefaultModel();
        r = m.createResource( "http://localhost/DirectInheritanceTest" );
        theInstance = manager.read( r, AbstractEntity.class );
    }

    @Test
    public void testConcreteMethod() {
        Assertions.assertEquals( "Y", theInstance.getY() );
    }

    @Test
    public void testSetRetrieve() {
        String x = theInstance.getX();
        Assertions.assertNull( x );
        theInstance.setX( "foo" );
        x = theInstance.getX();
        Assertions.assertEquals( "foo", theInstance.getX() );
    }

}
