/*
 * Copyright 2012, XENEI.com
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

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.exceptions.MissingAnnotationException;
import org.xenei.jena.entities.exceptions.NotInterfaceException;

/**
 * An implementation of the EntityManager interface.
 *
 */
public class EntityManagerImpl implements EntityManager {
    private static Logger LOG = LoggerFactory.getLogger( EntityManagerImpl.class );
    private final SubjectInfoFactory factory;

    /**
     * Constructor.
     */
    public EntityManagerImpl() {
        factory = new SubjectInfoFactory();
    }

    @Override
    public void reset() {
        factory.clear();
    }

    /**
     * Read an instance of clazz from Object source. If source does not have the
     * required types as defined in the Subject annotation of clazz they will be
     * added.
     * <p>
     * This method may modify the graph and should be called within the scope of
     * a transaction if the underlying graph requires them.
     * </p>
     *
     * @param source
     *            Must either implement Resource or ResourceWrapper interfaces.
     * @param clazz
     *            The class containing the Subject annotation.
     * @return source for chaining
     * @throws MissingAnnotationException
     *             if clazz does not have Subject annotations.
     * @throws IllegalArgumentException
     *             if source implements neither Resource nor ResourceWrapper.
     */
    @Override
    public <T> T addInstanceProperties(final T source, final Class<?> clazz)
            throws MissingAnnotationException, IllegalArgumentException {
        final Resource r = ResourceWrapper.getResource( source );
        final Subject e = clazz.getAnnotation( Subject.class );
        if (e == null) {
            throw new MissingAnnotationException( "No Subject annotationin " + clazz.getCanonicalName() );
        }
        final Model model = r.getModel(); // may be null;
        if (e != null) {
            for (final String type : e.types()) {
                final Resource object = (model != null) ? model.createResource( type )
                        : ResourceFactory.createResource( type );
                if (!r.hasProperty( RDF.type, object )) {
                    r.addProperty( RDF.type, object );
                }
            }
        }
        return source;
    }

