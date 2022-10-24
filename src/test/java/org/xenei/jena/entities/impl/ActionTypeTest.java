package org.xenei.jena.entities.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ActionTypeTest
{

	@BeforeEach
	public void setUp() throws Exception
	{
	}

	@AfterEach
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testExistentialTypes()
	{
		final ActionType type = ActionType.parse("hasX");
		Assertions.assertTrue(
				type == ActionType.EXISTENTIAL,
				"hasX should be EXISTENTIAL");
	}

	@Test
	public void testGetterTypes()
	{
		ActionType type = ActionType.parse("getX");
		Assertions.assertTrue(type == ActionType.GETTER, "Should be GETTER");
		type = ActionType.parse("getIs");
		Assertions.assertTrue(type == ActionType.GETTER,"Should be GETTER");
	}

	@Test
	public void testRemoverTypes()
	{
		final ActionType type = ActionType.parse("removeX");
		Assertions.assertTrue(type == ActionType.REMOVER, "hasX should be REMOVER");
	}

	@Test
	public void testSetterTypes()
	{
		ActionType type = ActionType.parse("setX");
		Assertions.assertTrue(type == ActionType.SETTER, "setX should be SETTER");
		type = ActionType.parse("addX");
		Assertions.assertTrue(type == ActionType.SETTER, "setX should be SETTER");
	}
}
