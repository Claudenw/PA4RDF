package org.xenei.jena.entities.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.shared.NotFoundException;
import org.xenei.jena.entities.ResourceWrapper;

public class MethodLibrary {

    @FunctionalInterface
    public interface MethodCallHandler {
        Object invoke(ResourceEntityProxy proxy, Object[] args) throws Throwable;
    }

    public static final MethodLibrary INSTANCE = new MethodLibrary();

    private final Map<Method, MethodCallHandler> methods = new HashMap<>();

    private MethodLibrary() {
    };

    public void set(final Method method, final MethodCallHandler handler) {
        methods.put( method, handler );
    }

    public boolean has(final Method method) {
        return methods.containsKey( method );
    }

    public Object exec(final Method method, final ResourceEntityProxy proxy, final Object[] args) throws Throwable {
        final MethodCallHandler handler = methods.get( method );
        if (handler != null) {
            return handler.invoke( proxy, args );
        }
        throw new NotFoundException( method.toString() );
    }

    static {
        try {
            MethodLibrary.INSTANCE.set( Object.class.getDeclaredMethod( "toString" ),
                    (proxy, args) -> proxy.toString() );

            MethodLibrary.INSTANCE.set( Object.class.getDeclaredMethod( "equals", Object.class ),
                    (proxy, args) -> proxy.equals( args[0] ) );

            MethodLibrary.INSTANCE.set( Object.class.getDeclaredMethod( "hashCode" ),
                    (proxy, args) -> proxy.hashCode() );

            MethodLibrary.INSTANCE.set( ResourceWrapper.class.getDeclaredMethod( "getResource" ),
                    (proxy, args) -> ((ResourceWrapper) proxy).getResource() );

            MethodLibrary.INSTANCE.set( Object.class.getDeclaredMethod( "clone" ), (proxy, args) -> {
                throw new CloneNotSupportedException();
            } );

        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException( e );
        }
    }

}
