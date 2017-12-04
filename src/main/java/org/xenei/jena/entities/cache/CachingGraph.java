package org.xenei.jena.entities.cache;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.arq.querybuilder.ExprFactory;
import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.arq.querybuilder.UpdateBuilder;
import org.apache.jena.graph.Capabilities;
import org.apache.jena.graph.FrontsNode;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.GraphEventManager;
import org.apache.jena.graph.GraphStatisticsHandler;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.TransactionHandler;
import org.apache.jena.graph.Triple;
import org.apache.jena.graph.impl.AllCapabilities;
import org.apache.jena.graph.impl.GraphBase;
import org.apache.jena.mem.TrackingTripleIterator;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.AnonId;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.AddDeniedException;
import org.apache.jena.shared.DeleteDeniedException;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.path.Path;
import org.apache.jena.sparql.path.PathFactory;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.IteratorIterator;
import org.apache.jena.util.iterator.WrappedIterator;
import org.xenei.jena.entities.impl.EntityManagerImpl;

/**
 * A graph that caches remote data.
 * 
 * This implementation caches predicates and objects for each subject that is
 * retrieved from the remote system.
 *
 */
public class CachingGraph extends GraphBase implements Graph {
	private final static Log LOG = LogFactory.getLog(CachingGraph.class);
	private final Map<Node, SoftReference<SubjectTable>> map;
	private final EntityManagerImpl entityManager;
	private final Node graphNode;
	private final Node graphWriteNode;
	private static final Var S = Var.alloc("s");
	private static final Var P = Var.alloc("p");
	private static final Var O = Var.alloc("o");

	/**
	 * Constructor.
	 * 
	 * @param connection
	 *            the connection to the remote system.
	 */
	public CachingGraph(EntityManagerImpl entityManager) {
		this.entityManager = entityManager;
		this.graphNode = entityManager.getModelName();
		this.graphWriteNode = (graphNode.equals(Quad.unionGraph)) ? Quad.defaultGraphIRI : graphNode;
		map = Collections.synchronizedMap(new HashMap<Node, SoftReference<SubjectTable>>());
	}

	@Override
	public Capabilities getCapabilities() {
		{
			if (capabilities == null)
				capabilities = new AllCapabilities() {

					@Override
					public boolean sizeAccurate() {
						return false;
					}
				};
			return capabilities;
		}
	}

	/**
	 * Sync the caching graph with the remote graph. predicates and objects for
	 * every subject is read from the remote graph. Local changes are discarded.
	 */
	public void sync() {
		HashSet<Node> keys = new HashSet<Node>();
		synchronized (map) {
			keys.addAll(map.keySet());
		}
		for (final Node subject : keys) {
			final SoftReference<SubjectTable> sr = map.get(subject);
			SubjectTable st = sr==null?null:sr.get();
			if (st == null) {
				map.remove(subject);
			} else {
				if (st.getSubject().equals(subject)) {
					new TableLoader(subject);
				}
			}
		}
	}

	/**
	 * Get the SubjectTable for the specified subject.
	 * 
	 * @param subject
	 *            the subject to get the subject for.
	 * 
	 *            If the subject table is already loaded it is returned otherwise it
	 *            is created by reading from the remote system.
	 * 
	 * @return the subject table.
	 */
	public SubjectTable getTable(Node subject) {
		final SoftReference<SubjectTable> tblRef = map.get(subject);
		SubjectTable tbl = null;
		if (tblRef != null) {
			tbl = tblRef.get();
		}
		if (tbl == null || tbl.isEmpty()) {
			tbl = new TableLoader(subject).getTable();
		}
		return tbl;
	}

