package org.xenei.jena.entities.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Test;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.impl.datatype.CharacterDatatype;
import org.xenei.jena.entities.testing.abst.SingleValueMixedTypeTestClass;
import org.xenei.jena.entities.testing.iface.MultiValueInterface;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public class EffectivePredicateTest {
    private EffectivePredicate underTest;
    
    static {
        EntityManagerImpl.registerTypes();
    }

    @Test
    public void simpleGetTest() throws Exception {
        underTest = new EffectivePredicate( SimpleInterface.class.getMethod( "getX" ) );
        assertEquals( ActionType.GETTER, underTest.actionType() );
        assertEquals( String.class, underTest.type() );
        assertEquals( Literal.class, underTest.internalType() );
        assertEquals( XSDDatatype.XSDstring, underTest.literalType() );
        assertEquals( "http://example.com/", underTest.namespace() );
        assertEquals( "x", underTest.name() );
        assertTrue( underTest.postExec().isEmpty() );
        assertFalse( underTest.isCollection() );
        assertNull( underTest.collectionType() );
        assertFalse( underTest.impl() );
        assertFalse( underTest.emptyIsNull() );
        assertFalse( underTest.upcase() );
    }

    @Test
    public void simpleSetTest() throws Exception {
        underTest = new EffectivePredicate( SimpleInterface.class.getMethod( "setX", String.class ) );
        assertEquals( ActionType.SETTER, underTest.actionType() );
        assertEquals( String.class, underTest.type() );
        assertEquals( Literal.class, underTest.internalType() );
        assertEquals( XSDDatatype.XSDstring, underTest.literalType() );
        assertEquals( "http://example.com/", underTest.namespace() );
        assertEquals( "x", underTest.name() );
        assertTrue( underTest.postExec().isEmpty() );
        assertFalse( underTest.isCollection() );
        assertNull( underTest.collectionType() );
        assertFalse( underTest.impl() );
        assertFalse( underTest.emptyIsNull() );
        assertFalse( underTest.upcase() );
    }

    @Test
    public void simpleHasTest() throws Exception {
        underTest = new EffectivePredicate( SimpleInterface.class.getMethod( "hasX" ) );
        assertEquals( ActionType.EXISTENTIAL, underTest.actionType() );
        assertEquals( Predicate.UNSET.class, underTest.type() );
        assertEquals( RDFNode.class, underTest.internalType() );
        assertNull( underTest.literalType() );
        assertEquals( "http://example.com/", underTest.namespace() );
        assertEquals( "x", underTest.name() );
        assertTrue( underTest.postExec().isEmpty() );
        assertFalse( underTest.isCollection() );
        assertNull( underTest.collectionType() );
        assertFalse( underTest.impl() );
        assertFalse( underTest.emptyIsNull() );
        assertFalse( underTest.upcase() );
    }

    @Test
    public void simpleRemoveTest() throws Exception {
        underTest = new EffectivePredicate( SimpleInterface.class.getMethod( "removeX" ) );
        assertEquals( ActionType.REMOVER, underTest.actionType() );
        assertEquals( Predicate.UNSET.class, underTest.type() );
        assertEquals( RDFNode.class, underTest.internalType() );
        assertEquals( null, underTest.literalType() );
        assertEquals( "http://example.com/", underTest.namespace() );
        assertEquals( "x", underTest.name() );
        assertTrue( underTest.postExec().isEmpty() );
        assertFalse( underTest.isCollection() );
        assertNull( underTest.collectionType() );
        assertFalse( underTest.impl() );
        assertFalse( underTest.emptyIsNull() );
        assertFalse( underTest.upcase() );
    }

    @Test
    public void simpleValueCharTest() throws Exception {
        underTest = new EffectivePredicate( SingleValueMixedTypeTestClass.class.getMethod( "getChar" ) );
        assertEquals( ActionType.GETTER, underTest.actionType() );
        assertEquals( Character.class, underTest.type() );
        assertEquals( Literal.class, underTest.internalType() );
        assertEquals( CharacterDatatype.INSTANCE, underTest.literalType() );
        assertEquals( "http://localhost/test#", underTest.namespace() );
        assertEquals( "char", underTest.name() );
        assertTrue( underTest.postExec().isEmpty() );
        assertFalse( underTest.isCollection() );
        assertNull( underTest.collectionType() );
        assertFalse( underTest.impl() );
        assertFalse( underTest.emptyIsNull() );
        assertFalse( underTest.upcase() );
    }

    @Test
    public void multiValueGetTest() throws Exception {
        // getLst has not predicate annotation so some value will be missing.
        underTest = new EffectivePredicate( MultiValueInterface.class.getMethod( "getLst" ) );
        assertEquals( ActionType.GETTER, underTest.actionType() );
        assertEquals( null, underTest.type() );
        assertEquals( null, underTest.internalType() );
        assertEquals( null, underTest.literalType() );
        assertEquals( "http://localhost/test#", underTest.namespace() );
        assertEquals( "lst", underTest.name() );
        assertTrue( underTest.postExec().isEmpty() );
        assertTrue( underTest.isCollection() );
        assertEquals( Collection.class, underTest.collectionType() );
        assertFalse( underTest.impl() );
        assertFalse( underTest.emptyIsNull() );
        assertFalse( underTest.upcase() );

        final EffectivePredicate setter = new EffectivePredicate(
                MultiValueInterface.class.getMethod( "setLst", Collection.class ) );
        underTest.merge( setter );
        // now all values should be set
        assertEquals( ActionType.GETTER, underTest.actionType() );
        assertEquals( String.class, underTest.type() );
        assertEquals( Literal.class, underTest.internalType() );
        assertEquals( XSDDatatype.XSDstring, underTest.literalType() );
        assertEquals( "http://localhost/test#", underTest.namespace() );
        assertEquals( "lst", underTest.name() );
        assertTrue( underTest.postExec().isEmpty() );
        assertTrue( underTest.isCollection() );
        assertEquals( Collection.class, underTest.collectionType() );
        assertFalse( underTest.impl() );
        assertFalse( underTest.emptyIsNull() );
        assertFalse( underTest.upcase() );
    }

    @Test
    public void multiValueSetTest() throws Exception {
        underTest = new EffectivePredicate( MultiValueInterface.class.getMethod( "setLst", Collection.class ) );
        assertEquals( ActionType.SETTER, underTest.actionType() );
        assertEquals( String.class, underTest.type() );
        assertEquals( Literal.class, underTest.internalType() );
        assertEquals( XSDDatatype.XSDstring, underTest.literalType() );
        assertEquals( "http://localhost/test#", underTest.namespace() );
        assertEquals( "lst", underTest.name() );
        assertTrue( underTest.postExec().isEmpty() );
        assertTrue( underTest.isCollection() );
        assertEquals( Collection.class, underTest.collectionType() );
        assertFalse( underTest.impl() );
        assertFalse( underTest.emptyIsNull() );
        assertFalse( underTest.upcase() );
    }

    @Test
    public void multiValueHasTest() throws Exception {
        underTest = new EffectivePredicate( MultiValueInterface.class.getMethod( "hasLst", String.class ) );
        assertEquals( ActionType.EXISTENTIAL, underTest.actionType() );
        assertEquals( String.class, underTest.type() );
        assertEquals( Literal.class, underTest.internalType() );
        assertEquals( XSDDatatype.XSDstring, underTest.literalType() );
        assertEquals( "http://localhost/test#", underTest.namespace() );
        assertEquals( "lst", underTest.name() );
        assertTrue( underTest.postExec().isEmpty() );
        assertTrue( underTest.isCollection() );
        assertEquals( Predicate.UNSET.class, underTest.collectionType() );
        assertFalse( underTest.impl() );
        assertFalse( underTest.emptyIsNull() );
        assertFalse( underTest.upcase() );
    }

    @Test
    public void multiValueRemoveTest() throws Exception {
        underTest = new EffectivePredicate( MultiValueInterface.class.getMethod( "removeLst", String.class ) );
        assertEquals( ActionType.REMOVER, underTest.actionType() );
        assertEquals( String.class, underTest.type() );
        assertEquals( Literal.class, underTest.internalType() );
        assertEquals( XSDDatatype.XSDstring, underTest.literalType() );
        assertEquals( "http://localhost/test#", underTest.namespace() );
        assertEquals( "lst", underTest.name() );
        assertTrue( underTest.postExec().isEmpty() );
        assertTrue( underTest.isCollection() );
        assertEquals( Predicate.UNSET.class, underTest.collectionType() );
        assertFalse( underTest.impl() );
        assertFalse( underTest.emptyIsNull() );
        assertFalse( underTest.upcase() );
    }
}
