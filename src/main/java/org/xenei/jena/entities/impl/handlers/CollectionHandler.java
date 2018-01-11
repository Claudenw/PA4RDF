package org.xenei.jena.entities.impl.handlers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.WrappedIterator;
import org.xenei.jena.entities.impl.ObjectHandler;


/**
 * Create and Manipulate RDFList objects or get/set for Collections or Arrays.
 * 
 * The get/set options should not be confused with add/contains/remove functionality
 *
 */
public class CollectionHandler extends AbstractObjectHandler {
    private enum Type { Collection, Iterator };
    /**
     * The class type that we are going to return
     */
    private final Class<?> returnType;

    private final ObjectHandler innerHandler;

    private final Type type; 

    private Constructor<?> constructor;

    /**
     * Create a collection handler that returns the associated type.
     * @param innerHandler
     * @param returnType
     */
    public CollectionHandler(final ObjectHandler innerHandler, Class<?> returnType) {
        this.returnType = returnType;
        this.innerHandler = innerHandler;
        if (Collection.class.isAssignableFrom( returnType))
        {
            type = Type.Collection;
            try {
                constructor = returnType.getConstructor( );
                return;
            } catch (NoSuchMethodException | SecurityException acceptable) {
                // do nothing
            }
            String cType="";
            try {

                if (List.class.isAssignableFrom(  returnType ))
                {
                    cType = "ArrayList";
                    constructor = ArrayList.class.getConstructor();
                    return;
                } 
                if (Set.class.isAssignableFrom( returnType ))
                {
                    cType = "HashSet";
                    constructor = HashSet.class.getConstructor();
                    return;
                } 
                if (Queue.class.isAssignableFrom( returnType ))
                {
                    cType = "LinkedList";
                    constructor = LinkedList.class.getConstructor();
                    return;
                }
                throw new IllegalStateException( "Can not find implementation for "+returnType );

            } catch (NoSuchMethodException | SecurityException e) {
                throw new IllegalStateException( "Can not find no argument constructor for "+cType, e );
            }
        } else
            if (Iterator.class.isAssignableFrom( returnType ))
            {
                type = Type.Iterator;
            } else {
                throw new IllegalArgumentException( returnType+" is not a supported collection type");
            }

    }

    @Override
    public RDFNode createRDFNode(Object obj) {
        return innerHandler.createRDFNode( obj );
    }

    @Override
    public boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj.getClass().isArray()) {
            final Object[] ary = (Object[]) obj;
            return ary.length == 0;
        } else if (obj instanceof Collection) {
            final Collection<?> col = (Collection<?>) obj;
            return col.size() == 0;
        } else if (obj instanceof RDFNode) {
            return ((RDFNode) obj).as( RDFList.class ).isEmpty();
        }
        throw new IllegalArgumentException( String.format( "%s is not an RDFList or convertable object type", obj ) );
    }

    @Override
    public Object parseObject(RDFNode node) {
        if (node.canAs( RDFList.class )) {
            final RDFList lst = node.as( RDFList.class );
            return lst.asJavaList().stream().map( r -> innerHandler.parseObject( r ) ).collect( Collectors.toList() );
        }
        return node;
    }

    public Object makeCollection( Iterator<?> oIter ) {
        switch (type)
        {
        case Iterator:
            return oIter;
        case Collection:           
            try {
                @SuppressWarnings("unchecked")
                final Collection<Object> col = (Collection<Object>) constructor.newInstance( );
                col.addAll(  WrappedIterator.create( oIter ).toList() );
                return col;
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new IllegalStateException( constructor+" call to no arg constructor failed", e);
            }

        }
        throw new IllegalStateException( type+" is not Iterator or Collection");


    }   

    @Override
    public boolean equals(final Object o) {
        if (o instanceof CollectionHandler) {
            final CollectionHandler other = (CollectionHandler) o;
            return new EqualsBuilder()
                    .append(  returnType, other.returnType )
                    .append( innerHandler, other.innerHandler )                   
                    .build();            
        }
        return false;
    }

    @Override
    public int hashCode() {
        return innerHandler.hashCode();
    }

    @Override
    public String toString() {
        return String.format(  "CollectionHandler{ inner:%s return:%s}", innerHandler, returnType );
    }
}
