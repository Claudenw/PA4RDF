package org.xenei.pa4rdf.bean.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.proxy.exception.InvokerException;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.xenei.pa4rdf.bean.EntityFactory;
import org.xenei.pa4rdf.bean.PredicateInfo;
import org.xenei.pa4rdf.bean.ResourceWrapper;
import org.xenei.pa4rdf.bean.SubjectInfo;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.exceptions.MissingAnnotation;
import org.xenei.pa4rdf.bean.handlers.EntityHandler;
import org.xenei.pa4rdf.bean.handlers.LiteralHandler;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Implements the invoker that the proxy uses.
 * 
 * This method intercepts the annotated entity methods as well as the
 * ResourceWrapper.getResource() method.
 */
public class ResourceEntityProxy implements MethodInterceptor
{
	private final Resource resource;
	
	private final EntityFactory factory;
	
	private final static Method GET_RESOURCE;
	private final static Method GET_SUBJECTINFO;
	private final static Method GET_RESOURCE_PROP;
	private final static Method SET_RESOURCE_RES;
	private final static Method SET_RESOURCE_WRAP;
	private final static Method ADD_RESOURCE_RES;
	private final static Method ADD_RESOURCE_WRAP;
	private final static Method SET_LITERAL;
	private final static Method ADD_LITERAL;
	private final static Method GET_LITERAL;
	private final static Method SET_ENTITY;
	private final static Method ADD_ENTITY;
	private final static Method GET_ENTITY;
	
	
	static {
		try
		{
			GET_RESOURCE = ResourceWrapper.class.getMethod("getResource");
			GET_SUBJECTINFO = ResourceWrapper.class.getMethod("getSubjectInfo");
			GET_RESOURCE_PROP = ResourceWrapper.class.getMethod("getResource", Property.class);
			SET_RESOURCE_RES = ResourceWrapper.class.getMethod("setResource", Property.class, RDFNode.class);
			SET_RESOURCE_WRAP = ResourceWrapper.class.getMethod("setResource", Property.class, ResourceWrapper.class);
			ADD_RESOURCE_RES = ResourceWrapper.class.getMethod("addResource", Property.class, RDFNode.class);
			ADD_RESOURCE_WRAP = ResourceWrapper.class.getMethod("addResource", Property.class, ResourceWrapper.class);
			SET_LITERAL = ResourceWrapper.class.getMethod("setLiteral", Property.class, Object.class);
			ADD_LITERAL = ResourceWrapper.class.getMethod("addLiteral", Property.class, Object.class);
			GET_LITERAL = ResourceWrapper.class.getMethod("getLiteral", Property.class, Class.class);
			SET_ENTITY = ResourceWrapper.class.getMethod("setLiteral", Property.class, Object.class);
			ADD_ENTITY = ResourceWrapper.class.getMethod("setLiteral", Property.class, Object.class);
			GET_ENTITY = ResourceWrapper.class.getMethod("getLiteral", Property.class, Class.class);
		} catch (NoSuchMethodException | SecurityException e)
		{
			throw new IllegalStateException( "Missing method: "+e.getMessage(), e );
		}
	
		
	}
	/**
	 * The constructor
	 * 
	 * @param factory
	 *            The magic bean factory to use.
	 * @param resource
	 *            The resource that is being wrapped
	 */
	public ResourceEntityProxy(final EntityFactory factory,
			final Resource resource)
	{
		if (resource.getModel() == null)
		{
			final Model m = ModelFactory.createDefaultModel();
			this.resource = m.createResource(resource.getURI());
		} else
		{
			this.resource = resource;
		}
		
		this.factory = factory;
	}

	/**
	 * Invokes an method on the proxy.
	 */
	@Override
	public Object intercept(final Object obj, final Method method,
			final Object[] args, final MethodProxy proxy) throws Throwable
	{
		// handle the cases where the method is not abstract
		if (!Modifier.isAbstract(method.getModifiers()))
		{
			return interceptNonAbstract(obj, method, args, proxy);
		} else
		{
			return interceptAnnotated(obj, method, args, proxy);
		}
	}

