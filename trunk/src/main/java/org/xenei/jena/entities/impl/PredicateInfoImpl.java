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
import java.util.Iterator;
import java.util.List;

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
	private ObjectHandler objectHandler;
	private Class<?> concreteType;
	private Class<?> valueClass;
	private String methodName;
	private Property property;
	private ActionType actionType;
	private EffectivePredicate predicate;

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
	 * Constructor.
	 * 
	 * @param entityManager The EntityManager that this predicate is assocatied with.
	 * @param predicate The EffectivePredicate instance that describes the predicate.
	 * @param methodName The name of the method that this predicate calls.
	 * @param valueClass The class type for the return (getter) or parameter (setter)
	 * @throws MissingAnnotation If an annotation was required but not provided.
	 */
	public PredicateInfoImpl( EntityManager entityManager, EffectivePredicate predicate,
			String methodName, Class<?> valueClass ) throws MissingAnnotation
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
			if (valueClass != null
					&& Iterator.class.isAssignableFrom(valueClass))
			{
				concreteType = predicate.type();
			}
			else
			{
				concreteType = valueClass;
			}
		}

		if (concreteType != null && valueClass != null)
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
		objectHandler = getHandler(entityManager, concreteType, predicate);
	}

	/**
	 * Execute the method against the resource with the arguments.
	 * 
	 * @param method The method to execute
	 * @param resource The resource to execute it against 
	 * @param args The arguments to the method.
	 * @return The result of the execution
	 * @throws NullPointerException if the return type of the method is a primitive and the 
	 * predicate does not exist on the resource.
	 */
	public Object exec( Method method, Resource resource, Object[] args )
	{
		Property p = createResourceProperty(resource);
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.PredicateInfo#getFunction()
	 */
	public String getMethodName()
	{
		return methodName;
	}

	/**
	 * Get the action type for the functin.
	 */
	public ActionType getActionType()
	{
		return actionType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.PredicateInfo#getNamespace()
	 */
	public String getNamespace()
	{
		return predicate.namespace();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.PredicateInfo#getValue()
	 */
	public Class<?> getValueClass()
	{
		return valueClass;
	}

	@Override
	public String toString()
	{
		return String.format("%s(%s)", methodName, valueClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.PredicateInfo#getUri()
	 */
	public String getUriString()
	{
		return predicate.namespace() + predicate.name();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xenei.jena.entities.impl.PredicateInfo#getProperty()
	 */
	public Property getProperty()
	{
		if (property == null)
		{
			property = ResourceFactory.createProperty(getUriString());
		}
		return property;
	}

	private Object execHas( Resource resource, Property p, Object[] args )
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

	private Object execRemove( Resource resource, Property p, Object[] args )
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

	private Object execAdd( Resource resource, Property p, Object[] args )
	{
		resource.addProperty(p, objectHandler.createRDFNode(args[0]));
		return null;
	}

	private Object execSet( Resource resource, Property p, Object[] args )
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
			List<Statement> l = resource.listProperties(p).toList();
			if (l.size() > 1)
			{
				Logger log = LoggerFactory.getLogger(PredicateInfoImpl.class);
				try
				{
					throw new Exception(String.format(
							"Error processing %s.set%s", resource, p));
				}
				catch (Exception e)
				{
					log.error("Error:", e);
					for (Statement s : l)
					{
						log.error("Statement: {} ", s.asTriple());
					}
				}

			}
			resource.getModel().leaveCriticalSection();
		}
	}

	private Object execRead( Resource resource, Property p )
	{
		if (Iterator.class.isAssignableFrom(valueClass))
		{
			return execReadMultiple(resource, p);
		}
		else
		{
			return execReadSingle(resource, p);
		}
	}

	private Object execReadSingle( Resource resource, Property p )
	{
		try
		{
			resource.getModel().enterCriticalSection(Lock.READ);

			StmtIterator iter = resource.listProperties(p);
			Object retval = null;
			if (iter.hasNext())
			{
				Statement s = iter.next();
				return objectHandler.parseObject(s.getObject());
			}
			iter.close();
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

	private ExtendedIterator<?> execReadMultiple( Resource resource, Property p )
	{
		try
		{
			resource.getModel().enterCriticalSection(Lock.READ);

			NodeIterator iter = resource.getModel().listObjectsOfProperty(
					resource, p);

			return iter.mapWith(new Map1<RDFNode, Object>() {

				public Object map1( RDFNode rdfNode )
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

	private Property createResourceProperty( Resource resource )
	{
		return (resource.getModel() == null) ? ResourceFactory
				.createProperty(getUriString()) : resource.getModel()
				.createProperty(getUriString());
	}

	public ObjectHandler getObjectHandler()
	{
		return objectHandler;
	}

	/**
	 * Get the ObjectHandler for a predicate.
	 * @param entityManager The entity manager this to use.
	 * @param returnType The ObjectHandler of the proper type
	 * @param pred The EffectivePredicate definition.
	 * @return The object handler.
	 */
	public static ObjectHandler getHandler( EntityManager entityManager,
			Class<?> returnType, EffectivePredicate pred )
	{
		TypeMapper typeMapper = TypeMapper.getInstance();
		RDFDatatype dt = null;
		if (pred != null && !pred.literalType().equals(""))
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
}
