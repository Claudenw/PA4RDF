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
package org.xenei.pa4rdf.bean.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;

/**
 * The interface for the internal ObjectHandler.
 * 
 * The handler will create the RDFNode from the object passed to the setter
 * method, and will create the object to be returned for the getter method.
 * 
 * In general X.equals( parseObject( createRDFNode( X ))) should hold true as
 * should X.equals( createRDFNode( parseObject( X ))).
 */
public interface ObjectHandler {
	/**
	 * Parse the object into an RDFNode representation.
	 * 
	 * @param obj
	 *            The object to represent
	 * @return The RDFNode representation
	 */
	RDFNode createRDFNode(Object obj);

	/**
	 * Returns true if the object is considered empty.
	 * 
	 * @param obj
	 *            the object that would be passed to createRDFNode
	 * @return true if the object is a empty.
	 */
	boolean isEmpty(Object obj);

	/**
	 * Parse the RDF node into an object.
	 * 
	 * @param node
	 *            The RDFNode to be unparsed.
	 * @return The Object from the unparsed RDFNode.
	 */
	Object parseObject(RDFNode node);

	/**
	 * Perform any actions to remove the node.
	 * 
	 * @param stmt
	 *            The statement to evaluate
	 * @param value
	 *            the RDFNode value to remove.
	 */
	void removeObject(Statement stmt, RDFNode value);

	@SuppressWarnings("unchecked")
	default Collection<RDFNode> asCollection( boolean emptyIsNull, Object obj ) {
		Collection<Object> objs = null;
		if (obj == null)
		{
			return Collections.emptyList();
		}
		if (obj instanceof Collection)
		{
			objs = (Collection<Object>) obj;
		} else if (obj.getClass().isArray())
		{
			objs = new ArrayList<Object>(  Arrays.asList( obj ) );
		} else {
			objs = new ArrayList<Object>();
			if (isEmpty(obj) && emptyIsNull)
			{
				objs.add(null);
			}
			else 
			{
				objs.add(  obj  );
			}
		}
		return objs.stream().map( o -> createRDFNode(o)).collect(  Collectors.toList() );
	}

}