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

import org.xenei.jena.entities.impl.ObjectHandler;

/**
 * An ObjectHandler that does not convert RDFNodes. 
 */
public class ResourceHandler implements ObjectHandler
{
	/**
	 * Convert an object to an RDFNode.
	 * @throws ClassCastExcepton if obj is not an instance of RDFNode  
	 */
	public RDFNode createRDFNode( Object obj )
	{
		return (RDFNode) obj;
	}

	/**
	 * Returns the argument
	 * @param node The RDFNode to parse/return
	 * @return The node parameter 
	 */
	public Object parseObject( RDFNode node )
	{
		return node;
	}
	
	@Override
	public String toString() { return "ResourceHandler"; }
	
	@Override
	public boolean equals( Object o )
	{
		return o instanceof ResourceHandler;
	}
	
	@Override
	public int hashCode() { return toString().hashCode(); }
}