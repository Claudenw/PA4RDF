package org.xenei.jena.entities.impl.handlers;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.testing.iface.TwoValueSimpleInterface;

public class EntityHandlerTest implements HandlerTestInterface {
    EntityHandler handler;
    EntityManager em;
    RDFNode node;
    TwoValueSimpleInterface instance;

    @BeforeEach
    public void setup() throws Exception {
        em = EntityManagerFactory.getEntityManager();
        handler = new EntityHandler( em, TwoValueSimpleInterface.class );
        node = ResourceFactory.createResource();
        instance = em.read( node, TwoValueSimpleInterface.class );
    }

    @Override
    @Test
    public void testCreateRDFNode() {
        final RDFNode n = handler.createRDFNode( instance );
        Assertions.assertNotNull( n );
        Assertions.assertEquals( node, n );
    }

    @Override
    @Test
    public void testIsEmpty() {
        Assertions.assertTrue( handler.isEmpty( null ) );
        Assertions.assertFalse( handler.isEmpty( instance ) );
    }

    @Override
    @Test
    public void testParseObject() {
        final Object o = handler.parseObject( node );
        Assertions.assertNotNull( o );
        Assertions.assertTrue( o instanceof TwoValueSimpleInterface );
        final TwoValueSimpleInterface a2 = (TwoValueSimpleInterface) o;
        Assertions.assertEquals( instance, a2 );
    }
}
