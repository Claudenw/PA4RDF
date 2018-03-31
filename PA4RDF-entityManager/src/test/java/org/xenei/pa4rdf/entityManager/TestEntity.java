package org.xenei.pa4rdf.entityManager;

import org.xenei.pa4rdf.bean.ResourceWrapper;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;

@Subject( namespace="http://example.com/test#")
public interface TestEntity extends ResourceWrapper
{
	@Predicate
	public String getName();
	public void setName( String name );

}
