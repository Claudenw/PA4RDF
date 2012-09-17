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

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import org.apache.commons.lang3.StringUtils;
import org.xenei.jena.entities.impl.ObjectHandler;

/**
 * An ObjectHandler that encodes objects as Literals and visa versa.
 * 
 */
public class LiteralHandler implements ObjectHandler
{
	private RDFDatatype literalDatatype ;
	
	/**
	 * Create a LiteralHandler that converts literalDatatype objects.
	 * @param literalDatatype The RDFDatatype that will be converted to/from.
	 */
	public LiteralHandler( RDFDatatype literalDatatype )
	{
		this.literalDatatype=literalDatatype;
	}
	
	/**
	 * Convert the object to a Literal using the literalDatatype.
	 * @param obj the Object to convert.
	 * @return The literal representation of the object
	 * @throws An exception of object can not be parsed to a lexical form.
	 */
	public Literal createRDFNode( Object obj )
	{
		String lexicalForm = literalDatatype.unparse(obj);
		return ResourceFactory.createTypedLiteral( lexicalForm, literalDatatype );
	}

	/**
	 * Convert a Literal to an object using the Literaldata type to parse it.
	 * @param node The literal node.
	 * @return The parsed object
	 * @throws An exception if node is not a Liter that can be parsed by literalDatatype.
	 */
	public Object parseObject( RDFNode node )
	{
		return literalDatatype.parse( node.asLiteral().getLexicalForm());
	}
	
	public boolean isEmpty( Object obj )
	{
		if (obj != null)
		{
			return StringUtils.isBlank(createRDFNode( obj ).getLexicalForm());
		}
		return true;
	}
	
	@Override
	public String toString(){ return "LiteralHandler{"+literalDatatype+"}";}
	
	@Override
	public boolean equals( Object o)
	{
		if (o instanceof LiteralHandler)
		{
			return literalDatatype.equals( ((LiteralHandler)o).literalDatatype );
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
}