	@Override
	protected ExtendedIterator<Triple> graphBaseFind(Triple triplePattern) {
		if (triplePattern.getSubject().isConcrete()) {
			// SoftReference<SubjectTable> tblRef =
			// map.get(triplePattern.getSubject());
			final SubjectTable tbl = getTable(triplePattern.getSubject());

			final Graph graph = tbl.asGraph();
			return graph.find(triplePattern);
		} else {
			/*
			 * we need to pull in all the subjects from the parent graph and add them to our
			 * graph as we find them.
			 * 
			 * If we find one we already have we keep our copy and return it. We do this
			 * becaue we may have deleted items that have not yet been deleted in the remote
			 * system.
			 */
			BlockIterator blockIterator = new BlockIterator(triplePattern);

			Iterator<Iterator<Triple>> iter = WrappedIterator.create(blockIterator)
					.mapWith(tbl -> tbl.asGraph().find(triplePattern));
			return WrappedIterator.create(new IteratorIterator(iter));
		}
	}

	@Override
	public void performAdd(Triple t) {
		final SoftReference<SubjectTable> tblRef = map.get(t.getSubject());
		SubjectTable tbl = null;
		if (tblRef != null) {
			tbl = tblRef.get();
		}
		if (tbl == null) {
			tbl = new TableLoader(t.getSubject()).getTable();
		}
		tbl.addValue(t.getPredicate(), t.getObject());
		entityManager.getUpdateHandler().prepare(new UpdateBuilder().addInsert(graphWriteNode, t).build());
	}

	@Override
	public void performDelete(Triple t) {
		final SoftReference<SubjectTable> tblRef = map.get(t.getSubject());
		SubjectTable tbl = null;
		if (tblRef != null) {
			tbl = tblRef.get();
		}
		if (tbl == null) {
			tbl = new TableLoader(t.getSubject()).getTable();
		}
		tbl.removeValue(t.getPredicate(), t.getObject());
		entityManager.getUpdateHandler().prepare(new UpdateBuilder().addDelete(graphWriteNode, t).build());
		if (tbl.isEmpty()) {
			map.remove(t.getSubject());
		}
	}

	@Override
	protected int graphBaseSize() {
		int retval = 0;
		synchronized (map) {
			for (final SoftReference<SubjectTable> ref : map.values()) {
				final SubjectTable tbl = ref.get();

				if (tbl != null) {
					retval += tbl.size();
				}
			}
		}
		return retval;
	}

	private class SubjectTableImpl implements SubjectTable {

		private final Node subject;
		private final Graph subGraph;

		public SubjectTableImpl(Node subject, Model model) {
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
			return getValues(predicate.asNode());
		}

		public Set<Node> getValues(Node predicate) {
			if (isEmpty()) {
				return Collections.emptySet();
			}
			return Collections.unmodifiableSet(
					subGraph.find(subject, predicate, Node.ANY).mapWith(tpl -> tpl.getObject()).toSet());
		}

		@Override
		public void addValue(FrontsNode predicate, FrontsNode value) {
			addValue(predicate.asNode(), value.asNode());
		}

		@Override
		public void addValue(Node predicate, Node value) {
			subGraph.add(new Triple(subject, predicate, value));
		}

		@Override
		public void removeValue(FrontsNode predicate, FrontsNode value) {
			removeValue(predicate.asNode(), value.asNode());
		}

		@Override
		public void removeValue(Node predicate, Node value) {
			subGraph.remove(subject, predicate, value);
		}

		@Override
		public Set<Node> getPedicates(FrontsNode value) {
			return getPedicates(value.asNode());
		}

		@Override
		public Set<Node> getPedicates(Node value) {
			if (isEmpty()) {
				return Collections.emptySet();
			}
			return Collections.unmodifiableSet(
					subGraph.find(subject, Node.ANY, value).mapWith(tpl -> tpl.getPredicate()).toSet());
		}

		@Override
		public Graph asGraph() {
			return new WrappedGraph(subGraph);
		}

		@Override
		public boolean has(FrontsNode predicate, FrontsNode value) {
			return has(predicate.asNode(), value.asNode());
		}

		@Override
		public boolean has(Node predicate, Node value) {
			if (isEmpty()) {
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
			return String.format("SubjectTable[%s %s]", subject, subGraph);
		}
	}

	private class WrappedGraph implements Graph {
		private Graph subGraph;

		private WrappedGraph(Graph subGraph) {
			this.subGraph = subGraph;
		}

