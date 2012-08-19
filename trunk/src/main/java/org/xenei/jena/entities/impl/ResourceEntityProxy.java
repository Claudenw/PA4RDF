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

import org.apache.commons.proxy.Invoker;
import org.apache.commons.proxy.exception.InvokerException;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.SubjectInfo;

/**
 * Implements the invoker that the proxy uses. 
 * 
 * This method intercepts the annotated entity methods as well as the ResourceWrapper.getResource() method. 
 */
public class ResourceEntityProxy implements Invoker
{
	private Resource resource;
	private SubjectInfo subjectInfo;
	private EntityManager entityManager;

	/**
	 * The getResource method from the ResourceWrapper class.
	 */
	private static Method GET_RESOURCE;

	static
	{
		try
		{
			GET_RESOURCE = ResourceWrapper.class
					.getDeclaredMethod("getResource");
		}
		catch (SecurityException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * The constructor
	 * @param entityManager The entity manager to use.
	 * @param resource The resource that is being wrapped
	 * @param subjectInfo The subjectInfo for the resource.
	 */
	public ResourceEntityProxy( EntityManager entityManager, Resource resource,
			SubjectInfo subjectInfo )
	{
		this.resource = resource;
		this.entityManager = entityManager;
		this.subjectInfo = subjectInfo;
	}

	/**
	 * Invokes an method on the proxy.
	 */
	public Object invoke( Object proxy, Method m, Object[] args )
			throws Throwable
	{
		if (!Modifier.isAbstract(m.getModifiers()))
		{

			if (m.getName().equals("toString") && !m.isVarArgs()
					&& m.getParameterTypes().length == 0)
			{
				return String.format("%s[%s]", subjectInfo.getClass(), resource);
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

			return m.invoke(proxy, args);
		}

		if (GET_RESOURCE.equals(m))
		{
			return resource;
		}

		SubjectInfo workingInfo = subjectInfo;
		if (m.getDeclaringClass() != subjectInfo.getImplementedClass())
		{
			workingInfo = entityManager.getSubjectInfo(m.getDeclaringClass());
		}
		PredicateInfo pi = workingInfo.getPredicateInfo(m);

		if (pi == null)
		{
			if (TypeChecker.canBeSetFrom( workingInfo.getImplementedClass(), subjectInfo.getImplementedClass() ) &&
					TypeChecker.canBeSetFrom( workingInfo.getImplementedClass(), Resource.class ))
			{
				Class<?>[] argTypes = new Class<?>[args.length];
				for (int i=0;i<args.length;i++ )
				{
					argTypes[i] = args[i].getClass();
				}
				try {
					Method resourceMethod = Resource.class.getMethod(m.getName(), argTypes );
					return resourceMethod.invoke(resource, args);
				}
				catch (Exception e) {
					// do nothing  thow exception ouside of if.
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

}