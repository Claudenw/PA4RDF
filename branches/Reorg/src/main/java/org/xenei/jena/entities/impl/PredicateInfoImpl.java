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

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Map1;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.datatype.CharDatatype;
import org.xenei.jena.entities.impl.datatype.CharacterDatatype;
import org.xenei.jena.entities.impl.datatype.LongDatatype;
import org.xenei.jena.entities.impl.handlers.EntityHandler;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.impl.handlers.VoidHandler;

/**
 * The parsed information about a predicate method.
 * 
 * Use of this class registers three (3) new RDFDatatypes:
 * CharacterDatatype that parses Character objects
 * CharDatatype that parses char.class objects
 * LongDatatype that parses Longs and always returns a long.
 */
public class PredicateInfoImpl implements PredicateInfo
{
	private final ObjectHandler objectHandler;
	private Class<?> concreteType;
	private final Class<?> valueClass;
	private final String methodName;
	private Property property;
	private final ActionType actionType;
	private final EffectivePredicate predicate;

	static
	{
		RDFDatatype rtype = new CharacterDatatype();
		TypeMapper.getInstance().registerDatatype(rtype);
		rtype = new CharDatatype();
		TypeMapper.getInstance().registerDatatype(rtype);
		rtype = new LongDatatype();
		TypeMapper.getInstance().registerDatatype(rtype);
	}

	/**
	 * Create a sorted list of registered data types.
	 * 
	 * The format is a String.format format string for two (2) string inputs.
	 * The first one is the URI of the data type, the second the class name or a
	 * blank.
	 * To reverse the display use "%2$s | %1$s"
	 * 
	 * If the nullCLassString is null registered data types without classes will
	 * not be included.
	 * 
	 * @param format
	 *            The output format or "%s | %s" if not specified.
	 * @param nullClassString
	 *            the string to print for null java class.
	 * @return A sorted list of registeded data types.
	 */
	public static List<String> dataTypeDump( final String format,
			final String nullClassString )
	{
		final List<String> retval = new ArrayList<String>();
		final String fmt = StringUtils.defaultIfEmpty(format, "%s | %s");

		final TypeMapper mapper = TypeMapper.getInstance();

		for (final Iterator<RDFDatatype> iter = mapper.listTypes(); iter
				.hasNext();)
		{
			final RDFDatatype dt = iter.next();
			if ((dt.getJavaClass() != null) || (nullClassString != null))
			{
				retval.add(String.format(
						fmt,
						dt.getURI(),
						PredicateInfoImpl.dataTypeDump_ClassType(
								dt.getJavaClass(), nullClassString)));
			}
		}
		Collections.sort(retval);
		return retval;
	}

	// helper function for dataTypeDump to format java class
	private static String dataTypeDump_ClassType( final Class<?> clazz,
			final String nullClassString )
	{
		if (clazz == null)
		{
			return nullClassString;
		}
		if (clazz.isArray())
		{
			return PredicateInfoImpl.dataTypeDump_ClassType(
					clazz.getComponentType(), nullClassString)
					+ "[]";
		}
		return clazz.getName();
	}

	/**
	 * Get the ObjectHandler for a predicate.
	 * 
	 * @param entityManager
	 *            The entity manager this to use.
	 * @param returnType
	 *            The ObjectHandler of the proper type
	 * @param pred
	 *            The EffectivePredicate definition.
	 * @return The object handler.
	 */
	public static ObjectHandler getHandler( final EntityManager entityManager,
			final Class<?> returnType, final EffectivePredicate pred )
	{
		final TypeMapper typeMapper = TypeMapper.getInstance();
		RDFDatatype dt = null;
		if ((pred != null) && !pred.literalType().equals(""))
		{
			dt = typeMapper.getSafeTypeByName(pred.literalType());
		}
		else
		{
			dt = typeMapper.getTypeByClass(returnType);
		}
		if (dt != null)
		{
			return new LiteralHandler(dt);
		}
		if (returnType != null)
		{
			if (returnType.getAnnotation(Subject.class) != null)
			{
				return new EntityHandler(entityManager, returnType);
			}
			if (RDFNode.class.isAssignableFrom(returnType))
			{
				return new ResourceHandler();
			}
			if (returnType.equals(URI.class))
			{
				return new UriHandler();
			}
		}
		return new VoidHandler();
	}

