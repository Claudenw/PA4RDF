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
import org.xenei.jena.entities.testing.abst.SingleValuePrimitiveTestClass;

public class SingleValuePrimitiveEntityTests
{

	private SingleValuePrimitiveTestClass tc;
	private Model model;

	@Before
	public void setup()
	{
		model = ModelFactory.createDefaultModel();
		final Resource r = model
				.createResource("http://localhost/SingleValuePrimitiveEntityTests");
		final EntityManager manager = EntityManagerFactory.getEntityManager();
		tc = manager.read(r, SingleValuePrimitiveTestClass.class);
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
		try
		{
			tc.isBool();
			Assert.fail("Should have thrown NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// expected
		}
	}

	@Test
	public void testChar()
	{
		final char c = 'a';
		final Character cc = new Character(c);
		tc.setChar(c);
		Assert.assertEquals(cc, Character.valueOf(tc.getChar()));
		tc.setChar('x');
		Assert.assertTrue(!cc.equals(tc.getChar()));
		tc.setChar(cc);
		Assert.assertEquals(cc, Character.valueOf(tc.getChar()));
		tc.removeChar();
		try
		{
			tc.getChar();
			Assert.fail("Should have thrown NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// expected
		}
	}

	@Test
	public void testDbl()
	{
		final double c = 3.14;
		final Double cc = new Double(c);
		tc.setDbl(c);
		Assert.assertEquals(cc, Double.valueOf(tc.getDbl()));
		tc.setDbl(0.0);
		Assert.assertTrue(!cc.equals(tc.getDbl()));
		tc.setDbl(cc);
		Assert.assertEquals(cc, Double.valueOf(tc.getDbl()));
		tc.removeDbl();
		try
		{
			tc.getDbl();
			Assert.fail("Should have thrown NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// expected
		}
		;
	}

	@Test
	public void testFlt()
	{
		final float c = 3.14F;
		final Float cc = new Float(c);
		tc.setFlt(c);
		Assert.assertEquals(cc, Float.valueOf(tc.getFlt()));
		tc.setFlt(0.0F);
		Assert.assertTrue(!cc.equals(tc.getFlt()));
		tc.setFlt(cc);
		Assert.assertEquals(cc, Float.valueOf(tc.getFlt()));
		tc.removeFlt();
		try
		{
			tc.getFlt();
			Assert.fail("Should have thrown NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// expected
		}
		;
	}

	@Test
	public void testInt()
	{
		final int c = 3;
		final Integer cc = new Integer(c);
		tc.setInt(c);
		Assert.assertEquals(cc, Integer.valueOf(tc.getInt()));
		tc.setInt(0);
		Assert.assertTrue(!cc.equals(tc.getInt()));
		tc.setInt(cc);
		Assert.assertEquals(cc, Integer.valueOf(tc.getInt()));
		tc.removeInt();
		try
		{
			tc.getInt();
			Assert.fail("Should have thrown NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// expected
		}
		;
	}

	@Test
	public void testLng()
	{
		final long c = 3;
		final Long cc = new Long(c);
		tc.setLng(c);
		Assert.assertEquals(cc, Long.valueOf(tc.getLng()));
		tc.setLng(0L);
		Assert.assertTrue(!cc.equals(tc.getLng()));
		tc.setLng(cc);
		Assert.assertEquals(cc, Long.valueOf(tc.getLng()));
		tc.removeLng();
		try
		{
			tc.getLng();
			Assert.fail("Should have thrown NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// expected
		}
		;
	}

}
