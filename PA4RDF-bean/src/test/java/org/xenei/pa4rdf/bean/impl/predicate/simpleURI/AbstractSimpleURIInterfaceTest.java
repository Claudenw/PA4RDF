package org.xenei.pa4rdf.bean.impl.predicate.simpleURI;

import java.lang.reflect.Method;

import org.apache.jena.rdf.model.RDFNode;
import org.junit.Test;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.URI;
import org.xenei.pa4rdf.bean.impl.ActionType;
import org.xenei.pa4rdf.bean.impl.EffectivePredicate;
import org.xenei.pa4rdf.bean.impl.predicate.AbstractPredicateTest;

public abstract class AbstractSimpleURIInterfaceTest
		extends AbstractPredicateTest
{

	protected AbstractSimpleURIInterfaceTest(Class<?> interfaceClass)
			throws NoSuchMethodException, SecurityException
	{
		super(interfaceClass);

		builder.setNamespace("http://example.com/").setName("u")
				.setInternalType(RDFNode.class);

	}

	/*
	 * order Predicate : Getter Predicate : Other
	 * 
	 * Class method order with same name.
	 * 
	 * 
	 */
	@Override
	public void processOrderTest()
			throws NoSuchMethodException, SecurityException
	{

		/*
		 * @Predicate public void setU(@URI String b);
		 */

		final EffectivePredicate setU_S = new EffectivePredicate(
				interfaceClass.getMethod("setU", String.class));
		builder.setType(RDFNode.class);

		Method mthd = interfaceClass.getMethod("setU", RDFNode.class);
		final EffectivePredicate setU = new EffectivePredicate(mthd)
				.merge(setU_S);
		builder.setActionType(ActionType.SETTER);
		assertSame(builder, setU, mthd);

		mthd = interfaceClass.getMethod("getU");
		final EffectivePredicate getU = new EffectivePredicate(mthd)
				.merge(setU_S);
		builder.setActionType(ActionType.GETTER);
		assertSame(builder, getU, mthd);

		mthd = interfaceClass.getMethod("removeU");
		builder.setActionType(ActionType.REMOVER)
				.setType(Predicate.UNSET.class);
		final EffectivePredicate removeX = new EffectivePredicate(mthd)
				.merge(setU_S);
		assertSame(builder, removeX, mthd);

		mthd = interfaceClass.getMethod("hasU");
		builder.setActionType(ActionType.EXISTENTIAL);
		final EffectivePredicate hasU = new EffectivePredicate(mthd)
				.merge(setU_S);
		assertSame(builder, hasU, mthd);

	}

	@Test
	public void testParseGetter() throws Exception
	{
		builder.setActionType(ActionType.GETTER).setType(RDFNode.class);
		updateGetter();
		assertSame(builder, interfaceClass.getMethod("getU"));

	}

	protected void updateGetter() throws Exception
	{
	}

	@Test
	public void testParseSetterS() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setType(URI.class);
		updateSetterS();
		assertSame(builder, interfaceClass.getMethod("setU", String.class));
	}

	protected void updateSetterS() throws Exception
	{
	}

	@Test
	public void testParseSetterR() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setType(RDFNode.class)
				.setName("u");
		updateSetterR();
		assertSame(builder, interfaceClass.getMethod("setU", RDFNode.class));
	}

	protected void updateSetterR() throws Exception
	{
	}

	@Test
	public void testParseExistential() throws Exception
	{
		builder.setActionType(ActionType.EXISTENTIAL)
				.setType(Predicate.UNSET.class);
		updateHas();
		assertSame(builder, interfaceClass.getMethod("hasU"));

	}

	protected void updateHas() throws Exception
	{
	}

	@Test
	public void testParseRemover() throws Exception
	{
		builder.setActionType(ActionType.REMOVER)
				.setType(Predicate.UNSET.class);
		updateRemover();
		assertSame(builder, interfaceClass.getMethod("removeU"));

	}

	protected void updateRemover() throws Exception
	{
	}

}