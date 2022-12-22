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

package org.xenei.jena.entities;

import org.apache.jena.rdf.model.RDFNode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.ClassUtils;

/**
 * An class that mimics the Predicate annotation but allows processing to modify
 * the values.
 *
 * @see org.xenei.jena.entities.annotations.Predicate
 */
public class EffectivePredicate {
    private boolean upcase = false;
    private String name = "";
    private String namespace = "";
    private String literalType = "";
    private Class<?> type = null;
    private boolean emptyIsNull = false;
    private boolean impl = false;
    private List<Method> postExec = null;

    /**
     * Constructs an empty predicate.
     */
    public EffectivePredicate() {
    }

    /**
     * Constructs an effective predicate by parsing the method and its
     * annotations.
     *
     * @param method
     *            the method to parse.
     */
    public EffectivePredicate(final Method method) {

        /* lower priority settings get done first */

        // type based on argumnet/return type
        final ActionType actionType = ActionType.parse( method.getName() );

        type = actionType.predicateClass( method );
        if (ClassUtils.isCollection( type )) {
            type = void.class;
        }

        // method parameter annotation overrides argument/return type.
        if (method.getParameterCount() > 0) {
            for (final Annotation a : method.getParameterAnnotations()[0]) {
                if (a instanceof URI) {
                    type = URI.class;
                }
            }
        }

        // Subject annotation on class sets default namespace.
        final Subject s = method.getDeclaringClass().getAnnotation( Subject.class );
        if (s != null) {
            namespace = s.namespace();
        }

        // if there is a predicate it sets the values
        final Predicate predicate = method.getAnnotation( Predicate.class );
        // set upcase before name
        if (predicate != null) {
            upcase = predicate.upcase();

            if (StringUtils.isNotBlank( predicate.name() )) {
                setName( predicate.name() );
            }
            if (StringUtils.isNotBlank( predicate.namespace() )) {
                namespace = predicate.namespace();
            }
            if (StringUtils.isNotBlank( predicate.literalType() )) {
                literalType = predicate.literalType();
            }
            emptyIsNull = predicate.emptyIsNull();

            impl = predicate.impl();

            if (void.class != predicate.type()) {
                type = predicate.type();
            }
            if (StringUtils.isNotBlank( predicate.postExec() )) {
                final String mName = predicate.postExec().trim();
                try {
                    final Class<?> argType = actionType.predicateClass( method );
                    if (argType == null) {
                        throw new IllegalArgumentException( String.format( "%s is not an Action method", method ) );
                    }
                    final Method peMethod = method.getDeclaringClass().getMethod( mName, argType );
                    if (argType.equals( peMethod.getReturnType() )) {
                        addPostExec( peMethod );
                    } else {
                        throw new RuntimeException(
                                String.format( "%s does not return its parameter type", peMethod ) );
                    }
                } catch (final NoSuchMethodException e) {
                    throw new RuntimeException( "Error parsing predicate annotation", e );
                } catch (final SecurityException e) {
                    throw new RuntimeException( "Error parsing predicate annotation", e );
                } catch (final IllegalArgumentException e) {
                    throw new RuntimeException( "Error parsing predicate annotation action type", e );
                }
            }
        }
        if (StringUtils.isBlank( name )) {
            setName( actionType.extractName( method.getName() ) );
        }
    }

    public EffectivePredicate copy() {
        final EffectivePredicate result = new EffectivePredicate();
        result.upcase = upcase;
        result.emptyIsNull = emptyIsNull;
        result.impl = impl;
        result.name = name;
        result.namespace = namespace;
        result.literalType = literalType;
        result.type = type;
        if (postExec != null) {
            result.addPostExec( postExec );
        }
        return result;
    }

    /**
     * Add postExec processing to the predicate.
     *
     * @param peMethods
     *            A collection PostExec methods to execute.
     */
    public void addPostExec(final Collection<Method> peMethods) {
        for (final Method m : peMethods) {
            addPostExec( m );
        }
    }

    /**
     * Add postExec processing to the predicate. A method many only be added
     * once. An attempt to add the method a second time will be ignored.
     *
     * @param peMethod
     *            the PostExec method to execute.
     */
    public void addPostExec(final Method peMethod) {
        if (postExec == null) {
            postExec = new ArrayList<>();
        }
        if (!postExec.contains( peMethod )) {
            postExec.add( peMethod );
        }
    }

