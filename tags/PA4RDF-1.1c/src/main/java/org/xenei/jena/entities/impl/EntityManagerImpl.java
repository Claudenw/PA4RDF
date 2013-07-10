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

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.impl.Util;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDF;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.proxy.exception.InvokerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;

/**
 * An implementation of the EntityManager interface.
 * 
 */
public class EntityManagerImpl implements EntityManager
{

	private static Logger LOG = LoggerFactory
			.getLogger(EntityManagerImpl.class);

	private final Map<Class<?>, SubjectInfoImpl> classInfo = new HashMap<Class<?>, SubjectInfoImpl>();

	/**
	 * Constructor.
	 */
	public EntityManagerImpl()
	{
		try
		{
			parse(ResourceWrapper.class);
		}
		catch (final MissingAnnotation e)
		{
			throw new RuntimeException(e);
		}
	}

	private Method addGetterMethods( final String methodName,
			final SubjectInfoImpl subjectInfo, final Class<?> setterValueType,
			final EffectivePredicate parentPredicate, final boolean multiAdd,
			final Subject subject ) throws MissingAnnotation
	{
		Method m = null;
		try
		{
			// only get methods without arguments
			m = subjectInfo.getImplementedClass().getMethod(methodName,
					(Class<?>[]) null);

			if (shouldProcess(m))
			{
				final EffectivePredicate predicate = getEffectivePredicate(m,
						null, null, parentPredicate, subject);

				// ExtendedIterator or Collection return type
				if (m.getReturnType().equals(ExtendedIterator.class)
						|| Collection.class.isAssignableFrom(m.getReturnType()))
				{

					final Predicate declaredPredicate = m
							.getAnnotation(Predicate.class);
					if (declaredPredicate == null)
					{
						if (multiAdd)
						{

							throw new MissingAnnotation(
									String.format(
											"%s.%s must have a Predicate annotation to define type",
											subjectInfo.getImplementedClass(),
											methodName));
						}
						// return type is setter type after accounting for URI
						// type
						if (predicate.type.equals(URI.class))
						{
							predicate.type = RDFNode.class;
						}
					}
					else
					{
						predicate.type = declaredPredicate.type();
					}

				}

				boolean okToProcess = TypeChecker.canBeSetFrom(
						predicate.type(), setterValueType);
				if (!okToProcess)
				{
					// handle URI from RDFNode
					okToProcess = predicate.type().equals(URI.class)
							&& (setterValueType.equals(RDFNode.class));
				}
				if (!okToProcess)
				{
					// handle RDFNode from URI
					okToProcess = predicate.type().equals(URI.class)
							&& (setterValueType.equals(String.class));
					if (okToProcess)
					{
						if (m.getReturnType().equals(RDFNode.class))
						{
							predicate.type = RDFNode.class;
						}
					}
				}

				if (okToProcess)
				{
					subjectInfo.add(new PredicateInfoImpl(this, predicate,
							methodName, m.getReturnType()));
				}
				else
				{
					m = null;
				}
			}
			else
			{
				m = null;
			}
		}
		catch (final SecurityException e)
		{
			// acceptable response
		}
		catch (final NoSuchMethodException e)
		{
			// acceptable response
		}
		return m;
	}

	private Method addHasMethods( final String methodName,
			final SubjectInfoImpl subjectInfo, final Class<?> valueType,
			final EffectivePredicate parentPredicate, final Subject subject )
	{
		Method m = null;
		try
		{
			m = subjectInfo.getImplementedClass().getMethod(methodName,
					new Class<?>[] { valueType });
			if (shouldProcess(m))
			{
				subjectInfo.add(new PredicateInfoImpl(this,
						getEffectivePredicate(m, getParameterAnnotation(m),
								valueType, parentPredicate, subject),
						methodName, valueType));
			}
			else
			{
				m = null;
			}
		}
		catch (final SecurityException e)
		{
			// acceptable response
		}
		catch (final NoSuchMethodException e)
		{
			// acceptable response
		}
		catch (final MissingAnnotation e)
		{
			EntityManagerImpl.LOG.error(e.toString());
		}
		return m;
	}

