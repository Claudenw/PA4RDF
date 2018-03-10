package org.xenei.jena.reasoner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.graph.Capabilities;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.BaseInfGraph;
import org.apache.jena.reasoner.Finder;
import org.apache.jena.reasoner.InfGraph;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerException;
import org.apache.jena.reasoner.TriplePattern;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.NiceIterator;
import org.xenei.jena.entities.EntityManager;
import org.xenei.pa4rdf.bean.PredicateInfo;
import org.xenei.pa4rdf.bean.SubjectInfo;
import org.xenei.pa4rdf.bean.impl.ActionType;

/**
 * A reasoner that attempts to perform the same duplicate checking that the
 * Entity manager performs when adding values for properties.
 *
 */
public class EntityManagerReasoner implements Reasoner, EntityManager.Listener {
    private static final Log LOG = LogFactory.getLog( EntityManagerReasoner.class );

    private enum MethodType {
        SINGLE, MULTI, CONFLICT
    };

    private final Map<Node, MethodType> map = Collections.synchronizedMap( new HashMap<Node, MethodType>() );

    /**
     * Constructor
     * 
     * @param em
     *            The entity manager to use.
     */
    public EntityManagerReasoner(EntityManager em) {
        em.registerListener( this );
        for (final SubjectInfo si : em.getSubjects()) {
            processSubjectInfo( si );
        }

    }

    private void processSubjectInfo(SubjectInfo si) {
        for (final PredicateInfo pi : si.getPredicates().stream()
                .filter( p -> p.getActionType().equals( ActionType.SETTER ) ).collect( Collectors.toList() )) {
            final MethodType typ = map.get( pi.getProperty().asNode() );
            if (pi.getMethodName().startsWith( "set" )) {
                if (typ != null) {
                    if (!typ.equals( MethodType.SINGLE )) {
                        map.put( pi.getProperty().asNode(), MethodType.CONFLICT );
                        LOG.info(
                                String.format( "Conflict detected for %s while processing %s", pi.getProperty(), pi ) );
                    }
                } else {
                    map.put( pi.getProperty().asNode(), MethodType.SINGLE );
                }
            } else {
                if (typ != null) {
                    if (!typ.equals( MethodType.MULTI )) {
                        map.put( pi.getProperty().asNode(), MethodType.CONFLICT );
                        LOG.info(
                                String.format( "Conflict detected for %s while processing %s", pi.getProperty(), pi ) );
                    }
                } else {
                    map.put( pi.getProperty().asNode(), MethodType.MULTI );
                }
            }
        }

    }

    @Override
    public Reasoner bindSchema(Graph tbox) throws ReasonerException {
        return this;
    }

    @Override
    public Reasoner bindSchema(Model tbox) throws ReasonerException {
        return this;
    }

    @Override
    public InfGraph bind(Graph data) throws ReasonerException {
        return new BaseInfGraph( data, this ) {

            @Override
            public ExtendedIterator<Triple> findWithContinuation(TriplePattern pattern, Finder continuation) {
                if (continuation == null) {
                    return NiceIterator.emptyIterator();
                }
                return continuation.find( pattern );
            }

            @Override
            public Graph getSchemaGraph() {
                return Graph.emptyGraph;
            }

            @Override
            public synchronized void performAdd(Triple t) {
                final Node n = t.getPredicate();
                final MethodType mt = map.get( n );
                if (mt != null && mt.equals( MethodType.SINGLE )) {
                    super.remove( t.getSubject(), t.getPredicate(), Node.ANY );
                }
                super.performAdd( t );
            }
        };
    }

    @Override
    public void setDerivationLogging(boolean logOn) {
        // does nothing
    }

    @Override
    public void setParameter(Property parameterUri, Object value) {
        // does nothing

    }

    @Override
    public Model getReasonerCapabilities() {
        return ModelFactory.createModelForGraph( Graph.emptyGraph );
    }

    @Override
    public void addDescription(Model configSpec, Resource base) {
        // does nothing

    }

    @Override
    public boolean supportsProperty(Property property) {
        return false;
    }

    @Override
    public Capabilities getGraphCapabilities() {
        return Graph.emptyGraph.getCapabilities();
    }

    @Override
    public void onParseClass(SubjectInfo info) {
        processSubjectInfo( info );
    }

}
