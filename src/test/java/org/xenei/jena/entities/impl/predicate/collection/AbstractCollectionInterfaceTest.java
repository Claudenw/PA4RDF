package org.xenei.jena.entities.impl.predicate.collection;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.junit.Test;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.EffectivePredicate;
import org.xenei.jena.entities.impl.predicate.AbstractPredicateTest;

public abstract class AbstractCollectionInterfaceTest extends AbstractPredicateTest {

    protected AbstractCollectionInterfaceTest(final Class<?> interfaceClass)
            throws NoSuchMethodException, SecurityException {
        super( interfaceClass );
        builder.setNamespace( "http://example.com/" ).setName( "x" ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDstring );
    }

    /*
     * order Predicate : Getter Predicate : Other
     * 
     * Class method order with same name.
     * 
     * @Predicate void addX(String x);
     */
    public void processOrderTest() throws NoSuchMethodException, SecurityException {
        EffectivePredicate base = new EffectivePredicate( interfaceClass.getMethod( "addX", String.class ) );
        builder.setInternalType( Literal.class ).setLiteralType( XSDDatatype.XSDstring )
        .setType( String.class );

        Method mthd = interfaceClass.getMethod( "getX" );
        EffectivePredicate setX = new EffectivePredicate( mthd ).merge( base );
        builder.setActionType( ActionType.GETTER ).setCollectionType( List.class );
        assertSame( builder, setX, mthd );

        mthd = interfaceClass.getMethod( "hasX", String.class );
        builder.setActionType( ActionType.EXISTENTIAL ).setCollectionType(  Predicate.UNSET.class );
        EffectivePredicate hasX = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, hasX, mthd );

        mthd = interfaceClass.getMethod( "removeX", String.class );
        builder.setActionType( ActionType.REMOVER );
        EffectivePredicate removeX = new EffectivePredicate( mthd ).merge( base );
        assertSame( builder, removeX, mthd );

    }

    @Test
    public void testParseGetter() throws Exception {
        builder.setActionType( ActionType.GETTER ).setCollectionType( List.class ).setInternalType( null )
                .setLiteralType( null );
        updateGetter();
        assertSame( builder, interfaceClass.getMethod( "getX" ) );

    }

    protected void updateGetter() {
    }

    @Test
    public void testParseSetter() throws Exception {
        builder.setActionType( ActionType.SETTER ).setLiteralType( XSDDatatype.XSDstring ).setType( String.class )
        .setCollectionType(  Predicate.UNSET.class );
        updateSetter();
        assertSame( builder, interfaceClass.getMethod( "addX", String.class ) );
    }

    protected void updateSetter() {
    }

    @Test
    public void testParseExistential() throws Exception {
        builder.setActionType( ActionType.EXISTENTIAL ).setType( String.class )
        .setCollectionType(  Predicate.UNSET.class );       
        updateExistential();
        assertSame( builder, interfaceClass.getMethod( "hasX", String.class ) );
    }

    protected void updateExistential() {
    }

    @Test
    public void testParseRemover() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setType( String.class )
        .setCollectionType(  Predicate.UNSET.class );   
        updateRemover();
        assertSame( builder, interfaceClass.getMethod( "removeX", String.class ) );
    }

    protected void updateRemover() {
    }

}
