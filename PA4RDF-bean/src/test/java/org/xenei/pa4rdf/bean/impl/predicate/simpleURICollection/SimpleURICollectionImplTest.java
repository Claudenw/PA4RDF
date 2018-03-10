package org.xenei.pa4rdf.bean.impl.predicate.simpleURICollection;

import org.xenei.pa4rdf.bean.test.impl.SimpleURICollectionImpl;

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
