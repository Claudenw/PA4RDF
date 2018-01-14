package org.xenei.jena.entities.impl.predicate.singleValue;

import java.lang.reflect.Method;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.junit.Test;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.EffectivePredicate;
import org.xenei.jena.entities.impl.datatype.CharacterDatatype;
import org.xenei.jena.entities.impl.datatype.LongDatatype;
import org.xenei.jena.entities.impl.predicate.AbstractPredicateTest;
import org.xenei.jena.entities.testing.iface.TestInterface;

public abstract class AbstractSingleValueObjectTest extends AbstractPredicateTest {

    protected AbstractSingleValueObjectTest(final Class<?> classUnderTest) {
        super( classUnderTest );
        builder.setNamespace( "http://localhost/test#" );
    }

    /*
     * order Predicate : Getter Predicate : Other
     * 
     * Class method order with same name.
     * 
     * 
     * 
     * 
     * 
     * @Predicate void setBool(Boolean b);
     * 
     * @Predicate void setChar(Character b);
     * 
     * @Predicate void setDbl(Double b);
     * 
     * @Predicate void setEnt(TestInterface b);
     * 
     * @Predicate void setFlt(Float b);
     * 
     * @Predicate void setInt(Integer b);
     * 
     * @Predicate void setLng(Long b);
     * 
     * @Predicate void setRDF(RDFNode b);
     * 
     * @Predicate void setStr(String b);
     * 
     * @Predicate void setSubPredicate(SubPredicate subPredicate);
     * 
     * @Predicate void setU(@URI String b);
     * 
     * @Predicate(type = URI.class, name = "u") String getU2();
     * 
     * 
     */
    @Override
    public void processOrderTest() throws NoSuchMethodException, SecurityException {

        // bool
        EffectivePredicate base = new EffectivePredicate( interfaceClass.getMethod( "setBool", Boolean.class ) );
        builder.setType( Boolean.class ).setName( "bool" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDboolean );

        Method mthd = interfaceClass.getMethod( "isBool" );
        EffectivePredicate othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeBool" );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // char
        base = new EffectivePredicate( interfaceClass.getMethod( "setChar", Character.class ) );
        builder.setType( Character.class ).setName( "char" ).setLiteralType( CharacterDatatype.INSTANCE );

        mthd = interfaceClass.getMethod( "getChar" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeChar" );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // dbl
        base = new EffectivePredicate( interfaceClass.getMethod( "setDbl", Double.class ) );
        builder.setType( Double.class ).setName( "dbl" ).setLiteralType( XSDDatatype.XSDdouble );

        mthd = interfaceClass.getMethod( "getDbl" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeDbl" );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // ent
        base = new EffectivePredicate( interfaceClass.getMethod( "setEnt", TestInterface.class ) );
        builder.setType( TestInterface.class ).setName( "ent" ).setInternalType( RDFNode.class ).setLiteralType( null );

        mthd = interfaceClass.getMethod( "getEnt" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeEnt" );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // flt
        base = new EffectivePredicate( interfaceClass.getMethod( "setFlt", Float.class ) );
        builder.setType( Float.class ).setName( "flt" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDfloat );
        ;

        mthd = interfaceClass.getMethod( "getFlt" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeFlt" );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // int
        base = new EffectivePredicate( interfaceClass.getMethod( "setInt", Integer.class ) );
        builder.setType( Integer.class ).setName( "int" ).setLiteralType( XSDDatatype.XSDint );
        ;

        mthd = interfaceClass.getMethod( "getInt" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeInt" );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // lng
        base = new EffectivePredicate( interfaceClass.getMethod( "setLng", Long.class ) );
        builder.setType( Long.class ).setName( "lng" ).setLiteralType( LongDatatype.INSTANCE );

        mthd = interfaceClass.getMethod( "getLng" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeLng" );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // RDF
        base = new EffectivePredicate( interfaceClass.getMethod( "setRDF", RDFNode.class ) );
        builder.setType( RDFNode.class ).setName( "rDF" ).setInternalType( RDFNode.class ).setLiteralType( null );

        mthd = interfaceClass.getMethod( "getRDF" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeRDF" );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // str
        base = new EffectivePredicate( interfaceClass.getMethod( "setStr", String.class ) );
        builder.setType( String.class ).setName( "str" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDstring );

        mthd = interfaceClass.getMethod( "getStr" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeStr" );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // U
        base = new EffectivePredicate( interfaceClass.getMethod( "getU" ) );
        builder.setType( RDFNode.class ).setName( "u" ).setInternalType( RDFNode.class ).setLiteralType( null );

        mthd = interfaceClass.getMethod( "setU", RDFNode.class );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.SETTER );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeU" );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // U2
        base = new EffectivePredicate( interfaceClass.getMethod( "getU2" ) );
        builder.setType( RDFNode.class ).setCollectionType( ExtendedIterator.class ).setName( "u2" );

    }

    @Test
    public void testIsBool() throws Exception {
        builder.setActionType( ActionType.GETTER ).setName( "bool" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDboolean ).setType( Boolean.class );
        assertSame( builder, interfaceClass.getMethod( "isBool" ) );
    }

    @Test
    public void testSetBool() throws Exception {
        builder.setActionType( ActionType.SETTER ).setName( "bool" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDboolean ).setType( Boolean.class );
        assertSame( builder, interfaceClass.getMethod( "setBool", Boolean.class ) );
    }

    @Test
    public void testRemoveBool() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setName( "bool" ).setType( Predicate.UNSET.class )
                .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeBool" ) );
    }

    @Test
    public void testGetChar() throws Exception {
        builder.setActionType( ActionType.GETTER ).setName( "char" ).setInternalType( Literal.class )
                .setLiteralType( CharacterDatatype.INSTANCE ).setType( Character.class );
        assertSame( builder, interfaceClass.getMethod( "getChar" ) );
    }

    @Test
    public void testSetChar() throws Exception {
        builder.setActionType( ActionType.SETTER ).setName( "char" ).setInternalType( Literal.class )
                .setLiteralType( CharacterDatatype.INSTANCE ).setType( Character.class );
        assertSame( builder, interfaceClass.getMethod( "setChar", Character.class ) );
    }

    @Test
    public void testRemoveChar() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setName( "char" ).setType( Predicate.UNSET.class )
                .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeChar" ) );
    }

    @Test
    public void testGetDbl() throws Exception {
        builder.setActionType( ActionType.GETTER ).setName( "dbl" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDdouble ).setType( Double.class );
        assertSame( builder, interfaceClass.getMethod( "getDbl" ) );
    }

    @Test
    public void testSetDbl() throws Exception {
        builder.setActionType( ActionType.SETTER ).setName( "dbl" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDdouble ).setType( Double.class );
        assertSame( builder, interfaceClass.getMethod( "setDbl", Double.class ) );
    }

    @Test
    public void testRemoveDbl() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setName( "dbl" ).setType( Predicate.UNSET.class )
                .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeDbl" ) );
    }

    @Test
    public void testGetEnt() throws Exception {
        builder.setActionType( ActionType.GETTER ).setName( "ent" ).setInternalType( RDFNode.class )
                .setType( TestInterface.class );
        assertSame( builder, interfaceClass.getMethod( "getEnt" ) );
    }

    @Test
    public void testSetEnt() throws Exception {
        builder.setActionType( ActionType.SETTER ).setName( "ent" ).setInternalType( RDFNode.class )
                .setType( TestInterface.class );
        assertSame( builder, interfaceClass.getMethod( "setEnt", TestInterface.class ) );
    }

    @Test
    public void testRemoveEnt() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setName( "ent" ).setType( Predicate.UNSET.class )
                .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeEnt" ) );
    }

    @Test
    public void testGetFlt() throws Exception {
        builder.setActionType( ActionType.GETTER ).setName( "flt" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDfloat ).setType( Float.class );
        assertSame( builder, interfaceClass.getMethod( "getFlt" ) );
    }

    @Test
    public void testSetFlt() throws Exception {
        builder.setActionType( ActionType.SETTER ).setName( "flt" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDfloat ).setType( Float.class );
        assertSame( builder, interfaceClass.getMethod( "setFlt", Float.class ) );
    }

    @Test
    public void testRemoveFlt() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setName( "flt" ).setType( Predicate.UNSET.class )
                .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeFlt" ) );
    }

    @Test
    public void testGetInt() throws Exception {
        builder.setActionType( ActionType.GETTER ).setName( "int" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDint ).setType( Integer.class );
        assertSame( builder, interfaceClass.getMethod( "getInt" ) );
    }

    @Test
    public void testSetInt() throws Exception {
        builder.setActionType( ActionType.SETTER ).setName( "int" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDint ).setType( Integer.class );
        assertSame( builder, interfaceClass.getMethod( "setInt", Integer.class ) );
    }

    @Test
    public void testRemoveInt() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setName( "int" ).setType( Predicate.UNSET.class )
                .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeInt" ) );
    }

    @Test
    public void testGetLng() throws Exception {
        builder.setActionType( ActionType.GETTER ).setName( "lng" ).setInternalType( Literal.class )
                .setLiteralType( LongDatatype.INSTANCE ).setType( Long.class );
        assertSame( builder, interfaceClass.getMethod( "getLng" ) );
    }

    @Test
    public void testSetLng() throws Exception {
        builder.setActionType( ActionType.SETTER ).setName( "lng" ).setInternalType( Literal.class )
                .setLiteralType( LongDatatype.INSTANCE ).setType( Long.class );
        assertSame( builder, interfaceClass.getMethod( "setLng", Long.class ) );
    }

    @Test
    public void testRemoveLng() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setName( "lng" ).setType( Predicate.UNSET.class )
                .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeLng" ) );
    }

