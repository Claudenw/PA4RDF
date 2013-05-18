package org.xenei.jena.entities.testing.iface;

import com.hp.hpl.jena.rdf.model.Resource;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

@Subject( namespace = "http://example.com/" )
public interface SimpleInterface //extends Resource
{
	String getX();
	
	boolean hasX();
	
	void removeX();

	@Predicate
	void setX( String x );

}