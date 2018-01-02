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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.RDFNode;
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
    boolean upcase = false;
    String name = "";
    String namespace = "";
    String literalType = "";
    Class<?> type = null;
    Class<?> contained = RDFNode.class;
    boolean emptyIsNull = true;
    boolean impl = false;
    List<Method> postExec = null;
    boolean isCollection = false;

    public EffectivePredicate() {
    }

    public EffectivePredicate(final EffectivePredicate ep) {
        this();
        merge( ep );
    }

    public EffectivePredicate(final Method method) {
        if (method != null) {

            if (method.getParameterTypes().length > 0) {

                for (final Annotation a : method.getParameterAnnotations()[0]) {
                    if (a instanceof URI) {
                        this.type = URI.class;
                    }
                }
            }

            final Subject s = method.getDeclaringClass().getAnnotation( Subject.class );
            if (s != null) {
                this.namespace = s.namespace();
            }
            isCollection = MethodParser.isCollection( method );
            final Predicate p = method.getAnnotation( Predicate.class );          
            if (p != null) {
                merge( p );
                if (StringUtils.isNotBlank( p.postExec() )) {
                    final String mName = p.postExec().trim();
                    try {
                        Method peMethod = null;
                        Class<?> argType = null;
                        final ActionType actionType = ActionType.parse( method.getName() );
                        switch (actionType) {
                        case GETTER:
                            argType = method.getReturnType();
                            break;

                        case SETTER:
                        case EXISTENTIAL:
                        case REMOVER:
                            if (method.getParameterTypes().length != 1) {
                                throw new RuntimeException(
                                        String.format( "%s does not have a single parameter", peMethod ) );
                            }
                            argType = method.getParameterTypes()[0];
                            break;
                        }
                        peMethod = method.getDeclaringClass().getMethod( mName, argType );
                        if (actionType == ActionType.GETTER && !argType.equals( peMethod.getReturnType() )) {
                            throw new RuntimeException(
                                    String.format( "%s does not return its parameter type", peMethod ) );
                        }
                        addPostExec( peMethod );
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
                    final ActionType actionType = ActionType.parse( method.getName() );
                    setName( actionType.extractName( method.getName() ) );
                } catch (final IllegalArgumentException e) {
                    // expected when not an action method.
                }
            }
        }
    }

    public EffectivePredicate(final Predicate p) {
        this();
        merge( p );
    }

    public void addPostExec(Collection<Method> peMethods) {
        for (final Method m : peMethods) {
            addPostExec( m );
        }
    }

    public void addPostExec(Method peMethod) {
        if (postExec == null) {
            postExec = new ArrayList<Method>();
        }
        if (!postExec.contains( peMethod )) {
            postExec.add( peMethod );
        }
    }

    public boolean emptyIsNull() {
        return emptyIsNull;
    }

    public boolean impl() {
        return impl;
    }

    public boolean isTypeNotSet() {
        return type == null;
    }

    public String literalType() {
        return literalType;
    }

    public EffectivePredicate merge(final EffectivePredicate predicate) {
        if (predicate != null) {
            upcase = predicate.upcase();
            setName( StringUtils.isBlank( predicate.name() ) ? name : predicate.name() );
            namespace = StringUtils.isBlank( predicate.namespace() ) ? namespace : predicate.namespace();
            literalType = StringUtils.isBlank( predicate.literalType() ) ? literalType : predicate.literalType();
            if (type == null)
            {
                type = predicate.type();
            }
            contained = contained.equals(  RDFNode.class ) ? predicate.contained() : contained;
            impl |= predicate.impl();
            isCollection |= predicate.isCollection();
        }
        return this;
    }
    
    public boolean isCollection() {
        return isCollection;
    }

    public EffectivePredicate merge(final Predicate predicate) {
        if (predicate != null) {
            upcase = predicate.upcase();
            setName( StringUtils.isBlank( predicate.name() ) ? name : predicate.name() );
            namespace = StringUtils.isBlank( predicate.namespace() ) ? namespace : predicate.namespace();
            literalType = StringUtils.isBlank( predicate.literalType() ) ? literalType : predicate.literalType();
            if (type == null)
            {
                type = predicate.type();
            } else {
                type = RDFNode.class.equals( predicate.type() ) ? type : predicate.type();
            }
            contained = predicate.contained();
            emptyIsNull = predicate.emptyIsNull();
            impl |= predicate.impl();
        }
        return this;
    }

    public String name() {
        return name;
    }

    public String namespace() {
        return namespace;
    }

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
     * Gets the type.  
     * @param dflt The default value if the type is not set.
     * @return the type.
     */
    public Class<?> type(Class<?> dflt) {
        return type == null ? dflt : type;
    }

    /**
     * Gets the type.  If not set defaults to RDFNode.class
     * @return the type.
     */
    public Class<?> type() {
        return type;
    }
    
    public Class<?> contained() {
        return contained;// == null ? RDFNode.class : type;
    }

    public boolean upcase() {
        return upcase;
    }
    
    @Override
    public String toString() {
        return String.format(  "EffectivePredicate[ ns:%s n:%s impl:%s, colection:%s type:%s cntd:%s uc:%s enull:%s lit:%s postExec:%s ]", 
                namespace, name, impl, isCollection, type, contained, upcase, emptyIsNull, literalType, postExec==null?0:postExec.size() );
    }

}