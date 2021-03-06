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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.shared.Lock;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.WrappedIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.handlers.EntityHandler;
import org.xenei.jena.entities.impl.handlers.ListHandler;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.impl.handlers.VoidHandler;

/**
 * The parsed information about a predicate method.
 * 
 * Use of this class registers three (3) new RDFDatatypes: CharacterDatatype
 * that parses Character objects CharDatatype that parses char.class objects
 * LongDatatype that parses Longs and always returns a long.
 */
public class PredicateInfoImpl implements PredicateInfo {
    private Class<?> concreteType;
    private final Class<?> valueClass;
    private final String methodName;
    private Property property;
    private final ActionType actionType;
    private final EffectivePredicate predicate;
    private final Map<Class<?>, Annotation> annotations;

    /**
     * Create a sorted list of registered data types.
     * 
     * The format is a String.format format string for two (2) string inputs.
     * The first one is the URI of the data type, the second the class name or a
     * blank. To reverse the display use "%2$s | %1$s"
     * 
     * If the nullCLassString is null registered data types without classes will
     * not be included.
     * 
     * @param format
     *            The output format or "%s | %s" if not specified.
     * @param nullClassString
     *            the string to print for null java class.
     * @return A sorted list of registeded data types.
     */
    public static List<String> dataTypeDump(final String format, final String nullClassString) {
        final List<String> retval = new ArrayList<String>();
        final String fmt = StringUtils.defaultIfEmpty( format, "%s | %s" );

        final TypeMapper mapper = TypeMapper.getInstance();

        for (final Iterator<RDFDatatype> iter = mapper.listTypes(); iter.hasNext();) {
            final RDFDatatype dt = iter.next();
            if ((dt.getJavaClass() != null) || (nullClassString != null)) {
                retval.add( String.format( fmt, dt.getURI(),
                        PredicateInfoImpl.dataTypeDump_ClassType( dt.getJavaClass(), nullClassString ) ) );
            }
        }
        Collections.sort( retval );
        return retval;
    }

    // helper function for dataTypeDump to format java class
    private static String dataTypeDump_ClassType(final Class<?> clazz, final String nullClassString) {
        if (clazz == null) {
            return nullClassString;
        }
        if (clazz.isArray()) {
            return PredicateInfoImpl.dataTypeDump_ClassType( clazz.getComponentType(), nullClassString ) + "[]";
        }
        return clazz.getName();
    }

    /**
     * Get the ObjectHandler for a predicate.
     * 
     * @param entityManager
     *            The entity manager this to use.
     * @param returnType
     *            The ObjectHandler of the proper type
     * @param pred
     *            The EffectivePredicate definition.
     * @return The object handler.
     */
    public static ObjectHandler getHandler(final EntityManager entityManager, final Class<?> returnType,
            final EffectivePredicate pred) {
        final TypeMapper typeMapper = TypeMapper.getInstance();
        RDFDatatype dt = null;
        if ((pred != null) && !pred.literalType().equals( "" )) {
            dt = typeMapper.getSafeTypeByName( pred.literalType() );
        } else {
            dt = typeMapper.getTypeByClass( returnType );
        }
        if (dt != null) {
            return new LiteralHandler( dt );
        }
        if (returnType != null) {
            if (returnType.getAnnotation( Subject.class ) != null) {
                return new EntityHandler( entityManager, returnType );
            }
            if (RDFList.class.isAssignableFrom( returnType )) {
                Class<?> contained;
                ObjectHandler innerHandler;
                if (pred != null) {
                    contained = pred.contained();
                    if (contained == null || contained.equals( RDFList.class )) {
                        throw new IllegalArgumentException( "contained value must be set and may not be RDFList" );
                    }
                    innerHandler = getHandler( entityManager, contained, null );
                } else {
                    innerHandler = new ResourceHandler();
                }
                return new ListHandler( entityManager, innerHandler );
            }
            if (RDFNode.class.isAssignableFrom( returnType )) {
                return new ResourceHandler();
            }
            if (returnType.equals( URI.class )) {
                return new UriHandler();
            }
        }
        return new VoidHandler();
    }

    /**
     * Constructor.
     * 
     * @param entityManager
     *            The EntityManager that this predicate is assocatied with.
     * @param predicate
     *            The EffectivePredicate instance that describes the predicate.
     * @param methodName
     *            The name of the method that this predicate calls.
     * @param valueClass
     *            The class type for the return (getter) or parameter (setter)
     */
    public PredicateInfoImpl(final EntityManagerImpl entityManager, final EffectivePredicate predicate,
            final Method method, final Class<?> valueClass) {
        if (predicate == null) {
            throw new IllegalArgumentException( "Predicate may not be null" );
        }
        this.methodName = method.getName();
        this.actionType = ActionType.parse( methodName );
        this.valueClass = valueClass;
        this.annotations = new HashMap<Class<?>, Annotation>();
        this.predicate = predicate;
        addAnnotations( method );
        if (URI.class.equals( predicate.type() )) {
            concreteType = URI.class;
        } else {
            if ((valueClass != null) && (Iterator.class.isAssignableFrom( valueClass )
                    || Collection.class.isAssignableFrom( valueClass ))) {
                concreteType = predicate.type();
            } else {
                concreteType = valueClass;
            }
        }

        if ((concreteType != null) && (valueClass != null)) {
            if (concreteType.isPrimitive() && !valueClass.isPrimitive()) {
                /*
                 * This allows us to have setters that take primitives but
                 * getters that return objects.
                 */
                concreteType = valueClass;
            } else if (!concreteType.isPrimitive() && valueClass.isPrimitive()) {
                concreteType = valueClass;
            }
        }
    }

