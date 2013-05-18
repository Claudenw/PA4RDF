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
import org.xenei.jena.entities.testing.abst.MultiValueObjectTestClass;
import org.xenei.jena.entities.testing.abst.TestClass;

public class MultiValueEntityTests
{

	// BOOLEAN, CHAR, DOUBLE, FLOAT, LONG, INTEGER, STRING, RDFNODE, ENTITY,
	// URI, VOID

	private MultiValueObjectTestClass tc;
	private Model m;
	private final EntityManager manager = EntityManagerFactory
			.getEntityManager();

	@Before
	public void setup() throws MissingAnnotation
	{
		m = ModelFactory.createDefaultModel();
		final Resource r = m
				.createResource("http://localhost/MultiValueEntityTests");
		tc = manager.read(r, MultiValueObjectTestClass.class);
	}

	@After
	public void teardown()
	{
		m.close();
	}

	@Test
	public void testBoolean()
	{
		tc.addBool(true);
		Assert.assertTrue(tc.hasBool(true));
		Assert.assertTrue(!tc.hasBool(false));
		Assert.assertEquals(1, tc.getBool().toSet().size());

		tc.addBool(false);
		Assert.assertTrue(tc.hasBool(true));
		Assert.assertTrue(tc.hasBool(false));
		Assert.assertEquals(2, tc.getBool().toSet().size());

		tc.addBool(false);
		Assert.assertEquals(2, tc.getBool().toSet().size());
		Assert.assertEquals(2, tc.getBool().toList().size());

		tc.removeBool(false);
		Assert.assertTrue(tc.hasBool(true));
		Assert.assertTrue(!tc.hasBool(false));
		Assert.assertEquals(1, tc.getBool().toSet().size());

	}

	@Test
	public void testChar()
	{
		final char c = 'a';
		final Character cc = new Character(c);
		final char d = 'b';
		final Character dd = new Character(d);

		tc.addChar(c);
		Assert.assertTrue(tc.hasChar(c));
		Assert.assertTrue(tc.hasChar(cc));
		Assert.assertTrue(!tc.hasChar(d));
		Assert.assertTrue(!tc.hasChar(dd));
		Assert.assertEquals(1, tc.getChar().toList().size());

		tc.addChar(dd);
		Assert.assertTrue(tc.hasChar(c));
		Assert.assertTrue(tc.hasChar(cc));
		Assert.assertTrue(tc.hasChar(d));
		Assert.assertTrue(tc.hasChar(dd));
		Assert.assertEquals(2, tc.getChar().toList().size());

		tc.removeChar(cc);
		Assert.assertTrue(!tc.hasChar(c));
		Assert.assertTrue(!tc.hasChar(cc));
		Assert.assertTrue(tc.hasChar(d));
		Assert.assertTrue(tc.hasChar(dd));
		Assert.assertEquals(1, tc.getChar().toList().size());

		tc.removeChar(d);
		Assert.assertTrue(!tc.hasChar(c));
		Assert.assertTrue(!tc.hasChar(cc));
		Assert.assertTrue(!tc.hasChar(d));
		Assert.assertTrue(!tc.hasChar(dd));
		Assert.assertEquals(0, tc.getChar().toList().size());

	}

	@Test
	public void testDbl()
	{
		final double c = 3.14;
		final Double cc = new Double(c);

		final double d = 2.157;
		final Double dd = new Double(d);

		tc.addDbl(c);
		Assert.assertTrue(tc.hasDbl(c));
		Assert.assertTrue(tc.hasDbl(cc));
		Assert.assertTrue(!tc.hasDbl(d));
		Assert.assertTrue(!tc.hasDbl(dd));
		Assert.assertEquals(1, tc.getDbl().toList().size());

		tc.addDbl(dd);
		Assert.assertTrue(tc.hasDbl(c));
		Assert.assertTrue(tc.hasDbl(cc));
		Assert.assertTrue(tc.hasDbl(d));
		Assert.assertTrue(tc.hasDbl(dd));
		Assert.assertEquals(2, tc.getDbl().toList().size());

		tc.removeDbl(cc);
		Assert.assertTrue(!tc.hasDbl(c));
		Assert.assertTrue(!tc.hasDbl(cc));
		Assert.assertTrue(tc.hasDbl(d));
		Assert.assertTrue(tc.hasDbl(dd));
		Assert.assertEquals(1, tc.getDbl().toList().size());

		tc.removeDbl(d);
		Assert.assertTrue(!tc.hasDbl(c));
		Assert.assertTrue(!tc.hasDbl(cc));
		Assert.assertTrue(!tc.hasDbl(d));
		Assert.assertTrue(!tc.hasDbl(dd));
		Assert.assertEquals(0, tc.getDbl().toList().size());

	}