		@Override
		public boolean dependsOn(Graph other) {
			return subGraph.dependsOn(other);
		}

		@Override
		public TransactionHandler getTransactionHandler() {
			return subGraph.getTransactionHandler();
		}

		@Override
		public Capabilities getCapabilities() {
			return subGraph.getCapabilities();
		}

		@Override
		public GraphStatisticsHandler getStatisticsHandler() {
			return subGraph.getStatisticsHandler();
		}

		@Override
		public PrefixMapping getPrefixMapping() {
			return subGraph.getPrefixMapping();
		}

		@Override
		public void add(Triple t) throws AddDeniedException {
			subGraph.add(t);
		}

		@Override
		public void delete(Triple t) throws DeleteDeniedException {
			subGraph.delete(t);
		}

		@Override
		public ExtendedIterator<Triple> find(Triple m) {
			return new DeletingTripleIterator(subGraph.find(m));
		}

		@Override
		public ExtendedIterator<Triple> find(Node s, Node p, Node o) {
			return new DeletingTripleIterator(subGraph.find(s, p, o));
		}

		@Override
		public ExtendedIterator<Triple> find() {
			return new DeletingTripleIterator(subGraph.find());
		}

		@Override
		public boolean isIsomorphicWith(Graph g) {
			return subGraph.isIsomorphicWith(g);
		}

		@Override
		public boolean contains(Node s, Node p, Node o) {
			return subGraph.contains(s, p, o);
		}

		@Override
		public boolean contains(Triple t) {
			return subGraph.contains(t);
		}

		@Override
		public void clear() {
			subGraph.clear();
		}

		@Override
		public void remove(Node s, Node p, Node o) {
			subGraph.remove(s, p, o);
		}

		@Override
		public void close() {
			subGraph.close();
		}

		@Override
		public boolean isEmpty() {
			return subGraph.isEmpty();
		}

		@Override
		public int size() {
			return subGraph.size();
		}

		@Override
		public boolean isClosed() {
			return subGraph.isClosed();
		}

		@Override
		public GraphEventManager getEventManager() {
			return subGraph.getEventManager();
		}

	}

	private class DeletingTripleIterator extends TrackingTripleIterator {

		private DeletingTripleIterator(Iterator<Triple> it) {
			super(it);
		}

		@Override
		public void remove() {
			super.remove();
			CachingGraph.this.getEventManager().notifyDeleteTriple(CachingGraph.this, current);
			entityManager.getUpdateHandler().prepare(new UpdateBuilder().addDelete(graphWriteNode, current).build());
		}
	}

	private class BlockIterator implements Iterator<SubjectTable> {

		private ResultSet rs;
		private Resource subj;

		private BlockIterator(Triple triplePattern) {
			SelectBuilder sb = getSelectQuery(triplePattern);

			/*
			 * we need to pull in all the subjects from the parent graph and add them to our
			 * graph as we find them.
			 * 
			 * If we find one we already have we keep our copy and return it. We do this
			 * becaue we may have deleted items that have not yet been deleted in the remote
			 * system.
			 */
			rs = entityManager.execute(sb.build()).execSelect();
			subj = null;
		}

		private SelectBuilder getSelectQuery(Triple triplePattern) {
			SelectBuilder sb = new SelectBuilder().setDistinct(true).addWhere(S, P, O).addOrderBy(S);

			if (triplePattern.equals(Triple.ANY)) {
				sb.addVar(S);
			} else {
				Object s = triplePattern.getSubject().isConcrete() ? triplePattern.getSubject() : S;
				Object p = triplePattern.getPredicate().isConcrete() ? triplePattern.getPredicate() : Var.alloc("p2");
				Object o = triplePattern.getObject().isConcrete() ? triplePattern.getObject() : Var.alloc("o2");

				// SelectBuilder inner = new SelectBuilder()
				// .addWhere( s, p, o );

				sb.addWhere(s, p, o);
				if (!s.equals(S)) {
					ExprFactory exprF = sb.getExprFactory();
					sb.addVar(exprF.asExpr(s), S);
				} else {
					sb.addVar(S);
				}

			}
			return sb;
		}

