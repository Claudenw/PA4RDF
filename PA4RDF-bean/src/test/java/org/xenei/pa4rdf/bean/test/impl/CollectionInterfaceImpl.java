package org.xenei.pa4rdf.bean.test.impl;

import java.util.List;

import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.exceptions.EntityFactoryRequiredException;
import org.xenei.pa4rdf.bean.test.iface.CollectionInterface;

public class CollectionInterfaceImpl implements CollectionInterface
{

	@Override
	@Predicate(impl = true)
	public void addX(final String x)
	{
		throw new EntityFactoryRequiredException();
	}

	@Override
	@Predicate(impl = true)
	public List<String> getX()
	{
		throw new EntityFactoryRequiredException();
	}

	@Override
	@Predicate(impl = true)
	public boolean hasX(final String x)
	{
		throw new EntityFactoryRequiredException();
	}

	@Override
	@Predicate(impl = true)
	public void removeX(final String x)
	{
		throw new EntityFactoryRequiredException();
	}

}