    /**
     * Returns the postExec processing list. If the postExec has not been set
     * will return an empty list.
     *
     * @return an unmodifiable copy of the processingList.
     */
    public List<Method> postExec() {
        if (postExec == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList( postExec );
    }

    /**
     * If true empty strings are assumed to be null and are not inserted.
     *
     * @return true if empty strings should be considered as nulls.
     */
    public boolean emptyIsNull() {
        return emptyIsNull;
    }

    /**
     * Indicates that a method is an implementation of an abstract method to
     * allow the class to be concrete while not providing a concrete
     * implementation of the Predicate annotated methods.
     *
     * @return true if the implementation should be overridden.
     */
    public boolean impl() {
        return impl;
    }

    /**
     * Returns true if the type has not been set.
     *
     * @return true if the type has not been set.
     */
    public boolean isTypeNotSet() {
        return type == null;
    }

    /**
     * The name of the literal type or an empty string if not is use. If
     * specified it is used in a call to typeMapper.getSafeTypeByName() to get
     * the RDFDatatype used to parse and unparse literal values.
     *
     * @return The name of the literal type or an empty string.
     */
    public String literalType() {
        return literalType;
    }

    public void setLiteralType(final String literalType) {
        this.literalType = literalType;
    }

    /**
     * Merges an EffectivePredicate into this one.
     *
     * @param predicate
     *            the other effective predicate to merge.
     * @return this EffectivePredicate.
     */
    public EffectivePredicate merge(final EffectivePredicate predicate) {
        if (predicate == null) {
            return copy();
        }
        final EffectivePredicate result = new EffectivePredicate();
        result.upcase = upcase || predicate.upcase;
        result.impl = impl || predicate.impl;
        result.emptyIsNull = emptyIsNull;
        result.name = StringUtils.isNotBlank( name ) ? name : predicate.name;
        result.namespace = StringUtils.isNotBlank( namespace ) ? namespace : predicate.namespace;
        result.literalType = StringUtils.isNotBlank( literalType ) ? literalType : predicate.literalType;
        result.type = void.class.equals( type ) ? predicate.type : type;
        result.impl = impl;
        if (postExec != null) {
            result.addPostExec( postExec );
        }
        return result;
    }

    /**
     * The name of the predicate. This is the local name in RDF parlance. If not
     * specified it defaults to the name of the function with the action prefix
     * removed. @see{ @link org.xenei.jena.entities.impl.ActionType}.
     *
     * The namespace may be specified as part of the name. In this case the
     * namespace value need not be set.
     *
     * @return the local name of the RDF predicate.
     */
    public String name() {
        return name;
    }

    /**
     * Set the name of this predicate.
     *
     * @param name
     *            the name to set.
     */
    public void setName(final String name) {
        if (StringUtils.isNotBlank( name )) {
            final String s = name.substring( 0, 1 );
            if (upcase()) {
                this.name = name.replaceFirst( s, s.toUpperCase() );
            } else {
                this.name = name.replaceFirst( s, s.toLowerCase() );
            }
        }
    }

    /**
     * The namespace for the predicate. If not specified defaults to the
     * namespace for the subject that this predicate is part of. The namespace
     * may be specified with this field or as part of the name field.
     *
     * @return The namespace portion of the RDF predicate.
     */
    public String namespace() {
        return namespace;
    }

    void setNamespace(final String namespace) {
        this.namespace = namespace;
    }

    /**
     * The java object class that will be returned when the object is read from
     * the RDF model.
     *
     * @return The object class. If type is null {@code RDFNode.class} is
     *         returned.
     */
    public Class<?> type() {
        return type == null ? RDFNode.class : type;
    }

    /**
     * The java object class that will be returned when the object is read from
     * the RDF model.
     *
     * @return The object class.
     */
    public Class<?> rawType() {
        return type;
    }

    /**
     * Sets the java object class that will be returned when the object is read
     * from the RDF model.
     *
     * @param type
     *            the type to set.
     */
    public void setType(final Class<?> type) {
        this.type = type;
    }

    /**
     * determines if the local name should have the first character upper cased.
     * If false (the default) the first character will be lower cased. If true,
     * the first character will be upper cased.
     *
     * @return true if the first character of the name should be upper cased.
     */
    public boolean upcase() {
        return upcase;
    }

}