	/**
	 * Read an instance of clazz from Resource r. If r does not have the
	 * required types as defined in the Subject annotation they will be added.
	 * 
	 * @param r
	 *            The Resource to verify.
	 * @param clazz
	 *            The Subject annotated class to verify against.
	 * @return r for chaining
	 */
	public Resource addInstanceProperties( final Resource r,
			final Class<?> clazz )
	{
		final Subject e = clazz.getAnnotation(Subject.class);
		final Model model = r.getModel(); // may be null;
		if (e != null)
		{
			for (final String type : e.types())
			{
				final Resource object = (model != null) ? model
						.createResource(type) : ResourceFactory
						.createResource(type);
				if (!r.hasProperty(RDF.type, object))
				{
					r.addProperty(RDF.type, object);
				}
			}
		}
		return r;
	}

	private Method addRemoverMethod( final Class<?> argType, final String name,
			final SubjectInfoImpl subjectInfo,
			final EffectivePredicate parentPredicate, final Subject subject )
			throws MissingAnnotation
	{
		Method m = null;
		try
		{
			if (argType == null)
			{
				m = subjectInfo.getImplementedClass().getMethod(name);
			}
			else
			{
				m = subjectInfo.getImplementedClass().getMethod(name,
						new Class<?>[] { argType });
			}
			if (shouldProcess(m))
			{
				final EffectivePredicate predicate = getEffectivePredicate(m,
						getParameterAnnotation(m), argType, parentPredicate,
						subject);
				if (argType == null)
				{
					predicate.type = null;
				}
				subjectInfo.add(new PredicateInfoImpl(this, predicate, m
						.getName(), argType));
			}
			else
			{
				m = null;
			}

		}
		catch (final SecurityException e)
		{
			// acceptable error
		}
		catch (final NoSuchMethodException e)
		{
			// acceptable error
		}
		return m;
	}

