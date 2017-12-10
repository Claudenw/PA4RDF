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

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.arq.querybuilder.AskBuilder;
import org.apache.jena.arq.querybuilder.UpdateBuilder;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.enhanced.EnhGraph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.AnonId;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.syntax.ElementNamedGraph;
import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.cache.CachingGraph;
import org.xenei.jena.entities.cache.SubjectTable;
import org.xenei.jena.entities.impl.datatype.CharDatatype;
import org.xenei.jena.entities.impl.datatype.CharacterDatatype;
import org.xenei.jena.entities.impl.datatype.LongDatatype;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * An implementation of the EntityManager interface.
 * 
 */
public class EntityManagerImpl implements EntityManager
{
	private static Logger LOG = LoggerFactory
			.getLogger(EntityManagerImpl.class);

	private final Map<Byte, List<SoftReference<Resource>>> resourceCache;

	private final Map<Class<?>, SubjectInfo> classInfo;

	private final List<WeakReference<Listener>> listeners;

	private final RDFConnection connection;

	private final CachingGraph cachingGraph;

	protected final Model cachingModel;

	private final UpdateHandler updateHandler;

	private final Node modelName;

	static
	{
		registerTypes();
	}

	/**
	 * Register the datatypes used by the entity manger specifically xsd:long :
	 * java.util.Long xsd:string : java.util.Character xsd:string :
	 * java.lang.char
	 * 
	 * and finally resetting xsd:string to java.lang.String
	 */
	public static void registerTypes()
	{
		RDFDatatype rtype = null;
		// handle the string types
		// preserve string class and put it back later.
		final RDFDatatype stype = TypeMapper.getInstance()
				.getTypeByClass(String.class);
		rtype = new CharacterDatatype();
		TypeMapper.getInstance().registerDatatype(rtype);
		rtype = new CharDatatype();
		TypeMapper.getInstance().registerDatatype(rtype);
		// put the string type back so that it is the registered type for
		// xsd:string
		TypeMapper.getInstance().registerDatatype(stype);

		// change the long type.
		rtype = new LongDatatype();
		TypeMapper.getInstance().registerDatatype(rtype);
	}

