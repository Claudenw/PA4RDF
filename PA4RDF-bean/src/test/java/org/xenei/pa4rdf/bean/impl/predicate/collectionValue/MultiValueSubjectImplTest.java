package org.xenei.pa4rdf.bean.impl.predicate.collectionValue;

import org.xenei.pa4rdf.bean.test.impl.MultiValueSubjectImpl;

public class MultiValueSubjectImplTest extends MultiValueInterfaceTest
{

	public MultiValueSubjectImplTest()
			throws NoSuchMethodException, SecurityException
	{
		super(MultiValueSubjectImpl.class);
		builder.setImpl(true).setNamespace("");
	}

}
