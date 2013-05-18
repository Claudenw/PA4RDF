package org.xenei.jena.entities.testing.iface;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import java.util.List;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;

@Subject( namespace = "http://example.com/" )
public interface SimpleURICollectionInterface extends Resource
{
	List<RDFNode> getU();
	
	boolean hasU( @URI String x );
	
	void removeU( @URI String x );

	@Predicate
	void addU( @URI String x );
	
	
	void addU2( RDFNode b );
	
	@Predicate( type = RDFNode.class )
	ExtendedIterator<RDFNode> getU2();

	Boolean hasU2( @URI String b );
	
	void removeU2( RDFNode b );

	void removeU2( @URI String b );


}