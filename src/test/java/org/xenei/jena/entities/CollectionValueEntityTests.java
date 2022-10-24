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
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.xenei.jena.entities.testing.abst.CollectionValueAnnoatedAbst;
import org.xenei.jena.entities.testing.iface.TestInterface;

public class CollectionValueEntityTests
{

	// BOOLEAN, CHAR, DOUBLE, FLOAT, LONG, INTEGER, STRING, RDFNODE, ENTITY,
	// URI, VOID

	private CollectionValueAnnoatedAbst tc;
	private Model m;
	private final EntityManager manager = EntityManagerFactory
			.getEntityManager();

	@BeforeEach
	public void setup() throws MissingAnnotation
	{
		m = ModelFactory.createDefaultModel();
		final Resource r = m
				.createResource("http://localhost/CollectionValueEntityTests");
		tc = manager.read(r, CollectionValueAnnoatedAbst.class);
	}

	@AfterEach
	public void teardown()
	{
		m.close();
	}

	@Test
	public void testBoolean()
	{
		tc.addBool(true);
		assertTrue(tc.hasBool(true));
		assertTrue(!tc.hasBool(false));
		assertEquals(1, tc.getBool().size());

		tc.addBool(false);
		assertTrue(tc.hasBool(true));
		assertTrue(tc.hasBool(false));
		assertEquals(2, tc.getBool().size());

		tc.addBool(false);
		assertEquals(2, tc.getBool().size());

		tc.removeBool(false);
		assertTrue(tc.hasBool(true));
		assertTrue(!tc.hasBool(false));
		assertEquals(1, tc.getBool().size());

	}

	@Test
	public void testChar()
	{
		final char c = 'a';
		final Character cc = Character.valueOf( c ) ;
		final char d = 'b';
		final Character dd = Character.valueOf(d);

		tc.addChar(c);
		assertTrue(tc.hasChar(c));
		assertTrue(tc.hasChar(cc));
		assertTrue(!tc.hasChar(d));
		assertTrue(!tc.hasChar(dd));
		assertEquals(1, tc.getChar().size());

		tc.addChar(dd);
		assertTrue(tc.hasChar(c));
		assertTrue(tc.hasChar(cc));
		assertTrue(tc.hasChar(d));
		assertTrue(tc.hasChar(dd));
		assertEquals(2, tc.getChar().size());

		tc.removeChar(cc);
		assertTrue(!tc.hasChar(c));
		assertTrue(!tc.hasChar(cc));
		assertTrue(tc.hasChar(d));
		assertTrue(tc.hasChar(dd));
		assertEquals(1, tc.getChar().size());

		tc.removeChar(d);
		assertTrue(!tc.hasChar(c));
		assertTrue(!tc.hasChar(cc));
		assertTrue(!tc.hasChar(d));
		assertTrue(!tc.hasChar(dd));
		assertEquals(0, tc.getChar().size());

	}

	@Test
	public void testDbl()
	{
		final double c = 3.14;
		final Double cc = Double.valueOf(c);

		final double d = 2.157;
		final Double dd = Double.valueOf(d);

		tc.addDbl(c);
		assertTrue(tc.hasDbl(c));
		assertTrue(tc.hasDbl(cc));
		assertTrue(!tc.hasDbl(d));
		assertTrue(!tc.hasDbl(dd));
		assertEquals(1, tc.getDbl().size());

		tc.addDbl(dd);
		assertTrue(tc.hasDbl(c));
		assertTrue(tc.hasDbl(cc));
		assertTrue(tc.hasDbl(d));
		assertTrue(tc.hasDbl(dd));
		assertEquals(2, tc.getDbl().size());

		tc.removeDbl(cc);
		assertTrue(!tc.hasDbl(c));
		assertTrue(!tc.hasDbl(cc));
		assertTrue(tc.hasDbl(d));
		assertTrue(tc.hasDbl(dd));
		assertEquals(1, tc.getDbl().size());

		tc.removeDbl(d);
		assertTrue(!tc.hasDbl(c));
		assertTrue(!tc.hasDbl(cc));
		assertTrue(!tc.hasDbl(d));
		assertTrue(!tc.hasDbl(dd));
		assertEquals(0, tc.getDbl().size());

	}

