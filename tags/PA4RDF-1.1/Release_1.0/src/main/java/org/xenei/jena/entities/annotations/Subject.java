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

package org.xenei.jena.entities.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation that defines an entity class.
 * 
 * An entity class is a resource with known properties.
 * 
 * The types attribute are the URIs of the entity types as defined by
 * <code> &lt;entity&gt; a &lt;type&gt;</code>
 * 
 * The namespace attribute specifies the default namespace for properties.

 * 
 */
@Retention( RetentionPolicy.RUNTIME )
public @interface Subject
{
	/**
	 * The types that entities of this type must have to be valid.
	 * To be a valid entity of this type the resource must have RDFS:type properties with 
	 * objects with the URIs listed.
	 * @return the list of URIs (Strings) that the RDFS:type properties must have.
	 */
	String[] types() default {};

	/**
	 * The namespace for this subject.  Must be defined and not be an empty string.
	 * @return The namespace for the properties.
	 */
	String namespace() default "";
}
