package org.xenei.jena.entities.impl.methodParser;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

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
import org.xenei.jena.entities.testing.iface.CollectionValueInterface;

public class CollectionValueObjectTest {

    final String namespace = "http://localhost/test#";
    final SubjectInfoImpl subjectInfo = new SubjectInfoImpl( CollectionValueInterface.class );
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
        final Method method = CollectionValueInterface.class.getMethod( "addChar", Character.class );
        PredicateInfo pi = methodParser.parse( method );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );
        Assertions.assertNotNull( subjectInfo
                .getPredicateInfo( CollectionValueInterface.class.getMethod( "removeChar", Character.class ) ) );
        Assertions
                .assertNotNull( subjectInfo.getPredicateInfo( CollectionValueInterface.class.getMethod( "getChar" ) ) );
        Assertions.assertNotNull( subjectInfo
                .getPredicateInfo( CollectionValueInterface.class.getMethod( "hasChar", Character.class ) ) );

        String expectedHandler = PredicateInfoImplTest.createHandler( "string", "class java.lang.Character" );
        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "addChar", Character.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "char", namespace, Character.class,
                false );

        pi = subjectInfo.getPredicateInfo( CollectionValueInterface.class.getMethod( "getChar" ) );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, List.class, "getChar", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty, Character.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "char", namespace, Character.class,
                false );

        pi = subjectInfo.getPredicateInfo( CollectionValueInterface.class.getMethod( "removeChar", Character.class ) );
        PredicateInfoImplTest.assertValues( pi, ActionType.REMOVER, void.class, "removeChar", Character.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "char", namespace, Character.class,
                false );

        pi = subjectInfo.getPredicateInfo( CollectionValueInterface.class.getMethod( "hasChar", Character.class ) );
        expectedHandler = PredicateInfoImplTest.createHandler( "boolean", "class java.lang.Boolean" );
        PredicateInfoImplTest.assertValues( pi, ActionType.EXISTENTIAL, Boolean.class, "hasChar", Character.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "char", namespace, Character.class,
                false );
    }

    @Test
    public void uSeriesTest() throws Exception {
        final Property expectedProperty = ResourceFactory.createProperty( namespace, "u" );

        Method method = CollectionValueInterface.class.getMethod( "addU", String.class );
        PredicateInfo pi = methodParser.parse( method );
        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "addU", String.class, UriHandler.class,
                "UriHandler", expectedProperty, URI.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "u", namespace, URI.class, false );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );

        method = CollectionValueInterface.class.getMethod( "addU", RDFNode.class );
        pi = methodParser.parse( method );
        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "addU", RDFNode.class,
                ResourceHandler.class, "ResourceHandler", expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "u", namespace, RDFNode.class,
                false );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );

        method = CollectionValueInterface.class.getMethod( "getU" );
        pi = methodParser.parse( method );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, Set.class, "getU", void.class, ResourceHandler.class,
                "ResourceHandler", expectedProperty, RDFNode.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "u", namespace, RDFNode.class,
                false );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );

        method = CollectionValueInterface.class.getMethod( "getU2" );
        pi = methodParser.parse( method );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, List.class, "getU2", void.class, UriHandler.class,
                "UriHandler", expectedProperty, URI.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, false, "", "u", namespace, URI.class, false );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );
    }

}
