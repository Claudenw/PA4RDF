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

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.testing.abst.TestPropertyRenamingInterface;
import org.xenei.jena.entities.testing.iface.TestInterface;

public class ResourceEntityProxyTest
{

	private Model model;
	private EntityManager manager;

	@BeforeEach
	public void setup()
	{
		model = ModelFactory.createMemModelMaker().createDefaultModel();
		manager = EntityManagerFactory.getEntityManager();
	}

	@Test
	public void testEquality() throws MissingAnnotation
	{
		final Resource r = model.createResource("http://localhost/foo");
		final TestInterface ti1 = manager.read(r, TestInterface.class);
		final TestInterface ti2 = manager.read(r, TestInterface.class);

		Assertions.assertEquals(ti1, ti2);
		Assertions.assertEquals(ti2, ti1);
		Assertions.assertEquals(ti1.hashCode(), ti2.hashCode());
	}

	@Test
	public void testParsing() throws Exception
	{
		final Resource r = model.createResource("http://localhost/foo");
		manager.read(r, TestInterface.class);
		final SubjectInfo ci = manager.getSubjectInfo(TestInterface.class);
		final String namespaceStr = "http://localhost/test#";

		Assertions.assertEquals(TestInterface.class, ci.getImplementedClass());

		PredicateInfo pi = null;
		Method m = null;

		// BAR test

		m = TestInterface.class.getMethod("setBar", String.class);
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("setBar", pi.getMethodName());
		Assertions.assertEquals(namespaceStr, pi.getNamespace());
		Assertions.assertEquals(namespaceStr + "bar", pi.getUriString());
		Assertions.assertEquals(String.class, pi.getValueClass());

		m = TestInterface.class.getMethod("getBar");
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("getBar", pi.getMethodName());
		Assertions.assertEquals(namespaceStr, pi.getNamespace());
		Assertions.assertEquals(namespaceStr + "bar", pi.getUriString());
		Assertions.assertEquals(String.class, pi.getValueClass());

		m = TestInterface.class.getMethod("removeBar");
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("removeBar", pi.getMethodName());
		Assertions.assertEquals(namespaceStr, pi.getNamespace());
		Assertions.assertEquals(namespaceStr + "bar", pi.getUriString());
		Assertions.assertEquals(null, pi.getValueClass());

		// BAZ test

		m = TestInterface.class.getMethod("addBaz", String.class);
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("addBaz", pi.getMethodName());
		Assertions.assertEquals(namespaceStr, pi.getNamespace());
		Assertions.assertEquals(namespaceStr + "baz", pi.getUriString());
		Assertions.assertEquals(String.class, pi.getValueClass());

		m = TestInterface.class.getMethod("getBaz");
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("getBaz", pi.getMethodName());
		Assertions.assertEquals(namespaceStr, pi.getNamespace());
		Assertions.assertEquals(namespaceStr + "baz", pi.getUriString());
		Assertions.assertEquals(ExtendedIterator.class, pi.getValueClass());

		m = TestInterface.class.getMethod("removeBaz", String.class);
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("removeBaz", pi.getMethodName());
		Assertions.assertEquals(namespaceStr, pi.getNamespace());
		Assertions.assertEquals(namespaceStr + "baz", pi.getUriString());
		Assertions.assertEquals(String.class, pi.getValueClass());

		// flag test

		m = TestInterface.class.getMethod("setFlag", Boolean.class);
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("setFlag", pi.getMethodName());
		Assertions.assertEquals(namespaceStr, pi.getNamespace());
		Assertions.assertEquals(namespaceStr + "flag", pi.getUriString());
		Assertions.assertEquals(Boolean.class, pi.getValueClass());

		m = TestInterface.class.getMethod("isFlag");
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("isFlag", pi.getMethodName());
		Assertions.assertEquals(namespaceStr, pi.getNamespace());
		Assertions.assertEquals(namespaceStr + "flag", pi.getUriString());
		Assertions.assertEquals(Boolean.class, pi.getValueClass());

		m = TestInterface.class.getMethod("removeFlag");
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("removeFlag", pi.getMethodName());
		Assertions.assertEquals(namespaceStr, pi.getNamespace());
		Assertions.assertEquals(namespaceStr + "flag", pi.getUriString());
		Assertions.assertEquals(null, pi.getValueClass());
	}

