package org.xenei.jena.entities.impl.handlers;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UriHandlerTest implements HandlerTestInterface {
    UriHandler handler;
    RDFNode node;
    Integer instance;

    @BeforeEach
    public void setup() {
        handler = UriHandler.INSTANCE;
        node = ResourceFactory.createResource( "http://example.com" );
        instance = 5;
    }

    @Override
    @Test
    public void testCreateRDFNode() {
        final RDFNode n = handler.createRDFNode( "http://example.com" );
        Assertions.assertNotNull( n );
        Assertions.assertEquals( node, n );
    }

    @Override
    @Test
    public void testIsEmpty() {
        Assertions.assertTrue( handler.isEmpty( null ) );
        Assertions.assertFalse( handler.isEmpty( node ) );
        Assertions.assertTrue( handler.isEmpty( "" ) );
        Assertions.assertTrue( handler.isEmpty( " " ) );
        Assertions.assertFalse( handler.isEmpty( "foo" ) );

    }

    @Override
    @Test
    public void testParseObject() {
        final Object o = handler.parseObject( node );
        Assertions.assertNotNull( o );
        Assertions.assertEquals( "http://example.com", o );
    }
}
