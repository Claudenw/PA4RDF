package org.xenei.jena.entities.cache;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.arq.querybuilder.ExprFactory;
import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.graph.Capabilities;
import org.apache.jena.graph.FrontsNode;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.GraphEvents;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.TransactionHandler;
import org.apache.jena.graph.Triple;
import org.apache.jena.graph.impl.AllCapabilities;
import org.apache.jena.graph.impl.GraphBase;
import org.apache.jena.graph.impl.SimpleEventManager;
import org.apache.jena.graph.impl.TransactionHandlerBase;
import org.apache.jena.query.ARQ;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.graph.GraphFactory;
import org.apache.jena.sparql.path.Path;
import org.apache.jena.sparql.path.PathFactory;
import org.apache.jena.util.iterator.ClosableIterator;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.IteratorIterator;
import org.apache.jena.util.iterator.NiceIterator;
import org.apache.jena.util.iterator.WrappedIterator;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.jena.entities.impl.TransactionHolder;

/**
 * A graph that caches remote data.
 * <p>
 * Rhis implementation caches predicates and objects for each subject that is
 * retrieved from the remote system.
 * </p>
 * <p>
 * <b> This is not a full Graph implementation.</b> Specifically triples are
 * restricted to the values available in a Model Statement.
 * </p>
 */
public class CachingGraph extends GraphBase implements Graph {
    private final static Log LOG = LogFactory.getLog( CachingGraph.class );
    private final Map<Node, SoftReference<SubjectTable>> map;
    private final EntityManagerImpl entityManager;
    private final Node graphNode;
    private final Node graphWriteNode;
    private static final Var S = Var.alloc( "s" );
    private static final Var P = Var.alloc( "p" );
    private static final Var O = Var.alloc( "o" );
    private final UpdateByDiff updater;
    private final TxnHandler txnHandler;

    static {
        ARQ.getContext().set( ARQ.inputGraphBNodeLabels, true );
    }

    /**
     * Constructor.
     * 
     * @param connection
     *            the connection to the remote system.
     */
    public CachingGraph(EntityManagerImpl entityManager) {
        this.entityManager = entityManager;
        this.graphNode = entityManager.getModelName();
        this.graphWriteNode = (graphNode.equals( Quad.unionGraph )) ? Quad.defaultGraphIRI : graphNode;
        this.map = Collections.synchronizedMap( new HashMap<Node, SoftReference<SubjectTable>>() );
        this.updater = new UpdateByDiff();
        this.txnHandler = new TxnHandler();
        this.gem = new EventManager();
    }

    @Override
    public TransactionHandler getTransactionHandler() {
        return txnHandler;
    }

    @Override
    public Capabilities getCapabilities() {
        {
            if (capabilities == null) {
                capabilities = new AllCapabilities() {

                    @Override
                    public boolean sizeAccurate() {
                        return false;
                    }
                };
            }
            return capabilities;
        }
    }

    /**
     * Clears the cache and resets to starting condition
     */
    public void reset() {
        map.clear();
        updater.clear();
    }

    /**
     * Sync the caching graph with the remote graph. predicates and objects for
     * every subject is read from the remote graph. Local changes are discarded.
     */
    public void sync(boolean singleThreaded) {
        final List<Node> changed = updater.sync( entityManager, graphWriteNode );
        if (!changed.isEmpty()) {

            final Runnable r = new Runnable() {

                @Override
                public void run() {
                    for (final Node subject : changed) {
                        final SoftReference<SubjectTable> sr = map.get( subject );
                        final SubjectTable st = sr == null ? null : sr.get();
                        if (st == null) {
                            map.remove( subject );
                        } else {
                            if (st.getSubject().equals( subject )) {
                                new TableLoader( subject );
                            }
                        }
                    }

                }
            };

            // if (singleThreaded)
            // {
            r.run();
            // } else
            // {
            //
            // final Thread t = new Thread(r,
            // "sync - " + System.currentTimeMillis());
            //
            // t.start();
            // }
        }

    }

