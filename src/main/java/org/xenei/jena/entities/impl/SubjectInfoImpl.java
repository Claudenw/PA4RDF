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
import com.hp.hpl.jena.rdf.model.Resource;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.proxy.exception.InvokerException;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

public class SubjectInfoImpl implements SubjectInfo
{
	private boolean validated;
	private final Class<?> implementedClass;
	private final Map<String, Map<ObjectHandler, PredicateInfo>> predicateInfo = new HashMap<String, Map<ObjectHandler, PredicateInfo>>();

	public SubjectInfoImpl( final Class<?> implementedClass )
	{
		this.implementedClass = implementedClass;
		this.validated = false;
	}
	
	/**
	 * Get the subject annotation for this class.
	 * @return
	 */
	public Subject getSubject() {
		return implementedClass.getAnnotation(Subject.class);
	}

	/**
	 * Add a predicate info to this subject.
	 * 
	 * @param pi
	 *            The predicateInfo to add.
	 */
	public void add( final PredicateInfoImpl pi )
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
	 * @see org.xenei.jena.entities.impl.SubjectInfo#getImplementedClass()
	 */
	@Override
	public Class<?> getImplementedClass()
	{
		return implementedClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xenei.jena.entities.impl.SubjectInfo#getPredicateInfo(java.lang.reflect
	 * .Method)
	 */
	@Override
	public PredicateInfo getPredicateInfo( final Method m )
	{
		if (m.isVarArgs() || (m.getParameterTypes().length > 1))
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

	private PredicateInfo getPredicateInfo( final String function )
	{
		final Map<ObjectHandler, PredicateInfo> map = predicateInfo
				.get(function);
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
	 * @see
	 * org.xenei.jena.entities.impl.SubjectInfo#getPredicateInfo(java.lang.String
	 * , java.lang.Class)
	 */
	@Override
	public PredicateInfo getPredicateInfo( final String function,
			final Class<?> clazz )
	{
		final Map<ObjectHandler, PredicateInfo> map = predicateInfo
				.get(function);
		if (map != null)
		{
			for (final PredicateInfo pi : map.values())
			{
				final Class<?> valueClass = pi.getValueClass();
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
							if ((clazz == null) || clazz.equals(void.class))
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
	 * Get the RDF Property for the method
	 * 
	 * @param m
	 *            The method to get the property for.
	 */
	@Override
	public Property getPredicateProperty( final Method m )
	{
		return getPredicateInfo(m).getProperty();
	}

	/**
	 * Get the RDF property for a method name.
	 * 
	 * @param methodName
	 *            The method name to locate
	 */
	@Override
	public Property getPredicateProperty( final String methodName )
	{
		return getPredicateInfo(methodName).getProperty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xenei.jena.entities.impl.SubjectInfo#getUri(java.lang.reflect.Method)
	 */
	@Override
	public String getPredicateUriStr( final Method m )
	{
		return getPredicateInfo(m).getUriString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.SubjectInfo#getUri(java.lang.String)
	 */
	@Override
	public String getPredicateUriStr( final String function )
	{
		return getPredicateInfo(function).getUriString();
	}

	/**
	 * Remove a predicate info from this subject.
	 * 
	 * @param m
	 *            the method to remove
	 */
	public void removePredicateInfo( final Method m )
	{
		if (m.isVarArgs() || (m.getParameterTypes().length > 1))
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

	/**
	 * Remove a predicate info from this subject.
	 * 
	 * @param function
	 *            The function to remove
	 * @param clazz
	 *            The class that is expected for the parameter (setter) or for
	 *            return (getter).
	 */
	public void removePredicateInfo( final String function, final Class<?> clazz )
	{
		final Map<ObjectHandler, PredicateInfo> map = predicateInfo
				.get(function);
		if (map != null)
		{
			map.remove(clazz);
			if (map.isEmpty())
			{
				predicateInfo.remove(function);
			}
		}
	}

	@Override
	public void validate( final Collection<Class<?>> iface )
	{
		if (validated)
		{
			return;
		}
		final Collection<Class<?>> clazz = new ArrayList<Class<?>>(iface);
		if (!implementedClass.isInterface())
		{
			clazz.add(implementedClass);
		}
		// clazz.remove(Resource.class);
		verifyNoNullMethods(clazz);
		validated = true;
	}

	/**
	 * Verify that all methods are implements or annotated with @Predicate
	 * 
	 * @param clazz
	 * @param subjectInfo
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	private void verifyNoNullMethods( final Collection<Class<?>> clazz )
	{

		for (final Class<?> c : clazz)
		{
			for (final Method method : c.getMethods())
			{
				if (getPredicateInfo(method) == null)
				{
					// if the method is not annotated check for instance
					if (method.getAnnotation(Predicate.class) == null)
					{
						Method m;
						try
						{
							m = implementedClass.getMethod(method.getName(),
									method.getParameterTypes());
						}
						catch (NoSuchMethodException | SecurityException e)
						{
							throw new InvokerException(
									String.format(
											"%s.%s (declared as %s.%2$s) is not implemented and does not have @Predicate annotation",
											c.getName(), method.getName(),
											method.getDeclaringClass()));
						}
						if (Modifier.isAbstract(m.getModifiers()))
						{
							if (m.getAnnotation(Predicate.class) == null)
							{
								// see if we implement Resource and is method is
								// from Resource
								if (Resource.class
										.isAssignableFrom(implementedClass))
								{
									try
									{
										Resource.class.getMethod(
												method.getName(),
												method.getParameterTypes());
										// method is found so all is ok
										return;
									}
									catch (final NoSuchMethodException expected)
									{
										// do nothing
									}
								}
								throw new InvokerException(
										String.format(
												"%s.%s (declared as %s.%2$s) is not implemented and does not have @Predicate annotation",
												c.getName(), method.getName(),
												method.getDeclaringClass()));

							}
						}
					}
				}
			}
		}

	}

}
