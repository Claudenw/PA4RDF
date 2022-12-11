package org.xenei.jena.entities.impl.method;

import java.lang.reflect.Method;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.xenei.jena.entities.EffectivePredicate;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.exceptions.MissingAnnotationException;
import org.xenei.jena.entities.impl.Action;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.ClassUtils;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.TypeChecker;

public class AbstractMethodParser extends BaseMethodParser {

    AbstractMethodParser(final BaseMethodParser methodParser) {
        super( methodParser );
    }

    /**
     * parses Predicate annotations and abstract methods.
     *
     * @param action
     *            The action of the method.
     * @param method
     *            The method that is being processed
     * @param addCount
     *            The count of add methods.
     * @throws MissingAnnotationException
     *             If an Predicate annotation is missing.
     */
    void parse(final Action action, final EffectivePredicate predicate)
            throws MissingAnnotationException {

        // process "set" if only one arg and not varargs
        switch (action.actionType) {
        case SETTER:
            final Integer i = getAddCount( action.method );
            if (i != null) {
                parseSetter( action, predicate, (i > 1) );
            }
            break;

        case EXISTENTIAL:
            if (action.method.getParameterTypes().length <= 1) {
                parseExistential( action, predicate );
            }
            break;

        case GETTER:
            if (action.method.getParameterTypes().length == 0) {
                parseGetter( action, predicate );
            }
            break;

        case REMOVER:
            parseRemover( action, predicate );
            break;

        }
    }

    private void parseExistential(final Action action, final EffectivePredicate superPredicate) {
        // we only parse boolean results
        if (TypeChecker.canBeSetFrom( Boolean.class, action.method.getReturnType() ) && (action.method.getParameterCount() <= 1)) {
            final EffectivePredicate predicate = new EffectivePredicate( action.method ).merge( superPredicate );
            subjectInfo.add( action.method, new PredicateInfoImpl( predicate, action ) );
        }
    }

    private void parseGetter(final Action action, final EffectivePredicate superPredicate)
            throws MissingAnnotationException {
        // predicate for getter method includes predicate info for setter
        // method.
        String actionName = action.name();
        final EffectivePredicate predicate = new EffectivePredicate( action.method ).merge( superPredicate );
        // ExtendedIterator or Collection return type
        if (PredicateInfoImpl.isCollection( action.method.getReturnType() )) {
            if (!isMultiAdd( actionName )) {
                // returning a collection and we have a single add method.
                // so set the return type if not set.
                if (predicate.isTypeNotSet()) {
                    for (final Method setterMethod : getSetterMethods( actionName )) {
                        // we have a setter method so lets merge info

                        final EffectivePredicate ep = new EffectivePredicate( setterMethod );
                        predicate.merge( ep );
                        final PredicateInfo setterPI = parse( setterMethod, ep );
                        if (setterPI == null) {
                            throw new IllegalStateException( "Could not parse setter " + setterMethod.getName() );
                        }
                        if (predicate.isTypeNotSet()) {
                            predicate.setType( setterPI.getArgumentType() );
                        } else {
                            if (predicate.type() == URI.class) {
                                predicate.setType( RDFNode.class );
                            }
                        }
                    }
                }
            } else {
                // returning a collection and multple add methods
                // so we must have a type set.
                if (predicate.isTypeNotSet()) {
                    throw new MissingAnnotationException( String.format( "%s.%s must have a Predicate annotation to define type",
                            subjectInfo.getImplementedClass(), action.method.getName() ) );
                }
            }
        } else {
            // returning a single type so set the type to the return type.
            if (predicate.isTypeNotSet()) {
                predicate.setType( action.method.getReturnType() );
            }
        }
        final PredicateInfo pi = new PredicateInfoImpl( predicate, action);
        subjectInfo.add( action.method, pi );
        processAssociatedMethods( pi, action);
    }

    /**
     * Processes "setX" and "addX" functions.
     *
     * @param subjectInfo
     * @param method
     * @param multiAdd
     * @throws MissingAnnotationException
     */
    private void parseSetter(Action action, final EffectivePredicate superPredicate, final boolean multiAdd)
            throws MissingAnnotationException {

        final Class<?> parms[] = action.method.getParameterTypes();
        if (parms.length == 1) {
            final EffectivePredicate predicate = new EffectivePredicate( action.method ).merge( superPredicate );
            final PredicateInfoImpl pi = new PredicateInfoImpl( predicate, action );
            subjectInfo.add( action.method, pi );

            // predicate.setType( null );
            predicate.setLiteralType( "" );

            if (multiAdd) {
                processAssociatedSetters( pi, action, predicate );
            }
            processAssociatedMethods( pi, action );
        }
    }

    private void processAssociatedMethods(final PredicateInfo pi, final Action action) {
        action.getAssociatedActions()
        .forEach( a -> {
            try {
                parse( a.method, pi.getPredicate());
            } catch (MissingAnnotationException e) {
                log.error( a.method.toString()+" missing required annotation", e );
            }
        });
        
        /*
        final Class<?> context = method.getDeclaringClass();
        final EffectivePredicate predicate = pi.getPredicate();
        
        parseAssociatedMethod( context, "get" + subName, predicate, null );
        parseAssociatedMethod( context, "is" + subName, predicate, null );
        if (actionType.isMultiple( method )) {
            parseAssociatedMethod( context, "add" + subName, predicate, pi.getValueType() );
            parseAssociatedMethod( context, "has" + subName, predicate, pi.getValueType() );
            parseAssociatedMethod( context, "remove" + subName, predicate, pi.getValueType() );
            if (pi.getValueType() == RDFNode.class) {
                // look for @URI annotated strings
                try {
                    final Method m2 = method.getDeclaringClass().getMethod( "has" + subName, String.class );
                    if (hasURIParameter( m2 )) {
                        parseAssociatedMethod( context, "has" + subName, predicate, String.class );
                    }
                } catch (final NoSuchMethodException acceptable) {
                    // do nothing
                }
                try {
                    final Method m2 = method.getDeclaringClass().getMethod( "remove" + subName, String.class );
                    if (hasURIParameter( m2 )) {
                        parseAssociatedMethod( context, "remove" + subName, predicate, String.class );
                    }
                } catch (final NoSuchMethodException acceptable) {
                    // do nothing
                }
            }
        } else {
            parseAssociatedMethod( context, "set" + subName, predicate, pi.getValueType() );
            parseAssociatedMethod( context, "has" + subName, predicate, void.class );
            parseAssociatedMethod( context, "remove" + subName, predicate, null );
        }
        */
    }
}
