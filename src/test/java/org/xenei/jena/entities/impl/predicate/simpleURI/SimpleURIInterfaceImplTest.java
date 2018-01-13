package org.xenei.jena.entities.impl.predicate.simpleURI;

import org.xenei.jena.entities.testing.impl.SimpleURIInterfaceImpl;

public class SimpleURIInterfaceImplTest extends AbstractSimpleURIInterfaceTest {

    public SimpleURIInterfaceImplTest() throws Exception {
        super( SimpleURIInterfaceImpl.class );
        builder.setImpl( true )
        .setNamespace( "" );
    }

}
