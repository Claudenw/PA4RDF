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

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import org.apache.commons.proxy.exception.InvokerException;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.jena.entities.testing.abst.CollectionValueAnnoatedAbst;
import org.xenei.jena.entities.testing.abst.MultiValueAnnotatedAbst;
import org.xenei.jena.entities.testing.bad.UnannotatedInterface;

public class EntityManagerTest
{

	private final EntityManager manager = new EntityManagerImpl();

	@Before
	public void setup()
	{
		PropertyConfigurator.configure("./src/test/resources/log4j.properties");
	}

	@Test
	public void testBasicParser() throws Exception
	{
		manager.getSubjectInfo(MultiValueAnnotatedAbst.class);
		manager.getSubjectInfo(CollectionValueAnnoatedAbst.class);
	}

	@Test
	public void testClassParser() throws Exception
	{
		manager.parseClasses(new String[] {
				MultiValueAnnotatedAbst.class.getName(),
				CollectionValueAnnoatedAbst.class.getName() });
		SubjectInfo ci = manager.getSubjectInfo(MultiValueAnnotatedAbst.class);
		Assert.assertNotNull(ci.getPredicateInfo(MultiValueAnnotatedAbst.class
				.getMethod("getU")));
		ci = manager.getSubjectInfo(CollectionValueAnnoatedAbst.class);
		Assert.assertNotNull(ci
				.getPredicateInfo(CollectionValueAnnoatedAbst.class
						.getMethod("getU")));
	}

	@Test
	public void testPathParser() throws Exception
	{
		manager.parseClasses(new String[] { "org.xenei.jena.entities" });
		SubjectInfo ci = manager.getSubjectInfo(MultiValueAnnotatedAbst.class);
		Assert.assertNotNull(ci.getPredicateInfo(MultiValueAnnotatedAbst.class
				.getMethod("getU")));
		ci = manager.getSubjectInfo(CollectionValueAnnoatedAbst.class);
		Assert.assertNotNull(ci
				.getPredicateInfo(CollectionValueAnnoatedAbst.class
						.getMethod("getU")));
	}

	@Test
	public void testPathParserWithBadClasses() throws Exception
	{
		final Model model = ModelFactory.createDefaultModel();
		try
		{
			manager.parseClasses(new String[] { "org.xenei.jena.entities.bad" });
			manager.read(
					model.createResource(),
					org.xenei.jena.entities.testing.iface.SimpleInterface.class,
					UnannotatedInterface.class);
			Assert.fail("Should have thrown InvokerException");
		}
		catch (final InvokerException e)
		{
			// expected
		}
	}
}
