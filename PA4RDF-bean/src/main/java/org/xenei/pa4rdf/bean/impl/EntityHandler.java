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
package org.xenei.pa4rdf.bean.impl;

import org.apache.jena.rdf.model.RDFNode;
import org.xenei.pa4rdf.bean.EntityFactory;
import org.xenei.pa4rdf.bean.ResourceWrapper;
import org.xenei.pa4rdf.bean.exceptions.MissingAnnotation;
import org.xenei.pa4rdf.bean.handlers.AbstractObjectHandler;

/**
 * An ObjectHandler that creates EntityManager managed entities from
 * RDFResources and visa versa.
 * 
 */
public class EntityHandler extends AbstractObjectHandler {
	private final Class<?> valueClass;
	private final EntityFactory factory;

	/**
	 * Constructor.
	 * 
	 * @param entityManager
	 *            The EntityManager to use.
	 * @param valueClass
	 *            The Subject annotated class to create.
	 */
	public EntityHandler(final EntityFactory factory, final Class<?> valueClass) {
		this.factory = factory;
		this.valueClass = valueClass;
	}

	/**
	 * Create an RDFNode representation for the subject class.
	 */
	@Override
	public RDFNode createRDFNode(final Object obj) {
		return obj == null ? null : ((ResourceWrapper) obj).getResource();
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof EntityHandler)
		{
			return ((EntityHandler)o).valueClass.equals(  valueClass );
		}
		return false;
	}

	@Override
	public int hashCode() {
		return valueClass.hashCode();
	}

	@Override
	public boolean isEmpty(final Object obj) {
		return obj == null;
	}

	/**
	 * Use the entity manager to create an instance of the valueClass from the
	 * resource. Effectively calls entityManager.read( node.asResource,
	 * valueClass )
	 * 
	 * @param node
	 *            The RDFNode to wrap with the valueClass.
	 * @return the instance of the valueClass.
	 */
	@Override
	public Object parseObject(final RDFNode node) {
		try {
			return factory.makeInstance( node, valueClass );
		} catch (final MissingAnnotation e) {
			throw new RuntimeException( e );
		}
	}

	@Override
	public String toString() {
		return String.format( "EntityHandler[ %s ]", valueClass );
	}

}