package org.xenei.jena.entities.impl.methodParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.xenei.jena.entities.EffectivePredicate;
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
import org.xenei.jena.entities.testing.iface.CollectionValueHelper;
import org.xenei.jena.entities.testing.iface.CollectionValueHelper.EnhancedPredicateInfo;
import org.xenei.jena.entities.testing.iface.CollectionValueInterface;
import org.xenei.jena.entities.testing.iface.TestInterface;

public class CollectionValueTest {

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

    @ParameterizedTest
    @MethodSource("parserTestParams")
    public void parserTest(String name, EnhancedPredicateInfo expected) throws Exception {
        List<String> rdfNodeMethods = List.of( "getU", "getU3");
        List<String> uriNodeMethods = List.of( "getU2", "getU4");
        
        Method method = null;
        switch (expected.getActionType()) {
        case EXISTENTIAL:
        case REMOVER:
            if (expected.getArgumentType() == URI.class) {
                method =  CollectionValueInterface.class.getMethod( expected.getMethodName(), expected.getMethodArgType());
                break;
            }
        case SETTER:
            method =  CollectionValueInterface.class.getMethod( expected.getMethodName(), expected.getArgumentType());
            break;
        case GETTER:
            method = CollectionValueInterface.class.getMethod( expected.getMethodName());
        }
        PredicateInfo actual = methodParser.parse( method );
            assertEquals( expected.getActionType(), actual.getActionType());
            assertEquals( expected.getArgumentType(), actual.getArgumentType());
            if (expected.getActionType() != ActionType.GETTER) {
                assertEquals( expected.getEnclosedType(), actual.getEnclosedType());
                assertEquals( expected.getObjectHandler().getClass(), actual.getObjectHandler().getClass());
            }
            assertEquals( expected.getMethodName(), actual.getMethodName());
            assertEquals( expected.getNamespace(), actual.getNamespace());
            assertEquals( expected.getPostExec(), actual.getPostExec());
            EffectivePredicate ep = actual.getPredicate();
            Class<?> epType = null;
            switch (expected.getActionType()) {
            case SETTER:
                epType = expected.getEnclosedType() == void.class ? expected.getArgumentType() : expected.getEnclosedType();
                break;
            case GETTER:
                epType = rdfNodeMethods.contains(expected.getMethodName()) ? RDFNode.class : 
                    uriNodeMethods.contains(expected.getMethodName()) ? URI.class : void.class;
                break;
            case EXISTENTIAL:
                epType = expected.getValueType();
                break;
            case REMOVER:
                epType = expected.getArgumentType();
                break;
            }
            EffectivePredicateTest.assertValues( ep, false, false, "", expected.getProperty().getLocalName(), 
                    CollectionValueHelper.NAMESPACE, epType, false );
            assertEquals( expected.getProperty(), actual.getProperty());
            assertEquals( expected.getReturnType(), actual.getReturnType());
            assertEquals( expected.getUriString(), actual.getUriString());
            assertEquals( expected.getValueType(), actual.getValueType());
    }
    
    public static final Stream<Arguments> parserTestParams() {
        return CollectionValueHelper.createAllPredicateInfo().stream()
                .map( p -> Arguments.of(p.getMethodName()+"#"+p.getArgumentType(), p));
    }
}
