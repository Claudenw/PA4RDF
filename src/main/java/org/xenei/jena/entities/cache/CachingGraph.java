package org.xenei.jena.entities.cache;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.graph.impl.GraphBase;
import org.apache.jena.util.iterator.ExtendedIterator;

public class CachingGraph extends GraphBase implements Graph {

	public CachingGraph() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ExtendedIterator<Triple> graphBaseFind(Triple triplePattern) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void performAdd(Triple t) {
		// TODO Auto-generated method stub
		super.performAdd(t);
	}

	@Override
	public void performDelete(Triple t) {
		// TODO Auto-generated method stub
		super.performDelete(t);
	}

	@Override
	protected int graphBaseSize() {
		// TODO Auto-generated method stub
		return super.graphBaseSize();
	}

}