	private Map<String, Integer> countAdders( final Method[] methods )
	{
		final Map<String, Integer> addCount = new HashMap<String, Integer>();
		for (final Method m : methods)
		{
			if (isAdd(m))
			{
				Integer i = addCount.get(m.getName());
				if (i == null)
				{
					i = 1;
				}
				else
				{
					i = i + 1;
				}
				addCount.put(m.getName(), i);
			}
		}
		return addCount;
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * Adapted from http://snippets.dzone.com/posts/show/4831 and extended to
	 * support use of JAR files
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Set<String> findClasses( final String directory,
			final String packageName ) throws IOException
	{
		final Set<String> classes = new HashSet<String>();
		if (directory.startsWith("file:") && directory.contains("!"))
		{
			final String[] split = directory.split("!");
			final URL jar = new URL(split[0]);
			final ZipInputStream zip = new ZipInputStream(jar.openStream());
			ZipEntry entry = null;
			while ((entry = zip.getNextEntry()) != null)
			{
				if (entry.getName().endsWith(".class"))
				{
					final String className = entry.getName()
							.replaceAll("[$].*", "").replaceAll("[.]class", "")
							.replace('/', '.');
					classes.add(className);
				}
			}
		}
		final File dir = new File(directory);
		if (!dir.exists())
		{
			return classes;
		}
		final File[] files = dir.listFiles();
		for (final File file : files)
		{
			if (file.isDirectory())
			{
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file.getAbsolutePath(), packageName
						+ "." + file.getName()));
			}
			else if (file.getName().endsWith(".class"))
			{
				classes.add(packageName
						+ '.'
						+ file.getName().substring(0,
								file.getName().length() - 6));
			}
		}
		return classes;
	}

	/**
	 * Returns the list of annotations if this method has the Predicate
	 * annotation
	 * 
	 * @param subjectInfo
	 * @param m
	 * @return
	 */
	private Annotation[] getAnnotationsIfProcessable(
			final SubjectInfo subjectInfo, final Method m )
	{
		final Annotation[] annotations = m.getAnnotations();
		boolean process = false;
		for (final Annotation a : annotations)
		{
			if (a instanceof org.xenei.jena.entities.annotations.Predicate)
			{
				process = subjectInfo.getPredicateInfo(m) == null;
			}
		}
		return process ? annotations : null;
	}

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 * Adapted from http://snippets.dzone.com/posts/show/4831 and extended to
	 * support use of JAR files
	 * 
	 * @param packageName
	 *            The base package or class name
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private Collection<Class<?>> getClasses( final String packageName )
	{

		final ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		assert classLoader != null;
		final String path = packageName.replace('.', '/');
		Enumeration<URL> resources;
		try
		{
			resources = classLoader.getResources(path);
		}
		catch (final IOException e1)
		{
			EntityManagerImpl.LOG.error(e1.toString());
			return Collections.emptyList();
		}
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		if (resources.hasMoreElements())
		{
			while (resources.hasMoreElements())
			{
				final URL resource = resources.nextElement();
				try
				{
					for (final String clazz : findClasses(resource.getFile(),
							packageName))
					{
						try
						{
							classes.add(Class.forName(clazz));
						}
						catch (final ClassNotFoundException e)
						{
							EntityManagerImpl.LOG.warn(e.toString());
						}
						catch (final ExceptionInInitializerError e)
						{
							System.out.println("WHAT?");
						}
					}
				}
				catch (final IOException e)
				{
					EntityManagerImpl.LOG.warn(e.toString());
				}
			}
		}
		else
		{
			// there are no resources at that path so see if it is a class
			try
			{
				classes.add(Class.forName(packageName));
			}
			catch (final ClassNotFoundException e)
			{
				EntityManagerImpl.LOG.warn(
						"{} was neither a package name nor a class name",
						packageName);
			}
		}
		return classes;
	}

	private EffectivePredicate getEffectivePredicate( final Method m )
			throws MissingAnnotation
	{
		return getEffectivePredicate(m, null, null, null, null);
	}

	private EffectivePredicate getEffectivePredicate( final Method m,
			final Annotation[] paramAnnotations, final Class<?> returnType,
			final EffectivePredicate dflt, final Subject subject )
			throws MissingAnnotation
	{
		boolean typeSet = false;

		final EffectivePredicate retval = new EffectivePredicate().merge(
				m.getAnnotation(Predicate.class)).merge(dflt);
		if (StringUtils.isBlank(retval.namespace()) && (subject != null))
		{
			retval.namespace = subject.namespace();
		}
		if (StringUtils.isBlank(retval.namespace()))
		{
			retval.namespace = getMethodNamespace(m);

		}
		if (paramAnnotations != null)
		{
			for (final Annotation a : paramAnnotations)
			{
				if (a instanceof URI)
				{
					retval.type = URI.class;
					typeSet = true;
				}
			}
		}
		if (!typeSet && (returnType != null))
		{
			retval.type = returnType;
		}
		if (retval.namespace().length() == 0)
		{
			retval.namespace = getNamespace(m.getDeclaringClass());
		}
		if (retval.name().length() == 0)
		{
			final ActionType actionType = ActionType.parse(m.getName());
			retval.name = actionType.extractName(m.getName());
		}

		// check to see if local name includes namespace
		final Node n = Node.createURI(retval.name);
		final String localNamespace = n.getNameSpace();
		// Node.getNameSpace() always returns part of the name as the
		// namespace (i.e. it is never 0 length)
		if (Util.notNameChar(localNamespace.charAt(localNamespace.length() - 1)))
		{
			retval.namespace = localNamespace;
			retval.name = n.getLocalName();
		}

		if (retval.namespace().length() == 0)
		{
			throw new MissingAnnotation(
					String.format(
							"Namespace is not defined in property, entity or uriString for %s",
							m.toString()));
		}
		if (!Util.notNameChar(retval.namespace().charAt(
				retval.namespace().length() - 1)))
		{
			throw new MissingAnnotation(String.format(
					"Namespace (%s) ends with invalid character",
					retval.namespace()));
		}
		final String s = retval.name.substring(0, 1);
		if (retval.upcase())
		{
			retval.name = retval.name.replaceFirst(s, s.toUpperCase());
		}
		else
		{
			retval.name = retval.name.replaceFirst(s, s.toLowerCase());
		}

		final int split = Util.splitNamespace(retval.namespace()
				+ retval.name());
		if (split != retval.namespace().length())
		{
			throw new MissingAnnotation(
					String.format(
							"uriString (%s) has invalid characters in the localname part (%s)",
							retval.namespace() + retval.name(),
							retval.namespace() + retval.name().substring(split)));
		}
		return retval;
	}

	private EffectivePredicate getEffectivePredicate( final Method m,
			final Annotation[] paramAnnotations, final Class<?> returnType,
			final Subject subject ) throws MissingAnnotation
	{
		return getEffectivePredicate(m, paramAnnotations, returnType, null,
				subject);
	}

	/**
	 * Get the namespace from annotations on the methods that method overrides.
	 * 
	 * @param method
	 *            the method that overrides other methods.
	 * @return the namespace string or an empty string.
	 */
	private String getMethodNamespace( final Method method )
	{

		if (method.getDeclaringClass().equals(Object.class))
		{
			return "";
		}
		final Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
		interfaces.addAll(Arrays.asList(method.getDeclaringClass()
				.getInterfaces()));

		for (Class<?> superClass = method.getDeclaringClass().getSuperclass(); superClass != Object.class && superClass != null; superClass = superClass
				.getSuperclass())
		{
			interfaces.addAll(Arrays.asList(superClass.getInterfaces()));
			try
			{
				final Method m = superClass.getMethod(method.getName(),
						method.getParameterTypes());
				final Predicate p = m.getAnnotation(Predicate.class);
				if ((p != null) && StringUtils.isNotBlank(p.namespace()))
				{
					return p.namespace();
				}
				final Subject s = superClass.getAnnotation(Subject.class);
				if ((s != null) && StringUtils.isNotBlank(s.namespace()))
				{
					return s.namespace();
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
				final Predicate p = m.getAnnotation(Predicate.class);
				if ((p != null) && StringUtils.isNotBlank(p.namespace()))
				{
					return p.namespace();
				}
				final Subject s = iface.getAnnotation(Subject.class);
				if ((s != null) && StringUtils.isNotBlank(s.namespace()))
				{
					return s.namespace();
				}
			}
			catch (final NoSuchMethodException ignored)
			{
				// do nothing
			}
		}
		return "";

	}

	private String getNamespace( final Class<?> clazz )
	{
		final Subject e = clazz.getAnnotation(Subject.class);
		return e != null ? e.namespace() : "";
	}

	private Annotation[] getParameterAnnotation( final Method m )
	{
		return (m.getParameterAnnotations().length > 0) ? m
				.getParameterAnnotations()[0] : null;
	}

	private Resource getResource( final Object target )
	{
		if (target instanceof ResourceWrapper)
		{
			return ((ResourceWrapper) target).getResource();
		}
		if (target instanceof Resource)
		{
			return (Resource) target;
		}
		throw new IllegalArgumentException(String.format(
				"%s implements neither Resource nor ResourceWrapper",
				target.getClass()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xenei.jena.entities.impl.EntityManager#getSubjectInfo(java.lang.Class
	 * )
	 */
	@Override
	public SubjectInfo getSubjectInfo( final Class<?> clazz )
	{
		try
		{
			return parse(clazz);
		}
		catch (final MissingAnnotation e)
		{
			throw new IllegalArgumentException(e);
		}
	}

	private boolean isAdd( final Method m )
	{
		try
		{
			if (ActionType.parse(m.getName()) == ActionType.SETTER)
			{
				final Class<?> parms[] = m.getParameterTypes();
				return (parms != null) && (parms.length == 1);
			}
		}
		catch (final IllegalArgumentException expected)
		{
			// do nothing
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xenei.jena.entities.impl.EntityManager#isInstance(com.hp.hpl.jena
	 * .rdf.model.Resource, java.lang.Class)
	 */
	@Override
	public boolean isInstance( final Object target, final Class<?> clazz )
	{
		final Subject subject = clazz.getAnnotation(Subject.class);
		if (subject == null)
		{
			throw new IllegalArgumentException(String.format(
					"%s is not annotated as a Subject", clazz.getName()));
		}

		Resource r = null;
		try
		{
			r = getResource(target);
		}
		catch (final IllegalArgumentException e)
		{
			return false;
		}

		for (final String type : subject.types())
		{
			final Resource object = ResourceFactory.createResource(type);
			if (!r.hasProperty(RDF.type, object))
			{
				return false;
			}
		}

		return true;
	}

	private SubjectInfoImpl parse( final Class<?> clazz )
			throws MissingAnnotation
	{
		SubjectInfoImpl subjectInfo = classInfo.get(clazz);

		if (subjectInfo == null)
		{
			EntityManagerImpl.LOG.info("Parsing {}", clazz);
			subjectInfo = new SubjectInfoImpl(clazz);
			final Subject subject = clazz.getAnnotation(Subject.class);
			final Map<String, Integer> addCount = countAdders(clazz
					.getMethods());
			Annotation[] annotations;
			for (final Method m : clazz.getMethods())
			{

				// only process abstract methods and does not have var args
				if (shouldProcess(m))
				{
					try
					{
						final ActionType actionType = ActionType.parse(m
								.getName());
						// process "set" if only one arg and not varargs
						switch (actionType)
						{
							case SETTER:
								final Integer i = addCount.get(m.getName());
								if (i != null)
								{
									parseSetter(subjectInfo, m, (i > 1),
											subject);
								}
								break;

							case EXISTENTIAL:
								annotations = getAnnotationsIfProcessable(
										subjectInfo, m);
								if (annotations != null)
								{
									parseExistential(subjectInfo, m, subject);
								}
								break;

							case GETTER:
								annotations = getAnnotationsIfProcessable(
										subjectInfo, m);
								if (annotations != null)
								{
									if (m.getParameterTypes().length == 0)
									{
										addGetterMethods(
												m.getName(),
												subjectInfo,
												m.getAnnotation(Predicate.class)
														.type(), null, false,
												subject);
									}
								}
								break;

							case REMOVER:
								annotations = getAnnotationsIfProcessable(
										subjectInfo, m);
								if (annotations != null)
								{
									parseRemover(subjectInfo, m, subject);
								}
								break;

						}
					}
					catch (final IllegalArgumentException e)
					{
						// expected when method is not an action method.
					}
				}

			}
			classInfo.put(clazz, subjectInfo);
		}
		return subjectInfo;
	}

	@Override
	public void parseClasses( final String packageName )
			throws MissingAnnotation
	{
		parseClasses(new String[] { packageName });
	}

	@Override
	public void parseClasses( final String[] packageNames )
			throws MissingAnnotation
	{
		boolean hasErrors = false;
		for (final String pkg : packageNames)
		{

			for (final Class<?> c : getClasses(pkg))
			{
				if (c.getAnnotation(Subject.class) != null)
				{
					try
					{
						parse(c);
					}
					catch (final MissingAnnotation e)
					{
						EntityManagerImpl.LOG.warn("Error processing {}: {}",
								c, e.getMessage());
						hasErrors = true;
					}
				}
			}
		}
		if (hasErrors)
		{
			throw new MissingAnnotation(String.format(
					"Unable to parse all %s See log for more details",
					(Object) packageNames));
		}
	}

	private void parseExistential( final SubjectInfoImpl subjectInfo,
			final Method m, final Subject subject ) throws MissingAnnotation
	{
		Method m2 = null;
		if (m.getName().startsWith("has"))
		{
			if (m.getParameterTypes().length == 1)
			{
				m2 = addHasMethods(m.getName(), subjectInfo,
						m.getParameterTypes()[0], getEffectivePredicate(m),
						subject);
			}
		}
		else if (m.getName().startsWith("is"))
		{
			if (m.getParameterTypes().length == 0)
			{
				m2 = addGetterMethods(m.getName(), subjectInfo, Boolean.class,
						getEffectivePredicate(m), false, subject);
			}
		}
		if ((m2 != null) && !Boolean.class.equals(m2.getReturnType()))
		{
			subjectInfo.removePredicateInfo(m2);
		}

	}

	private void parseRemover( final SubjectInfoImpl subjectInfo,
			final Method m, final Subject subject ) throws MissingAnnotation
	{
		if (m.getParameterTypes().length == 1)
		{
			addRemoverMethod(
					m.getParameterTypes()[0],
					m.getName(),
					subjectInfo,
					getEffectivePredicate(m, m.getParameterAnnotations()[0],
							m.getParameterTypes()[0], subject), subject);
		}
		else
		{
			addRemoverMethod(null, m.getName(), subjectInfo,
					getEffectivePredicate(m), subject);
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
	private void parseSetter( final SubjectInfoImpl subjectInfo,
			final Method m, final boolean multiAdd, final Subject subject )
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
			final EffectivePredicate predicate = getEffectivePredicate(m,
					m.getParameterAnnotations()[0], parms[0], subject);
			final PredicateInfoImpl pi = new PredicateInfoImpl(this, predicate,
					m.getName(), valueType);
			subjectInfo.add(pi);
			Method isMethod = null;
			if (m.getName().startsWith("set"))
			{
				addGetterMethods("get" + subName, subjectInfo, valueType,
						predicate, false, subject);
				isMethod = addGetterMethods("is" + subName, subjectInfo,
						valueType, predicate, false, subject);
				addRemoverMethod(null, "remove" + subName, subjectInfo,
						predicate, subject);
			}
			else
			// if (m.getName().startsWith("add"))
			{
				addGetterMethods("get" + subName, subjectInfo, valueType,
						predicate, multiAdd, subject);
				isMethod = addHasMethods("has" + subName, subjectInfo,
						valueType, predicate, subject);

				addRemoverMethod(parms[0], "remove" + subName, subjectInfo,
						predicate, subject);
			}
			if ((isMethod != null)
					&& !Boolean.class.equals(isMethod.getReturnType()))
			{
				subjectInfo.removePredicateInfo(isMethod);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xenei.jena.entities.impl.EntityManager#read(com.hp.hpl.jena.rdf.model
	 * .Resource, java.lang.Class, java.lang.Class)
	 */
	@Override
	@SuppressWarnings( "unchecked" )
	public <T> T read( final Object source, final Class<T> primaryClass,
			final Class<?>... secondaryClasses )
	{
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		SubjectInfoImpl subjectInfo;
		try
		{
			subjectInfo = parse(primaryClass);
		}
		catch (final MissingAnnotation e)
		{
			throw new RuntimeException(e);
		}
		if (primaryClass.isInterface())
		{
			classes.add(primaryClass);
		}
		for (final Class<?> cla : secondaryClasses)
		{
			if (!classes.contains(cla))
			{
				try
				{
					parse(cla);
					classes.add(cla);
				}
				catch (final MissingAnnotation e)
				{
					throw new RuntimeException(e);
				}

			}
		}
		if (!classes.contains(ResourceWrapper.class))
		{
			classes.add(ResourceWrapper.class);
		}
		subjectInfo.validate(classes);
		final MethodInterceptor interceptor = new ResourceEntityProxy(this,
				getResource(source), subjectInfo);
		final Class<?>[] classArray = new Class<?>[classes.size()];

		final Enhancer e = new Enhancer();
		if (!primaryClass.isInterface())
		{
			e.setSuperclass(primaryClass);
		}
		e.setInterfaces(classes.toArray(classArray));
		e.setCallback(interceptor);
		return (T) e.create();
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

	/**
	 * Since the EntityManger implements the manager as a live data read against
	 * the Model, this method
	 * provides a mechanism to copy all the values from the source to the
	 * target. It reads scans the
	 * target class for "set" methods and the source class for associated "get"
	 * methods. If a pairing is
	 * found the value of the "get" call is passed to the "set" call.
	 * 
	 * @param source
	 *            The object that has the values to transfer.
	 * @param target
	 *            The object that has the receptors for the values.
	 * @return The target object after all setters have been called.
	 */
	@Override
	public Object update( final Object source, final Object target )
	{
		final Class<?> targetClass = target.getClass();
		final Class<?> sourceClass = source.getClass();
		for (final Method targetMethod : targetClass.getMethods())
		{
			if (ActionType.SETTER.isA(targetMethod.getName()))
			{
				final Class<?>[] targetMethodParams = targetMethod
						.getParameterTypes();
				if (targetMethodParams.length == 1)
				{
					final String partialName = ActionType.SETTER
							.extractName(targetMethod.getName());

					Method configMethod = null;
					// try "getX" method
					String configMethodName = "get" + partialName;
					try
					{
						configMethod = sourceClass.getMethod(configMethodName);
					}
					catch (final NoSuchMethodException e)
					{
						// no "getX" method so try "isX"
						try
						{
							configMethodName = "is" + partialName;
							configMethod = sourceClass
									.getMethod(configMethodName);
						}
						catch (final NoSuchMethodException expected)
						{
							// no getX or setX
							// configMethod will be null.
						}
					}
					// verify that the config method was annotated as a
					// predicate before
					// we use it.

					try
					{
						if (configMethod != null)
						{
							final boolean setNull = !targetMethodParams[0]
									.isPrimitive();
							if (TypeChecker.canBeSetFrom(targetMethodParams[0],
									configMethod.getReturnType()))
							{
								final Object val = configMethod.invoke(source);
								if (setNull || (val != null))
								{
									targetMethod.invoke(target, val);
								}
							}

						}
					}
					catch (final IllegalArgumentException e)
					{
						throw new RuntimeException(e);
					}
					catch (final IllegalAccessException e)
					{
						throw new RuntimeException(e);
					}
					catch (final InvocationTargetException e)
					{
						throw new RuntimeException(e);
					}

				}
			}
		}
		return target;
	}

	
}
