package org.xenei.jena.entities.testing.bad;

import com.hp.hpl.jena.rdf.model.Model;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

/**
 * A class that will fail.
 * 
 * This should fail because getModel() does not have an implementation and this does not extend
 * Resource
 */
@Subject( namespace = "http://example.com/" )
public interface UnannotatedInterface
{
	Model getModel();

	String getZ();

	@Predicate
	void setZ( String z );

}