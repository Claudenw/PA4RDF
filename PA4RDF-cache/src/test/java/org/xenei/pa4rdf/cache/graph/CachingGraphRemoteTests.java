package org.xenei.pa4rdf.cache.graph;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.jena.fuseki.embedded.FusekiServer;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.system.Txn;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.xenei.pa4rdf.cache.QueryExecutor;
import org.xenei.pa4rdf.cache.impl.QueryExecutorImpl;

public class CachingGraphRemoteTests extends AbstractCachingGraphTests {
    public static FusekiServer server;
    private static DatasetGraph serverdsg = DatasetGraphFactory.createTxnMem();

    private ExecutorService execService = Executors.newFixedThreadPool(5);

    
    @BeforeClass
    public static void beforeClass() {
        server = FusekiServer.make( 3030, "/ds", serverdsg );
        server.start();
    }

    @AfterClass
    public static void afterClass() {
        server.stop();
    }

    @Before
    public void setup() {
        // Clear up data in server servers
        Txn.executeWrite( serverdsg, () -> serverdsg.clear() );
        connection = RDFConnectionFactory.connect( "http://localhost:3030/ds" );
        QueryExecutor executor = new QueryExecutorImpl( connection, modelName );
        graph = new CachingGraph( executor, execService  );
    }

    // @Test
    // public void testLoadTableConcrete() {
    // final Resource r = model.createResource( "urn:myRes");
    // final Resource anon = model.createResource();
    // final Literal lit = model.createTypedLiteral( "Testing System");
    //
    // r.addProperty( DC_11.creator, lit);
    // r.addProperty(RDF.type, OWL.Thing);
    // r.addProperty( DC_11.publisher, anon);
    //
    // final SubjectTable table = graph.getTable( r.asNode() );
    // assertEquals( r.asNode(), table.getSubject() );
    //
    // Set<Node> values = table.getValues(RDF.type);
    // assertEquals( 1, values.size());
    // assertEquals( OWL.Thing.asNode(), values.iterator().next());
    //
    // values = table.getValues( DC_11.creator);
    // assertEquals( 1, values.size());
    // assertEquals( lit.asNode(), values.iterator().next());
    //
    // values = table.getValues( DC_11.publisher);
    // assertEquals( 1, values.size());
    // assertEquals( anon.asNode(), values.iterator().next());
    //
    // }
    //
    // @Test
    // public void testLoadTableAnonymous() {
    // final Resource r = model.createResource();
    // final Resource anon = model.createResource();
    // final Literal lit = model.createTypedLiteral( "Testing System");
    //
    // r.addProperty( DC_11.creator, lit);
    // r.addProperty(RDF.type, OWL.Thing);
    // r.addProperty( DC_11.publisher, anon);
    //
    // final SubjectTable table = graph.getTable( r.asNode() );
    // assertEquals( r.asNode(), table.getSubject() );
    //
    // Set<Node> values = table.getValues(RDF.type);
    // assertEquals( 1, values.size());
    // assertEquals( OWL.Thing.asNode(), values.iterator().next());
    //
    // values = table.getValues( DC_11.creator);
    // assertEquals( 1, values.size());
    // assertEquals( lit.asNode(), values.iterator().next());
    //
    // values = table.getValues( DC_11.publisher);
    // assertEquals( 1, values.size());
    // assertEquals( anon.asNode(), values.iterator().next());
    //
    // }
    //
    // @Test
    // public void testAnonymousLinkages() {
    // Node a1 = node("_1");
    // Node a2 = node("_2");
    // Node p3 = node( "P3");
    // Node p2 = node( "P2");
    // Node foo = node( "'foo'");
    // Graph g = model.getGraph();
    // txnBegin(g);
    // graphWith(g,
    // "S P O; S P1 _1; _1 P2 _2; _2 P3 _3; _3 P4 'foo'");
    // txnCommit(g);
    // Set<Triple> answ;
    // answ = graph.find( Node.ANY, node("P4"), foo).toSet();
    // assertEquals( 1, answ.size());
    // answ = graph.find( node("S"), Node.ANY, Node.ANY).toSet();
    // answ = graph.find( a1, p2, Node.ANY).toSet();
    // assertEquals( 1, answ.size());
    // assertEquals( a2, answ.iterator().next().getObject());
    // }
}
