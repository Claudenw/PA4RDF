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

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.impl.datatype.CharDatatype;
import org.xenei.jena.entities.impl.datatype.CharacterDatatype;
import org.xenei.jena.entities.impl.datatype.LongDatatype;

/**
 * An implementation of the EntityManager interface.
 *
 */
public class EntityManagerImpl implements EntityManager {
    private static Logger LOG = LoggerFactory.getLogger( EntityManagerImpl.class );

    private final Map<Class<?>, SubjectInfoImpl> classInfo = new HashMap<>();

    static {
        EntityManagerImpl.registerTypes();
    }

    /**
     * Register the datatypes used by the entity manger specifically xsd:long :
     * java.util.Long xsd:string : java.util.Character xsd:string :
     * java.lang.char
     *
     * and finally resetting xsd:string to java.lang.String
     */
    public static void registerTypes() {
        RDFDatatype rtype = null;
        // handle the string types
        // preserve string class and put it back later.
        final RDFDatatype stype = TypeMapper.getInstance().getTypeByClass( String.class );
        rtype = new CharacterDatatype();
        TypeMapper.getInstance().registerDatatype( rtype );
        rtype = new CharDatatype();
        TypeMapper.getInstance().registerDatatype( rtype );
        // put the string type back so that it is the registered type for
        // xsd:string
        TypeMapper.getInstance().registerDatatype( stype );

        // change the long type.
        rtype = new LongDatatype();
        TypeMapper.getInstance().registerDatatype( rtype );
    }