    @Override
    public Subject getSubject(final Class<?> clazz) {
        final Set<Class<?>> interfaces = new LinkedHashSet<>();
        for (Class<?> cls = clazz; (cls != Object.class) && (cls != null); cls = cls.getSuperclass()) {
            final Subject retval = cls.getAnnotation( Subject.class );
            if (retval != null) {
                return retval;
            }
            interfaces.addAll( Arrays.asList( cls.getInterfaces() ) );
        }

        for (final Class<?> cls : interfaces) {
            final Subject retval = cls.getAnnotation( Subject.class );
            if (retval != null) {
                return retval;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.xenei.jena.entities.impl.EntityManager#getSubjectInfo(java.lang.Class
     * )
     */
    @Override
    public SubjectInfo getSubjectInfo(final Class<?> clazz) {
        try {
            return factory.parse( clazz );
        } catch (final MissingAnnotationException e) {
            throw new IllegalArgumentException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.xenei.jena.entities.impl.EntityManager#isInstance(org.apache.jena
     * .rdf.model.Resource, java.lang.Class)
     */
    @Override
    public boolean isInstance(final Object target, final Class<?> clazz) {
        final Subject subject = clazz.getAnnotation( Subject.class );
        if (subject == null) {
            throw new IllegalArgumentException( String.format( "%s is not annotated as a Subject", clazz.getName() ) );
        }

        Resource r = null;
        try {
            r = ResourceWrapper.getResource( target );
        } catch (final IllegalArgumentException e) {
            return false;
        }

        for (final String type : subject.types()) {
            final Resource object = ResourceFactory.createResource( type );
            if (!r.hasProperty( RDF.type, object )) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void parseClasses(final String packageName) throws MissingAnnotationException, NotInterfaceException {
        parseClasses( new String[] { packageName } );
    }

    @Override
    public void parseClasses(final String[] packageNames) throws MissingAnnotationException, NotInterfaceException {
        boolean hasErrors = false;
        for (final String pkg : packageNames) {

            for (final Class<?> c : ClassUtils.getClasses( pkg )) {
                if (c.getAnnotation( Subject.class ) != null) {
                    try {
                        factory.parse( c );
                    } catch (final MissingAnnotationException e) {
                        EntityManagerImpl.LOG.warn( "Error processing {}: {}", c, e.getMessage() );
                        hasErrors = true;
                    }
                }
            }
        }
        if (hasErrors) {
            throw new MissingAnnotationException(
                    String.format( "Unable to parse all %s See log for more details", Arrays.asList( packageNames ) ) );
        }
    }

    @Override
    public <T> T make(final Object source, final Class<T> result, final Class<?>... secondaryClasses)
            throws MissingAnnotationException, NotInterfaceException {
        ClassUtils.validateInterface( result );
        Resource r = addInstanceProperties( ResourceWrapper.getResource( source ), result );
        for (final Class<?> c : secondaryClasses) {
            r = addInstanceProperties( r, c );
        }
        return read( r, result, secondaryClasses );
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.xenei.jena.entities.impl.EntityManager#read(org.apache.jena.rdf.model
     * .Resource, java.lang.Class, java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T read(final Object source, final Class<T> result, final Class<?>... interfaces)
            throws MissingAnnotationException, NotInterfaceException {
        if (result == null) {
            throw new IllegalArgumentException( "result type may not be null or empty." );
        }
        final List<Class<?>> classes = new ArrayList<>();

        ClassUtils.validateInterface( result );
        final SubjectInfoImpl subjectInfo = factory.parse( result );
        if (subjectInfo == null) {
            throw new MissingAnnotationException( result );
        }
        classes.add( result );

        if ((interfaces != null) && (interfaces.length > 0)) {
            for (final Class<?> cla : interfaces) {
                ClassUtils.validateInterface( cla );
                if (!classes.contains( cla )) {
                    if (factory.parse( cla ) == null) {
                        throw new MissingAnnotationException( result );
                    }
                    classes.add( cla );
                }
            }
        }
        if (!classes.contains( ResourceWrapper.class )) {
            classes.add( ResourceWrapper.class );
        }

        SubjectInfo sourceSubject = null;
        try {
            sourceSubject = factory.parse( source.getClass() );
        } catch (final MissingAnnotationException expected) {
            // ignore as is expected in some cases.
        }
        final ResourceEntityProxy resourceEntityProxy = new ResourceEntityProxy( this, source, subjectInfo,
                sourceSubject );
        final Class<?>[] classArray = new Class<?>[classes.size()];
        return (T) Proxy.newProxyInstance( result.getClassLoader(), classes.toArray( classArray ),
                resourceEntityProxy );
    }

    /**
     * Since the EntityManger implements the manager as a live data read against
     * the Model, this method provides a mechanism to copy all the values from
     * the source to the target. It reads scans the target class for "set"
     * methods and the source class for associated "get" methods. If a pairing
     * is found the value of the "get" call is passed to the "set" call.
     *
     * @param source
     *            The object that has the values to transfer.
     * @param target
     *            The object that has the receptors for the values.
     * @return The target object after all setters have been called.
     */
    @Override
    public Object update(final Object source, final Object target) {
        final Class<?> targetClass = target.getClass();
        final Class<?> sourceClass = source.getClass();
        for (final Method targetMethod : targetClass.getMethods()) {
            if (ActionType.SETTER.isA( targetMethod.getName() )) {
                final Class<?>[] targetMethodParams = targetMethod.getParameterTypes();
                if (targetMethodParams.length == 1) {
                    final String partialName = ActionType.SETTER.extractName( targetMethod.getName() );

                    Method configMethod = null;
                    // try "getX" method
                    String configMethodName = "get" + partialName;
                    try {
                        configMethod = sourceClass.getMethod( configMethodName );
                    } catch (final NoSuchMethodException e) {
                        // no "getX" method so try "isX"
                        try {
                            configMethodName = "is" + partialName;
                            configMethod = sourceClass.getMethod( configMethodName );
                        } catch (final NoSuchMethodException expected) {
                            // no getX or setX
                            // configMethod will be null.
                        }
                    }
                    // verify that the config method was annotated as a
                    // predicate before
                    // we use it.

                    try {
                        if (configMethod != null) {
                            final boolean setNull = !targetMethodParams[0].isPrimitive();
                            if (TypeChecker.canBeSetFrom( targetMethodParams[0], configMethod.getReturnType() )) {
                                final Object val = configMethod.invoke( source );
                                if (setNull || (val != null)) {
                                    targetMethod.invoke( target, val );
                                }
                            }

                        }
                    } catch (final IllegalArgumentException e) {
                        throw new RuntimeException( e );
                    } catch (final IllegalAccessException e) {
                        throw new RuntimeException( e );
                    } catch (final InvocationTargetException e) {
                        throw new RuntimeException( e );
                    }

                }
            }
        }
        return target;
    }

}
