package org.xenei.jena.entities.bad;

import com.hp.hpl.jena.rdf.model.Resource;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

/**
 * A class that is OK for testing
 */
@Subject( namespace = "http://example.com/" )
public interface A
{
	String getX();

	@Predicate
	void setX( String x );

}