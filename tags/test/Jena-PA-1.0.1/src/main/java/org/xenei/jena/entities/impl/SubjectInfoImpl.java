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

import com.hp.hpl.jena.rdf.model.Property;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.SubjectInfo;

public class SubjectInfoImpl implements SubjectInfo
{
	private Class<?> implementedClass;
	private Map<String, Map<ObjectHandler, PredicateInfo>> predicateInfo = new HashMap<String, Map<ObjectHandler, PredicateInfo>>();

	public SubjectInfoImpl( Class<?> implementedClass )
	{
		this.implementedClass = implementedClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.SubjectInfo#getImplementedClass()
	 */
	public Class<?> getImplementedClass()
	{
		return implementedClass;
	}

	/**
	 * Add a predicate info to this subject.
	 * 
	 * @param pi
	 *            The predicateInfo to add.
	 */
	public void add( PredicateInfoImpl pi )
	{
		Map<ObjectHandler, PredicateInfo> map = predicateInfo.get(pi
				.getMethodName());
		if (map == null)
		{
			map = new HashMap<ObjectHandler, PredicateInfo>();
			predicateInfo.put(pi.getMethodName(), map);
		}

		map.put(pi.getObjectHandler(), pi);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xenei.jena.entities.impl.SubjectInfo#getPredicateInfo(java.lang.String
	 * , java.lang.Class)
	 */
	public PredicateInfo getPredicateInfo( String function, Class<?> clazz )
	{
		Map<ObjectHandler, PredicateInfo> map = predicateInfo.get(function);
		if (map != null)
		{
			for (PredicateInfo pi : map.values())
			{
				Class<?> valueClass = pi.getValueClass();
				switch (pi.getActionType())
				{
					case SETTER:
						if (TypeChecker.canBeSetFrom(valueClass, clazz))
						{
							return pi;
						}
						break;

					case GETTER:
						if (TypeChecker.canBeSetFrom(clazz, valueClass))
						{
							return pi;
						}
						break;

					case REMOVER:
					case EXISTENTIAL:
						if (valueClass != null)
						{
							// it needs an argument
							if (TypeChecker.canBeSetFrom(valueClass, clazz))
							{
								return pi;
							}
						}
						else
						{
							// it does not want an argument
							if (clazz == null || clazz.equals(void.class))
							{
								return pi;
							}
						}
						break;
				}
			}
		}
		return null;
	}

	/**
	 * Remove a predicate info from this subject.
	 * 
	 * @param function
	 *            The function to remove
	 * @param clazz
	 *            The class that is expected for the parameter (setter) or for
	 *            return (getter).
	 */
	public void removePredicateInfo( String function, Class<?> clazz )
	{
		Map<ObjectHandler, PredicateInfo> map = predicateInfo.get(function);
		if (map != null)
		{
			map.remove(clazz);
			if (map.isEmpty())
			{
				predicateInfo.remove(function);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xenei.jena.entities.impl.SubjectInfo#getPredicateInfo(java.lang.reflect
	 * .Method)
	 */
	public PredicateInfo getPredicateInfo( Method m )
	{
		if (m.isVarArgs() || m.getParameterTypes().length > 1)
		{
			return null;
		}
		if (m.getParameterTypes().length == 0)
		{
			// must be a getter or single value remove
			return getPredicateInfo(m.getName(), m.getReturnType());
		}
		else
		{
			return getPredicateInfo(m.getName(), m.getParameterTypes()[0]);
		}
	}

	/**
	 * Remove a predicate info from this subject.
	 * 
	 * @param m
	 *            the method to rermove
	 */
	public void removePredicateInfo( Method m )
	{
		if (m.isVarArgs() || m.getParameterTypes().length > 1)
		{
			return;
		}
		if (m.getParameterTypes().length == 0)
		{
			// must be a getter
			removePredicateInfo(m.getName(), m.getReturnType());
		}
		else
		{
			removePredicateInfo(m.getName(), m.getParameterTypes()[0]);
		}
	}

	private PredicateInfo getPredicateInfo( String function )
	{
		Map<ObjectHandler, PredicateInfo> map = predicateInfo.get(function);
		if (map == null)
		{
			throw new IllegalArgumentException(String.format(
					"Function %s not found", function));
		}
		if (map.values().isEmpty())
		{
			{
				throw new IllegalArgumentException(String.format(
						"Function %s not found", function));
			}
		}
		return map.values().iterator().next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.SubjectInfo#getUri(java.lang.String)
	 */
	public String getPredicateUriStr( String function )
	{
		return getPredicateInfo(function).getUriString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xenei.jena.entities.impl.SubjectInfo#getUri(java.lang.reflect.Method)
	 */
	public String getPredicateUriStr( Method m )
	{
		return getPredicateInfo(m).getUriString();
	}

	/**
	 * Get the RDF Property for the method
	 * 
	 * @param m
	 *            The method to get the property for.
	 */
	public Property getPredicateProperty( Method m )
	{
		return getPredicateInfo(m).getProperty();
	}

	/**
	 * Get the RDF property for a method name.
	 * 
	 * @param methodName
	 *            The method name to locate
	 */
	public Property getPredicateProperty( String methodName )
	{
		return getPredicateInfo(methodName).getProperty();
	}

}
