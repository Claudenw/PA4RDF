package org.xenei.jena.entities.impl;

import org.xenei.jena.entities.testing.impl.SimpleInterfaceImpl;

public class MethodParserTest_SimpleInterfaceImpl extends AbstractSimpleInterfaceTest {

    public MethodParserTest_SimpleInterfaceImpl() throws Exception {
        super( SimpleInterfaceImpl.class );
        PIMap.put( getter, mockPredicateInfo( getter, "x", ActionType.GETTER, String.class, 1 ) );
        OMMap.put( remover, lh );
        OMMap.put( existential, lh );
    }

}
