package org.xenei.jena.entities.testing.impl;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public class SimpleInterfaceImpl implements SimpleInterface
{
	public String lastGetX;
	public SimpleInterfaceImpl()
	{
	}

	@Override
	@Predicate( impl = true, postExec="postGetX" )
	public String getX()
	{
		throw new EntityManagerRequiredException();
	}
	
	public String postGetX(String s)
	{
		lastGetX = s;
		return s;
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
