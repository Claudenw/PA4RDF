/*
 * Copyright 2012-2018, XENEI.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xenei.jena.entities.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.handlers.UriHandler;

/**
 * An implementation of the EntityManager interface.
 * 
 */
public class MethodParser {

    /**
     * A class that parses abstract methods.
     *
     */
    private class AbstractMethodParser {
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
        private void parse(final Method method, final EffectivePredicate predicate) throws MissingAnnotation {

            if (predicate.actionType().isType( method )) {

                switch (predicate.actionType()) {
                case SETTER:
                    parseSetter( method, predicate );
                    break;

                case EXISTENTIAL:
                    parseExistential( method, predicate );
                    break;

                case GETTER:
                    parseGetter( method, predicate );

                    break;

                case REMOVER:
                    parseRemover( method, predicate );
                    break;
                }
            }
        }

        private void parseExistential(final Method method, final EffectivePredicate predicate) {
            // we only parse boolean results
            if (TypeChecker.canBeSetFrom( Boolean.class, method.getReturnType() )
                    && (method.getParameterTypes().length <= 1)) {
                predicate.checkValid();
                final Class<?> valueType = method.getReturnType();
                subjectInfo.add( new PredicateInfoImpl( entityManager, predicate, method, valueType ) );
            }
        }

        private void parseGetter(final Method method, final EffectivePredicate predicate) throws MissingAnnotation {

            final String nameSuffix = predicate.actionType().extractName( method.getName() );
            if (isCollection( method )) {
                if (!isMultiAdd( nameSuffix )) {
                    // returning a collection and we have a single add method.
                    // so set the return type if not set.
                    if (predicate.isTypeNotSet()) {
                        for (final Method setterMethod : getSetterMethods( nameSuffix )) {
                            // we have a setter method so lets merge info

                            final EffectivePredicate ep = new EffectivePredicate( setterMethod );
                            predicate.merge( ep );
                            final PredicateInfo setterPI = MethodParser.this.parse( setterMethod, ep );
                            if (setterPI == null) {
                                throw new IllegalStateException( "Could not parse setter " + setterMethod.getName() );
                            }
                            if (predicate.isTypeNotSet()) {
                                predicate.type( setterPI.getValueClass() );
                            } else {
                                if (predicate.type().equals( URI.class )) {
                                    predicate.type( RDFNode.class );
                                }
                            }
                        }
                    }
                } else {
                    // returning a collection and multiple add methods
                    // so we must have a type set.
                    if (predicate.isTypeNotSet()) {
                        throw new MissingAnnotation(
                                String.format( "%s.%s must have a Predicate annotation to define type",
                                        subjectInfo.getImplementedClass(), method.getName() ) );
                    }
                }

            }
            predicate.checkValid();
            subjectInfo.add( new PredicateInfoImpl( entityManager, predicate, method, method.getReturnType() ) );

            processAssociatedMethods( predicate.type(), predicate, method );

        }

        /**
         * Processes "setX" and "addX" functions.
         * 
         * @param subjectInfo
         * @param method
         * @param multiAdd
         * @throws MissingAnnotation
         */
        private void parseSetter(final Method method, final EffectivePredicate predicate) throws MissingAnnotation {

            final Class<?> parms[] = method.getParameterTypes();
            if (parms.length == 1) {
                final Class<?> valueType = parms[0];
                predicate.checkValid();
                final PredicateInfoImpl pi = new PredicateInfoImpl( entityManager, predicate, method, valueType );
                subjectInfo.add( pi );

                processAssociatedMethods( valueType, predicate, method );
            }
        }

        private void processAssociatedMethods(final Class<?> valueType, final EffectivePredicate predicate,
                final Method method) throws MissingAnnotation {
            final String nameSuffix = ActionType.parse( method.getName() ).extractName( method.getName() );

            for (final Method mthd : subjectInfo.getImplementedClass().getMethods()) {
                for (final ActionType actionType : ActionType.values()) {
                    if (actionType.isType( mthd ) && actionType.extractName( mthd.getName() ).equals( nameSuffix )) {
                        MethodParser.this.parse( mthd, predicate );
                    }
                }
            }
        }
    }

    private class ImplementedMethodParser {

