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

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;

/**
 * An interface to retrieve a resource from a subject annotated object.
 * 
 */
@Subject(namespace = "http://xenei.org/jena/entities/resourceWrapper#")
public interface ResourceWrapper
{
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
	
	public void setLiteral( Property property, Object value);
	public void addLiteral( Property property, Object value);
	public <T> T getLiteral( Property property, Class<T> clazz );
	
	public RDFNode getResource( Property property );	
	public void setResource( Property property, RDFNode resource );
	public void setResource( Property property, ResourceWrapper wrapper );
	public void addResource( Property property, RDFNode resource );
	public void addResource( Property property, ResourceWrapper wrapper );
	
	public <T> T getEntity( Property p, Class<T> clazz );
	public <T> T addEntity( Property p, Class<T> clazz );
	public <T> void setEntity( Property p, Class<T> clazz, T entity );
	
}
