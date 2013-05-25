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

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;

/**
 * An implementation of the EntityManager interface.
 * 
 */
public class MethodParser
{

	private static Logger LOG = LoggerFactory.getLogger(MethodParser.class);

	private final SubjectInfoImpl subjectInfo;
	private final Map<String, Integer> addCount;
	private final EntityManager entityManager;
	private final Stack<Method> parseStack;

	/**
	 * Constructor.
	 */
	public MethodParser( final EntityManager entityManager,
			final SubjectInfoImpl subjectInfo,
			final Map<String, Integer> addCount )
	{
		this.entityManager = entityManager;
		this.subjectInfo = subjectInfo;
		this.addCount = addCount;
		this.parseStack = new Stack<Method>();
	}

	/**
	 * Return the set of implemented abstract classes and interfacesin the order
	 * they were declared.
	 * 
	 * @param clazz
	 * @return The ordered Set.
	 */
	public Set<Class<?>> findAbstractss( final Class<?> clazz )
	{
		if (clazz.equals(Object.class))
		{
			return Collections.emptySet();
		}
		final Queue<Class<?>> queue = new LinkedList<Class<?>>();
		queue.offer(clazz);
		// order preserving set
		final Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();

		for (Class<?> cls = queue.poll(); cls != null; cls = queue.poll())
		{

			if (!cls.equals(Object.class))
			{
				if (Modifier.isAbstract(cls.getModifiers()))
				{
					interfaces.add(cls);
				}
				final Class<?> sc = cls.getSuperclass();
				if ((sc != null) && !interfaces.contains(sc)
						&& !queue.contains(sc))
				{
					queue.offer(sc);
				}
			}
			for (final Class<?> c : cls.getInterfaces())
			{
				if (!interfaces.contains(c) && !queue.contains(c))
				{
					// interfaces.add(c);
					queue.offer(c);
				}
			}

		}
		return interfaces;
	}

	private void findAssociatedMethod( final Set<Class<?>> abstracts,
			final EffectivePredicate ep, final String name,
			final Class<?> valueClass ) throws MissingAnnotation
	{
		final Class<?>[] cAry = valueClass == null ? new Class<?>[0]
				: new Class<?>[] { valueClass };
		final List<Method> lm = findMethod(abstracts, name, cAry);
		if (lm.size() > 0)
		{
			parse(lm.get(0), ep);
		}
	}

	private void findAssociatedURIMethod( final Set<Class<?>> abstracts,
			final EffectivePredicate ep, final String name )
			throws MissingAnnotation
	{
		final List<Method> lm = findMethod(abstracts, name,
				new Class<?>[] { String.class });
		for (final Method m2 : lm)
		{
			for (final Annotation a : m2.getParameterAnnotations()[0])
			{
				if (a instanceof URI)
				{
					parse(m2, ep);
					return;
				}
			}
		}
	}

	private void findExistential( final Set<Class<?>> abstracts,
			final Method method, final EffectivePredicate superPredicate )
			throws MissingAnnotation
	{
		for (final Method m : findMethod(abstracts, method))
		{
			// we only parse boolean results
			if (TypeChecker.canBeSetFrom(Boolean.class, m.getReturnType())
					&& (m.getParameterTypes().length <= 1))
			{
				final SubjectInfo si = entityManager.getSubjectInfo(m
						.getDeclaringClass());
				final PredicateInfoImpl pi = (PredicateInfoImpl) si
						.getPredicateInfo(m);
				if (pi != null)
				{
					subjectInfo.add(pi);
					return;
				}
			}
		}
	}

	private void findGetter( final Set<Class<?>> abstracts,
			final Method method, final EffectivePredicate superPredicate )
			throws MissingAnnotation
	{
		// predicate for getter method includes predicate infor for setter
		// method.

		for (final Method m : findMethod(abstracts, method))
		{
			final PredicateInfoImpl pi = (PredicateInfoImpl) parse(m,
					superPredicate);
			if (pi != null)
			{
				subjectInfo.add(pi);
				return;
			}
		}
	}

	/**
	 * Find a method in the list of classes
	 * 
	 * @param classSet
	 * @param methodName
	 * @return
	 */
	public List<Method> findMethod( final Set<Class<?>> classSet,
			final Method method )
	{
		return findMethod(classSet, method.getName(),
				method.getParameterTypes());
	}

	public List<Method> findMethod( final Set<Class<?>> classSet,
			final String methodName, final Class<?>[] aParams )
	{
		final List<Class<?>> lParams = Arrays.asList(aParams);
		final List<Method> lst = new ArrayList<Method>();
		for (final Class<?> c : classSet)
		{
			for (final Method m : c.getDeclaredMethods())
			{
				if (m.getName().equals(methodName)
						&& Modifier.isAbstract(m.getModifiers()))
				{

					final List<Class<?>> pl = Arrays.asList(m
							.getParameterTypes());
					if ((lParams.size() == pl.size())
							&& (Collections.indexOfSubList(pl, lParams) == 0))
					{
						lst.add(m);
					}
				}
			}
		}
		return lst;
	}

