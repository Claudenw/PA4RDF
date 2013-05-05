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
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.SingleValueObjectTestClass.SubPredicate;

public class SingleValueObjectEntityTests
{

	private SingleValueObjectTestClass tc;
	private Model model;
	private EntityManager manager;

	@Before
	public void setup()
	{
		manager = EntityManagerFactory.getEntityManager();
		model = ModelFactory.createDefaultModel();
		final Resource r = model
				.createResource("http://localhost/SingleValueObjectEntityTests");
		tc = manager.read(r, SingleValueObjectTestClass.class);
	}

	@After
	public void teardown()
	{
		model.close();
	}

	@Test
	public void testBoolean()
	{
		tc.setBool(true);
		Assert.assertTrue(tc.isBool());
		tc.setBool(false);
		Assert.assertTrue(!tc.isBool());
		tc.setBool(Boolean.TRUE);
		Assert.assertTrue(tc.isBool());
		tc.setBool(Boolean.FALSE);
		Assert.assertTrue(!tc.isBool());
		tc.removeBool();
		Assert.assertNull(tc.isBool());
	}

	@Test
	public void testChar()
	{
		final char c = 'a';
		final Character cc = new Character(c);
		tc.setChar(c);
		Assert.assertEquals(cc, tc.getChar());
		tc.setChar('x');
		Assert.assertTrue(!cc.equals(tc.getChar()));
		tc.setChar(cc);
		Assert.assertEquals(cc, tc.getChar());
		tc.removeChar();
		Assert.assertNull(tc.getChar());
	}

	@Test
	public void testDbl()
	{
		final double c = 3.14;
		final Double cc = new Double(c);
		tc.setDbl(c);
		Assert.assertEquals(cc, tc.getDbl());
		tc.setDbl(0.0);
		Assert.assertTrue(!cc.equals(tc.getDbl()));
		tc.setDbl(cc);
		Assert.assertEquals(cc, tc.getDbl());
		tc.removeDbl();
		Assert.assertNull(tc.getDbl());
	}

	@Test
	public void testEntity()
	{
		Resource r = model.createResource("testclass");
		final TestClass c = manager.read(r, TestClass.class);
		tc.setEnt(c);
		Assert.assertEquals(c, tc.getEnt());
		r = model.createResource("testclass2");
		final TestClass cc = manager.read(r, TestClass.class);
		tc.setEnt(cc);
		Assert.assertTrue(!c.equals(tc.getEnt()));
		tc.removeEnt();
		Assert.assertNull(tc.getEnt());

	}

	@Test
	public void testFlt()
	{
		final float c = 3.14F;
		final Float cc = new Float(c);
		tc.setFlt(c);
		Assert.assertEquals(cc, tc.getFlt());
		tc.setFlt(0.0F);
		Assert.assertTrue(!cc.equals(tc.getFlt()));
		tc.setFlt(cc);
		Assert.assertEquals(cc, tc.getFlt());
		tc.removeFlt();
		Assert.assertNull(tc.getFlt());
	}

	@Test
	public void testInt()
	{
		final int c = 3;
		final Integer cc = new Integer(c);
		tc.setInt(c);
		Assert.assertEquals(cc, tc.getInt());
		tc.setInt(0);
		Assert.assertTrue(!cc.equals(tc.getInt()));
		tc.setInt(cc);
		Assert.assertEquals(cc, tc.getInt());
		tc.removeInt();
		Assert.assertNull(tc.getInt());
	}

	@Test
	public void testLng()
	{
		final long c = 3;
		final Long cc = new Long(c);
		tc.setLng(c);
		Assert.assertEquals(cc, tc.getLng());
		tc.setLng(0L);
		Assert.assertTrue(!cc.equals(tc.getLng()));
		tc.setLng(cc);
		Assert.assertEquals(cc, tc.getLng());
		tc.removeLng();
		Assert.assertNull(tc.getLng());
	}

	@Test
	public void testRdf()
	{
		final RDFNode n = model.createResource("rdfNode");
		tc.setRDF(n);
		Assert.assertEquals(n, tc.getRDF());
		tc.setRDF(model.createResource("anotherRdfNode"));
		Assert.assertTrue(!n.equals(tc.getStr()));
		tc.removeRDF();
		Assert.assertNull(tc.getRDF());

	}

	@Test
	public void testStr()
	{
		final String cc = "string";
		tc.setStr("string");
		Assert.assertEquals(cc, tc.getStr());
		tc.setStr("foo");
		Assert.assertTrue(!cc.equals(tc.getStr()));
		tc.setStr(cc);
		Assert.assertEquals(cc, tc.getStr());
		tc.removeStr();
		Assert.assertNull(tc.getStr());
	}

	@Test
	public void testSubPredicate()
	{
		Resource r = model.createResource("http://localhost/SubPredicateTest1");
		final SubPredicate sp = manager.read(r, SubPredicate.class);
		sp.setName("spTest");

		r = model.createResource("http://localhost/SubPredicateTest2");
		final SubPredicate sp2 = manager.read(r, SubPredicate.class);
		sp2.setName("spTest2");

		tc.setSubPredicate(sp);
		Assert.assertNotNull(tc.getSubPredicate());
		Assert.assertTrue(tc.getSubPredicate() instanceof SubPredicate);
		Assert.assertEquals(sp.getName(), tc.getSubPredicate().getName());

		tc.setSubPredicate(sp2);
		Assert.assertNotNull(tc.getSubPredicate());
		Assert.assertTrue(tc.getSubPredicate() instanceof SubPredicate);
		Assert.assertEquals(sp2.getName(), tc.getSubPredicate().getName());

		tc.removeSubPredicate();
		Assert.assertNull(tc.getSubPredicate());

	}

	@Test
	public void testURI()
	{
		final Resource r = ResourceFactory.createResource("uriTest");
		tc.setU(r);
		Assert.assertEquals(r, tc.getU());
		Assert.assertEquals("uriTest", tc.getU2());
		tc.setU("uriTest2");
		Assert.assertTrue(!r.equals(tc.getU()));
		tc.removeU();
		Assert.assertNull(tc.getU());
		Assert.assertNull(tc.getU2());
	}
}
