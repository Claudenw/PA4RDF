package org.xenei.pa4rdf.bean.handlers;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.xenei.pa4rdf.bean.test.iface.TwoValueSimpleInterface;
import org.xenei.pa4rdf.bean.EntityFactory;
import org.xenei.pa4rdf.bean.handlers.EntityHandler;

public class EntityHandlerTest extends AbstractObjectHandlerTest {
    EntityFactory em = EntityFactory.INSTANCE; 
	RDFNode node;
    TwoValueSimpleInterface instance;

    @Before
    public void setup() throws Exception {
        underTest = new EntityHandler( em, TwoValueSimpleInterface.class );
        node = ResourceFactory.createResource( "http://example.com/tvsi");
        instance = EntityFactory.read( node.asResource(), TwoValueSimpleInterface.class );
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