		@Override
		public boolean hasNext() {
			return rs.hasNext();
		}

		@Override
		public SubjectTable next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			// qs now has an answer
			subj = rs.next().getResource("s");
			SoftReference<SubjectTable> stbl = map.get(subj.asNode());
			if (stbl != null) {
				SubjectTable tbl = stbl.get();
				if (tbl != null) {
					return tbl;
				}
			}

			TableLoader loader = new TableLoader(subj.asNode());
			System.out.println(loader.getTable());
			return loader.getTable();
		}

	}

	private class TableLoader implements Runnable {
		private SubjectTableImpl subjectTable;
		private Model model;
		private Queue<Triple> queue;

		private TableLoader(Node subject) {
			LOG.debug("Building table for: " + subject);
			queue = new LinkedList<Triple>();
			model = ModelFactory.createDefaultModel();
			Resource r;
			SelectBuilder sb = new SelectBuilder().addVar(P).addVar(O);
			if (subject.isBlank()) {
				r = model.createResource(new AnonId(subject.getBlankNodeId()));
				ExprFactory exprF = sb.getExprFactory();
				sb.addWhere(S, P, O).addVar(S).addFilter(exprF.isBlank(S));
			} else {
				r = model.createResource(subject.getURI());
				sb.addWhere(subject, P, O);
			}
			Iterator<QuerySolution> rs = entityManager.execute(sb.build()).execSelect();
			if (subject.isBlank()) {
				rs = WrappedIterator.create(rs).filterKeep(
						qs -> qs.getResource("s").getId().getBlankNodeId().equals(subject.getBlankNodeId()));
			}
			while (rs.hasNext()) {
				QuerySolution qs = rs.next();
				model.add(r, qs.getResource("p").as(Property.class), qs.get("o"));
				if (!subject.isBlank() && qs.get("o").isAnon()) {
					Triple t = new Triple(subject, qs.getResource("p").asNode(), qs.get("o").asNode());
					queue.add(t);
				}
			}

			subjectTable = new SubjectTableImpl(subject, model);
			map.put(subject, new SoftReference<SubjectTable>(subjectTable));
			if (!queue.isEmpty()) {
				// this should be in a thread
				run();
			}
		}

		public SubjectTable getTable() {
			return subjectTable;
		}

		@Override
		public void run() {
			SelectBuilder sb = null;
			Stack<Triple> stack = new Stack<Triple>();
			while (!queue.isEmpty()) {
				stack.clear();
				stack.push(queue.poll());
				process(stack);
				stack.pop();
				if (!stack.isEmpty()) {
					throw new IllegalStateException("Stack was not clear");
				}
			}
		}

		private void process(Stack<Triple> triples) {
			Triple t = triples.peek();
			int end = triples.size() - 1;
			if (!map.containsKey(t.getObject())) {
				SelectBuilder sb = new SelectBuilder().addVar(S).addVar(P).addVar(O).addWhere(S, P, O);

				Path p = null;
				Triple t2;
				for (int i = 0; i < triples.size(); i++) {
					t2 = triples.get(i);
					Path p2 = PathFactory.pathLink(t2.getPredicate());
					p = (p == null) ? p2 : PathFactory.pathSeq(p, p2);
				}
				t2 = triples.firstElement();
				sb.addWhere(t2.getSubject(), p, S);

				ResultSet rs = entityManager.execute(sb.build()).execSelect();
				while (rs.hasNext()) {
					QuerySolution qs = rs.next();
					model.add(qs.getResource("s"), qs.getResource("p").as(Property.class), qs.get("o"));
					map.put(qs.getResource("s").asNode(), new SoftReference<SubjectTable>(subjectTable));
					if (qs.get("o").isAnon()) {
						Triple t3 = new Triple(qs.getResource("s").asNode(), qs.getResource("p").asNode(),
								qs.get("o").asNode());
						triples.push(t3);
						process(triples);
						triples.pop();
					}
				}
			}

		}
	}
}
