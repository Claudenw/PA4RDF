package org.xenei.pa4rdf.bean.impl.parser.simple;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.pa4rdf.bean.PredicateInfo;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.impl.ActionType;
import org.xenei.pa4rdf.bean.impl.TypeChecker;
import org.xenei.pa4rdf.bean.impl.parser.AbstractMethodParserTest;

public abstract class AbstractSimpleInterfaceTest
		extends AbstractMethodParserTest
{

	protected final Method getter;
	protected final Method setter;
	protected final Method remover;
	protected final Method existential;

	protected AbstractSimpleInterfaceTest(Class<?> interfaceClass)
			throws NoSuchMethodException, SecurityException
	{
		super(interfaceClass);

		getter = interfaceClass.getMethod("getX");
		PIMap.put(getter, mockPredicateInfo(getter, "x", ActionType.GETTER,
				String.class, 0));

		setter = interfaceClass.getMethod("setX", String.class);
		PIMap.put(setter, mockPredicateInfo(setter, "x", ActionType.SETTER,
				String.class, 0));

		remover = interfaceClass.getMethod("removeX");
		PIMap.put(remover, mockPredicateInfo(remover, "x", ActionType.REMOVER,
				Predicate.UNSET.class, 0));

		existential = interfaceClass.getMethod("hasX");
		PIMap.put(existential,
				mockPredicateInfo(existential, "x", ActionType.EXISTENTIAL,
						TypeChecker.getPrimitiveClass(Boolean.class), 0));

		addCount.put("setX", Integer.valueOf(1));

	}

	@Test
	public void testParseGetter() throws Exception
	{
		final PredicateInfo predicateInfo = parser.parse(getter);
		assertSame(PIMap.get(getter), predicateInfo, getter);
		assertSame(getter);
		assertSame(setter);
		assertSame(existential);
		assertSame(remover);

	}

	@Test
	public void testParseSetter() throws Exception
	{
		final PredicateInfo predicateInfo = parser.parse(setter);
		assertSame(PIMap.get(setter), predicateInfo, setter);
		assertSame(getter);
		assertSame(setter);
		assertSame(existential);
		assertSame(remover);
	}

	@Test
	public void testParseExistential() throws Exception
	{
		final PredicateInfo predicateInfo = parser.parse(existential);
		assertSame(PIMap.get(existential), predicateInfo, existential);
		Assert.assertNull(subjectInfo.getPredicateInfo(getter));
		Assert.assertNull(subjectInfo.getPredicateInfo(setter));
		assertSame(existential);
		Assert.assertNull(subjectInfo.getPredicateInfo(remover));
	}

	@Test
	public void testParseRemover() throws Exception
	{
		final PredicateInfo predicateInfo = parser.parse(remover);
		assertSame(PIMap.get(remover), predicateInfo, remover);
		Assert.assertNull(subjectInfo.getPredicateInfo(getter));
		Assert.assertNull(subjectInfo.getPredicateInfo(setter));
		Assert.assertNull(subjectInfo.getPredicateInfo(existential));
		assertSame(remover);
	}

}