	private Object interceptAnnotated(final Object obj, final Method method,
			final Object[] args, final MethodProxy proxy) throws Throwable
	{

		
		/* handle the resource wrapper method calls. */
		// if (ResourceEntityProxy.GET_RESOURCE.equals(m))
		if (method.equals( GET_RESOURCE ))		
		{
			return getResource();
		}

		SubjectInfo subjectInfo = factory.getSubjectInfo(method.getDeclaringClass());
		
		if (method.equals( GET_SUBJECTINFO))
		{
			return getSubjectInfo( method );
		}
		
		if (method.equals( GET_RESOURCE_PROP )) {  return getResource( (Property)args[0] ); }
		if (method.equals( SET_RESOURCE_RES )) {  setResource( (Property)args[0], (RDFNode)args[1] ); return null; }
		if (method.equals( SET_RESOURCE_WRAP )) {  setResource( (Property)args[0], (ResourceWrapper)args[1] ); return null;}
		if (method.equals( ADD_RESOURCE_RES )) {  addResource( (Property)args[0], (RDFNode)args[1] ); return null; }
		if (method.equals( ADD_RESOURCE_WRAP )) {  addResource( (Property)args[0], (ResourceWrapper)args[1] ); return null;}
		if (method.equals( SET_LITERAL )) {  setLiteral( (Property)args[0], args[1] ); return null;}
		if (method.equals( ADD_LITERAL )) {  addLiteral( (Property)args[0], args[1] ); return null;}
		if (method.equals( GET_LITERAL )) {  return getLiteral( (Property)args[0], (Class<?>)args[1] );}
		if (method.equals( SET_ENTITY )) {  setEntity( (Property)args[0], (Object)args[1] ); return null;}
		if (method.equals( ADD_ENTITY )) {  addEntity( (Property)args[0], (Class<?>)args[1] ); return null;}
		if (method.equals( GET_ENTITY )) {  return getEntity( (Property)args[0], (Class<?>)args[1] );}

		
		

		/* handle the normal annotations */
		SubjectInfo workingInfo = subjectInfo;
		if (method.getDeclaringClass() != subjectInfo.getImplementedClass())
		{
			// handle the case where T implements Resource and the method is
			// declared in the Resource interface.
			if (method.getDeclaringClass().isAssignableFrom(Resource.class)
					&& Resource.class.isAssignableFrom(
							subjectInfo.getImplementedClass()))
			{
				return method.invoke(resource, args);
			} else
			{
				workingInfo = factory.getSubjectInfo(method.getDeclaringClass());
			}
		}
		final PredicateInfo pi = workingInfo.getPredicateInfo(method);

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
					final Method resourceMethod = Resource.class
							.getMethod(method.getName(), argTypes);
					return resourceMethod.invoke(resource, args);
				} catch (final Exception e)
				{
					// do nothing thow exception ouside of if.
				}
			}
			throw new InvokerException(
					String.format("Null method (%s) called", method.getName()));
		}

		if (pi instanceof PredicateInfoImpl)
		{
			Object o = ((PredicateInfoImpl) pi).exec(factory, method, resource,
					args);
			subjectInfo.getPredicateInfo(method).getPostExec();
			for (final Method peMethod : subjectInfo.getPredicateInfo(method)
					.getPostExec())
			{
				switch (pi.getActionType())
				{
					case GETTER:
						o = peMethod.invoke(obj, o);
						break;

					case EXISTENTIAL:
					case REMOVER:
					case SETTER:
						peMethod.invoke(obj, args);
				}

			}
			return o;
		}

		throw new RuntimeException(
				String.format("Internal predicateinfo class (%s) not (%s)",
						pi.getClass(), PredicateInfoImpl.class));
	}
	
	/* non abstract annotated methods
     override the implementation unless annotatation includes the
     update=true value -- not implemented */
    private Object interceptAnnotatedNonAbstract(final Object obj, final Method m, final Object[] args,
            final MethodProxy proxy, final Predicate p) throws Throwable {
        return interceptAnnotated( obj, m, args, proxy );
    }

  

	// handle the cases where the method is not abstract
	private Object interceptNonAbstract(final Object obj, final Method m,
			final Object[] args, final MethodProxy proxy) throws Throwable
	{
		// handle the special case methods
		if (m.getName().equals("toString") && !m.isVarArgs()
				&& (m.getParameterTypes().length == 0))
		{
			return String.format("%s[%s]", m.getDeclaringClass(), resource);
		}
		if (m.getName().equals("hashCode"))
		{
			return resource.hashCode();
		}
		if (m.getName().equals("equals"))
		{
			if (args[0] instanceof ResourceWrapper)
			{
				return resource
						.equals(((ResourceWrapper) args[0]).getResource());
			}
			if (args[0] instanceof Resource)
			{
				return resource.equals(args[0]);
			}
			return false;
		}
		
		final Predicate p = m.getAnnotation( Predicate.class );
        if (p != null) {
            return interceptAnnotatedNonAbstract( obj, m, args, proxy, p );
        }

		return proxy.invokeSuper(obj, args);
	}

	
	public Resource getResource()
	{
		return resource;
	}

	
	public EntityFactory getEntityFactory()
	{
		return factory;
	}

	public SubjectInfo getSubjectInfo( Method method )
	{
		return getEntityFactory().getSubjectInfo(method.getDeclaringClass());
	}

	public RDFNode getResource( Property property)
	{
		Statement stmt = resource.getProperty(property); 
		return stmt == null?null:stmt.getObject();
	}


	public void setResource(Property property, RDFNode rdfNode)
	{
		resource.removeAll(property);
		addResource(property, rdfNode);
	}

	public void setResource(Property property, ResourceWrapper wrapper)
	{
		setResource( property, wrapper.getResource());
	}
	
	public void addResource(Property property, RDFNode rdfNode)
	{
		resource.addProperty(property, rdfNode);
	}

	public void addResource(Property property, ResourceWrapper wrapper)
	{
		addResource( property, wrapper.getResource());
	}
	
	private LiteralHandler getLiteralHandler( Class<?> clazz )
	{
		RDFDatatype dataType = TypeMapper.getInstance().getTypeByClass(clazz);
		if (dataType == null)
		{
			throw new IllegalArgumentException( String.format( "%s is not a registered type", clazz.getName()));
		}
		return new LiteralHandler( dataType );
	}
	
	public void setLiteral(Property property, Object value)
	{		
		LiteralHandler handler = getLiteralHandler( value.getClass() );
		resource.removeAll(property);
		addResource(property, handler.createRDFNode(value));		
	}
	
	public void addLiteral(Property property, Object value)
	{	
		addResource( property, getLiteralHandler( value.getClass() ).createRDFNode(value));
	}

	public <T> T getLiteral(Property property, Class<T> clazz)
	{	
		Statement stmt = resource.getProperty(property);
		if (stmt == null)
		{
			return null;
		}
		return clazz.cast( getLiteralHandler(clazz).parseObject( stmt.getObject()));
	}

	public <T> T getEntity(Property p, Class<T> clazz) throws MissingAnnotation
	{
		EntityHandler handler = new EntityHandler( factory, clazz );
		RDFNode node = getResource( p );		
		return node == null?null:factory.makeInstance(node, clazz);	
	}

	public void setEntity(Property property, Object entity)
	{
		EntityHandler handler = new EntityHandler( factory, entity.getClass());		
		addResource(property, handler.createRDFNode(entity));

	}
	
	public void addEntity(Property property, Object entity)
	{
		EntityHandler handler = new EntityHandler( factory, entity.getClass());		
		setResource(property, handler.createRDFNode(entity));
	}
	
	
	
}