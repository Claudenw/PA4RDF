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
package org.xenei.jena.entities.impl.datatype;

import com.hp.hpl.jena.datatypes.DatatypeFormatException;
import com.hp.hpl.jena.datatypes.xsd.impl.XSDBaseNumericType;

import org.apache.xerces.impl.dv.ValidatedInfo;

/**
 * A XSDBaseNumericType instance that converts a number into Long and visa
 * versa.
 * 
 * The standard Jena numeric data types covert longs to integer if they fall
 * below the
 * Integer.MAX_VALUE limit.
 */
public class LongDatatype extends XSDBaseNumericType
{

	public LongDatatype()
	{
		super("long", Long.class);
	}

	@Override
	public Object convertValidatedDataValue( final ValidatedInfo validatedInfo )
			throws DatatypeFormatException
	{
		return new Long(validatedInfo.normalizedValue);
	}

}