	private void findRemover( final Set<Class<?>> abstracts,
			final Method method, final EffectivePredicate predicate )
			throws MissingAnnotation
	{
		for (final Method m : findMethod(abstracts, method))
		{
			EffectivePredicate ep = new EffectivePredicate(m).merge( predicate );
			final PredicateInfoImpl pi = (PredicateInfoImpl) parse(m, ep);
			if (pi != null)
			{
				subjectInfo.add(pi);
				return;
			}
		}
	}

	/**
	 * Processes "setX" and "addX" functions.
	 * 
	 * @param subjectInfo
	 * @param m
	 * @param multiAdd
	 * @throws MissingAnnotation
	 */
	private void findSetter( final Set<Class<?>> abstracts,
			final Method method, final EffectivePredicate superPredicate,
			final boolean multiAdd ) throws MissingAnnotation
	{
		final String subName = ActionType.SETTER.extractName(method.getName());
		for (final Method m : findMethod(abstracts, method))
		{
			if (m.getAnnotation(Predicate.class) != null)
			{
				// found the predicate annotated instance
				final SubjectInfo si = entityManager.getSubjectInfo(m
						.getDeclaringClass());
				final PredicateInfoImpl pi = (PredicateInfoImpl) si
						.getPredicateInfo(m);
				subjectInfo.add(pi);
				
				// add the sub types here
				final boolean isMultiple = m.getName().startsWith("add");
				findAssociatedMethod(abstracts, pi.getEffectivePredicate(), "get" + subName, null);
				findAssociatedMethod(abstracts, pi.getEffectivePredicate(), "is" + subName, null);
				if (isMultiple)
				{
					findAssociatedMethod(abstracts, pi.getEffectivePredicate(), "has" + subName,
							pi.getValueClass());
					findAssociatedMethod(abstracts, pi.getEffectivePredicate(), "remove" + subName,
							pi.getValueClass());

					if (pi.getValueClass() == RDFNode.class)
					{
						// look for @URI annotated strings
						findAssociatedURIMethod(abstracts, pi.getEffectivePredicate(), "has" + subName);
						findAssociatedURIMethod(abstracts, pi.getEffectivePredicate(), "remove"
								+ subName);
					}
				}
				else
				{
					findAssociatedMethod(abstracts, pi.getEffectivePredicate(), "has" + subName, null);
					findAssociatedMethod(abstracts, pi.getEffectivePredicate(), "remove" + subName,
							null);
				}
				return;

			}
		}
		throw new MissingAnnotation(String.format(
				"Can not locate annotated %s from %s", method.getName(),
				method.getDeclaringClass()));

	}

	private Method getSetterMethod( final String nameSuffix )
	{
		// find the setter
		for (final Method testMth : subjectInfo.getImplementedClass()
				.getMethods())
		{
			if (testMth.getName().equals("set" + nameSuffix)
					|| testMth.getName().equals("add" + nameSuffix))
			{
				if ((testMth.getParameterTypes().length == 1)
						&& shouldProcess(testMth))
				{
					return testMth;
				}
			}
		}
		return null;
	}

	/**
	 * Parse the class if necessary.
	 * 
	 * The first time the class is seen it is parsed, after that a cached
	 * version is returned.
	 * 
	 * @param clazz
	 * @return The SubjectInfo for the class.
	 * @throws MissingAnnotation
	 */
	public PredicateInfo parse( final Method method,
			final EffectivePredicate predicate ) throws MissingAnnotation
	{
		PredicateInfo pi = subjectInfo.getPredicateInfo(method);

		// only process if we havn't yet.
		if (pi == null)
		{
			// only process abstract methods and does not have var args
			if (shouldProcess(method))
			{
				if (!parseStack.contains(method))
				{
					parseStack.push(method);

					try
					{
						final ActionType actionType = ActionType.parse(method
								.getName());
						if (Modifier.isAbstract(method.getModifiers()))
						{
							parseAbstractMethod(actionType, method, predicate);
						}
						else
						{
							parseConcreteMethod(actionType, method, predicate);
						}
						pi = subjectInfo.getPredicateInfo(method);

					}
					catch (final IllegalArgumentException e)
					{
						// expected when method is not an action method.
					}
					finally
					{

						final Method m2 = parseStack.pop();
						if (method != m2)
						{
							throw new IllegalStateException(
									String.format(
											"Parsing stack out of sync: expected %s got %s",
											method, m2));
						}
					}
				}
			}
		}
		return pi;
	}