	@Test
	public void testEntity() throws MissingAnnotation
	{
		Resource r = m.createResource("cc");
		final TestInterface cc = manager.read(r, TestInterface.class);
		r = m.createResource("dd");
		final TestInterface dd = manager.read(r, TestInterface.class);

		tc.addEnt(cc);
		assertTrue(tc.hasEnt(cc));
		assertTrue(!tc.hasEnt(dd));
		assertEquals(1, tc.getEnt().size());

		tc.addEnt(dd);
		assertTrue(tc.hasEnt(cc));
		assertTrue(tc.hasEnt(dd));
		assertEquals(2, tc.getEnt().size());

		tc.removeEnt(cc);
		assertTrue(!tc.hasEnt(cc));
		assertTrue(tc.hasEnt(dd));
		assertEquals(1, tc.getEnt().size());

		tc.removeEnt(dd);
		assertTrue(!tc.hasEnt(cc));
		assertTrue(!tc.hasEnt(dd));
		assertEquals(0, tc.getEnt().size());

	}

	@Test
	public void testFlt()
	{
		final float c = 3.14F;
		final Float cc = Float.valueOf(c);

		final float d = 2.157F;
		final Float dd = Float.valueOf(d);

		tc.addFlt(c);
		assertTrue(tc.hasFlt(c));
		assertTrue(tc.hasFlt(cc));
		assertTrue(!tc.hasFlt(d));
		assertTrue(!tc.hasFlt(dd));
		assertEquals(1, tc.getFlt().size());

		tc.addFlt(dd);
		assertTrue(tc.hasFlt(c));
		assertTrue(tc.hasFlt(cc));
		assertTrue(tc.hasFlt(d));
		assertTrue(tc.hasFlt(dd));
		assertEquals(2, tc.getFlt().size());

		tc.removeFlt(cc);
		assertTrue(!tc.hasFlt(c));
		assertTrue(!tc.hasFlt(cc));
		assertTrue(tc.hasFlt(d));
		assertTrue(tc.hasFlt(dd));
		assertEquals(1, tc.getFlt().size());

		tc.removeFlt(d);
		assertTrue(!tc.hasFlt(c));
		assertTrue(!tc.hasFlt(cc));
		assertTrue(!tc.hasFlt(d));
		assertTrue(!tc.hasFlt(dd));
		assertEquals(0, tc.getFlt().size());
	}

	@Test
	public void testInt()
	{
		final int c = 3;
		final Integer cc = Integer.valueOf(c);

		final int d = 42;
		final Integer dd = Integer.valueOf(d);

		tc.addInt(c);
		assertTrue(tc.hasInt(c));
		assertTrue(tc.hasInt(cc));
		assertTrue(!tc.hasInt(d));
		assertTrue(!tc.hasInt(dd));
		assertEquals(1, tc.getInt().size());

		tc.addInt(dd);
		assertTrue(tc.hasInt(c));
		assertTrue(tc.hasInt(cc));
		assertTrue(tc.hasInt(d));
		assertTrue(tc.hasInt(dd));
		assertEquals(2, tc.getInt().size());

		tc.removeInt(cc);
		assertTrue(!tc.hasInt(c));
		assertTrue(!tc.hasInt(cc));
		assertTrue(tc.hasInt(d));
		assertTrue(tc.hasInt(dd));
		assertEquals(1, tc.getInt().size());

		tc.removeInt(d);
		assertTrue(!tc.hasInt(c));
		assertTrue(!tc.hasInt(cc));
		assertTrue(!tc.hasInt(d));
		assertTrue(!tc.hasInt(dd));
		assertEquals(0, tc.getInt().size());
	}

