/*
 * Copyright 2012, XENEI.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xenei.jena.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.RDFWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.handlers.EntityHandler;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.testing.iface.CollectionValueInterface;
import org.xenei.jena.entities.testing.iface.TestInterface;


public class CollectionValueObjectEntityTests {
    
    private static final String NAMESPACE = "http://localhost/test#";

    private CollectionValueInterface underTest;
    private Model model;
    private EntityManager manager;
    private SubjectInfo subjectInfo;

    @BeforeEach
    public void setup() throws Exception {
        manager = EntityManagerFactory.getEntityManager();
        model = ModelFactory.createDefaultModel();
        subjectInfo = manager.getSubjectInfo( CollectionValueInterface.class );
        final Resource r = model.createResource( "http://localhost/CollectionValueObjectEntityTest" );
        underTest = manager.read( r, CollectionValueInterface.class );
    }

    @AfterEach
    public void teardown() {
        model.close();
    }
    
    static String getUri(String shortName) {
        return NAMESPACE+shortName.substring( 0,1 ).toLowerCase()+shortName.substring(1);
    }
    
    Method setter(String shortName, Class<?> dataType) throws NoSuchMethodException, SecurityException {
        return CollectionValueInterface.class.getMethod("add"+shortName, dataType);
    }
    
    Method getter(String shortName) throws NoSuchMethodException, SecurityException {
        Method m = CollectionValueInterface.class.getMethod("get"+shortName);
        assertTrue( Collection.class.isAssignableFrom( m.getReturnType() ));
        return m;
     }
    
    Method exist(String shortName, Class<?> dataType) throws NoSuchMethodException, SecurityException {
        Method m =  CollectionValueInterface.class.getMethod("has"+shortName, dataType);
        assertEquals( Boolean.class, m.getReturnType());
        return m;
    }
    
    Method remover(String shortName, Class<?> dataType) throws NoSuchMethodException, SecurityException {
        return CollectionValueInterface.class.getMethod("remove"+shortName, dataType);
    }
    
    @ParameterizedTest
    @MethodSource("normalSuiteTestParams")
    void normalSuiteTest(String shortName, Class<?> dataType, Object first, Object second ) throws Exception {
        Property p = model.createProperty( getUri(shortName));
        Method setter = setter(shortName, dataType);
        Method getter = getter(shortName);
        Method exist = exist(shortName, dataType);
        Method remover = remover(shortName, dataType);
        
        assertTrue(model.isEmpty());
        assertFalse((Boolean) exist.invoke(underTest, first));
        assertFalse((Boolean) exist.invoke(underTest, second));
        
        setter.invoke(underTest, first);
        assertEquals( 1, model.listObjectsOfProperty(p).toList().size());
        assertTrue((Boolean) exist.invoke(underTest, first));
        assertFalse((Boolean) exist.invoke(underTest, second));
        
        setter.invoke(underTest, second);
        assertEquals( 2, model.listObjectsOfProperty(p).toList().size());
        assertTrue((Boolean) exist.invoke(underTest, first));
        assertTrue((Boolean) exist.invoke(underTest, second));
        
        Collection<?> collection = (Collection<?>) getter.invoke(underTest);
        assertEquals(2, collection.size());
        assertTrue(collection.remove(first));
        assertTrue(collection.remove(second));

        remover.invoke(underTest, first);
        assertEquals( 1, model.listObjectsOfProperty(p).toList().size());
        assertFalse((Boolean) exist.invoke(underTest, first));
        assertTrue((Boolean) exist.invoke(underTest, second));

        remover.invoke(underTest,second);
        assertEquals( 0, model.listObjectsOfProperty(p).toList().size());
        assertFalse((Boolean) exist.invoke(underTest, first));
        assertFalse((Boolean) exist.invoke(underTest, second));
    }
    
    public static final Stream<Arguments> normalSuiteTestParams() {
        List<Arguments> args = new ArrayList<>();
        args.add( Arguments.of( "Bool", Boolean.class, true, Boolean.FALSE ));
        args.add( Arguments.of( "Char", Character.class, Character.valueOf('A'), Character.valueOf( 'b' ) ));
        args.add( Arguments.of( "Dbl", Double.class, Double.MAX_VALUE, 3.14d ));
        args.add( Arguments.of( "Flt", Float.class, Float.MAX_VALUE, 3.14f ));
        args.add( Arguments.of( "Int", Integer.class, Integer.MAX_VALUE, 314 ));
        args.add( Arguments.of( "Lng", Long.class, Long.MAX_VALUE, 314l));
        args.add( Arguments.of( "RDF", RDFNode.class, ResourceFactory.createStringLiteral( "foo" ), ResourceFactory.createResource()));
        args.add( Arguments.of( "Str", String.class, "first", "second"));
        args.add( Arguments.of( "U", RDFNode.class, ResourceFactory.createStringLiteral( "foo" ), ResourceFactory.createResource()));
        args.add( Arguments.of( "U3", RDFNode.class, ResourceFactory.createStringLiteral( "foo" ), ResourceFactory.createResource()));
        return args.stream();
    }
    
    @ParameterizedTest
    @MethodSource("uriSuiteTestParams")
    public void uriSuiteTest(String shortName, String shortName2) throws Exception {
        String first = "http://example.com#first";
        Resource firstR = model.createResource(first);
        String second = "http://example.com#second";
        Resource secondR = model.createResource(second);
        
        Method setter = setter(shortName, String.class);
        Method getter = getter(shortName2);
        Method exist = exist(shortName, String.class);
        Method remover = remover(shortName, String.class);
        
        Method setterR = setter(shortName, RDFNode.class);
        Method getterR = getter(shortName);
        Method existR = exist(shortName, RDFNode.class);
        Method removerR = remover(shortName, RDFNode.class);
        
        setter.invoke( underTest, first );
        setterR.invoke( underTest, secondR );

        assertTrue( (Boolean) exist.invoke( underTest, first ));
        assertTrue( (Boolean) existR.invoke( underTest, firstR ));
        assertTrue( (Boolean) exist.invoke( underTest,  second ));
        assertTrue( (Boolean) existR.invoke( underTest, secondR ));
        
        @SuppressWarnings("unchecked")
        Collection<RDFNode> collection = (Collection<RDFNode>) getterR.invoke( underTest );
        assertEquals(2, collection.size());
        collection.remove( firstR  );
        collection.remove( secondR );
        assertEquals(0, collection.size());
        
        @SuppressWarnings("unchecked")
        Collection<String> collectionStr = (Collection<String>) getter.invoke( underTest );
        assertEquals(2, collectionStr.size());
        collectionStr.remove( first  );
        collectionStr.remove( second );
        assertEquals(0, collectionStr.size());

        removerR.invoke( underTest, firstR);
        assertFalse( (Boolean) exist.invoke( underTest, first ));
        assertFalse( (Boolean) existR.invoke( underTest, firstR ));
        assertTrue( (Boolean) exist.invoke( underTest,  second ));
        assertTrue( (Boolean) existR.invoke( underTest, secondR ));

        remover.invoke( underTest, second );
        assertFalse( (Boolean) exist.invoke( underTest,  second ));
        assertFalse( (Boolean) existR.invoke( underTest, secondR ));
    }
    
    public static final Stream<Arguments> uriSuiteTestParams() {
        List<Arguments> args = new ArrayList<>();
        args.add( Arguments.of( "U", "U2" ));
        args.add( Arguments.of( "U3", "U4"));
        return args.stream();
    }
    
    @ParameterizedTest
    @MethodSource("subjectInfoTestParams")
    public void subjectInfoTest(String methodName, Class<?> methodArg, PredicateInfo expected) {
        PredicateInfo actual = subjectInfo.getPredicateInfo(methodName, methodArg);
        assertNotNull(actual, "Method not found");
        assertEquals( expected.getActionType(), actual.getActionType());
        assertEquals( expected.getArgumentType(), actual.getArgumentType());
        assertEquals( expected.getEnclosedType(), actual.getEnclosedType());
        assertEquals( expected.getMethodName(), actual.getMethodName());
        assertEquals( expected.getNamespace(), actual.getNamespace());
        assertEquals( expected.getObjectHandler().getClass(), actual.getObjectHandler().getClass());
        assertEquals( expected.getPostExec(), actual.getPostExec());
        EffectivePredicate ep = actual.getPredicate();
        Class<?> epType = null;
        switch (expected.getActionType()) {
        case SETTER:
            epType = expected.getEnclosedType() == void.class ? expected.getArgumentType() : expected.getEnclosedType();
            break;
        case GETTER:
            epType = expected.getEnclosedType();
            break;
        case EXISTENTIAL:
            epType = expected.getValueType();
            break;
        case REMOVER:
            epType = expected.getArgumentType();
            break;
        }
        EffectivePredicateTest.assertValues( ep, false, false, "", expected.getProperty().getLocalName(), 
                NAMESPACE, epType, false );
        assertEquals( expected.getProperty(), actual.getProperty());
        assertEquals( expected.getReturnType(), actual.getReturnType());
        assertEquals( expected.getUriString(), actual.getUriString());
        assertEquals( expected.getValueType(), actual.getValueType());
        
    }
   
    public static final Stream<Arguments> subjectInfoTestParams() {

        List<Arguments> args = literalFunctions( new ArrayList<>(), "Bool", Boolean.class, Set.class);
        args = literalFunctions( args, "Char", Character.class, List.class);
        args = literalFunctions( args, "Dbl", Double.class, Queue.class);
        args = literalFunctions( args, "Flt", Float.class, Set.class);
        args = literalFunctions( args, "Int", Integer.class, Queue.class);
        args = literalFunctions( args, "Lng", Long.class, List.class);
        args = literalFunctions( args, "Str", String.class, Set.class);

        args = resourceFunctions( args, "RDF", RDFNode.class, List.class);
        args = resourceFunctions( args, "U", RDFNode.class, Set.class);
        args = resourceFunctions( args, "U3", RDFNode.class, Queue.class);
        
        args = uriFunctions( args, "U", "U2", List.class);
        args = uriFunctions( args, "U3", "U4", Set.class);
        args = entFunctions( args, "Ent", TestInterface.class, Queue.class);

        return args.stream();
    }
    
    private static List<Arguments> literalFunctions(List<Arguments> args, String shortName, Class<?> valueType,
            Class<?> collectionType) {
        final TypeMapper typeMapper = TypeMapper.getInstance();
        final String namespace = getUri(shortName);
        args.add( makeInfo( valueType, ActionType.SETTER, valueType, void.class, "add"+shortName,
                new LiteralHandler( typeMapper.getTypeByClass( valueType ) ), Collections.emptyList(), void.class,
                namespace, valueType ) );

        args.add( makeInfo( collectionType, ActionType.GETTER, void.class, valueType, "get"+shortName,
                new LiteralHandler( typeMapper.getTypeByClass( valueType ) ), Collections.emptyList(), collectionType,
                namespace, collectionType ) );
        
        args.add( makeInfo( valueType, ActionType.EXISTENTIAL, valueType, void.class, "has"+shortName,
                new LiteralHandler( typeMapper.getTypeByClass( Boolean.class ) ), Collections.emptyList(), Boolean.class,
                namespace, valueType ) );

        args.add( makeInfo( valueType, ActionType.REMOVER, valueType, void.class, "remove"+shortName,
                new LiteralHandler( typeMapper.getTypeByClass( valueType ) ), Collections.emptyList(), void.class,
                namespace, valueType ) );
        return args;
    }
    
    private static List<Arguments> resourceFunctions(List<Arguments> args, String shortName, Class<?> valueType,
            Class<?> collectionType) {
        final String namespace = getUri(shortName);        
        args.add( makeInfo( valueType, ActionType.SETTER, valueType, void.class, "add"+shortName,
                ResourceHandler.INSTANCE, Collections.emptyList(), void.class,
                namespace, valueType ) );

        args.add( makeInfo( collectionType, ActionType.GETTER, void.class, valueType, "get"+shortName,
                ResourceHandler.INSTANCE, Collections.emptyList(), collectionType,
                namespace, collectionType ) );
        
        args.add( makeInfo( valueType, ActionType.EXISTENTIAL, valueType, void.class, "has"+shortName,
                ResourceHandler.INSTANCE, Collections.emptyList(), Boolean.class,
                namespace, valueType ) );

        args.add( makeInfo( valueType, ActionType.REMOVER, valueType, void.class, "remove"+shortName,
                ResourceHandler.INSTANCE, Collections.emptyList(), void.class,
                namespace, valueType ) );
        return args;
    }
    
    private static List<Arguments> uriFunctions(List<Arguments> args, String shortName, String collectionName, Class<?> collectionType) {
        final String namespace = getUri(shortName);
        args.add( makeInfo( String.class, ActionType.SETTER, String.class, URI.class, "add"+shortName,
                UriHandler.INSTANCE, Collections.emptyList(), void.class,
                namespace, String.class ) );

        args.add( makeInfo( collectionType, ActionType.GETTER, void.class, URI.class, "get"+collectionName,
                UriHandler.INSTANCE, Collections.emptyList(), collectionType,
                namespace, collectionType ) );
        
        args.add( makeInfo( String.class, ActionType.EXISTENTIAL, URI.class, void.class, "has"+shortName,
                UriHandler.INSTANCE, Collections.emptyList(), Boolean.class,
                namespace, URI.class ) );

        args.add( makeInfo( String.class, ActionType.REMOVER, URI.class, void.class, "remove"+shortName,
                UriHandler.INSTANCE, Collections.emptyList(), void.class,
                namespace, URI.class ) );
        return args;
    }
    
    private static List<Arguments> entFunctions(List<Arguments> args, String shortName, Class<?> valueType,
            Class<?> collectionType) {
        final String namespace = getUri(shortName);
        args.add( makeInfo( valueType, ActionType.SETTER, valueType, void.class, "add"+shortName,
                new EntityHandler(valueType), Collections.emptyList(), void.class,
                namespace, valueType ) );

        args.add( makeInfo( collectionType, ActionType.GETTER, void.class, valueType, "get"+shortName,
                new EntityHandler(valueType), Collections.emptyList(), collectionType,
                namespace, collectionType ) );
        
        args.add( makeInfo( valueType, ActionType.EXISTENTIAL, valueType, void.class, "has"+shortName,
                new EntityHandler(valueType), Collections.emptyList(), Boolean.class,
                namespace, valueType ) );

        args.add( makeInfo( valueType, ActionType.REMOVER, valueType, void.class, "remove"+shortName,
                new EntityHandler(valueType), Collections.emptyList(), void.class,
                namespace, valueType ) );
        return args;
    }
    
    private static Arguments makeInfo(Class<?> methodArgType, ActionType actionType, Class<?> argumentType, Class<?> enclosedType,
            String methodName, ObjectHandler objectHandler, List<Method> postExec,
            Class<?> returnType, String uriString, Class<?> valueType){
        return Arguments.of( methodName, methodArgType,  new PredicateInfo() {

            @Override
            public ActionType getActionType() {
                return actionType;
            }

            @Override
            public String getMethodName() {
                return methodName;
            }

            @Override
            public String getNamespace() {
                return NAMESPACE;
            }

            @Override
            public Property getProperty() {
                return ResourceFactory.createProperty( uriString );
            }

            @Override
            public String getUriString() {
                return uriString;
            }

            @Override
            public List<Method> getPostExec() {
                return postExec;
            }

            @Override
            public ObjectHandler getObjectHandler() {
                return objectHandler;
            }

            @Override
            public EffectivePredicate getPredicate() {
                return null;
            }

            @Override
            public Class<?> getArgumentType() {
                return argumentType;
            }

            @Override
            public Class<?> getReturnType() {
                return returnType;
            }

            @Override
            public Class<?> getEnclosedType() {
                return enclosedType;
            }

            @Override
            public Class<?> getValueType() {
                return valueType;
            }}
            );
        
    }

    /* separate test because we have to create the test interface in the graph */
    @Test
    public void testEnt() throws Exception {
        assertTrue(model.isEmpty());
        Property p = model.createProperty( getUri("Ent"));
        
        Resource t1r = model.createResource( getUri("t1p"));
        TestInterface first = manager.read( t1r, TestInterface.class );
        Resource t2r = model.createResource( getUri("t2p"));
        TestInterface second = manager.read( t2r, TestInterface.class );

        assertFalse(underTest.hasEnt(first));
        assertFalse(underTest.hasEnt(second));
        
        underTest.addEnt( first );
        
        
        RDFWriter.source(model)
        .format(RDFFormat.TURTLE_LONG)
        .output(System.out);
        
        
        assertEquals( 1, model.listObjectsOfProperty(p).toList().size());
        assertTrue(underTest.hasEnt( first));
        assertFalse(underTest.hasEnt(second));
        
        underTest.addEnt( second);
        assertEquals( 2, model.listObjectsOfProperty(p).toList().size());
        assertTrue(underTest.hasEnt( first));
        assertTrue(underTest.hasEnt( second));
        
        Collection<TestInterface> collection = underTest.getEnt();
        assertEquals(2, collection.size());
        assertTrue(collection.remove(first));
        assertTrue(collection.remove(second));

        underTest.removeEnt( first);
        assertEquals( 1, model.listObjectsOfProperty(p).toList().size());
        assertFalse(underTest.hasEnt( first));
        assertTrue(underTest.hasEnt( second));

        underTest.removeEnt( second);
        assertEquals( 0, model.listObjectsOfProperty(p).toList().size());
        assertFalse(underTest.hasEnt(first));
        assertFalse(underTest.hasEnt(second));
    }
}