	/**
	 * Constructor.
	 */
	public EntityManagerImpl(RDFConnection connection, boolean writeThrough)
	{
		this.modelName = Quad.defaultGraphIRI;
		this.connection = connection;
		this.cachingGraph = new CachingGraph(this);
		this.cachingModel = ModelFactory.createModelForGraph(cachingGraph);
		this.resourceCache = new HashMap<Byte, List<SoftReference<Resource>>>();

		if (writeThrough)
		{
			this.updateHandler = new UpdateDirect();
		} else
		{
			this.updateHandler = new UpdateCached();
		}
		listeners = Collections
				.synchronizedList(new ArrayList<WeakReference<Listener>>());
		classInfo = new HashMap<Class<?>, SubjectInfo>()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 8181650326448307726L;

			private void notifyListeners(SubjectInfo value)
			{
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
							l.onParseClass(value);
						}
					}
				}
			}

			@Override
			public SubjectInfo put(Class<?> key, SubjectInfo value)
			{
				notifyListeners(value);
				return super.put(key, value);
			}

			@Override
			public void putAll(Map<? extends Class<?>, ? extends SubjectInfo> m)
			{
				for (final SubjectInfo si : m.values())
				{
					notifyListeners(si);
				}
				super.putAll(m);
			}
		};
		try
		{
			parse(ResourceWrapper.class);
		} catch (final MissingAnnotation e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Constructor.
	 */
	private EntityManagerImpl(EntityManagerImpl base, Node modelName)
	{
		this.modelName = modelName == null ? Quad.defaultGraphIRI : modelName;
		this.connection = base.connection;
		this.cachingGraph = new CachingGraph(this);
		this.cachingModel = ModelFactory.createModelForGraph(cachingGraph);
		this.updateHandler = base.updateHandler;
		this.listeners = base.listeners;
		this.classInfo = base.classInfo;
		this.resourceCache = new HashMap<Byte, List<SoftReference<Resource>>>();
	}

	@Override
	public RDFConnection getConnection() {
		return connection;
	}
	
	@Override
	public EntityManager getNamedManager(Node modelName)
	{
		// check for same name
		String defaultName = Quad.defaultGraphIRI.getURI();
		String newModelName = StringUtils.defaultIfBlank(modelName.getURI(),
				defaultName);
		if (newModelName.equals(StringUtils
				.defaultIfBlank(getModelName().getURI(), defaultName)))
		{
			return this;
		}
		if (!defaultName.equals(newModelName)
				&& !Quad.unionGraph.getURI().equals(newModelName))
		{
			AskBuilder askBuilder = new AskBuilder().addGraph(newModelName,
					new AskBuilder().addWhere("?s", "?p", "?o"));
			if (!connection.queryAsk(askBuilder.build()))
			{
				connection.put(newModelName, ModelFactory.createDefaultModel());
			}
		}
		return new EntityManagerImpl(this, NodeFactory.createURI(newModelName));
	}

	@Override
	public EntityManager getDefaultManager()
	{
		return getNamedManager(Quad.defaultGraphIRI);
	}

	@Override
	public Node getModelName()
	{
		return modelName;
	}

	@Override
	public void reset()
	{
		updateHandler.reset();
		classInfo.clear();
		registerTypes();
		try
		{
			parse(ResourceWrapper.class);
		} catch (final MissingAnnotation e)
		{
			throw new RuntimeException(e);
		}

	}

	/**
	 * Read an instance of clazz from Object source. If source does not have the
	 * required types as defined in the Subject annotation of clazz they will be
	 * added.
	 * <p>
	 * This method may modify the graph and should be called within the scope of
	 * a transaction if the underlying graph requires them.
	 * </p>
	 * 
	 * @param source
	 *            Must either implement Resource or ResourceWrapper interfaces.
	 * @param clazz
	 *            The class containing the Subject annotation.
	 * @return source for chaining
	 * @throws MissingAnnotation
	 *             if clazz does not have Subject annotations.
	 * @throws IllegalArgumentException
	 *             if source implements neither Resource nor ResourceWrapper.
	 */
	@Override
	public <T> T addInstanceProperties(final T source, final Class<?> clazz)
			throws MissingAnnotation, IllegalArgumentException
	{
		final Resource r = getResource(source);
		final Subject e = clazz.getAnnotation(Subject.class);
		if (e == null)
		{
			throw new MissingAnnotation(
					"No Subject annotationin " + clazz.getCanonicalName());
		}
		final Model model = r.getModel(); // may be null;
		if (e != null)
		{
			for (final String type : e.types())
			{
				final Resource object = (model != null)
						? model.createResource(type)
						: ResourceFactory.createResource(type);
				if (!r.hasProperty(RDF.type, object))
				{
					r.addProperty(RDF.type, object);
				}
			}
		}
		return source;
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
	 * Recursive method used to find all classes in a given directory and
	 * subdirs. Adapted from http://snippets.dzone.com/posts/show/4831 and
	 * extended to support use of JAR files
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Set<String> findClasses(final String directory,
			final String packageName) throws IOException
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
				classes.addAll(findClasses(file.getAbsolutePath(),
						packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class"))
			{
				classes.add(packageName + '.' + file.getName().substring(0,
						file.getName().length() - 6));
			}
		}
		return classes;
	}

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages. Adapted from
	 * http://snippets.dzone.com/posts/show/4831 and extended to support use of
	 * JAR files
	 * 
	 * @param packageName
	 *            The base package or class name
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private Collection<Class<?>> getClasses(final String packageName)
	{

		final ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		assert classLoader != null;
		final String path = packageName.replace('.', '/');
		Enumeration<URL> resources;
		try
		{
			resources = classLoader.getResources(path);
		} catch (final IOException e1)
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
						} catch (final ClassNotFoundException e)
						{
							EntityManagerImpl.LOG.warn(e.toString());
						}
					}
				} catch (final IOException e)
				{
					EntityManagerImpl.LOG.warn(e.toString());
				}
			}
		} else
		{
			// there are no resources at that path so see if it is a class
			try
			{
				classes.add(Class.forName(packageName));
			} catch (final ClassNotFoundException e)
			{
				EntityManagerImpl.LOG.warn(
						"{} was neither a package name nor a class name",
						packageName);
			}
		}
		return classes;
	}

	private Resource register(Resource r)
	{
	    if (cachingModel != r.getModel())
	    {
	        r = cachingModel.wrapAsResource( r.asNode() );
	    }
		final ResourceInterceptor interceptor = new ResourceInterceptor(r);
		final Enhancer e = new Enhancer();
		e.setInterfaces(new Class[] { Resource.class });
		e.setCallback(interceptor);
		return (Resource) e.create();
	}

	@Override
	public Resource createResource(String uri)
	{
		Resource r = cachingModel.createResource(uri);
		return getResourceFromCache(calcBloomFilter(r), r);
	}

	@Override
	public Resource createResource(String uri, Resource type)
	{
		Resource r = createResource(uri);
		r.addProperty(RDF.type, type);
		return r;
	}

	@Override
	public Resource createResource()
	{
		Resource r = cachingModel.createResource();
		return getResourceFromCache(calcBloomFilter(r), r);
	}

	@Override
	public Resource createResource(AnonId id)
	{
		Resource r = cachingModel.createResource(id);
		return getResourceFromCache(calcBloomFilter(r), r);
	}

	@Override
	public boolean hasResource(String uri)
	{
		Resource r = cachingModel.createResource(uri);
		if (cachingModel.contains(r, null, (RDFNode) null))
		{
			return true;
		}
		AskBuilder builder = new AskBuilder().addWhere(r, null, null);
		return connection.queryAsk(builder.build());
	}

	private Resource getResource(final Object target)
			throws IllegalArgumentException
	{
		Resource r = null;
		if (target instanceof ResourceWrapper)
		{
			r = ((ResourceWrapper) target).getResource();
		} else if (target instanceof Resource)
		{
			r = (Resource) target;
		} else
		{

			throw new IllegalArgumentException(String.format(
					"%s implements neither Resource nor ResourceWrapper",
					target.getClass()));
		}

		/*
		 * make sure the resource is loaded in the table make the resource point
		 * to the caching model. bind the table to the resource to keep it from
		 * being garbage collected.
		 */

		return getResourceFromCache(calcBloomFilter(r), r);

	}

	private Byte calcBloomFilter(Resource r)
	{
		String name = null;
		if (r.isAnon())
		{
			name = r.getId().toString();
		} else
		{
			name = r.getURI();
		}

		short first = (short) (name.hashCode() & 0xFFFF);
		short second = (short) (((name.hashCode() & 0xFFFF0000) >> 16)
				& 0xFFFF);
		byte b = 0;
		for (int i = 0; i < 3; i++)
		{
			b |= (1 << Math.abs(first % (short) 8));
			first += second;
		}
		return Byte.valueOf(b);
	}

	private synchronized Resource getResourceFromCache(Byte b, Resource r)
	{
		List<SoftReference<Resource>> lst = resourceCache.get(b);
		if (lst != null)
		{
			Iterator<SoftReference<Resource>> iter = lst.iterator();
			while (iter.hasNext())
			{
				SoftReference<Resource> wr = iter.next();
				Resource r2 = wr.get();
				if (r2 == null)
				{
					iter.remove();
				} else
				{
					if (r2.equals(r))
					{
						return r2;
					}
				}
			}
		} else
		{
			lst = new ArrayList<SoftReference<Resource>>();
			resourceCache.put(b, lst);
		}

		Resource retval = register(r);
		// if (r.isAnon())
		// {
		// retval = createResource( r.getId());
		// } else {
		// retval = createResource( r.getURI());
		// }
		lst.add(new SoftReference<Resource>(retval));
		return retval;
	}

	@Override
	public Subject getSubject(final Class<?> clazz)
	{
		final Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
		for (Class<?> cls = clazz; cls != Object.class
				&& cls != null; cls = cls.getSuperclass())
		{
			final Subject retval = cls.getAnnotation(Subject.class);
			if (retval != null)
			{
				return retval;
			}
			interfaces.addAll(Arrays.asList(cls.getInterfaces()));
		}

		for (final Class<?> cls : interfaces)
		{
			final Subject retval = cls.getAnnotation(Subject.class);
			if (retval != null)
			{
				return retval;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xenei.jena.entities.impl.EntityManager#getSubjectInfo(java.lang.Class
	 * )
	 */
	@Override
	public SubjectInfo getSubjectInfo(final Class<?> clazz)
	{
		try
		{
			return parse(clazz);
		} catch (final MissingAnnotation e)
		{
			throw new IllegalArgumentException(e);
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xenei.jena.entities.impl.EntityManager#isInstance(org.apache.jena
	 * .rdf.model.Resource, java.lang.Class)
	 */
	@Override
	public boolean isInstance(final Object target, final Class<?> clazz)
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
		} catch (final IllegalArgumentException e)
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
	private SubjectInfoImpl parse(final Class<?> clazz) throws MissingAnnotation
	{
		SubjectInfoImpl subjectInfo = (SubjectInfoImpl) classInfo.get(clazz);

		if (subjectInfo == null)
		{
			EntityManagerImpl.LOG.info("Parsing {}", clazz);
			subjectInfo = new SubjectInfoImpl(clazz);

			final MethodParser parser = new MethodParser(this, subjectInfo,
					countAdders(clazz.getMethods()));

			boolean foundAnnotation = false;
			final List<Method> annotated = new ArrayList<Method>();
			for (final Method method : clazz.getMethods())
			{
				try
				{
					final ActionType actionType = ActionType
							.parse(method.getName());
					if (method.getAnnotation(Predicate.class) != null)
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
					// not an action type ignore method
				}

			}
			if (!foundAnnotation)
			{
				throw new MissingAnnotation(
						"No annotated methods in " + clazz.getCanonicalName());
			}

			for (final Method method : annotated)
			{
				parser.parse(method);
			}
			classInfo.put(clazz, subjectInfo);
		}
		return subjectInfo;
	}

	@Override
	public void parseClasses(final String packageName) throws MissingAnnotation
	{
		parseClasses(new String[] { packageName });
	}

	@Override
	public void parseClasses(final String[] packageNames)
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
					} catch (final MissingAnnotation e)
					{
						EntityManagerImpl.LOG.warn("Error processing {}: {}", c,
								e.getMessage());
						hasErrors = true;
					}
				}
			}
		}
		if (hasErrors)
		{
			throw new MissingAnnotation(String.format(
					"Unable to parse all %s See log for more details",
					Arrays.asList(packageNames)));
		}
	}

	@Override
	public <T> T make(final Object source, final Class<T> primaryClass,
			final Class<?>... secondaryClasses) throws MissingAnnotation
	{
		Resource r = addInstanceProperties(getResource(source), primaryClass);
		for (final Class<?> c : secondaryClasses)
		{
			r = addInstanceProperties(r, c);
		}
		return read(r, primaryClass, secondaryClasses);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xenei.jena.entities.impl.EntityManager#read(org.apache.jena.rdf.model
	 * .Resource, java.lang.Class, java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T read(final Object source, final Class<T> primaryClass,
			final Class<?>... secondaryClasses)
			throws MissingAnnotation, IllegalArgumentException
	{
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		SubjectInfoImpl subjectInfo;
		subjectInfo = parse(primaryClass);

		if (primaryClass.isInterface())
		{
			classes.add(primaryClass);
		}
		for (final Class<?> cla : secondaryClasses)
		{
			if (!classes.contains(cla))
			{
				parse(cla);
				classes.add(cla);
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

	@Override
	public void close()
	{
		connection.close();
	}

	/**
	 * Since the EntityManger implements the manager as a live data read against
	 * the Model, this method provides a mechanism to copy all the values from
	 * the source to the target. It reads scans the target class for "set"
	 * methods and the source class for associated "get" methods. If a pairing
	 * is found the value of the "get" call is passed to the "set" call.
	 * 
	 * @param source
	 *            The object that has the values to transfer.
	 * @param target
	 *            The object that has the receptors for the values.
	 * @return The target object after all setters have been called.
	 */
	@Override
	public Object update(final Object source, final Object target)
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
					} catch (final NoSuchMethodException e)
					{
						// no "getX" method so try "isX"
						try
						{
							configMethodName = "is" + partialName;
							configMethod = sourceClass
									.getMethod(configMethodName);
						} catch (final NoSuchMethodException expected)
						{
							// no getX or setX
							// configMethod will be null.
						}
					}
					/*
					 * verify that the config method was annotated as a
					 * predicate before we use it.
					 */

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
					} catch (final IllegalArgumentException e)
					{
						throw new RuntimeException(e);
					} catch (final IllegalAccessException e)
					{
						throw new RuntimeException(e);
					} catch (final InvocationTargetException e)
					{
						throw new RuntimeException(e);
					}

				}
			}
		}
		return target;
	}

	@Override
	public Collection<SubjectInfo> getSubjects()
	{
		return classInfo.values();
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

	/**
	 * Get the Update Handler.
	 * 
	 * @return the update handler.
	 */
	public UpdateHandler getUpdateHandler()
	{
		return updateHandler;
	}

	@Override
	public void sync()
	{
		updateHandler.execute();
		cachingGraph.sync();
	}

	@Override
	public QueryExecution execute(Query query)
	{
		Query q = query.cloneQuery();
		if (!modelName.equals(Quad.defaultGraphIRI))
		{
			ElementNamedGraph eng = new ElementNamedGraph(modelName,
					q.getQueryPattern());
			q.setQueryPattern(eng);
		}
		return connection.query(q);
	}
	
	@Override
	public String toString() {
	    return String.format(  "EntityManager[%s]", modelName );
	}

	/**
	 * A class that ensures that there is a hard reference between the resource
	 * and its subject table.
	 */
	private class ResourceInterceptor implements MethodInterceptor
	{
		// we just need to hold a reference to the table.
		@SuppressWarnings("unused")
		private final SubjectTable tbl;
		//
		private final Resource res;

		/**
		 * Constructor.
		 * 
		 * @param res
		 *            the resource
		 */
		private ResourceInterceptor(Resource res)
		{
			this.tbl = cachingGraph.getTable(res.asNode());
			this.res = res;
		}

		@Override
		public Object intercept(Object obj, Method method, Object[] args,
				MethodProxy proxy) throws Throwable
		{
			return method.invoke(res, args);
		}
	}

	public interface UpdateHandler
	{
		void reset();

		void execute();

		void prepare(Update update);

		void prepare(UpdateRequest updateReq);
	}

	private class UpdateDirect implements UpdateHandler
	{

		@Override
		public synchronized void prepare(UpdateRequest updateReq)
		{
			if (updateReq.iterator().hasNext())
			{
				synchronized (connection)
				{
					connection.begin(ReadWrite.WRITE);
					try
					{
						connection.update(updateReq);
						connection.commit();
					} catch (final RuntimeException e)
					{
						connection.abort();
						throw e;
					}
				}
			}
		}

		@Override
		public synchronized void prepare(Update update)
		{
			synchronized (connection)
			{
				connection.begin(ReadWrite.WRITE);
				try
				{
					connection.update(update);
					connection.commit();
				} catch (final RuntimeException e)
				{
					connection.abort();
					throw e;
				}
			}
		}

		public void execute()
		{
		}

		public void reset()
		{
		};
	}

	private class UpdateCached implements UpdateHandler
	{

		private UpdateRequest updates = null;

		@Override
		public synchronized void prepare(UpdateRequest updateReq)
		{
			Iterator<Update> iter = updateReq.iterator();
			if (iter.hasNext())
			{
				if (updates == null)
				{
					updates = updateReq;
				} else
				{
					while (iter.hasNext())
					{
						updates.add(iter.next());
					}
				}
			}
		}

		@Override
		public synchronized void prepare(Update update)
		{
			if (updates == null)
			{
				updates = new UpdateRequest(update);
			} else
			{
				updates.add(update);
			}
		}

		public synchronized void execute()
		{
			if (updates.iterator().hasNext())
			{
				synchronized (connection)
				{
					connection.begin(ReadWrite.WRITE);
					try
					{
						connection.update(updates);
						connection.commit();
					} catch (final RuntimeException e)
					{
						connection.abort();
						throw e;
					}
				}
			}
			reset();
		}

		public synchronized void reset()
		{
			updates = null;
		}

	}

}
