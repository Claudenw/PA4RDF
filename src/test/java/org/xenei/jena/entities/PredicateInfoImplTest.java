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
import org.xenei.jena.entities.impl.Action;
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
    
    public static String createHandler(final String base, final String type) {
        return String.format( "LiteralHandler{Datatype[http://www.w3.org/2001/XMLSchema#%s -> %s]}", base, type );
    }


    Property expectedProperty(String name) {
        return ResourceFactory.createProperty( "http://example.com/", name );
    }

    interface TestInterface {
        Integer getInteger();
        int getInt();
        void setInt(int i);
        Set<Integer> getSet();
        void setIterator(Iterator<Integer> iter);
        RDFNode getRDF();
        void setRDF(RDFNode i);
        TestInterface2 getZ(); 
        }
    
    @Subject()
    interface TestInterface2 {
        }
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
    public void testGetConstructor() throws Exception {
        final EffectivePredicate ep = initializePredicate();
        final String expectedHandler = "LiteralHandler{Datatype[http://www.w3.org/2001/XMLSchema#int -> class java.lang.Integer]}";
        Action action = new Action( TestInterface.class.getMethod(  "getInteger" ) );
        final PredicateInfo pi = new PredicateInfoImpl( ep, action );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, Integer.class, "getInteger", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty("integer"), void.class );
    }

    @Test
    public void testNameChangeConstructor() throws Exception {
        final EffectivePredicate ep = initializePredicate();
        ep.setName( "foo" );
        final String expectedHandler = "LiteralHandler{Datatype[http://www.w3.org/2001/XMLSchema#int -> class java.lang.Integer]}";
        Action action = new Action( TestInterface.class.getMethod( "getInteger" ) );
        
        final PredicateInfo pi = new PredicateInfoImpl( ep, action );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, Integer.class, "getInteger", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty("foo"), void.class );
    }

    @Test
    public void testURIConstructor() throws Exception {
        final EffectivePredicate ep = initializePredicate();
        // URI class
        ep.setType( URI.class );
        Action action = new Action( TestInterface.class.getMethod(  "getInteger" ) );
        
        final PredicateInfo pi = new PredicateInfoImpl( ep, action );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, URI.class, "getInteger", void.class, UriHandler.class,
                "UriHandler", expectedProperty("integer"), void.class );
    }

    @Test
    public void testGetterConstructor() throws Exception {
        final EffectivePredicate ep = initializePredicate();
        final String expectedHandler = "LiteralHandler{Datatype[http://www.w3.org/2001/XMLSchema#int -> class java.lang.Integer]}";
        Action action = new Action( TestInterface.class.getMethod(  "getInteger" ) );
        
        final PredicateInfo pi = new PredicateInfoImpl( ep, action );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, Integer.class, "getInteger", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty("integer"), void.class );
    }

    @Test
    public void testGetCollectionConstructor() throws Exception {
        // collection type
        final EffectivePredicate ep = initializePredicate();
        final String expectedHandler = "LiteralHandler{Datatype[http://www.w3.org/2001/XMLSchema#int -> class java.lang.Integer]}";
        ep.setType( Integer.class );
        Action action = new Action( TestInterface.class.getMethod(  "getSet" ) );
        
        final PredicateInfo pi = new PredicateInfoImpl( ep, action );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, Set.class, "getSet", void.class, LiteralHandler.class,
                expectedHandler, expectedProperty("set"), Integer.class );
    }

    @Test
    public void testSetCollectionConstructor() throws Exception {
        final EffectivePredicate ep = initializePredicate();
        ep.setType( int.class );
        Action action = new Action( TestInterface.class.getMethod(  "setIterator", Iterator.class ) );
        
        final PredicateInfo pi = new PredicateInfoImpl( ep, action );
        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "setIterator", Iterator.class,
                VoidHandler.class, "VoidHandler", expectedProperty("iterator"), int.class );
    }

    @Test
    public void testResourceSetterConstructor() throws Exception {
        final EffectivePredicate ep = initializePredicate();
        Action action = new Action( TestInterface.class.getMethod(  "setRDF", RDFNode.class ) );
        
        final PredicateInfo pi = new PredicateInfoImpl( ep, action );
        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "setRDF", RDFNode.class,
                ResourceHandler.class, "ResourceHandler", expectedProperty("rDF"), void.class );
    }

    @Test
    public void testResourceGetterConstructor() throws Exception {
        final EffectivePredicate ep = initializePredicate();
        Action action = new Action( TestInterface.class.getMethod( "getRDF" ) );
        
        final PredicateInfo pi = new PredicateInfoImpl( ep, action );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, RDFNode.class, "getRDF", void.class,
                ResourceHandler.class, "ResourceHandler", expectedProperty("rDF"), void.class );
    }

    @Test
    public void testSubjectAnnotatedValueConstructor() throws Exception {
        final EffectivePredicate ep = initializePredicate();
        Action action = new Action( TestInterface.class.getMethod( "getZ" ) );
        
        final PredicateInfo pi = new PredicateInfoImpl( ep, action );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, TestInterface2.class, "getZ", void.class,
                EntityHandler.class, "EntityHandler", expectedProperty("z"), void.class );
    }

}
