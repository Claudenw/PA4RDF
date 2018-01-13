package org.xenei.jena.entities.impl.predicate.simple;

import org.xenei.jena.entities.testing.impl.SimpleInterfaceImpl;

public class SimpleInterfaceImplTest extends AbstractSimpleInterfaceTest {

    public SimpleInterfaceImplTest() throws Exception {
        super( SimpleInterfaceImpl.class );
        builder.setImpl( true )
        .setNamespace( "" );
    }

    @Override
    protected void updateGetter() throws NoSuchMethodException, SecurityException {
        builder.setPostExec( SimpleInterfaceImpl.class.getMethod( "postGetX", String.class ) );
    }
}
