package org.xenei.jena.entities.impl.datatype;

import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.impl.LiteralLabel;
import org.apache.jena.graph.impl.LiteralLabelFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CharDatatypeTest {

    @Test
    public void parseTest() {
        char c = 'c';
        Object o = CharDatatype.INSTANCE.parse( "c" );
        Assertions.assertEquals( c, o );

        final String s1 = "\uE282";
        c = s1.charAt( 0 );
        o = CharDatatype.INSTANCE.parse( s1 );
        Assertions.assertEquals( c, o );
    }

    @Test
    public void unparseTest() {
        char c = 'c';
        String lexicalForm = CharDatatype.INSTANCE.unparse( c );
        Assertions.assertEquals( "c", lexicalForm );
        Literal l = ResourceFactory.createTypedLiteral( lexicalForm, CharDatatype.INSTANCE );
        Assertions.assertEquals( "c^^http://www.w3.org/2001/XMLSchema#string", l.toString() );
        char ch = l.getChar();
        Assertions.assertEquals( c, ch );

        final String s1 = "\uE282";
        c = s1.charAt( 0 );
        lexicalForm = CharDatatype.INSTANCE.unparse( c );
        Assertions.assertEquals( s1, lexicalForm );
        l = ResourceFactory.createTypedLiteral( lexicalForm, CharDatatype.INSTANCE );
        Assertions.assertEquals( "\uE282^^http://www.w3.org/2001/XMLSchema#string", l.toString() );
        ch = l.getChar();
        Assertions.assertEquals( c, ch );

    }

    @Test
    public void isEqualTest() {
        final LiteralLabel ll1 = LiteralLabelFactory.create( "c", CharDatatype.INSTANCE );
        LiteralLabel ll2 = LiteralLabelFactory.create( "c", CharDatatype.INSTANCE );
        CharDatatype.INSTANCE.isEqual( ll1, ll2 );

        ll2 = LiteralLabelFactory.create( "c", XSDDatatype.XSDstring );
        CharDatatype.INSTANCE.isEqual( ll1, ll2 );
    }

    @Test
    public void parseValidatedTest() {
        Assertions.assertEquals( 'c', CharDatatype.INSTANCE.parseValidated( "c" ) );
        Assertions.assertThrows( DatatypeFormatException.class, () -> CharDatatype.INSTANCE.parseValidated( "cd" ) );

        final String s1 = "\uE282";
        final char c = s1.charAt( 0 );
        Assertions.assertEquals( c, CharDatatype.INSTANCE.parseValidated( s1 ) );

    }
}