        private void parse(final Method method, final EffectivePredicate predicate) throws MissingAnnotation {

            /*
             * If the class of the method has a Subject annotation then all the
             * Subject annotations from parent classes are overridden so just
             * call the abstractMethodParser as it will handle the results
             * correctly.
             */
            if (method.getDeclaringClass().isAnnotationPresent( Subject.class )) {
                MethodParser.this.abstractMethodParser.parse( method, predicate );
                return;
            }

            final Set<Class<?>> interfaces = findAbstracts( method.getDeclaringClass() );

            switch (predicate.actionType()) {
            case SETTER:
                final Integer i = addCount.get( method.getName() );
                if (i != null) {
                    parseSetter( interfaces, method, (i > 1) );
                }
                break;

            case EXISTENTIAL:
                if (method.getParameterTypes().length <= 1) {
                    parseExistential( interfaces, method );
                }
                break;

            case GETTER:
                if (method.getParameterTypes().length == 0) {
                    parseGetter( interfaces, method );
                }
                break;

            case REMOVER:
                if (method.getParameterTypes().length <= 1) {
                    parseRemover( interfaces, method );
                }
                break;

            }

        }

        private void parseAssociatedMethod(Class<?> clazz, final Set<Class<?>> abstracts, final EffectivePredicate ep,
                final String name, final Class<?> valueClass) throws MissingAnnotation {
            try {
                Method m = null;
                if (valueClass == null) {
                    m = clazz.getMethod( name );
                } else {
                    m = clazz.getMethod( name, valueClass );
                }
                MethodParser.this.parse( m, ep );
            } catch (NoSuchMethodException | SecurityException e) {
                final Class<?>[] cAry = valueClass == null ? new Class<?>[0] : new Class<?>[] { valueClass };

                final List<Method> lm = findMethod( abstracts, name, cAry );
                if (lm.size() > 0) {
                    MethodParser.this.parse( lm.get( 0 ), ep );
                }
            }
        }

        private void parseAssociatedURIMethod(Class<?> clazz, final Set<Class<?>> abstracts,
                final EffectivePredicate ep, final String name) throws MissingAnnotation {
            try {
                final Method m = clazz.getMethod( name, String.class );
                MethodParser.this.parse( m, ep );
            } catch (NoSuchMethodException | SecurityException e) {
                final List<Method> lm = findMethod( abstracts, name, new Class<?>[] { String.class } );
                for (final Method m2 : lm) {
                    if (hasURIParameter( m2 )) {
                        MethodParser.this.parse( m2, ep );
                        return;
                    }
                }
            }
        }

        private void parseExistential(final Set<Class<?>> abstracts, final Method method) {
            for (final Method m : findMethod( abstracts, method )) {
                // we only parse boolean results
                if (TypeChecker.canBeSetFrom( Boolean.class, m.getReturnType() )
                        && (m.getParameterTypes().length <= 1)) {
                    final SubjectInfo si = entityManager.getSubjectInfo( m.getDeclaringClass() );
                    final PredicateInfoImpl pi = (PredicateInfoImpl) si.getPredicateInfo( m );
                    if (pi != null) {
                        final PredicateInfoImpl pi2 = new PredicateInfoImpl( pi );
                        subjectInfo.add( pi2 );
                        return;
                    }
                }
            }
        }

        private void parseGetter(final Set<Class<?>> abstracts, final Method method) throws MissingAnnotation {
            // predicate for getter method includes predicate infor for setter
            // method.
            final EffectivePredicate ep = new EffectivePredicate( method );

            for (final Method m : findMethod( abstracts, method )) {
                final PredicateInfoImpl pi = (PredicateInfoImpl) MethodParser.this.parse( m );
                if (pi != null) {
                    final PredicateInfoImpl newPi = new PredicateInfoImpl( pi );

                    newPi.getEffectivePredicate().addPostExec( ep.postExec() );

                    subjectInfo.add( newPi );
                    return;
                }
            }
        }

        private void parseRemover(final Set<Class<?>> abstracts, final Method method) throws MissingAnnotation {
            for (final Method m : findMethod( abstracts, method )) {
                final SubjectInfo si = entityManager.getSubjectInfo( m.getDeclaringClass() );
                final PredicateInfoImpl pi = (PredicateInfoImpl) si.getPredicateInfo( m );
                if (pi != null) {
                    subjectInfo.add( new PredicateInfoImpl( pi ) );
                    return;
                }
            }
        }

