package org.xenei.jena.entities.impl.handlers;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ResourceHandlerTest implements HandlerTestInterface {
    private ResourceHandler handler;
    private RDFNode node;

    @BeforeEach
    public void setup() {
        handler = new ResourceHandler();
        node = ResourceFactory.createResource();
    }

    @Override
    @Test
    public void testCreateRDFNode() {
        final RDFNode n = handler.createRDFNode( node );
        Assertions.assertNotNull( n );
        Assertions.assertEquals( node, n );
        Assertions.assertThrows( IllegalArgumentException.class, () -> handler.createRDFNode( Long.valueOf(  5  ) ));
    }

    @Override
    @Test
    public void testIsEmpty() {
        Assertions.assertTrue( handler.isEmpty( null ) );
        Assertions.assertFalse( handler.isEmpty( node ) );
        Assertions.assertTrue( handler.isEmpty( "foo" ) );
        Assertions.assertTrue( handler.isEmpty( "" ) );
        Assertions.assertTrue( handler.isEmpty( " " ) );
        Assertions.assertTrue( handler.isEmpty( ResourceFactory.createResource( "" ) ) );
        Assertions.assertTrue( handler.isEmpty( ResourceFactory.createResource( " " ) ) );
        Assertions.assertFalse( handler.isEmpty( ResourceFactory.createResource() ) );
        Assertions.assertTrue( handler.isEmpty( ResourceFactory.createTypedLiteral( "", XSDDatatype.XSDstring ) ) );
        Assertions.assertTrue( handler.isEmpty( ResourceFactory.createTypedLiteral( " ", XSDDatatype.XSDstring ) ) );
    }

    @Override
    @Test
    public void testParseObject() {
        final Object o = handler.parseObject( node );
        Assertions.assertNotNull( o );
        Assertions.assertEquals( node, o );
    }
}
