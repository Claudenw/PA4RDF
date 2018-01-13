package org.xenei.jena.entities.impl.predicate.collection;

import org.xenei.jena.entities.impl.predicate.REVIEW.REVIEW_AbstractCollectionValueTest;
import org.xenei.jena.entities.testing.impl.CollectionSubjectImpl;

public class CollectionSubjectImplTest extends AbstractCollectionInterfaceTest {
    public CollectionSubjectImplTest() throws NoSuchMethodException, SecurityException {
        super( CollectionSubjectImpl.class );
        builder.setImpl( true  );
    }
}
