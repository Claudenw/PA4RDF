/*
 * Copyright 2012, XENEI.com)
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.rdf.model.Property;
import org.xenei.pa4rdf.bean.PredicateInfo;
import org.xenei.pa4rdf.bean.SubjectInfo;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;
import org.xenei.pa4rdf.bean.annotations.URI;

public class SubjectInfoImpl implements SubjectInfo
{

	private final static Log LOG = LogFactory.getLog(SubjectInfoImpl.class);
	/**
	 * The class that we parsed.
	 */
	private final Class<?> implementedClass;
	/**
	 * The predicates for the parsed class
	 */
	private final PredicateMap predicateInfo = new PredicateMap();

	public SubjectInfoImpl(final Class<?> implementedClass)
	{
		this.implementedClass = implementedClass;
	}

	public boolean isMagicBean()
	{
		return SubjectInfo.isMagicBean(implementedClass);
	}

	@Override
	public String toString()
	{
		return String.format("SubjectInfo[%s]", implementedClass);
	}

	/**
	 * Add a predicate info to this subject.
	 * 
	 * PredicateInfo are stored in a multi-dimentional map first by
	 * argument/return type and then by method name.
	 * 
	 * @param pi
	 *            The predicateInfo to add.
	 */
	public void add(final PredicateInfo pi)
	{
		if (pi == null)
		{
			throw new IllegalArgumentException("PredicateInfo may not be null");
		}
		Map<Class<?>, PredicateInfo> map = predicateInfo
				.get(pi.getMethodName());
		if (map == null)
		{
			map = new HashMap<Class<?>, PredicateInfo>();
			predicateInfo.put(pi.getMethodName(), map);
		}

		Class<?> idx = null;
		switch (pi.getActionType())
		{
			case GETTER:
			case SETTER:
				idx = pi.getValueClass();
				break;

			case REMOVER:
			case EXISTENTIAL:
				if (URI.class.equals(pi.getEffectivePredicate().type()))
				{
					idx = String.class;
				} else
				{
					idx = pi.getEffectivePredicate().type();
				}
				break;
		}

		map.put(idx, pi);
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
	 * @see org.xenei.jena.entities.impl.SubjectInfo#getPredicateInfo(java.lang.
	 * reflect .Method)
	 */
	@Override
	public PredicateInfo getPredicateInfo(final Method m)
	{
		final EffectivePredicate ep = new EffectivePredicate(m);
		// verify that the method params match for type.
		if (ep.actionType().isType(m))
		{
			switch (ep.actionType())
			{
				case SETTER:
					return getPredicateInfo(m.getName(),
							m.getParameterTypes()[0]);
				case GETTER:
					return getPredicateInfo(m.getName(), m.getReturnType());
				case EXISTENTIAL:
				case REMOVER:
					return getPredicateInfo(m.getName(), ep.type());
			}
		}
		return null;
	}

	private PredicateInfo getPredicateInfo(final String function)
	{
		final Map<Class<?>, PredicateInfo> map = predicateInfo.get(function);
		if (map == null)
		{
			throw new IllegalArgumentException(
					String.format("Function %s not found", function));
		}
		if (map.values().isEmpty())
		{
			{
				throw new IllegalArgumentException(
						String.format("Function %s not found", function));
			}
		}
		return map.values().iterator().next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.SubjectInfo#getPredicateInfo(java.lang.
	 * String , java.lang.Class)
	 */
	@Override
	public PredicateInfo getPredicateInfo(final String function,
			final Class<?> clazz)
	{

		final Map<Class<?>, PredicateInfo> map = predicateInfo.get(function);
		if (map != null)
		{
			Class<?> idx = clazz;
			if (clazz == null || clazz.equals(void.class))
			{
				idx = Predicate.UNSET.class;
			}
			if (URI.class.equals(clazz))
			{
				idx = String.class;
			}
			final PredicateInfo retval = map.get(idx);
			if (retval != null)
			{
				return retval;
			}
			for (final Map.Entry<Class<?>, PredicateInfo> entry : map
					.entrySet())
			{
				switch (entry.getValue().getActionType())
				{
					case REMOVER:
					case SETTER:
					case EXISTENTIAL:
						if (TypeChecker.canBeSetFrom(entry.getKey(), idx))
						{
							return entry.getValue();
						}
						break;

					case GETTER:
						if (TypeChecker.canBeSetFrom(idx, entry.getKey()))
						{
							return entry.getValue();
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
	public Property getPredicateProperty(final Method m)
	{
		final PredicateInfo pi = getPredicateInfo(m);
		return pi == null ? null : pi.getProperty();
	}

	/**
	 * Get the RDF property for a method name.
	 * 
	 * @param methodName
	 *            The method name to locate
	 */
	@Override
	public Property getPredicateProperty(final String methodName)
	{
		final PredicateInfo pi = getPredicateInfo(methodName);
		return pi == null ? null : pi.getProperty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xenei.jena.entities.impl.SubjectInfo#getUri(java.lang.reflect.Method)
	 */
	@Override
	public String getPredicateUriStr(final Method m)
	{
		final PredicateInfo pi = getPredicateInfo(m);
		return pi == null ? null : pi.getUriString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.SubjectInfo#getUri(java.lang.String)
	 */
	@Override
	public String getPredicateUriStr(final String function)
	{
		final PredicateInfo pi = getPredicateInfo(function);
		return pi == null ? null : pi.getUriString();
	}

	/**
	 * Get the subject annotation for this class.
	 * 
	 * @return The subject annotation.
	 */
	@Override
	public Subject getSubject()
	{
		return implementedClass.getAnnotation(Subject.class);
	}

	/**
	 * Remove a predicate info from this subject.
	 * 
	 * @param m
	 *            the method to remove
	 */
	public void removePredicateInfo(final Method m)
	{
		if (m.isVarArgs() || (m.getParameterTypes().length > 1))
		{
			return;
		}
		if (m.getParameterTypes().length == 0)
		{
			// must be a getter
			removePredicateInfo(m.getName(), m.getReturnType());
		} else
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
	public void removePredicateInfo(final String function, final Class<?> clazz)
	{
		final Map<Class<?>, PredicateInfo> map = predicateInfo.get(function);
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
	public Collection<PredicateInfo> getPredicates()
	{
		final Set<PredicateInfo> set = new HashSet<PredicateInfo>();
		for (final Map<Class<?>, PredicateInfo> m : predicateInfo.values())
		{
			set.addAll(m.values());
		}
		return set;
	}

	/**
	 * Normalize the predicate info objects based on the collections. Call after
	 * last method is added. This method scans the collection of predicate info
	 * objects and gathers the methods for a single predicate. It then
	 * normalizes the predicate info for each method so that the getter/setter
	 * specific information is available in the other methods.
	 */
	public void normalize()
	{
		final Map<String, List<PredicateInfo>> byPredicate = new HashMap<String, List<PredicateInfo>>();

		for (final String methodName : predicateInfo.keySet())
		{
			final ActionType actionType = ActionType.parse(methodName);
			final String key = actionType.extractName(methodName);
			List<PredicateInfo> lst = byPredicate.get(key);
			if (lst == null)
			{
				lst = new ArrayList<PredicateInfo>();
				byPredicate.put(key, lst);
			}
			lst.addAll(predicateInfo.get(methodName).values());
		}

		for (final List<PredicateInfo> lst : byPredicate.values())
		{
			if (lst.get(0).getEffectivePredicate().isCollection())
			{
				EffectivePredicate master = null;

				for (final PredicateInfo info : lst)
				{
					final Class<?> collectionType = info.getEffectivePredicate()
							.collectionType();
					if ((!Predicate.UNSET.class.equals(collectionType))
							&& collectionType != null
							&& !Collection.class.equals(collectionType))
					{
						master = info.getEffectivePredicate();
					}
				}
				if (master == null)
				{
					LOG.error("Unable to normalize: "
							+ lst.get(0).getMethodName());
				} else
				{
					for (final PredicateInfo info : lst)
					{
						info.getEffectivePredicate()
								.collectionType(master.collectionType());
					}
				}
			}
		}
	}

	private class PredicateMap
			extends HashMap<String, Map<Class<?>, PredicateInfo>>
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	};
}
