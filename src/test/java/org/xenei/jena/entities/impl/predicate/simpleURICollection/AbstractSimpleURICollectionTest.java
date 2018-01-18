package org.xenei.jena.entities.impl.predicate.simpleURICollection;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.junit.Test;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.EffectivePredicate;
import org.xenei.jena.entities.impl.predicate.AbstractPredicateTest;
import org.xenei.jena.entities.testing.iface.SimpleURICollectionInterface;

public abstract class AbstractSimpleURICollectionTest extends AbstractPredicateTest {

    protected AbstractSimpleURICollectionTest(Class<?> interfaceClass)
            throws NoSuchMethodException, SecurityException {
        super( interfaceClass );
        builder.setNamespace( "http://example.com/" ).setInternalType( RDFNode.class );

    }
    
    /*
     * order Predicate : Getter Predicate : Other
     * 
     * Class method order with same name.
     * 
     * 
     */
    public void processOrderTest() throws NoSuchMethodException, SecurityException {

        EffectivePredicate getU = new EffectivePredicate( interfaceClass.getMethod( "getU" ) );
        builder.setType( RDFNode.class ).setCollectionType( List.class ).setName( "u" );

        Method mthd = interfaceClass.getMethod( "addU", RDFNode.class );
        EffectivePredicate othr = new EffectivePredicate( mthd ).merge( getU );
        builder.setActionType( ActionType.SETTER );
        assertSame( builder, othr, mthd );    

        mthd = interfaceClass.getMethod( "addU", String.class );
        othr = new EffectivePredicate( mthd ).merge( getU );
        builder.setActionType( ActionType.SETTER ).setType(  URI.class );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "hasU", String.class );
        builder.setActionType( ActionType.EXISTENTIAL );
        othr = new EffectivePredicate( mthd ).merge( getU );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "removeU", String.class );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( getU );
        assertSame( builder, othr, mthd );
        builder.setType( RDFNode.class );
        
        // U2 Checks        
        EffectivePredicate getU2 = new EffectivePredicate( interfaceClass.getMethod( "getU2" ) );
        builder.setType( RDFNode.class ).setCollectionType( ExtendedIterator.class ).setName( "u2" );
        
        mthd = interfaceClass.getMethod( "addU2", String.class );
        othr = new EffectivePredicate( mthd ).merge( getU2 );
        builder.setActionType( ActionType.SETTER ).setType( URI.class );
        assertSame( builder, othr, mthd );
        builder.setType(  RDFNode.class );

        mthd = interfaceClass.getMethod( "addU2", RDFNode.class );
        othr = new EffectivePredicate( mthd ).merge( getU2 );
        builder.setActionType( ActionType.SETTER );
        assertSame( builder, othr, mthd );

        mthd = interfaceClass.getMethod( "hasU2", String.class );
        builder.setActionType( ActionType.EXISTENTIAL ).setType( URI.class );;
        othr = new EffectivePredicate( mthd ).merge( getU2 );
        assertSame( builder, othr, mthd );
        
        mthd = interfaceClass.getMethod( "removeU2", String.class );
        builder.setActionType( ActionType.REMOVER );
        othr = new EffectivePredicate( mthd ).merge( getU2 );
        assertSame( builder, othr, mthd );
        
        mthd = interfaceClass.getMethod( "removeU2", RDFNode.class );
        builder.setActionType( ActionType.REMOVER ).setType( RDFNode.class );;
        othr = new EffectivePredicate( mthd ).merge( getU2 );
        assertSame( builder, othr, mthd );
        
    }

    @Test
    public void testParseGetter() throws Exception {
        builder.setActionType( ActionType.GETTER ).setName( "u" ).setCollectionType( List.class )
                .setType( RDFNode.class );
        updateGetter();
        assertSame( builder, interfaceClass.getMethod( "getU" ) );

    }

    protected void updateGetter() throws Exception {
    }

    @Test
    public void testParseSetterS() throws Exception {
        builder.setActionType( ActionType.SETTER ).setName( "u" ).setCollectionType( Predicate.UNSET.class ).setType( URI.class );
        updateSetterS();
        assertSame( builder, interfaceClass.getMethod( "addU", String.class ) );
    }

    protected void updateSetterS() throws Exception {
    }

    @Test
    public void testParseSetterR() throws Exception {
        builder.setActionType( ActionType.SETTER ).setName( "u" ).setCollectionType( Predicate.UNSET.class ).setType( RDFNode.class );
        updateSetterR();
        assertSame( builder, interfaceClass.getMethod( "addU", RDFNode.class ) );
    }

    protected void updateSetterR() throws Exception {
    }

    @Test
    public void testParseExistential() throws Exception {
        builder.setActionType( ActionType.EXISTENTIAL ).setName( "u" ).setCollectionType( Predicate.UNSET.class ).setType( URI.class );
        updateExtential();
        assertSame( builder, interfaceClass.getMethod( "hasU", String.class ) );
    }

    protected void updateExtential() throws Exception {
    }

    @Test
    public void testParseRemover() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setName( "u" ).setCollectionType( Predicate.UNSET.class ).setType( URI.class );
        updateRemover();
        assertSame( builder, interfaceClass.getMethod( "removeU", String.class ) );
    }

    protected void updateRemover() throws Exception {
    }

    @Test
    public void testParseGetter2() throws Exception {
        builder.setActionType( ActionType.GETTER ).setCollectionType( ExtendedIterator.class ).setType( RDFNode.class )
                .setName( "u2" );
        updateGetter2();
        assertSame( builder, interfaceClass.getMethod( "getU2" ) );

    }

    protected void updateGetter2() throws Exception {
    }

    @Test
    public void testParseSetterR2() throws Exception {
        builder.setActionType( ActionType.SETTER ).setType( RDFNode.class )
        .setCollectionType( Predicate.UNSET.class ).setName( "u2" );
        updateSetterR();
        assertSame( builder, interfaceClass.getMethod( "addU2", RDFNode.class ) );
    }

    protected void updateSetterR2() throws Exception {
    }

    @Test
    public void testParseSetterS2() throws Exception {
        builder.setActionType( ActionType.SETTER ).setType( URI.class ).setCollectionType( Predicate.UNSET.class ).setName( "u2" );
        updateSetterS2();
        assertSame( builder, interfaceClass.getMethod( "addU2", String.class ) );
    }

    protected void updateSetterS2() throws Exception {
    }

    @Test
    public void testParseExistential2() throws Exception {
        builder.setActionType( ActionType.EXISTENTIAL ).setType( URI.class ).setCollectionType( Predicate.UNSET.class ).setName( "u2" );
        updateExtential2();
        assertSame( builder, interfaceClass.getMethod( "hasU2", String.class ) );
    }

    protected void updateExtential2() throws Exception {
    }

    @Test
    public void testParseRemoverR2() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setType( RDFNode.class ).setCollectionType( Predicate.UNSET.class ).setName( "u2" );
        updateRemoverR2();
        assertSame( builder, interfaceClass.getMethod( "removeU2", RDFNode.class ) );
    }

    protected void updateRemoverR2() throws Exception {
    }

    @Test
    public void testParseRemoverS2() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setType( URI.class ).setCollectionType( Predicate.UNSET.class ).setName( "u2" );
        updateRemoverS2();
        assertSame( builder, interfaceClass.getMethod( "removeU2", String.class ) );
    }

    protected void updateRemoverS2() throws Exception {
    }

}