	@Test
	public void testEntity() throws MissingAnnotation
	{
		Resource r = m.createResource("cc");
		final TestClass cc = manager.read(r, TestClass.class);
		r = m.createResource("dd");
		final TestClass dd = manager.read(r, TestClass.class);

		tc.addEnt(cc);
		Assert.assertTrue(tc.hasEnt(cc));
		Assert.assertTrue(!tc.hasEnt(dd));
		Assert.assertEquals(1, tc.getEnt().toList().size());

		tc.addEnt(dd);
		Assert.assertTrue(tc.hasEnt(cc));
		Assert.assertTrue(tc.hasEnt(dd));
		Assert.assertEquals(2, tc.getEnt().toList().size());

		tc.removeEnt(cc);
		Assert.assertTrue(!tc.hasEnt(cc));
		Assert.assertTrue(tc.hasEnt(dd));
		Assert.assertEquals(1, tc.getEnt().toList().size());

		tc.removeEnt(dd);
		Assert.assertTrue(!tc.hasEnt(cc));
		Assert.assertTrue(!tc.hasEnt(dd));
		Assert.assertEquals(0, tc.getEnt().toList().size());

	}

	@Test
	public void testFlt()
	{
		final float c = 3.14F;
		final Float cc = new Float(c);

		final float d = 2.157F;
		final Float dd = new Float(d);

		tc.addFlt(c);
		Assert.assertTrue(tc.hasFlt(c));
		Assert.assertTrue(tc.hasFlt(cc));
		Assert.assertTrue(!tc.hasFlt(d));
		Assert.assertTrue(!tc.hasFlt(dd));
		Assert.assertEquals(1, tc.getFlt().toList().size());

		tc.addFlt(dd);
		Assert.assertTrue(tc.hasFlt(c));
		Assert.assertTrue(tc.hasFlt(cc));
		Assert.assertTrue(tc.hasFlt(d));
		Assert.assertTrue(tc.hasFlt(dd));
		Assert.assertEquals(2, tc.getFlt().toList().size());

		tc.removeFlt(cc);
		Assert.assertTrue(!tc.hasFlt(c));
		Assert.assertTrue(!tc.hasFlt(cc));
		Assert.assertTrue(tc.hasFlt(d));
		Assert.assertTrue(tc.hasFlt(dd));
		Assert.assertEquals(1, tc.getFlt().toList().size());

		tc.removeFlt(d);
		Assert.assertTrue(!tc.hasFlt(c));
		Assert.assertTrue(!tc.hasFlt(cc));
		Assert.assertTrue(!tc.hasFlt(d));
		Assert.assertTrue(!tc.hasFlt(dd));
		Assert.assertEquals(0, tc.getFlt().toList().size());
	}

	@Test
	public void testInt()
	{
		final int c = 3;
		final Integer cc = new Integer(c);

		final int d = 42;
		final Integer dd = new Integer(d);

		tc.addInt(c);
		Assert.assertTrue(tc.hasInt(c));
		Assert.assertTrue(tc.hasInt(cc));
		Assert.assertTrue(!tc.hasInt(d));
		Assert.assertTrue(!tc.hasInt(dd));
		Assert.assertEquals(1, tc.getInt().toList().size());

		tc.addInt(dd);
		Assert.assertTrue(tc.hasInt(c));
		Assert.assertTrue(tc.hasInt(cc));
		Assert.assertTrue(tc.hasInt(d));
		Assert.assertTrue(tc.hasInt(dd));
		Assert.assertEquals(2, tc.getInt().toList().size());

		tc.removeInt(cc);
		Assert.assertTrue(!tc.hasInt(c));
		Assert.assertTrue(!tc.hasInt(cc));
		Assert.assertTrue(tc.hasInt(d));
		Assert.assertTrue(tc.hasInt(dd));
		Assert.assertEquals(1, tc.getInt().toList().size());

		tc.removeInt(d);
		Assert.assertTrue(!tc.hasInt(c));
		Assert.assertTrue(!tc.hasInt(cc));
		Assert.assertTrue(!tc.hasInt(d));
		Assert.assertTrue(!tc.hasInt(dd));
		Assert.assertEquals(0, tc.getInt().toList().size());
	}

