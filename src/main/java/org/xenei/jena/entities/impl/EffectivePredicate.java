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
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.Literal;
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
    private boolean upcase = false;
    private String name = "";
    private String namespace = "";
    private RDFDatatype literalType = null;
    private Class<?> type = null;
    private Class<?> internalType = null;
    private Class<?> collectionType = null;
    private boolean emptyIsNull = false;
    private boolean impl = false;
    private List<Method> postExec = null;
    private final ActionType actionType;

    public EffectivePredicate(final EffectivePredicate ep) {        
        actionType = ep.actionType;
        collectionType = ep.collectionType;
        emptyIsNull = ep.emptyIsNull;
        impl = ep.impl;
        internalType = ep.internalType;
        literalType = ep.literalType;
        name = ep.name;
        namespace = ep.namespace;
        if (ep.postExec != null) {
            postExec = new ArrayList<Method>( ep.postExec );
        }
        type = ep.type;
        upcase = ep.upcase;
    }

    public EffectivePredicate(ActionType actionType, Class<?> collectionType, boolean emptyIsNull, 
            boolean impl, Class<?> internalType, RDFDatatype literalType, String name, 
            String namespace, List<Method> postExec, Class<?> type, boolean upcase) { 
        this.actionType = actionType;
        this.collectionType = collectionType;
        this.emptyIsNull = emptyIsNull;
        this.impl = impl;
        this.internalType = internalType;
        this.literalType = literalType;
        this.name = name;
        this.namespace = namespace;
        this.postExec = postExec;
        this.type = type;
        this.upcase = upcase;
        
    }
    public EffectivePredicate(final Method method) {
        
        if (method != null) {
            // set the type and collection type.
            this.actionType = ActionType.parse( method.getName() );
            switch (actionType) {
            case EXISTENTIAL:
                boolean b = method.getReturnType().equals( Boolean.class );
                if (!b) {
                    try {
                        b = method.getReturnType().equals( Boolean.class.getField( "TYPE" ).get( null ) );
                    } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
                            | SecurityException e) {
                        throw new IllegalStateException( e );
                    }
                }
                if (!b) {
                    throw new RuntimeException( String.format( "%s does not return boolean or Boolean", method ) );
                }
                // fall through
            case REMOVER:
            case SETTER:
                if (method.getParameterTypes().length == 1) {
                    for (final Annotation a : method.getParameterAnnotations()[0]) {
                        if (a instanceof URI) {
                            this.type = URI.class;
                            this.internalType = RDFNode.class;
                        }
                    }
                }
                if (this.type == null) {
                    if (method.getParameterTypes().length == 1) {
                        if (!MethodParser.isCollection( method )) {
                            this.type = method.getParameterTypes()[0];
                        } else {
                            this.collectionType = method.getParameterTypes()[0];
                        }
                    } else {
                        this.type = Predicate.UNSET.class;;
                    }

                }
                break;
            case GETTER:
                if (!MethodParser.isCollection( method )) {
                    this.type = method.getReturnType();
                } else {
                    this.collectionType = method.getReturnType();
                }
                break;
            }
           
            final Subject s = method.getDeclaringClass().getAnnotation( Subject.class );           
            if (s != null) {
                this.namespace = s.namespace();
            }
            final Predicate predicate = method.getAnnotation( Predicate.class );
            if (predicate != null) {
                merge( predicate );
            }

            setInternalType();

            if (predicate != null) {
                if (StringUtils.isNotBlank( predicate.postExec() )) {
                    final String mName = predicate.postExec().trim();
                    try {
                        final Class<?> argType = isCollection() ? collectionType : type;
                        final Method peMethod = method.getDeclaringClass().getMethod( mName, argType );
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
                    setName( actionType.extractName( method.getName() ) );
                } catch (final IllegalArgumentException e) {
                    // expected when not an action method.
                }
            }
        }
        else {
            actionType = null;
        }
    }

    public void checkValid() {
        /* type may not be null and may only be unset in the REMOVER action type */
        if ((this.type == null) ||
                ( (! actionType.allowsNull()) && this.type == Predicate.UNSET.class)  )   
        {
            throw new RuntimeException( String.format( "%s %s does not define the parameter type", actionType, name ) );
        }
        if (this.internalType == null) {
            throw new RuntimeException( String.format( "%s %s does not define the internal type", actionType, name ) );
        }
        if (this.internalType.equals(  Literal.class ) && literalType == null)
        {
            throw new RuntimeException( String.format( "%s %s defines the internal type as Literal but no literal type", actionType, name ) );
        }        
    }

    private void setInternalType() {
        if (this.internalType != null || type == null) {
            return;
        }
        if (RDFNode.class.isAssignableFrom( type )) {
            this.internalType = type;
        } else {
            final TypeMapper typeMapper = TypeMapper.getInstance();
            this.literalType = typeMapper.getTypeByClass( type );
            if (this.literalType != null) {
                this.internalType = Literal.class;
            } else {
                this.internalType = RDFNode.class;
            }
        }
    }

    public ActionType actionType() {
        return actionType;
    }

    public List<Method> postExec() {
        return notNull( postExec, Collections.emptyList() );
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

    public RDFDatatype literalType() {
        return literalType;
    }

    private <T> T notNull(T opt, T orig) {
        return (opt == null || Predicate.UNSET.class.equals( opt )) ? orig : opt;
    }

    public EffectivePredicate merge(final EffectivePredicate predicate) {
        if (predicate != null) {
            upcase = predicate.upcase();
            setName( StringUtils.isBlank( predicate.name() ) ? name : predicate.name() );
            namespace = StringUtils.isBlank( predicate.namespace() ) ? namespace : predicate.namespace();
            
            if (Predicate.UNSET.class.equals( this.type ))
            {
                if (!actionType.allowsNull())
                {
                if (URI.class.equals(  predicate.type ))
                {                    
                    this.type = RDFNode.class;                     
                }
                else {
                    this.type = predicate.type;
                    this.internalType = predicate.internalType;
                    this.literalType = predicate.literalType;               
                }
                } else {
                    if (RDFNode.class.equals( internalType ))
                    {
                        if (predicate.internalType != null && !Predicate.UNSET.class.equals( predicate.internalType))
                        {
                            internalType = predicate.internalType;
                            literalType = predicate.literalType;
                        }
                    }
                }
            } else if (URI.class.equals(  predicate.type ) || URI.class.equals(  this.type )) {
                // URI type is custom by annotation and not changeable.
            } else {
                if ( !actionType.allowsNull() )
                {
                    type = notNull( type, predicate.type() );
                    internalType = notNull( internalType, predicate.internalType );
                    literalType = notNull( literalType, predicate.literalType );
                    setInternalType();
                }

            }

            collectionType = notNull( predicate.collectionType, collectionType );
            impl |= predicate.impl;
        }
        return this;
    }

    public boolean isCollection() {
        return collectionType != null;
    }

    public EffectivePredicate merge(final Predicate predicate) {
        if (predicate != null) {
            upcase = predicate.upcase();
            setName( StringUtils.isBlank( predicate.name() ) ? name : predicate.name() );
            namespace = StringUtils.isBlank( predicate.namespace() ) ? namespace : predicate.namespace();
            if (StringUtils.isNotBlank( predicate.literalType() )) {
                final TypeMapper typeMapper = TypeMapper.getInstance();
                literalType = typeMapper.getSafeTypeByName( predicate.literalType() );
            }
            type = notNull( predicate.type(), type );
            internalType = notNull( predicate.internalType(), internalType );
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
     * Get the User type. This is the type of object that should be returned
     * (getter) or accepted (other cases). If the actual type is a collection
     * then this is the type containted by the collection.
     * 
     * @return the type.
     */
    public Class<?> type() {
        return type;
    }

    public void type(Class<?> type) {
        this.type = type;
        setInternalType();
    }

    public Class<?> internalType() {
        return internalType;
    }

    public Class<?> collectionType() {
        return collectionType;
    }
    
    public void collectionType( Class<?> collectionType) {
        this.collectionType = collectionType;
    }

    public boolean isTypeNotSet() {
        return type == null || type.equals( Predicate.UNSET.class );
    }

    /**
     * True if the first character in the method name should be uppercased.
     * Otherwise it is lowercased.
     * 
     * @return true if the first character should be lower cased.
     */
    public boolean upcase() {
        return upcase;
    }

    @Override
    public String toString() {
        return String.format(
                "EffectivePredicate[ ns:%s n:%s impl:%s,  Types[e:%s i:%s c:%s] uc:%s enull:%s lit:%s postExec:%s ]",
                namespace, name, impl, type, internalType, collectionType, upcase, emptyIsNull, literalType,
                postExec == null ? 0 : postExec.size() );
    }
    
    @Override
    public boolean equals( Object o )
    {
        if (o instanceof EffectivePredicate)
        {
            EffectivePredicate other = (EffectivePredicate) o;
            if (actionType == null) 
            {
                return super.equals( o );
            }
            return new EqualsBuilder()
                    .append( actionType, other.actionType )
                    .append( upcase, other.upcase )
                    .append( name, other.name )
                    .append( namespace, other.namespace )
                    .append( literalType, other.literalType )
                    .append( type, other.type )
                    .append( internalType, other.internalType )
                    .append( collectionType, other.collectionType )
                    .append( emptyIsNull, other.emptyIsNull )
                    .append( impl, other.impl )
                    .append( postExec, other.postExec )
                    .build();
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return actionType==null?super.hashCode():actionType.hashCode();
    }
    
    public String formattedString() {
        return String.format(
                "EffectivePredicate[ %n\tAction: %s%n\tCollection: %s%n\temptyIsNull: %s%n\t"
                + "Impl: %s%n\tInternalType: %s%n\tLiteralType %s%n\tName: %s%n\tNamespace: %s%n\t"
                + "Type: %s%n\tUpcase: %s%n\tPostExce: %s%n]",
                actionType, collectionType, emptyIsNull, impl, internalType, literalType, name,
                namespace, type, upcase, postExec );
    }

}