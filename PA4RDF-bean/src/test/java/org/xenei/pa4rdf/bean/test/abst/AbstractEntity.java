package org.xenei.pa4rdf.bean.test.abst;

import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;

@Subject(namespace = "http://example.com/")
public abstract class AbstractEntity
{
	abstract public String getX();

	public String getY()
	{
		return "Y";
	}

	@Predicate
	abstract public void setX(String x);

}