    /**
     * Get the SubjectTable for the specified subject.
     * 
     * @param subject
     *            the subject to get the subject for.
     * 
     *            If the subject table is already loaded it is returned
     *            otherwise it is created by reading from the remote system.
     * 
     * @return the subject table.
     */
    public SubjectTable getTable(Node subject) {
        final SoftReference<SubjectTable> tblRef = map.get( subject );
        SubjectTable tbl = null;
        if (tblRef != null) {
            tbl = tblRef.get();
        }
        if (tbl == null || tbl.isEmpty()) {
            tbl = new TableLoader( subject ).getTable();
        }
        return tbl;
    }

    @Override
    protected ExtendedIterator<Triple> graphBaseFind(Triple triplePattern) {
        if (triplePattern.getSubject().isConcrete()) {
            // SoftReference<SubjectTable> tblRef =
            // map.get(triplePattern.getSubject());
            final SubjectTable tbl = getTable( triplePattern.getSubject() );

            final Graph graph = tbl.asGraph();
            return WrappedIterator.create( new CachingGraphIterator( graph.find( triplePattern ), tbl ) );
        } else {
            /*
             * we need to pull in all the subjects from the parent graph and add
             * them to our graph as we find them.
             * 
             * If we find one we already have we keep our copy and return it. We
             * do this because we may have deleted items that have not yet been
             * deleted in the remote system.
             */
            final BlockIterator blockIterator = new BlockIterator( triplePattern );

            final Iterator<Iterator<Triple>> iter = WrappedIterator.create( blockIterator )
                    .mapWith( tbl -> new CachingGraphIterator( tbl.asGraph().find( triplePattern ), tbl ) );
            return WrappedIterator.create( new IteratorIterator( iter ) );
        }
    }

    @Override
    public void performAdd(Triple t) {
        if (!t.isConcrete()) {
            return;
        }
        final SoftReference<SubjectTable> tblRef = map.get( t.getSubject() );
        SubjectTable tbl = null;
        if (tblRef != null) {
            tbl = tblRef.get();
        }
        if (tbl == null) {
            tbl = new TableLoader( t.getSubject() ).getTable();
        }
        updater.register( tbl );
        tbl.addValue( t.getPredicate(), t.getObject() );
    }

    @Override
    public void performDelete(Triple t) {
        if (!t.isConcrete()) {
            return;
        }
        final SoftReference<SubjectTable> tblRef = map.get( t.getSubject() );
        SubjectTable tbl = null;
        if (tblRef != null) {
            tbl = tblRef.get();
        }
        if (tbl == null) {
            tbl = new TableLoader( t.getSubject() ).getTable();
        }
        updater.register( tbl );
        tbl.removeValue( t.getPredicate(), t.getObject() );
        if (tbl.isEmpty()) {
            map.remove( t.getSubject() );
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
            return getValues( predicate.asNode() );
        }

        public Set<Node> getValues(Node predicate) {
            if (isEmpty()) {
                return Collections.emptySet();
            }
            return Collections.unmodifiableSet(
                    subGraph.find( subject, predicate, Node.ANY ).mapWith( tpl -> tpl.getObject() ).toSet() );
        }

        @Override
        public void addValue(FrontsNode predicate, FrontsNode value) {
            addValue( predicate.asNode(), value.asNode() );
        }

        @Override
        public void addValue(Node predicate, Node value) {
            subGraph.add( new Triple( subject, predicate, value ) );
        }

        @Override
        public void removeValue(FrontsNode predicate, FrontsNode value) {
            removeValue( predicate.asNode(), value.asNode() );
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
            if (isEmpty()) {
                return Collections.emptySet();
            }
            return Collections.unmodifiableSet(
                    subGraph.find( subject, Node.ANY, value ).mapWith( tpl -> tpl.getPredicate() ).toSet() );
        }

        @Override
        public Graph asGraph() {
            return subGraph;
        }

        @Override
        public Graph snapshot() {
            final Graph g = GraphFactory.createDefaultGraph();
            subGraph.find().forEachRemaining( t -> g.add( t ) );
            return g;
        }

        @Override
        public void reset(Graph graph) {
            subGraph.clear();
            graph.find().forEachRemaining( t -> subGraph.add( t ) );
        }

        @Override
        public boolean has(FrontsNode predicate, FrontsNode value) {
            return has( predicate.asNode(), value.asNode() );
        }

        @Override
        public boolean has(Node predicate, Node value) {
            if (isEmpty()) {
                return false;
            }
            return subGraph.find( subject, predicate, value ).hasNext();
        }

        @Override
        public int size() {
            return subGraph.size();
        }

        @Override
        public String toString() {
            return String.format( "SubjectTable[%s %s]", subject, subGraph );
        }
    }

