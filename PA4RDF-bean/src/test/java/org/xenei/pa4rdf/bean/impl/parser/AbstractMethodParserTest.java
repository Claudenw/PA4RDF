package org.xenei.pa4rdf.bean.impl.parser;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.xenei.pa4rdf.bean.MethodParser;
import org.xenei.pa4rdf.bean.PredicateInfo;
import org.xenei.pa4rdf.bean.datatypes.Init;
import org.xenei.pa4rdf.bean.impl.ActionType;
import org.xenei.pa4rdf.bean.impl.FactoryImpl;
import org.xenei.pa4rdf.bean.impl.SubjectInfoImpl;

public abstract class AbstractMethodParserTest
{

	protected final Map<String, Integer> addCount = new HashMap<String, Integer>();
	protected final Map<Method, PredicateInfo> PIMap = new HashMap<Method, PredicateInfo>();
	protected final SubjectInfoImpl subjectInfo;
	protected final MethodParser parser;
	protected String NS = "http://example.com/";

	static
	{
		Init.registerTypes();
	}

	protected AbstractMethodParserTest(Class<?> interfaceClass)
	{
		subjectInfo = new SubjectInfoImpl(interfaceClass);
		parser = new MethodParser(new FactoryImpl(), subjectInfo, addCount);
	}

	protected void assertSame(Method method)
	{
		assertSame(PIMap.get(method), subjectInfo.getPredicateInfo(method),
				method);
	}

	protected void assertSame(PredicateInfo expected, PredicateInfo actual,
			Method method)
	{
		Assert.assertNotNull(
				method.toGenericString() + "\n Missing actual predicate info "
						+ expected.getActionType(),
				actual);
		Assert.assertNotNull(
				method.toGenericString() + "\n Missing expected predicate info "
						+ expected.getActionType(),
				expected);

		checkEquals(actual, expected, method);

		Assert.assertEquals(method.toGenericString() + "\n Wrong action type",
				expected.getActionType(), actual.getActionType());
		Assert.assertEquals(method.toGenericString() + "\n Wrong method name",
				expected.getMethodName(), actual.getMethodName());
		Assert.assertEquals(method.toGenericString() + "\n Wrong namespace",
				expected.getNamespace(), actual.getNamespace());
		Assert.assertEquals(
				method.toGenericString() + "\n Found unexpected postExec",
				expected.getPostExec().size(), actual.getPostExec().size());
		Assert.assertEquals(method.toGenericString() + "\n Wrong property",
				expected.getProperty(), actual.getProperty());
		Assert.assertEquals(method.toGenericString() + "\n Wrong URI string",
				expected.getUriString(), actual.getUriString());
		Assert.assertEquals(method.toGenericString() + "\n Wrong value class",
				expected.getValueClass(), actual.getValueClass());

		// Assert.assertEquals( method.toGenericString()+"\n Wrong object
		// handler\n", OMMap.get( method ),
		// ((PredicateInfoImpl) actual).getObjectHandler( (EntityManager) null )
		// );
	}

	/*
	 * this is required because the predicate info equality checks for post exec
	 * method types to be equal
	 */
	private void checkEquals(PredicateInfo expected, PredicateInfo actual,
			Method method)
	{
		if (!new EqualsBuilder()
				.append(expected.getActionType(), actual.getActionType())
				.append(expected.getMethodName(), actual.getMethodName())
				.append(expected.getNamespace(), actual.getNamespace())
				.append(expected.getPostExec().size(),
						actual.getPostExec().size())
				.append(expected.getProperty(), actual.getProperty())
				.append(expected.getUriString(), actual.getUriString())
				.append(expected.getValueClass(), actual.getValueClass())
				.build())
		{
			System.err.println(method);
			printPredicateInfo("EXPECTED", expected);
			printPredicateInfo("ACTUAL", actual);
		}
	}

	private void printPredicateInfo(String title, PredicateInfo pi)
	{
		System.err.println(String.format(
				"%s%n\tAction Type:\t%s%n\tMethod:\\tt%s%n\tNamespace:\t%s%n\tProperty:\t%s%n\tURI:\t\t%s%n\tValue:\t\t%s%n\tPost Exec:\t%s%n",
				title, pi.getActionType(), pi.getMethodName(),
				pi.getNamespace(), pi.getProperty(), pi.getUriString(),
				pi.getValueClass(), pi.getPostExec()));
	}

	@SuppressWarnings("unchecked")
	protected PredicateInfo mockPredicateInfo(Method m, String shortName,
			ActionType type, @SuppressWarnings("rawtypes") Class valueClass,
			int execCount)
	{
		final PredicateInfo pi = mock(PredicateInfo.class);
		when(pi.getNamespace()).thenReturn(NS);
		when(pi.getUriString()).thenReturn(NS + shortName);
		when(pi.getProperty())
				.thenReturn(ResourceFactory.createProperty(NS + shortName));
		when(pi.getActionType()).thenReturn(type);
		when(pi.getMethodName()).thenReturn(m.getName());
		when(pi.getValueClass()).thenReturn(valueClass);
		new ArrayList<Annotation>();

		final List<Method> mthd = new ArrayList<Method>();
		for (int i = 0; i < execCount; i++)
		{
			mthd.add(m);
		}
		when(pi.getPostExec()).thenReturn(mthd);
		return pi;
	}
}
