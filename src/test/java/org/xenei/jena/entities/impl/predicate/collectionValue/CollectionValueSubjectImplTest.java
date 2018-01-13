package org.xenei.jena.entities.impl.predicate.collectionValue;

import org.xenei.jena.entities.testing.impl.CollectionValueSubjectImpl;

public class CollectionValueSubjectImplTest extends AbstractCollectionValueInterfaceTest {

    public CollectionValueSubjectImplTest() throws NoSuchMethodException, SecurityException {
        super( CollectionValueSubjectImpl.class );
        builder.setImpl(  true  );
    }

}