    public PredicateInfoImpl(PredicateInfoImpl pi) {
        this.actionType = pi.actionType;
        this.concreteType = pi.concreteType;
        this.methodName = pi.methodName;
        this.predicate = new EffectivePredicate( pi.predicate );
        this.property = pi.property;
        this.valueClass = pi.valueClass;
        this.annotations = new HashMap<Class<?>, Annotation>( pi.annotations );
    }

    /* package private */ void addAnnotations(Method method) {
        for (final Annotation a : method.getAnnotations()) {
            annotations.put( a.annotationType(), a );
        }
    }

    /**
     * Get the ObjectHandler for this predicate info.
     * 
     * @param entityManager
     *            the EntityManager to use for the object handler.
     * @return the ObjectHandler instance for this predicate info
     */
    public ObjectHandler getObjectHandler(EntityManager entityManager) {
        return PredicateInfoImpl.getHandler( entityManager, concreteType, predicate );
    }

    private Property createResourceProperty(final Resource resource) {
        return (resource.getModel() == null) ? ResourceFactory.createProperty( getUriString() )
                : resource.getModel().createProperty( getUriString() );
    }

    /**
     * Execute the method against the resource with the arguments.
     * 
     * @param method
     *            The method to execute
     * @param resource
     *            The resource to execute it against
     * @param args
     *            The arguments to the method.
     * @return The result of the execution
     * @throws NullPointerException
     *             if the return type of the method is a primitive and the
     *             predicate does not exist on the resource.
     */
    public Object exec(final EntityManagerImpl entityManager, final Method method, final Resource resource,
            final Object[] args) {
        final ObjectHandler objectHandler = getObjectHandler( entityManager );
        final Property p = createResourceProperty( resource );
        Object retval = null;
        switch (actionType) {
        case GETTER:
            retval = execRead( objectHandler, resource, p );
            break;
        case SETTER:
            if (method.getName().startsWith( "set" )) {
                retval = execSet( entityManager, objectHandler, resource, p, args );
            } else {
                retval = execAdd( objectHandler, resource, p, args );
            }
            break;
        case REMOVER:
            retval = execRemove( entityManager, objectHandler, resource, p, args );
            break;
        case EXISTENTIAL:
            retval = execHas( objectHandler, resource, p, args );
            break;
        }
        return retval;
    }

    private Object execAdd(final ObjectHandler objectHandler, final Resource resource, final Property p,
            final Object[] args) {
        final boolean empty = objectHandler.isEmpty( args[0] );
        if (!empty || !predicate.emptyIsNull()) {
            final RDFNode o = objectHandler.createRDFNode( args[0] );
            if (o != null) {
                resource.addProperty( p, o );
            }
        }
        return null;
    }

    private Object execHas(final ObjectHandler objectHandler, final Resource resource, final Property p,
            final Object[] args) {
        try {
            resource.getModel().enterCriticalSection( Lock.READ );

            return resource.hasProperty( p, objectHandler.createRDFNode( args[0] ) );
        } finally {
            resource.getModel().leaveCriticalSection();
        }
    }

    private Object execRead(final ObjectHandler objectHandler, final Resource resource, final Property p) {
        if (Iterator.class.isAssignableFrom( valueClass )) {
            return execReadMultiple( objectHandler, resource, p );
        } else if (Collection.class.isAssignableFrom( valueClass )) {
            return execReadCollection( objectHandler, resource, p );
        } else {
            return execReadSingle( objectHandler, resource, p );
        }
    }

    private Object execReadCollection(final ObjectHandler objectHandler, final Resource resource, final Property p) {
        if (RDFList.class.equals( this.concreteType )) {
            return execReadSingle( objectHandler, resource, p );
        }
        resource.getModel().enterCriticalSection( Lock.READ );
        try {

            final NodeIterator iter = resource.getModel().listObjectsOfProperty( resource, p );

            final ExtendedIterator<Object> oIter = iter.mapWith( new Function<RDFNode, Object>() {

                @Override
                public Object apply(final RDFNode rdfNode) {
                    return objectHandler.parseObject( rdfNode );
                }
            } );
            if (List.class.isAssignableFrom( valueClass )) {
                return oIter.toList();
            } else if (Set.class.isAssignableFrom( valueClass )) {
                return oIter.toSet();
            } else if (Queue.class.isAssignableFrom( valueClass )) {
                return new LinkedList<Object>( oIter.toList() );
            } else {
                return oIter.toList();
            }

        } finally {
            resource.getModel().leaveCriticalSection();
        }
    }

