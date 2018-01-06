package org.xenei.jena.entities.impl.handlers;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VoidHandlerTest extends AbstractObjectHandlerTest {
    RDFNode node;
    Integer instance;

    @Before
    public void setup() {
        underTest = VoidHandler.INSTANCE;
        node = ResourceFactory.createPlainLiteral( "5" );
        instance = 5;
    }

    @Override
    @Test
    public void testCreateRDFNode() {
        final RDFNode n = underTest.createRDFNode( Integer.valueOf( 5 ) );
        Assert.assertNull( n );
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
        Assert.assertTrue( underTest.isEmpty( instance ) );
        Assert.assertTrue( underTest.isEmpty( "" ) );
        Assert.assertTrue( underTest.isEmpty( " " ) );
        Assert.assertTrue( underTest.isEmpty( "foo" ) );

    }

    @Override
    @Test
    public void testParseObject() {
        final Object o = underTest.parseObject( node );
        Assert.assertNull( o );
    }
}
