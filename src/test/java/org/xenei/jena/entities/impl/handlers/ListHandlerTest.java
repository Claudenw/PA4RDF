package org.xenei.jena.entities.impl.handlers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;

public class ListHandlerTest implements HandlerTestInterface {
    EntityManager em;
    ListHandler handler;
    Integer instance;
    String[] args = { "Hello", "World" };

    @Before
    public void setup() {
        em = EntityManagerFactory.create();
        handler = new ListHandler( em, new LiteralHandler( XSDDatatype.XSDstring ) );

    }

    @Override
    @Test
    public void testCreateRDFNode() {
        final RDFNode n = handler.createRDFNode( args );
        Assert.assertNotNull( n );
        Assert.assertTrue( n.canAs( RDFList.class ) );
        final List<RDFNode> lst = n.as( RDFList.class ).asJavaList();
        assertEquals( 2, lst.size() );
        assertEquals( args[0], lst.get( 0 ).asLiteral().toString() );
        assertEquals( args[1], lst.get( 1 ).asLiteral().toString() );
    }

    @Test
    public void testCreateRDFNode_List() {

        final RDFNode n = handler.createRDFNode( Arrays.asList( args ) );
        Assert.assertNotNull( n );
        Assert.assertTrue( n.canAs( RDFList.class ) );
        final List<RDFNode> lst = n.as( RDFList.class ).asJavaList();
        assertEquals( 2, lst.size() );
        assertEquals( args[0], lst.get( 0 ).asLiteral().toString() );
        assertEquals( args[1], lst.get( 1 ).asLiteral().toString() );
    }

    @Override
    @Test
    public void testCreateRDFNode_Null() {
        final RDFNode n = handler.createRDFNode( null );
        Assert.assertNull( n );
    }

    @Override
    @Test
    public void testIsEmpty() {
        final Model model = ModelFactory.createDefaultModel();
        final RDFNode[] nodes = { ResourceFactory.createPlainLiteral( "Hello" ),
                ResourceFactory.createPlainLiteral( "World" ) };
        final RDFList lst = model.createList( nodes );

        Assert.assertTrue( handler.isEmpty( null ) );
        Assert.assertFalse( handler.isEmpty( lst ) );
        Assert.assertTrue( handler.isEmpty( model.createList( new RDFNode[0] ) ) );

        Assert.assertTrue( handler.isEmpty( new Object[0] ) );
        Assert.assertTrue( handler.isEmpty( new ArrayList<String>() ) );
        Assert.assertTrue( handler.isEmpty( new HashSet<String>() ) );
    }

    @Override
    @Test
    public void testParseObject() {
        final Model model = ModelFactory.createDefaultModel();
        final RDFNode[] nodes = { ResourceFactory.createPlainLiteral( "Hello" ),
                ResourceFactory.createPlainLiteral( "World" ) };
        final RDFList lst = model.createList( nodes );
        final Object o = handler.parseObject( lst );
        Assert.assertNotNull( o );
        Assert.assertTrue( o instanceof List );
        final List<?> lst2 = (List<?>) o;
        assertEquals( 2, lst2.size() );
        assertEquals( args[0], lst2.get( 0 ).toString() );
        assertEquals( args[1], lst2.get( 1 ).toString() );
    }
}
