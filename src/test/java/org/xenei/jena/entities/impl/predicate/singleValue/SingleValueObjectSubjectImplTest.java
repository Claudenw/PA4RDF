package org.xenei.jena.entities.impl.predicate.singleValue;

import org.xenei.jena.entities.testing.impl.SingleValueObjectSubjectImpl;

public class SingleValueObjectSubjectImplTest extends AbstractSingleValueObjectTest {

    public SingleValueObjectSubjectImplTest() {
        super( SingleValueObjectSubjectImpl.class );
        builder.setImpl( true );
    }

}
