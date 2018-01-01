package org.xenei.jena.entities.impl.handlers;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.testing.iface.TwoValueSimpleInterface;

public class EntityHandlerTest extends AbstractObjectHandlerTest {
    EntityManager em;
    RDFNode node;
    TwoValueSimpleInterface instance;

    @Before
    public void setup() throws Exception {
        em = EntityManagerFactory.create();
        underTest = new EntityHandler( em, TwoValueSimpleInterface.class );
        node = ResourceFactory.createResource();
        instance = em.read( node, TwoValueSimpleInterface.class );
    }

    @Override
    @Test
    public void testCreateRDFNode() {
        final RDFNode n = underTest.createRDFNode( instance );
        Assert.assertNotNull( n );
        Assert.assertEquals( node, n );
    }

    @Override
    @Test
    public void testCreateRDFNode_Null() {
        final RDFNode n = underTest.createRDFNode( null );
        Assert.assertNull( n );
    }

    @Override
    @Test
    public void testIsEmpty() {
        Assert.assertTrue( underTest.isEmpty( null ) );
        Assert.assertFalse( underTest.isEmpty( instance ) );
    }

    @Override
    @Test
    public void testParseObject() {
        final Object o = underTest.parseObject( node );
        Assert.assertNotNull( o );
        Assert.assertTrue( o instanceof TwoValueSimpleInterface );
        final TwoValueSimpleInterface a2 = (TwoValueSimpleInterface) o;
        Assert.assertEquals( instance, a2 );

    }
}
