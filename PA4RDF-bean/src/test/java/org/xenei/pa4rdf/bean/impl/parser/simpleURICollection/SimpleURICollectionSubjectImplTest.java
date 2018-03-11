package org.xenei.pa4rdf.bean.impl.parser.simpleURICollection;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.xenei.pa4rdf.bean.exceptions.MissingAnnotation;
import org.xenei.pa4rdf.bean.test.impl.SimpleURICollectionSubjectImpl;

public class SimpleURICollectionSubjectImplTest
		extends AbstractSimpleURICollectionTest
{

	public SimpleURICollectionSubjectImplTest()
			throws NoSuchMethodException, SecurityException
	{
		super(SimpleURICollectionSubjectImpl.class);
	}

	@Override
	@Test
	public void testParseSetterS() throws Exception
	{
		/*
		 * we are returning a collection and have multiple add methods so getU()
		 * should have a type set by other adders, but we are not parsing the
		 * full set here so parsing it fails.
		 */
		try
		{
			parser.parse(setterS);
			fail("Should have thrown MissingAnnotation");
		} catch (final MissingAnnotation expected)
		{
			// this is what should happen
		}

	}

}
