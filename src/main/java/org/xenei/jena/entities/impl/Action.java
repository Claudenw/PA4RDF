package org.xenei.jena.entities.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.WrappedIterator;
import org.xenei.jena.entities.annotations.Predicate;

public class Action {
    public final ActionType actionType;
    public final boolean isMultiple;
    public final Method method;

    protected static final Function<Method, Action> actionMap = new Function<>() {

        @Override
        public Action apply(final Method method) {
            try {
                return new Action( method );
            } catch (final IllegalArgumentException e) {
                return null;
            }
        }
    };

    private boolean deriveMultiple(final ActionType actionType, final Method method) {
        switch (actionType) {
        case GETTER:
            return ClassUtils.isCollection( method.getReturnType() );
            
        case SETTER:
            return method.getName().startsWith( "add" );

        case EXISTENTIAL:
        case REMOVER:
            return method.getParameterCount() > 0;
        }
        throw new IllegalStateException( String.format( "%s is not an ActionType", actionType ) );
    }

    public Action(final Method method) {
        actionType = ActionType.parse( method.getName() );
        isMultiple = deriveMultiple( actionType, method );
        this.method = method;
    }

    public String name() {
        return actionType.extractName( method.getName() );
    }

    public Class<?> voidOrClass(final Class<?> dflt) {
        return dflt == null ? void.class : dflt;
    }

    public boolean hasArgumentAnnotation(final Class<?> ann) {
        return getArgumentAnnotation(ann) != null;
    }
    
    public <T> T getArgumentAnnotation(final Class<T> ann) {
        if (method.getParameterCount() > 0) {
            final Annotation[] annotations = method.getParameterAnnotations()[0];
            for (final Annotation a : annotations) {
                if (a.annotationType().isAssignableFrom( ann )) {
                    return ann.cast( a );
                }
            }
        }
        return null;
    }

    public boolean hasMethodTypeAnnotation(final Class<?> ann) {
        final Predicate p = method.getAnnotation( Predicate.class );
        return p == null ? false : p.type().isAssignableFrom( ann );
    }

    
    public Class<?> getArgument() {
        return voidOrClass( method.getParameterCount() > 0 ? method.getParameterTypes()[0] : null );
    }

    public Class<?> getReturn() {
        return voidOrClass( method.getReturnType() );
    }

    public Class<?> context() {
        return method.getDeclaringClass();
    }

    public ExtendedIterator<Action> getAssociatedActions(final java.util.function.Predicate<Method> dropFilter) {
        final Set<String> newNames = ActionType.allNames( name() ).toSet();
        return WrappedIterator.create( Arrays.asList( context().getMethods() ).iterator() )
                .filterKeep( m -> newNames.contains( m.getName() ) ).filterDrop( m -> method.equals( m ) )
                .filterDrop( dropFilter ).mapWith( Action.actionMap ).filterDrop( a -> {
                    return a == null;
                } );
    }
}