    /**
     * Constructor.
     */
    public EntityManagerImpl() {
        try {
            parse( ResourceWrapper.class );
        } catch (final MissingAnnotation e) {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void reset() {
        classInfo.clear();
        EntityManagerImpl.registerTypes();
        try {
            parse( ResourceWrapper.class );
        } catch (final MissingAnnotation e) {
            throw new RuntimeException( e );
        }
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
     * @throws MissingAnnotation
     *             if clazz does not have Subject annotations.
     * @throws IllegalArgumentException
     *             if source implements neither Resource nor ResourceWrapper.
     */
    @Override
    public <T> T addInstanceProperties(final T source, final Class<?> clazz)
            throws MissingAnnotation, IllegalArgumentException {
        final Resource r = getResource( source );
        final Subject e = clazz.getAnnotation( Subject.class );
        if (e == null) {
            throw new MissingAnnotation( "No Subject annotationin " + clazz.getCanonicalName() );
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

    private Map<String, Integer> countAdders(final Method[] methods) {
        final Map<String, Integer> addCount = new HashMap<>();
        for (final Method m : methods) {
            if (isAdd( m )) {
                Integer i = addCount.get( m.getName() );
                if (i == null) {
                    i = 1;
                } else {
                    i = i + 1;
                }
                addCount.put( m.getName(), i );
            }
        }
        return addCount;
    }

    /**
     * Recursive method used to find all classes in a given directory and
     * subdirs. Adapted from http://snippets.dzone.com/posts/show/4831 and
     * extended to support use of JAR files
     *
     * @param directory
     *            The base directory
     * @param packageName
     *            The package name for classes found inside the base directory
     * @return The classes
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Set<String> findClasses(final String directory, final String packageName) throws IOException {
        final Set<String> classes = new HashSet<>();
        if (directory.startsWith( "file:" ) && directory.contains( "!" )) {
            final String[] split = directory.split( "!" );
            final URL jar = new URL( split[0] );
            final ZipInputStream zip = new ZipInputStream( jar.openStream() );
            ZipEntry entry = null;
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.getName().endsWith( ".class" )) {
                    final String className = entry.getName().replaceAll( "[$].*", "" ).replaceAll( "[.]class", "" )
                            .replace( '/', '.' );
                    classes.add( className );
                }
            }
        }
        final File dir = new File( directory );
        if (!dir.exists()) {
            return classes;
        }
        final File[] files = dir.listFiles();
        for (final File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains( "." );
                classes.addAll( findClasses( file.getAbsolutePath(), packageName + "." + file.getName() ) );
            } else if (file.getName().endsWith( ".class" )) {
                classes.add( packageName + '.' + file.getName().substring( 0, file.getName().length() - 6 ) );
            }
        }
        return classes;
    }

    /**
     * Scans all classes accessible from the context class loader which belong
     * to the given package and subpackages. Adapted from
     * http://snippets.dzone.com/posts/show/4831 and extended to support use of
     * JAR files
     *
     * @param packageName
     *            The base package or class name
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private Collection<Class<?>> getClasses(final String packageName) {

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        final String path = packageName.replace( '.', '/' );
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources( path );
        } catch (final IOException e1) {
            EntityManagerImpl.LOG.error( e1.toString() );
            return Collections.emptyList();
        }
        final Set<Class<?>> classes = new HashSet<>();
        if (resources.hasMoreElements()) {
            while (resources.hasMoreElements()) {
                final URL resource = resources.nextElement();
                try {
                    for (final String clazz : findClasses( resource.getFile(), packageName )) {
                        try {
                            classes.add( Class.forName( clazz ) );
                        } catch (final ClassNotFoundException e) {
                            EntityManagerImpl.LOG.warn( e.toString() );
                        }
                    }
                } catch (final IOException e) {
                    EntityManagerImpl.LOG.warn( e.toString() );
                }
            }
        } else {
            // there are no resources at that path so see if it is a class
            try {
                classes.add( Class.forName( packageName ) );
            } catch (final ClassNotFoundException e) {
                EntityManagerImpl.LOG.warn( "{} was neither a package name nor a class name", packageName );
            }
        }
        return classes;
    }

    private Resource getResource(final Object target) throws IllegalArgumentException {
        if (target instanceof ResourceWrapper) {
            return ((ResourceWrapper) target).getResource();
        }
        if (target instanceof Resource) {
            return (Resource) target;
        }
        throw new IllegalArgumentException(
                String.format( "%s implements neither Resource nor ResourceWrapper", target.getClass() ) );
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
            return parse( clazz );
        } catch (final MissingAnnotation e) {
            throw new IllegalArgumentException( e );
        }
    }

    private boolean isAdd(final Method m) {
        try {
            if (ActionType.parse( m.getName() ) == ActionType.SETTER) {
                final Class<?> parms[] = m.getParameterTypes();
                return (parms != null) && (parms.length == 1);
            }
        } catch (final IllegalArgumentException expected) {
            // do nothing
        }
        return false;
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
            r = getResource( target );
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

    /**
     * Parse the class if necessary.
     *
     * The first time the class is seen it is parsed, after that a cached
     * version is returned.
     *
     * @param clazz
     * @return The SubjectInfo for the class.
     * @throws MissingAnnotation
     */
    private SubjectInfoImpl parse(final Class<?> clazz) throws MissingAnnotation {
        SubjectInfoImpl subjectInfo = classInfo.get( clazz );

        if (subjectInfo == null) {
            EntityManagerImpl.LOG.info( "Parsing {}", clazz );
            subjectInfo = new SubjectInfoImpl( clazz );

            final MethodParser parser = new MethodParser( this, subjectInfo, countAdders( clazz.getMethods() ) );

            boolean foundAnnotation = false;
            final List<Method> annotated = new ArrayList<>();
            for (final Method method : clazz.getMethods()) {
                try {
                    final ActionType actionType = ActionType.parse( method.getName() );
                    if (method.getAnnotation( Predicate.class ) != null) {
                        foundAnnotation = true;
                        if (ActionType.GETTER == actionType) {
                            parser.parse( method );
                        } else {
                            annotated.add( method );
                        }
                    }
                } catch (final IllegalArgumentException expected) {
                    // not an action type ignore method
                }

            }
            if (!foundAnnotation) {
                throw new MissingAnnotation( "No annotated methods in " + clazz.getCanonicalName() );
            }

            for (final Method method : annotated) {
                parser.parse( method );
            }
            classInfo.put( clazz, subjectInfo );
        }
        return subjectInfo;
    }

    @Override
    public void parseClasses(final String packageName) throws MissingAnnotation {
        parseClasses( new String[] { packageName } );
    }

    @Override
    public void parseClasses(final String[] packageNames) throws MissingAnnotation {
        boolean hasErrors = false;
        for (final String pkg : packageNames) {

            for (final Class<?> c : getClasses( pkg )) {
                if (c.getAnnotation( Subject.class ) != null) {
                    try {
                        parse( c );
                    } catch (final MissingAnnotation e) {
                        EntityManagerImpl.LOG.warn( "Error processing {}: {}", c, e.getMessage() );
                        hasErrors = true;
                    }
                }
            }
        }
        if (hasErrors) {
            throw new MissingAnnotation(
                    String.format( "Unable to parse all %s See log for more details", Arrays.asList( packageNames ) ) );
        }
    }

    @Override
    public <T> T make(final Object source, final Class<T> primaryClass, final Class<?>... secondaryClasses)
            throws MissingAnnotation {
        Resource r = addInstanceProperties( getResource( source ), primaryClass );
        for (final Class<?> c : secondaryClasses) {
            r = addInstanceProperties( r, c );
        }
        return read( r, primaryClass, secondaryClasses );
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
    public <T> T read(final Object source, final Class<T> primaryClass, final Class<?>... secondaryClasses)
            throws MissingAnnotation, IllegalArgumentException {
        final List<Class<?>> classes = new ArrayList<>();
        SubjectInfoImpl subjectInfo;
        subjectInfo = parse( primaryClass );

        if (primaryClass.isInterface()) {
            classes.add( primaryClass );
        }
        for (final Class<?> cla : secondaryClasses) {
            if (!classes.contains( cla )) {
                parse( cla );
                classes.add( cla );
            }
        }
        if (!classes.contains( ResourceWrapper.class )) {
            classes.add( ResourceWrapper.class );
        }
        subjectInfo.validate( classes );
        final MethodInterceptor interceptor = new ResourceEntityProxy( this, getResource( source ), subjectInfo );
        final Class<?>[] classArray = new Class<?>[classes.size()];

        final Enhancer e = new Enhancer();
        if (!primaryClass.isInterface()) {
            e.setSuperclass( primaryClass );
        }
        e.setInterfaces( classes.toArray( classArray ) );
        e.setCallback( interceptor );
        return (T) e.create();
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
