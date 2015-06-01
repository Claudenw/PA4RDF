package org.xenei.jena.entities.testing.iface;

import org.apache.jena.rdf.model.RDFNode;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;

@Subject( namespace = "http://example.com/" )
public interface SimpleURIInterface
{
	public RDFNode getU();

	public boolean hasU();

	public void removeU();

	public void setU( RDFNode b );

	@Predicate
	public void setU( @URI String b );

}