package org.xenei.jena.entities.impl;

import static org.junit.Assert.*;

import com.hp.hpl.jena.rdf.model.RDFNode;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.impl.methodParserClasses.*;
import org.xenei.jena.entities.testing.iface.SimpleURIInterface;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.PredicateInfo;

public class MethodParserURITest
{
	private MethodParser parser;
	
	private  EntityManager entityManager;
	private SubjectInfoImpl subjectInfo;

	@Before
	public void setUp() throws Exception
	{
		subjectInfo = new SubjectInfoImpl(SimpleURIInterface.class);
		final Map<String, Integer> addCount = countAdders(SimpleURIInterface.class.getMethods());
		parser = new MethodParser( entityManager, subjectInfo, addCount );
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
	
	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testStandardSetter() throws Exception
	{		
		Method m = SimpleURIInterface.class.getMethod( "setU", String.class );
		PredicateInfo pi = parser.parse( m, new EffectivePredicate( m ));
		assertNotNull( "setU( String ) not parsed",  pi );
		assertEquals( ActionType.SETTER, pi.getActionType());
		assertEquals( "setU", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/u", pi.getUriString());
		assertEquals( UriHandler.class, ((PredicateInfoImpl)pi).getObjectHandler().getClass());
		assertEquals( String.class, pi.getValueClass());
		/*
		pi = subjectInfo.getPredicateInfo( SimpleURIInterface.class.getMethod( "setU", RDFNode.class ) );
		assertNotNull( "setU( RDFNode ) not parsed",  pi );
		assertEquals( ActionType.SETTER, pi.getActionType());
		assertEquals( "setU", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/u", pi.getUriString());
		assertEquals( RDFNode.class, pi.getValueClass());
		*/
		pi = subjectInfo.getPredicateInfo( SimpleURIInterface.class.getMethod( "getU" ) );
		assertNotNull( "getU not parsed",  pi );
		assertEquals( ActionType.GETTER, pi.getActionType());
		assertEquals( "getU", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/u", pi.getUriString());
		assertEquals( ResourceHandler.class, ((PredicateInfoImpl)pi).getObjectHandler().getClass());
		assertEquals( RDFNode.class, pi.getValueClass());
		
		pi = subjectInfo.getPredicateInfo( SimpleURIInterface.class.getMethod( "hasU") );
		assertNotNull( "hasU not parsed",  pi );
		assertEquals( ActionType.EXISTENTIAL, pi.getActionType());
		assertEquals( "hasU", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/u", pi.getUriString());
		assertEquals( boolean.class, pi.getValueClass());
		
		pi = subjectInfo.getPredicateInfo( SimpleURIInterface.class.getMethod( "removeU"));
		assertNotNull( "removeU not parsed",  pi );
		assertEquals( ActionType.REMOVER, pi.getActionType());
		assertEquals( "removeU", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/u", pi.getUriString());
		assertEquals( null, pi.getValueClass());
	}

	@Test
	public void testStandardGetter() throws Exception
	{		
		Method m = SimpleURIInterface.class.getMethod( "getU");
		PredicateInfo pi = parser.parse( m, new EffectivePredicate( m ));
		assertNotNull( "getU not parsed",  pi );
		assertEquals( ActionType.GETTER, pi.getActionType());
		assertEquals( "getU", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/u", pi.getUriString());
		assertEquals( RDFNode.class, pi.getValueClass());	
	}

	@Test
	public void testStandardHas() throws Exception
	{		
		Method m = SimpleURIInterface.class.getMethod( "hasU");
		PredicateInfo pi = parser.parse( m, new EffectivePredicate( m ));
		assertNotNull( "hasU not parsed",  pi );
		assertEquals( ActionType.EXISTENTIAL, pi.getActionType());
		assertEquals( "hasU", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/u", pi.getUriString());
		assertEquals( boolean.class, pi.getValueClass());
		
	}
	
	@Test
	public void testStandardRemove() throws Exception
	{		
		Method m = SimpleURIInterface.class.getMethod( "removeU");
		PredicateInfo pi = parser.parse( m, new EffectivePredicate( m ));
		assertNotNull( "removeU not parsed",  pi );
		assertEquals( ActionType.REMOVER, pi.getActionType());
		assertEquals( "removeU", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/u", pi.getUriString());
		assertEquals( null, pi.getValueClass());
		
	}
}
