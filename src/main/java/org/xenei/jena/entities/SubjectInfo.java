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

import org.apache.jena.rdf.model.Property;

import java.lang.reflect.Method;
import java.util.Collection;

import org.xenei.jena.entities.annotations.Subject;

/**
 * Information about a subject annotated object.
 */
public interface SubjectInfo
{

	/**
	 * Get the class that was annotated with the Subject annotation. e.g. the
	 * class
	 * that this SubjectInfo is about.
	 * 
	 * @return The class that contains the annotation.
	 */
	public Class<?> getImplementedClass();

	/**
	 * Get the predicateInfo or return null if not found
	 * 
	 * @param method
	 *            The method to get information for.
	 * @return PredicateInfo or null.
	 */
	public PredicateInfo getPredicateInfo( Method method );

	/**
	 * Get the predicate info for a method.
	 * 
	 * @param methodName
	 *            The method name
	 * @param clazz
	 *            The return type (for a getter type method) or parameter type
	 *            (for a setter type method)
	 * @return the PredicateInfo for the method.
	 */
	public PredicateInfo getPredicateInfo( String methodName, Class<?> clazz );

	/**
	 * Get the Property that is the predicate for the method.
	 * 
	 * @param method
	 *            The method to lookup.
	 * @return A property or null if method was not found
	 */
	public Property getPredicateProperty( Method method );

	/**
	 * Get the Property that is the predicate for the method.
	 * 
	 * @param methodName
	 *            The method name to lookup.
	 * @return A property or null if function was not found
	 */
	public Property getPredicateProperty( String methodName );

	/**
	 * The the URI string of the predicate property.
	 * 
	 * @param method
	 *            The method to get the predicate for.
	 * @return the URI string for the predicate property.
	 */
	public String getPredicateUriStr( Method method );

	/**
	 * The the URI string of the predicate property.
	 * 
	 * @param methodName
	 *            The name of the method to get the predicate for.
	 * @return the URI string for the predicate property.
	 */
	public String getPredicateUriStr( String methodName );

	/**
	 * Get the @Subject annotation object for this subject.
	 * 
	 * @return the Subject annotation for this object.
	 */
	public Subject getSubject();

	/**
	 * Validate that this subject info implements all the methods for all the
	 * interfaces listed in iface.
	 * 
	 * @param iface
	 *            The list of interfaces that this subjectInfo should implement.
	 */
	public void validate( final Collection<Class<?>> iface );

}