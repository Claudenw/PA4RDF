package org.xenei.jena.entities.cache;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.arq.querybuilder.ConstructBuilder;
import org.apache.jena.arq.querybuilder.UpdateBuilder;
import org.apache.jena.graph.BlankNodeId;
import org.apache.jena.graph.FrontsNode;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.GraphListener;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.NodeVisitor;
import org.apache.jena.graph.Node_ANY;
import org.apache.jena.graph.Node_Blank;
import org.apache.jena.graph.Node_Literal;
import org.apache.jena.graph.Node_URI;
import org.apache.jena.graph.Node_Variable;
import org.apache.jena.graph.Triple;
import org.apache.jena.graph.impl.CollectionGraph;
import org.apache.jena.graph.impl.GraphBase;
import org.apache.jena.graph.impl.LiteralLabel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.NiceIterator;
import org.xenei.jena.entities.impl.EntityManagerImpl;

/**
 * A graph that caches remote data.
 * 
 * This implementation caches predicates and objects for each subject that is 
 * retrieved from the remote system.  
 *
 */
public class CachingGraph extends GraphBase implements Graph {

	private final Map<Node,SoftReference<SubjectTable>> map;
	private final EntityManagerImpl entityManager;
	private final Node graphNode;
	private final Node graphWriteNode;
	private static final Var p = Var.alloc("p");
	private static final Var o = Var.alloc("o");
	/**
	 * Constructor.
	 * @param connection the connection to the remote system.
	 */
	public CachingGraph(EntityManagerImpl entityManager) {
		this.entityManager = entityManager;
		this.graphNode = entityManager.getModelName();
		this.graphWriteNode = (graphNode.equals( Quad.unionGraph))?Quad.defaultGraphIRI:graphNode;
		map = new HashMap<Node,SoftReference<SubjectTable>>();	
		this.getEventManager().register( new Listener());
	}

	/**
	 * Load the table from the remote system.
	 * @param subject the subject of the table.
	 * @return the SubjectTable for the subject.
	 */
	private SubjectTable loadTable( Node subject )
	{
		ConstructBuilder cb = new ConstructBuilder().addConstruct(subject, p, o).addWhere( subject, p, o);			
		final Model model = entityManager.execute( cb.build() ).execConstruct();
		final SubjectTable st = new SubjectTableImpl( subject, model );
		map.put( subject, new SoftReference<SubjectTable>( st ));
		return st;
	}

	/**
	 * Sync the caching graph with the remote graph.
	 * predicates and objects for every subject is read from the 
	 * remote graph.  Local changes are discarded.
	 */
	public void sync() {
		for (final Node subject : map.keySet())
		{
			final SoftReference<SubjectTable> sr = map.get(subject);
			if (sr.get() == null)
			{
				map.remove(subject);
			}
			else
			{
				loadTable( subject );
			}
		}
	}

	/**
	 * Get the SubjectTable for the specified subject.
	 * @param subject the subject to get the subject for.
	 * 
	 * If the subject table is already loaded it is returned otherwise
	 * it is created by reading from the remote system.
	 * 
	 * @return the subject table.
	 */
	public SubjectTable getTable( Node subject )
	{
		final SoftReference<SubjectTable> tblRef = map.get(subject);
		SubjectTable tbl = null;
		if (tblRef != null)
		{
			tbl = tblRef.get();
		}
		if (tbl == null || tbl.isEmpty())
		{
			tbl = loadTable( subject );
		}
		return tbl;
	}

	@Override
	protected ExtendedIterator<Triple> graphBaseFind(Triple triplePattern) {
		if (triplePattern.getSubject().isConcrete())
		{
			//SoftReference<SubjectTable> tblRef = map.get(triplePattern.getSubject());
			final SubjectTable tbl = getTable( triplePattern.getSubject());

			final Graph graph = tbl.asGraph();
			return graph.find(triplePattern);
		} else {
			ExtendedIterator<Triple> retval = NiceIterator.emptyIterator();
			for (final SoftReference<SubjectTable> ref : map.values())
			{
				final SubjectTable tbl = ref.get();

				if (tbl != null)
				{
					retval = retval.andThen( tbl.asGraph().find(triplePattern));
				}	
			}
			return retval;
		}
	}

	@Override
	public void performAdd(Triple t) {
		final SoftReference<SubjectTable> tblRef = map.get(t.getSubject());
		SubjectTable tbl = null;
		if (tblRef != null)
		{
			tbl = tblRef.get();
		}
		if (tbl == null)
		{
			tbl = loadTable( t.getSubject());
		}
		tbl.addValue(t.getPredicate(), t.getObject());		
	}

	@Override
	public void performDelete(Triple t) {
		final SoftReference<SubjectTable> tblRef = map.get(t.getSubject());
		SubjectTable tbl = null;
		if (tblRef != null)
		{
			tbl = tblRef.get();
		}
		if (tbl == null)
		{
			tbl = loadTable( t.getSubject());
		}
		tbl.removeValue(t.getPredicate(), t.getObject());
		if (tbl.isEmpty())
		{
			map.remove( t.getSubject() );
		}
	}

	@Override
	protected int graphBaseSize() {
		int retval = 0;
		for (final SoftReference<SubjectTable> ref : map.values())
		{
			final SubjectTable tbl = ref.get();

			if (tbl != null)
			{
				retval += tbl.size();
			}	
		}
		return retval;
	}

