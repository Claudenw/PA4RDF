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

    /**
     * Copy constructor.
     * @param ep the Effective predicate to copy.
     */
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

    /**
     * Constructor. 
     * @param actionType The action type (may not be null)
     * @param collectionType The collection class type (may be null)
     * @param emptyIsNull True if empty values should be considered as null resources.
     * @param impl True if the predicate fronts a concrete implementation. 
     * @param internalType The class type of the object in the graph.
     * @param literalType The internal type of Class type is Literal.
     * @param name The name for the predicate.
     * @param namespace the namespace for the predicate
     * @param postExec a list of methods to execute at end of processing (may be null)
     * @param type The class that is used in the user/api interface.
     * @param upcase true if the first character of the name should be upper case.
     */
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
    
    /**
     * Constructor.
     * @param method The method to build the effective predicate from.
     */
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
                if (MethodParser.isCollection( method )) {
                    this.collectionType = method.getReturnType();
                } else {
                    this.type = method.getReturnType();
                }
                break;
            }
           
            if (this.collectionType == null && ActionType.isMultiple( method ))
            {
                this.collectionType = Predicate.UNSET.class;
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
                    name( actionType.extractName( method.getName() ) );
                } catch (final IllegalArgumentException e) {
                    // expected when not an action method.
                }
            }
        }
        else {
            actionType = null;
        }
    }

    /**
     * Check that the predicate is valid. 
     * 
     * Checks that type and internaleType are set and that literalType is set as required.
     * Throws RuntimeException if there are any issues.
     */
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

    /**
     * Set the internal type and literal type if necessary base on the type.
     */
    /* package private */void setInternalType() {
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

    /**
     * 
     * @return get the action type.
     */
    public ActionType actionType() {
        return actionType;
    }

    /**
     * May be empty but not null.
     * @return get the post exec method list.  
     */
    public List<Method> postExec() {
        return notNull( postExec, Collections.emptyList() );
    }

    /**
     * Add a methods to the post exec processing.
     * @param peMethods the collection of methods to add 
     */
    public void addPostExec(Collection<Method> peMethods) {
        for (final Method m : peMethods) {
            addPostExec( m );
        }
    }

    /**
     * Add a single method to the post exec processing.
     * @param peMethod the method to add
     */
    public void addPostExec(Method peMethod) {
        if (postExec == null) {
            postExec = new ArrayList<Method>();
        }
        if (!postExec.contains( peMethod )) {
            postExec.add( peMethod );
        }
    }

    /**
     * @return true if empty values should be considered as null.
     */
    public boolean emptyIsNull() {
        return emptyIsNull;
    }

    /**
     * 
     * @return true if the method is a concreate method.
     */
    public boolean impl() {
        return impl;
    }

    public RDFDatatype literalType() {
        return literalType;
    }

    /**
     * Return a value if it is not null.
     * if opt is Predicate.UNSET.class it is considered as null.
     * @param opt the item to check.
     * @param dflt the default if opt is null.
     * @return opt or null.
     */
    private <T> T notNull(T opt, T dflt) {
        return (opt == null || Predicate.UNSET.class.equals( opt )) ? dflt : opt;
    }

    /**
     * Merge another effective predicate into this on.
     * @param predicate the other predicate to merge.
     * @return this predicate for chaining.
     */
    public EffectivePredicate merge(final EffectivePredicate predicate) {
        if (predicate != null) {
            upcase = predicate.upcase();
            // use effective predicate name if specified
            name( StringUtils.isBlank( predicate.name() ) ? name : predicate.name() );
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

    
    /**
     * Merge from another effective predicate that is considered an ancestor to this one.
     * @param predicate the other predicate to merge.
     * @return this predicate for chaining.
     */
    public EffectivePredicate mergeParent(final EffectivePredicate predicate) {
        if (predicate != null) {
            upcase = predicate.upcase();
            // use effective predicate name if specified
            name( StringUtils.isBlank( predicate.name() ) ? name : predicate.name() );
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
            } else if ( URI.class.equals( predicate.type )) {
                // URI type is custom by annotation and not changeable.
                this.type = URI.class;
                internalType = notNull( internalType, predicate.internalType );
                literalType = notNull( literalType, predicate.literalType );
                setInternalType();

            } else {
                if ( !actionType.allowsNull() )
                {
                    type = notNull( type, predicate.type() );
                    internalType = notNull( internalType, predicate.internalType );
                    literalType = notNull( literalType, predicate.literalType );
                    setInternalType();
                }

            }
            for (Method m : predicate.postExec())
            {
                addPostExec( m );
            }

            collectionType = notNull( collectionType, predicate.collectionType );
            impl |= predicate.impl;
            
        }
        return this;
    }
    /**
     * Determine if this is a collection.  it is a collection of the collection type is not null.
     * @return true if this is a collection.
     */
    public boolean isCollection() {
        return collectionType != null;
    }

    /**
     * Merge a predicate into this effective predicate.
     * @param predicate the predicate to merge
     * @return this predicate for chaining.
     */
    public EffectivePredicate merge(final Predicate predicate) {
        if (predicate != null) {
            upcase = predicate.upcase();
            // use predicate name if specified
            name( StringUtils.isBlank( predicate.name() ) ? name : predicate.name() );
            
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

    /**
     * 
     * @return Get the name of the predicate this method represents in the graph.
     */
    public String name() {
        return name;
    }

    /**
     * 
     * @return Get the namespace for the predicate this method represents.
     */
    public String namespace() {
        return namespace;
    }

    /**
     * Set the name for the predicate this method represents.
     * The value of the name may be modified by the upcase flag.
     * @param name the name to set the predicate to.
     */
    public void name(final String name) {
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
     * then this is the type contained by the collection.
     * 
     * @return the type.
     */
    public Class<?> type() {
        return type;
    }

    /**
     * Set the user type.
     * @param type the class for the user type.
     */
    public void type(Class<?> type) {
        this.type = type;
        setInternalType();
    }

    /**
     * 
     * @return The internal type stored in the graph. 
     */
    public Class<?> internalType() {
        return internalType;
    }

    /**
     * 
     * @return The collection type or null if not set.
     */
    public Class<?> collectionType() {
        return collectionType;
    }
    
    /**
     * Set the collection type.
     * @param collectionType ollection type.
     */
    public void collectionType( Class<?> collectionType) {
        this.collectionType = collectionType;
    }

    /**
     * 
     * @return true if type is null or Predicate.UNSET.class
     */
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
    
    /**
     * @return a formatted string suitable for logging information.
     */
    public String formattedString() {
        return String.format(
                "EffectivePredicate[ %n\tAction: %s%n\tCollection: %s%n\temptyIsNull: %s%n\t"
                + "Impl: %s%n\tInternalType: %s%n\tLiteralType %s%n\tName: %s%n\tNamespace: %s%n\t"
                + "Type: %s%n\tUpcase: %s%n\tPostExce: %s%n]",
                actionType, collectionType, emptyIsNull, impl, internalType, literalType, name,
                namespace, type, upcase, postExec );
    }

}