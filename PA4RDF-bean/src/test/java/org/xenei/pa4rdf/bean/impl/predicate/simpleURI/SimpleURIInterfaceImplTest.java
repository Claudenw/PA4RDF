package org.xenei.pa4rdf.bean.impl.predicate.simpleURI;

import org.xenei.pa4rdf.bean.test.impl.SimpleURIInterfaceImpl;

public class SimpleURIInterfaceImplTest extends AbstractSimpleURIInterfaceTest
{

	public SimpleURIInterfaceImplTest() throws Exception
	{
		super(SimpleURIInterfaceImpl.class);
		builder.setImpl(true).setNamespace("");
	}

}
