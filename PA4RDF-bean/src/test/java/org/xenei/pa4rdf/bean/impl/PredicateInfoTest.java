package org.xenei.pa4rdf.bean.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Test;
import org.xenei.pa4rdf.bean.EntityFactory;
import org.xenei.pa4rdf.bean.PredicateInfo;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;
import org.xenei.pa4rdf.bean.handlers.LiteralHandler;
import org.xenei.pa4rdf.bean.test.ComponentA;
import org.xenei.pa4rdf.bean.test.iface.CollectionInterface;

public class PredicateInfoTest
{

	private final EntityFactory factory = mock(EntityFactory.class);
	private final Model model = ModelFactory.createDefaultModel();

	@Test
	public void testConstructor() throws Exception
	{

		final Method method = ComponentA.class.getMethod("setA", int.class);

		final EffectivePredicate predicate = new EffectivePredicate(method);

		final PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		final Resource resource = model
				.createResource("http://example.com/ComponentA");
		final Object[] args = { 1 };
		final Object result = impl.exec(factory, method, resource, args);
		assertNull(result);
		assertTrue(resource.hasLiteral(
				ResourceFactory.createProperty("http://example.com/a"), 1));

	}

	@Test
	public void isPredicateTest() throws Exception
	{

		assertTrue(PredicateInfo.isPredicate(X.class.getMethod("get")));
		assertFalse(PredicateInfo.isPredicate(X.class.getMethod("getNotPredicate")));
	}

	@Test
	public void getActionTypeTest() throws Exception
	{
		final Method method = ComponentA.class.getMethod("setA", int.class);

		final EffectivePredicate predicate = new EffectivePredicate(method);

		final PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals(ActionType.SETTER, impl.getActionType());
	}

	@Test
	public void getEffectivePredicateTest() throws Exception
	{
		final Method method = ComponentA.class.getMethod("setA", int.class);

		final EffectivePredicate predicate = new EffectivePredicate(method);

		final PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals(predicate, impl.getEffectivePredicate());
	}

	@Test
	public void getMethodNameTest() throws Exception
	{
		final Method method = ComponentA.class.getMethod("setA", int.class);

		final EffectivePredicate predicate = new EffectivePredicate(method);

		final PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals("setA", impl.getMethodName());

	}

	@Test
	public void getNamespaceTest() throws Exception
	{
		final Method method = ComponentA.class.getMethod("setA", int.class);

		final EffectivePredicate predicate = new EffectivePredicate(method);

		final PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals("http://example.com/", impl.getNamespace());

	}

	@Test
	public void getObjectHandlerTest() throws Exception
	{
		final Method method = ComponentA.class.getMethod("setA", int.class);

		final EffectivePredicate predicate = new EffectivePredicate(method);

		final PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals(LiteralHandler.class,
				impl.getObjectHandler(factory).getClass());

	}

	@Test
	public void getPostExecTest() throws Exception
	{
		final Method method = ComponentA.class.getMethod("setA", int.class);

		final EffectivePredicate predicate = new EffectivePredicate(method);

		final PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals(0, impl.getPostExec().size());

	}

	@Test
	public void getPropertyTest() throws Exception
	{
		final Method method = ComponentA.class.getMethod("setA", int.class);

		final EffectivePredicate predicate = new EffectivePredicate(method);

		final PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals(ResourceFactory.createResource("http://example.com/a"),
				impl.getProperty());

	}

	@Test
	public void getUriStringTest() throws Exception
	{
		final Method method = ComponentA.class.getMethod("setA", int.class);

		final EffectivePredicate predicate = new EffectivePredicate(method);

		final PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals("http://example.com/a", impl.getUriString());

	}

	@Test
	public void getValueClassTest() throws Exception
	{
		final Method method = ComponentA.class.getMethod("setA", int.class);

		final EffectivePredicate predicate = new EffectivePredicate(method);

		final PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals(int.class, impl.getValueClass());

	}
	
	@Test
	public void showSetReplacesValues() throws Exception
	{
		final Method method = ComponentA.class.getMethod("setA", int.class);

		final EffectivePredicate predicate = new EffectivePredicate(method);

		final PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);
		
		final Resource resource = model
				.createResource("http://example.com/ComponentA");
		Object[] args = new Object[] { Integer.valueOf(1) };
		impl.exec(factory, method, resource, args);
		args = new Object[] { Integer.valueOf(2) };
		impl.exec(factory, method, resource, args);
		assertEquals("Should only be one statement", 1, model.listStatements().toList().size());
	}

	@Test
	public void showAddDoesNotReplacesValues() throws Exception
	{
		final Method method = CollectionInterface.class.getMethod("addX", String.class);

		final EffectivePredicate predicate = new EffectivePredicate(method);

		final PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, String.class);
		
		final Resource resource = model
				.createResource("http://example.com/ComponentA");
		Object[] args = new Object[] { "one" };
		impl.exec(factory, method, resource, args);
		args = new Object[] { "two" };
		impl.exec(factory, method, resource, args);
		assertEquals("Should only be one statement", 2, model.listStatements().toList().size());
	}
	
	@Subject
	private static class X
	{
		@Predicate
		public int get()
		{
			return 1;
		}
		
		public String getNotPredicate() {
			return "Not Predicate";
		}
	}

}
