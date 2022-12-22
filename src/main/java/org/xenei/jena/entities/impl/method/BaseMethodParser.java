package org.xenei.jena.entities.impl.method;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.jena.util.iterator.WrappedIterator;
import org.slf4j.Logger;
import org.xenei.jena.entities.EffectivePredicate;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.exceptions.MissingAnnotationException;
import org.xenei.jena.entities.impl.Action;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.SubjectInfoImpl;

public abstract class BaseMethodParser {
    protected final Logger log;
    private final Map<String, Integer> addCount;
    protected final SubjectInfoImpl subjectInfo;
    final Stack<Method> parseStack;

    private AbstractMethodParser abstractMethodParser;
    private ImplMethodParser implMethodParser;

    protected BaseMethodParser(final Stack<Method> parseStack, final SubjectInfoImpl subjectInfo,
            final Map<String, Integer> addCount, final Logger log) {
        this.parseStack = parseStack;
        this.subjectInfo = subjectInfo;
        this.addCount = addCount;
        this.log = log;
    }

    protected BaseMethodParser(final BaseMethodParser other) {
        this( other.parseStack, other.subjectInfo, other.addCount, other.log );
    }

    /**
     * Parse the method if necessary.
     *
     * The first time the method is seen it is parsed, after that a cached
     * version is returned.
     *
     * @param method
     *            The method to parse
     * @return the PredicateInfo for the class.
     * @throws MissingAnnotationException
     *             if the Method does not have an annotation and needs one.
     * @throws NotAbstract
     *             if the Method is not abstract.
     */
    public PredicateInfo parse(final Method method) throws MissingAnnotationException {
        return parse( method, null );
    }

    protected PredicateInfo parse(final Method method, final EffectivePredicate predicate)
            throws MissingAnnotationException {
        PredicateInfo pi = subjectInfo.getPredicateInfo( method );

        // only process if we havn't yet.
        if (pi == null) {
            // only process abstract methods and does not have var args
            if (shouldProcess( method )) {
                // only process if we are not already processing
                if (!parseStack.contains( method )) {
                    parseStack.push( method );
                    log.debug( "parsing {}", method );
                    try {
                        final Action action = new Action( method );
                        final EffectivePredicate ep = new EffectivePredicate( method ).merge( predicate );
                        if (Modifier.isAbstract( method.getModifiers() )) {
                            asAbstractMethodParser().parse( action, ep );
                        } else {
                            if (ep.impl()) {
                                asImplMethodParser().parse( action, ep );
                            }
                        }
                        pi = subjectInfo.getPredicateInfo( method );
                    } catch (final IllegalArgumentException e) {
                        // expected when method is not an action method.
                    } finally {
                        final Method m2 = parseStack.pop();
                        if (method != m2) {
                            throw new IllegalStateException(
                                    String.format( "Parsing stack out of sync: expected %s got %s", method, m2 ) );
                        }
                    }
                }
            }
        }
        return pi;
    }

    private AbstractMethodParser asAbstractMethodParser() {
        if (abstractMethodParser == null) {
            abstractMethodParser = new AbstractMethodParser( this );
        }
        return abstractMethodParser;
    }

    private ImplMethodParser asImplMethodParser() {
        if (implMethodParser == null) {
            implMethodParser = new ImplMethodParser( this );
        }
        return implMethodParser;
    }

    protected boolean isMultiAdd(final String nameSuffix) {
        return WrappedIterator.create( ActionType.SETTER.createNames( nameSuffix ) ).filterKeep( n -> {
            final Integer i = addCount.get( n );
            return (i != null) && (i > 1);
        } ).hasNext();
    }

    protected List<Method> getSetterMethods(final String nameSuffix) {
        return Stream.of( subjectInfo.getImplementedClass().getMethods() )
                .filter( m -> ActionType.SETTER.isA( m.getName() ) )
                .filter( m -> nameSuffix.equals( ActionType.SETTER.extractName( m.getName() ) ) )
                .collect( Collectors.toList() );
    }

    /**
     * Only process abstract methods or methods with Predicate annotations and
     * only when either of those is not varArgs.
     *
     * @param m
     * @return
     */
    private boolean shouldProcess(final Method m) {
        boolean retval = !m.isVarArgs();
        if (retval) {
            retval = Modifier.isAbstract( m.getModifiers() ) || (m.getAnnotation( Predicate.class ) != null);
        }
        return retval;
    }

    protected void parseRemover(final Action action, final EffectivePredicate predicate) {
        subjectInfo.add( action.method, new PredicateInfoImpl( predicate, action ) );
    }

    protected void processAssociatedMethods(final PredicateInfo pi, final Action action) {
        java.util.function.Predicate<Method> p = m -> parseStack.contains( m );
        p = p.or( m -> subjectInfo.getPredicateInfo( m ) != null );
        action.getAssociatedActions( p ).forEach( a -> {
            try {
                parse( a.method, pi.getPredicate() );
            } catch (final MissingAnnotationException e) {
                log.error( a.method.toString() + " missing required annotation", e );
            }
        } );
    }
}