    private ExtendedIterator<?> execReadMultiple(final ObjectHandler objectHandler, final Resource resource,
            final Property p) {
        if (RDFList.class.equals( this.concreteType )) {
            final List<?> lst = (List<?>) execReadSingle( objectHandler, resource, p );
            return WrappedIterator.create( lst.iterator() );
        }

        resource.getModel().enterCriticalSection( Lock.READ );
        try {
            final NodeIterator iter = resource.getModel().listObjectsOfProperty( resource, p );

            return iter.mapWith( new Function<RDFNode, Object>() {

                @Override
                public Object apply(final RDFNode rdfNode) {
                    return objectHandler.parseObject( rdfNode );
                }
            } );
        } finally {
            resource.getModel().leaveCriticalSection();
        }
    }

    private Object execReadSingle(final ObjectHandler objectHandler, final Resource resource, final Property p) {
        try {
            resource.getModel().enterCriticalSection( Lock.READ );

            final StmtIterator iter = resource.listProperties( p );
            final Object retval = null;
            try {
                if (iter.hasNext()) {
                    final Statement s = iter.next();
                    return objectHandler.parseObject( s.getObject() );
                }
            } finally {
                iter.close();
            }
            if (retval == null) {
                if (concreteType.isPrimitive()) {
                    throw new NullPointerException(
                            String.format( "Null valueClass (%s) was assigned to a variable of primitive type: %s",
                                    this.methodName, concreteType ) );
                }

            }
            return retval;
        } finally {
            resource.getModel().leaveCriticalSection();
        }
    }

    private Object execRemove(final EntityManagerImpl entityManager, final ObjectHandler objectHandler,
            final Resource resource, final Property p, final Object[] args) {
        try {
            resource.getModel().enterCriticalSection( Lock.WRITE );
            final RDFNode value = args.length == 0 ? null : objectHandler.createRDFNode( args[0] );
            for (final Statement stmt : resource.listProperties( p ).toList()) {
                objectHandler.removeObject( stmt, value );
            }
            if (valueClass == null) {
                resource.removeAll( p );
            }
            return null;
        } finally {
            resource.getModel().leaveCriticalSection();
        }
    }

    private Object execSet(final EntityManagerImpl entityManager, final ObjectHandler objectHandler,
            final Resource resource, final Property p, final Object[] args) {
        final List<Statement> stmtLst = resource.listProperties( p ).toList();
        try {
            resource.getModel().enterCriticalSection( Lock.WRITE );
            final RDFNode value = objectHandler.createRDFNode( args[0] );

            for (final Statement stmt : stmtLst) {
                objectHandler.removeObject( stmt, value );
            }
            resource.removeAll( p ); // just in case it get set by another
            // thread
            // first.
            return execAdd( objectHandler, resource, p, args );
        } finally {
            resource.getModel().leaveCriticalSection();

            if (stmtLst.size() > 1) {
                final Logger log = LoggerFactory.getLogger( PredicateInfoImpl.class );
                try {
                    throw new Exception( String.format( "Error processing %s.set%s", resource, p ) );
                } catch (final Exception e) {
                    log.error( "Error:", e );
                    for (final Statement s : stmtLst) {
                        log.error( "Statement: {} ", s.asTriple() );
                    }
                }

            }
        }
    }

    /**
     * Get the action type for the functin.
     */
    @Override
    public ActionType getActionType() {
        return actionType;
    }

    public EffectivePredicate getEffectivePredicate() {
        return predicate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xenei.jena.entities.impl.PredicateInfo#getFunction()
     */
    @Override
    public String getMethodName() {
        return methodName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xenei.jena.entities.impl.PredicateInfo#getNamespace()
     */
    @Override
    public String getNamespace() {
        return predicate.namespace();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xenei.jena.entities.impl.PredicateInfo#getProperty()
     */
    @Override
    public Property getProperty() {
        if (property == null) {
            property = ResourceFactory.createProperty( getUriString() );
        }
        return property;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xenei.jena.entities.impl.PredicateInfo#getUri()
     */
    @Override
    public String getUriString() {
        return predicate.namespace() + predicate.name();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xenei.jena.entities.impl.PredicateInfo#getValue()
     */
    @Override
    public Class<?> getValueClass() {
        return valueClass;
    }

    @Override
    public String toString() {
        return String.format( "%s(%s)", methodName, valueClass );
    }

    @Override
    public List<Method> getPostExec() {
        if (predicate.postExec == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList( predicate.postExec );
    }

    @Override
    public Collection<Annotation> getAnnotations() {
        return Collections.unmodifiableCollection( annotations.values() );
    }
}
