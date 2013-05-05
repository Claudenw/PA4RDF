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
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResourceEntityProxyTest
{

	private Model model;
	private EntityManager manager;

	@Before
	public void setup()
	{
		model = ModelFactory.createMemModelMaker().createDefaultModel();
		manager = EntityManagerFactory.getEntityManager();
	}

	@Test
	public void testEquality()
	{
		final Resource r = model.createResource("http://localhost/foo");
		final TestClass ti1 = manager.read(r, TestClass.class);
		final TestClass ti2 = manager.read(r, TestClass.class);

		Assert.assertEquals(ti1, ti2);
		Assert.assertEquals(ti2, ti1);
		Assert.assertEquals(ti1.hashCode(), ti2.hashCode());
	}

	@Test
	public void testParsing() throws SecurityException, NoSuchMethodException
	{
		final Resource r = model.createResource("http://localhost/foo");
		manager.read(r, TestClass.class);
		final SubjectInfo ci = manager.getSubjectInfo(TestClass.class);
		final String namespaceStr = "http://localhost/test#";

		Assert.assertEquals(TestClass.class, ci.getImplementedClass());

		PredicateInfo pi = null;
		Method m = null;

		// BAR test

		m = TestClass.class.getMethod("setBar", String.class);
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("setBar", pi.getMethodName());
		Assert.assertEquals(namespaceStr, pi.getNamespace());
		Assert.assertEquals(namespaceStr + "bar", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

		m = TestClass.class.getMethod("getBar");
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("getBar", pi.getMethodName());
		Assert.assertEquals(namespaceStr, pi.getNamespace());
		Assert.assertEquals(namespaceStr + "bar", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

		m = TestClass.class.getMethod("removeBar");
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("removeBar", pi.getMethodName());
		Assert.assertEquals(namespaceStr, pi.getNamespace());
		Assert.assertEquals(namespaceStr + "bar", pi.getUriString());
		Assert.assertEquals(null, pi.getValueClass());

		// BAZ test

		m = TestClass.class.getMethod("addBaz", String.class);
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("addBaz", pi.getMethodName());
		Assert.assertEquals(namespaceStr, pi.getNamespace());
		Assert.assertEquals(namespaceStr + "baz", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

		m = TestClass.class.getMethod("getBaz");
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("getBaz", pi.getMethodName());
		Assert.assertEquals(namespaceStr, pi.getNamespace());
		Assert.assertEquals(namespaceStr + "baz", pi.getUriString());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());

		m = TestClass.class.getMethod("removeBaz", String.class);
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("removeBaz", pi.getMethodName());
		Assert.assertEquals(namespaceStr, pi.getNamespace());
		Assert.assertEquals(namespaceStr + "baz", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

		// flag test

		m = TestClass.class.getMethod("setFlag", Boolean.class);
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("setFlag", pi.getMethodName());
		Assert.assertEquals(namespaceStr, pi.getNamespace());
		Assert.assertEquals(namespaceStr + "flag", pi.getUriString());
		Assert.assertEquals(Boolean.class, pi.getValueClass());

		m = TestClass.class.getMethod("isFlag");
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("isFlag", pi.getMethodName());
		Assert.assertEquals(namespaceStr, pi.getNamespace());
		Assert.assertEquals(namespaceStr + "flag", pi.getUriString());
		Assert.assertEquals(Boolean.class, pi.getValueClass());

		m = TestClass.class.getMethod("removeFlag");
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("removeFlag", pi.getMethodName());
		Assert.assertEquals(namespaceStr, pi.getNamespace());
		Assert.assertEquals(namespaceStr + "flag", pi.getUriString());
		Assert.assertEquals(null, pi.getValueClass());
	}

	@Test
	public void testPropertyRenaming() throws SecurityException,
			NoSuchMethodException
	{
		final SubjectInfo ci = manager
				.getSubjectInfo(TestPropertyRenamingInterface.class);
		final String namespace = "http://localhost/different#";

		Assert.assertEquals(TestPropertyRenamingInterface.class,
				ci.getImplementedClass());

		PredicateInfo pi = null;
		Method m = null;

		// BAR test

		m = TestPropertyRenamingInterface.class.getMethod("setFoomer2",
				String.class);
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("setFoomer2", pi.getMethodName());
		Assert.assertEquals(namespace, pi.getNamespace());
		Assert.assertEquals(namespace + "foo2", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

		m = TestPropertyRenamingInterface.class.getMethod("getFoomer2");
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("getFoomer2", pi.getMethodName());
		Assert.assertEquals(namespace, pi.getNamespace());
		Assert.assertEquals(namespace + "foo2", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

		m = TestPropertyRenamingInterface.class.getMethod("removeFoomer2");
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("removeFoomer2", pi.getMethodName());
		Assert.assertEquals(namespace, pi.getNamespace());
		Assert.assertEquals(namespace + "foo2", pi.getUriString());
		Assert.assertEquals(null, pi.getValueClass());

		// BAZ test

		m = TestPropertyRenamingInterface.class.getMethod("addFoomer",
				String.class);
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("addFoomer", pi.getMethodName());
		Assert.assertEquals(namespace, pi.getNamespace());
		Assert.assertEquals(namespace + "foo", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

		m = TestPropertyRenamingInterface.class.getMethod("getFoomer");
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("getFoomer", pi.getMethodName());
		Assert.assertEquals(namespace, pi.getNamespace());
		Assert.assertEquals(namespace + "foo", pi.getUriString());
		Assert.assertEquals(ExtendedIterator.class, pi.getValueClass());

		m = TestPropertyRenamingInterface.class.getMethod("removeFoomer",
				String.class);
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("removeFoomer", pi.getMethodName());
		Assert.assertEquals(namespace, pi.getNamespace());
		Assert.assertEquals(namespace + "foo", pi.getUriString());
		Assert.assertEquals(String.class, pi.getValueClass());

		// flag test

		m = TestPropertyRenamingInterface.class.getMethod("setFoomer3",
				Boolean.class);
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("setFoomer3", pi.getMethodName());
		Assert.assertEquals(namespace, pi.getNamespace());
		Assert.assertEquals(namespace + "foo3", pi.getUriString());
		Assert.assertEquals(Boolean.class, pi.getValueClass());

		m = TestPropertyRenamingInterface.class.getMethod("isFoomer3");
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("isFoomer3", pi.getMethodName());
		Assert.assertEquals(namespace, pi.getNamespace());
		Assert.assertEquals(namespace + "foo3", pi.getUriString());
		Assert.assertEquals(Boolean.class, pi.getValueClass());

		m = TestPropertyRenamingInterface.class.getMethod("removeFoomer3");
		pi = ci.getPredicateInfo(m);
		Assert.assertEquals("removeFoomer3", pi.getMethodName());
		Assert.assertEquals(namespace, pi.getNamespace());
		Assert.assertEquals(namespace + "foo3", pi.getUriString());
		Assert.assertEquals(null, pi.getValueClass());
	}

	@Test
	public void testSingleGetSetRemove()
	{
		final Resource r = model.createResource("http://localhost/foo");
		final TestClass ti1 = manager.read(r, TestClass.class);

		ti1.setBar("foo");
		Assert.assertEquals("foo", ti1.getBar());
		ti1.removeBar();
		Assert.assertEquals(null, ti1.getBar());

		ti1.addBaz("foo");
		ti1.addBaz("foo2");
		List<String> result = ti1.getBaz().toList();
		Assert.assertEquals(2, result.size());
		Assert.assertTrue(result.contains("foo"));
		Assert.assertTrue(result.contains("foo2"));
		ti1.removeBaz("foo");
		result = ti1.getBaz().toList();
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(result.contains("foo2"));
		ti1.removeBaz("foo2");
		result = ti1.getBaz().toList();
		Assert.assertEquals(0, result.size());

		ti1.setFlag(true);
		Assert.assertTrue(ti1.isFlag());
		ti1.setFlag(false);
		Assert.assertTrue(!ti1.isFlag());
		ti1.removeFlag();
		Assert.assertNull(ti1.isFlag());

	}
}