	/**
	 * parses Predicate annotations and absctract methods.
	 * 
	 * @param subjectInfo
	 *            The subject Info to add data to.
	 * @param method
	 *            The method that is being processed
	 * @param addCount
	 *            The count of add methods.
	 * @throws MissingAnnotation
	 *             If an Predicate annotation is missing.
	 */
	private void parseAbstractMethod( final ActionType actionType,
			final Method method, final EffectivePredicate predicate )
			throws MissingAnnotation
	{

		// process "set" if only one arg and not varargs
		switch (actionType)
		{
			case SETTER:
				final Integer i = addCount.get(method.getName());
				if (i != null)
				{
					parseSetter(method, predicate, (i > 1));
				}
				break;

			case EXISTENTIAL:
				if (method.getParameterTypes().length <= 1)
				{
					parseExistential(method, predicate);
				}
				break;

			case GETTER:
				if (method.getParameterTypes().length == 0)
				{
					final String nameSuffix = actionType.extractName(method
							.getName());

					parseGetter(nameSuffix, method, predicate);
				}

				break;

			case REMOVER:
				parseRemover(method, predicate);
				break;

		}
	}

	/**
	 * Adds getter methods from base class
	 * 
	 * @param methodName
	 * @param setterValueType
	 * @param parentPredicate
	 * @param multiAdd
	 * @return
	 * @throws MissingAnnotation
	 */
	private void parseAssociatedMethod( final String methodName,
			final EffectivePredicate parentPredicate, final Class<?> argClass )
			throws MissingAnnotation
	{
		try
		{
			Method m;
			if (argClass == null)
			{
				m = subjectInfo.getImplementedClass().getMethod(methodName);
			}
			else
			{
				m = subjectInfo.getImplementedClass().getMethod(methodName,
						argClass);
			}
			final EffectivePredicate predicate = new EffectivePredicate(m)
					.merge(parentPredicate);
			parse(m, predicate);
		}
		catch (final NoSuchMethodException e)
		{
			// do nothing
		}
	}

	private void parseConcreteMethod( final ActionType actionType,
			final Method method, final EffectivePredicate predicate )
			throws MissingAnnotation
	{
		predicate.merge(method.getAnnotation(Predicate.class));
		if (predicate.impl())
		{
			parseImplMethod(actionType, method, predicate);
		}
		else
		{
			parseInterceptedMethod();
		}
	}

	private void parseExistential( final Method m,
			final EffectivePredicate superPredicate ) throws MissingAnnotation
	{
		// we only parse boolean results
		if (TypeChecker.canBeSetFrom(Boolean.class, m.getReturnType())
				&& (m.getParameterTypes().length <= 1))
		{
			final EffectivePredicate predicate = new EffectivePredicate(m)
					.merge(superPredicate);
			Class<?> valueType = m.getReturnType();

			if (predicate.isTypeNotSet())
			{
				if (m.getParameterTypes().length == 1)
				{
					predicate.type = m.getParameterTypes()[0];
				}
			}

			if (m.getParameterTypes().length == 1)
			{
				valueType = m.getParameterTypes()[0];
			}
			else
			{
				// nothing special
			}
			subjectInfo.add(new PredicateInfoImpl(entityManager, predicate, m
					.getName(), valueType));
		}
	}

	// FIXME need to handle isX();
	private void parseGetter( final String nameSuffix, final Method method,
			final EffectivePredicate superPredicate ) throws MissingAnnotation
	{
		// predicate for getter method includes predicate infor for setter
		// method.
		final boolean multiAdd = (addCount.get("set" + nameSuffix) == null ? 0
				: addCount.get("set" + nameSuffix)
						+ (addCount.get("add" + nameSuffix) == null ? 0
								: addCount.get("add" + nameSuffix))) > 1;

		final EffectivePredicate predicate = new EffectivePredicate(method)
				.merge(superPredicate);
		// ExtendedIterator or Collection return type
		if (method.getReturnType().equals(ExtendedIterator.class)
				|| Collection.class.isAssignableFrom(method.getReturnType()))
		{
			if (!multiAdd)
			{
				// returning a collection and we have a single add method.
				// so set the return type if not set.
				if (predicate.isTypeNotSet())
				{
					final Method setterMethod = getSetterMethod(nameSuffix);
					// we have a setter method so lets merge info
					if (setterMethod != null)
					{

						final EffectivePredicate ep = new EffectivePredicate(
								setterMethod);
						predicate.merge(ep);
						final PredicateInfo setterPI = parse(setterMethod, ep);
						if (setterPI == null)
						{
							throw new IllegalStateException(
									"Could not parse setter "
											+ setterMethod.getName());
						}
						if (predicate.isTypeNotSet())
						{
							predicate.type = setterPI.getValueClass();
						}
						else
						{
							if (predicate.type == URI.class)
							{
								predicate.type = RDFNode.class;
							}
						}
					}
				}
			}
			else
			{
				// returning a collection and multple add methods
				// so we must have a type set.
				if (predicate.isTypeNotSet())
				{
					throw new MissingAnnotation(
							String.format(
									"%s.%s must have a Predicate annotation to define type",
									subjectInfo.getImplementedClass(),
									method.getName()));
				}
			}
		}
		else
		{
			// returning a single type so set the type to the return type.
			if (predicate.isTypeNotSet())
			{
				predicate.type = method.getReturnType();
			}
		}
		subjectInfo.add(new PredicateInfoImpl(entityManager, predicate, method
				.getName(), method.getReturnType()));

	}

