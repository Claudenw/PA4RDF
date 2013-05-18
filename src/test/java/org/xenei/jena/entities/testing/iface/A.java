package org.xenei.jena.entities.testing.iface;

import com.hp.hpl.jena.rdf.model.Resource;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

@Subject( namespace = "http://example.com/" )
public interface A extends Resource
{
	String getX();

	String getZ();

	@Predicate
	void setX( String x );

	@Predicate( emptyIsNull = true )
	void setZ( String z );

}