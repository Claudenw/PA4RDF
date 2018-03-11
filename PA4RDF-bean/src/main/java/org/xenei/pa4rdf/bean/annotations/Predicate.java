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

package org.xenei.pa4rdf.bean.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation that modifies the standard info about a property.
 * <p>
 * the name attribute will specify the name of the property. for example:
 * <code>Iterator&lt;RDFNode&gt; getClassType()</code> would return a list of
 * <code>class</code>s in the RDF sense. But <code>getClass()</code> would
 * conflict with the java function. <code>@Predicate( class='class' )
 * Iterator&lt;RDFNode&gt; getClassType()</code> resolves the problem.
 * </p>
 * <p>
 * The namespace attribute specifies that the namespace of the attribute is not
 * the same as for the entity.
 * </p>
 * <p>
 * The upcase boolean attribute (default false) will upcase the first character
 * of the attribute name. This is the first character after the "get", "set" or
 * "is" prefix.
 * </p>
 * <p>
 * the type attribute is used when an iterator is returned. It specifies the
 * enclosed class type. default = <code>RDFNode.class</code> In the
 * <code>getClassType()</code> example above we would use the following:
 * <code>@Predicate( class='class', type=RDFNode.class )
 * Iterator&lt;RDFNode&gt; getClassType()</code>
 * </p>
 * <p>
 * NOTE: if <code>type</code> is set to <code>URI.class</code> the return value
 * is interpreted as a URI string.
 * </p>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Predicate
{
	/**
	 * If true empty strings are assumed to be null and are not inserted.
	 * 
	 * Default value = false
	 * 
	 * @return true if empty strings should be considered as nulls.
	 */
	boolean emptyIsNull() default false;

	/**
	 * Indicates that a method is an implementation of an abstract method to
	 * allow the class to be concrete while not providing a concrete
	 * implementation of the Predicate annotated methods.
	 * 
	 * @return true if the implementation should be overridden.
	 */
	boolean impl() default false;

	/**
	 * The name of the literal type or an empty string if not in use. If
	 * specified it is used in a call to typeMapper.getSafeTypeByName() to get
	 * the RDFDatatype used to parse and unparse literal values.
	 * 
	 * @return The name of the literal type or an empty string.
	 */
	String literalType() default "";

	/**
	 * The name of the predicate. This is the local name in RDF parlance. If not
	 * specified it defaults to the name of the function with the action prefix
	 * removed. @see{ @link org.xenei.jena.entities.impl.ActionType}.
	 * 
	 * The namespace may be specified as part of the name. In this case the
	 * namespace value need not be set.
	 * 
	 * @return the local name of the RDF predicate.
	 */
	String name() default "";

	/**
	 * The namespace for the predicate. If not specified defaults to the
	 * namespace for the subject that this predicate is part of. The namespace
	 * may be specified with this field or as part of the name field.
	 * 
	 * @return The namespace portion of the RDF predicate.
	 */
	String namespace() default "";

	/**
	 * The java object class that will be returned when the object is read from
	 * the RDF model.
	 * 
	 * @return The object class.
	 */
	Class<?> type() default UNSET.class;

	/**
	 * The java object class that is contained in the list when type is RDFList
	 * 
	 * @return The list contents type class.
	 */
	Class<?> internalType() default UNSET.class;

	/**
	 * determines if the local name should have the first character upper cased.
	 * If false (the default) the first character will be lower cased. If true,
	 * the first character will be upper cased.
	 * 
	 * @return true if the first character of the name should be upper cased.
	 */
	boolean upcase() default false;

	/**
	 * Method on object to call after the method is executed.
	 * 
	 * @return the name of the method to call.
	 */
	String postExec() default "";

	public static class UNSET
	{
	};
}
