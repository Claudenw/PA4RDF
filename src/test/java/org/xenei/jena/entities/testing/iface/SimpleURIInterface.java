package org.xenei.jena.entities.testing.iface;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;

@Subject( namespace = "http://example.com/" )
public interface SimpleURIInterface extends Resource
{
	public RDFNode getU();
	
	public void setU( RDFNode b );
	
	public void removeU();
	
	public boolean hasU();

	@Predicate
	public void setU( @URI String b );

}