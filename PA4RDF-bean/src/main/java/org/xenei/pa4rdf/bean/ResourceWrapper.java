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

package org.xenei.pa4rdf.bean;

import org.apache.jena.rdf.model.Resource;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;

/**
 * An interface to retrieve a resource from a subject annotated object.
 * 
 */
@Subject(namespace = "http://xenei.org/jena/entities/resourceWrapper#")
public interface ResourceWrapper {
	/**
	 * Get the wrapped resource.
	 * 
	 * @return The Jena model resource.
	 */
	@Predicate
	public Resource getResource();

	/**
	 * Get the EntityFactory that created or decorated this Object
	 * 
	 * @return The EntityFactory that created this object.
	 */
	@Predicate
	public EntityFactory getEntityFactory();

	/**
	 * Get the SubjectInfo for this object.
	 * 
	 * @return The SubjectInfo for this object.
	 */
	@Predicate
	public SubjectInfo getSubjectInfo();
}
