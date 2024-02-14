package org.xenei.jena.entities.impl.methodParser;

import java.lang.reflect.Method;

import org.apache.jena.rdf.model.Property;
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
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.MethodParser;
import org.xenei.jena.entities.impl.SubjectInfoImpl;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.VoidHandler;
import org.xenei.jena.entities.testing.iface.SingleValuePrimitiveInterface;

public class SingleValuePrimitiveTest {

    final String namespace = "http://localhost/test#";
    final SubjectInfoImpl subjectInfo = new SubjectInfoImpl( SingleValuePrimitiveInterface.class );
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
        final Method method = SingleValuePrimitiveInterface.class.getMethod( "setChar", char.class );
        PredicateInfo pi = methodParser.parse( method );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );
        Assertions.assertNotNull(
                subjectInfo.getPredicateInfo( SingleValuePrimitiveInterface.class.getMethod( "removeChar" ) ) );
        Assertions.assertNotNull(
                subjectInfo.getPredicateInfo( SingleValuePrimitiveInterface.class.getMethod( "getChar" ) ) );
        Assertions.assertNotNull(
                subjectInfo.getPredicateInfo( SingleValuePrimitiveInterface.class.getMethod( "hasChar" ) ) );

        String expectedHandler = PredicateInfoImplTest.createHandler( "string", "char" );
        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "setChar", char.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "char", namespace, char.class,
                false );

        pi = subjectInfo.getPredicateInfo( SingleValuePrimitiveInterface.class.getMethod( "getChar" ) );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, char.class, "getChar", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "char", namespace, char.class,
                false );

        pi = subjectInfo.getPredicateInfo( SingleValuePrimitiveInterface.class.getMethod( "removeChar" ) );
        PredicateInfoImplTest.assertValues( pi, ActionType.REMOVER, void.class, "removeChar", void.class,
                VoidHandler.class, "VoidHandler", expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "char", namespace, char.class,
                false );

        pi = subjectInfo.getPredicateInfo( SingleValuePrimitiveInterface.class.getMethod( "hasChar" ) );
        expectedHandler = PredicateInfoImplTest.createHandler( "boolean", "class java.lang.Boolean" );
        PredicateInfoImplTest.assertValues( pi, ActionType.EXISTENTIAL, boolean.class, "hasChar", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "char", namespace, char.class,
                false );
    }

    @Test
    public void booleanTest() throws Exception {
        String expectedHandler = PredicateInfoImplTest.createHandler( "boolean", "class java.lang.Boolean" );
        final Property expectedProperty = ResourceFactory.createProperty( namespace, "bool" );
        final Method method = SingleValuePrimitiveInterface.class.getMethod( "setBool", boolean.class );
        PredicateInfo pi = methodParser.parse( method );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );
        Assertions.assertNotNull(
                subjectInfo.getPredicateInfo( SingleValuePrimitiveInterface.class.getMethod( "removeBool" ) ) );
        Assertions.assertNotNull(
                subjectInfo.getPredicateInfo( SingleValuePrimitiveInterface.class.getMethod( "isBool" ) ) );
        Assertions.assertNotNull(
                subjectInfo.getPredicateInfo( SingleValuePrimitiveInterface.class.getMethod( "hasBool" ) ) );

        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "setBool", boolean.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "bool", namespace, boolean.class,
                false );

        pi = subjectInfo.getPredicateInfo( SingleValuePrimitiveInterface.class.getMethod( "isBool" ) );
        PredicateInfoImplTest.assertValues( pi, ActionType.EXISTENTIAL, boolean.class, "isBool", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "bool", namespace, boolean.class,
                false );

        pi = subjectInfo.getPredicateInfo( SingleValuePrimitiveInterface.class.getMethod( "removeBool" ) );
        PredicateInfoImplTest.assertValues( pi, ActionType.REMOVER, void.class, "removeBool", void.class,
                VoidHandler.class, "VoidHandler", expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "bool", namespace, boolean.class,
                false );

        pi = subjectInfo.getPredicateInfo( SingleValuePrimitiveInterface.class.getMethod( "hasBool" ) );
        expectedHandler = PredicateInfoImplTest.createHandler( "boolean", "class java.lang.Boolean" );
        PredicateInfoImplTest.assertValues( pi, ActionType.EXISTENTIAL, boolean.class, "hasBool", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "bool", namespace, boolean.class,
                false );

    }
}
