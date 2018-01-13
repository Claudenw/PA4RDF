package org.xenei.jena.entities.impl.predicate.collection;

import org.xenei.jena.entities.testing.impl.CollectionInterfaceImpl;

public class CollectionInterfaceImplTest extends AbstractCollectionInterfaceTest {
    public CollectionInterfaceImplTest() throws Exception {
        super( CollectionInterfaceImpl.class );
        builder.setImpl( true ).setNamespace( "" );
    }
}
