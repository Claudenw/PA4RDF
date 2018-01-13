package org.xenei.jena.entities.impl.predicate.simple;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Test;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.predicate.AbstractPredicateTest;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public abstract class AbstractSimpleInterfaceTest extends AbstractPredicateTest {

    protected AbstractSimpleInterfaceTest(Class<? extends SimpleInterface> interfaceClass)
            throws NoSuchMethodException, SecurityException {
        super( interfaceClass );

        builder.setNamespace( "http://example.com/").setName(  "x" );


    }

    @Test
    public final void testParseGetter() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setInternalType(  Literal.class )
        .setLiteralType(  XSDDatatype.XSDstring )
        .setType(  String.class );
        updateGetter();
        assertSame( builder, interfaceClass.getMethod( "getX" ) );
    }

    protected void updateGetter() throws Exception {}
    
    @Test
    public final void testParseSetter() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setInternalType(  Literal.class )
        .setLiteralType(  XSDDatatype.XSDstring )
        .setType(  String.class );
        updateSetter();
        assertSame( builder, interfaceClass.getMethod( "setX", String.class ) );
    }
    
    protected void updateSetter() throws Exception {}

    @Test
    public final void testParseExistential() throws Exception {
        builder.setActionType( ActionType.EXISTENTIAL )
        .setInternalType(  RDFNode.class )
        .setType(  Predicate.UNSET.class );
        updateExistential();
        assertSame( builder, interfaceClass.getMethod( "hasX" ) );
    }

    protected void updateExistential() throws Exception {}
    
    @Test
    public final void testParseRemover() throws Exception {
        builder.setActionType( ActionType.REMOVER )
        .setInternalType(  RDFNode.class )
        .setType(  Predicate.UNSET.class );
        updateRemover();
        assertSame( builder, interfaceClass.getMethod( "removeX" ) );
    }

    protected void updateRemover() throws Exception {}
}
