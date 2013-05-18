package org.xenei.jena.entities.testing.iface;

import com.hp.hpl.jena.rdf.model.Resource;

import java.util.List;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

@Subject( namespace = "http://example.com/" )
public interface SimpleCollectionInterface extends Resource
{
	List<String> getX();
	
	boolean hasX( String x );
	
	void removeX( String x );

	@Predicate
	void addX( String x );

}