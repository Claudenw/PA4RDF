package org.xenei.pa4rdf.bean.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.xenei.pa4rdf.bean.SubjectInfo;
import org.xenei.pa4rdf.bean.exceptions.MissingAnnotation;
import org.xenei.pa4rdf.bean.test.fullMagic.FullMagic;
import org.xenei.pa4rdf.bean.test.fullMagic.FullMagicA;
import org.xenei.pa4rdf.bean.test.fullMagic.FullMagicB;
import org.xenei.pa4rdf.bean.test.partialMagic.PartialMagic;
import org.xenei.pa4rdf.bean.test.partialMagic.PartialMagicA;
import org.xenei.pa4rdf.bean.test.partialMagic.PartialMagicB;

public class FactoryImplTest
{

	FactoryImpl factory = new FactoryImpl();
	
	@Test
	public void testMagicParsing() throws Exception
	{
		factory.parse( FullMagic.class );
		assertNotNull( "FullMagic not found", factory.getSubjectInfo( FullMagic.class ) );
		assertNotNull( "FullMagicA not found", factory.getSubjectInfo( FullMagicA.class ) );
		assertNotNull( "FullMagicB not found", factory.getSubjectInfo( FullMagicB.class ) );
		
		factory.parse( PartialMagic.class );
		assertNotNull( "PartialMagic not found", factory.getSubjectInfo( PartialMagic.class ) );
		assertNotNull( "PartialMagicA not found", factory.getSubjectInfo( PartialMagicA.class ) );
		assertNotNull( "PartialMagicB not found", factory.getSubjectInfo( PartialMagicB.class ) );
		
	}
}
