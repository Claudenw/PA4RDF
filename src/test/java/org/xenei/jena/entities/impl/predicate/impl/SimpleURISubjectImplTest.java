package org.xenei.jena.entities.impl.predicate.impl;

import org.apache.jena.rdf.model.RDFNode;
import org.junit.Test;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.predicate.AbstractPredicateTest;
import org.xenei.jena.entities.testing.impl.SimpleURISubjectImpl;

public class SimpleURISubjectImplTest extends AbstractPredicateTest
{
    public SimpleURISubjectImplTest() {
        super( SimpleURISubjectImpl.class );
        builder.setNamespace( "http://example.com/" )
        .setName(  "u" )
        .setImpl(  true  );

    }

    @Test
    public final void testParseGetter() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setInternalType(  RDFNode.class )
        .setType(  RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "getU" ) );
    }

    @Test
    public final void testParseSetter() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setInternalType(  RDFNode.class )
        .setType(  URI.class );
        assertSame( builder, interfaceClass.getMethod( "setU", String.class ) );
    }

    @Test
    public final void testParseExistential() throws Exception {
        builder.setActionType( ActionType.EXISTENTIAL )
        .setInternalType(  RDFNode.class )
        .setType(  Predicate.UNSET.class );
        assertSame( builder, interfaceClass.getMethod( "hasU" ) );
    }

    @Test
    public final void testParseRemover() throws Exception {
        builder.setActionType( ActionType.REMOVER )
        .setInternalType(  RDFNode.class )
        .setType(  Predicate.UNSET.class );
        assertSame( builder, interfaceClass.getMethod( "removeU" ) );
    }


}
