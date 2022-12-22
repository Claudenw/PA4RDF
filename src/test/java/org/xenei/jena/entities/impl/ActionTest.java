package org.xenei.jena.entities.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.apache.jena.util.iterator.ExtendedIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public class ActionTest {

    List<Method> seen = new ArrayList<>();
    Set<Method> processed = new HashSet<>();

    @Test
    public void getAssociatedActionsTest() throws Exception {
        final Method method = SimpleInterface.class.getMethod( "getX" );
        final Action action = new Action( method );
        getActions( "", action );
        Assertions.assertEquals( 2, processed.size() );
        Assertions.assertEquals( 4, seen.size() );
    }

    private void getActions(final String prefix, final Action action) {
        processed.add( action.method );
        System.out.println( String.format( "starting %s", action.method ) );
        final java.util.function.Predicate<Method> p = m -> seen.contains( m );
        final ExtendedIterator<Action> iter = action.getAssociatedActions( p );
        iter.forEach( a -> {
            System.out.println( String.format( "%s %s", prefix, a.method ) );
            seen.add( a.method );
            switch (a.actionType) {
            case GETTER:
            case SETTER:
                getActions( prefix + ">", a );
            default:
                break;
            }
        } );
    }
}
