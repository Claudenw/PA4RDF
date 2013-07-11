/*
 * Copyright 2012, XENEI.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xenei.jena.entities;

import org.apache.commons.proxy.exception.InvokerException;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.impl.EntityManagerImpl;

public class EntityManagerTest {
	
	EntityManager manager = new EntityManagerImpl();
	
	@Before
	public void setup()
	{
		PropertyConfigurator.configure("./src/test/resources/log4j.properties");
	}
	
	@Test
	public void testBasicParser() throws Exception
	{
		manager.getSubjectInfo(MultiValueObjectTestClass.class);
	}

	@Test
	public void testPathParser() throws Exception
	{
		manager.parseClasses( new String[] {"org.xenei.jena.entities"});
		SubjectInfo ci = manager.getSubjectInfo( MultiValueObjectTestClass.class );
		Assert.assertNotNull( ci.getPredicateInfo(MultiValueObjectTestClass.class.getMethod("getU" ) ));
	}
	
	@Test
	public void testPathParserWithBadClasses() throws Exception
	{
		try {
			manager.parseClasses( new String[] {"org.xenei.jena.bad_entities"});
			Assert.fail( "Should have thrown InvokerException" );
		}
		catch (InvokerException e)
		{
			// expected
		}
	}
	
	@Test
	public void testClassParser() throws Exception
	{
		manager.parseClasses( new String[] {MultiValueObjectTestClass.class.getName()});
		SubjectInfo ci = manager.getSubjectInfo( MultiValueObjectTestClass.class );
		Assert.assertNotNull( ci.getPredicateInfo(MultiValueObjectTestClass.class.getMethod("getU" ) ));
	}
}
