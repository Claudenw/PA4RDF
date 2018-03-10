package org.xenei.pa4rdf.bean.test;

import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;

@Subject( namespace="http://example.com/")
public interface ComponentA
{
	
	int getA();
	@Predicate()
	void setA( int i );
	boolean hasA();
	
	
}
