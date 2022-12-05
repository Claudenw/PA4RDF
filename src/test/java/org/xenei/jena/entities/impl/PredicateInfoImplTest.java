package org.xenei.jena.entities.impl;

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
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;
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

    private void assertValues(final PredicateInfoImpl pi, final ActionType actionType, final Class<?> concreteType,
            final String methodName, final Class<?> handlerClass, final String handlerString, final Property property,
            final Class<?> valueClass) {
        Assertions.assertEquals( actionType, pi.getActionType() );
        Assertions.assertEquals( concreteType, pi.getConcreteType() );
        Assertions.assertEquals( methodName, pi.getMethodName() );
        Assertions.assertEquals( handlerClass, pi.getObjectHandler().getClass() );
        Assertions.assertEquals( handlerString, pi.getObjectHandler().toString() );
        Assertions.assertEquals( property, pi.getProperty() );
        Assertions.assertEquals( valueClass, pi.getValueClass() );
    }

    @Test
    public void testConstructor() {
        final EffectivePredicate ep = new EffectivePredicate();
        ep.setNamespace( "http://example.com/" );
        ep.setName( "name" );
        PredicateInfoImpl pi = new PredicateInfoImpl( ep, "getX", Integer.class );
        final String expectedHandler = "LiteralHandler{Datatype[http://www.w3.org/2001/XMLSchema#int -> class java.lang.Integer]}";
        final Property expectedProperty = ResourceFactory.createProperty( ep.namespace(), ep.name() );
        assertValues( pi, ActionType.GETTER, Integer.class, "getX", LiteralHandler.class, expectedHandler,
                expectedProperty, Integer.class );

        // URI class
        ep.setType( URI.class );
        pi = new PredicateInfoImpl( ep, "getX", Integer.class );
        assertValues( pi, ActionType.GETTER, URI.class, "getX", UriHandler.class, "UriHandler", expectedProperty,
                Integer.class );

        // iterator type
        ep.setType( Integer.class );
        pi = new PredicateInfoImpl( ep, "getX", Iterator.class );
        assertValues( pi, ActionType.GETTER, Integer.class, "getX", LiteralHandler.class, expectedHandler,
                expectedProperty, Iterator.class );

        // collection type
        pi = new PredicateInfoImpl( ep, "getX", Set.class );
        assertValues( pi, ActionType.GETTER, Integer.class, "getX", LiteralHandler.class, expectedHandler,
                expectedProperty, Set.class );

        // void handler class
        ep.setType( int.class );
        pi = new PredicateInfoImpl( ep, "setX", Iterator.class );
        assertValues( pi, ActionType.SETTER, Iterator.class, "setX", VoidHandler.class, "VoidHandler", expectedProperty,
                Iterator.class );

        // resource value class
        ep.setType( null );
        pi = new PredicateInfoImpl( ep, "setX", RDFNode.class );
        assertValues( pi, ActionType.SETTER, RDFNode.class, "setX", ResourceHandler.class, "ResourceHandler",
                expectedProperty, RDFNode.class );

        // @Subject annotated value class
        pi = new PredicateInfoImpl( ep, "getX", PITestInterface.class );
        assertValues( pi, ActionType.GETTER, PITestInterface.class, "getX", EntityHandler.class, "EntityHandler",
                expectedProperty, PITestInterface.class );
    }

    @Subject()
    interface PITestInterface {

    }
}