	private static class SubjectTableImpl implements SubjectTable {

		private final Node subject;
		private final Graph subGraph;

		public SubjectTableImpl( Node subject, Model model )
		{
			this.subject = subject;
			this.subGraph = model.getGraph();
		}

		@Override
		public Node getSubject() {
			return subject;
		}
		
		@Override
		public boolean isEmpty() {
			return subGraph.isEmpty();
		}

		@Override
		public Set<Node> getValues(FrontsNode predicate) {
			return getValues( predicate.asNode() );
		}

		public Set<Node> getValues(Node predicate)
		{
			if (isEmpty())
			{
				return Collections.emptySet();
			}
			return Collections.unmodifiableSet(subGraph.find(subject, predicate, Node.ANY).mapWith( tpl -> tpl.getObject()).toSet());
		}

		@Override
		public void addValue(FrontsNode predicate, FrontsNode value) {
			addValue( predicate.asNode(), value.asNode() );
		}

		@Override
		public void addValue(Node predicate, Node value) {
			subGraph.add( new Triple( subject, predicate, value ));
		}

		@Override
		public void removeValue(FrontsNode predicate, FrontsNode value) {
			removeValue( predicate.asNode(), value.asNode());
		}

		@Override
		public void removeValue(Node predicate, Node value) {
			subGraph.remove( subject, predicate, value );
		}

		@Override
		public Set<Node> getPedicates(FrontsNode value) {
			return getPedicates( value.asNode() );
		}

		@Override
		public Set<Node> getPedicates(Node value) {
			if (isEmpty())
			{
				return Collections.emptySet();
			}
			return Collections.unmodifiableSet(subGraph.find(subject, Node.ANY, value).mapWith( tpl -> tpl.getPredicate()).toSet());
		}

		@Override
		public Graph asGraph() {
			return subGraph;
		}

		@Override
		public boolean has(FrontsNode predicate, FrontsNode value) {
			return has( predicate.asNode(), value.asNode());
		}

		@Override
		public boolean has(Node predicate, Node value) {
			if (isEmpty())
			{
				return false;
			}
			return subGraph.find(subject, predicate, value).hasNext();
		}

		@Override
		public int size() {
			return subGraph.size();
		}

		@Override
		public String toString() {
			return String.format( "SubjectTable[%s %s]", subject, subGraph);
		}
	}
	
	private class Listener implements GraphListener {
		
		@Override
		public void notifyAddTriple(Graph g, Triple t)
		{
			 entityManager.getUpdateHandler().prepare(new UpdateBuilder().addInsert( graphWriteNode, t).build());
		}

		@Override
		public void notifyAddArray(Graph g, Triple[] triples)
		{
			UpdateBuilder builder = new UpdateBuilder();
			for (Triple t : triples)
			{
				builder.addInsert(graphWriteNode,t);
			}
			entityManager.getUpdateHandler().prepare( builder.build() );
		}

		@Override
		public void notifyAddList(Graph g, List<Triple> triples)
		{
			UpdateBuilder builder = new UpdateBuilder();
			triples.forEach( t -> builder.addInsert(graphWriteNode,t));
			entityManager.getUpdateHandler().prepare( builder.build() );
		}

		@Override
		public void notifyAddIterator(Graph g, Iterator<Triple> it)
		{
			UpdateBuilder builder = new UpdateBuilder();
			it.forEachRemaining( t -> builder.addInsert(graphWriteNode,t));
			entityManager.getUpdateHandler().prepare( builder.build() );
		}

		@Override
		public void notifyAddGraph(Graph g, Graph added)
		{
			UpdateBuilder builder = new UpdateBuilder();
			added.find().forEachRemaining( t -> builder.addInsert(graphWriteNode,t));
			entityManager.getUpdateHandler().prepare( builder.build() );
		}

		@Override
		public void notifyDeleteTriple(Graph g, Triple t)
		{
			entityManager.getUpdateHandler().prepare(new UpdateBuilder().addDelete(graphWriteNode, t).build());
		}

		@Override
		public void notifyDeleteList(Graph g, List<Triple> L)
		{
			UpdateBuilder builder = new UpdateBuilder();
			L.forEach( t -> builder.addDelete(graphWriteNode,t));
			entityManager.getUpdateHandler().prepare( builder.build() );
		}

		@Override
		public void notifyDeleteArray(Graph g, Triple[] triples)
		{
			UpdateBuilder builder = new UpdateBuilder();
			for (Triple t : triples)
			{
				builder.addDelete(graphWriteNode,t);
			}
			entityManager.getUpdateHandler().prepare( builder.build() );
		}

		@Override
		public void notifyDeleteIterator(Graph g, Iterator<Triple> it)
		{
			UpdateBuilder builder = new UpdateBuilder();
			it.forEachRemaining( t -> builder.addDelete(graphWriteNode,t));
			entityManager.getUpdateHandler().prepare( builder.build() );
		}

		@Override
		public void notifyDeleteGraph(Graph g, Graph removed)
		{
			UpdateBuilder builder = new UpdateBuilder();
			removed.find().forEachRemaining( t -> builder.addDelete(graphWriteNode,t));
			entityManager.getUpdateHandler().prepare( builder.build() );
		}

		@Override
		public void notifyEvent(Graph source, Object value)
		{
			// do nothing
		}
		
	}

}