        /**
         * Processes "setX" and "addX" functions.
         * 
         * @param subjectInfo
         * @param m
         * @param multiAdd
         * @throws MissingAnnotation
         */
        private void parseSetter(final Set<Class<?>> abstracts, final Method method, final boolean multiAdd)
                throws MissingAnnotation {

            final List<Method> lm = findMethod( abstracts, method );
            System.out.println( String.format( "Matching method count for %s: %s", method, lm.size() ) );
            assert (lm.size() < 2);
            for (final Method m : lm) {

                final SubjectInfo si = entityManager.getSubjectInfo( m.getDeclaringClass() );
                final PredicateInfoImpl oldPi = (PredicateInfoImpl) si.getPredicateInfo( m );
                if (oldPi != null) {
                    final PredicateInfoImpl pi = new PredicateInfoImpl( oldPi );

                    subjectInfo.add( pi );

                    if (multiAdd) {
                        processAssociatedSetters( pi, method, pi.getEffectivePredicate() );
                    }

                    final String nameSuffix = pi.getEffectivePredicate().actionType().extractName( method.getName() );

                    // add sub types
                    for (final String fnName : ActionType.GETTER.functionNames( nameSuffix )) {
                        parseAssociatedMethod( method.getDeclaringClass(), abstracts, pi.getEffectivePredicate(),
                                fnName, null );
                    }

                    if (ActionType.isMultiple( method )) {
                        for (final String fnName : ActionType.EXISTENTIAL.functionNames( nameSuffix )) {
                            parseAssociatedMethod( method.getDeclaringClass(), abstracts, pi.getEffectivePredicate(),
                                    fnName, pi.getValueClass() );
                            if (pi.getValueClass() == RDFNode.class) {
                                // look for @URI annotated strings
                                parseAssociatedURIMethod( method.getDeclaringClass(), abstracts,
                                        pi.getEffectivePredicate(), fnName );
                            }
                        }
                        for (final String fnName : ActionType.REMOVER.functionNames( nameSuffix )) {
                            parseAssociatedMethod( method.getDeclaringClass(), abstracts, pi.getEffectivePredicate(),
                                    fnName, pi.getValueClass() );
                            if (pi.getValueClass() == RDFNode.class) {
                                // look for @URI annotated strings
                                parseAssociatedURIMethod( method.getDeclaringClass(), abstracts,
                                        pi.getEffectivePredicate(), fnName );
                            }
                        }

                    } else {
                        for (final String fnName : ActionType.EXISTENTIAL.functionNames( nameSuffix )) {
                            parseAssociatedMethod( method.getDeclaringClass(), abstracts, pi.getEffectivePredicate(),
                                    fnName, null );
                            if (pi.getValueClass() == RDFNode.class) {
                                // look for @URI annotated strings
                                parseAssociatedURIMethod( method.getDeclaringClass(), abstracts,
                                        pi.getEffectivePredicate(), fnName );
                            }
                        }
                        for (final String fnName : ActionType.REMOVER.functionNames( nameSuffix )) {
                            parseAssociatedMethod( method.getDeclaringClass(), abstracts, pi.getEffectivePredicate(),
                                    fnName, null );
                            if (pi.getValueClass() == RDFNode.class) {
                                // look for @URI annotated strings
                                parseAssociatedURIMethod( method.getDeclaringClass(), abstracts,
                                        pi.getEffectivePredicate(), fnName );
                            }
                        }
                    }

                    return;
                }
            }
            throw new MissingAnnotation( String.format( "Can not locate annotated %s from %s", method.getName(),
                    method.getDeclaringClass() ) );

        }

    }

    private class InterceptedMethodParser {
        private void parse() {
            throw new RuntimeException( "Not IMPLEMENTED" );
        }
    }

    /**
     * The subject info we are adding to
     */
    private final SubjectInfoImpl subjectInfo;
    /**
     * A map of the number of add methods by method name (eg. number of addX and
     * setX methods) without respect to argument types. These are only adds that
     * we count as an add method. (i.e. no add methods with zero or more than
     * one argument).
     */
    private final Map<String, Integer> addCount;
    /**
     * The entity manager we are working with.
     */
    private final EntityManagerImpl entityManager;
    /**
     * The stack of methods we are currently parsing.
     */
    private final Stack<Method> parseStack;
    /**
     * The method parser to parse abstract methods.
     */
    private final AbstractMethodParser abstractMethodParser = new AbstractMethodParser();
    /**
     * The method parser to parse implemented methods.
     */
    private final ImplementedMethodParser implementedMethodParser = new ImplementedMethodParser();
    /**
     * the method parser to parse intercepted methods (NOT IMPLEMENTED)
     */
    private final InterceptedMethodParser interceptedMethodParser = new InterceptedMethodParser();

