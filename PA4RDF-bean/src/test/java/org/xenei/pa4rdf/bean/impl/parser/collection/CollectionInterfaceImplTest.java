package org.xenei.pa4rdf.bean.impl.parser.collection;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.pa4rdf.bean.PredicateInfo;
import org.xenei.pa4rdf.bean.test.impl.CollectionInterfaceImpl;

public class CollectionInterfaceImplTest extends AbstractCollectionInterfaceTest
{
	public CollectionInterfaceImplTest() throws Exception
	{
		super(CollectionInterfaceImpl.class);
	}

	@Override
	@Test
	public void testParseGetter() throws Exception
	{
		// getter changes output types
		final PredicateInfo predicateInfo = parser.parse(getter);
		assertSame(PIMap.get(getter), predicateInfo, getter);
		assertSame(getter);
		Assert.assertNull(subjectInfo.getPredicateInfo(setter));
		Assert.assertNull(subjectInfo.getPredicateInfo(existential));
		Assert.assertNull(subjectInfo.getPredicateInfo(remover));
	}
}
