package org.xenei.jena.entities.impl.predicate.singleValue;

import java.lang.reflect.Method;

import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.ObjectHandler;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.datatype.CharacterDatatype;
import org.xenei.jena.entities.impl.datatype.LongDatatype;
import org.xenei.jena.entities.impl.handlers.EntityHandler;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.impl.handlers.VoidHandler;
import org.xenei.jena.entities.impl.predicate.AbstractPredicateTest;
import org.xenei.jena.entities.impl.predicate.REVIEW.REVIEW_BaseAbstractParserTest;
import org.xenei.jena.entities.testing.iface.SingleValueObjectInterface;
import org.xenei.jena.entities.testing.iface.TestInterface;

public abstract class AbstractSingleValueObjectTest extends AbstractPredicateTest {

    
    protected AbstractSingleValueObjectTest(final Class<?> classUnderTest) {
        super( classUnderTest );
        builder.setNamespace( "http://localhost/test#" );        
    }

    @Test
    public void testIsBool() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setName(  "bool" )
        .setInternalType( Literal.class )
        .setLiteralType( XSDDatatype.XSDboolean )
        .setType( Boolean.class );
        assertSame( builder, interfaceClass.getMethod( "isBool" ) );
    }
    
    @Test
    public void testSetBool() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setName(  "bool" )
        .setInternalType( Literal.class )
        .setLiteralType( XSDDatatype.XSDboolean )
        .setType( Boolean.class );
        assertSame( builder, interfaceClass.getMethod( "setBool", Boolean.class ) );
    }
    
    @Test
    public void testRemoveBool() throws Exception {
        builder.setActionType( ActionType.REMOVER )       
        .setName(  "bool" )
        .setType(  Predicate.UNSET.class )
        .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeBool" ) );
    }

    @Test
    public void testGetChar() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setName(  "char" )
        .setInternalType( Literal.class )
        .setLiteralType( CharacterDatatype.INSTANCE  )
        .setType( Character.class );
        assertSame( builder, interfaceClass.getMethod( "getChar" ) );
    }
    
    @Test
    public void testSetChar() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setName(  "char" )
        .setInternalType( Literal.class )
        .setLiteralType( CharacterDatatype.INSTANCE  )
        .setType( Character.class );
        assertSame( builder, interfaceClass.getMethod( "setChar", Character.class ) );
    }
    
    @Test
    public void testRemoveChar() throws Exception {
        builder.setActionType( ActionType.REMOVER )       
        .setName(  "char" )
        .setType(  Predicate.UNSET.class )
        .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeChar" ) );
    }

    @Test
    public void testGetDbl() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setName(  "dbl" )
        .setInternalType( Literal.class )
        .setLiteralType( XSDDatatype.XSDdouble )
        .setType( Double.class );
        assertSame( builder, interfaceClass.getMethod( "getDbl" ) );
    }
    
    @Test
    public void testSetDbl() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setName(  "dbl" )
        .setInternalType( Literal.class )
        .setLiteralType( XSDDatatype.XSDdouble  )
        .setType( Double.class );
        assertSame( builder, interfaceClass.getMethod( "setDbl", Double.class ) );
    }
    
    @Test
    public void testRemoveDbl() throws Exception {
        builder.setActionType( ActionType.REMOVER )       
        .setName(  "dbl" )
        .setType(  Predicate.UNSET.class )
        .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeDbl" ) );
    }

    @Test
    public void testGetEnt() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setName(  "ent" )
        .setInternalType( RDFNode.class )
        .setType( TestInterface.class );
        assertSame( builder, interfaceClass.getMethod( "getEnt" ) );
    }
    
    @Test
    public void testSetEnt() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setName(  "ent" )
        .setInternalType( RDFNode.class )
        .setType( TestInterface.class );
        assertSame( builder, interfaceClass.getMethod( "setEnt", TestInterface.class ) );
    }
    
    @Test
    public void testRemoveEnt() throws Exception {
        builder.setActionType( ActionType.REMOVER )       
        .setName(  "ent" )
        .setType(  Predicate.UNSET.class )
        .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeEnt" ) );
    }

    @Test
    public void testGetFlt() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setName(  "flt" )
        .setInternalType( Literal.class )
        .setLiteralType( XSDDatatype.XSDfloat )
        .setType( Float.class );
        assertSame( builder, interfaceClass.getMethod( "getFlt" ) );
    }
    
    @Test
    public void testSetFlt() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setName(  "flt" )
        .setInternalType( Literal.class )
        .setLiteralType( XSDDatatype.XSDfloat  )
        .setType( Float.class );
        assertSame( builder, interfaceClass.getMethod( "setFlt", Float.class ) );
    }
    
    @Test
    public void testRemoveFlt() throws Exception {
        builder.setActionType( ActionType.REMOVER )       
        .setName(  "flt" )
        .setType(  Predicate.UNSET.class )
        .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeFlt" ) );
    }
    
    @Test
    public void testGetInt() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setName(  "int" )
        .setInternalType( Literal.class )
        .setLiteralType( XSDDatatype.XSDint )
        .setType( Integer.class );
        assertSame( builder, interfaceClass.getMethod( "getInt" ) );
    }
    
    @Test
    public void testSetInt() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setName(  "int" )
        .setInternalType( Literal.class )
        .setLiteralType( XSDDatatype.XSDint  )
        .setType( Integer.class );
        assertSame( builder, interfaceClass.getMethod( "setInt", Integer.class ) );
    }
    
    @Test
    public void testRemoveInt() throws Exception {
        builder.setActionType( ActionType.REMOVER )       
        .setName(  "int" )
        .setType(  Predicate.UNSET.class )
        .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeInt" ) );
    }

    @Test
    public void testGetLng() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setName(  "lng" )
        .setInternalType( Literal.class )
        .setLiteralType( LongDatatype.INSTANCE )
        .setType( Long.class );
        assertSame( builder, interfaceClass.getMethod( "getLng" ) );
    }
    
    @Test
    public void testSetLng() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setName(  "lng" )
        .setInternalType( Literal.class )
        .setLiteralType( LongDatatype.INSTANCE  )
        .setType( Long.class );
        assertSame( builder, interfaceClass.getMethod( "setLng", Long.class ) );
    }
    
    @Test
    public void testRemoveLng() throws Exception {
        builder.setActionType( ActionType.REMOVER )       
        .setName(  "lng" )
        .setType(  Predicate.UNSET.class )
        .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeLng" ) );
    }
 
    @Test
    public void testGetRDF() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setName(  "rDF" )
        .setInternalType( RDFNode.class )
        .setType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "getRDF" ) );
    }
    
    @Test
    public void testSetRDF() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setName(  "rDF" )
        .setInternalType( RDFNode.class )
        .setType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "setRDF", RDFNode.class ) );
    }
    
    @Test
    public void testRemoveRDF() throws Exception {
        builder.setActionType( ActionType.REMOVER )       
        .setName(  "rDF" )
        .setType(  Predicate.UNSET.class )
        .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeRDF" ) );
    }
    
    @Test
    public void testGetStr() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setName(  "str" )
        .setInternalType( Literal.class )
        .setLiteralType( XSDDatatype.XSDstring )
        .setType( String.class );
        assertSame( builder, interfaceClass.getMethod( "getStr" ) );
    }
    
    @Test
    public void testSetStr() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setName(  "str" )
        .setInternalType( Literal.class )
        .setLiteralType( XSDDatatype.XSDstring  )
        .setType( String.class );
        assertSame( builder, interfaceClass.getMethod( "setStr", String.class ) );
    }
    
    @Test
    public void testRemoveStr() throws Exception {
        builder.setActionType( ActionType.REMOVER )       
        .setName(  "str" )
        .setType(  Predicate.UNSET.class )
        .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeStr" ) );
    }

    @Test
    public void testGetU() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setName(  "u" )
        .setInternalType( RDFNode.class )
        .setType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "getU" ) );
    }
    
    @Test
    public void testSetU_R() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setName(  "u" )
        .setInternalType( RDFNode.class )
        .setType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "setU", RDFNode.class ) );
    }
    
    @Test
    public void testSetU_S() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setName(  "u" )
        .setInternalType( RDFNode.class )
        .setType( URI.class );
        assertSame( builder, interfaceClass.getMethod( "setU", String.class ) );
    }
    @Test
    public void testRemoveU() throws Exception {
        builder.setActionType( ActionType.REMOVER )       
        .setName(  "u" )
        .setType(  Predicate.UNSET.class )
        .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeU" ) );
    }
    
    @Test
    public void testGetU2() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setName(  "u" )
        .setInternalType( RDFNode.class )
        .setType( URI.class );
        updateGetU2();
        assertSame( builder, interfaceClass.getMethod( "getU2" ) );
    }
    
    protected void updateGetU2() {}
}

