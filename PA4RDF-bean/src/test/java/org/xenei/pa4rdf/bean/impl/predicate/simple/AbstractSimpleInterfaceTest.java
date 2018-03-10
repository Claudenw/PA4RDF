package org.xenei.pa4rdf.bean.impl.predicate.simple;

import java.lang.reflect.Method;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Test;
import org.xenei.pa4rdf.bean.test.iface.SimpleInterface;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.impl.ActionType;
import org.xenei.pa4rdf.bean.impl.EffectivePredicate;
import org.xenei.pa4rdf.bean.impl.predicate.AbstractPredicateTest;

public abstract class AbstractSimpleInterfaceTest extends AbstractPredicateTest {

    protected AbstractSimpleInterfaceTest(Class<?> interfaceClass)
            throws NoSuchMethodException, SecurityException {
        super( interfaceClass );

        builder.setNamespace( "http://example.com/" ).setName( "x" );

    }

    /*
     * order Predicate : Getter Predicate : Other
     * 
     * Class method order with same name.
     * 
     * 
     */
    public void processOrderTest() throws NoSuchMethodException, SecurityException {
        EffectivePredicate getX = new EffectivePredicate( interfaceClass.getMethod( "getX" ) );
        builder.setInternalType( Literal.class ).setLiteralType( XSDDatatype.XSDstring ).setType( String.class );

        Method mthd = interfaceClass.getMethod( "setX", String.class );
        EffectivePredicate setX = new EffectivePredicate( mthd ).merge( getX );
        builder.setActionType( ActionType.SETTER );
        assertSame( builder, setX, mthd );

        mthd = interfaceClass.getMethod( "hasX" );
        builder.setActionType( ActionType.EXISTENTIAL ).setType(  Predicate.UNSET.class );
        EffectivePredicate hasX = new EffectivePredicate( mthd ).merge( getX );
        assertSame( builder, hasX, mthd );

        mthd = interfaceClass.getMethod( "removeX" );
        builder.setActionType( ActionType.REMOVER );
        EffectivePredicate removeX = new EffectivePredicate( mthd ).merge( getX );
        assertSame( builder, removeX, mthd );

    }

    @Test
    public final void testParseGetter() throws Exception {
        builder.setActionType( ActionType.GETTER ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDstring ).setType( String.class );
        updateGetter();
        assertSame( builder, interfaceClass.getMethod( "getX" ) );
    }

    protected void updateGetter() throws Exception {
    }

    @Test
    public final void testParseSetter() throws Exception {
        builder.setActionType( ActionType.SETTER ).setInternalType( Literal.class )
                .setLiteralType( XSDDatatype.XSDstring ).setType( String.class );
        updateSetter();
        assertSame( builder, interfaceClass.getMethod( "setX", String.class ) );
    }

    protected void updateSetter() throws Exception {
    }

    @Test
    public final void testParseExistential() throws Exception {
        builder.setActionType( ActionType.EXISTENTIAL ).setInternalType( RDFNode.class )
                .setType( Predicate.UNSET.class );
        updateExistential();
        assertSame( builder, interfaceClass.getMethod( "hasX" ) );
    }

    protected void updateExistential() throws Exception {
    }

    @Test
    public final void testParseRemover() throws Exception {
        builder.setActionType( ActionType.REMOVER ).setInternalType( RDFNode.class ).setType( Predicate.UNSET.class );
        updateRemover();
        assertSame( builder, interfaceClass.getMethod( "removeX" ) );
    }

    protected void updateRemover() throws Exception {
    }
}
