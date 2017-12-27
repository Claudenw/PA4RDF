package org.xenei.jena.entities.cache;

import static org.apache.jena.testing_framework.GraphHelper.assertContainsAll ;
import static org.apache.jena.testing_framework.GraphHelper.assertIsomorphic ;
import static org.apache.jena.testing_framework.GraphHelper.assertOmitsAll ;
import static org.apache.jena.testing_framework.GraphHelper.graphAddTxn ;
import static org.apache.jena.testing_framework.GraphHelper.graphWith ;
import static org.apache.jena.testing_framework.GraphHelper.iteratorToSet ;
import static org.apache.jena.testing_framework.GraphHelper.memGraph ;
import static org.apache.jena.testing_framework.GraphHelper.node ;
import static org.apache.jena.testing_framework.GraphHelper.nodeSet ;
import static org.apache.jena.testing_framework.GraphHelper.triple ;
import static org.apache.jena.testing_framework.GraphHelper.tripleArray ;
import static org.apache.jena.testing_framework.GraphHelper.tripleSet ;
import static org.apache.jena.testing_framework.GraphHelper.txnBegin ;
import static org.apache.jena.testing_framework.GraphHelper.txnCommit ;
import static org.apache.jena.testing_framework.GraphHelper.txnRollback ;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.fuseki.embedded.FusekiServer;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.ARQ;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.AnonId;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.system.Txn;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.junit.contract.ContractTest;


public abstract class AbstractCachingGraphTests
{
	protected CachingGraph graph;
	private final Node modelName = NodeFactory.createURI( "testing");
	protected RDFConnection connection;


	@Test
	public void testLoadTableConcrete() {
		Model model = connection.fetch();
		final Resource r = model.createResource( "urn:myRes");
		final Resource anon = model.createResource();
		final Literal lit = model.createTypedLiteral( "Testing System");
		
		r.addProperty( DC_11.creator, lit);
		r.addProperty(RDF.type, OWL.Thing);
		r.addProperty( DC_11.publisher, anon);

		connection.put(model);
		SelectBuilder sb = new SelectBuilder().addVar( "?p").addWhere( r, DC_11.publisher, "?p"); 
		connection.begin(ReadWrite.READ);
		//ARQ.getContext().set(ARQ.inputGraphBNodeLabels, true);
		Resource r2=connection.query(sb.build()).execSelect().next().getResource("p");
		Resource r3=connection.query(sb.build()).execSelect().next().getResource("p");
		connection.commit();
		assertEquals( "Anonymous node not read with same ID", r2.getId(), r3.getId() );
		
		SubjectTable table = graph.getTable( r.asNode() );
		assertEquals( r.asNode(), table.getSubject() );

		Set<Node> values = table.getValues(RDF.type);
		assertEquals( 1, values.size());
		assertEquals( OWL.Thing.asNode(), values.iterator().next());

		values = table.getValues( DC_11.creator);
		assertEquals( 1, values.size());
		assertEquals( lit.asNode(), values.iterator().next());

		values = table.getValues( DC_11.publisher);
		assertEquals( 1, values.size());
		
		Node n2 = values.iterator().next();
		graph.reset();
		table = graph.getTable( r.asNode() );
		values = table.getValues( DC_11.publisher);
		assertEquals( n2, values.iterator().next());

	}

	@Test
	public void testLoadTableAnonymous() {
		Model model = connection.fetch();
		Resource r = model.createResource();
		final Resource anon = model.createResource();
		final Literal lit = model.createTypedLiteral( "Testing System");

		r.addProperty( DC_11.creator, lit);
		r.addProperty(RDF.type, OWL.Thing);
		r.addProperty( DC_11.publisher, anon);
		connection.put( model );
		
		connection.fetch().listStatements().forEachRemaining( stmt -> System.out.println( stmt ));

		r = connection.fetch().listStatements().next().getResource();
		final SubjectTable table = graph.getTable( r.asNode() );
		
		assertEquals( r.asNode(), table.getSubject() );

		Set<Node> values = table.getValues(RDF.type);
		assertEquals( 1, values.size());
		assertEquals( OWL.Thing.asNode(), values.iterator().next());

		values = table.getValues( DC_11.creator);
		assertEquals( 1, values.size());
		assertEquals( lit.asNode(), values.iterator().next());

		values = table.getValues( DC_11.publisher);
		assertEquals( 1, values.size());
		assertEquals( anon.asNode(), values.iterator().next());

	}
	
	@Test
	public void testAnonymousLinkages() {		
		Model model = connection.fetch();
		Node a1 = node("_1");
		Node a2 = node("_2");
		Node p3 = node( "P3");
		Node p2 = node( "P2");
		Node foo = node( "'foo'");
		Graph g = model.getGraph();
		txnBegin(g);
		graphWith(g,
				"S P O; S P1 _1; _1 P2 _2; _2 P3 _3; _3 P4 'foo'");
		txnCommit(g);
		connection.put( model );
		
		Set<Triple> answ;
		answ = graph.find( Node.ANY, node("P4"), foo).toSet();
		assertEquals( 1, answ.size());
		answ = graph.find( node("S"), Node.ANY, Node.ANY).toSet();
		answ = graph.find( a1, p2, Node.ANY).toSet();
		assertEquals( 1, answ.size());
		assertEquals( a2, answ.iterator().next().getObject());	
	}
}
