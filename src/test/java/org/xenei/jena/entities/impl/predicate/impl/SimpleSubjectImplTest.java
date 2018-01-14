package org.xenei.jena.entities.impl.predicate.impl;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Test;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.predicate.AbstractPredicateTest;
import org.xenei.jena.entities.testing.impl.SimpleSubjectImpl;

public class SimpleSubjectImplTest extends AbstractPredicateTest {
    public SimpleSubjectImplTest() throws NoSuchMethodException, SecurityException {
        super( SimpleSubjectImpl.class );
        builder.setNamespace( "http://example.com/" ).setName( "x" ).setImpl( true );

    }

    @Test
    public final void testParseGetter() throws Exception {
        builder.setActionType( ActionType.GETTER ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDstring ).setType( String.class );
        assertSame( builder, interfaceClass.getMethod( "getX" ) );
    }

    @Test
    public final void testParseSetter() throws Exception {
        builder.setActionType( ActionType.SETTER ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDstring ).setType( String.class );
        assertSame( builder, interfaceClass.getMethod( "setX", String.class ) );
    }

    @Test
    public final void testParseExistential() throws Exception {
        builder.setActionType( ActionType.EXISTENTIAL ).setInternalType( RDFNode.class )
                .setType( Predicate.UNSET.class );
        assertSame( builder, interfaceClass.getMethod( "hasX" ) );
    }

    @Test
    public final void testParseRemover() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setInternalType( RDFNode.class ).setType( Predicate.UNSET.class );
        assertSame( builder, interfaceClass.getMethod( "removeX" ) );
    }

}
