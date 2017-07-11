package org.xenei.jena.entities.cache;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.jena.graph.FrontsNode;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.graph.impl.CollectionGraph;
import org.apache.jena.graph.impl.GraphBase;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.NiceIterator;

public class CachingGraph extends GraphBase implements Graph {
	
	private Map<Node,SoftReference<SubjectTable>> map;
	private final RDFConnection connection;

	public CachingGraph(RDFConnection connection) {
		this.connection = connection;
		map = new HashMap<Node,SoftReference<SubjectTable>>();		
	}

	private SubjectTable loadTable( Node subject )
	{
		String queryStr = String.format( "CONSTRUCT { <%1$s> ?p ?o } WHERE { <%1$s> ?p ?o }", subject);
		Model model = connection.queryConstruct( queryStr );
		SubjectTable st = new SubjectTableImpl( subject, model );
		map.put( subject, new SoftReference<SubjectTable>( st ));
		return st;
	}
	
	@Override
	protected ExtendedIterator<Triple> graphBaseFind(Triple triplePattern) {
		if (triplePattern.getSubject().isConcrete())
		{
			SoftReference<SubjectTable> tblRef = map.get(triplePattern.getSubject());
			SubjectTable tbl = null;
			if (tblRef != null)
			{
				tbl = tblRef.get();
			}
			if (tbl == null)
			{
				tbl = loadTable( triplePattern.getSubject());
			}
			Graph graph = tbl.asGraph();
			return graph.find(triplePattern);
		} else {
			ExtendedIterator<Triple> retval = NiceIterator.emptyIterator();
			for (SoftReference<SubjectTable> ref : map.values())
			{
				SubjectTable tbl = ref.get();
				
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
		SoftReference<SubjectTable> tblRef = map.get(t.getSubject());
		SubjectTable tbl = null;
		if (tblRef != null)
		{
			tbl = tblRef.get();
		}
		if (tbl == null)
		{
			tbl = loadTable( t.getSubject());
		}
		tbl.addValue(t.getPredicate(), t.getSubject());
	}

	@Override
	public void performDelete(Triple t) {
		SoftReference<SubjectTable> tblRef = map.get(t.getSubject());
		SubjectTable tbl = null;
		if (tblRef != null)
		{
			tbl = tblRef.get();
		}
		if (tbl == null)
		{
			tbl = loadTable( t.getSubject());
		}
		tbl.removeValue(t.getPredicate(), t.getSubject());
	}

	@Override
	protected int graphBaseSize() {
		int retval = 0;
		for (SoftReference<SubjectTable> ref : map.values())
		{
			SubjectTable tbl = ref.get();
			
			if (tbl != null)
			{
				retval += tbl.size();
			}	
		}
		return retval;
	}
	
	private static class SubjectTableImpl implements SubjectTable {

		private Node subject;
		private Map<Node,Set<Node>> map;
		
		public SubjectTableImpl( Node subject, Model model )
		{
			this.subject = subject;
			this.map = new HashMap<Node,Set<Node>>();
			Iterator<Triple> iter =  model.getGraph().find(subject, Node.ANY, Node.ANY);
			while (iter.hasNext())
			{
				Triple t = iter.next();
				Set<Node> set = map.get(t.getPredicate());
				if (set == null)
				{
					set = new HashSet<Node>();
					map.put( t.getPredicate(), set);
				}
				set.add( t.getObject());
			}			
		}
		
		@Override
		public Node getSubject() {
			return subject;
		}

		@Override
		public Set<Node> getValues(FrontsNode predicate) {
			return getValues( predicate.asNode() );
		}
		
		public Set<Node> getValues(Node predicate)
		{
			
			if (Node.ANY.equals( predicate ))
			{
				Set<Node> retval = new HashSet<Node>();
				for (Node p : map.keySet())
				{
					retval.addAll( getValues( p ));
				}
				return retval;
			}
			Set<Node> nodes = map.get(predicate);
			if (nodes == null)
			{
				return Collections.emptySet();
			}
			return Collections.unmodifiableSet( nodes );
		}

		@Override
		public void addValue(FrontsNode predicate, FrontsNode value) {
			addValue( predicate.asNode(), value.asNode() );
		}

		@Override
		public void addValue(Node predicate, Node value) {
			Set<Node> set = map.get(predicate);
			if (set == null)
			{
				set = new HashSet<Node>();
				map.put( predicate, set );
			}
			set.add( value );
		}

		@Override
		public void removeValue(FrontsNode predicate, FrontsNode value) {
			removeValue( predicate.asNode(), value.asNode());
		}

		@Override
		public void removeValue(Node predicate, Node value) {
			Set<Node> set = map.get(predicate);
			if (set != null)
			{
				set.remove( value );		
			}
		}

		@Override
		public Set<Node> getPedicates(FrontsNode value) {
			return getPedicates( value.asNode() );
		}

		@Override
		public Set<Node> getPedicates(Node value) {
			Set<Node> retval = new HashSet<Node>();
			for (Node predicate : map.keySet())
			{
				Set<Node> values = map.get(predicate);
				if (values.contains(value))
				{
					retval.add( predicate );
				}
			}
			return retval;
		}

		@Override
		public Set<Triple> asTriples() {
			Set<Triple> retval = new HashSet<Triple>();
			for (Node predicate : map.keySet())
			{
				for (Node object : map.get(predicate))
				{					
					retval.add( new Triple( subject, predicate, object) );
				}
			}
			return retval;
		}

		@Override
		public Graph asGraph() {
			return new CollectionGraph( asTriples() );
		}

		@Override
		public boolean has(FrontsNode predicate, FrontsNode value) {
			return has( predicate.asNode(), value.asNode());
		}
		
		@Override
		public boolean has(Node predicate, Node value) {
			Set<Node> set = map.get(predicate);
			return set==null?false:set.contains(value);
		}

		@Override
		public int size() {
			int retval = 0;
			for (Set<Node> s : map.values())
			{
				retval += s.size();
			}
			return retval;
		}
		
	}

}
