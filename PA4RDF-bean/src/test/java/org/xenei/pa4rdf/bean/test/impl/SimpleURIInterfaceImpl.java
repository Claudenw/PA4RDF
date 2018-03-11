package org.xenei.pa4rdf.bean.test.impl;

import org.apache.jena.rdf.model.RDFNode;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.URI;
import org.xenei.pa4rdf.bean.exceptions.EntityFactoryRequiredException;
import org.xenei.pa4rdf.bean.test.iface.SimpleURIInterface;

public class SimpleURIInterfaceImpl implements SimpleURIInterface
{

	@Override
	@Predicate(impl = true)
	public RDFNode getU()
	{
		throw new EntityFactoryRequiredException();
	}

	@Override
	@Predicate(impl = true)
	public boolean hasU()
	{
		throw new EntityFactoryRequiredException();
	}

	@Override
	@Predicate(impl = true)
	public void removeU()
	{
		throw new EntityFactoryRequiredException();
	}

	@Override
	@Predicate(impl = true)
	public void setU(final RDFNode b)
	{
		throw new EntityFactoryRequiredException();
	}

	@Override
	@Predicate(impl = true)
	public void setU(@URI final String b)
	{
		throw new EntityFactoryRequiredException();
	}

}
