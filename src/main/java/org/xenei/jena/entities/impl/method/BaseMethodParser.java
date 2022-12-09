package org.xenei.jena.entities.impl.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.jena.rdf.model.RDFNode;
import org.xenei.jena.entities.EffectivePredicate;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.SubjectInfoImpl;
import org.xenei.jena.entities.impl.handlers.UriHandler;

public abstract class BaseMethodParser {
    private final Map<String, Integer> addCount;
    protected final SubjectInfoImpl subjectInfo;
    private final Stack<Method> parseStack;
    
    private AbstractMethodParser abstractMethodParser;
    private ImplMethodParser implMethodParser;

    protected BaseMethodParser(final Stack<Method> parseStack, final SubjectInfoImpl subjectInfo,
            final Map<String, Integer> addCount) {
        this.parseStack = parseStack;
        this.subjectInfo = subjectInfo;
        this.addCount = addCount;
    }

    BaseMethodParser(final BaseMethodParser other) {
        this( other.parseStack, other.subjectInfo, other.addCount );
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
     * @throws MissingAnnotation
     *             if the Method does not have an annotation and needs one.
     * @throws NotAbstract
     *             if the Method is not abstract.
     */
    public PredicateInfo parse(final Method method) throws MissingAnnotation {
        return parse( method, null );
    }

    PredicateInfo parse(final Method method, final EffectivePredicate predicate) throws MissingAnnotation {
        PredicateInfo pi = subjectInfo.getPredicateInfo( method );

        // only process if we havn't yet.
        if (pi == null) {
            // only process abstract methods and does not have var args
            if (shouldProcess( method )) {
                // only process if we are not already processing
                if (!parseStack.contains( method )) {
                    parseStack.push( method );

                    try {
                        final ActionType actionType = ActionType.parse( method.getName() );
                        final EffectivePredicate ep = new EffectivePredicate( method ).merge( predicate );
                        if (Modifier.isAbstract( method.getModifiers() )) {
                            asAbstractMethodParser().parse( actionType, method, ep );
                        } else {
                            ep.merge( method.getAnnotation( Predicate.class ) );
                            if (ep.impl()) {
                                asImplMethodParser().parse( actionType, method, ep );
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
            abstractMethodParser = new AbstractMethodParser(this);
        }
        return abstractMethodParser;
    }
    
    private ImplMethodParser asImplMethodParser() {
        if (implMethodParser == null) {
            implMethodParser = new ImplMethodParser(this);
        }
        return implMethodParser;
    }

    public Integer getAddCount(final Method method) {
        return addCount.get( method.getName() );
    }

    protected boolean isMultiAdd(final String nameSuffix) {
        return ActionType.SETTER.createNames( nameSuffix ).anyMatch( n -> addCount.get( n ) != null );
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

    public void parseRemover(final Method method, final EffectivePredicate predicate) {
        final EffectivePredicate ep = new EffectivePredicate( method ).merge( predicate );
        Class<?> valueClass = null;
        if (method.getParameterTypes().length == 1) {
            valueClass = method.getParameterTypes()[0];
            if (hasURIParameter( method )) {
                ep.setType( URI.class );
            }
        } else {
            ep.setType( null );
        }
        final PredicateInfoImpl pii = new PredicateInfoImpl( ep, method.getName(), valueClass );
        subjectInfo.add( pii );
    }

    protected void processAssociatedSetters(final PredicateInfoImpl pi, final Method m,
            final EffectivePredicate predicate) throws MissingAnnotation {
        try {
            if ((pi.getValueClass() == String.class) && (pi.getObjectHandler().getClass() == UriHandler.class)) {
                parse( m.getDeclaringClass().getMethod( pi.getMethodName(), RDFNode.class ), predicate );

            }
            if (pi.getValueClass() == RDFNode.class) {

                final Method m2 = m.getDeclaringClass().getMethod( pi.getMethodName(), String.class );
                if (hasURIParameter( m2 )) {
                    parse( m2, predicate );
                }

            }
        } catch (NoSuchMethodException | SecurityException e) {
            // ignore
        }
    }

}
