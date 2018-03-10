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

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.arq.querybuilder.AskBuilder;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.rdf.model.AnonId;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.syntax.ElementNamedGraph;
import org.apache.jena.util.iterator.WrappedIterator;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.cache.CachingModel;
import org.xenei.jena.entities.cache.ModelInterceptor.Intercepted;
import org.xenei.pa4rdf.bean.ResourceWrapper;
import org.xenei.pa4rdf.bean.SubjectInfo;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;
import org.xenei.pa4rdf.bean.datatypes.CharDatatype;
import org.xenei.pa4rdf.bean.datatypes.CharacterDatatype;
import org.xenei.pa4rdf.bean.datatypes.LongDatatype;
import org.xenei.pa4rdf.bean.exceptions.MissingAnnotation;
import org.xenei.pa4rdf.bean.impl.ActionType;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * An implementation of the EntityManager interface.
 * 
 */
public class EntityManagerImpl implements EntityManager {
    private static Logger LOG = LoggerFactory.getLogger( EntityManagerImpl.class );

    private final Map<Class<?>, SubjectInfo> classInfo;

    private final List<WeakReference<Listener>> listeners;

    private final RDFConnection connection;

    protected final CachingModel cachingModel;

    private final Node modelName;
    
    private final ExecutorService execService;

    

    /**
     * Constructor.
     */
    public EntityManagerImpl(RDFConnection connection) {
        this.modelName = Quad.defaultGraphIRI;
        this.connection = connection;
        DelayedBlockingQueue queue = new DelayedBlockingQueue();
        this.execService = new ThreadPoolExecutor( 1, 5, 30, TimeUnit.SECONDS, queue.asRunnableQueue() );      
        listeners = Collections.synchronizedList( new ArrayList<WeakReference<Listener>>() );
        this.cachingModel = CachingModel.makeInstance( this );

        classInfo = new HashMap<Class<?>, SubjectInfo>() {

            /**
             * 
             */
            private static final long serialVersionUID = 8181650326448307726L;

            private void notifyListeners(SubjectInfo value) {
                synchronized (listeners) {
                    final Iterator<WeakReference<Listener>> iter = listeners.iterator();
                    while (iter.hasNext()) {
                        final WeakReference<Listener> listener = iter.next();
                        final Listener l = listener.get();
                        if (l == null) {
                            iter.remove();
                        } else {
                            l.onParseClass( value );
                        }
                    }
                }
            }

            @Override
            public SubjectInfo put(Class<?> key, SubjectInfo value) {
                notifyListeners( value );
                return super.put( key, value );
            }

            @Override
            public void putAll(Map<? extends Class<?>, ? extends SubjectInfo> m) {
                for (final SubjectInfo si : m.values()) {
                    notifyListeners( si );
                }
                super.putAll( m );
            }
        };
        try {
            parse( ResourceWrapper.class );
        } catch (final MissingAnnotation e) {
            throw new RuntimeException( e );
        }
    }

    /**
     * Constructor.
     */
    private EntityManagerImpl(EntityManagerImpl base, Node modelName) {
        this.modelName = modelName == null ? Quad.defaultGraphIRI : modelName;
        this.connection = base.connection;
        this.execService = base.execService;
        this.listeners = base.listeners;
        this.classInfo = base.classInfo;
        this.cachingModel = CachingModel.makeInstance( this );
    }
    
    public ExecutorService getExecutorService()
    {
        return execService;
    }

    @Override
    public RDFConnection getConnection() {
        return connection;
    }

    @Override
    public EntityManager getNamedManager(Node modelName) {
        // check for same name
        final String defaultName = Quad.defaultGraphIRI.getURI();
        final String modelNameStr = modelName.getURI();
        if (modelNameStr.equals( StringUtils.defaultIfBlank( getModelName().getURI(), defaultName ) )) {
            return this;
        }
        if (!defaultName.equals( modelNameStr ) && !Quad.unionGraph.getURI().equals( modelNameStr )) {
            final AskBuilder askBuilder = new AskBuilder().addGraph( modelName,
                    new AskBuilder().addWhere( "?s", "?p", "?o" ) );
            if (!connection.queryAsk( askBuilder.build() )) {
                connection.load( modelNameStr, ModelFactory.createDefaultModel() );
            }
        }
        return new EntityManagerImpl( this, modelName );
    }

