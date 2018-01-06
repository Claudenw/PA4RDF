package org.xenei.jena.entities.impl;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.testing.impl.SimpleInterfaceImpl;

public class MethodParserTest_SimpleInterfaceImpl extends AbstractSimpleInterfaceTest {

    public MethodParserTest_SimpleInterfaceImpl() throws Exception {
        super( SimpleInterfaceImpl.class );

        PIMap.clear();
        PIMap.put( getter, mockPredicateInfo( getter, "x", ActionType.GETTER, String.class, 1, 1 ) );
        PIMap.put( setter, mockPredicateInfo( setter, "x", ActionType.SETTER, String.class, 1, 0 ) );
        PIMap.put( remover, mockPredicateInfo( remover, "x", ActionType.REMOVER, null, 1, 0 ) );
        PIMap.put( existential, mockPredicateInfo( existential, "x", ActionType.EXISTENTIAL,
                TypeChecker.getPrimitiveClass( Boolean.class ), 1, 0 ) );
        OMMap.put( remover, lh );
        OMMap.put( existential, lh );
    }
   
}
