package org.xenei.jena.entities.impl.predicate.simple;

import org.xenei.jena.entities.testing.impl.SimpleSubjectImpl;

public class SimpleSubjectImplTest extends AbstractSimpleInterfaceTest {
    public SimpleSubjectImplTest() throws NoSuchMethodException, SecurityException {
        super( SimpleSubjectImpl.class );
        builder.setImpl( true );
    }

}
