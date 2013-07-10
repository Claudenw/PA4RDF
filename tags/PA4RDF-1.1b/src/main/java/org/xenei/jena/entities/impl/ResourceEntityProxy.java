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

import com.hp.hpl.jena.rdf.model.Resource;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.proxy.exception.InvokerException;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.annotations.Predicate;

/**
 * Implements the invoker that the proxy uses.
 * 
 * This method intercepts the annotated entity methods as well as the
 * ResourceWrapper.getResource() method.
 */
public class ResourceEntityProxy implements MethodInterceptor // Invoker
{
	private final Resource resource;
	private final SubjectInfo subjectInfo;
	private final EntityManager entityManager;

	/**
	 * The getResource method from the ResourceWrapper class.
	 */
	private static Method GET_RESOURCE;

	static
	{
		try
		{
			ResourceEntityProxy.GET_RESOURCE = ResourceWrapper.class
					.getDeclaredMethod("getResource");
		}
		catch (final SecurityException e)
		{
			throw new RuntimeException(e);
		}
		catch (final NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * The constructor
	 * 
	 * @param entityManager
	 *            The entity manager to use.
	 * @param resource
	 *            The resource that is being wrapped
	 * @param subjectInfo
	 *            The subjectInfo for the resource.
	 */
	public ResourceEntityProxy( final EntityManager entityManager,
			final Resource resource, final SubjectInfo subjectInfo )
	{
		this.resource = resource;
		this.entityManager = entityManager;
		this.subjectInfo = subjectInfo;
	}

	/**
	 * Invokes an method on the proxy.
	 */
	@Override
	public Object intercept( final Object obj, final Method m,
			final Object[] args, final MethodProxy proxy ) throws Throwable
	{
		// handle the cases where the method is not abstract
		if (!Modifier.isAbstract(m.getModifiers()))
		{
			return interceptNonAbstract( obj, m, args, proxy );
		} else {
			return interceptAnnotated( obj, m, args, proxy );
		}
	}

	// handle the cases where the method is not abstract
	private Object interceptNonAbstract( final Object obj, final Method m,
			final Object[] args, final MethodProxy proxy ) throws Throwable
	{	
		// handle the special case methods
		if (m.getName().equals("toString") && !m.isVarArgs()
				&& (m.getParameterTypes().length == 0))
		{
			return String
					.format("%s[%s]", subjectInfo.getClass(), resource);
		}
		if (m.getName().equals("hashCode"))
		{
			return resource.hashCode();
		}
		if (m.getName().equals("equals"))
		{
			if (args[0] instanceof ResourceWrapper)
			{
				return resource.equals(((ResourceWrapper) args[0])
						.getResource());
			}
			if (args[0] instanceof Resource)
			{
				return resource.equals(args[0]);
			}
			return false;
		}

		Predicate p = m.getAnnotation( Predicate.class );
		if (p != null)
		{
			return interceptAnnotatedNonAbstract( obj, m, args, proxy, p );
		}
		return proxy.invokeSuper(obj, args);
	}

	private Object interceptAnnotated( final Object obj, final Method m,
			final Object[] args, final MethodProxy proxy ) throws Throwable
	{
		
		// handle the resource wrapper method call.
		if (ResourceEntityProxy.GET_RESOURCE.equals(m))
		{
			return resource;
		}

		
		SubjectInfo workingInfo = subjectInfo;
		if (m.getDeclaringClass() != subjectInfo.getImplementedClass())
		{
			workingInfo = entityManager.getSubjectInfo(m.getDeclaringClass());
		}
		final PredicateInfo pi = workingInfo.getPredicateInfo(m);

		if (pi == null)
		{
			if (TypeChecker.canBeSetFrom(workingInfo.getImplementedClass(),
					subjectInfo.getImplementedClass())
					&& TypeChecker.canBeSetFrom(
							workingInfo.getImplementedClass(), Resource.class))
			{
				final Class<?>[] argTypes = new Class<?>[args.length];
				for (int i = 0; i < args.length; i++)
				{
					argTypes[i] = args[i].getClass();
				}
				try
				{
					final Method resourceMethod = Resource.class.getMethod(
							m.getName(), argTypes);
					return resourceMethod.invoke(resource, args);
				}
				catch (final Exception e)
				{
					// do nothing thow exception ouside of if.
				}
			}
			throw new InvokerException(String.format("Null method (%s) called",
					m.getName()));
		}

		if (pi instanceof PredicateInfoImpl)
		{
			return ((PredicateInfoImpl) pi).exec(m, resource, args);
		}

		throw new RuntimeException(String.format(
				"Internal predicateinfo class (%s) not (%s)", pi.getClass(),
				PredicateInfoImpl.class));
	}

	// non abstract annotated methods 
	// override the implementation unless annotatation includes the 
	// update=true value -- not implemented
	private Object interceptAnnotatedNonAbstract( final Object obj, final Method m,
			final Object[] args, final MethodProxy proxy, Predicate p ) throws Throwable
	{
		return interceptAnnotated( obj, m, args, proxy );
	}
}