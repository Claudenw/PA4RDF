package org.xenei.jena.entities.testing.impl;

import com.hp.hpl.jena.rdf.model.Resource;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public class SimpleInterfaceImpl implements SimpleInterface
{

	public SimpleInterfaceImpl()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getX()
	{
		throw new EntityManagerRequiredException();
	}


	@Override
	@Predicate
	public void setX( final String x )
	{
		throw new EntityManagerRequiredException();
	}


	@Override
	public boolean hasX()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	public void removeX()
	{
		throw new EntityManagerRequiredException();
	}

}
