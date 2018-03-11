package org.xenei.pa4rdf.bean.handlers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class CollectionHandlerMakeCollectionTest
{
	private final String[] args = { "Hello", "World" };
	private final Iterator<String> oIter;
	private final CollectionHandler underTest;
	private final Class<?> expected;

	public CollectionHandlerMakeCollectionTest(Class<?> type, Class<?> expected)
	{
		this.expected = expected;
		this.oIter = Arrays.asList(args).iterator();
		underTest = new CollectionHandler(
				new LiteralHandler(XSDDatatype.XSDstring), type);
	}

	@Test
	public void testMakeCollection()
	{
		final Object o = underTest.makeCollection(oIter);
		Assert.assertTrue(expected.isAssignableFrom(o.getClass()));
		final List<?> lst = new ArrayList<Object>();
		lst.addAll((Collection) o);
		;
		assertEquals(args.length, lst.size());
		for (int i = 0; i < args.length; i++)
		{
			assertEquals("Error at " + i, args[i], lst.get(i));
		}
	}

	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> data()
	{
		return Arrays.asList(new Object[][] { { List.class, ArrayList.class },
				{ ArrayList.class, ArrayList.class },
				{ LinkedList.class, LinkedList.class },
				{ Vector.class, Vector.class }, { Stack.class, Stack.class },
				{ Queue.class, Queue.class },
				{ ArrayDeque.class, ArrayDeque.class },
				{ Set.class, HashSet.class },
				{ LinkedHashSet.class, LinkedHashSet.class },
				{ TreeSet.class, TreeSet.class },
				{ HashSet.class, HashSet.class },

				// import java.util.Iterator;
		});
	}

}