	@Test
	public void testPropertyRenaming() throws SecurityException,
			NoSuchMethodException
	{
		final SubjectInfo ci = manager
				.getSubjectInfo(TestPropertyRenamingInterface.class);
		final String namespace = "http://localhost/different#";

		Assertions.assertEquals(TestPropertyRenamingInterface.class,
				ci.getImplementedClass());

		PredicateInfo pi = null;
		Method m = null;

		// BAR test

		m = TestPropertyRenamingInterface.class.getMethod("setFoomer2",
				String.class);
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("setFoomer2", pi.getMethodName());
		Assertions.assertEquals(namespace, pi.getNamespace());
		Assertions.assertEquals(namespace + "foo2", pi.getUriString());
		Assertions.assertEquals(String.class, pi.getValueClass());

		m = TestPropertyRenamingInterface.class.getMethod("getFoomer2");
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("getFoomer2", pi.getMethodName());
		Assertions.assertEquals(namespace, pi.getNamespace());
		Assertions.assertEquals(namespace + "foo2", pi.getUriString());
		Assertions.assertEquals(String.class, pi.getValueClass());

		m = TestPropertyRenamingInterface.class.getMethod("removeFoomer2");
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("removeFoomer2", pi.getMethodName());
		Assertions.assertEquals(namespace, pi.getNamespace());
		Assertions.assertEquals(namespace + "foo2", pi.getUriString());
		Assertions.assertEquals(null, pi.getValueClass());

		// BAZ test

		m = TestPropertyRenamingInterface.class.getMethod("addFoomer",
				String.class);
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("addFoomer", pi.getMethodName());
		Assertions.assertEquals(namespace, pi.getNamespace());
		Assertions.assertEquals(namespace + "foo", pi.getUriString());
		Assertions.assertEquals(String.class, pi.getValueClass());

		m = TestPropertyRenamingInterface.class.getMethod("getFoomer");
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("getFoomer", pi.getMethodName());
		Assertions.assertEquals(namespace, pi.getNamespace());
		Assertions.assertEquals(namespace + "foo", pi.getUriString());
		Assertions.assertEquals(ExtendedIterator.class, pi.getValueClass());

		m = TestPropertyRenamingInterface.class.getMethod("removeFoomer",
				String.class);
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("removeFoomer", pi.getMethodName());
		Assertions.assertEquals(namespace, pi.getNamespace());
		Assertions.assertEquals(namespace + "foo", pi.getUriString());
		Assertions.assertEquals(String.class, pi.getValueClass());

		// flag test

		m = TestPropertyRenamingInterface.class.getMethod("setFoomer3",
				Boolean.class);
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("setFoomer3", pi.getMethodName());
		Assertions.assertEquals(namespace, pi.getNamespace());
		Assertions.assertEquals(namespace + "foo3", pi.getUriString());
		Assertions.assertEquals(Boolean.class, pi.getValueClass());

		m = TestPropertyRenamingInterface.class.getMethod("isFoomer3");
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("isFoomer3", pi.getMethodName());
		Assertions.assertEquals(namespace, pi.getNamespace());
		Assertions.assertEquals(namespace + "foo3", pi.getUriString());
		Assertions.assertEquals(Boolean.class, pi.getValueClass());

		m = TestPropertyRenamingInterface.class.getMethod("removeFoomer3");
		pi = ci.getPredicateInfo(m);
		Assertions.assertEquals("removeFoomer3", pi.getMethodName());
		Assertions.assertEquals(namespace, pi.getNamespace());
		Assertions.assertEquals(namespace + "foo3", pi.getUriString());
		Assertions.assertEquals(null, pi.getValueClass());
	}

	@Test
	public void testSingleGetSetRemove() throws Exception
	{
		final Resource r = model.createResource("http://localhost/foo");
		final TestInterface ti1 = manager.read(r, TestInterface.class);

		ti1.setBar("foo");
		Assertions.assertEquals("foo", ti1.getBar());
		ti1.removeBar();
		Assertions.assertEquals(null, ti1.getBar());

		ti1.addBaz("foo");
		ti1.addBaz("foo2");
		List<String> result = ti1.getBaz().toList();
		Assertions.assertEquals(2, result.size());
		Assertions.assertTrue(result.contains("foo"));
		Assertions.assertTrue(result.contains("foo2"));
		ti1.removeBaz("foo");
		result = ti1.getBaz().toList();
		Assertions.assertEquals(1, result.size());
		Assertions.assertTrue(result.contains("foo2"));
		ti1.removeBaz("foo2");
		result = ti1.getBaz().toList();
		Assertions.assertEquals(0, result.size());

		ti1.setFlag(true);
		Assertions.assertTrue(ti1.isFlag());
		ti1.setFlag(false);
		Assertions.assertTrue(!ti1.isFlag());
		ti1.removeFlag();
		Assertions.assertNull(ti1.isFlag());

	}
}