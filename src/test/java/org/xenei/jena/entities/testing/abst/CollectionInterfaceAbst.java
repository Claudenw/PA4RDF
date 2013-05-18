package org.xenei.jena.entities.testing.abst;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFVisitor;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import java.util.List;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.testing.iface.CollectionInterface;

public abstract class CollectionInterfaceAbst implements CollectionInterface
{

	public String getSomeValue()
	{
		return "Some value";
	}

}
