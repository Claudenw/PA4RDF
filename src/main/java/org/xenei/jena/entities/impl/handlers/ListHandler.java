package org.xenei.jena.entities.impl.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.WrappedIterator;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.impl.EffectivePredicate;
import org.xenei.jena.entities.impl.ObjectHandler;
import org.xenei.jena.entities.impl.PredicateInfoImpl;

/**
 * Create and Manipulate RDFList objects or get/set for Collections or Arrays.
 * 
 * The get/set options should not be confused with add/contains/remove functionality
 *
 */
public class ListHandler extends AbstractObjectHandler {
    /**
     * The class type that we are going to return
     */
    private final Class<?> returnType;
    
    private final ObjectHandler innerHandler;

    public ListHandler(final ObjectHandler innerHandler, Class<?> returnType) {
       this.returnType = returnType;
      //  this.entityManager = entityManager;
        this.innerHandler = innerHandler; 
        
    }


    @Override
    public RDFNode createRDFNode(Object obj) {
//        if (isList)
//        {
//        if (obj == null) {
//            return null;
//        }
//        if (obj instanceof Iterator) {
//            final ExtendedIterator<RDFNode> iter = WrappedIterator.create( (Iterator<?>) obj )
//                    .mapWith( o -> innerHandler.createRDFNode( o ) );
//            return entityManager.getModel().createList( iter );
//        }
//        if (obj.getClass().isArray()) {
//            final Object[] ary = (Object[]) obj;
//            return createRDFNode( Arrays.asList( ary ).iterator() );
//        } else if (obj instanceof Collection) {
//            return createRDFNode( ((Collection<?>) obj).iterator() );
//        } else if (obj instanceof RDFNode) {
//            return ((RDFNode) obj).as( RDFList.class );
//        }
//        throw new IllegalArgumentException( String.format( "%s is not an RDFList or convertable object type", obj ) );
//        }
        
        
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
        throw new IllegalArgumentException( String.format( "%s is not an RDFList or convertable object type", node ) );
    }

    @Override
    public void removeObject(Statement stmt, RDFNode value) {
//        if (isList)
//        {
//        if (stmt.getObject().canAs( RDFList.class )) {
//            final RDFList lst = stmt.getObject().as( RDFList.class );
//            lst.removeList();
//        }
//        stmt.getSubject().getModel().remove( stmt );
//        }
//        else {
            super.removeObject( stmt, value );
//        }
    }

    public Collection<RDFNode> asCollection( boolean emptyisNull, Object obj ) {
        Collection<Object> objs = null;
//        if (isList)
//        {
//            RDFList lst = (RDFList) createRDFNode( obj );
//            return Arrays.asList(  lst  );
//        }
        if (obj instanceof Collection)
        {
            objs = (Collection<Object>) obj;
        } else if (obj.getClass().isArray())
        {
            objs = new ArrayList<Object>(  Arrays.asList( obj ) );
        } else {
            objs = new ArrayList<Object>();
            objs.add(  obj  );
        }
        return objs.stream().map( o -> createRDFNode(o)).collect(  Collectors.toList() );
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof ListHandler) {
            ListHandler other = (ListHandler) o;
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
        return String.format(  "ListHandler{ inner:%s return:%s}", innerHandler, returnType );
    }
}
