package org.xenei.jena.entities.impl.predicate.collectionValue;

import org.xenei.jena.entities.testing.impl.CollectionValueImpl;

public class CollectionValueIntfImplTest extends AbstractCollectionValueInterfaceTest {

    public CollectionValueIntfImplTest() throws NoSuchMethodException, SecurityException {
        super( CollectionValueImpl.class );
        builder.setImpl( true ).setNamespace( "" );
    }

    @Override
    protected void updateGetU() {
        builder.setType(  null ).setInternalType(  null );
    }

    @Override
    protected void updateGetU3() {
        builder.setType(  null ).setInternalType(  null );
    }

    @Override
    protected void updateGetU4() {
        builder.setType(  null ).setInternalType(  null ).setName(  "u4" );
    }


}
