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
package org.xenei.jena.entities.impl;

import org.apache.jena.rdf.model.Resource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.proxy.exception.InvokerException;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.annotations.Predicate;

/**
 * Implements the invoker that the proxy uses.
 *
 * This method intercepts the annotated entity methods as well as the
 * ResourceWrapper.getResource() method.
 */
public class ResourceEntityProxy implements InvocationHandler
{
    private final Resource resource;
    private final SubjectInfo subjectInfo;
    private final EntityManager entityManager;

    /**
     * The constructor
     *
     * @param entityManager
     *            The entity manager to use.
     * @param resource
     *            The resource that is being wrapped
     * @param subjectInfo
     *            The subjectInfo for the resource.
     */
    public ResourceEntityProxy(final EntityManager entityManager, final Resource resource,
            final SubjectInfo subjectInfo) {
        this.resource = resource;
        this.entityManager = entityManager;
        this.subjectInfo = subjectInfo;
    }

    /**
     * Invokes an method on the proxy.
     */
    @Override
    public Object invoke(final Object self, final Method thisMethod, final Object[] args) throws Throwable {
        // handle the cases where the method is not abstract
        if (!Modifier.isAbstract( thisMethod.getModifiers() )) {
            return interceptNonAbstract( self, thisMethod, args);
        }
        return interceptAnnotated( self, thisMethod, args);
    }

    private Object interceptAnnotated(final Object self, final Method thisMethod, final Object[] args)
            throws Throwable {
        // handle the resource wrapper method call.
        // if (ResourceEntityProxy.GET_RESOURCE.equals(m))
        if (thisMethod.getName().equals( "getResource" ) && (thisMethod.getParameterTypes().length == 0)
                && (thisMethod.getReturnType() == Resource.class)) {
            return resource;
        }

        SubjectInfo workingInfo = subjectInfo;
        if (thisMethod.getDeclaringClass() != subjectInfo.getImplementedClass()) {
            // handle the case where T implements Resource and the method is
            // declared in the Resource interface.
            if (thisMethod.getDeclaringClass().isAssignableFrom( Resource.class )
                    && Resource.class.isAssignableFrom( subjectInfo.getImplementedClass() )) {
                return thisMethod.invoke( resource, args );
            }
            workingInfo = entityManager.getSubjectInfo( thisMethod.getDeclaringClass() );
        }
        final PredicateInfo pi = workingInfo.getPredicateInfo( thisMethod );

        if (pi == null) {
            if (TypeChecker.canBeSetFrom( workingInfo.getImplementedClass(), subjectInfo.getImplementedClass() )
                    && TypeChecker.canBeSetFrom( workingInfo.getImplementedClass(), Resource.class )) {
                final Class<?>[] argTypes = new Class<?>[args.length];
                for (int i = 0; i < args.length; i++) {
                    argTypes[i] = args[i].getClass();
                }
                try {
                    final Method resourceMethod = Resource.class.getMethod( thisMethod.getName(), argTypes );
                    return resourceMethod.invoke( resource, args );
                } catch (final Exception e) {
                    // do nothing throw exception outside of if.
                }
            }
            throw new InvokerException( String.format( "Null method (%s) called", thisMethod.getName() ) );
        }

        if (pi instanceof PredicateInfoImpl) {
            Object o = ((PredicateInfoImpl) pi).exec( thisMethod, resource, args );
            for (final Method peMethod : subjectInfo.getPredicateInfo( thisMethod ).getPostExec()) {
                o = peMethod.invoke( self, o );
            }
            return o;
        }

        throw new RuntimeException(
                String.format( "Internal predicateinfo class (%s) not (%s)", pi.getClass(), PredicateInfoImpl.class ) );
    }

    // non abstract annotated methods
    // override the implementation unless annotatation includes the
    // update=true value -- not implemented
    private Object interceptAnnotatedNonAbstract(final Object self, final Method thisMethod, final Object[] args,
            final Predicate p) throws Throwable {
        return interceptAnnotated( self, thisMethod, args);
    }

    // handle the cases where the method is not abstract
    private Object interceptNonAbstract(final Object self, final Method thisMethod, final Object[] args)
            throws Throwable {
        // handle the special case methods
        if (thisMethod.getName().equals( "toString" ) && !thisMethod.isVarArgs() && (thisMethod.getParameterTypes().length == 0)) {
            return String.format( "%s[%s]", subjectInfo.getClass(), resource );
        }
        if (thisMethod.getName().equals( "hashCode" )) {
            return resource.hashCode();
        }
        if (thisMethod.getName().equals( "equals" )) {
            if (args[0] instanceof ResourceWrapper) {
                return resource.equals( ((ResourceWrapper) args[0]).getResource() );
            }
            if (args[0] instanceof Resource) {
                return resource.equals( args[0] );
            }
            return false;
        }

        final Predicate p = thisMethod.getAnnotation( Predicate.class );
        if (p != null) {
            return interceptAnnotatedNonAbstract( self, thisMethod, args, p );
        }
        return thisMethod.invoke( self, args );
    }
}