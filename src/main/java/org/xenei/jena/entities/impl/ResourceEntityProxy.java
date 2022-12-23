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
import org.apache.jena.shared.NotFoundException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.apache.commons.proxy.exception.InvokerException;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.SubjectInfo;

/**
 * Implements the invoker that the proxy uses.
 *
 * This method intercepts the annotated entity methods as well as the
 * ResourceWrapper.getResource() method.
 */
public class ResourceEntityProxy implements InvocationHandler {
    private final Object source;
    private final Resource resource;
    private final SubjectInfo subjectInfo;
    private final EntityManager entityManager;
    private final SubjectInfo sourceSubjectInfo;

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
    public ResourceEntityProxy(final EntityManager entityManager, final Object source, final SubjectInfo subjectInfo,
            final SubjectInfo sourceSubjectInfo) {
        this.source = source;
        resource = ResourceWrapper.getResource( source );
        this.entityManager = entityManager;
        this.subjectInfo = subjectInfo;
        this.sourceSubjectInfo = sourceSubjectInfo;
    }

    /**
     * Invokes an method on the proxy.
     */
    @Override
    public Object invoke(final Object self, final Method thisMethod, final Object[] args) throws Throwable {
        final PredicateInfo pi = sourceSubjectInfo == null ? null : sourceSubjectInfo.getPredicateInfo( thisMethod );
        if (pi != null) {
            if (pi.getPredicate().impl()) {
                return interceptNonAbstract( self, thisMethod, args, pi );
            }
        }
        return interceptAnnotated( self, thisMethod, args );
    }

    private Object interceptAnnotated(final Object self, final Method thisMethod, final Object[] args)
            throws Throwable {
        // handle the resource wrapper method call.
        // if (ResourceEntityProxy.GET_RESOURCE.equals(m))
        // Pair<Boolean,Object> result = execute( self, thisMethod, args);
        // if (result.getLeft() ) {
        // return result.getRight();
        // }
        if (thisMethod.getName().equals( "getResource" ) && (thisMethod.getParameterTypes().length == 0)
                && (thisMethod.getReturnType() == Resource.class)) {
            return resource;
        }

        SubjectInfo workingInfo = null;
        if (thisMethod.getDeclaringClass() != subjectInfo.getImplementedClass()) {
            // handle the case where T implements Resource and the method is
            // declared in the Resource interface.
            if (thisMethod.getDeclaringClass().isAssignableFrom( Resource.class )
                    && Resource.class.isAssignableFrom( subjectInfo.getImplementedClass() )) {
                return thisMethod.invoke( resource, args );
            }
            workingInfo = entityManager.getSubjectInfo( thisMethod.getDeclaringClass() );
        }
        workingInfo = workingInfo == null ? subjectInfo : workingInfo;

        final PredicateInfo pi = workingInfo.getPredicateInfo( thisMethod );

        if (pi == null) {
            try {
                return MethodLibrary.INSTANCE.exec( thisMethod, this, args );
            } catch (final NotFoundException e) {
                // expected
            }
            if (TypeChecker.canBeSetFrom( thisMethod.getDeclaringClass(), subjectInfo.getImplementedClass() )
                    && TypeChecker.canBeSetFrom( thisMethod.getDeclaringClass(), Resource.class )) {
                final Class<?>[] argTypes = new Class<?>[args == null ? 0 : args.length];
                for (int i = 0; i < argTypes.length; i++) {
                    argTypes[i] = args[i].getClass();
                }
                try {
                    final Method resourceMethod = Resource.class.getMethod( thisMethod.getName(), argTypes );
                    return thisMethod.invoke( resource, args );
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

    // handle the cases where the method is not abstract
    private Object interceptNonAbstract(final Object self, final Method thisMethod, final Object[] args,
            final PredicateInfo pi) throws Throwable {
        // handle the special case methods
        if (thisMethod.getName().equals( "toString" ) && !thisMethod.isVarArgs()
                && (thisMethod.getParameterTypes().length == 0)) {
            return source.toString();
        }
        if (thisMethod.getName().equals( "hashCode" )) {
            return source.hashCode();
        }
        if (thisMethod.getName().equals( "equals" )) {
            return source.equals( args[0] );
        }

        Object result = interceptAnnotated( self, thisMethod, args );
        for (final Method m : pi.getPostExec()) {
            result = m.invoke( source, result );
        }
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ResourceWrapper) {
            final ResourceWrapper other = (ResourceWrapper) o;
            return other.getResource().equals( resource );
        }
        return false;
    }

    @Override
    public String toString() {
        return resource.toString();
    }

    @Override
    public int hashCode() {
        return resource.hashCode();
    }
}