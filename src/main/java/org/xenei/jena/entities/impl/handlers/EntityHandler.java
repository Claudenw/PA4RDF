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
package org.xenei.jena.entities.impl.handlers;

import com.hp.hpl.jena.rdf.model.RDFNode;

import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.impl.ObjectHandler;

/**
 * An ObjectHandler that creates EntityManager managed entitys from RDFResources and visa versa.
 * 
 */
public class EntityHandler implements ObjectHandler
{
	private Class<?> valueClass;
	private EntityManager entityManager;
	
	/**
	 * Constructor.
	 * @param entityManager The EntityManager to use.
	 * @param valueClass The Subject annotated class to create.
	 */
	public EntityHandler( EntityManager entityManager, Class<?> valueClass )
	{
		this.entityManager = entityManager;
		this.valueClass = valueClass;
	}

	/**
	 * Create an RDFNode representation for the subject class.
	 */
	public RDFNode createRDFNode( Object obj )
	{
		return ((ResourceWrapper) obj).getResource();
	}

	/**
	 * Use the entity manager to create an instance of the valueClass from the
	 * resource.  Effectively calls entityManager.read( node.asResource, valueClass )
	 * 
	 * @param node The RDFNode to wrap with the valueClass.
	 * @return the instance of the valueClass.
	 */
	public Object parseObject( RDFNode node )
	{
		return entityManager.read(node.asResource(), valueClass);
	}
	
	@Override
	public String toString() { return "EntityHandler"; }
	
	@Override
	public boolean equals( Object o )
	{
		return o instanceof EntityHandler;
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
}