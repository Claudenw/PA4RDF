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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.annotations.Subject;

public class SubjectInfoImpl implements SubjectInfo {
    private boolean validated;
    private final Class<?> implementedClass;
    private final Map<String, Map<ObjectHandler, PredicateInfo>> predicateInfo = new HashMap<>();

    public SubjectInfoImpl(final Class<?> implementedClass) {
        this.implementedClass = implementedClass;
        validated = false;
    }

    /**
     * Add a predicate info to this subject.
     *
     * @param pi
     *            The predicateInfo to add.
     */
    public void add(final PredicateInfoImpl pi) {
        if (pi == null) {
            throw new IllegalArgumentException( "PredicateInfo may not be null" );
        }
        Map<ObjectHandler, PredicateInfo> map = predicateInfo.get( pi.getMethodName() );
        if (map == null) {
            map = new HashMap<>();
            predicateInfo.put( pi.getMethodName(), map );
        }

        map.put( pi.getObjectHandler(), pi );
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
    public PredicateInfo getPredicateInfo(final Method m) {
        if (m.isVarArgs() || (m.getParameterTypes().length > 1)) {
            return null;
        }
        if (m.getParameterTypes().length == 0) {
            // must be a getter or single value remove
            return getPredicateInfo( m.getName(), m.getReturnType() );
        }
        return getPredicateInfo( m.getName(), m.getParameterTypes()[0] );
    }

    private PredicateInfo getPredicateInfo(final String function) {
        final Map<ObjectHandler, PredicateInfo> map = predicateInfo.get( function );
        if (map == null) {
            throw new IllegalArgumentException( String.format( "Function %s not found", function ) );
        }
        if (map.values().isEmpty()) {
            {
                throw new IllegalArgumentException( String.format( "Function %s not found", function ) );
            }
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
        final Map<ObjectHandler, PredicateInfo> map = predicateInfo.get( function );
        if (map != null) {
            for (final PredicateInfo pi : map.values()) {
                final Class<?> valueClass = pi.getValueClass();
                switch (pi.getActionType()) {
                case SETTER:
                    if (TypeChecker.canBeSetFrom( valueClass, clazz )) {
                        return pi;
                    }
                    break;

                case GETTER:
                    if (TypeChecker.canBeSetFrom( clazz, valueClass )) {
                        return pi;
                    }
                    break;

                case REMOVER:
                case EXISTENTIAL:
                    if (valueClass != null) {
                        // it needs an argument
                        if (TypeChecker.canBeSetFrom( valueClass, clazz )) {
                            return pi;
                        }
                    } else {
                        // it does not want an argument
                        if ((clazz == null) || clazz.equals( void.class )) {
                            return pi;
                        }
                    }
                    break;
                }
            }
        }
        return null;
    }

    /**
     * Get the RDF Property for the method
     *
     * @param m
     *            The method to get the property for.
     */
    @Override
    public Property getPredicateProperty(final Method m) {
        return getPredicateInfo( m ).getProperty();
    }

    /**
     * Get the RDF property for a method name.
     *
     * @param methodName
     *            The method name to locate
     */
    @Override
    public Property getPredicateProperty(final String methodName) {
        return getPredicateInfo( methodName ).getProperty();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.xenei.jena.entities.impl.SubjectInfo#getUri(java.lang.reflect.Method)
     */
    @Override
    public String getPredicateUriStr(final Method m) {
        return getPredicateInfo( m ).getUriString();
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

    /**
     * Remove a predicate info from this subject.
     *
     * @param m
     *            the method to remove
     */
    public void removePredicateInfo(final Method m) {
        if (m.isVarArgs() || (m.getParameterTypes().length > 1)) {
            return;
        }
        if (m.getParameterTypes().length == 0) {
            // must be a getter
            removePredicateInfo( m.getName(), m.getReturnType() );
        } else {
            removePredicateInfo( m.getName(), m.getParameterTypes()[0] );
        }
    }

    /**
     * Remove a predicate info from this subject.
     *
     * @param function
     *            The function to remove
     * @param clazz
     *            The class that is expected for the parameter (setter) or for
     *            return (getter).
     */
    public void removePredicateInfo(final String function, final Class<?> clazz) {
        final Map<ObjectHandler, PredicateInfo> map = predicateInfo.get( function );
        if (map != null) {
            for (final Map.Entry<ObjectHandler, PredicateInfo> entry : map.entrySet()) {
                if (entry.getValue().getValueClass().equals( clazz )) {
                    map.remove( entry.getKey() );
                }
            }
            if (map.isEmpty()) {
                predicateInfo.remove( function );
            }
        }
    }

    @Override
    public void validate(final Collection<Class<?>> iface) {
        if (validated) {
            return;
        }
        // final Collection<Class<?>> clazz = new ArrayList<Class<?>>(iface);
        // if (!implementedClass.isInterface())
        // {
        // clazz.add(implementedClass);
        // }
        // // clazz.remove(Resource.class);
        // verifyNoNullMethods(clazz);
        validated = true;
    }

}