    /**
     * Constructor.
     * 
     * @param entityManager
     *            The EntityManager we are working with.
     * @param subjectInfo
     *            The Subject Info that we are adding to.
     * @param addCount
     *            A mapping of add function names to counts.
     */
    public MethodParser(final EntityManagerImpl entityManager, final SubjectInfoImpl subjectInfo,
            final Map<String, Integer> addCount) {
        this.entityManager = entityManager;
        this.subjectInfo = subjectInfo;
        this.addCount = addCount;
        this.parseStack = new Stack<Method>();
    }

    /**
     * Returns true if the user facing objects are Collections or Arrays.
     * 
     * @param method
     *            the Method to check.
     * @return true if the method handles collections.
     */
    public static boolean isCollection(Method method) {
        final ActionType action = ActionType.parse( method.getName() );
        Class<?> c = null;
        switch (action) {
        case EXISTENTIAL:
        case REMOVER:
            return false;
        case GETTER:
            c = method.getReturnType();
            break;
        case SETTER:
            if (method.isVarArgs()) {
                return false;
            }
            c = method.getParameterTypes()[0];
            break;
        }

        return c.isArray() || Collection.class.isAssignableFrom( c ) || RDFList.class.isAssignableFrom( c )
                || Iterator.class.isAssignableFrom( c );
    }

    /**
     * Return the set of implemented abstract classes and interfaces in the
     * order they were declared.
     * 
     * @param clazz
     *            The class to find abstract base classes and interfaces for.
     * @return The ordered Set.
     */
    public Set<Class<?>> findAbstracts(final Class<?> clazz) {
        if (clazz.equals( Object.class )) {
            return Collections.emptySet();
        }
        final Queue<Class<?>> queue = new LinkedList<Class<?>>();
        queue.offer( clazz );
        // order preserving set
        final Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();

        for (Class<?> cls = queue.poll(); cls != null; cls = queue.poll()) {

            if (!cls.equals( Object.class )) {
                if (Modifier.isAbstract( cls.getModifiers() )) {
                    interfaces.add( cls );
                }
                final Class<?> sc = cls.getSuperclass();
                if ((sc != null) && !interfaces.contains( sc ) && !queue.contains( sc )) {
                    queue.offer( sc );
                }
            }
            for (final Class<?> c : cls.getInterfaces()) {
                if (!interfaces.contains( c ) && !queue.contains( c )) {
                    // interfaces.add(c);
                    queue.offer( c );
                }
            }

        }
        return interfaces;
    }

    /**
     * Find a method in the set of classes.
     * 
     * Matches by name and parameter types.
     * 
     * @param classSet
     *            The set of classes to scan.
     * @param method
     *            The method to locate.
     * @return The first method in the set of classes.
     */
    public List<Method> findMethod(final Set<Class<?>> classSet, final Method method) {
        return findMethod( classSet, method.getName(), method.getParameterTypes() );
    }

    /**
     * Find a method find a method in the set of classes
     * 
     * @param classSet
     *            the set of classes to scan.
     * @param methodName
     *            the method name to locate
     * @param aParams
     *            the array of parameters to the method.
     * @return
     */
    public List<Method> findMethod(final Set<Class<?>> classSet, final String methodName, final Class<?>[] aParams) {
        final List<Class<?>> lParams = Arrays.asList( aParams );
        final List<Method> lst = new ArrayList<Method>();
        for (final Class<?> c : classSet) {
            for (final Method m : c.getDeclaredMethods()) {
                if (m.getName().equals( methodName ) && Modifier.isAbstract( m.getModifiers() )) {

                    final List<Class<?>> pl = Arrays.asList( m.getParameterTypes() );
                    if (lParams.size() == pl.size()) {
                        if ((pl.size() == 0) || (Collections.indexOfSubList( pl, lParams ) == 0)) {
                            lst.add( m );
                        }
                    }
                }
            }
        }
        return lst;
    }

    /**
     * Get the list of setter methods.
     * 
     * @param nameSuffix
     *            the suffix for the setter methods (i.e. the X in setX and
     *            addX)
     * @return a list of methods.
     */
    private List<Method> getSetterMethods(final String nameSuffix) {
        final List<Method> retval = new ArrayList<Method>();
        // find the setter
        for (final Method testMth : subjectInfo.getImplementedClass().getMethods()) {
            final String methodName = testMth.getName();
            if (ActionType.SETTER.isA( methodName )
                    && ActionType.SETTER.extractName( methodName ).equals( nameSuffix )) {
                if ((testMth.getParameterTypes().length == 1) && shouldProcess( testMth )) {
                    retval.add( testMth );
                }
            }
        }
        return retval;
    }

