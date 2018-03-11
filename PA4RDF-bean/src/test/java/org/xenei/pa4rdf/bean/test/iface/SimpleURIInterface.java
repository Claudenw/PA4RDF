package org.xenei.pa4rdf.bean.test.iface;

import org.apache.jena.rdf.model.RDFNode;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;
import org.xenei.pa4rdf.bean.annotations.URI;

@Subject(namespace = "http://example.com/")
public interface SimpleURIInterface
{
	public RDFNode getU();

	public boolean hasU();

	public void removeU();

	public void setU(RDFNode b);

	@Predicate
	public void setU(@URI String b);

}