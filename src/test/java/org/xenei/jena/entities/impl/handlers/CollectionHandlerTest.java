package org.xenei.jena.entities.impl.handlers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
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

public class CollectionHandlerTest extends AbstractObjectHandlerTest {
    private EntityManager em;
    private final String[] args = { "Hello", "World" };

    @Before
    public void setup() {
        em = EntityManagerFactory.create();
        underTest = new CollectionHandler( new LiteralHandler( XSDDatatype.XSDstring ), List.class );
    }

    @Override
    @Test
    public void testCreateRDFNode() {
        final RDFNode n = underTest.createRDFNode( args );
        Assert.assertNotNull( n );
        Assert.assertTrue( n instanceof Literal );
        Assert.assertTrue(  n.asLiteral().getDatatype().equals(  XSDDatatype.XSDstring ) );        
    }

    @Test
    public void testMakeCollection() {
        final Iterator<String> oIter = Arrays.asList( args ).iterator();
        final Object o = ((CollectionHandler)underTest).makeCollection( oIter );
        Assert.assertTrue( o instanceof List);
        final List<?> lst = (List)o;
        assertEquals( args.length, lst.size() );
        for (int i=0;i<args.length;i++)
        {
            assertEquals( "Error at "+i,args[i], lst.get( i ));    
        }
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
        final Model model = ModelFactory.createDefaultModel();
        final RDFNode[] nodes = { ResourceFactory.createPlainLiteral( "Hello" ),
                ResourceFactory.createPlainLiteral( "World" ) };
        final RDFList lst = model.createList( nodes );

        Assert.assertTrue( underTest.isEmpty( null ) );
        Assert.assertFalse( underTest.isEmpty( lst ) );
        Assert.assertTrue( underTest.isEmpty( model.createList( new RDFNode[0] ) ) );

        Assert.assertTrue( underTest.isEmpty( new Object[0] ) );
        Assert.assertTrue( underTest.isEmpty( new ArrayList<String>() ) );
        Assert.assertTrue( underTest.isEmpty( new HashSet<String>() ) );
    }

    @Override
    @Test
    public void testParseObject() {
        final Model model = ModelFactory.createDefaultModel();
        final RDFNode[] nodes = { ResourceFactory.createPlainLiteral( "Hello" ),
                ResourceFactory.createPlainLiteral( "World" ) };
        final RDFList lst = model.createList( nodes );
        final Object o = underTest.parseObject( lst );
        Assert.assertNotNull( o );
        Assert.assertTrue( o instanceof List );
        final List<?> lst2 = (List<?>) o;
        assertEquals( 2, lst2.size() );
        assertEquals( args[0], lst2.get( 0 ).toString() );
        assertEquals( args[1], lst2.get( 1 ).toString() );
    }
}
