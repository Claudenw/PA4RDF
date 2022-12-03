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

public class CharDatatypeTest {

    @Test
    public void parseTest() {
        char c = 'c';
        Object o = CharDatatype.INSTANCE.parse( "c" );
        assertEquals( c, o );
        
        String s1 = "\uE282";
        c = s1.charAt( 0 );
        o = CharDatatype.INSTANCE.parse( s1 );
        assertEquals( c, o );
    }
    
    @Test
    public void unparseTest() {
        char c = 'c';
        String lexicalForm = CharDatatype.INSTANCE.unparse( c );
        assertEquals( "c", lexicalForm );
        Literal l = ResourceFactory.createTypedLiteral( lexicalForm, CharDatatype.INSTANCE );
        assertEquals( "c^^http://www.w3.org/2001/XMLSchema#string", l.toString());
        char ch = l.getChar();
        assertEquals( c, ch );
        
        String s1 = "\uE282";
        c = s1.charAt( 0 );
        lexicalForm = CharDatatype.INSTANCE.unparse( c );
        assertEquals( s1, lexicalForm );
        l = ResourceFactory.createTypedLiteral( lexicalForm, CharDatatype.INSTANCE );
        assertEquals( "\uE282^^http://www.w3.org/2001/XMLSchema#string", l.toString());
        ch = l.getChar();
        assertEquals( c, ch );
        
    }
    
    @Test
    public void isEqualTest() {
        LiteralLabel ll1 = LiteralLabelFactory.create("c", CharDatatype.INSTANCE);
        LiteralLabel ll2 = LiteralLabelFactory.create("c", CharDatatype.INSTANCE);
        CharDatatype.INSTANCE.isEqual( ll1, ll2);
        
        ll2 = LiteralLabelFactory.create("c", XSDDatatype.XSDstring);
        CharDatatype.INSTANCE.isEqual(ll1, ll2);
    }
    
    @Test
    public void parseValidatedTest() {
        assertEquals( 'c', CharDatatype.INSTANCE.parseValidated( "c" ));
        assertThrows( DatatypeFormatException.class, ()->CharDatatype.INSTANCE.parseValidated( "cd" ));
        
        String s1 = "\uE282";
        char c = s1.charAt( 0 );
        assertEquals( c, CharDatatype.INSTANCE.parseValidated( s1 ));
        
    }
}
