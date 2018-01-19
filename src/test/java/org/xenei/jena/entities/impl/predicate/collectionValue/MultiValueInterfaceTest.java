package org.xenei.jena.entities.impl.predicate.collectionValue;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.EffectivePredicate;
import org.xenei.jena.entities.impl.datatype.CharacterDatatype;
import org.xenei.jena.entities.impl.datatype.LongDatatype;
import org.xenei.jena.entities.testing.iface.MultiValueInterface;
import org.xenei.jena.entities.testing.iface.TestInterface;

public class MultiValueInterfaceTest extends AbstractCollectionValueInterfaceTest {

    public MultiValueInterfaceTest() throws NoSuchMethodException, SecurityException {
        super( MultiValueInterface.class );
    }

    protected MultiValueInterfaceTest(Class<?> underTest) throws NoSuchMethodException, SecurityException {
        super( underTest );
    }
    

    /*
     * order Predicate : Getter Predicate : Other
     * 
     * Class method order with same name.
     * 
     * @Predicate public abstract void addBool(Boolean b);
     * 
     * @Predicate public abstract void addChar(Character b);
     * 
     * @Predicate public abstract void addDbl(Double b);
     * 
     * @Predicate public abstract void addEnt(TestInterface b);
     * 
     * @Predicate public abstract void addFlt(Float b);
     * 
     * @Predicate public abstract void addInt(Integer b);
     * 
     * @Predicate public abstract void addLng(Long b);
     * 
     * @Predicate public abstract void addRDF(RDFNode b);
     * 
     * @Predicate public abstract void addStr(String b);
     * 
     * @Predicate public abstract void addU(@URI String b);
     * 
     * @Predicate public abstract void addU3(RDFNode b);
     * 
     * @Predicate(type = RDFNode.class) public abstract Set<RDFNode> getU();
     * 
     * @Predicate(type = URI.class, name = "u") public abstract List<String>
     * getU2();
     * 
     * @Predicate(type = RDFNode.class) public abstract Queue<RDFNode> getU3();
     * 
     * @Predicate(type = URI.class, name = "u3") public abstract Set<String>
     * getU4();
     * 
     * 
     */
    @Override
    public void processOrderTest() throws NoSuchMethodException, SecurityException {

        // bool
        EffectivePredicate base = new EffectivePredicate( interfaceClass.getMethod( "addBool", Boolean.class ) );
        builder.setType( Boolean.class ).setName( "bool" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDboolean );
                
        Method mthd = interfaceClass.getMethod( "getBool" );
        EffectivePredicate othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER ).setCollectionType( ExtendedIterator.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "hasBool", Boolean.class );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.EXISTENTIAL ).setCollectionType( Predicate.UNSET.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeBool", Boolean.class );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // char
        base = new EffectivePredicate( interfaceClass.getMethod( "addChar", Character.class ) );
        builder.setType( Character.class ).setName( "char" ).setLiteralType( CharacterDatatype.INSTANCE );

        mthd = interfaceClass.getMethod( "getChar" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER ).setCollectionType( ExtendedIterator.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "hasChar", Character.class  );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.EXISTENTIAL ).setCollectionType( Predicate.UNSET.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeChar", Character.class );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // dbl
        base = new EffectivePredicate( interfaceClass.getMethod( "addDbl", Double.class ) );
        builder.setType( Double.class ).setName( "dbl" ).setLiteralType( XSDDatatype.XSDdouble );

        mthd = interfaceClass.getMethod( "getDbl" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER ).setCollectionType( ExtendedIterator.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "hasDbl", Double.class );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.EXISTENTIAL ).setCollectionType( Predicate.UNSET.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeDbl", Double.class );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // ent
        base = new EffectivePredicate( interfaceClass.getMethod( "addEnt", TestInterface.class ) );
        builder.setType( TestInterface.class ).setName( "ent" ).setInternalType( RDFNode.class ).setLiteralType( null );

        mthd = interfaceClass.getMethod( "getEnt" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER ).setCollectionType( ExtendedIterator.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "hasEnt", TestInterface.class );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.EXISTENTIAL ).setCollectionType( Predicate.UNSET.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeEnt", TestInterface.class );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // flt
        base = new EffectivePredicate( interfaceClass.getMethod( "addFlt", Float.class ) );
        builder.setType( Float.class ).setName( "flt" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDfloat );

        mthd = interfaceClass.getMethod( "getFlt"  );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER ).setCollectionType( ExtendedIterator.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "hasFlt", Float.class  );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.EXISTENTIAL ).setCollectionType( Predicate.UNSET.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeFlt", Float.class  );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // int
        base = new EffectivePredicate( interfaceClass.getMethod( "addInt", Integer.class ) );
        builder.setType( Integer.class ).setName( "int" ).setLiteralType( XSDDatatype.XSDint );

        mthd = interfaceClass.getMethod( "getInt" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER ).setCollectionType( ExtendedIterator.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "hasInt", Integer.class );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.EXISTENTIAL ).setCollectionType( Predicate.UNSET.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeInt", Integer.class );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // lng
        base = new EffectivePredicate( interfaceClass.getMethod( "addLng", Long.class ) );
        builder.setType( Long.class ).setName( "lng" ).setLiteralType( LongDatatype.INSTANCE );

        mthd = interfaceClass.getMethod( "getLng" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER ).setCollectionType( ExtendedIterator.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "hasLng", Long.class );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.EXISTENTIAL ).setCollectionType( Predicate.UNSET.class );
        assertSame( builder, othr, mthd );
        
        mthd = interfaceClass.getMethod( "removeLng", Long.class );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // RDF
        base = new EffectivePredicate( interfaceClass.getMethod( "addRDF", RDFNode.class ) );
        builder.setType( RDFNode.class ).setName( "rDF" ).setInternalType( RDFNode.class ).setLiteralType( null );

        mthd = interfaceClass.getMethod( "getRDF" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER ).setCollectionType( ExtendedIterator.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "hasRDF", RDFNode.class );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.EXISTENTIAL ).setCollectionType( Predicate.UNSET.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeRDF", RDFNode.class );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // str
        base = new EffectivePredicate( interfaceClass.getMethod( "addStr", String.class ) );
        builder.setType( String.class ).setName( "str" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDstring );

        mthd = interfaceClass.getMethod( "getStr" );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER ).setCollectionType( ExtendedIterator.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "hasStr", String.class );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.EXISTENTIAL ).setCollectionType( Predicate.UNSET.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeStr", String.class );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // U
        base = new EffectivePredicate( interfaceClass.getMethod( "getU" ) );
        builder.setType( RDFNode.class ).setName( "u" ).setInternalType( RDFNode.class )
        .setLiteralType( null ).setCollectionType( ExtendedIterator.class );

        mthd = interfaceClass.getMethod( "addU", RDFNode.class );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.SETTER );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "hasU", RDFNode.class );
        othr = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.EXISTENTIAL );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeU", RDFNode.class );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, othr, mthd );

        // U2
        base = new EffectivePredicate( interfaceClass.getMethod( "getU2" ) );
        builder.setType( RDFNode.class ).setCollectionType( ExtendedIterator.class ).setName( "u2" );

    }


    @Override
    protected void updateGetBool() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetChar() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetU() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetU3() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetU4() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetDbl() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetEnt() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetFlt() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetInt() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetLng() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetRDF() {
        builder.setCollectionType( ExtendedIterator.class );
    }

    @Override
    protected void updateGetStr() {
        builder.setCollectionType( ExtendedIterator.class );
    }
}
