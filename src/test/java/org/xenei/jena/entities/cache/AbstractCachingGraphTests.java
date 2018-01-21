package org.xenei.jena.entities.cache;

import static org.apache.jena.testing_framework.GraphHelper.graphWith;
import static org.apache.jena.testing_framework.GraphHelper.node;
import static org.apache.jena.testing_framework.GraphHelper.txnBegin;
import static org.apache.jena.testing_framework.GraphHelper.txnCommit;
import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.apache.jena.arq.querybuilder.ExprFactory;
import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.ARQ;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.testing_framework.NodeCreateUtils;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.junit.Ignore;
import org.junit.Test;

public abstract class AbstractCachingGraphTests {
    protected CachingGraph graph;
    private final Node modelName = NodeFactory.createURI( "testing" );
    protected RDFConnection connection;

    @Test
    public void testLoadTableConcrete() {
        final Model model = connection.fetch();
        final Resource r = model.createResource( "urn:myRes" );
        final Resource anon = model.createResource();
        final Literal lit = model.createTypedLiteral( "Testing System" );

        r.addProperty( DC_11.creator, lit );
        r.addProperty( RDF.type, OWL.Thing );
        r.addProperty( DC_11.publisher, anon );

        connection.put( model );
        final SelectBuilder sb = new SelectBuilder().addVar( "?p" ).addWhere( r, DC_11.publisher, "?p" );
        connection.begin( ReadWrite.READ );
        ARQ.getContext().set( ARQ.inputGraphBNodeLabels, true );
        final Resource r2 = connection.query( sb.build() ).execSelect().next().getResource( "p" );
        final Resource r3 = connection.query( sb.build() ).execSelect().next().getResource( "p" );
        connection.commit();
        assertEquals( "Anonymous node not read with same ID", r2.getId(), r3.getId() );

        SubjectTable table = graph.getTable( r.asNode() );
        assertEquals( r.asNode(), table.getSubject() );

        Set<Node> values = table.getValues( RDF.type );
        assertEquals( 1, values.size() );
        assertEquals( OWL.Thing.asNode(), values.iterator().next() );

        values = table.getValues( DC_11.creator );
        assertEquals( 1, values.size() );
        assertEquals( lit.asNode(), values.iterator().next() );

        values = table.getValues( DC_11.publisher );
        assertEquals( 1, values.size() );

        final Node n2 = values.iterator().next();
        graph.reset();
        table = graph.getTable( r.asNode() );
        values = table.getValues( DC_11.publisher );
        assertEquals( n2, values.iterator().next() );

    }

    @Test
    //@Ignore
    public void testLoadTableAnonymous() {
        final Model model = connection.fetch();
        Resource r = model.createResource();
        Resource anon = model.createResource();
        final Literal lit = model.createTypedLiteral( "Testing System" );

        r.addProperty( DC_11.creator, lit );
        r.addProperty( RDF.type, OWL.Thing );
        r.addProperty( DC_11.publisher, anon );
        connection.put( model );
        ARQ.enableBlankNodeResultLabels(true);
        //ARQ.getContext().set( ARQ.inputGraphBNodeLabels, true );

        final SelectBuilder sb = new SelectBuilder().addVar( "s" ).addVar( "o" );

        sb.addWhere( "?s", DC_11.publisher, "?o" );

        QuerySolution qs = connection.query( sb.build() ).execSelect().next();
        r = qs.getResource( "s" );
        anon = qs.getResource( "o" );
        
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

    //@Ignore
    @Test
    public void testAnonymousLinkages() {
        final Model model = connection.fetch();
      
        final Node p2 = node( "P2" );
        final Node foo = node( "'foo'" );
        final Graph g = model.getGraph();
        txnBegin( g );
        graphWith( g, "S P O; S P1 _1; _1 P2 _2; _2 P3 _3; _3 P4 'foo'" );
        txnCommit( g );
        connection.put( model );
        
        final SelectBuilder sb = new SelectBuilder().addVar( "s" ).addVar( "o" ).addWhere( "?s", p2, "?o" );

        QuerySolution qs = connection.query( sb.build() ).execSelect().next();
        Node a1 = qs.getResource(  "s"  ).asNode();
        Node a2 = qs.getResource( "o" ).asNode();

        Set<Triple> answ;
        answ = graph.find( Node.ANY, node( "P4" ), foo ).toSet();
        assertEquals( 1, answ.size() );
        answ = graph.find( node( "S" ), Node.ANY, Node.ANY ).toSet();
        assertEquals( 2, answ.size() );
        answ = graph.find( a1, p2, Node.ANY ).toSet();
        assertEquals( 1, answ.size() );
        assertEquals( a2, answ.iterator().next().getObject() );
    }
}
