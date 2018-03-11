package org.xenei.pa4rdf.bean.test.iface;

import org.xenei.pa4rdf.bean.ResourceWrapper;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;

@Subject(namespace = "http://example.com/")
public interface AWrapper extends ResourceWrapper
{
	String getX();

	String getZ();

	@Predicate
	void setX(String x);

	@Predicate(emptyIsNull = true)
	void setZ(String z);

}