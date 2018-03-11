//package org.xenei.pa4rdf.cache;
//
//import org.apache.jena.graph.Graph;
//import org.apache.jena.graph.GraphContractTest;
//import org.apache.jena.graph.Node;
//import org.apache.jena.graph.NodeFactory;
//import org.apache.jena.tdb.store.GraphTxnTDB;
//import org.apache.jena.tdb.sys.TDBMaker;
//import org.apache.jena.tdb.transaction.DatasetGraphTransaction;
//import org.apache.jena.testing_framework.AbstractGraphProducer;
//import org.junit.Ignore;
//import org.junit.runner.RunWith;
//import org.xenei.junit.contract.Contract;
//import org.xenei.junit.contract.ContractExclude;
//import org.xenei.junit.contract.ContractImpl;
//import org.xenei.junit.contract.ContractSuite;
//import org.xenei.junit.contract.IProducer;
//
//@Ignore( "Not working with current contract tests")
//@RunWith(ContractSuite.class)
//@ContractImpl(GraphTxnTDB.class)
//@ContractExclude(value = GraphContractTest.class, methods = { "testPartialUpdate" })
//public class GraphTxnTDB_CS {
//
//    private final DatasetGraphTransaction dsg = TDBMaker.createDatasetGraphTransaction();
//    private int i = 0;
//
//    private final AbstractGraphProducer<GraphTxnTDB> producer = new AbstractGraphProducer<GraphTxnTDB>() {
//
//        @Override
//        public GraphTxnTDB createNewGraph() {
//            final Node n = NodeFactory.createURI( "Graph" + i++ );
//            final GraphTxnTDB g = new GraphTxnTDB( dsg, n );
//            return g;
//        }
//
//        @Override
//        public Graph[] getDependsOn(Graph g) {
//            return null;
//        }
//
//        @Override
//        public Graph[] getNotDependsOn(Graph g) {
//            return null;
//        }
//
//    };
//
//    @Contract.Inject
//    public IProducer<GraphTxnTDB> getTripleStore() {
//        return producer;
//    }
//}