package org.xenei.jena.entities.impl.datatype;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.impl.LiteralLabel;
import org.apache.jena.graph.impl.LiteralLabelFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LongDatatypeTest {

    @Test
    public void parseTest() {
        final Long l = Long.valueOf( 5L );
        final Object o = LongDatatype.INSTANCE.parse( "5" );
        Assertions.assertEquals( l, o );
    }

    @Test
    public void unparseTest() {
        final Long l = Long.valueOf( 5L );
        final String lexicalForm = LongDatatype.INSTANCE.unparse( l );
        Assertions.assertEquals( "5", lexicalForm );
        final Literal lit = ResourceFactory.createTypedLiteral( lexicalForm, LongDatatype.INSTANCE );
        Assertions.assertEquals( "5^^http://www.w3.org/2001/XMLSchema#long", lit.toString() );
        final long lng = lit.getLong();
        Assertions.assertEquals( 5L, lng );
    }

    @Test
    public void isEqualTest() {
        final LiteralLabel ll1 = LiteralLabelFactory.create( "6", LongDatatype.INSTANCE );
        LiteralLabel ll2 = LiteralLabelFactory.create( "6", LongDatatype.INSTANCE );
        CharacterDatatype.INSTANCE.isEqual( ll1, ll2 );

        ll2 = LiteralLabelFactory.create( "6", XSDDatatype.XSDstring );
        CharacterDatatype.INSTANCE.isEqual( ll1, ll2 );
    }
}
