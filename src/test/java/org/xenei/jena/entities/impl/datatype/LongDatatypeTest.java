package org.xenei.jena.entities.impl.datatype;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.impl.LiteralLabel;
import org.apache.jena.graph.impl.LiteralLabelFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.Test;

public class LongDatatypeTest {

    @Test
    public void parseTest() {
        Long l = Long.valueOf( 5L );
        Object o = LongDatatype.INSTANCE.parse( "5");
        assertEquals( l, o );
    }
    
    @Test
    public void unparseTest() {
        Long l = Long.valueOf( 5L );
        String lexicalForm = LongDatatype.INSTANCE.unparse( l );
        assertEquals( "5", lexicalForm );
        Literal lit = ResourceFactory.createTypedLiteral( lexicalForm, LongDatatype.INSTANCE );
        assertEquals( "5^^http://www.w3.org/2001/XMLSchema#long", lit.toString());
        long lng = lit.getLong();
        assertEquals( 5L, lng );
    }
    
    @Test
    public void isEqualTest() {
        LiteralLabel ll1 = LiteralLabelFactory.create("6", LongDatatype.INSTANCE);
        LiteralLabel ll2 = LiteralLabelFactory.create("6", LongDatatype.INSTANCE);
        CharacterDatatype.INSTANCE.isEqual( ll1, ll2);
        
        ll2 = LiteralLabelFactory.create("6", XSDDatatype.XSDstring);
        CharacterDatatype.INSTANCE.isEqual(ll1, ll2);
    }
}
