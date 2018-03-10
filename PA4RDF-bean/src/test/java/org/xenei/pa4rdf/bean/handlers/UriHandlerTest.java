package org.xenei.pa4rdf.bean.handlers;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.pa4rdf.bean.handlers.UriHandler;

public class UriHandlerTest extends AbstractObjectHandlerTest {
    RDFNode node;
    Integer instance;

    @Before
    public void setup() {
        underTest = UriHandler.INSTANCE;
        node = ResourceFactory.createResource( "http://example.com" );
        instance = 5;
    }

    @Override
    @Test
    public void testCreateRDFNode() {
        final RDFNode n = underTest.createRDFNode( "http://example.com" );
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
        Assert.assertFalse( underTest.isEmpty( node ) );
        Assert.assertTrue( underTest.isEmpty( "" ) );
        Assert.assertTrue( underTest.isEmpty( " " ) );
        Assert.assertFalse( underTest.isEmpty( "foo" ) );

    }

    @Override
    @Test
    public void testParseObject() {
        final Object o = underTest.parseObject( node );
        Assert.assertNotNull( o );
        Assert.assertEquals( "http://example.com", o );
    }
}
