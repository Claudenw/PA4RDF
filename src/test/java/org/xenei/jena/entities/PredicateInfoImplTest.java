package org.xenei.jena.entities;

import java.util.Iterator;
import java.util.Set;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.handlers.EntityHandler;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.impl.handlers.VoidHandler;

public class PredicateInfoImplTest {
    @BeforeAll
    public static void setupClass() {
        EntityManagerFactory.setEntityManager( Mockito.mock( EntityManager.class ) );
    }

    @AfterAll
    public static void teardownClass() {
        EntityManagerFactory.setEntityManager( null );
    }

    final Property expectedProperty = ResourceFactory.createProperty( "http://example.com/", "x" );

    /**
     *
     * @param pi
     * @param actionType
     * @param returnType
     * @param methodName
     * @param argumentClass
     * @param handlerClass
     * @param handlerString
     * @param property
     * @param enclosedClass
     */
    public static void assertValues(final PredicateInfo pi, final ActionType actionType, final Class<?> returnType,
            final String methodName, final Class<?> argumentClass, final Class<?> handlerClass,
            final String handlerString, final Property property, final Class<?> enclosedClass) {
        Assertions.assertEquals( actionType, pi.getActionType(), pi.toString() + " actionType error" );
        Assertions.assertEquals( returnType, pi.getReturnType(), pi.toString() + " returnType error" );
        Assertions.assertEquals( methodName, pi.getMethodName(), pi.toString() + " methodName error" );
        Assertions.assertEquals( argumentClass, pi.getArgumentType(), pi.toString() + " argumentClass error" );
        Assertions.assertEquals( handlerClass, pi.getObjectHandler().getClass(),
                pi.toString() + " handlerClass error" );
        Assertions.assertEquals( handlerString, pi.getObjectHandler().toString(),
                pi.toString() + " handlerString error" );
        Assertions.assertEquals( property, pi.getProperty(), pi.toString() + " property error" );
        Assertions.assertEquals( enclosedClass, pi.getEnclosedType(), pi.toString() + " enclosedClass error" );
    }

    private EffectivePredicate initializePredicate() {
        final EffectivePredicate ep = new EffectivePredicate();
        ep.setNamespace( "http://example.com/" );
        return ep;
    }

    @Test
    public void testGetConstructor() {
        final EffectivePredicate ep = initializePredicate();
        final String expectedHandler = "LiteralHandler{Datatype[http://www.w3.org/2001/XMLSchema#int -> class java.lang.Integer]}";

        final PredicateInfo pi = new PredicateInfoImpl( ep, Integer.class, "getX", void.class );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, Integer.class, "getX", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
    }

    @Test
    public void testNameChangeConstructor() {
        final EffectivePredicate ep = initializePredicate();
        ep.setName( "foo" );
        final String expectedHandler = "LiteralHandler{Datatype[http://www.w3.org/2001/XMLSchema#int -> class java.lang.Integer]}";
        final Property expectedProperty = ResourceFactory.createProperty( ep.namespace(), "foo" );

        final PredicateInfo pi = new PredicateInfoImpl( ep, Integer.class, "getX", void.class );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, Integer.class, "getX", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
    }

    @Test
    public void testURIConstructor() {
        final EffectivePredicate ep = initializePredicate();

        // URI class
        ep.setType( URI.class );
        final PredicateInfo pi = new PredicateInfoImpl( ep, Integer.class, "getX", void.class );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, URI.class, "getX", void.class, UriHandler.class,
                "UriHandler", expectedProperty, void.class );
    }

    @Test
    public void testGetterConstructor() {
        final EffectivePredicate ep = initializePredicate();
        final String expectedHandler = "LiteralHandler{Datatype[http://www.w3.org/2001/XMLSchema#int -> class java.lang.Integer]}";

        final PredicateInfo pi = new PredicateInfoImpl( ep, Integer.class, "getX", void.class );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, Integer.class, "getX", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
    }

    @Test
    public void testGetCollectionConstructor() {
        // collection type
        final EffectivePredicate ep = initializePredicate();
        final String expectedHandler = "LiteralHandler{Datatype[http://www.w3.org/2001/XMLSchema#int -> class java.lang.Integer]}";

        ep.setType( Integer.class );
        final PredicateInfo pi = new PredicateInfoImpl( ep, Set.class, "getX", void.class );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, Set.class, "getX", void.class, LiteralHandler.class,
                expectedHandler, expectedProperty, Integer.class );
    }

    @Test
    public void testSetCollectionConstructor() {
        final EffectivePredicate ep = initializePredicate();

        ep.setType( int.class );
        final PredicateInfo pi = new PredicateInfoImpl( ep, void.class, "setX", Iterator.class );
        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "setX", Iterator.class,
                VoidHandler.class, "VoidHandler", expectedProperty, int.class );
    }

    @Test
    public void testResourceSetterConstructor() {
        final EffectivePredicate ep = initializePredicate();

        final PredicateInfo pi = new PredicateInfoImpl( ep, void.class, "setX", RDFNode.class );
        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "setX", RDFNode.class,
                ResourceHandler.class, "ResourceHandler", expectedProperty, void.class );
    }

    @Test
    public void testResourceGetterConstructor() {
        final EffectivePredicate ep = initializePredicate();

        final PredicateInfo pi = new PredicateInfoImpl( ep, RDFNode.class, "getX", void.class );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, RDFNode.class, "getX", void.class,
                ResourceHandler.class, "ResourceHandler", expectedProperty, void.class );
    }

    @Test
    public void testSubjectAnnotatedValueConstructor() {
        final EffectivePredicate ep = initializePredicate();

        final PredicateInfo pi = new PredicateInfoImpl( ep, PITestInterface.class, "getX", void.class );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, PITestInterface.class, "getX", void.class,
                EntityHandler.class, "EntityHandler", expectedProperty, void.class );
    }

    public static String createHandler(final String base, final String type) {
        return String.format( "LiteralHandler{Datatype[http://www.w3.org/2001/XMLSchema#%s -> %s]}", base, type );
    }

    @Subject()
    interface PITestInterface {

    }
}