    /**
     * Return true if the method has a uri parameter annotation.
     * 
     * @param method
     *            the method to check.
     * @return true if the method has a uri parameter annotation.
     */
    private boolean hasURIParameter(final Method method) {
        if (method.getParameterAnnotations() != null) {
            for (final Annotation a : method.getParameterAnnotations()[0]) {
                if (a instanceof URI) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determine if there are multiple add statements for a name suffix.
     * 
     * @param nameSuffix
     *            the name to check
     * @return true if the suffix has multiple add statements.
     */
    private boolean isMultiAdd(final String nameSuffix) {
        int count = 0;
        for (final String fName : ActionType.SETTER.functionNames( nameSuffix )) {
            final Integer n = addCount.get( fName );
            count += n == null ? 0 : n;
            if (count > 1) {
                return true;
            }
        }
        return count > 1;
    }

    /**
     * Parse the method if necessary.
     * 
     * The first time the method is seen it is parsed, after that a cached
     * version is returned.
     * 
     * @param method
     *            The method to parse
     * @return the PredicateInfo for the class or null if not a method to parse.
     * @throws MissingAnnotation
     *             if the Method does not have an annotation.
     */
    public PredicateInfo parse(final Method method) throws MissingAnnotation {
        return parse( method, null );
    }

    /**
     * Parse the method using the effective predicate for defaults.
     * 
     * @param method
     *            The method to parse.
     * @param predicate
     *            the effective predicate to use for defaults.
     * @return the Predicate info.
     * @throws MissingAnnotation
     *             if the Method does not have an annotation.
     */
    private PredicateInfo parse(final Method method, final EffectivePredicate predicate) throws MissingAnnotation {
        PredicateInfo pi = subjectInfo.getPredicateInfo( method );

        // only process if we havn't yet.
        if (pi == null) {
            // does the method meet the requirements for parsing
            if (shouldProcess( method )) {
                // only process if we are not already processing
                if (!parseStack.contains( method )) {
                    parseStack.push( method );

                    try {
                        final EffectivePredicate ep = new EffectivePredicate( method ).merge( predicate );
                        if (Modifier.isAbstract( method.getModifiers() )) {
                            abstractMethodParser.parse( method, ep );
                        } else {
                            ep.merge( method.getAnnotation( Predicate.class ) );
                            if (ep.impl()) {
                                implementedMethodParser.parse( method, ep );
                            } else {
                                interceptedMethodParser.parse();
                            }

                        }

                        /*
                         * retrieve the predicate info we just parsed may be
                         * null if we should not have parsed it
                         */
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

    /**
     * Parse a remove method.
     * 
     * @param method
     *            the remove method.
     * @param predicate
     *            the current effective predicate
     */
    private void parseRemover(final Method method, final EffectivePredicate predicate) {
        final Class<?> valueClass = predicate.type();
        predicate.checkValid();
        final PredicateInfoImpl pii = new PredicateInfoImpl( entityManager, predicate, method, valueClass );
        subjectInfo.add( pii );
    }

    /**
     * Process the other setters associated with the method.
     * 
     * @param pi
     *            the PredicateInfo from the previous setter parse.
     * @param method
     *            as setter method.
     * @param predicate
     *            the effective predicate from the parsing thus far.
     * @throws MissingAnnotation
     *             if a required annotation is missing.
     */
    private void processAssociatedSetters(final PredicateInfoImpl pi, final Method method,
            final EffectivePredicate predicate) throws MissingAnnotation {
        try {
            if ((pi.getValueClass() == String.class)
                    && (pi.getObjectHandler( entityManager ).getClass() == UriHandler.class)) {
                parse( method.getDeclaringClass().getMethod( pi.getMethodName(), RDFNode.class ), predicate );

            }
            if (RDFNode.class.isAssignableFrom( pi.getValueClass() )) {

                final Method m2 = method.getDeclaringClass().getMethod( pi.getMethodName(), String.class );
                if (hasURIParameter( m2 )) {
                    parse( m2, predicate );
                }

            }
        } catch (NoSuchMethodException | SecurityException e) {
            // ignore
        }
    }

    /**
     * Only process abstract methods or methods with Predicate annotations and
     * only when either of those is not varArgs.
     * 
     * @param method
     * @return true if we should parse this, false otherwise.
     */
    private boolean shouldProcess(final Method method) {
        boolean retval = !method.isVarArgs();
        if (retval) {
            retval = Modifier.isAbstract( method.getModifiers() ) || (method.getAnnotation( Predicate.class ) != null);
        }
        return retval;
    }
}
