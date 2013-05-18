package org.xenei.jena.entities.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.impl.methodParserClasses.*;
import org.xenei.jena.entities.testing.iface.SimpleInterface;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.PredicateInfo;

public class MethodParserSimpleTest
{
	private MethodParser parser;
	
	private  EntityManager entityManager;
	private SubjectInfoImpl subjectInfo;

	@Before
	public void setUp() throws Exception
	{
		subjectInfo = new SubjectInfoImpl(SimpleInterface.class);
		final Map<String, Integer> addCount = countAdders(SimpleInterface.class.getMethods());
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
		Method m = SimpleInterface.class.getMethod( "setX", String.class );
		PredicateInfo pi = parser.parse( m, new EffectivePredicate( m ));
		assertNotNull( "setX not parsed",  pi );
		assertEquals( ActionType.SETTER, pi.getActionType());
		assertEquals( "setX", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/x", pi.getUriString());
		assertEquals( String.class, pi.getValueClass());

		
		pi = subjectInfo.getPredicateInfo( SimpleInterface.class.getMethod( "getX" ) );
		assertNotNull( "getX not parsed",  pi );
		assertEquals( ActionType.GETTER, pi.getActionType());
		assertEquals( "getX", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/x", pi.getUriString());
		assertEquals( String.class, pi.getValueClass());
		
		pi = subjectInfo.getPredicateInfo( SimpleInterface.class.getMethod( "hasX") );
		assertNotNull( "hasX not parsed",  pi );
		assertEquals( ActionType.EXISTENTIAL, pi.getActionType());
		assertEquals( "hasX", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/x", pi.getUriString());
		assertEquals( boolean.class, pi.getValueClass());
		
		pi = subjectInfo.getPredicateInfo( SimpleInterface.class.getMethod( "removeX"));
		assertNotNull( "removeX not parsed",  pi );
		assertEquals( ActionType.REMOVER, pi.getActionType());
		assertEquals( "removeX", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/x", pi.getUriString());
		assertEquals( null, pi.getValueClass());
	}

	@Test
	public void testStandardGetter() throws Exception
	{		
		Method m = SimpleInterface.class.getMethod( "getX");
		PredicateInfo pi = parser.parse( m, new EffectivePredicate( m ));
		assertNotNull( "getX not parsed",  pi );
		assertEquals( ActionType.GETTER, pi.getActionType());
		assertEquals( "getX", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/x", pi.getUriString());
		assertEquals( String.class, pi.getValueClass());
		
	}

	@Test
	public void testStandardHas() throws Exception
	{		
		Method m = SimpleInterface.class.getMethod( "hasX");
		PredicateInfo pi = parser.parse( m, new EffectivePredicate( m ));
		assertNotNull( "hasX not parsed",  pi );
		assertEquals( ActionType.EXISTENTIAL, pi.getActionType());
		assertEquals( "hasX", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/x", pi.getUriString());
		assertEquals( boolean.class, pi.getValueClass());
		
	}
	
	@Test
	public void testStandardRemove() throws Exception
	{		
		Method m = SimpleInterface.class.getMethod( "removeX");
		PredicateInfo pi = parser.parse( m, new EffectivePredicate( m ));
		assertNotNull( "removeX not parsed",  pi );
		assertEquals( ActionType.REMOVER, pi.getActionType());
		assertEquals( "removeX", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/x", pi.getUriString());
		assertEquals( null, pi.getValueClass());
		
	}
}
