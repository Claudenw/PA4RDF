package org.xenei.jena.entities.impl.predicate.collectionValue;

import org.xenei.jena.entities.testing.impl.MultiValueSubjectImpl;

public class MultiValueSubjectImplTest extends MultiValueInterfaceTest {

    public MultiValueSubjectImplTest() throws NoSuchMethodException, SecurityException {
        super( MultiValueSubjectImpl.class );
        builder.setImpl( true ).setNamespace( "" );
    }

}
