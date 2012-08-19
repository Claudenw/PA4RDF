package org.xenei.jena.entities;

import com.hp.hpl.jena.rdf.model.Resource;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

@Subject( namespace="http://example.com/")
public interface A extends Resource
{
	@Predicate
	void setX( String x );
	String getX();
	
}