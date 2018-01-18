package org.xenei.jena.entities.impl.parser.simpleURICollection;

import static org.junit.Assert.*;

import org.junit.Test;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.testing.impl.SimpleURICollectionSubjectImpl;

public class SimpleURICollectionSubjectImplTest extends AbstractSimpleURICollectionTest {
    public SimpleURICollectionSubjectImplTest() throws NoSuchMethodException, SecurityException {
        super( SimpleURICollectionSubjectImpl.class );
    }

    @Test
    public void testParseSetterS() throws Exception {
        /*
         *  we are returning a collection and have multiple add methods
         * so getU() should have a type set by other adders, but we are not parsing the full set
         * here so parsing it fails.
         */
        try {
        parser.parse( setterS );
        fail( "Should have thrown MissingAnnotation");
        }catch(MissingAnnotation expected) {
            // this is what should happen
        }
        
    }

}