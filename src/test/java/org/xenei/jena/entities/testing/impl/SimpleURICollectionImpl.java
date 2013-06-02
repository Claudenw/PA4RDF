package org.xenei.jena.entities.testing.impl;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import java.util.List;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.testing.iface.SimpleURICollectionInterface;

public class SimpleURICollectionImpl implements SimpleURICollectionInterface
{

	@Override	
	@Predicate( impl = true )
	public void addU( @URI String x )
	{
		throw new EntityManagerRequiredException();
	}

	@Override	
	@Predicate( impl = true )
	public void addU( RDFNode x )
	{
		throw new EntityManagerRequiredException();
	}
	
	@Override	
	@Predicate( impl = true )
	public void addU2( @URI String x )
	{
		throw new EntityManagerRequiredException();
	}
	@Override
	@Predicate( impl = true )
	public void addU2( RDFNode b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public List<RDFNode> getU()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public ExtendedIterator<RDFNode> getU2()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public boolean hasU( @URI String x )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public Boolean hasU2( @URI String b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public void removeU( @URI String x )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public void removeU2( RDFNode b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public void removeU2( @URI String b )
	{
		throw new EntityManagerRequiredException();
	}

}
