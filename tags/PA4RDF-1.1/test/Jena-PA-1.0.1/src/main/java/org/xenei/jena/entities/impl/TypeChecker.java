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
package org.xenei.jena.entities.impl;


/**
 * A utility class to verify that an instance of one class can be set with an instance of another.
 */
public class TypeChecker
{

	/**
	 * True if "a" can be set from "b"
	 * @param a
	 * @param b
	 * @return true if an instance of a can be set with an instance of b
	 */
	public static boolean canBeSetFrom( Class<?> a, Class<?> b)
	{
		if (a != null && b != null)
		{
			if (a.isAssignableFrom(b))
			{
				return true;
			}
			Class<?> aPrime = TypeChecker.getPrimitiveClass( a );
			Class<?> bPrime = TypeChecker.getPrimitiveClass( b );
			if (aPrime != null && bPrime != null)
			{
				return aPrime.isAssignableFrom(bPrime);
			}
		}
		return false;
	}

	/**
	 * Get the primitive class that is equivalent to clazz.
	 * 
	 * If clazz is a primitive returns clazz.
	 * If clazz does not wrap a primitive returns null.
	 * @param clazz the class to unwrap.
	 * @return The primitive class or null.
	 */
	public static Class<?> getPrimitiveClass(Class<?> clazz)
	{
		if (clazz.isPrimitive())
		{
			return clazz;
		}
		try
		{
			return (Class<?>) clazz.getField("TYPE").get(null);
		}
		catch (IllegalArgumentException e)
		{
			new RuntimeException(e );
		}
		catch (SecurityException e)
		{
			new RuntimeException(e );
		}
		catch (IllegalAccessException e)
		{
			new RuntimeException(e );
		}
		catch (NoSuchFieldException e)
		{
			// expected in some cases
		}
		return null;
	}

}
