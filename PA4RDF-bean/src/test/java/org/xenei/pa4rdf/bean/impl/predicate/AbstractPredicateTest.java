package org.xenei.pa4rdf.bean.impl.predicate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.apache.jena.datatypes.RDFDatatype;
import org.junit.Assert;
import org.junit.Test;
import org.xenei.pa4rdf.bean.datatypes.Init;
import org.xenei.pa4rdf.bean.impl.ActionType;
import org.xenei.pa4rdf.bean.impl.EffectivePredicate;

public abstract class AbstractPredicateTest
{

	protected EffectivePredicateBuilder builder;
	protected final Class<?> interfaceClass;

	static
	{
		Init.registerTypes();
	}

	protected AbstractPredicateTest(Class<?> interfaceClass)
	{
		builder = new EffectivePredicateBuilder();
		this.interfaceClass = interfaceClass;
	}

	protected void assertSame(EffectivePredicateBuilder builder, Method method)
	{
		assertSame(builder, new EffectivePredicate(method), method);
	}

	protected void assertSame(EffectivePredicateBuilder builder,
			EffectivePredicate actual, Method method)
	{
		final EffectivePredicate expected = builder.build();
		if (!actual.equals(expected))
		{
			System.err.println(method.toGenericString());
			System.err.print("ACTUAL ");
			System.err.println(actual.formattedString());
			System.err.print("EXPECTED ");
			System.err.println(expected.formattedString());
		}

		/*
		 * this.actionType = actionType; this.collectionType = collectionType;
		 * this.emptyIsNull = emptyIsNull; this.impl = impl; this.internalType =
		 * internalType; this.literalType = literalType; this.name = name;
		 * this.namespace = namespace; this.postExec = postExec; this.type =
		 * type; this.upcase = upcase;
		 */
		Assert.assertEquals(method.toGenericString() + "\n Wrong ACTION type",
				expected.actionType(), actual.actionType());
		Assert.assertEquals(
				method.toGenericString() + "\n Wrong COLLECTION type",
				expected.collectionType(), actual.collectionType());
		Assert.assertEquals(method.toGenericString() + "\n Wrong emptyIsNull",
				expected.emptyIsNull(), actual.emptyIsNull());
		Assert.assertEquals(method.toGenericString() + "\n Wrong impl",
				expected.impl(), actual.impl());
		Assert.assertEquals(method.toGenericString() + "\n Wrong INTERNAL type",
				expected.internalType(), actual.internalType());
		Assert.assertEquals(method.toGenericString() + "\n Wrong LITERAL type",
				expected.literalType(), actual.literalType());
		Assert.assertEquals(method.toGenericString() + "\n Wrong name",
				expected.name(), actual.name());
		Assert.assertEquals(method.toGenericString() + "\n Wrong namespace",
				expected.namespace(), actual.namespace());
		Assert.assertEquals(method.toGenericString() + "\n Wrong postExec",
				expected.postExec(), actual.postExec());
		Assert.assertEquals(method.toGenericString() + "\n Wrong type",
				expected.type(), actual.type());
		Assert.assertEquals(method.toGenericString() + "\n Wrong upcase",
				expected.upcase(), actual.upcase());
	}

	protected void assertOverride(EffectivePredicateBuilder builder,
			Method... methods) throws Exception
	{
		final Method method = methods[0];

		final Stack<EffectivePredicate> stack = new Stack<EffectivePredicate>();
		for (final Method m : methods)
		{
			stack.push(new EffectivePredicate(m));
		}

		EffectivePredicate actual = stack.pop();
		while (!stack.isEmpty())
		{
			final EffectivePredicate base = actual;
			actual = stack.pop();
			actual.merge(base);
		}
		assertSame(builder, actual, method);
	}

	/*
	 * order Predicate : Getter Predicate : Other
	 * 
	 * Class method order with same name.
	 * 
	 * 
	 */
	@Test
	public abstract void processOrderTest()
			throws NoSuchMethodException, SecurityException;

	public static class EffectivePredicateBuilder
	{
		private boolean upcase = false;
		private RDFDatatype literalType = null;
		private Class<?> internalType = null;
		private Class<?> collectionType = null;
		private Class<?> type = null;
		private boolean emptyIsNull = false;
		private boolean impl = false;
		private List<Method> postExec = null;
		private ActionType actionType;
		private String name;
		private String namespace;

		public EffectivePredicate build()
		{
			/*
			 * this.actionType = actionType; this.collectionType =
			 * collectionType; this.emptyIsNull = emptyIsNull; this.impl = impl;
			 * this.internalType = internalType; this.literalType = literalType;
			 * this.name = name; this.namespace = namespace; this.postExec =
			 * postExec; this.type = type; this.upcase = upcase;
			 */
			return new EffectivePredicate(actionType, collectionType,
					emptyIsNull, impl, internalType, literalType, name,
					namespace, postExec, type, upcase);

		}

		public EffectivePredicateBuilder setActionType(ActionType actionType)
		{
			this.actionType = actionType;
			return this;
		}

		public EffectivePredicateBuilder setName(String name)
		{
			this.name = name;
			return this;
		}

		public EffectivePredicateBuilder setNamespace(String namespace)
		{
			this.namespace = namespace;
			return this;
		}

		public EffectivePredicateBuilder setUpcase(boolean state)
		{
			upcase = state;
			return this;
		}

		public EffectivePredicateBuilder setLiteralType(RDFDatatype literalType)
		{
			this.literalType = literalType;
			return this;
		}

		public EffectivePredicateBuilder setType(Class<?> type)
		{
			this.type = type;
			return this;
		}

		public EffectivePredicateBuilder setInternalType(Class<?> internalType)
		{
			this.internalType = internalType;
			return this;
		}

		public EffectivePredicateBuilder setCollectionType(
				Class<?> collectionType)
		{
			this.collectionType = collectionType;
			return this;
		}

		public EffectivePredicateBuilder setEmptyIsNull(boolean state)
		{
			emptyIsNull = state;
			return this;
		}

		public EffectivePredicateBuilder setImpl(boolean state)
		{
			impl = state;
			return this;
		}

		public EffectivePredicateBuilder setPostExec(List<Method> lst)
		{
			postExec = lst;
			return this;
		}

		public EffectivePredicateBuilder setPostExec(Method... lst)
		{
			return setPostExec(Arrays.asList(lst));
		}

		public EffectivePredicateBuilder setPostExec(Method mthd)
		{
			postExec = new ArrayList<Method>();
			postExec.add(mthd);
			return this;
		}
	}
}
