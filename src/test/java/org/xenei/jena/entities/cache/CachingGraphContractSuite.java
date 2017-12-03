package org.xenei.jena.entities.cache;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.testing_framework.AbstractGraphProducer;
import org.junit.runner.RunWith;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.junit.contract.Contract;
import org.xenei.junit.contract.ContractImpl;
import org.xenei.junit.contract.ContractSuite;
import org.xenei.junit.contract.IProducer;

@RunWith(ContractSuite.class)
@ContractImpl(CachingGraph.class)
public class CachingGraphContractSuite
{
	EntityManagerImpl entityManager = (EntityManagerImpl) EntityManagerFactory.create();
	int i = 0;

	private AbstractGraphProducer<CachingGraph> producer = new AbstractGraphProducer<CachingGraph>()
	{

		@Override
		public CachingGraph createNewGraph()
		{
			Node n = NodeFactory.createURI( "Graph" + i++);
			CachingGraph g = new CachingGraph(
					(EntityManagerImpl)entityManager.getNamedManager(n));
			return g;
		}

		@Override
		public Graph[] getDependsOn(Graph g)
		{
			return null;
		}

		@Override
		public Graph[] getNotDependsOn(Graph g)
		{
			return null;
		}

		

	};

	@Contract.Inject
	public IProducer<CachingGraph> getTripleStore()
	{
		return producer;
	}
}