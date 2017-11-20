package org.xenei.jena.entities.impl.parser;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public class SimpleInterfaceTest extends AbstractSimpleTest
{

	public SimpleInterfaceTest()
	{
		super(SimpleInterface.class);
	}

	@Override
	protected Class<?>[] getGetAnnotations()
	{
		return new Class<?>[0];
	}

	@Override
	protected Class<?>[] getHasAnnotations()
	{
		return new Class<?>[0];
	}

	@Override
	protected Class<?>[] getRemoveAnnotations()
	{
		return new Class<?>[0];
	}

	@Override
	protected Class<?>[] getSetAnnotations()
	{
		return new Class<?>[] { Predicate.class };
	}

}