    @Override
    public EntityManager getDefaultManager() {
        return getNamedManager( Quad.defaultGraphIRI );
    }

    @Override
    public Node getModelName() {
        return modelName;
    }

    @Override
    public void reset() {
        cachingModel.clear();
        classInfo.clear();
        registerTypes();
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
        final Map<String, Integer> addCount = new HashMap<String, Integer>();
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
        final Set<String> classes = new HashSet<String>();
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
        final Set<Class<?>> classes = new HashSet<Class<?>>();
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

    @Override
    public Resource createResource(String uri) {
        return cachingModel.createResource( uri );
    }

    @Override
    public Resource createResource(String uri, Resource type) {
        final Resource r = createResource( uri );
        r.addProperty( RDF.type, type );
        return r;
    }

    @Override
    public Resource createResource() {
        return cachingModel.createResource();
    }

    @Override
    public Resource createResource(AnonId id) {
        return cachingModel.createResource( id );
    }

    @Override
    public boolean hasResource(String uri) {
        final Resource r = cachingModel.createResource( uri );
        if (cachingModel.contains( r, null, (RDFNode) null )) {
            return true;
        }
        final AskBuilder builder = new AskBuilder().addWhere( r, null, null );
        return connection.queryAsk( builder.build() );
    }

    private Resource getResource(final Object target) throws IllegalArgumentException {
        Resource r = null;
        if (target instanceof ResourceWrapper) {
            r = ((ResourceWrapper) target).getResource();
        } else if (target instanceof Resource) {
            r = (Resource) target;
        } else {

            throw new IllegalArgumentException(
                    String.format( "%s implements neither Resource nor ResourceWrapper", target.getClass() ) );
        }

        /*
         * make sure the resource is loaded in the table make the resource point
         * to the caching model. bind the table to the resource to keep it from
         * being garbage collected.
         */
        return cachingModel.adopt( r );

    }

    @Override
    public Subject getSubject(final Class<?> clazz) {
        final Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
        for (Class<?> cls = clazz; cls != Object.class && cls != null; cls = cls.getSuperclass()) {
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
    /* package private*/ SubjectInfoImpl parse(final Class<?> clazz) throws MissingAnnotation {
        SubjectInfoImpl subjectInfo = (SubjectInfoImpl) classInfo.get( clazz );

        if (subjectInfo == null) {
            EntityManagerImpl.LOG.info( "Parsing {}", clazz );
            subjectInfo = new SubjectInfoImpl( clazz );

            final MethodParser parser = new MethodParser( this, subjectInfo, countAdders( clazz.getMethods() ) );

            /*
             * Parse getter annotated methods.  All others put into annotated list to parse later.
             * Parsing getters provides us with extra information about the methods (like the return type)
             */
            boolean foundAnnotation = false;
            final List<Method> annotated = new ArrayList<Method>();
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

            /*
             * Now parse all the annotated non-getter methods
             */
            for (final Method method : annotated) {
                parser.parse( method );
            }
            
            /* make sure all methods have all data */
            subjectInfo.normalize();
            
            /* save the result */
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
        final List<Class<?>> classes = new ArrayList<Class<?>>();
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
        final Resource resolvedResource = getResource( source );
        if (resolvedResource instanceof Intercepted) {
            classes.add( Intercepted.class );
        }
        final MethodInterceptor interceptor = new ResourceEntityProxy( this, resolvedResource, subjectInfo );
        final Class<?>[] classArray = new Class<?>[classes.size()];

        final Enhancer e = new Enhancer();
        if (!primaryClass.isInterface()) {
            e.setSuperclass( primaryClass );
        }
        e.setInterfaces( classes.toArray( classArray ) );
        e.setCallback( interceptor );
        return (T) e.create();
    }

    @Override
    public void close() {
        connection.close();
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
    public <T> T update(final Object source, final T target) {
        final List<Method> sMethods = Arrays.asList( source.getClass().getMethods() ).stream()
                .filter( m -> ActionType.GETTER.isA( m.getName() ) ).collect( Collectors.toList() );
        for (final Method tMethod : target.getClass().getMethods()) {
            if (ActionType.SETTER.isA( tMethod.getName() )) {
                final String tName = ActionType.SETTER.extractName( tMethod.getName() );
                final Class<?>[] params = tMethod.getParameterTypes();
                if (params.length == 1) {
                    final List<Method> candidates = sMethods.stream()
                            .filter( m -> ActionType.GETTER.extractName( m.getName() ).equals( tName ) )
                            .collect( Collectors.toList() );
                    for (final Method sMethod : candidates) {
                        if (sMethod.getReturnType().equals( params[0] ) && sMethod.getParameterTypes().length == 0) {
                            try {
                                final Object o = sMethod.invoke( source );
                                tMethod.invoke( target, o );
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                                System.out.println( "StackTrace: " + tMethod.toString() );
                                e.printStackTrace( System.out );
                                System.out.println( "End StackTrace" );
                                throw new IllegalStateException( String.format(
                                        "Unable to execute %s to and pass result to %s", sMethod, tMethod ), e );
                            }
                        }
                    }
                }
            }
        }

        return target;
    }

    @Override
    public Collection<SubjectInfo> getSubjects() {
        return classInfo.values();
    }

    @Override
    public void registerListener(Listener listener) {
        listeners.add( new WeakReference<Listener>( listener ) );
    }

    @Override
    public void unregisterListener(Listener listener) {
        synchronized (listeners) {
            final Iterator<WeakReference<Listener>> iter = listeners.iterator();
            while (iter.hasNext()) {
                final WeakReference<Listener> wl = iter.next();
                final Listener l = wl.get();
                if (l == null || l == listener) {
                    iter.remove();
                }
            }
        }
    }

    @Override
    public void sync() {
        cachingModel.sync();
    }

    @Override
    public Model getModel() {
        return cachingModel;
    }

    @Override
    public QueryExecution execute(Query query) {
        final Query q = query.cloneQuery();
        if (!modelName.equals( Quad.defaultGraphIRI )) {
            final ElementNamedGraph eng = new ElementNamedGraph( modelName, q.getQueryPattern() );
            q.setQueryPattern( eng );
        }
        return connection.query( q );
    }

    @Override
    public String toString() {
        return String.format( "EntityManager[%s]", modelName );
    }

    
    public interface DR extends Delayed, Runnable {
    
        public static DR make( Runnable r )
        {
            if (r instanceof DR)
            {
                return (DR)r;
            }
            Impl impl = new Impl(r);
            if (r instanceof Delayed)
            {
                impl.expires = ((Delayed) r).getDelay( TimeUnit.MILLISECONDS );
            }
            return impl;
        }
        
        public static DR make( Runnable r, long expires )
        {
            if (r instanceof DR)
            {
                if (expires == ((DR)r).getDelay( TimeUnit.MILLISECONDS ))
                {
                    return (DR)r;
                }
            }
            return new Impl(r, expires);
        }
        
        public class Impl implements DR {
       
            private Runnable r;
            private long expires;
            
            private Impl( Runnable r)
            {
                this(r,System.currentTimeMillis());
            }

            private Impl( Runnable r ,long expires)
            {
                this.r = r;
                this.expires = expires;
            }

            @Override
            public long getDelay(TimeUnit unit) {
                return unit.convert( expires-System.currentTimeMillis(), TimeUnit.MILLISECONDS );
            }
    
            @Override
            public int compareTo(Delayed o) {
                return Long.compare(  getDelay( TimeUnit.MILLISECONDS), o.getDelay(  TimeUnit.MILLISECONDS ) );
            }
    
            @Override
            public void run() {
                r.run();
            }
            
            @Override
            public boolean equals( Object o )
            {
                if (o instanceof Impl)
                {
                    return r.equals( ((Impl )o).equals(  r  ));
                }
                return false;
            }
            
            @Override
            public int hashCode() {
                return r.hashCode();
            }
                        
        }
    }
    
    
    public class DelayedBlockingQueue extends DelayQueue<DR>  {
        
        public BlockingQueue<Runnable> asRunnableQueue() {
            return new BlockingQueue<Runnable>(){
                DelayedBlockingQueue dbq = DelayedBlockingQueue.this;
                public boolean add(Runnable e) {
                    return dbq.add( DR.make( e ));
                }

                
                private List<DR> makeList( Collection<? extends Runnable> coll)
                {
                   return coll.stream().map( r -> DR.make( r ) ).collect( Collectors.toList() ) ;
                }
                
                
                public boolean addAll(Collection<? extends Runnable> arg0) {
                    return dbq.addAll(makeList( arg0 ) );
                }

                public void clear() {
                    dbq.clear();
                }

                public boolean contains(Object o) {
                    if (o instanceof Runnable) { 
                        return dbq.contains( DR.make( (Runnable)o ) );
                    } 
                    return false;
                }

                public boolean containsAll(Collection<?> arg0) {
                    List<DR> lst = new ArrayList<DR>();
                    for (Object o : arg0)
                    {
                        if (o instanceof Runnable)
                        {
                            lst.add(  DR.make(  (Runnable)o ) );
                        }
                        else {
                            return false;
                        }
                    }
                
                    return dbq.containsAll( lst );
                }

                public int drainTo(Collection<? super Runnable> c, int maxElements) {
                    return dbq.drainTo( c, maxElements );
                }

                public int drainTo(Collection<? super Runnable> c) {
                    return dbq.drainTo( c );
                }

                public Runnable element() {
                    return dbq.element();
                }                

                public void forEach(Consumer<? super Runnable> arg0) {
                    dbq.forEach( arg0 );
                }
               
                public boolean isEmpty() {
                    return dbq.isEmpty();
                }

                public Iterator<Runnable> iterator() {
                    return WrappedIterator.create( dbq.iterator() ).mapWith( dr -> (Runnable)dr );
                }

                public boolean offer(Runnable e, long timeout, TimeUnit unit) throws InterruptedException {
                    return dbq.offer( DR.make( e ), timeout, unit );
                }

                public boolean offer(Runnable e) {
                    return dbq.offer( DR.make( e ) );
                }

//                public Stream<Runnable> parallelStream() {
//                    return dbq.parallelStream().map(  dr -> (Runnable)dr );
//                }

                public Runnable peek() {
                    return dbq.peek();
                }

                public Runnable poll() {
                    return dbq.poll();
                }

                public Runnable poll(long timeout, TimeUnit unit) throws InterruptedException {
                    return dbq.poll( timeout, unit );
                }

                public void put(Runnable e) throws InterruptedException {
                    dbq.put( DR.make(e) );
                }

                public int remainingCapacity() {
                    return dbq.remainingCapacity();
                }

                public Runnable remove() {
                    return dbq.remove();
                }

                public boolean remove(Object o) {
                    if (o instanceof Runnable)
                    {
                        return dbq.remove( DR.make(  (Runnable)o) );
                    }
                    return false;
                }

                public boolean removeAll(Collection<?> arg0) {
                    List<DR> lst = new ArrayList<DR>();
                    for (Object o : arg0)
                    {
                        if (o instanceof Runnable)
                        {
                            lst.add(  DR.make(  (Runnable)o ) );
                        }
                        
                    }               
                    return dbq.removeAll( lst );
                }

//                public boolean removeIf(java.util.function.Predicate<? super Runnable> arg0) {
//                    return dbq.removeIf( arg0 );
//                }

                public boolean retainAll(Collection<?> arg0) {
                    return dbq.retainAll( arg0 );
                }

                public int size() {
                    return dbq.size();
                }
               
//                public Stream<Runnable> stream() {
//                    return dbq.stream().map(  dr -> (Runnable) dr );
//                }

                public Runnable take() throws InterruptedException {
                    return dbq.take();
                }

                public Object[] toArray() {
                    return dbq.toArray();
                }

                public <T> T[] toArray(T[] arg0) {
                    return dbq.toArray( arg0 );
                }
                
        };
        
        
        
    }
        
    }
}
