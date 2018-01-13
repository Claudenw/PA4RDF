package org.xenei.jena.entities.impl.predicate.simpleURI;

import org.apache.jena.rdf.model.RDFNode;
import org.junit.Test;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.predicate.AbstractPredicateTest;
import org.xenei.jena.entities.testing.iface.SimpleURIInterface;

public abstract class AbstractSimpleURIInterfaceTest extends AbstractPredicateTest {


    protected AbstractSimpleURIInterfaceTest(Class<? extends SimpleURIInterface> interfaceClass)
            throws NoSuchMethodException, SecurityException {
        super( interfaceClass );

        builder.setNamespace( "http://example.com/").setName(  "u" )
        .setInternalType( RDFNode.class );

    }

    @Test
    public void testParseGetter() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setType( RDFNode.class );
        updateGetter();
        assertSame( builder, interfaceClass.getMethod( "getU" ) );

    }

    protected void updateGetter() throws Exception {}

    @Test
    public void testParseSetterS() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setType( URI.class );
        updateSetterS();
        assertSame( builder, interfaceClass.getMethod( "setU", String.class ) );
    }

    protected void updateSetterS() throws Exception {}

    @Test
    public void testParseSetterR() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setType(  RDFNode.class ).setName( "u" );
        updateSetterR();
        assertSame( builder, interfaceClass.getMethod( "setU", RDFNode.class ) );
    }

    protected void updateSetterR() throws Exception {}


    @Test
    public void testParseExistential() throws Exception {
        builder.setActionType( ActionType.EXISTENTIAL )
        .setType( Predicate.UNSET.class );
        updateHas();
        assertSame( builder, interfaceClass.getMethod( "hasU" ) );

    }

    protected void updateHas() throws Exception {}


    @Test
    public void testParseRemover() throws Exception {
        builder.setActionType( ActionType.REMOVER )
        .setType( Predicate.UNSET.class );
        updateRemover();
        assertSame( builder, interfaceClass.getMethod( "removeU" ) );;
    }

    protected void updateRemover() throws Exception {}


}
