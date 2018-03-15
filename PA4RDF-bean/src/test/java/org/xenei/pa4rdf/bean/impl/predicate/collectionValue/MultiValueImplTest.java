package org.xenei.pa4rdf.bean.impl.predicate.collectionValue;

import org.xenei.pa4rdf.bean.test.impl.MultiValueImpl;

public class MultiValueImplTest extends MultiValueInterfaceTest
{

	public MultiValueImplTest() throws NoSuchMethodException, SecurityException
	{
		super(MultiValueImpl.class);
		builder.setImpl(true).setNamespace("");
	}

	@Override
	protected void updateGetU()
	{
		super.updateGetU();
		builder.setType(null).setInternalType(null);
	}

	@Override
	protected void updateGetU3()
	{
		super.updateGetU3();
		builder.setType(null).setInternalType(null);
	}

	@Override
	protected void updateGetU4()
	{
		super.updateGetU4();
		builder.setType(null).setInternalType(null).setName("u4");
	}

}