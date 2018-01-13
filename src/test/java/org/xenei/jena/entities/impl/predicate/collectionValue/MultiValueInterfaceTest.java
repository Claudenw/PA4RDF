package org.xenei.jena.entities.impl.predicate.collectionValue;

import org.apache.jena.util.iterator.ExtendedIterator;
import org.xenei.jena.entities.testing.iface.MultiValueInterface;

public class MultiValueInterfaceTest extends AbstractCollectionValueInterfaceTest {

    public MultiValueInterfaceTest() throws NoSuchMethodException, SecurityException {
        super( MultiValueInterface.class );
    }

    protected MultiValueInterfaceTest( Class<?> underTest) throws NoSuchMethodException, SecurityException {
        super( underTest );
    }

    @Override
    protected void updateGetBool() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetChar() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetU() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetU3() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetU4() {
        builder.setCollectionType( ExtendedIterator.class );
    }


    @Override
    protected void updateGetDbl() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetEnt() {
        builder.setCollectionType( ExtendedIterator.class );
    }
    @Override
    protected void updateGetFlt() {
        builder.setCollectionType( ExtendedIterator.class );
    }
    @Override
    protected void updateGetInt() {
        builder.setCollectionType( ExtendedIterator.class );
    }
    @Override
    protected void updateGetLng() {
        builder.setCollectionType( ExtendedIterator.class );
    }
    @Override
    protected void updateGetRDF() {
        builder.setCollectionType( ExtendedIterator.class );
    }
    @Override
    protected void updateGetStr() {
        builder.setCollectionType( ExtendedIterator.class );
    }
}
