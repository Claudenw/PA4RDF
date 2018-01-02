package org.xenei.jena.entities.impl;

import org.apache.jena.rdf.model.RDFNode;
import org.xenei.jena.entities.testing.impl.SimpleURIInterfaceImpl;

public class MethodParserTest_SimpleURIInterfaceImpl extends AbstractSimpleURIInterfaceTest {

    public MethodParserTest_SimpleURIInterfaceImpl() throws Exception {
        super( SimpleURIInterfaceImpl.class );
        PIMap.clear();

        PIMap.put( getter, mockPredicateInfo( getter, "u", ActionType.GETTER, RDFNode.class, 1, 0 ) );

        PIMap.put( setterR, mockPredicateInfo( setterR, "u", ActionType.SETTER, RDFNode.class, 1, 0 ) );

        PIMap.put( setterS, mockPredicateInfo( setterS, "u", ActionType.SETTER, String.class, 1, 0 ) );

        PIMap.put( remover, mockPredicateInfo( remover, "u", ActionType.REMOVER, null, 1, 0 ) );

        PIMap.put( existential, mockPredicateInfo( existential, "u", ActionType.EXISTENTIAL,
                TypeChecker.getPrimitiveClass( Boolean.class ), 1, 0 ) );

    }

}
