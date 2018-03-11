package org.xenei.pa4rdf.bean.impl.predicate.collection;

import org.xenei.pa4rdf.bean.test.impl.CollectionInterfaceImpl;

public class CollectionInterfaceImplTest extends AbstractCollectionInterfaceTest
{
	public CollectionInterfaceImplTest() throws Exception
	{
		super(CollectionInterfaceImpl.class);
		builder.setImpl(true).setNamespace("");
	}
}
