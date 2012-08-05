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
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.proxy.Invoker;
import org.apache.commons.proxy.ProxyFactory;
import org.apache.commons.proxy.factory.cglib.CglibProxyFactory;
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

	private Map<Class<?>, SubjectInfoImpl> classInfo = new HashMap<Class<?>, SubjectInfoImpl>();

	/**
	 * Constructor.
	 */
	public EntityManagerImpl()
	{
		try
		{
			parse(ResourceWrapper.class);
		}
		catch (MissingAnnotation e)
		{
			throw new RuntimeException(e);
		}
	}

	private Resource getResource( Object target )
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
	public SubjectInfo getSubjectInfo( Class<?> clazz )
	{
		try
		{
			return parse(clazz);
		}
		catch (MissingAnnotation e)
		{
			throw new IllegalArgumentException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xenei.jena.entities.impl.EntityManager#read(com.hp.hpl.jena.rdf.model
	 * .Resource, java.lang.Class, java.lang.Class)
	 */
	@SuppressWarnings( "unchecked" )
	public <T> T read( Object source, Class<T> primaryClass,
			Class<?>... secondaryClasses )
	{
		List<Class<?>> classes = new ArrayList<Class<?>>();
		SubjectInfoImpl subjectInfo;
		try
		{
			subjectInfo = parse(primaryClass);
		}
		catch (MissingAnnotation e)
		{
			throw new RuntimeException(e);
		}
		classes.add(primaryClass);
		for (Class<?> cla : secondaryClasses)
		{
			if (!classes.contains(cla))
			{
				try
				{
					parse(cla);
					classes.add(cla);
				}
				catch (MissingAnnotation e)
				{
					throw new RuntimeException(e);
				}

			}
		}
		if (!classes.contains(ResourceWrapper.class))
		{
			classes.add(ResourceWrapper.class);
		}

		// ResourceEntityProxy interceptor;
		Invoker invoker = new ResourceEntityProxy(this, getResource(source),
				subjectInfo);
		ProxyFactory pf = new CglibProxyFactory();
		Class<?>[] classArray = new Class<?>[classes.size()];
		return (T) pf.createInvokerProxy(invoker, classes.toArray(classArray));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xenei.jena.entities.impl.EntityManager#isInstance(com.hp.hpl.jena
	 * .rdf.model.Resource, java.lang.Class)
	 */
	public boolean isInstance( Object target, Class<?> clazz )
	{
		Subject subject = clazz.getAnnotation(Subject.class);
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
		catch (IllegalArgumentException e)
		{
			return false;
		}

		for (String type : subject.types())
		{
			Resource object = ResourceFactory.createResource(type);
			if (!r.hasProperty(RDF.type, object))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Read an instance of clazz from Resource r. If r does not have the
	 * required types as defined in the Subject annotation they will be added.
	 * 
	 * @param r The Resource to verify.
	 * @param clazz The Subject annotated class to verify against.
	 * @return r for chaining
	 */
	public Resource addInstanceProperties( Resource r, Class<?> clazz )
	{
		Subject e = clazz.getAnnotation(Subject.class);
		Model model = r.getModel(); // may be null;
		if (e != null)
		{
			for (String type : e.types())
			{
				Resource object = (model != null) ? model.createResource(type)
						: ResourceFactory.createResource(type);
				if (!r.hasProperty(RDF.type, object))
				{
					r.addProperty(RDF.type, object);
				}
			}
		}
		return r;
	}

	private String getNamespace( Class<?> clazz )
	{
		Subject e = clazz.getAnnotation(Subject.class);
		return e != null ? e.namespace() : "";
	}

	private boolean isAdd( Method m )
	{
		try
		{
			if (ActionType.parse(m.getName()) == ActionType.SETTER)
			{
				Class<?> parms[] = m.getParameterTypes();
				return parms != null && parms.length == 1;
			}
		}
		catch (IllegalArgumentException expected)
		{
			// do nothing
		}
		return false;
	}

	private Map<String, Integer> countAdders( Method[] methods )
	{
		Map<String, Integer> addCount = new HashMap<String, Integer>();
		for (Method m : methods)
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
	 * Returns the list of annotations if this method has the Predicate
	 * annotation
	 * 
	 * @param subjectInfo
	 * @param m
	 * @return
	 */
	private Annotation[] getAnnotationsIfProcessable( SubjectInfo subjectInfo,
			Method m )
	{
		Annotation[] annotations = m.getAnnotations();
		boolean process = false;
		for (Annotation a : annotations)
		{
			if (a instanceof org.xenei.jena.entities.annotations.Predicate)
			{
				process = subjectInfo.getPredicateInfo(m) == null;
			}
		}
		return process ? annotations : null;
	}

	private SubjectInfoImpl parse( Class<?> clazz ) throws MissingAnnotation
	{
		SubjectInfoImpl subjectInfo = classInfo.get(clazz);

		if (subjectInfo == null)
		{
			LOG.info("Parsing {}", clazz);
			subjectInfo = new SubjectInfoImpl(clazz);

			Map<String, Integer> addCount = countAdders(clazz.getMethods());
			Annotation[] annotations;
			for (Method m : clazz.getMethods())
			{

				// only process abstract methods and does not have var args
				if (Modifier.isAbstract(m.getModifiers()) && !m.isVarArgs())
				{
					try
					{
						ActionType actionType = ActionType.parse(m.getName());
						// process "set" if only one arg and not varargs
						switch (actionType)
						{
							case SETTER:
								Integer i = addCount.get(m.getName());
								if (i != null)
								{
									parseSetter(subjectInfo, m, (i > 1));
								}
								break;

							case EXISTENTIAL:
								annotations = getAnnotationsIfProcessable(
										subjectInfo, m);
								if (annotations != null)
								{
									parseExistential(subjectInfo, m);
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
														.type(), null, false);
									}
								}
								break;

							case REMOVER:
								annotations = getAnnotationsIfProcessable(
										subjectInfo, m);
								if (annotations != null)
								{
									parseRemover(subjectInfo, m);
								}
								break;

						}
					}
					catch (IllegalArgumentException e)
					{
						// expected when method is not an action method.
					}
				}

			}

			classInfo.put(clazz, subjectInfo);
		}
		return subjectInfo;
	}

	private EffectivePredicate getEffectivePredicate( Method m )
			throws MissingAnnotation
	{
		return getEffectivePredicate(m, null, null, null);
	}

	private EffectivePredicate getEffectivePredicate( Method m,
			Annotation[] paramAnnotations, Class<?> returnType )
			throws MissingAnnotation
	{
		return getEffectivePredicate(m, paramAnnotations, returnType, null);
	}

	private EffectivePredicate getEffectivePredicate( Method m,
			Annotation[] paramAnnotations, Class<?> returnType, EffectivePredicate dflt )
			throws MissingAnnotation
	{
		boolean typeSet = false;
		EffectivePredicate retval = new EffectivePredicate().merge(
				m.getAnnotation(Predicate.class)).merge(dflt);
		if (paramAnnotations != null)
		{
			for (Annotation a : paramAnnotations)
			{
				if (a instanceof URI)
				{
					retval.type = URI.class;
					typeSet = true;
				}
			}
		}
		if (!typeSet && returnType != null)
		{
			retval.type = returnType;
		}
		if (retval.namespace().length() == 0)
		{
			retval.namespace = getNamespace(m.getDeclaringClass());
		}
		if (retval.name().length() == 0)
		{
			ActionType actionType = ActionType.parse(m.getName());
			retval.name = actionType.extractName(m.getName());
		}

		// check to see if local name includes namespace
		Node n = Node.createURI(retval.name);
		String localNamespace = n.getNameSpace();
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
							m.getName()));
		}
		if (!Util.notNameChar(retval.namespace().charAt(
				retval.namespace().length() - 1)))
		{
			throw new MissingAnnotation(String.format(
					"Namespace (%s) ends with invalid character",
					retval.namespace()));
		}
		String s = retval.name.substring(0, 1);
		if (retval.upcase())
		{
			retval.name = retval.name.replaceFirst(s, s.toUpperCase());
		}
		else
		{
			retval.name = retval.name.replaceFirst(s, s.toLowerCase());
		}

		int split = Util.splitNamespace(retval.namespace() + retval.name());
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

	/**
	 * Processes "setX" and "addX" functions.
	 * 
	 * @param subjectInfo
	 * @param m
	 * @param multiAdd
	 * @throws MissingAnnotation
	 */
	private void parseSetter( SubjectInfoImpl subjectInfo, Method m,
			boolean multiAdd ) throws MissingAnnotation
	{

		Class<?> parms[] = m.getParameterTypes();
		if (parms.length == 1)
		{
			Class<?> valueType = parms[0];
			String subName = ActionType.SETTER.extractName(m.getName()); // remove
																			// "set"
																			// or
																			// "add"
			EffectivePredicate predicate = getEffectivePredicate(m,
					m.getParameterAnnotations()[0], parms[0]);
			PredicateInfoImpl pi = new PredicateInfoImpl(this, predicate,
					m.getName(), valueType);
			subjectInfo.add(pi);
			Method isMethod = null;
			if (m.getName().startsWith("set"))
			{
				addGetterMethods("get" + subName, subjectInfo, valueType,
						predicate, false);
				isMethod = addGetterMethods("is" + subName, subjectInfo,
						valueType, predicate, false);
				addRemoverMethod(null, "remove" + subName, subjectInfo,
						predicate);
			}
			else
			// if (m.getName().startsWith("add"))
			{
				addGetterMethods("get" + subName, subjectInfo, valueType,
						predicate, multiAdd);
				isMethod = addHasMethods("has" + subName, subjectInfo,
						valueType, predicate);

				addRemoverMethod(parms[0], "remove" + subName, subjectInfo,
						predicate);
			}
			if (isMethod != null
					&& !Boolean.class.equals(isMethod.getReturnType()))
			{
				subjectInfo.removePredicateInfo(isMethod);
			}
		}
	}

	private void parseExistential( SubjectInfoImpl subjectInfo, Method m )
			throws MissingAnnotation
	{
		Method m2 = null;
		if (m.getName().startsWith("has"))
		{
			if (m.getParameterTypes().length == 1)
			{
				m2 = addHasMethods(m.getName(), subjectInfo,
						m.getParameterTypes()[0], getEffectivePredicate(m));
			}
		}
		else if (m.getName().startsWith("is"))
		{
			if (m.getParameterTypes().length == 0)
			{
				m2 = addGetterMethods(m.getName(), subjectInfo, Boolean.class,
						getEffectivePredicate(m), false);
			}
		}
		if (m2 != null && !Boolean.class.equals(m2.getReturnType()))
		{
			subjectInfo.removePredicateInfo(m2);
		}

	}

	private void parseRemover( SubjectInfoImpl subjectInfo, Method m )
			throws MissingAnnotation
	{
		if (m.getParameterTypes().length == 1)
		{
			addRemoverMethod(
					m.getParameterTypes()[0],
					m.getName(),
					subjectInfo,
					getEffectivePredicate(m, m.getParameterAnnotations()[0],
							m.getParameterTypes()[0]));
		}
		else
		{
			addRemoverMethod(null, m.getName(), subjectInfo,
					getEffectivePredicate(m));
		}
	}

	private Annotation[] getParameterAnnotation( Method m )
	{
		return (m.getParameterAnnotations().length > 0) ? m
				.getParameterAnnotations()[0] : null;
	}

	private Method addGetterMethods( String methodName,
			SubjectInfoImpl subjectInfo, Class<?> setterValueType,
			EffectivePredicate parentPredicate, boolean multiAdd )
			throws MissingAnnotation
	{
		Method m = null;
		try
		{
			// only get methods without arguments
			m = subjectInfo.getImplementedClass().getMethod(methodName,
					(Class<?>[]) null);

			if (Modifier.isAbstract(m.getModifiers()) && !m.isVarArgs())
			{
				EffectivePredicate predicate = getEffectivePredicate(m, null,
						null, parentPredicate);

				if (m.getReturnType().equals(ExtendedIterator.class))
				{

					Predicate declaredPredicate = m
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
		catch (SecurityException e)
		{
			// acceptable response
		}
		catch (NoSuchMethodException e)
		{
			// acceptable response
		}
		return m;
	}

	private Method addHasMethods( String methodName,
			SubjectInfoImpl subjectInfo, Class<?> valueType,
			EffectivePredicate parentPredicate )
	{
		Method m = null;
		try
		{
			m = subjectInfo.getImplementedClass().getMethod(methodName,
					new Class<?>[] { valueType });
			if (Modifier.isAbstract(m.getModifiers()) && !m.isVarArgs())
			{
				subjectInfo.add(new PredicateInfoImpl(this,
						getEffectivePredicate(m, getParameterAnnotation(m),
								valueType, parentPredicate), methodName,
						valueType));
			}
			else
			{
				m = null;
			}
		}
		catch (SecurityException e)
		{
			// acceptable response
		}
		catch (NoSuchMethodException e)
		{
			// acceptable response
		}
		catch (MissingAnnotation e)
		{
			LOG.error(e.toString());
		}
		return m;
	}

	private Method addRemoverMethod( Class<?> argType, String name,
			SubjectInfoImpl subjectInfo, EffectivePredicate parentPredicate )
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
			if (Modifier.isAbstract(m.getModifiers()) && !m.isVarArgs())
			{
				EffectivePredicate predicate = getEffectivePredicate(m,
						getParameterAnnotation(m), argType, parentPredicate);
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
		catch (SecurityException e)
		{
			// acceptable error
		}
		catch (NoSuchMethodException e)
		{
			// acceptable error
		}
		return m;
	}

	public void parseClasses( String packageName ) throws MissingAnnotation
	{
		parseClasses(new String[] { packageName });
	}

	public void parseClasses( String[] packageNames ) throws MissingAnnotation
	{
		boolean hasErrors = false;
		for (String pkg : packageNames)
		{

			for (Class<?> c : getClasses(pkg))
			{
				if (c.getAnnotation(Subject.class) != null)
				{
					try
					{
						parse(c);
					}
					catch (MissingAnnotation e)
					{
						LOG.warn("Error processing {}: {}", c, e.getMessage());
						hasErrors = true;
					}
				}
			}
		}
		if (hasErrors)
		{
			throw new MissingAnnotation(String.format(
					"Unable to parse all %s See log for more details",
					(Object)packageNames));
		}
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
	private Collection<Class<?>> getClasses( String packageName )
	{

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources;
		try
		{
			resources = classLoader.getResources(path);
		}
		catch (IOException e1)
		{
			LOG.error(e1.toString());
			return Collections.emptyList();
		}
		Set<Class<?>> classes = new HashSet<Class<?>>();
		if (resources.hasMoreElements())
		{
			while (resources.hasMoreElements())
			{
				URL resource = resources.nextElement();
				try
				{
					for (String clazz : findClasses(resource.getFile(),
							packageName))
					{
						try
						{
							classes.add(Class.forName(clazz));
						}
						catch (ClassNotFoundException e)
						{
							LOG.warn(e.toString());
						}
						catch (ExceptionInInitializerError e)
						{
							System.out.println("WHAT?");
						}
					}
				}
				catch (IOException e)
				{
					LOG.warn(e.toString());
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
			catch (ClassNotFoundException e)
			{
				LOG.warn("{} was neither a package name nor a class name",
						packageName);
			}
		}
		return classes;
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
	private Set<String> findClasses( String directory, String packageName )
			throws IOException
	{
		Set<String> classes = new HashSet<String>();
		if (directory.startsWith("file:") && directory.contains("!"))
		{
			String[] split = directory.split("!");
			URL jar = new URL(split[0]);
			ZipInputStream zip = new ZipInputStream(jar.openStream());
			ZipEntry entry = null;
			while ((entry = zip.getNextEntry()) != null)
			{
				if (entry.getName().endsWith(".class"))
				{
					String className = entry.getName().replaceAll("[$].*", "")
							.replaceAll("[.]class", "").replace('/', '.');
					classes.add(className);
				}
			}
		}
		File dir = new File(directory);
		if (!dir.exists())
		{
			return classes;
		}
		File[] files = dir.listFiles();
		for (File file : files)
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
	 * Since the EntityManger implements the manager as a live data read against the Model, this method 
	 * provides a mechanism to copy all the values from the source to the target.  It reads scans the 
	 * target class for "set" methods and the source class for associated "get" methods.  If a pairing is
	 * found the value of the "get" call is passed to the "set" call.
	 * 
	 * @param source The object that has the values to transfer.
	 * @param target The object that has the receptors for the values.
	 * @return The target object after all setters have been called.
	 */
	public Object update( Object source, Object target )
	{
		Class<?> targetClass = target.getClass();
		Class<?> sourceClass = source.getClass();
		for (Method targetMethod : targetClass.getMethods())
		{
			// process only target "setX" methods.
			if (targetMethod.getName().startsWith("set"))
			{
				Class<?>[] targetMethodParams = targetMethod
						.getParameterTypes();
				if (targetMethodParams.length == 1)
				{
					String partialName = targetMethod.getName().substring(3);

					Object targetObj = null;
					Object configObj = null;
					Method configMethod = null;
					// try "getX" method
					String configMethodName = "get" + partialName;
					try
					{
						configMethod = sourceClass.getMethod(configMethodName);
					}
					catch (NoSuchMethodException e)
					{
						// no "getX" method so try "isX"
						try
						{
							configMethodName = "is" + partialName;
							configMethod = sourceClass
									.getMethod(configMethodName);
						}
						catch (NoSuchMethodException expected)
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
						SubjectInfo subjectInfo = parse(configMethod
								.getDeclaringClass());
						if (subjectInfo.getPredicateInfo(configMethod) == null)
						{
							configMethod = null;
						}
					}
					catch (MissingAnnotation e)
					{
						configMethod = null;
					}
					if (configMethod != null)
					{
						// see if we can do the assignment
						boolean doAssignment = targetMethodParams[0]
								.isAssignableFrom(configMethod.getReturnType());
						// a flag to specify if we can accept a null value
						boolean setNull = true;
						try
						{
							if (!doAssignment)
							{
								// can't do the assignment so see if there is a
								// primitive -> object mapping issue
								targetObj = targetMethodParams[0];

								try
								{
									if (targetMethodParams[0].isPrimitive())
									{
										setNull = false; // we can not set a
															// primitive to null
									}
									else
									{
										// the the primitive object type
										targetObj = targetMethodParams[0]
												.getField("TYPE").get(null);
									}
									// set the configuration object type.
									configObj = configMethod.getReturnType()
											.isPrimitive() ? configMethod
											.getReturnType() : configMethod
											.getReturnType().getField("TYPE")
											.get(null);
									// if they are equal we can do the
									// assignment.
									doAssignment = targetObj.equals(configObj);
								}
								catch (NoSuchFieldException e)
								{
									// this occurs when the TYPE field is not
									// present
									// i.e. when the object is not a wrapper for
									// a primitive
									// type.
									if (e.getMessage().equals("TYPE"))
									{
										doAssignment = false;
									}
									else
									{
										throw e;
									}
								}

							}
							// can we do the assignment now?
							if (doAssignment)
							{
								Object val = configMethod.invoke(source);
								if (setNull || val != null)
								{
									targetMethod.invoke(target, val);
								}
							}
						}
						catch (SecurityException e)
						{
							throw new RuntimeException(e);
						}
						catch (NoSuchFieldException e)
						{
							throw new RuntimeException(e);
						}
						catch (IllegalAccessException e)
						{
							throw new RuntimeException(e);
						}
						catch (InvocationTargetException e)
						{
							throw new RuntimeException(e);
						}
					}
				}
			}
		}
		return target;
	}
}
