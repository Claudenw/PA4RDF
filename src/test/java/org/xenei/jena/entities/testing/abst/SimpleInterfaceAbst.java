package org.xenei.jena.entities.testing.abst;

import com.hp.hpl.jena.rdf.model.Resource;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

@Subject( namespace = "http://example.com/" )
abstract public class SimpleInterfaceAbst implements SimpleInterface
{
	String getSomeValue() {
		return "SomeValue";
	}

}