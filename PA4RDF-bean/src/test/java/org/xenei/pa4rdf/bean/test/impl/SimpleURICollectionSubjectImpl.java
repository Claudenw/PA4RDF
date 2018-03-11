package org.xenei.pa4rdf.bean.test.impl;

import java.util.List;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;
import org.xenei.pa4rdf.bean.annotations.URI;
import org.xenei.pa4rdf.bean.exceptions.EntityFactoryRequiredException;

@Subject(namespace = "http://example.com/")
public class SimpleURICollectionSubjectImpl
{

	@Predicate(impl = true)
	public void addU(final RDFNode x)
	{
		throw new EntityFactoryRequiredException();
	}

	@Predicate(impl = true)
	public void addU(@URI final String x)
	{
		throw new EntityFactoryRequiredException();
	}

	@Predicate(impl = true)
	public void addU2(final RDFNode b)
	{
		throw new EntityFactoryRequiredException();
	}

	@Predicate(impl = true)
	public void addU2(@URI final String x)
	{
		throw new EntityFactoryRequiredException();
	}

	@Predicate(impl = true)
	public List<RDFNode> getU()
	{
		throw new EntityFactoryRequiredException();
	}

	@Predicate(impl = true, type = RDFNode.class)
	public ExtendedIterator<RDFNode> getU2()
	{
		throw new EntityFactoryRequiredException();
	}

	@Predicate(impl = true)
	public boolean hasU(@URI final String x)
	{
		throw new EntityFactoryRequiredException();
	}

	@Predicate(impl = true)
	public Boolean hasU2(@URI final String b)
	{
		throw new EntityFactoryRequiredException();
	}

	@Predicate(impl = true)
	public void removeU(@URI final String x)
	{
		throw new EntityFactoryRequiredException();
	}

	@Predicate(impl = true)
	public void removeU2(final RDFNode b)
	{
		throw new EntityFactoryRequiredException();
	}

	@Predicate(impl = true)
	public void removeU2(@URI final String b)
	{
		throw new EntityFactoryRequiredException();
	}

}
