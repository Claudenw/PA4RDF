package org.xenei.jena.entities.impl.predicate.simpleURI;

import org.xenei.jena.entities.testing.impl.SimpleURISubjectImpl;

public class SimpleURISubjectImplTest extends AbstractSimpleURIInterfaceTest {
    public SimpleURISubjectImplTest() throws NoSuchMethodException, SecurityException {
        super( SimpleURISubjectImpl.class );
        builder.setImpl( true );

    }

}