	@Test
	public void testLng()
	{
		final long c = 3;
		final Long cc = Long.valueOf(c);

		final long d = 42;
		final Long dd = Long.valueOf(d);

		tc.addLng(c);
		assertTrue(tc.hasLng(c));
		assertTrue(tc.hasLng(cc));
		assertTrue(!tc.hasLng(d));
		assertTrue(!tc.hasLng(dd));
		assertEquals(1, tc.getLng().size());

		tc.addLng(dd);
		assertTrue(tc.hasLng(c));
		assertTrue(tc.hasLng(cc));
		assertTrue(tc.hasLng(d));
		assertTrue(tc.hasLng(dd));
		assertEquals(2, tc.getLng().size());

		tc.removeLng(cc);
		assertTrue(!tc.hasLng(c));
		assertTrue(!tc.hasLng(cc));
		assertTrue(tc.hasLng(d));
		assertTrue(tc.hasLng(dd));
		assertEquals(1, tc.getLng().size());

		tc.removeLng(d);
		assertTrue(!tc.hasLng(c));
		assertTrue(!tc.hasLng(cc));
		assertTrue(!tc.hasLng(d));
		assertTrue(!tc.hasLng(dd));
		assertEquals(0, tc.getLng().size());

	}

	@Test
	public void testRdf()
	{
		final RDFNode cc = ResourceFactory.createResource("cc");

		final RDFNode dd = ResourceFactory.createResource("dd");
		;

		tc.addRDF(cc);
		assertTrue(tc.hasRDF(cc));
		assertTrue(!tc.hasRDF(dd));
		assertEquals(1, tc.getRDF().size());

		tc.addRDF(dd);
		assertTrue(tc.hasRDF(cc));
		assertTrue(tc.hasRDF(dd));
		assertEquals(2, tc.getRDF().size());

		tc.removeRDF(cc);
		assertTrue(!tc.hasRDF(cc));
		assertTrue(tc.hasRDF(dd));
		assertEquals(1, tc.getRDF().size());

		tc.removeRDF(dd);
		assertTrue(!tc.hasRDF(cc));
		assertTrue(!tc.hasRDF(dd));
		assertEquals(0, tc.getRDF().size());
	}

	@Test
	public void testStr()
	{
		final String cc = "c";

		final String dd = "d";

		tc.addStr(cc);
		assertTrue(tc.hasStr(cc));
		assertTrue(!tc.hasStr(dd));
		assertEquals(1, tc.getStr().size());

		tc.addStr(dd);
		assertTrue(tc.hasStr(cc));
		assertTrue(tc.hasStr(dd));
		assertEquals(2, tc.getStr().size());

		tc.removeStr(cc);
		assertTrue(!tc.hasStr(cc));
		assertTrue(tc.hasStr(dd));
		assertEquals(1, tc.getStr().size());

		tc.removeStr(dd);
		assertTrue(!tc.hasStr(cc));
		assertTrue(!tc.hasStr(dd));
		assertEquals(0, tc.getStr().size());
	}

	@Test
	public void testURI()
	{

		final Resource cc = ResourceFactory.createResource("cc");
		final Resource dd = ResourceFactory.createResource("dd");

		tc.addU("cc");
		assertTrue(tc.hasU(cc));
		assertTrue(tc.hasU("cc"));
		assertTrue(!tc.hasU(dd));
		assertTrue(!tc.hasU("dd"));
		assertEquals(1, tc.getU().size());
		assertEquals(1, tc.getU2().size());

		tc.addU("dd");
		assertTrue(tc.hasU(cc));
		assertTrue(tc.hasU("cc"));
		assertTrue(tc.hasU(dd));
		assertTrue(tc.hasU("dd"));
		assertEquals(2, tc.getU().size());
		assertEquals(2, tc.getU2().size());

		tc.removeU(cc);
		assertTrue(!tc.hasU(cc));
		assertTrue(!tc.hasU("cc"));
		assertTrue(tc.hasU(dd));
		assertTrue(tc.hasU("dd"));
		assertEquals(1, tc.getU().size());
		assertEquals(1, tc.getU2().size());

		tc.removeU("dd");
		assertTrue(!tc.hasU(cc));
		assertTrue(!tc.hasU("cc"));
		assertTrue(!tc.hasU(dd));
		assertTrue(!tc.hasU("dd"));
		assertEquals(0, tc.getU().size());
		assertEquals(0, tc.getU2().size());

	}
}
