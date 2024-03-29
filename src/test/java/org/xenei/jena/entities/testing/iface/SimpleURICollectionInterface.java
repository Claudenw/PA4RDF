package org.xenei.jena.entities.testing.iface;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.List;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;

/**
 * __U() shows add annotation
 * __U2() shows get annotation
 */
@Subject( namespace = "http://example.com/" )
public interface SimpleURICollectionInterface
{
	void addU( RDFNode x );

	@Predicate
	void addU( @URI String x );

	void addU2( RDFNode b );

	void addU2( @URI String x );

	//
	// List with URI annotations
	//
	List<RDFNode> getU();

	@Predicate( type = RDFNode.class )
	ExtendedIterator<RDFNode> getU2();

	//
	// Extended iterator with URI annotations
	//

	boolean hasU( @URI String x );

	Boolean hasU2( @URI String b );

	void removeU( @URI String x );

	void removeU2( RDFNode b );

	void removeU2( @URI String b );

}