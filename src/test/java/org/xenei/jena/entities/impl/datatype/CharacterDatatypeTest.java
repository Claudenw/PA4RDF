package org.xenei.jena.entities.impl.datatype;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.impl.LiteralLabel;
import org.apache.jena.graph.impl.LiteralLabelFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.Test;

public class CharacterDatatypeTest {

    @Test
    public void parseTest() {
        Object o = CharacterDatatype.INSTANCE.parse( "c" );
        assertEquals( Character.valueOf( 'c' ), o );
        
        String s1 = "\uE282";
        Character c = Character.valueOf(  s1.charAt( 0 ));
        assertEquals( c, CharacterDatatype.INSTANCE.parseValidated( s1 ));
    }
    
    @Test
    public void unparseTest() {
        Character c = Character.valueOf( 'c' );
        String lexicalForm = CharacterDatatype.INSTANCE.unparse( c );
        assertEquals( "c", lexicalForm );
        Literal l = ResourceFactory.createTypedLiteral( lexicalForm, CharacterDatatype.INSTANCE );
        assertEquals( "c^^http://www.w3.org/2001/XMLSchema#string", l.toString());
        char ch = l.getChar();
        assertEquals( 'c', ch );
        
        String s1 = "\uE282";
        c = Character.valueOf( s1.charAt( 0 ));
        lexicalForm = CharacterDatatype.INSTANCE.unparse( c );
        assertEquals( s1, lexicalForm );
        l = ResourceFactory.createTypedLiteral( lexicalForm, CharacterDatatype.INSTANCE );
        assertEquals( "\uE282^^http://www.w3.org/2001/XMLSchema#string", l.toString());
        ch = l.getChar();
        assertEquals( s1.charAt( 0 ), ch );
    }
    
    @Test
    public void isEqualTest() {
        LiteralLabel ll1 = LiteralLabelFactory.create("c", CharacterDatatype.INSTANCE);
        LiteralLabel ll2 = LiteralLabelFactory.create("c", CharacterDatatype.INSTANCE);
        CharacterDatatype.INSTANCE.isEqual( ll1, ll2);
        
        ll2 = LiteralLabelFactory.create("c", XSDDatatype.XSDstring);
        CharacterDatatype.INSTANCE.isEqual(ll1, ll2);
    }
    
    @Test
    public void parseValidatedTest() {
        assertEquals( Character.valueOf( 'c' ), CharacterDatatype.INSTANCE.parseValidated( "c" ));
        assertThrows( DatatypeFormatException.class, ()->CharacterDatatype.INSTANCE.parseValidated( "cd" ));
        
        String s1 = "\uE282";
        char c = s1.charAt( 0 );
        assertEquals( Character.valueOf( c ), CharacterDatatype.INSTANCE.parseValidated( s1 ));
        
    }
}
