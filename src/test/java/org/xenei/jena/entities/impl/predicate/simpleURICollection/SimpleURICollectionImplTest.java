package org.xenei.jena.entities.impl.predicate.simpleURICollection;

import org.xenei.jena.entities.testing.impl.SimpleURICollectionImpl;

public class SimpleURICollectionImplTest extends AbstractSimpleURICollectionTest {

    public SimpleURICollectionImplTest() throws Exception {
        super( SimpleURICollectionImpl.class );
        builder.setImpl( true ).setNamespace( "" );
    }

    @Override
    protected void updateGetter() throws Exception {
        builder.setType( null ).setInternalType( null );
    }

    @Override
    protected void updateGetter2() throws Exception {
        builder.setType( null ).setInternalType( null );
    }

}
