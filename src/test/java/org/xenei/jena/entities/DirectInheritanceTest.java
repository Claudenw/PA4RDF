package org.xenei.jena.entities;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.exceptions.MissingAnnotationException;
import org.xenei.jena.entities.testing.bad.UnannotatedInterface;
import org.xenei.jena.entities.testing.iface.TwoValueSimpleInterface;

public class DirectInheritanceTest {
    private Model m;
    private final EntityManager manager = EntityManagerFactory.getEntityManager();
    private TwoValueSimpleInterface theInstance;
    private Resource r;

    @BeforeEach
    public void setup() throws Exception {
        m = ModelFactory.createDefaultModel();
        r = m.createResource( "http://localhost/DirectInheritanceTest" );
        System.err.println( "getting instance" );
        theInstance = manager.read( r, TwoValueSimpleInterface.class );
    }

    @Test
    public void testEmptyIsNull() {
        theInstance.setZ( "foo" );
        Assertions.assertNotNull( theInstance.getZ() );
        Assertions.assertEquals( "foo", theInstance.getZ() );

        theInstance.setZ( "" );
        Assertions.assertNull( theInstance.getZ() );

    }

    @Test
    public void testNullMethods() {
       Assertions.assertThrows( MissingAnnotationException.class, () -> manager.read( r, UnannotatedInterface.class ));
    }

    /*
     * @Test public void testResourceAncestorMethods() {
     *
     * Assertions.assertEquals(r.getModel(), theInstance.getModel());
     *
     * }
     *
     * @Test public void testResourceMethods() {
     * Assertions.assertEquals(r.getURI(), theInstance.getURI()); }
     */
}
