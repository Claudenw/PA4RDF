package org.xenei.jena.entities.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ActionTypeTest
{
	
	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testGetterTypes()
	{
		ActionType type = ActionType.parse( "getX");
		assertTrue( "Should be GETTER", type == ActionType.GETTER);
		 type = ActionType.parse( "getIs");
		assertTrue( "Should be GETTER", type == ActionType.GETTER);
	}

	@Test
	public void testSetterTypes()
	{
		ActionType type = ActionType.parse( "setX");
		assertTrue( "setX should be SETTER", type == ActionType.SETTER);
		 type = ActionType.parse( "addX");
		assertTrue( "addX should be SETTER", type == ActionType.SETTER);

	}
	
	@Test
	public void testExistentialTypes()
	{
		ActionType type = ActionType.parse( "hasX");
		assertTrue( "hasX should be EXISTENTIAL", type == ActionType.EXISTENTIAL);
	}
	
	@Test
	public void testRemoverTypes()
	{
		ActionType type = ActionType.parse( "removeX");
		assertTrue( "hasX should be REMOVER", type == ActionType.REMOVER);
	}
}
