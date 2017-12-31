package org.xenei.jena.entities.impl.handlers;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.WrappedIterator;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.impl.ObjectHandler;

/**
 * Create and Manipulate RDFList objects
 *
 */
public class ListHandler extends AbstractObjectHandler {
    private final EntityManager entityManager;
    private final ObjectHandler innerHandler;

    public ListHandler(EntityManager entityManager, ObjectHandler innerHandler) {
        this.entityManager = entityManager;
        this.innerHandler = innerHandler;
    }

    @Override
    public RDFNode createRDFNode(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Iterator) {
            final ExtendedIterator<RDFNode> iter = WrappedIterator.create( (Iterator<?>) obj )
                    .mapWith( o -> innerHandler.createRDFNode( o ) );
            return entityManager.getModel().createList( iter );
        }
        if (obj.getClass().isArray()) {
            final Object[] ary = (Object[]) obj;
            return createRDFNode( Arrays.asList( ary ).iterator() );
        } else if (obj instanceof Collection) {
            return createRDFNode( ((Collection<?>) obj).iterator() );
        } else if (obj instanceof RDFNode) {
            return ((RDFNode) obj).as( RDFList.class );
        }
        throw new IllegalArgumentException( String.format( "%s is not an RDFList or convertable object type", obj ) );
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
        if (stmt.getObject().canAs( RDFList.class )) {
            final RDFList lst = stmt.getObject().as( RDFList.class );
            lst.removeList();
        }
        stmt.getSubject().getModel().remove( stmt );
    }

}