	@Test
	public void testLng()
	{
		final long c = 3;
		final Long cc = new Long(c);

		final long d = 42;
		final Long dd = new Long(d);

		tc.addLng(c);
		Assert.assertTrue(tc.hasLng(c));
		Assert.assertTrue(tc.hasLng(cc));
		Assert.assertTrue(!tc.hasLng(d));
		Assert.assertTrue(!tc.hasLng(dd));
		Assert.assertEquals(1, tc.getLng().toList().size());

		tc.addLng(dd);
		Assert.assertTrue(tc.hasLng(c));
		Assert.assertTrue(tc.hasLng(cc));
		Assert.assertTrue(tc.hasLng(d));
		Assert.assertTrue(tc.hasLng(dd));
		Assert.assertEquals(2, tc.getLng().toList().size());

		tc.removeLng(cc);
		Assert.assertTrue(!tc.hasLng(c));
		Assert.assertTrue(!tc.hasLng(cc));
		Assert.assertTrue(tc.hasLng(d));
		Assert.assertTrue(tc.hasLng(dd));
		Assert.assertEquals(1, tc.getLng().toList().size());

		tc.removeLng(d);
		Assert.assertTrue(!tc.hasLng(c));
		Assert.assertTrue(!tc.hasLng(cc));
		Assert.assertTrue(!tc.hasLng(d));
		Assert.assertTrue(!tc.hasLng(dd));
		Assert.assertEquals(0, tc.getLng().toList().size());

	}

	@Test
	public void testRdf()
	{
		final RDFNode cc = ResourceFactory.createResource("cc");

		final RDFNode dd = ResourceFactory.createResource("dd");
		;

		tc.addRDF(cc);
		Assert.assertTrue(tc.hasRDF(cc));
		Assert.assertTrue(!tc.hasRDF(dd));
		Assert.assertEquals(1, tc.getRDF().toList().size());

		tc.addRDF(dd);
		Assert.assertTrue(tc.hasRDF(cc));
		Assert.assertTrue(tc.hasRDF(dd));
		Assert.assertEquals(2, tc.getRDF().toList().size());

		tc.removeRDF(cc);
		Assert.assertTrue(!tc.hasRDF(cc));
		Assert.assertTrue(tc.hasRDF(dd));
		Assert.assertEquals(1, tc.getRDF().toList().size());

		tc.removeRDF(dd);
		Assert.assertTrue(!tc.hasRDF(cc));
		Assert.assertTrue(!tc.hasRDF(dd));
		Assert.assertEquals(0, tc.getRDF().toList().size());
	}

	@Test
	public void testStr()
	{
		final String cc = "c";

		final String dd = "d";

		tc.addStr(cc);
		Assert.assertTrue(tc.hasStr(cc));
		Assert.assertTrue(!tc.hasStr(dd));
		Assert.assertEquals(1, tc.getStr().toList().size());

		tc.addStr(dd);
		Assert.assertTrue(tc.hasStr(cc));
		Assert.assertTrue(tc.hasStr(dd));
		Assert.assertEquals(2, tc.getStr().toList().size());

		tc.removeStr(cc);
		Assert.assertTrue(!tc.hasStr(cc));
		Assert.assertTrue(tc.hasStr(dd));
		Assert.assertEquals(1, tc.getStr().toList().size());

		tc.removeStr(dd);
		Assert.assertTrue(!tc.hasStr(cc));
		Assert.assertTrue(!tc.hasStr(dd));
		Assert.assertEquals(0, tc.getStr().toList().size());
	}

	@Test
	public void testURI()
	{

		final Resource cc = ResourceFactory.createResource("cc");
		final Resource dd = ResourceFactory.createResource("dd");

		tc.addU("cc");
		Assert.assertTrue(tc.hasU(cc));
		Assert.assertTrue(tc.hasU("cc"));
		Assert.assertTrue(!tc.hasU(dd));
		Assert.assertTrue(!tc.hasU("dd"));
		Assert.assertEquals(1, tc.getU().toList().size());
		Assert.assertEquals(1, tc.getU2().toList().size());

		tc.addU("dd");
		Assert.assertTrue(tc.hasU(cc));
		Assert.assertTrue(tc.hasU("cc"));
		Assert.assertTrue(tc.hasU(dd));
		Assert.assertTrue(tc.hasU("dd"));
		Assert.assertEquals(2, tc.getU().toList().size());
		Assert.assertEquals(2, tc.getU2().toList().size());

		tc.removeU(cc);
		Assert.assertTrue(!tc.hasU(cc));
		Assert.assertTrue(!tc.hasU("cc"));
		Assert.assertTrue(tc.hasU(dd));
		Assert.assertTrue(tc.hasU("dd"));
		Assert.assertEquals(1, tc.getU().toList().size());
		Assert.assertEquals(1, tc.getU2().toList().size());

		tc.removeU("dd");
		Assert.assertTrue(!tc.hasU(cc));
		Assert.assertTrue(!tc.hasU("cc"));
		Assert.assertTrue(!tc.hasU(dd));
		Assert.assertTrue(!tc.hasU("dd"));
		Assert.assertEquals(0, tc.getU().toList().size());
		Assert.assertEquals(0, tc.getU2().toList().size());

	}
}
