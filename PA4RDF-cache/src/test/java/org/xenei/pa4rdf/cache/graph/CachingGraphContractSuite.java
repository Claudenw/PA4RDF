package org.xenei.pa4rdf.cache.graph;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.GraphContractTest;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.testing_framework.AbstractGraphProducer;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.xenei.junit.contract.Contract;
import org.xenei.junit.contract.ContractExclude;
import org.xenei.junit.contract.ContractImpl;
import org.xenei.junit.contract.ContractSuite;
import org.xenei.junit.contract.IProducer;
import org.xenei.pa4rdf.cache.QueryExecutor;
import org.xenei.pa4rdf.cache.impl.QueryExecutorImpl;

//@Ignore( "Missing correct test suite")
@RunWith(ContractSuite.class)
@ContractImpl(CachingGraph.class)
@ContractExclude(value = GraphContractTest.class, methods = { "testContains_Node_Node_Node_Concrete_BlankPredicate",
"testContains_Triple_Concrete_BlankPredicate" })
public class CachingGraphContractSuite {

	
    private Dataset dataset = DatasetFactory.create();
    private RDFConnection connection = RDFConnectionFactory.connect(dataset);
    private ExecutorService execService = Executors.newFixedThreadPool(5);
    
    private int i = 0;

    private final AbstractGraphProducer<CachingGraph> producer = new AbstractGraphProducer<CachingGraph>() {

    	
        
        @Override
        public CachingGraph createNewGraph() {
            final Node n = NodeFactory.createURI( "Graph" + i++ );
            QueryExecutor queryExecutor = new QueryExecutorImpl( connection, n );              
            final CachingGraph g = new CachingGraph( queryExecutor, execService);
            return g;
        }

        @Override
        public Graph[] getDependsOn(Graph g) {
            return null;
        }

        @Override
        public Graph[] getNotDependsOn(Graph g) {
            return null;
        }

    };

    @Contract.Inject
    public IProducer<CachingGraph> getTripleStore() {
        return producer;
    }
}