    @Test
    public void testGetRDF() throws Exception {
        builder.setActionType( ActionType.GETTER ).setName( "rDF" ).setInternalType( RDFNode.class )
                .setType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "getRDF" ) );
    }

    @Test
    public void testSetRDF() throws Exception {
        builder.setActionType( ActionType.SETTER ).setName( "rDF" ).setInternalType( RDFNode.class )
                .setType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "setRDF", RDFNode.class ) );
    }

    @Test
    public void testRemoveRDF() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setName( "rDF" ).setType( Predicate.UNSET.class )
                .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeRDF" ) );
    }

    @Test
    public void testGetStr() throws Exception {
        builder.setActionType( ActionType.GETTER ).setName( "str" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDstring ).setType( String.class );
        assertSame( builder, interfaceClass.getMethod( "getStr" ) );
    }

    @Test
    public void testSetStr() throws Exception {
        builder.setActionType( ActionType.SETTER ).setName( "str" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDstring ).setType( String.class );
        assertSame( builder, interfaceClass.getMethod( "setStr", String.class ) );
    }

    @Test
    public void testRemoveStr() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setName( "str" ).setType( Predicate.UNSET.class )
                .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeStr" ) );
    }

    @Test
    public void testGetU() throws Exception {
        builder.setActionType( ActionType.GETTER ).setName( "u" ).setInternalType( RDFNode.class )
                .setType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "getU" ) );
    }

    @Test
    public void testSetU_R() throws Exception {
        builder.setActionType( ActionType.SETTER ).setName( "u" ).setInternalType( RDFNode.class )
                .setType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "setU", RDFNode.class ) );
    }

    @Test
    public void testSetU_S() throws Exception {
        builder.setActionType( ActionType.SETTER ).setName( "u" ).setInternalType( RDFNode.class ).setType( URI.class );
        assertSame( builder, interfaceClass.getMethod( "setU", String.class ) );
    }

    @Test
    public void testRemoveU() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setName( "u" ).setType( Predicate.UNSET.class )
                .setInternalType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeU" ) );
    }

    @Test
    public void testGetU2() throws Exception {
        builder.setActionType( ActionType.GETTER ).setName( "u" ).setInternalType( RDFNode.class ).setType( URI.class );
        updateGetU2();
        assertSame( builder, interfaceClass.getMethod( "getU2" ) );
    }

    protected void updateGetU2() {
    }
}
