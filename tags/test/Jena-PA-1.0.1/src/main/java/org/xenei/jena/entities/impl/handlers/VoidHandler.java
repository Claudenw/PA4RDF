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
 * An ObjectHandler that always returns null.
 */
public class VoidHandler implements ObjectHandler
{
	/**
	 * Convert the object to null
	 * @param obj The object to convert
	 * @return null
	 */
	public RDFNode createRDFNode( Object obj )
	{
		return null;
	}
	
	public boolean isEmpty( Object obj )
	{
		return true;
	}

	/**
	 * convert the node to a null
	 * @param node The node to convert.
	 * @return null.
	 */
	public Object parseObject( RDFNode node )
	{
		return null;
	}
	
	@Override
	public String toString() { return "VoidHandler"; }
	
	@Override
	public boolean equals( Object o )
	{
		return o instanceof VoidHandler;
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
}