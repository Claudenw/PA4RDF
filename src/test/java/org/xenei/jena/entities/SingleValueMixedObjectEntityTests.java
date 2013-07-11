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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.testing.abst.SingleValueMixedTypeTestClass;

public class SingleValueMixedObjectEntityTests
{

	private SingleValueMixedTypeTestClass tc;
	private Model model;

	@Before
	public void setup() throws Exception
	{
		final EntityManager manager = EntityManagerFactory.getEntityManager();
		model = ModelFactory.createDefaultModel();
		final Resource r = model
				.createResource("http://localhost/SingleValueMixedTypeEntityTests");
		tc = manager.read(r, SingleValueMixedTypeTestClass.class);
	}

	@After
	public void teardown()
	{
		model.close();
	}

	@Test
	public void testB()
	{
		tc.setB(true);
		Assert.assertTrue(tc.isB());
		tc.setB(false);
		Assert.assertTrue(!tc.isB());
		tc.setB(Boolean.TRUE);
		Assert.assertTrue(tc.isB());
		tc.setB(Boolean.FALSE);
		Assert.assertTrue(!tc.isB());

	}

	@Test
	public void testBoolean()
	{
		Assert.assertNull(tc.isBool());
		tc.setBool(true);
		Assert.assertTrue(tc.isBool());
		tc.setBool(false);
		Assert.assertTrue(!tc.isBool());
		tc.setBool(Boolean.TRUE);
		Assert.assertTrue(tc.isBool());
		tc.setBool(Boolean.FALSE);
		Assert.assertTrue(!tc.isBool());

	}

	@Test
	public void testC()
	{
		final char c = 'a';
		final Character cc = new Character(c);
		tc.setC(c);
		Assert.assertEquals(cc, Character.valueOf(tc.getC()));
		tc.setC('x');
		Assert.assertTrue(!cc.equals(tc.getC()));
		tc.setC(cc);
		Assert.assertEquals(cc, Character.valueOf(tc.getC()));

	}

	@Test
	public void testChar()
	{
		final char c = 'a';
		final Character cc = new Character(c);
		Assert.assertNull(tc.getChar());
		tc.setChar(c);
		Assert.assertEquals(cc, Character.valueOf(tc.getChar()));
		tc.setChar('x');
		Assert.assertTrue(!cc.equals(tc.getChar()));
		tc.setChar(cc);
		Assert.assertEquals(cc, Character.valueOf(tc.getChar()));
	}

	@Test
	public void testD()
	{
		final double c = 3.14;
		final Double cc = new Double(c);
		tc.setD(c);
		Assert.assertEquals(cc, Double.valueOf(tc.getD()));
		tc.setD(0.0);
		Assert.assertTrue(!cc.equals(tc.getD()));
		tc.setD(cc);
		Assert.assertEquals(cc, Double.valueOf(tc.getD()));
	}

	@Test
	public void testDbl()
	{
		final double c = 3.14;
		final Double cc = new Double(c);
		Assert.assertNull(tc.getDbl());
		tc.setDbl(c);
		Assert.assertEquals(cc, Double.valueOf(tc.getDbl()));
		tc.setDbl(0.0);
		Assert.assertTrue(!cc.equals(tc.getDbl()));
		tc.setDbl(cc);
		Assert.assertEquals(cc, Double.valueOf(tc.getDbl()));
	}

	@Test
	public void testF()
	{
		final float c = 3.14F;
		final Float cc = new Float(c);
		tc.setF(c);
		Assert.assertEquals(cc, Float.valueOf(tc.getF()));
		tc.setF(0.0F);
		Assert.assertTrue(!cc.equals(tc.getF()));
		tc.setF(cc);
		Assert.assertEquals(cc, Float.valueOf(tc.getF()));
	}

	@Test
	public void testFlt()
	{
		final float c = 3.14F;
		final Float cc = new Float(c);
		Assert.assertNull(tc.getFlt());
		tc.setFlt(c);
		Assert.assertEquals(cc, Float.valueOf(tc.getFlt()));
		tc.setFlt(0.0F);
		Assert.assertTrue(!cc.equals(tc.getFlt()));
		tc.setFlt(cc);
		Assert.assertEquals(cc, Float.valueOf(tc.getFlt()));

	}

	@Test
	public void testI()
	{
		final int c = 3;
		final Integer cc = new Integer(c);
		tc.setI(c);
		Assert.assertEquals(cc, Integer.valueOf(tc.getI()));
		tc.setI(0);
		Assert.assertTrue(!cc.equals(tc.getI()));
		tc.setI(cc);
		Assert.assertEquals(cc, Integer.valueOf(tc.getI()));
	}

	@Test
	public void testInt()
	{
		final int c = 3;
		final Integer cc = new Integer(c);
		Assert.assertNull(tc.getInt());
		tc.setInt(c);
		Assert.assertEquals(cc, Integer.valueOf(tc.getInt()));
		tc.setInt(0);
		Assert.assertTrue(!cc.equals(tc.getInt()));
		tc.setInt(cc);
		Assert.assertEquals(cc, Integer.valueOf(tc.getInt()));
	}

	@Test
	public void testL()
	{
		final long c = 3;
		final Long cc = new Long(c);
		tc.setL(c);
		Assert.assertEquals(cc, Long.valueOf(tc.getL()));
		tc.setL(0L);
		Assert.assertTrue(!cc.equals(tc.getL()));
		tc.setL(cc);
		Assert.assertEquals(cc, Long.valueOf(tc.getL()));
	}

	@Test
	public void testLng()
	{
		final long c = 3;
		final Long cc = new Long(c);
		Assert.assertNull(tc.getLng());
		tc.setLng(c);
		Assert.assertEquals(cc, Long.valueOf(tc.getLng()));
		tc.setLng(0L);
		Assert.assertTrue(!cc.equals(tc.getLng()));
		tc.setLng(cc);
		Assert.assertEquals(cc, Long.valueOf(tc.getLng()));
	}

}
