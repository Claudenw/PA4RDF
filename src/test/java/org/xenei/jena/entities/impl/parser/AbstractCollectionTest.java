package org.xenei.jena.entities.impl.parser;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.impl.ActionType;

public abstract class AbstractCollectionTest extends BaseAbstractParserTest
{

	protected AbstractCollectionTest( final Class<?> classUnderTest )
	{
		super(classUnderTest);
	}

	@Test
	public void testCollectionGetter() throws Exception
	{
		final Method m = classUnderTest.getMethod("getX");
		validatePredicateInfo( parser.parse(m), "getX", ActionType.GETTER, "x", List.class);
	}

	@Test
	public void testCollectionHas() throws Exception
	{
		final Method m = classUnderTest.getMethod("hasX", String.class);
		validatePredicateInfo( parser.parse(m), "hasX", ActionType.EXISTENTIAL, "x", String.class);
	}

	@Test
	public void testCollectionRemove() throws Exception
	{
		final Method m = classUnderTest.getMethod("removeX", String.class);
		validatePredicateInfo( parser.parse(m), "removeX", ActionType.REMOVER, "x", String.class);
	}

	@Test
	public void testCollectionSetter() throws Exception
	{
		final Method m = classUnderTest.getMethod("addX", String.class);
		validatePredicateInfo( parser.parse(m), "addX", ActionType.SETTER, "x", String.class);

		PredicateInfo pi = subjectInfo.getPredicateInfo(classUnderTest.getMethod("getX"));
		validatePredicateInfo( pi, "getX", ActionType.GETTER, "x", List.class);
		

		pi = subjectInfo.getPredicateInfo(classUnderTest.getMethod("hasX",
				String.class));
        validatePredicateInfo( pi, "hasX", ActionType.EXISTENTIAL, "x", String.class);
        
		pi = subjectInfo.getPredicateInfo(classUnderTest.getMethod("removeX",
				String.class));
		validatePredicateInfo( pi, "removeX", ActionType.REMOVER, "x", String.class);
	}
}
