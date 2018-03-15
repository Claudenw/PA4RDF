package org.xenei.pa4rdf.cache.graph;

import static org.apache.jena.testing_framework.GraphHelper.graphWith;
import static org.apache.jena.testing_framework.GraphHelper.node;
import static org.apache.jena.testing_framework.GraphHelper.txnBegin;
import static org.apache.jena.testing_framework.GraphHelper.txnCommit;
import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xenei.pa4rdf.cache.QueryExecutor;
import org.xenei.pa4rdf.cache.SubjectTable;
import org.xenei.pa4rdf.cache.impl.QueryExecutorImpl;

public class CachingGraphTests {
    private CachingGraph graph;
    private final Node graphName = Quad.defaultGraphIRI;
    private Model model;

    private QueryExecutor queryExecutor;
    private ExecutorService execService;
    
    @Before
    public void setup() {
    	
        model = ModelFactory.createDefaultModel();
        Dataset dataset = DatasetFactory.create( model );
        RDFConnection connection = RDFConnectionFactory.connect(dataset);
        queryExecutor = new QueryExecutorImpl( connection, graphName );  
        execService = Executors.newFixedThreadPool(5);
        
        graph = new CachingGraph( queryExecutor, execService );
    }
    
    @After
    public void after() {
    	execService.shutdown();
    	queryExecutor.getConnection().close();
    }

    @Test
    public void testLoadTableConcrete() {
        final Resource r = model.createResource( "urn:myRes" );
        final Resource anon = model.createResource();
        final Literal lit = model.createTypedLiteral( "Testing System" );

        r.addProperty( DC_11.creator, lit );
        r.addProperty( RDF.type, OWL.Thing );
        r.addProperty( DC_11.publisher, anon );

        final SubjectTable table = graph.getTable( r.asNode() );
        assertEquals( r.asNode(), table.getSubject() );

        Set<Node> values = table.getValues( RDF.type );
        assertEquals( 1, values.size() );
        assertEquals( OWL.Thing.asNode(), values.iterator().next() );

        values = table.getValues( DC_11.creator );
        assertEquals( 1, values.size() );
        assertEquals( lit.asNode(), values.iterator().next() );

        values = table.getValues( DC_11.publisher );
        assertEquals( 1, values.size() );
        assertEquals( anon.asNode(), values.iterator().next() );

    }

    @Test
    public void testLoadTableAnonymous() {
        final Resource r = model.createResource();
        final Resource anon = model.createResource();
        final Literal lit = model.createTypedLiteral( "Testing System" );

        r.addProperty( DC_11.creator, lit );
        r.addProperty( RDF.type, OWL.Thing );
        r.addProperty( DC_11.publisher, anon );

        final SubjectTable table = graph.getTable( r.asNode() );
        assertEquals( r.asNode(), table.getSubject() );

        Set<Node> values = table.getValues( RDF.type );
        assertEquals( 1, values.size() );
        assertEquals( OWL.Thing.asNode(), values.iterator().next() );

        values = table.getValues( DC_11.creator );
        assertEquals( 1, values.size() );
        assertEquals( lit.asNode(), values.iterator().next() );

        values = table.getValues( DC_11.publisher );
        assertEquals( 1, values.size() );
        assertEquals( anon.asNode(), values.iterator().next() );

    }

    @Test
    public void testAnonymousLinkages() {
        final Node a1 = node( "_1" );
        final Node a2 = node( "_2" );
        node( "P3" );
        final Node p2 = node( "P2" );
        final Node foo = node( "'foo'" );
        final Graph g = model.getGraph();
        txnBegin( g );
        graphWith( g, "S P O; S P1 _1; _1 P2 _2; _2 P3 _3; _3 P4 'foo'" );
        txnCommit( g );
        Set<Triple> answ;
        answ = graph.find( Node.ANY, node( "P4" ), foo ).toSet();
        assertEquals( 1, answ.size() );
        answ = graph.find( node( "S" ), Node.ANY, Node.ANY ).toSet();
        answ = graph.find( a1, p2, Node.ANY ).toSet();
        assertEquals( 1, answ.size() );
        assertEquals( a2, answ.iterator().next().getObject() );
    }
    
    @Test
    public void testRefresh() throws InterruptedException {
        final Resource r = model.createResource( "urn:myRes" );
        final Resource anon = model.createResource();
        final Literal lit = model.createTypedLiteral( "Testing System" );

        r.addProperty( DC_11.creator, lit );
        r.addProperty( RDF.type, OWL.Thing );
        r.addProperty( DC_11.publisher, anon );

        SubjectTable table = graph.getTable( r.asNode() );
        
        // update the model behind the graph
        
        final Literal title = model.createTypedLiteral( "The title" );
        r.addProperty(  DC_11.title, title );
        
        graph.refresh();
        
        Thread.sleep(  1000  );
        
        table = graph.getTable( r.asNode() );
        assertEquals( r.asNode(), table.getSubject() );

        Set<Node> values = table.getValues( RDF.type );
        assertEquals( 1, values.size() );
        assertEquals( OWL.Thing.asNode(), values.iterator().next() );

        values = table.getValues( DC_11.creator );
        assertEquals( 1, values.size() );
        assertEquals( lit.asNode(), values.iterator().next() );

        values = table.getValues( DC_11.publisher );
        assertEquals( 1, values.size() );
        assertEquals( anon.asNode(), values.iterator().next() );
        
        values = table.getValues( DC_11.title );
        assertEquals( 1, values.size() );
        assertEquals( title.asNode(), values.iterator().next() );

    }
}
