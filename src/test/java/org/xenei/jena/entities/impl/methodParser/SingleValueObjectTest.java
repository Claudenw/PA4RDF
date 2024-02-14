package org.xenei.jena.entities.impl.methodParser;

import java.lang.reflect.Method;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.xenei.jena.entities.EffectivePredicateTest;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.PredicateInfoImplTest;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.MethodParser;
import org.xenei.jena.entities.impl.SubjectInfoImpl;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.impl.handlers.VoidHandler;
import org.xenei.jena.entities.testing.iface.SingleValueObjectInterface;

public class SingleValueObjectTest {

    final String namespace = "http://localhost/test#";
    final SubjectInfoImpl subjectInfo = new SubjectInfoImpl( SingleValueObjectInterface.class );
    final MethodParser methodParser = new MethodParser( subjectInfo );

    @BeforeAll
    public static void setupClass() {
        EntityManagerFactory.setEntityManager( Mockito.mock( EntityManager.class ) );
    }

    @AfterAll
    public static void teardownClass() {
        EntityManagerFactory.setEntityManager( null );
    }

    @Test
    public void charTest() throws Exception {

        final Property expectedProperty = ResourceFactory.createProperty( namespace, "char" );
        final Method method = SingleValueObjectInterface.class.getMethod( "setChar", Character.class );
        PredicateInfo pi = methodParser.parse( method );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );
        Assertions.assertNotNull(
                subjectInfo.getPredicateInfo( SingleValueObjectInterface.class.getMethod( "removeChar" ) ) );
        Assertions.assertNotNull(
                subjectInfo.getPredicateInfo( SingleValueObjectInterface.class.getMethod( "getChar" ) ) );
        Assertions.assertNotNull(
                subjectInfo.getPredicateInfo( SingleValueObjectInterface.class.getMethod( "hasChar" ) ) );

        String expectedHandler = PredicateInfoImplTest.createHandler( "string", "class java.lang.Character" );
        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "setChar", Character.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "char", namespace, Character.class,
                false );

        pi = subjectInfo.getPredicateInfo( SingleValueObjectInterface.class.getMethod( "getChar" ) );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, Character.class, "getChar", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "char", namespace, Character.class,
                false );

        pi = subjectInfo.getPredicateInfo( SingleValueObjectInterface.class.getMethod( "removeChar" ) );
        PredicateInfoImplTest.assertValues( pi, ActionType.REMOVER, void.class, "removeChar", void.class,
                VoidHandler.class, "VoidHandler", expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "char", namespace, Character.class,
                false );

        pi = subjectInfo.getPredicateInfo( SingleValueObjectInterface.class.getMethod( "hasChar" ) );
        expectedHandler = PredicateInfoImplTest.createHandler( "boolean", "class java.lang.Boolean" );
        PredicateInfoImplTest.assertValues( pi, ActionType.EXISTENTIAL, Boolean.class, "hasChar", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "char", namespace, Character.class,
                false );
    }

    @Test
    public void booleanTest() throws Exception {
        // boolean
        String expectedHandler = PredicateInfoImplTest.createHandler( "boolean", "class java.lang.Boolean" );
        final Property expectedProperty = ResourceFactory.createProperty( namespace, "bool" );
        final Method method = SingleValueObjectInterface.class.getMethod( "setBool", Boolean.class );
        PredicateInfo pi = methodParser.parse( method );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );
        Assertions.assertNotNull(
                subjectInfo.getPredicateInfo( SingleValueObjectInterface.class.getMethod( "removeBool" ) ) );
        Assertions.assertNotNull(
                subjectInfo.getPredicateInfo( SingleValueObjectInterface.class.getMethod( "isBool" ) ) );
        Assertions.assertNotNull(
                subjectInfo.getPredicateInfo( SingleValueObjectInterface.class.getMethod( "hasBool" ) ) );

        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "setBool", Boolean.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "bool", namespace, Boolean.class,
                false );

        pi = subjectInfo.getPredicateInfo( SingleValueObjectInterface.class.getMethod( "isBool" ) );
        PredicateInfoImplTest.assertValues( pi, ActionType.EXISTENTIAL, Boolean.class, "isBool", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "bool", namespace, Boolean.class,
                false );

        pi = subjectInfo.getPredicateInfo( SingleValueObjectInterface.class.getMethod( "removeBool" ) );
        PredicateInfoImplTest.assertValues( pi, ActionType.REMOVER, void.class, "removeBool", void.class,
                VoidHandler.class, "VoidHandler", expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "bool", namespace, Boolean.class,
                false );

        pi = subjectInfo.getPredicateInfo( SingleValueObjectInterface.class.getMethod( "hasBool" ) );
        expectedHandler = PredicateInfoImplTest.createHandler( "boolean", "class java.lang.Boolean" );
        PredicateInfoImplTest.assertValues( pi, ActionType.EXISTENTIAL, Boolean.class, "hasBool", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "bool", namespace, Boolean.class,
                false );
    }

    @Test
    public void uSeriesTest() throws Exception {
        final Property expectedProperty = ResourceFactory.createProperty( namespace, "u" );

        Method method = SingleValueObjectInterface.class.getMethod( "getU2" );
        PredicateInfo pi = methodParser.parse( method );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, String.class, "getU2", void.class, UriHandler.class,
                "UriHandler", expectedProperty, URI.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "u", namespace, URI.class, false );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );

        method = SingleValueObjectInterface.class.getMethod( "setU", String.class );
        pi = methodParser.parse( method );
        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "setU", String.class, UriHandler.class,
                "UriHandler", expectedProperty, URI.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "u", namespace, URI.class, false );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );

        method = SingleValueObjectInterface.class.getMethod( "setU", RDFNode.class );
        pi = methodParser.parse( method );
        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "setU", RDFNode.class,
                ResourceHandler.class, "ResourceHandler", expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "u", namespace, RDFNode.class, false );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );

        method = SingleValueObjectInterface.class.getMethod( "getU" );
        pi = methodParser.parse( method );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, RDFNode.class, "getU", void.class,
                ResourceHandler.class, "ResourceHandler", expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "u", namespace, RDFNode.class, false );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );

    }

}
