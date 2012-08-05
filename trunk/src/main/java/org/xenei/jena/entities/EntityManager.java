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

/**
 * An Entity Manager to mange instances of entities annotated with the Subject annotation 
 * @see {@link org.xenei.jena.entities.annotations.Subject}
 * 
 * The EntityManager handles all client interactions with the Jena Model using the annotated classes.
 *
 */
public interface EntityManager
{

	/**
	 * Get the SubjectInfo for the class.
	 * 
	 * @param clazz
	 *            The class to get SubjectInfo for.
	 * @return The SubjectInfo
	 * @throws IllegalArgumentException
	 *             if clazz is not properly annotated with Subject annotations.
	 */
	public SubjectInfo getSubjectInfo( Class<?> clazz );

	/**
	 * Read an instance of clazz from source.
	 * 
	 * Does not verify that the resource passes the isInstance() check.
	 * 
	 * @see {@link isInstance()}
	 * @param source
	 *            Must either implement Resource or ResourceWrapper interfaces.
	 * @param clazz
	 * @return primaryClass instance that also implements ResourceWrapper.
	 * @throws MissingAnnotation
	 *             if any of the classes do not have Subject annotations.
	 * @throws IllegalArgumentException
	 *             if source implements neither Resource nor ResourceWrapper.
	 */
	public <T> T read( Object source, Class<T> primaryClass,
			Class<?>... secondaryClasses );

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
	public boolean isInstance( Object target, Class<?> clazz );

	/**
	 * Parses the the classes in a package (and subpackages) looking for Subject
	 * annotated classes.
	 * 
	 * Classes are located using classLoader.getResource(packageAsPath)
	 * 
	 * @See {@link classLoader.getResources(path) }
	 * 
	 *      If any Subject annotated classes are missing required annotations, a
	 *      log entry is written.
	 *      If any Subject annotated classes faild parsing a MissingAnnotation
	 *      exception is thrown after
	 *      all classes have been processed.
	 * 
	 * @param packageName
	 *            The name of the package to process
	 * @throws MissingAnnotation
	 */
	public void parseClasses( String packageName ) throws MissingAnnotation;

	/**
	 * Parses the the classes in an array of packages (and subpackages) looking
	 * for Subject annotated classes.
	 * 
	 * Classes are located using classLoader.getResource(packageAsPath)
	 * 
	 * @See {@link classLoader.getResources(path) }
	 * 
	 *      If any Subject annotated classes are missing required annotations, a
	 *      log entry is written.
	 *      If any Subject annotated classes faild parsing a MissingAnnotation
	 *      exception is thrown after
	 *      all classes have been processed.
	 * 
	 * @param packageNames
	 *            The array of package names to process
	 * @throws MissingAnnotation
	 */
	public void parseClasses( String[] packageNames ) throws MissingAnnotation;

	/**
	 * Calls the target.setX predicate methods with the results of the
	 * source.getX predicate
	 * methods.
	 * 
	 * @param source
	 *            The object to copy data from.
	 * @param target
	 *            The object to copy data to.
	 * @return The target object for chaining.
	 * @throws IllegalArgumentException
	 */
	public Object update( Object source, Object target )
			throws IllegalArgumentException;

}