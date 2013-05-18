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
import org.xenei.jena.entities.testing.iface.SimpleCollectionInterface;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.PredicateInfo;

public class MethodParserCollectionTest
{
	private MethodParser parser;
	
	private  EntityManager entityManager;
	private SubjectInfoImpl subjectInfo;

	@Before
	public void setUp() throws Exception
	{
		subjectInfo = new SubjectInfoImpl(SimpleCollectionInterface.class);
		final Map<String, Integer> addCount = countAdders(SimpleCollectionInterface.class.getMethods());
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
	public void testCollectionSetter() throws Exception
	{		
		Method m = SimpleCollectionInterface.class.getMethod( "addX", String.class );
		PredicateInfo pi = parser.parse( m, new EffectivePredicate( m ));
		assertNotNull( "addX not parsed",  pi );
		assertEquals( ActionType.SETTER, pi.getActionType());
		assertEquals( "addX", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/x", pi.getUriString());
		assertEquals( String.class, pi.getValueClass());

		
		pi = subjectInfo.getPredicateInfo( SimpleCollectionInterface.class.getMethod( "getX" ) );
		assertNotNull( "getX not parsed",  pi );
		assertEquals( ActionType.GETTER, pi.getActionType());
		assertEquals( "getX", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/x", pi.getUriString());
		assertEquals( List.class, pi.getValueClass());
		
		pi = subjectInfo.getPredicateInfo( SimpleCollectionInterface.class.getMethod( "hasX", String.class) );
		assertNotNull( "hasX not parsed",  pi );
		assertEquals( ActionType.EXISTENTIAL, pi.getActionType());
		assertEquals( "hasX", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/x", pi.getUriString());
		assertEquals( String.class, pi.getValueClass());
		
		pi = subjectInfo.getPredicateInfo( SimpleCollectionInterface.class.getMethod( "removeX", String.class));
		assertNotNull( "removeX not parsed",  pi );
		assertEquals( ActionType.REMOVER, pi.getActionType());
		assertEquals( "removeX", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/x", pi.getUriString());
		assertEquals( String.class, pi.getValueClass());
	}

	@Test
	public void testCollectionGetter() throws Exception
	{		
		Method m = SimpleCollectionInterface.class.getMethod( "getX");
		PredicateInfo pi = parser.parse( m, new EffectivePredicate( m ));
		assertNotNull( "getX not parsed",  pi );
		assertEquals( ActionType.GETTER, pi.getActionType());
		assertEquals( "getX", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/x", pi.getUriString());
		assertEquals( List.class, pi.getValueClass());
		
	}

	@Test
	public void testCollectionHas() throws Exception
	{		
		Method m = SimpleCollectionInterface.class.getMethod( "hasX", String.class );
		PredicateInfo pi = parser.parse( m, new EffectivePredicate( m ));
		assertNotNull( "hasX not parsed",  pi );
		assertEquals( ActionType.EXISTENTIAL, pi.getActionType());
		assertEquals( "hasX", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/x", pi.getUriString());
		assertEquals( String.class, pi.getValueClass());
		
	}
	
	@Test
	public void testCollectionRemove() throws Exception
	{		
		Method m = SimpleCollectionInterface.class.getMethod( "removeX", String.class);
		PredicateInfo pi = parser.parse( m, new EffectivePredicate( m ));
		assertNotNull( "removeX not parsed",  pi );
		assertEquals( ActionType.REMOVER, pi.getActionType());
		assertEquals( "removeX", pi.getMethodName());
		assertEquals( "http://example.com/", pi.getNamespace());
		assertEquals( "http://example.com/x", pi.getUriString());
		assertEquals( String.class, pi.getValueClass());
		
	}
}
