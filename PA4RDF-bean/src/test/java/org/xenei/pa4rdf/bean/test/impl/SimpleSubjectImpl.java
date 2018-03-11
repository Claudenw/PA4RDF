package org.xenei.pa4rdf.bean.test.impl;

import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;
import org.xenei.pa4rdf.bean.exceptions.EntityFactoryRequiredException;

@Subject(namespace = "http://example.com/")
public class SimpleSubjectImpl
{

	public SimpleSubjectImpl()
	{
	}

	@Predicate(impl = true)
	public String getX()
	{
		throw new EntityFactoryRequiredException();
	}

	@Predicate(impl = true)
	public boolean hasX()
	{
		throw new EntityFactoryRequiredException();
	}

	@Predicate(impl = true)
	public void removeX()
	{
		throw new EntityFactoryRequiredException();
	}

	@Predicate(impl = true)
	public void setX(final String x)
	{
		throw new EntityFactoryRequiredException();
	}

}
