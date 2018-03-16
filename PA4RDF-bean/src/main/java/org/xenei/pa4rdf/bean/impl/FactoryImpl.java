package org.xenei.pa4rdf.bean.impl;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.graph.FrontsNode;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.xenei.pa4rdf.bean.EntityFactory;
import org.xenei.pa4rdf.bean.Listener;
import org.xenei.pa4rdf.bean.MethodParser;
import org.xenei.pa4rdf.bean.PredicateInfo;
import org.xenei.pa4rdf.bean.ResourceWrapper;
import org.xenei.pa4rdf.bean.SubjectInfo;
import org.xenei.pa4rdf.bean.datatypes.Init;
import org.xenei.pa4rdf.bean.exceptions.MissingAnnotation;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class FactoryImpl implements EntityFactory
{
	static
	{
		Init.registerTypes();
	}

	private static final Log LOG = LogFactory.getLog(FactoryImpl.class);

	private final Map<Class<?>, SubjectInfoImpl> subjectInfoMap = new HashMap<Class<?>, SubjectInfoImpl>();

	private final List<WeakReference<Listener>> listeners = Collections
			.synchronizedList(new ArrayList<WeakReference<Listener>>());

	@Override
	public void reset_()
	{
		Init.registerTypes();
		subjectInfoMap.clear();
	}

	@Override
	public <T> T makeInstance(Object source, Class<T> clazz)
			throws MissingAnnotation
	{
		return makeInstance(source, parse(clazz));
	}

	@SuppressWarnings("unchecked")
	public <T> T makeInstance(Object source, SubjectInfo subjectInfo)
			throws MissingAnnotation
	{
		final Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		if (subjectInfo.getImplementedClass().isInterface())
		{
			classes.add(subjectInfo.getImplementedClass());
		}

		if (!classes.contains(ResourceWrapper.class))
		{
			classes.add(ResourceWrapper.class);
		}
		final Resource resolvedResource = resolveResource(source);

		final MethodInterceptor interceptor = new ResourceEntityProxy(this,
				resolvedResource);

		final Enhancer e = new Enhancer();
		if (!subjectInfo.getImplementedClass().isInterface())
		{
			e.setSuperclass(subjectInfo.getImplementedClass());
		}
		e.setInterfaces(classes.toArray(new Class<?>[classes.size()]));
		e.setCallback(interceptor);
		return (T) e.create();

	}

	private Resource resolveResource(final Object target)
			throws IllegalArgumentException
	{
		Resource r;
		if (target instanceof ResourceWrapper)
		{
			r = ((ResourceWrapper) target).getResource();
		} else if (target instanceof Resource)
		{
			r = (Resource) target;
		} else if (target instanceof RDFNode)
		{
			r = ((RDFNode) target).asResource();
		} else if (target instanceof FrontsNode)
		{
			r = ModelFactory.createDefaultModel()
					.createResource(((FrontsNode) target).asNode().getURI());
		} else
		{

			throw new IllegalArgumentException(String.format(
					"%s does not implement Resource, ResourceWrapper, RDFNode, or FrontsNode",
					target.getClass()));
		}
		if (r.getModel() == null)
		{
			final Model m = ModelFactory.createDefaultModel();
			r = m.createResource(r.getURI());
		}
		return r;
	}

	@Override
	public SubjectInfo getSubjectInfo(Class<?> clazz)
	{
		return subjectInfoMap.get(clazz);
	}

	private boolean isAdd(final Method m)
	{
		try
		{
			if (ActionType.parse(m.getName()) == ActionType.SETTER)
			{
				final Class<?> parms[] = m.getParameterTypes();
				return (parms != null) && (parms.length == 1);
			}
		} catch (final IllegalArgumentException expected)
		{
			// do nothing
		}
		return false;
	}

	private Map<String, Integer> countAdders(final Method[] methods)
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
				} else
				{
					i = i + 1;
				}
				addCount.put(m.getName(), i);
			}
		}
		return addCount;
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
	@Override
	public SubjectInfo parse(final Class<?> clazz) throws MissingAnnotation
	{
		SubjectInfo subjectInfo = subjectInfoMap.get(clazz);

		if (subjectInfo == null)
		{
			@SuppressWarnings("serial")
			Queue<Class<?>> queue = new LinkedList<Class<?>>()
			{
				@Override
				public boolean add(Class<?> e)
				{
					if (this.contains(e) || subjectInfoMap.containsKey(e))
					{
						return false;
					}
					return super.add(e);
				}
			};
			queue.add(clazz);
			parse(queue);
			subjectInfo = subjectInfoMap.get(clazz);
		}
		return subjectInfo;
	}

	private void parse(Queue<Class<?>> queue) throws MissingAnnotation
	{
		Class<?> clazz = null;
		while ((clazz = queue.poll()) != null)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.info("Parsing " + clazz);
			}
			SubjectInfoImpl subjectInfo = new SubjectInfoImpl(clazz);

			final MethodParser parser = new MethodParser(this, subjectInfo,
					countAdders(clazz.getMethods()));

			/*
			 * Parse getter annotated methods. All others put into annotated
			 * list to parse later. Parsing getters provides us with extra
			 * information about the methods (like the return type)
			 */
			boolean foundAnnotation = false;
			final List<Method> annotated = new ArrayList<Method>();
			for (final Method method : clazz.getMethods())
			{
				// if we declared the method process it.
				if (method.getDeclaringClass().equals(clazz))
				{
					try
					{
						final ActionType actionType = ActionType
								.parse(method.getName());
						if (PredicateInfo.isPredicate(method)
								|| subjectInfo.isMagicBean())
						{
							foundAnnotation = true;
							if (ActionType.GETTER == actionType)
							{
								parser.parse(method);
							} else
							{
								annotated.add(method);
							}
						}
					} catch (final IllegalArgumentException expected)
					{
						// not an action type ignore meannotationClassthod
					}
				} else
				{
					if (PredicateInfo.isPredicate(method) || SubjectInfo
							.isMagicBean(method.getDeclaringClass()))
					{
						queue.add(method.getDeclaringClass());
						foundAnnotation = true;
					}
				}
			}
			if (!foundAnnotation)
			{
				throw new MissingAnnotation(
						"No annotated methods in " + clazz.getCanonicalName());
			}

			/*
			 * Now parse all the annotated non-getter methods
			 */
			for (final Method method : annotated)
			{
				parser.parse(method);
			}

			/* make sure all methods have all data */
			subjectInfo.normalize();

			/* save the result */
			subjectInfoMap.put(clazz, subjectInfo);

			/* notify the listeners */
			synchronized (listeners)
			{
				final Iterator<WeakReference<Listener>> iter = listeners
						.iterator();
				while (iter.hasNext())
				{
					final WeakReference<Listener> listener = iter.next();
					final Listener l = listener.get();
					if (l == null)
					{
						iter.remove();
					} else
					{
						l.onParseClass(subjectInfo);
					}
				}
			}
		}
	}

	@Override
	public void registerListener(Listener listener)
	{
		listeners.add(new WeakReference<Listener>(listener));
	}

	@Override
	public void unregisterListener(Listener listener)
	{
		synchronized (listeners)
		{
			final Iterator<WeakReference<Listener>> iter = listeners.iterator();
			while (iter.hasNext())
			{
				final WeakReference<Listener> wl = iter.next();
				final Listener l = wl.get();
				if (l == null || l == listener)
				{
					iter.remove();
				}
			}
		}
	}

	@Override
	public Collection<? extends SubjectInfo> getSubjects()
	{
		return subjectInfoMap.values();
	}
}
