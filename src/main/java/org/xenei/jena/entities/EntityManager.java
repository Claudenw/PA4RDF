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

package org.xenei.jena.entities;

import java.util.Collection;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.rdf.model.AnonId;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.update.UpdateRequest;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.cache.SubjectTable;

/**
 * An Entity Manager to manage instances of entities annotated with the Subject
 * annotation
 *
 * Subject Annotation
 *
 * The EntityManager handles all client interactions with the Jena Model using
 * the annotated classes.
 *
 * @see org.xenei.jena.entities.annotations.Subject
 */
public interface EntityManager {

	/**
	 * Get the subject annotation for the class.
	 *
	 * Returns the Subject annotation if it exists for the class. Searches the
	 * super classes for the subject annotation. If no annotation is found in
	 * the super classes all the interfaces are searched and the first interface
	 * that contains the annotation is returned.
	 *
	 * @param clazz
	 *            The class to get the subject for.
	 * @return the Subject annotation or null if no annotation is found.
	 */
	public Subject getSubject(final Class<?> clazz);

	/**
	 * Get the SubjectInfo for the class.
	 *
	 * @param clazz
	 *            The class to get SubjectInfo for.
	 * @return The SubjectInfo
	 * @throws IllegalArgumentException
	 *             if clazz is not properly annotated with Subject annotations.
	 */
	public SubjectInfo getSubjectInfo(Class<?> clazz);

	/**
	 * Determine if target has all the properties required in the Subject( type
	 * ) annotation value.
	 *
	 * If target is not a Resource or ResourceWrapper returns false.
	 *
	 * @param target
	 *            The object to check.
	 * @param clazz
	 *            A Subject annotated class.
	 * @return true if the resource represented by target has all of the
	 *         required type predicates.
	 * @throws IllegalArgumentException
	 *             if clazz is not a Subject annotated class.
	 */
	public boolean isInstance(Object target, Class<?> clazz);

	/**
	 * Parses the the classes in a package (and subpackages) looking for Subject
	 * annotated classes.
	 *
	 * Classes are located using classLoader.getResource(packageAsPath)
	 *
	 * @see java.lang.ClassLoader#getResources(String)
	 *
	 *      If any Subject annotated classes are missing required annotations, a
	 *      log entry is written. If any Subject annotated classes faild parsing
	 *      a MissingAnnotation exception is thrown after all classes have been
	 *      processed.
	 *
	 * @param packageName
	 *            The name of the package to process
	 * @throws MissingAnnotation
	 *             if a subject annotated class has no predicate annotations.
	 */
	public void parseClasses(String packageName) throws MissingAnnotation;

	/**
	 * Parses the the classes in an array of packages (and subpackages) looking
	 * for Subject annotated classes.
	 *
	 * Classes are located using classLoader.getResource(packageAsPath)
	 *
	 *
	 * If any Subject annotated classes are missing required annotations, a log
	 * entry is written. If any Subject annotated classes faild parsing a
	 * MissingAnnotation exception is thrown after all classes have been
	 * processed.
	 *
	 * @param packageNames
	 *            The array of package names to process
	 * @throws MissingAnnotation
	 *             if a subject annotated class has not predicate annotations.
	 * @see java.lang.ClassLoader#getResources(String)
	 */
	public void parseClasses(String[] packageNames) throws MissingAnnotation;

	/**
	 * Make an instance of clazz from source.
	 *
	 * If source does not pass isInstance() check additional properties are
	 * added as required.
	 *
	 *
	 * @see #isInstance(Object, Class)
	 * @param source
	 *            Must either implement Resource or ResourceWrapper interfaces.
	 * @param primaryClass
	 *            The class of the object to be returned.
	 * @param secondaryClasses
	 *            A lost of other classes that are implemented.
	 * @param <T>
	 *            the instance type to return.
	 * @return primaryClass instance that also implements ResourceWrapper.
	 * @throws MissingAnnotation
	 *             if any of the classes do not have Subject annotations.
	 * @throws IllegalArgumentException
	 *             if source implements neither Resource nor ResourceWrapper.
	 */
	public <T> T make(Object source, Class<T> primaryClass,
			Class<?>... secondaryClasses) throws MissingAnnotation;