	/**
	 * Constructor.
	 * 
	 * @param entityManager
	 *            The EntityManager that this predicate is assocatied with.
	 * @param predicate
	 *            The EffectivePredicate instance that describes the predicate.
	 * @param methodName
	 *            The name of the method that this predicate calls.
	 * @param valueClass
	 *            The class type for the return (getter) or parameter (setter)
	 * @throws MissingAnnotation
	 *             If an annotation was required but not provided.
	 */
	public PredicateInfoImpl( final EntityManager entityManager,
			final EffectivePredicate predicate, final String methodName,
			final Class<?> valueClass )
	{
		this.methodName = methodName;
		this.actionType = ActionType.parse(methodName);
		this.valueClass = valueClass;
		if (predicate == null)
		{
			throw new IllegalArgumentException("Predicate may not be null");
		}
		this.predicate = predicate;
		if (URI.class.equals(predicate.type()))
		{
			concreteType = URI.class;
		}
		else
		{
			if ((valueClass != null)
					&& (Iterator.class.isAssignableFrom(valueClass) || Collection.class
							.isAssignableFrom(valueClass)))
			{
				concreteType = predicate.type();
			}
			else
			{
				concreteType = valueClass;
			}
		}

		if ((concreteType != null) && (valueClass != null))
		{
			if (concreteType.isPrimitive() && !valueClass.isPrimitive())
			{
				// This allows us to have setters that take primitives but
				// getters
				// that return objects.
				concreteType = valueClass;
			}
			else if (!concreteType.isPrimitive() && valueClass.isPrimitive())
			{
				concreteType = valueClass;
			}
		}
		objectHandler = PredicateInfoImpl.getHandler(entityManager,
				concreteType, predicate);
	}

	private Property createResourceProperty( final Resource resource )
	{
		return (resource.getModel() == null) ? ResourceFactory
				.createProperty(getUriString()) : resource.getModel()
				.createProperty(getUriString());
	}

	/**
	 * Execute the method against the resource with the arguments.
	 * 
	 * @param method
	 *            The method to execute
	 * @param resource
	 *            The resource to execute it against
	 * @param args
	 *            The arguments to the method.
	 * @return The result of the execution
	 * @throws NullPointerException
	 *             if the return type of the method is a primitive and the
	 *             predicate does not exist on the resource.
	 */
	public Object exec( final Method method, final Resource resource,
			final Object[] args )
	{
		final Property p = createResourceProperty(resource);
		Object retval = null;
		switch (actionType)
		{
			case GETTER:
				retval = execRead(resource, p);
				break;
			case SETTER:
				if (method.getName().startsWith("set"))
				{
					retval = execSet(resource, p, args);
				}
				else
				{
					retval = execAdd(resource, p, args);
				}
				break;
			case REMOVER:
				retval = execRemove(resource, p, args);
				break;
			case EXISTENTIAL:
				retval = execHas(resource, p, args);
				break;
		}
		return retval;
	}

	private Object execAdd( final Resource resource, final Property p,
			final Object[] args )
	{
		final boolean empty = objectHandler.isEmpty(args[0]);
		if (!empty || !predicate.emptyIsNull())
		{
			final RDFNode o = objectHandler.createRDFNode(args[0]);
			if (o != null)
			{
				resource.addProperty(p, o);
			}
		}
		return null;
	}

	private Object execHas( final Resource resource, final Property p,
			final Object[] args )
	{
		try
		{
			resource.getModel().enterCriticalSection(Lock.READ);

			return resource
					.hasProperty(p, objectHandler.createRDFNode(args[0]));
		}
		finally
		{
			resource.getModel().leaveCriticalSection();
		}
	}

	private Object execRead( final Resource resource, final Property p )
	{
		if (Iterator.class.isAssignableFrom(valueClass))
		{
			return execReadMultiple(resource, p);
		}
		else if (Collection.class.isAssignableFrom(valueClass))
		{
			return execReadCollection(resource, p);
		}
		else
		{
			return execReadSingle(resource, p);
		}
	}

