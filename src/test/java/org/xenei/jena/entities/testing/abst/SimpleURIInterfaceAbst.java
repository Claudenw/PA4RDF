package org.xenei.jena.entities.testing.abst;

import com.hp.hpl.jena.rdf.model.RDFNode;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.testing.iface.SimpleURIInterface;

public abstract class SimpleURIInterfaceAbst implements SimpleURIInterface
{

	public String getSomeValue()
	{
		return "Some value";
	}

}
