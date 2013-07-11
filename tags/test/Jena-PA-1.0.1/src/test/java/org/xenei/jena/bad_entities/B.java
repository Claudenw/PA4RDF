package org.xenei.jena.bad_entities;

import com.hp.hpl.jena.rdf.model.Model;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

/**
 * A class that will fail.
 */
@Subject( namespace="http://example.com/")
public interface B 
{
	@Predicate
	void setX( String x );
	String getX();
	
	Model getModel();
	
}