package org.xenei.pa4rdf.bean.impl.predicate.simple;

import org.xenei.pa4rdf.bean.test.impl.SimpleSubjectImpl;

public class SimpleSubjectImplTest extends AbstractSimpleInterfaceTest {
    public SimpleSubjectImplTest() throws NoSuchMethodException, SecurityException {
        super( SimpleSubjectImpl.class );
        builder.setImpl( true );
    }

}
