package org.xenei.pa4rdf.bean.impl.predicate.collection;

import org.xenei.pa4rdf.bean.test.impl.CollectionSubjectImpl;

public class CollectionSubjectImplTest extends AbstractCollectionInterfaceTest
{
	public CollectionSubjectImplTest()
			throws NoSuchMethodException, SecurityException
	{
		super(CollectionSubjectImpl.class);
		builder.setImpl(true);
	}
}
