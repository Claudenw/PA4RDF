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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.PredicateInfo;
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
	 * Adds getter methods from base class
	 * 
	 * @param methodName
	 * @param setterValueType
	 * @param parentPredicate
	 * @param multiAdd
	 * @return
	 * @throws MissingAnnotation
	 */
	private void addAssociatedMethod( final String methodName,
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
			// final EffectivePredicate predicate = getEffectivePredicate(m,
			// null,
			// null, parentPredicate, subjectInfo.getSubject());
			parse(m, predicate);
		}
		catch (final NoSuchMethodException e)
		{
			// do nothing
		}
	}

	// private Method addRemoverMethod( final Class<?> argType, final String
	// name,
	// final SubjectInfoImpl subjectInfo,
	// final EffectivePredicate parentPredicate ) throws MissingAnnotation
	// {
	// Method m = null;
	// try
	// {
	// if (argType == null)
	// {
	// m = subjectInfo.getImplementedClass().getMethod(name);
	// }
	// else
	// {
	// m = subjectInfo.getImplementedClass().getMethod(name,
	// new Class<?>[] { argType });
	// }
	// if (shouldProcess(m))
	// {
	// final EffectivePredicate predicate = getEffectivePredicate(m,
	// getParameterAnnotation(m), argType, parentPredicate,
	// subjectInfo.getSubject());
	// if (argType == null)
	// {
	// predicate.type = null;
	// }
	// subjectInfo.add(new PredicateInfoImpl(entityManager, predicate,
	// m.getName(), argType));
	// }
	// else
	// {
	// m = null;
	// }
	//
	// }
	// catch (final SecurityException e)
	// {
	// // acceptable error
	// }
	// catch (final NoSuchMethodException e)
	// {
	// // acceptable error
	// }
	// return m;
	// }

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
				// annotations = getAnnotationsIfProcessable(method);
				// if (annotations != null)
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
				// annotations = getAnnotationsIfProcessable(method);
				// if (annotations != null)
				// {
				parseRemover(method, predicate);
				// }
				break;

		}
	}

	private void parseConcreteMethod( final ActionType actionType,
			final Method method, final EffectivePredicate predicate )
			throws MissingAnnotation
	{
		// since we are concrete predicate must exist
		// final Predicate predicate = method.getAnnotation(Predicate.class);

		if (predicate.impl())
		{
			// annotated as implementing for effect only.
			// find the method that is being overridden.
			if (method.getDeclaringClass().equals(Object.class))
			{
				throw new MissingAnnotation(
						String.format(
								"A usable @Predicate annotated %s method was not found.",
								method.getName()));
			}

			final Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
			interfaces.addAll(Arrays.asList(method.getDeclaringClass()
					.getInterfaces()));

			for (Class<?> superClass = method.getDeclaringClass()
					.getSuperclass(); (superClass != Object.class)
					&& (superClass != null); superClass = superClass
					.getSuperclass())
			{
				interfaces.addAll(Arrays.asList(superClass.getInterfaces()));
				try
				{
					final Method m = superClass.getMethod(method.getName(),
							method.getParameterTypes());
					if (m.getAnnotation(Predicate.class) != null)
					{
						if (Modifier.isAbstract(m.getModifiers()))
						{
							parseAbstractMethod(actionType, m, predicate);
						}
						else
						{
							parseConcreteMethod(actionType, m, predicate);
						}
						return;
					}
				}
				catch (final NoSuchMethodException e)
				{
					// do nothing
				}
			}

			for (final Class<?> iface : interfaces)
			{
				try
				{
					final Method m = iface.getMethod(method.getName(),
							method.getParameterTypes());
					if (m.getAnnotation(Predicate.class) != null)
					{
						if (Modifier.isAbstract(m.getModifiers()))
						{
							parseAbstractMethod(actionType, m, predicate);
						}
						else
						{
							parseConcreteMethod(actionType, m, predicate);
						}
						return;
					}
				}
				catch (final NoSuchMethodException ignored)
				{
					// do nothing
				}
			}
			throw new MissingAnnotation(String.format(
					"A useable @Predicate annotated %s Method was not found.",
					method.getName()));

		}
		else
		{
			// does not implement another Predicate annotation so just process
			// it.
			parseAbstractMethod(actionType, method, predicate);
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
		/*
		 * else if (m.getName().startsWith("is"))
		 * {
		 * if (m.getParameterTypes().length == 0)
		 * {
		 * final Class<?> valueType = Boolean.class;
		 * final EffectivePredicate predicate = getEffectivePredicate(m,
		 * getParameterAnnotation(m), valueType, superPredicate,
		 * subjectInfo.getSubject());
		 * subjectInfo.add(new PredicateInfoImpl(entityManager, predicate,
		 * m.getName(), valueType));
		 * }
		 * }
		 */
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
						} else {
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

		/*
		 * 
		 * boolean okToProcess = TypeChecker.canBeSetFrom(predicate.type(),
		 * setterPredicate.type());
		 * if (!okToProcess)
		 * {
		 * // handle URI from RDFNode
		 * okToProcess = predicate.type().equals(URI.class)
		 * && (setterPredicate.type().equals(RDFNode.class));
		 * }
		 * if (!okToProcess)
		 * {
		 * // handle RDFNode from URI
		 * okToProcess = predicate.type().equals(URI.class)
		 * && (setterPredicate.type().equals(String.class));
		 * if (okToProcess)
		 * {
		 * if (method.getReturnType().equals(RDFNode.class))
		 * {
		 * predicate.type = RDFNode.class;
		 * }
		 * }
		 * }
		 */
		// if (okToProcess)
		{
			subjectInfo.add(new PredicateInfoImpl(entityManager, predicate,
					method.getName(), method.getReturnType()));
		}

	}

	private void parseRemover( final Method m,
			final EffectivePredicate predicate ) throws MissingAnnotation
	{
		final EffectivePredicate ep = new EffectivePredicate(m)
				.merge(predicate);

		if (m.getParameterTypes().length == 1)
		{
			ep.type = m.getParameterTypes()[0];
			// addRemoverMethod(
			// m.getParameterTypes()[0],
			// m.getName(),
			// subjectInfo,
			// getEffectivePredicate(m, m.getParameterAnnotations()[0],
			// m.getParameterTypes()[0], predicate,
			// subjectInfo.getSubject()));
		}
		else
		{
			ep.type = null;
		}
		// final Class<?> argType, final String name,
		// final SubjectInfoImpl subjectInfo,
		// final EffectivePredicate parentPredicate
		// Method m = null;
		// try
		// {
		// if (argType == null)
		// {
		// m = subjectInfo.getImplementedClass().getMethod(name);
		// }
		// else
		// {
		// m = subjectInfo.getImplementedClass().getMethod(name,
		// new Class<?>[] { argType });
		// }
		// if (shouldProcess(m))
		// {
		// final EffectivePredicate predicate = getEffectivePredicate(m,
		// getParameterAnnotation(m), argType, parentPredicate,
		// subjectInfo.getSubject());
		// if (argType == null)
		// {
		// predicate.type = null;
		// }
		// subjectInfo.add(new PredicateInfoImpl(entityManager, predicate,
		// m.getName(), argType));
		// }
		// else
		// {
		// m = null;
		// }
		//
		// }
		// catch (final SecurityException e)
		// {
		// // acceptable error
		// }
		// catch (final NoSuchMethodException e)
		// {
		// // acceptable error
		// }
		// return m;
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
			// getEffectivePredicate(m,
			// m.getParameterAnnotations()[0], parms[0], superPredicate,
			// subjectInfo.getSubject());

			final PredicateInfoImpl pi = new PredicateInfoImpl(entityManager,
					predicate, m.getName(), valueType);
			subjectInfo.add(pi);
			predicate.type = null;
			predicate.literalType = "";
			final boolean isMultiple = m.getName().startsWith("add");
			addAssociatedMethod("get" + subName, predicate, null);
			addAssociatedMethod("is" + subName, predicate, null);
			addAssociatedMethod("has" + subName, predicate,
					isMultiple ? valueType : null);
			addAssociatedMethod("remove" + subName, predicate,
					isMultiple ? valueType : null);

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
