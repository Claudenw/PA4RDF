package org.xenei.jena.entities.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActionTypeTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testExistentialTypes() {
        final ActionType type = ActionType.parse( "hasX" );
        Assert.assertTrue( "hasX should be EXISTENTIAL", type == ActionType.EXISTENTIAL );
    }

    @Test
    public void testGetterTypes() {
        ActionType type = ActionType.parse( "getX" );
        Assert.assertTrue( "Should be GETTER", type == ActionType.GETTER );
        type = ActionType.parse( "getIs" );
        Assert.assertTrue( "Should be GETTER", type == ActionType.GETTER );
    }

    @Test
    public void testRemoverTypes() {
        final ActionType type = ActionType.parse( "removeX" );
        Assert.assertTrue( "hasX should be REMOVER", type == ActionType.REMOVER );
    }

    @Test
    public void testSetterTypes() {
        ActionType type = ActionType.parse( "setX" );
        Assert.assertTrue( "setX should be SETTER", type == ActionType.SETTER );
        type = ActionType.parse( "addX" );
        Assert.assertTrue( "addX should be SETTER", type == ActionType.SETTER );

    }
}
