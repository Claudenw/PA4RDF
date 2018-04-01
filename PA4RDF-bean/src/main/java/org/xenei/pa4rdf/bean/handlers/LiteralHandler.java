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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.xenei.pa4rdf.bean.impl.TypeChecker;

/**
 * An ObjectHandler that encodes objects as Literals and visa versa.
 * 
 */
public class LiteralHandler extends AbstractObjectHandler
{
	private final RDFDatatype literalDatatype;

	/**
	 * Create a LiteralHandler that converts literalDatatype objects.
	 * 
	 * @param literalDatatype
	 *            The RDFDatatype that will be converted to/from.
	 */
	public LiteralHandler(final RDFDatatype literalDatatype)
	{
		this.literalDatatype = literalDatatype;
	}

	/**
	 * Convert the object to a Literal using the literalDatatype.
	 * 
	 * @param obj
	 *            the Object to convert.
	 * @return The literal representation of the object.
	 */
	@Override
	public Literal createRDFNode(final Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		final String lexicalForm = literalDatatype.unparse(obj);
		return ResourceFactory.createTypedLiteral(lexicalForm, literalDatatype);
	}

	private Class<?> getReducedClass()
	{
		final Class<?> pType = TypeChecker
				.getPrimitiveClass(literalDatatype.getJavaClass());
		return (pType == null) ? literalDatatype.getJavaClass() : pType;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (o instanceof LiteralHandler)
		{
			final Class<?> oType = ((LiteralHandler) o).getReducedClass();
			return getReducedClass().equals(oType) && (literalDatatype.getURI()
					.equals(((LiteralHandler) o).literalDatatype.getURI()));
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return getReducedClass().hashCode();
	}

	@Override
	public boolean isEmpty(final Object obj)
	{
		if (obj != null)
		{
			return StringUtils.isBlank(createRDFNode(obj).getLexicalForm());
		}
		return true;
	}

	/**
	 * Convert a Literal to an object using the Literaldata type to parse it.
	 * 
	 * @param node
	 *            The literal node.
	 * @return The parsed object
	 * @throws DatatypeFormatException
	 *             if node is not a Liter that can be parsed by literalDatatype.
	 */
	@Override
	public Object parseObject(final RDFNode node) throws DatatypeFormatException
	{
		String lexForm = node.asLiteral().getLexicalForm();
		if (literalDatatype.getJavaClass() == BigInteger.class)
		{
			return new BigInteger( lexForm );
		}
		if (literalDatatype.getJavaClass() == BigDecimal.class)
		{
			return new BigDecimal(lexForm);
		}
		return literalDatatype.parse( lexForm );
	}

	@Override
	public String toString()
	{
		return "LiteralHandler{" + literalDatatype + "}";
	}

}