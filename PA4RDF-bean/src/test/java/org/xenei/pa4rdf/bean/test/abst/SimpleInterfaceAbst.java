package org.xenei.pa4rdf.bean.test.abst;

import org.xenei.pa4rdf.bean.annotations.Subject;
import org.xenei.pa4rdf.bean.test.iface.SimpleInterface;

@Subject(namespace = "http://example.com/")
abstract public class SimpleInterfaceAbst implements SimpleInterface
{
	String getSomeValue()
	{
		return "SomeValue";
	}

}