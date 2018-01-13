package org.xenei.jena.entities.impl.predicate.simpleURICollection;

import org.junit.Assert;
import org.xenei.jena.entities.testing.impl.SimpleURICollectionImpl;

public class SimpleURICollectionImplTest extends AbstractSimpleURICollectionTest {

    public SimpleURICollectionImplTest() throws Exception {
        super( SimpleURICollectionImpl.class );
        builder.setImpl( true ).setNamespace( "" );
    }
    
    protected void updateGetter() throws Exception {
        builder.setType(  null ).setInternalType(  null );
    }

    protected void updateGetter2() throws Exception {
        builder.setType(  null ).setInternalType(  null );
    }

}
