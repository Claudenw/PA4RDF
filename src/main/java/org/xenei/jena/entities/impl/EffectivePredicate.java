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
    private boolean emptyIsNull = true;
    private boolean impl = false;
    private List<Method> postExec = null;

    /**
     * Constructs an empty predicate.
     */
    public EffectivePredicate() {
    }

    /**
     * Constructs an effective predicate as a copy of the argument.
     *
     * @param ep
     *            the predicate to copy.
     */
    public EffectivePredicate(final EffectivePredicate ep) {
        this();
        merge( ep );
    }

    /**
     * Constructs an effective predicate by parsing the method and its
     * annotations.
     *
     * @param m
     *            the method to parse.
     */
    public EffectivePredicate(final Method m) {
        if (m != null) {
            if (m.getParameterTypes().length > 0) {
                for (final Annotation a : m.getParameterAnnotations()[0]) {
                    if (a instanceof URI) {
                        type = URI.class;
                    }
                }
            }
            final ActionType actionType = ActionType.parse( m.getName() );
            final Subject s = m.getDeclaringClass().getAnnotation( Subject.class );
            if (s != null) {
                namespace = s.namespace();
            }
            final Predicate p = m.getAnnotation( Predicate.class );
            if (p != null) {
                merge( p );
                if (StringUtils.isNotBlank( p.postExec() )) {
                    final String mName = p.postExec().trim();
                    try {
                        Class<?> argType = actionType.predicateClass( m );
                        if (argType == null) {
                            throw new IllegalArgumentException( String.format( "%s is not an Action method", m ) );
                        }
                        Method peMethod = m.getDeclaringClass().getMethod( mName, argType );
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
                try {
                    setName( actionType.extractName( m.getName() ) );
                } catch (final IllegalArgumentException e) {
                    // expected when not an action method.
                }
            }
        }
    }

    /**
     * Constructs an effective predicate from a Predicate annotation.
     *
     * @param p
     *            the predicate to parse.
     */
    public EffectivePredicate(final Predicate p) {
        this();
        merge( p );
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
        if (predicate != null) {
            upcase = predicate.upcase();
            setName( StringUtils.isBlank( predicate.name() ) ? name : predicate.name() );
            namespace = StringUtils.isBlank( predicate.namespace() ) ? namespace : predicate.namespace();
            literalType = StringUtils.isBlank( predicate.literalType() ) ? literalType : predicate.literalType();
            type = RDFNode.class.equals( predicate.type() ) ? type : predicate.type();
            impl |= predicate.impl();
            if (predicate.postExec != null) {
                for (final Method m : predicate.postExec) {
                    addPostExec( m );
                }
            }

        }
        return this;
    }

    /**
     * Merges a Predicate into this one.
     *
     * @param predicate
     *            the predicate to merge.
     * @return this EffectivePredicate.
     */
    public EffectivePredicate merge(final Predicate predicate) {
        if (predicate != null) {
            upcase = predicate.upcase();
            setName( StringUtils.isBlank( predicate.name() ) ? name : predicate.name() );
            namespace = StringUtils.isBlank( predicate.namespace() ) ? namespace : predicate.namespace();
            literalType = StringUtils.isBlank( predicate.literalType() ) ? literalType : predicate.literalType();
            type = RDFNode.class.equals( predicate.type() ) ? type : predicate.type();
            // type = type!=null ? type : predicate.type();
            emptyIsNull = predicate.emptyIsNull();
            impl |= predicate.impl();
        }
        return this;
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
     * The namespace for the predicate. If not specified defaults to the
     * namespace for the subject that this predicate is part of. The namespace
     * may be specified with this field or as part of the name field.
     *
     * @return The namespace portion of the RDF predicate.
     */
    public String namespace() {
        return namespace;
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