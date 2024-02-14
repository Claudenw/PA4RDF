package org.xenei.jena.entities.impl.datatype;

import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.impl.LiteralLabel;
import org.apache.jena.graph.impl.LiteralLabelFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CharacterDatatypeTest {

    @Test
    public void parseTest() {
        final Object o = CharacterDatatype.INSTANCE.parse( "c" );
        Assertions.assertEquals( Character.valueOf( 'c' ), o );

        final String s1 = "\uE282";
        final Character c = Character.valueOf( s1.charAt( 0 ) );
        Assertions.assertEquals( c, CharacterDatatype.INSTANCE.parseValidated( s1 ) );
    }

    @Test
    public void unparseTest() {
        Character c = Character.valueOf( 'c' );
        String lexicalForm = CharacterDatatype.INSTANCE.unparse( c );
        Assertions.assertEquals( "c", lexicalForm );
        Literal l = ResourceFactory.createTypedLiteral( lexicalForm, CharacterDatatype.INSTANCE );
        Assertions.assertEquals( "c^^http://www.w3.org/2001/XMLSchema#string", l.toString() );
        char ch = l.getChar();
        Assertions.assertEquals( 'c', ch );

        final String s1 = "\uE282";
        c = Character.valueOf( s1.charAt( 0 ) );
        lexicalForm = CharacterDatatype.INSTANCE.unparse( c );
        Assertions.assertEquals( s1, lexicalForm );
        l = ResourceFactory.createTypedLiteral( lexicalForm, CharacterDatatype.INSTANCE );
        Assertions.assertEquals( "\uE282^^http://www.w3.org/2001/XMLSchema#string", l.toString() );
        ch = l.getChar();
        Assertions.assertEquals( s1.charAt( 0 ), ch );
    }

    @Test
    public void isEqualTest() {
        final LiteralLabel ll1 = LiteralLabelFactory.create( "c", CharacterDatatype.INSTANCE );
        LiteralLabel ll2 = LiteralLabelFactory.create( "c", CharacterDatatype.INSTANCE );
        CharacterDatatype.INSTANCE.isEqual( ll1, ll2 );

        ll2 = LiteralLabelFactory.create( "c", XSDDatatype.XSDstring );
        CharacterDatatype.INSTANCE.isEqual( ll1, ll2 );
    }

    @Test
    public void parseValidatedTest() {
        Assertions.assertEquals( Character.valueOf( 'c' ), CharacterDatatype.INSTANCE.parseValidated( "c" ) );
        Assertions.assertThrows( DatatypeFormatException.class,
                () -> CharacterDatatype.INSTANCE.parseValidated( "cd" ) );

        final String s1 = "\uE282";
        final char c = s1.charAt( 0 );
        Assertions.assertEquals( Character.valueOf( c ), CharacterDatatype.INSTANCE.parseValidated( s1 ) );

    }
}
