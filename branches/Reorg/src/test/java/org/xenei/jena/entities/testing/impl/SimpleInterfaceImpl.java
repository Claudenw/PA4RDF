package org.xenei.jena.entities.testing.impl;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public class SimpleInterfaceImpl implements SimpleInterface
{

	public SimpleInterfaceImpl()
	{
	}

	@Override
	@Predicate( impl = true )
	public String getX()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public boolean hasX()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public void removeX()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public void setX( final String x )
	{
		throw new EntityManagerRequiredException();
	}

}
