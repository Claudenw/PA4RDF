package org.xenei.jena.entities.cache;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.GraphContractTest;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.testing_framework.AbstractGraphProducer;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.junit.contract.Contract;
import org.xenei.junit.contract.ContractExclude;
import org.xenei.junit.contract.ContractImpl;
import org.xenei.junit.contract.ContractSuite;
import org.xenei.junit.contract.IProducer;

//@Ignore( "Missing correct test suite")
@RunWith(ContractSuite.class)
@ContractImpl(CachingGraph.class)
@ContractExclude(value = GraphContractTest.class, methods = { "testContains_Node_Node_Node_Concrete_BlankPredicate",
"testContains_Triple_Concrete_BlankPredicate" })
public class CachingGraphContractSuite {

    private final EntityManagerImpl entityManager = (EntityManagerImpl) EntityManagerFactory.create();
    private int i = 0;

    private final AbstractGraphProducer<CachingGraph> producer = new AbstractGraphProducer<CachingGraph>() {

        @Override
        public CachingGraph createNewGraph() {
            final Node n = NodeFactory.createURI( "Graph" + i++ );
            final CachingGraph g = new CachingGraph( (EntityManagerImpl) entityManager.getNamedManager( n ) );
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