	private void parseImplMethod( final ActionType actionType,
			final Method method, final EffectivePredicate predicate )
			throws MissingAnnotation
	{
		final Set<Class<?>> interfaces = findAbstractss(method
				.getDeclaringClass());
		// process "set" if only one arg and not varargs
		switch (actionType)
		{
			case SETTER:
				final Integer i = addCount.get(method.getName());
				if (i != null)
				{
					findSetter(interfaces, method, predicate, (i > 1));
				}
				break;

			case EXISTENTIAL:
				if (method.getParameterTypes().length <= 1)
				{
					findExistential(interfaces, method, predicate);
				}
				break;

			case GETTER:
				if (method.getParameterTypes().length == 0)
				{
					findGetter(interfaces, method, predicate);
				}

				break;

			case REMOVER:
				findRemover(interfaces, method, predicate);
				break;

		}

	}

	private void parseInterceptedMethod()
	{
		throw new RuntimeException("Not IMPLEMENTED");
	}

	private void parseRemover( final Method m,
			final EffectivePredicate predicate ) throws MissingAnnotation
	{
		final EffectivePredicate ep = new EffectivePredicate(m)
				.merge(predicate);

		if (m.getParameterTypes().length == 1)
		{
			ep.type = m.getParameterTypes()[0];
		}
		else
		{
			ep.type = null;
		}
		subjectInfo.add(new PredicateInfoImpl(entityManager, predicate, m
				.getName(), ep.type));
	}

	/**
	 * Processes "setX" and "addX" functions.
	 * 
	 * @param subjectInfo
	 * @param m
	 * @param multiAdd
	 * @throws MissingAnnotation
	 */
	private void parseSetter( final Method m,
			final EffectivePredicate superPredicate, final boolean multiAdd )
			throws MissingAnnotation
	{

		final Class<?> parms[] = m.getParameterTypes();
		if (parms.length == 1)
		{
			final Class<?> valueType = parms[0];
			final String subName = ActionType.SETTER.extractName(m.getName()); // remove
																				// "set"
																				// or
																				// "add"
			final EffectivePredicate predicate = new EffectivePredicate(m)
					.merge(superPredicate);

			final PredicateInfoImpl pi = new PredicateInfoImpl(entityManager,
					predicate, m.getName(), valueType);
			subjectInfo.add(pi);
			predicate.type = null;
			predicate.literalType = "";
			final boolean isMultiple = m.getName().startsWith("add");
			parseAssociatedMethod("get" + subName, predicate, null);
			parseAssociatedMethod("is" + subName, predicate, null);
			if (isMultiple)
			{
				parseAssociatedMethod("has" + subName, predicate, valueType);
				parseAssociatedMethod("remove" + subName, predicate, valueType);
				if (valueType == RDFNode.class)
				{
					// look for @URI annotated strings
					try
					{
						final Method m2 = m.getDeclaringClass().getMethod(
								"has" + subName, String.class);
						for (final Annotation a : m2.getParameterAnnotations()[0])
						{
							if (a instanceof URI)
							{
								parseAssociatedMethod("has" + subName,
										predicate, String.class);
							}
						}
					}
					catch (final NoSuchMethodException acceptable)
					{
						// do nothing
					}
					try
					{
						final Method m2 = m.getDeclaringClass().getMethod(
								"remove" + subName, String.class);
						for (final Annotation a : m2.getParameterAnnotations()[0])
						{
							if (a instanceof URI)
							{
								parseAssociatedMethod("remove" + subName,
										predicate, String.class);
							}
						}
					}
					catch (final NoSuchMethodException acceptable)
					{
						// do nothing
					}
				}
			}
			else
			{
				parseAssociatedMethod("has" + subName, predicate, null);
				parseAssociatedMethod("remove" + subName, predicate, null);
			}

		}
	}

	/**
	 * Only process abstract methods or methods with Predicate annotations
	 * and only when either of those is not varArgs.
	 * 
	 * @param m
	 * @return
	 */
	private boolean shouldProcess( final Method m )
	{
		boolean retval = !m.isVarArgs();
		if (retval)
		{
			retval = Modifier.isAbstract(m.getModifiers())
					|| (m.getAnnotation(Predicate.class) != null);
		}
		return retval;
	}

}
