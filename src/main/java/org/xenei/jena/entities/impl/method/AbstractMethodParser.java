package org.xenei.jena.entities.impl.method;

import java.lang.reflect.Method;
import java.util.Collection;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.xenei.jena.entities.EffectivePredicate;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.TypeChecker;

public class AbstractMethodParser extends BaseMethodParser {

    AbstractMethodParser(final BaseMethodParser methodParser) {
        super( methodParser );
    }

    /**
     * parses Predicate annotations and abstract methods.
     *
     * @param subjectInfo
     *            The subject Info to add data to.
     * @param method
     *            The method that is being processed
     * @param addCount
     *            The count of add methods.
     * @throws MissingAnnotation
     *             If an Predicate annotation is missing.
     */
    void parse(final ActionType actionType, final Method method, final EffectivePredicate predicate)
            throws MissingAnnotation {

        // process "set" if only one arg and not varargs
        switch (actionType) {
        case SETTER:
            final Integer i = getAddCount( method );
            if (i != null) {
                parseSetter( method, predicate, (i > 1) );
            }
            break;

        case EXISTENTIAL:
            if (method.getParameterTypes().length <= 1) {
                parseExistential( method, predicate );
            }
            break;

        case GETTER:
            if (method.getParameterTypes().length == 0) {
                final String nameSuffix = actionType.extractName( method.getName() );
                parseGetter( nameSuffix, method, predicate );
            }
            break;

        case REMOVER:
            parseRemover( method, predicate );
            break;

        }
    }

    /**
     * Adds getter methods from base class
     *
     * @param context
     * @param methodName
     * @param setterValueType
     * @param parentPredicate
     * @param multiAdd
     * @return
     * @throws MissingAnnotation
     */
    private void parseAssociatedMethod(Class<?> context, final String methodName, final EffectivePredicate parentPredicate,
            final Class<?> argClass) throws MissingAnnotation {
        try {
            final Method method = (argClass == null) ? context.getMethod( methodName )
                    : context.getMethod( methodName, argClass );
            parse( method, parentPredicate );
        } catch (final NoSuchMethodException e) {
            // do nothing
        }
    }

    private void parseExistential(final Method method, final EffectivePredicate superPredicate) {
        // we only parse boolean results
        if (TypeChecker.canBeSetFrom( Boolean.class, method.getReturnType() ) && (method.getParameterCount() <= 1)) {
            final EffectivePredicate predicate = new EffectivePredicate( method ).merge( superPredicate );

            //predicate.setConcreteType( method.getReturnType());
            if (method.getParameterCount() == 1) {
                predicate.setType( method.getParameterTypes()[0] );
            } else {
                predicate.setType( void.class );
            }

            PredicateInfoImpl pi =  new PredicateInfoImpl( predicate, method.getName(), predicate.type() );
            pi.setConcreteType(  method.getReturnType() );
            subjectInfo.add(pi );
        }
    }

    private void parseGetter(final String nameSuffix, final Method method, final EffectivePredicate superPredicate)
            throws MissingAnnotation {
        // predicate for getter method includes predicate info for setter
        // method.

        final EffectivePredicate predicate = new EffectivePredicate( method ).merge( superPredicate );
        // ExtendedIterator or Collection return type
        if (method.getReturnType().equals( ExtendedIterator.class )
                || Collection.class.isAssignableFrom( method.getReturnType() )) {
            if (!isMultiAdd( nameSuffix )) {
                // returning a collection and we have a single add method.
                // so set the return type if not set.
                if (predicate.isTypeNotSet()) {
                    for (final Method setterMethod : getSetterMethods( nameSuffix )) {
                        // we have a setter method so lets merge info

                        final EffectivePredicate ep = new EffectivePredicate( setterMethod );
                        predicate.merge( ep );
                        final PredicateInfo setterPI = parse( setterMethod, ep );
                        if (setterPI == null) {
                            throw new IllegalStateException( "Could not parse setter " + setterMethod.getName() );
                        }
                        if (predicate.isTypeNotSet()) {
                            predicate.setType( setterPI.getValueClass() );
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
                    throw new MissingAnnotation( String.format( "%s.%s must have a Predicate annotation to define type",
                            subjectInfo.getImplementedClass(), method.getName() ) );
                }
            }
        } else {
            // returning a single type so set the type to the return type.
            if (predicate.isTypeNotSet()) {
                predicate.setType( method.getReturnType() );
            }
        }
        subjectInfo.add( new PredicateInfoImpl( predicate, method.getName(), method.getReturnType() ) );
        processAssociatedMethods( ActionType.GETTER, predicate.rawType(), predicate, method );
    }

    /**
     * Processes "setX" and "addX" functions.
     *
     * @param subjectInfo
     * @param method
     * @param multiAdd
     * @throws MissingAnnotation
     */
    private void parseSetter(final Method method, final EffectivePredicate superPredicate, final boolean multiAdd)
            throws MissingAnnotation {

        final Class<?> parms[] = method.getParameterTypes();
        if (parms.length == 1) {
            final Class<?> valueType = parms[0];
            final EffectivePredicate predicate = new EffectivePredicate( method ).merge( superPredicate );

            final PredicateInfoImpl pi = new PredicateInfoImpl( predicate, method.getName(), valueType );
            subjectInfo.add( pi );

            predicate.setType( null );
            predicate.setLiteralType( "" );

            if (multiAdd) {
                processAssociatedSetters( pi, method, predicate );
            }
            processAssociatedMethods( ActionType.SETTER, valueType, predicate, method );
        }
    }

    private void processAssociatedMethods(final ActionType actionType, final Class<?> valueType,
            final EffectivePredicate predicate, final Method method) throws MissingAnnotation {
        predicate.setType( valueType );
        final String subName = actionType.extractName( method.getName() );
        final Class<?> context = method.getDeclaringClass();
        parseAssociatedMethod( context, "get" + subName, predicate, null );
        parseAssociatedMethod( context, "is" + subName, predicate, null );
        if (ActionType.isMultiple( method )) {
            parseAssociatedMethod( context, "add" + subName, predicate, valueType );
            parseAssociatedMethod( context, "has" + subName, predicate, valueType );
            parseAssociatedMethod( context, "remove" + subName, predicate, valueType );
            if (valueType == RDFNode.class) {
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
            parseAssociatedMethod( context, "set" + subName, predicate, valueType );
            parseAssociatedMethod( context, "has" + subName, predicate, null );
            parseAssociatedMethod( context, "remove" + subName, predicate, null );
        }
    }
}
