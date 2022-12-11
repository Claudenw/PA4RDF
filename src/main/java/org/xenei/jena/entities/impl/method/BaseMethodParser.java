package org.xenei.jena.entities.impl.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.WrappedIterator;
import org.slf4j.Logger;
import org.xenei.jena.entities.EffectivePredicate;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.exceptions.MissingAnnotationException;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.SubjectInfoImpl;
import org.xenei.jena.entities.impl.handlers.UriHandler;

public abstract class BaseMethodParser {
    protected final Logger log ;
    private final Map<String, Integer> addCount;
    protected final SubjectInfoImpl subjectInfo;
    private final Stack<Method> parseStack;

    private AbstractMethodParser abstractMethodParser;
    private ImplMethodParser implMethodParser;
    
    private final Function<Method,Action> actionMap = new Function<>() {

        @Override
        public Action apply(Method method) {
            try {
                return new Action(method);
                
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        
    };

    protected BaseMethodParser(final Stack<Method> parseStack, final SubjectInfoImpl subjectInfo,
            final Map<String, Integer> addCount, Logger log) {
        this.parseStack = parseStack;
        this.subjectInfo = subjectInfo;
        this.addCount = addCount;
        this.log = log;
    }

    BaseMethodParser(final BaseMethodParser other) {
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

    PredicateInfo parse(final Method method, final EffectivePredicate predicate) throws MissingAnnotationException {
        PredicateInfo pi = subjectInfo.getPredicateInfo( method );

        // only process if we havn't yet.
        if (pi == null) {
            // only process abstract methods and does not have var args
            if (shouldProcess( method )) {
                // only process if we are not already processing
                if (!parseStack.contains( method )) {
                    parseStack.push( method );

                    try {
                        final Action action = new Action( method );
                        final EffectivePredicate ep = new EffectivePredicate( method ).merge( predicate );
                        if (Modifier.isAbstract( method.getModifiers() )) {
                            asAbstractMethodParser().parse( action, ep );
                        } else {
                            ep.merge( method.getAnnotation( Predicate.class ) );
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

    public Integer getAddCount(final Method method) {
        return addCount.get( method.getName() );
    }

    protected boolean isMultiAdd(final String nameSuffix) {
        return WrappedIterator.create( ActionType.SETTER.createNames( nameSuffix ) )
                .filterKeep( n -> addCount.get( n ) != null )
                .hasNext();
    }

    protected List<Method> getSetterMethods(final String nameSuffix) {
        // find the setter
        return Stream.of( subjectInfo.getImplementedClass().getMethods() )
                .filter( m -> ActionType.SETTER.isA( m.getName() ) ).collect( Collectors.toList() );
    }

    protected boolean hasURIParameter(final Method m) {
        if (m.getParameterAnnotations() != null) {
            for (final Annotation a : m.getParameterAnnotations()[0]) {
                if (a instanceof URI) {
                    return true;
                }
            }
        }
        return false;
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

    public void parseRemover(final Action action, final EffectivePredicate predicate) {
        final EffectivePredicate ep = new EffectivePredicate( action.method ).merge( predicate );
        subjectInfo.add( action.method, new PredicateInfoImpl( ep, action.method ) );
    }

    protected void processAssociatedSetters(final PredicateInfoImpl pi, final Action action,
            final EffectivePredicate predicate) throws MissingAnnotationException {
        try {
            if ((pi.getArgumentType() == String.class) && (pi.getObjectHandler().getClass() == UriHandler.class)) {
                parse( action.method.getDeclaringClass().getMethod( pi.getMethodName(), RDFNode.class ), predicate );
            }
            if (pi.getArgumentType() == RDFNode.class) {

                final Method m2 = action.method.getDeclaringClass().getMethod( pi.getMethodName(), String.class );
                if (hasURIParameter( m2 )) {
                    parse( m2, predicate );
                }

            }
        } catch (NoSuchMethodException | SecurityException e) {
            // ignore
        }
    }
    
    class Action {
        final ActionType actionType;
        final boolean isMultiple;
        final Method method;
        
        
        private boolean deriveMultiple(final ActionType actionType, final Method method) {
            switch (actionType) {
            case GETTER:
                return Iterator.class.isAssignableFrom( method.getReturnType() )
                        || Collection.class.isAssignableFrom( method.getReturnType() )
                        || method.getReturnType().isArray();

            case SETTER:
                return method.getName().startsWith( "add" );

            case EXISTENTIAL:
            case REMOVER:
                return method.getParameterCount() > 0;
            }
            throw new IllegalStateException( String.format( "%s is not an ActionType", actionType ) );
        }

        Action(final Method method) {
            actionType = ActionType.parse( method.getName() );
            isMultiple = deriveMultiple( actionType, method );
            this.method = method;
        }

        public String name() {
            return actionType.extractName( method.getName() );
        }

        public Class<?> context() {
            return method.getDeclaringClass();
        }
        
        public ExtendedIterator<Action> getAssociatedActions() {
            Set<String> newNames = ActionType.allNames( name() ).toSet();
            return WrappedIterator.create( Arrays.asList( context().getMethods() ).iterator() )
                .filterKeep( m -> newNames.contains( m.getName() ) )
                .mapWith( actionMap )
                .filterDrop( a -> {return a==null;} )
                .filterDrop( a -> parseStack.contains( a.method ) )
                .filterDrop( a -> subjectInfo.getPredicateInfo( a.method ) != null)
                .filterDrop( a -> this.method.equals( a.method ) );
        }
    }
}