	private Object execReadCollection( final Resource resource, final Property p )
	{
		resource.getModel().enterCriticalSection(Lock.READ);
		try
		{
			final NodeIterator iter = resource.getModel()
					.listObjectsOfProperty(resource, p);

			final ExtendedIterator<Object> oIter = iter
					.mapWith(new Map1<RDFNode, Object>() {

						@Override
						public Object map1( final RDFNode rdfNode )
						{
							return objectHandler.parseObject(rdfNode);
						}
					});
			if (List.class.isAssignableFrom(valueClass))
			{
				return oIter.toList();
			}
			else if (Set.class.isAssignableFrom(valueClass))
			{
				return oIter.toSet();
			}
			else if (Queue.class.isAssignableFrom(valueClass))
			{
				return new LinkedList<Object>(oIter.toList());
			}
			else
			{
				return oIter.toList();
			}

		}
		finally
		{
			resource.getModel().leaveCriticalSection();
		}
	}

	private ExtendedIterator<?> execReadMultiple( final Resource resource,
			final Property p )
	{
		resource.getModel().enterCriticalSection(Lock.READ);
		try
		{
			final NodeIterator iter = resource.getModel()
					.listObjectsOfProperty(resource, p);

			return iter.mapWith(new Map1<RDFNode, Object>() {

				@Override
				public Object map1( final RDFNode rdfNode )
				{
					return objectHandler.parseObject(rdfNode);
				}
			});
		}
		finally
		{
			resource.getModel().leaveCriticalSection();
		}
	}

	private Object execReadSingle( final Resource resource, final Property p )
	{
		try
		{
			resource.getModel().enterCriticalSection(Lock.READ);

			final StmtIterator iter = resource.listProperties(p);
			final Object retval = null;
			try
			{
				if (iter.hasNext())
				{
					final Statement s = iter.next();
					return objectHandler.parseObject(s.getObject());
				}
			}
			finally
			{
				iter.close();
			}
			if (retval == null)
			{
				if (concreteType.isPrimitive())
				{
					throw new NullPointerException(
							String.format(
									"Null valueClass (%s) was assigned to a variable of primitive type: %s",
									this.methodName, concreteType));
				}

			}
			return retval;
		}
		finally
		{
			resource.getModel().leaveCriticalSection();
		}
	}

	private Object execRemove( final Resource resource, final Property p,
			final Object[] args )
	{
		try
		{
			resource.getModel().enterCriticalSection(Lock.WRITE);
			if (valueClass == null)
			{
				resource.removeAll(p);
			}
			else
			{
				resource.getModel().remove(resource, p,
						objectHandler.createRDFNode(args[0]));
			}
			return null;
		}
		finally
		{
			resource.getModel().leaveCriticalSection();
		}
	}

	private Object execSet( final Resource resource, final Property p,
			final Object[] args )
	{
		try
		{
			resource.getModel().enterCriticalSection(Lock.WRITE);
			resource.removeAll(p); // just in case it get set by another thread
									// first.
			return execAdd(resource, p, args);
		}
		finally
		{
			final List<Statement> l = resource.listProperties(p).toList();
			if (l.size() > 1)
			{
				final Logger log = LoggerFactory
						.getLogger(PredicateInfoImpl.class);
				try
				{
					throw new Exception(String.format(
							"Error processing %s.set%s", resource, p));
				}
				catch (final Exception e)
				{
					log.error("Error:", e);
					for (final Statement s : l)
					{
						log.error("Statement: {} ", s.asTriple());
					}
				}

			}
			resource.getModel().leaveCriticalSection();
		}
	}

	/**
	 * Get the action type for the functin.
	 */
	@Override
	public ActionType getActionType()
	{
		return actionType;
	}

	public EffectivePredicate getEffectivePredicate()
	{
		return predicate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.PredicateInfo#getFunction()
	 */
	@Override
	public String getMethodName()
	{
		return methodName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.PredicateInfo#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return predicate.namespace();
	}

	public ObjectHandler getObjectHandler()
	{
		return objectHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.PredicateInfo#getProperty()
	 */
	@Override
	public Property getProperty()
	{
		if (property == null)
		{
			property = ResourceFactory.createProperty(getUriString());
		}
		return property;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.PredicateInfo#getUri()
	 */
	@Override
	public String getUriString()
	{
		return predicate.namespace() + predicate.name();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.PredicateInfo#getValue()
	 */
	@Override
	public Class<?> getValueClass()
	{
		return valueClass;
	}

	@Override
	public String toString()
	{
		return String.format("%s(%s)", methodName, valueClass);
	}
}
