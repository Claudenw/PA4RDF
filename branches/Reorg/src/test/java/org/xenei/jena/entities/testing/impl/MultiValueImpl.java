package org.xenei.jena.entities.testing.impl;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.testing.iface.MultiValueInterface;
import org.xenei.jena.entities.testing.iface.TestInterface;

public class MultiValueImpl implements MultiValueInterface
{

	@Override
	@Predicate( impl = true )
	public void addBool( Boolean b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void addChar( Character b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void addDbl( Double b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void addEnt( TestInterface b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void addFlt( Float b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void addInt( Integer b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void addLng( Long b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void addRDF( RDFNode b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public void addStr( String b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void addU( RDFNode b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void addU( @URI String b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void addU3( RDFNode b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void addU3( @URI String b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public ExtendedIterator<Boolean> getBool()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public ExtendedIterator<Character> getChar()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public ExtendedIterator<Double> getDbl()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public ExtendedIterator<TestInterface> getEnt()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public ExtendedIterator<Float> getFlt()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public ExtendedIterator<Integer> getInt()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public ExtendedIterator<Long> getLng()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public ExtendedIterator<RDFNode> getRDF()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public ExtendedIterator<String> getStr()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public ExtendedIterator <RDFNode> getU()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public ExtendedIterator <String> getU2()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public ExtendedIterator <RDFNode> getU3()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public ExtendedIterator <String> getU4()
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public Boolean hasBool( Boolean b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public Boolean hasChar( Character b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public Boolean hasDbl( Double b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public Boolean hasEnt( TestInterface b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public Boolean hasFlt( Float b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public Boolean hasInt( Integer b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public Boolean hasLng( Long b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public Boolean hasRDF( RDFNode b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public Boolean hasStr( String b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public Boolean hasU( RDFNode b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public Boolean hasU( @URI String b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public Boolean hasU3( RDFNode b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public Boolean hasU3( @URI String b )
	{
		throw new EntityManagerRequiredException();
	}

	@Override
	@Predicate( impl = true )
	public void removeBool( Boolean b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void removeChar( Character b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void removeDbl( Double b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void removeEnt( TestInterface b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void removeFlt( Float b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void removeInt( Integer b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void removeLng( Long b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void removeRDF( RDFNode b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void removeStr( String b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void removeU( RDFNode b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void removeU( @URI String b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void removeU3( RDFNode b )
	{
		throw new EntityManagerRequiredException();

	}

	@Override
	@Predicate( impl = true )
	public void removeU3( @URI String b )
	{
		throw new EntityManagerRequiredException();

	}

}
