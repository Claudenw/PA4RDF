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
package org.xenei.pa4rdf.entityManager.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.arq.querybuilder.AskBuilder;
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
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.pa4rdf.bean.EntityFactory;
import org.xenei.pa4rdf.bean.Listener;
import org.xenei.pa4rdf.bean.ResourceWrapper;
import org.xenei.pa4rdf.bean.SubjectInfo;
import org.xenei.pa4rdf.bean.annotations.Subject;
import org.xenei.pa4rdf.bean.exceptions.MissingAnnotation;
import org.xenei.pa4rdf.bean.impl.ActionType;
import org.xenei.pa4rdf.bean.impl.FactoryImpl;
import org.xenei.pa4rdf.cache.DelayedBlockingQueue;
import org.xenei.pa4rdf.cache.QueryExecutor;
import org.xenei.pa4rdf.cache.impl.QueryExecutorImpl;
import org.xenei.pa4rdf.cache.model.CachingModel;
import org.xenei.pa4rdf.cache.model.ModelInterceptor;
import org.xenei.pa4rdf.entityManager.EntityManager;

/**
 * An implementation of the EntityManager interface.
 * 
 */
public class EntityManagerImpl implements EntityManager {


	private static Logger LOG = LoggerFactory.getLogger( EntityManagerImpl.class );

	private final EntityFactory factory;

	protected final CachingModel cachingModel;

	private final ExecutorService execService;

	private final QueryExecutor queryExecutor;

	/**
	 * Constructor.
	 */
	public EntityManagerImpl(RDFConnection connection) {
		this.factory = new FactoryImpl();
		this.queryExecutor = new QueryExecutorImpl( connection, Quad.defaultGraphIRI);
		final DelayedBlockingQueue queue = new DelayedBlockingQueue();
		this.execService = new ThreadPoolExecutor( 1, 5, 30, TimeUnit.SECONDS, queue.asRunnableQueue() );      
		final ModelInterceptor mi = new ModelInterceptor(queryExecutor, execService);
		this.cachingModel = CachingModel.makeInstance( mi, execService );

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
		this.queryExecutor = new QueryExecutorImpl( base.getConnection(), modelName );
		this.execService = base.execService;
		this.factory = base.factory;
		final ModelInterceptor mi = new ModelInterceptor(queryExecutor, execService);
		this.cachingModel = CachingModel.makeInstance( mi, execService );
	}

	
	
	public ExecutorService getExecutorService()
	{
		return execService;
	}

	@Override
	public RDFConnection getConnection() {
		return queryExecutor.getConnection();
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
			if (!queryExecutor.getConnection().queryAsk( askBuilder.build() )) {
				queryExecutor.getConnection().load( modelNameStr, ModelFactory.createDefaultModel() );
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
		return queryExecutor.getModelName();
	}

	@Override
	public void reset() {
		cachingModel.clear();
		factory.reset_();
		try {
			parse( ResourceWrapper.class );
		} catch (final MissingAnnotation e) {
			throw new RuntimeException( e );
		}

	}



	@Override
	public void registerListener(Listener listener)
	{
		factory.registerListener(listener);
	}

	@Override
	public void unregisterListener(Listener listener)
	{
		factory.unregisterListener(listener);
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
		return queryExecutor.getConnection().queryAsk( builder.build() );
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
	//
	//    @Override
	//    public <T> T make(final Object source, final Class<T> primaryClass, final Class<?>... secondaryClasses)
	//            throws MissingAnnotation {
	//        Resource r = addInstanceProperties( getResource( source ), primaryClass );
	//        for (final Class<?> c : secondaryClasses) {
	//            r = addInstanceProperties( r, c );
	//        }
	//        return read( r, primaryClass, secondaryClasses );
	//    }

	//    /*
	//     * (non-Javadoc)
	//     * 
	//     * @see
	//     * org.xenei.jena.entities.impl.EntityManager#read(org.apache.jena.rdf.model
	//     * .Resource, java.lang.Class, java.lang.Class)
	//     */
	//    @Override
	//    @SuppressWarnings("unchecked")
	//    public <T> T read(final Object source, final Class<T> primaryClass, final Class<?>... secondaryClasses)
	//            throws MissingAnnotation, IllegalArgumentException {
	//        final List<Class<?>> classes = new ArrayList<Class<?>>();
	//        SubjectInfoImpl subjectInfo;
	//        subjectInfo = parse( primaryClass );
	//
	//        if (primaryClass.isInterface()) {
	//            classes.add( primaryClass );
	//        }
	//        for (final Class<?> cla : secondaryClasses) {
	//            if (!classes.contains( cla )) {
	//                parse( cla );
	//                classes.add( cla );
	//            }
	//        }
	//        if (!classes.contains( ResourceWrapper.class )) {
	//            classes.add( ResourceWrapper.class );
	//        }
	//        final Resource resolvedResource = getResource( source );
	//        if (resolvedResource instanceof Intercepted) {
	//            classes.add( Intercepted.class );
	//        }
	//        final MethodInterceptor interceptor = new ResourceEntityProxy( this, resolvedResource, subjectInfo );
	//        final Class<?>[] classArray = new Class<?>[classes.size()];
	//
	//        final Enhancer e = new Enhancer();
	//        if (!primaryClass.isInterface()) {
	//            e.setSuperclass( primaryClass );
	//        }
	//        e.setInterfaces( classes.toArray( classArray ) );
	//        e.setCallback( interceptor );
	//        return (T) e.create();
	//    }

	@Override
	public void close() {
		queryExecutor.getConnection().close();
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
	public void sync() {
		cachingModel.sync();
	}

	@Override
	public void sync( Object resource ) {
		cachingModel.add( getResource(resource).getModel());
		cachingModel.sync();
	}

	@Override
	public Model getModel() {
		return cachingModel;
	}


	@Override
	public String toString() {
		return String.format( "EntityManager[%s]", queryExecutor.getModelName() );
	}

	@Override
	public <T> T makeInstance(Object source, Class<T> clazz)
			throws MissingAnnotation
	{
		return factory.makeInstance(getResource( source ), clazz);
	}

	@Override
	public SubjectInfo parse(Class<?> clazz) throws MissingAnnotation
	{
		return factory.parse( clazz );
	}

	@Override
	public void reset_()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public QueryExecution execute(Query query)
	{
		return queryExecutor.execute(query);
	}

	@Override
	public Collection<SubjectInfo> getSubjects()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
