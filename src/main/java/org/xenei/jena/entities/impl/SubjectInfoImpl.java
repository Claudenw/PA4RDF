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

import org.apache.jena.rdf.model.Property;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.annotations.Subject;

public class SubjectInfoImpl implements SubjectInfo {
    private final Class<?> implementedClass;
    private final Map<String, Map<Class<?>, PredicateInfo>> predicateInfo = new HashMap<>();

    public SubjectInfoImpl(final Class<?> implementedClass) {
        this.implementedClass = implementedClass;
    }

    /**
     * Add a predicate info to this subject.
     *
     * @param pi
     *            The predicateInfo to add.
     */
    public void add(final Method method, final PredicateInfo pi) {
        if (method == null) {
            throw new IllegalArgumentException( "Method may not be null" );
        }
        if (pi == null) {
            throw new IllegalArgumentException( "PredicateInfo may not be null" );
        }
        Map<Class<?>, PredicateInfo> map = predicateInfo.get( pi.getMethodName() );
        if (map == null) {
            map = new HashMap<>();
            predicateInfo.put( pi.getMethodName(), map );
        }

        map.put( pi.getActionType().predicateClass( method ), pi );
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xenei.jena.entities.impl.SubjectInfo#getImplementedClass()
     */
    @Override
    public Class<?> getImplementedClass() {
        return implementedClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xenei.jena.entities.impl.SubjectInfo#getPredicateInfo(java.lang.
     * reflect .Method)
     */
    @Override
    public PredicateInfo getPredicateInfo(final Method method) {
        if (method.isVarArgs() || (method.getParameterCount() > 1)) {
            return null;
        }
        try {
            final ActionType action = ActionType.parse( method.getName() );
            return getPredicateInfo( method.getName(), action.predicateClass( method ) );
        } catch (final IllegalArgumentException ignore) {
            return null;
        }
    }

    /**
     * Get the first predicateinfo for the function name.
     *
     * @param function
     *            the function to find.
     * @return A predicate info for the name.
     * @throws IllegalArgumentException
     *             if the function is not found.
     */
    private PredicateInfo getPredicateInfo(final String function) {
        final Map<Class<?>, PredicateInfo> map = predicateInfo.get( function );
        if (map == null) {
            throw new IllegalArgumentException( String.format( "Function %s not found", function ) );
        }
        return map.values().iterator().next();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xenei.jena.entities.impl.SubjectInfo#getPredicateInfo(java.lang.
     * String , java.lang.Class)
     */
    @Override
    public PredicateInfo getPredicateInfo(final String function, final Class<?> clazz) {
        final Map<Class<?>, PredicateInfo> map = predicateInfo.get( function );
        return (map != null) ? map.get( clazz == null ? void.class : clazz ) : null;
    }

    /**
     * Get the RDF Property for the method
     *
     * @param method
     *            The method to get the property for.
     */
    @Override
    public Property getPredicateProperty(final Method method) {
        final PredicateInfo pi = getPredicateInfo( method );
        return pi == null ? null : pi.getProperty();
    }

    /**
     * Get the RDF property for a method name.
     *
     * @param methodName
     *            The method name to locate
     * @return the Property or null if the method was not a predicate property.
     */
    @Override
    public Property getPredicateProperty(final String methodName) {
        try {
            final PredicateInfo pi = getPredicateInfo( methodName );
            return pi == null ? null : pi.getProperty();
        } catch (final IllegalArgumentException ignore) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.xenei.jena.entities.impl.SubjectInfo#getUri(java.lang.reflect.Method)
     */
    @Override
    public String getPredicateUriStr(final Method method) {
        final PredicateInfo pi = getPredicateInfo( method );
        return pi == null ? null : pi.getUriString();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xenei.jena.entities.impl.SubjectInfo#getUri(java.lang.String)
     */
    @Override
    public String getPredicateUriStr(final String function) {
        return getPredicateInfo( function ).getUriString();
    }

    /**
     * Get the subject annotation for this class.
     *
     * @return The subject annotation.
     */
    @Override
    public Subject getSubject() {
        return implementedClass.getAnnotation( Subject.class );
    }
}
