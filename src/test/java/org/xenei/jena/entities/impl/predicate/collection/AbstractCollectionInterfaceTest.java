package org.xenei.jena.entities.impl.predicate.collection;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.jena.entities.impl.TypeChecker;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.VoidHandler;
import org.xenei.jena.entities.impl.predicate.AbstractPredicateTest;
import org.xenei.jena.entities.testing.iface.CollectionInterface;

public abstract class AbstractCollectionInterfaceTest extends AbstractPredicateTest {
    

    protected AbstractCollectionInterfaceTest(final Class<?> interfaceClass) throws NoSuchMethodException, SecurityException {
        super( interfaceClass );
        builder.setNamespace( "http://example.com/")
        .setName(  "x"  )
        .setInternalType(  Literal.class  )
        .setLiteralType(  XSDDatatype.XSDstring );
    }
    
    @Test
    public void testParseGetter() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setCollectionType( List.class )
        .setInternalType( null ) 
        .setLiteralType( null );
        updateGetter();
        assertSame( builder, interfaceClass.getMethod( "getX" ) );

    }

    protected void updateGetter() {}
    
    @Test
    public void testParseSetter() throws Exception {
        builder.setActionType( ActionType.SETTER )
        .setLiteralType(  XSDDatatype.XSDstring )
        .setType(  String.class );
        updateSetter();
        assertSame( builder, interfaceClass.getMethod( "addX", String.class ) );
    }

    protected void updateSetter() {}

    @Test
    public void testParseExistential() throws Exception {
        builder.setActionType( ActionType.EXISTENTIAL )
        .setType(  String.class );
        updateExistential();
        assertSame( builder, interfaceClass.getMethod( "hasX", String.class ) );
    }

    protected void updateExistential() {}

    @Test
    public void testParseRemover() throws Exception {
        builder.setActionType( ActionType.REMOVER )
        .setType( String.class );
        updateRemover();
        assertSame( builder, interfaceClass.getMethod( "removeX", String.class ) );
    }

    protected void updateRemover() {}
    
}