    private class BlockIterator implements Iterator<SubjectTable> {

        private final ResultSet rs;
        private Resource subj;

        private BlockIterator(Triple triplePattern) {
            final SelectBuilder sb = getSelectQuery( triplePattern );

            /*
             * we need to pull in all the subjects from the parent graph and add
             * them to our graph as we find them.
             * 
             * If we find one we already have we keep our copy and return it. We
             * do this because we may have deleted items that have not yet been
             * deleted in the remote system.
             */
            rs = entityManager.execute( sb.build() ).execSelect();
            subj = null;
        }

        private SelectBuilder getSelectQuery(Triple triplePattern) {
            final SelectBuilder sb = new SelectBuilder().setDistinct( true ).addWhere( S, P, O ).addOrderBy( S );

            if (triplePattern.equals( Triple.ANY )) {
                sb.addVar( S );
            } else {
                final Object s = triplePattern.getSubject().isConcrete() ? triplePattern.getSubject() : S;
                final Object p = triplePattern.getPredicate().isConcrete() ? triplePattern.getPredicate()
                        : Var.alloc( "p2" );
                final Object o = triplePattern.getObject().isConcrete() ? triplePattern.getObject() : Var.alloc( "o2" );
                sb.addWhere( s, p, o );
                if (!s.equals( S )) {
                    final ExprFactory exprF = sb.getExprFactory();
                    sb.addVar( exprF.asExpr( s ), S );
                } else {
                    sb.addVar( S );
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
            subj = rs.next().getResource( "s" );
            final SoftReference<SubjectTable> stbl = map.get( subj.asNode() );
            if (stbl != null) {
                final SubjectTable tbl = stbl.get();
                if (tbl != null) {
                    return tbl;
                }
            }

            final TableLoader loader = new TableLoader( subj.asNode() );
            return loader.getTable();
        }

    }

    private class TableLoader implements Runnable {
        private final SubjectTableImpl subjectTable;
        private final Model model;
        private final Queue<Triple> queue;

        private TableLoader(Node subject) {
            LOG.debug( "Building table for: " + subject );
            queue = new LinkedList<Triple>();
            model = ModelFactory.createDefaultModel();
            final SelectBuilder sb = new SelectBuilder().addVar( S ).addVar( P ).addVar( O );
            if (subject.isBlank()) {
                final ExprFactory exprF = sb.getExprFactory();
                sb.addWhere( S, P, O ).addFilter( exprF.isBlank( S ) );
            } else {
                sb.addWhere( subject, P, O ).addBind( NodeValue.makeNode( subject ), S );
            }
            final TransactionHolder txn = new TransactionHolder( entityManager.getConnection(), ReadWrite.READ );
            try (QueryExecution qe = entityManager.execute( sb.build() )) {
                Iterator<QuerySolution> rs = qe.execSelect();

                if (subject.isBlank()) {
                    rs = WrappedIterator.create( rs ).filterKeep(
                            qs -> qs.getResource( "s" ).getId().getBlankNodeId().equals( subject.getBlankNodeId() ) );
                }
                while (rs.hasNext()) {
                    final QuerySolution qs = rs.next();
                    if (qs.getResource( "s" ) == null || qs.getResource( "p" ) == null || qs.get( "o" ) == null) {
                        System.out.println( "Found Null" );
                    }
                    model.add( qs.getResource( "s" ), qs.getResource( "p" ).as( Property.class ), qs.get( "o" ) );
                    if (!subject.isBlank() && qs.get( "o" ).isAnon()) {
                        final Triple t = new Triple( subject, qs.getResource( "p" ).asNode(), qs.get( "o" ).asNode() );
                        queue.add( t );
                    }
                }
            } finally {
                txn.end();
            }
            subjectTable = new SubjectTableImpl( subject, model );
            map.put( subject, new SoftReference<SubjectTable>( subjectTable ) );
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
            final Stack<Triple> stack = new Stack<Triple>();
            while (!queue.isEmpty()) {
                stack.clear();
                stack.push( queue.poll() );
                process( stack );
                stack.pop();
                if (!stack.isEmpty()) {
                    throw new IllegalStateException( "Stack was not clear" );
                }
            }
        }

        private void process(Stack<Triple> triples) {
            final Triple t = triples.peek();
            triples.size();
            if (!map.containsKey( t.getObject() )) {
                final SelectBuilder sb = new SelectBuilder().addVar( S ).addVar( P ).addVar( O ).addWhere( S, P, O );

                Path p = null;
                Triple t2;
                for (int i = 0; i < triples.size(); i++) {
                    t2 = triples.get( i );
                    final Path p2 = PathFactory.pathLink( t2.getPredicate() );
                    p = (p == null) ? p2 : PathFactory.pathSeq( p, p2 );
                }
                t2 = triples.firstElement();
                sb.addWhere( t2.getSubject(), p, S );
                final TransactionHolder txn = new TransactionHolder( entityManager.getConnection(), ReadWrite.READ );
                try {
                    final ResultSet rs = entityManager.execute( sb.build() ).execSelect();
                    while (rs.hasNext()) {
                        final QuerySolution qs = rs.next();
                        model.add( qs.getResource( "s" ), qs.getResource( "p" ).as( Property.class ), qs.get( "o" ) );
                        map.put( qs.getResource( "s" ).asNode(), new SoftReference<SubjectTable>( subjectTable ) );
                        if (qs.get( "o" ).isAnon()) {
                            final Triple t3 = new Triple( qs.getResource( "s" ).asNode(),
                                    qs.getResource( "p" ).asNode(), qs.get( "o" ).asNode() );
                            triples.push( t3 );
                            process( triples );
                            triples.pop();
                        }
                    }
                } finally {
                    txn.end();
                }
            }

        }
    }

    private class TxnHandler extends TransactionHandlerBase {

        @Override
        public boolean transactionsSupported() {
            return true;
        }

        @Override
        public void begin() {
        }

        @Override
        public void abort() {
            updater.rollback();
        }

        @Override
        public void commit() {
            sync( true );
        }

    }

    private class EventManager extends SimpleEventManager {

        @Override
        public void notifyEvent(Graph source, Object event) {
            /*
             * TODO add write watcher so that after N records are read we sync
             * the tables.
             */

            if (GraphEvents.finishRead.equals( event )) {
                sync( true );
            }

            super.notifyEvent( source, event );
        }

    }

    private class CachingGraphIterator implements ClosableIterator<Triple> {
        private final Iterator<Triple> delegate;
        private final SubjectTable tbl;
        private Triple last;

        public CachingGraphIterator(Iterator<Triple> delegate, SubjectTable tbl) {
            this.delegate = delegate;
            this.tbl = tbl;
            this.last = null;
        }

        @Override
        public void close() {
            NiceIterator.close( delegate );
        }

        @Override
        public void forEachRemaining(Consumer<? super Triple> action) {
            delegate.forEachRemaining( action );
        }

        @Override
        public boolean hasNext() {
            return delegate.hasNext();
        }

        @Override
        public Triple next() {
            last = delegate.next();
            return last;
        }

        @Override
        public void remove() {
            updater.register( tbl );
            delegate.remove();
            sync( true );
            getEventManager().notifyDeleteTriple( CachingGraph.this, last );
        }

    }
}