	/**
	 * Read an instance of clazz from source.
	 *
	 * Does not verify that the resource passes the isInstance() check.
	 *
	 * @see #isInstance(Object, Class)
	 * @param source
	 *            Must either implement Resource or ResourceWrapper interfaces.
	 * @param primaryClass
	 *            The class of the object to be returned.
	 * @param secondaryClasses
	 *            AList lost of other classes that are implemented.
	 * @param <T>
	 *            the instance type to return.
	 * @return primaryClass instance that also implements ResourceWrapper.
	 * @throws MissingAnnotation
	 *             if any of the classes do not have Subject annotations.
	 * @throws IllegalArgumentException
	 *             if source implements neither Resource nor ResourceWrapper.
	 */
	public <T> T read(Object source, Class<T> primaryClass,
			Class<?>... secondaryClasses) throws MissingAnnotation,
			IllegalArgumentException;

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
	 * @param <T>
	 *            the instance type to return.
	 * @return source for chaining
	 * @throws MissingAnnotation
	 *             if clazz does not have Subject annotations.
	 * @throws IllegalArgumentException
	 *             if source implements neither Resource nor ResourceWrapper.
	 */
	public <T> T addInstanceProperties(final T source, final Class<?> clazz)
			throws MissingAnnotation;

	/**
	 * Calls the target.setX predicate methods with the results of the
	 * source.getX predicate methods.
	 *
	 * @param source
	 *            The object to copy data from.
	 * @param target
	 *            The object to copy data to.
	 * @return The target object for chaining.
	 */
	public Object update(Object source, Object target);
	
	/**
	 * Reset the entity manager to its initial state.
	 * any pending updates are discarded. internal structures are reset to
	 * remote values.
	 */
	public void reset();
	
	/**
	 * Get all known subjects.
	 * @return The collection of SubjectInfo objects in this entitymanager
	 */
	public Collection<SubjectInfo> getSubjects();
	
	/**
	 * Register a listener to this EntityManager.
	 * 
	 * Listeners are held with weak references so if the
	 * listener can be garbage collected.
	 * 
	 * @param listener the listener to register.
	 */
	public void registerListener( Listener listener );
	
	/**
	 * Unregister a listener from this EntityManager.
	 * 
	 * @param listener the listener to remove.
	 */
	public void unregisterListener( Listener listener );
		
	/**
	 * Get the connection to the underlying RDF datastore.
	 * @return  the RDFConnection.
	 */
	//public RDFConnection getConnection();
	public void close();
	
	/**
	 * Get the model name this entity manager is working against.
	 * @return
	 */
	public Node getModelName();
	
	/**
	 * Get an Entity manager that executes on the specific named Model.
	 * 
	 * If the model does not exist in the dataset it is created.
	 * @param modelName the name to create.
	 * 
	 * @return the EntityManager for the specific model r null for default model.
	 */
	public EntityManager getNamedManager( Node modelName );
	
	/**
	 * Get the Entity manager that executes on the default model.
	 * @param modelName
	 * @return the EntityManager for the specific model.
	 */
	public EntityManager getDefaultManager();
	
	/**
	 * Execute a query.  The query will be modified to apply to the specific 
	 * graph represented by theis EntityManager.
	 * 
	 * @param query The query to execute.
	 * @return The query execution for the query
	 */
	public QueryExecution execute( Query query );
	
	/**
	 * Sync the system with the remote data store.
	 * If there are pending updates they will be executed and then
	 * the underlying graph synced.
	 */
	public void sync();
	
	/**
	 * Create resource with URI in the managed model.
	 * @param uri the URI to create the resource with.
	 * @return the resource in the managed model.
	 */
	public Resource createResource( String uri );
	
	/**
	 * Create resource with URI in the managed model.
	 * @param uri the URI to create the resource with.
	 * @param type the type for the resource.
	 * @return the resource in the managed model.
	 */
	public Resource createResource( String uri, Resource type );
	
	/**
	 * Create resource with specified anonymous ID in the managed model.
	 * @param id the Anonymous ID to create the resource with.
	 * @return the resource in the managed model.
	 */
	public Resource createResource( AnonId id );
	
	/**
	 * Create an anonymous resource in the managed model.
	 * @return an Anonymous resource.
	 */
	public Resource createResource();
	
	/**
	 * Return true if the managed datastore has the the resource
	 * @param uri the resource to check.
	 * @return true if the resource exists in the data store.
	 */
	public boolean hasResource( String uri );

	/**
	 * The listener interface.
	 *
	 */
	public static interface Listener {
		/**
		 * Called when a class is parsed.
		 * @param info the SubjectInfo for the parsed class.
		 */
		void onParseClass( SubjectInfo info );
	}
	
	
}