package org.xenei.jena.entities.testing.iface;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.params.provider.Arguments;
import org.xenei.jena.entities.EffectivePredicate;
import org.xenei.jena.entities.EffectivePredicateTest;
import org.xenei.jena.entities.ObjectHandler;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.handlers.EntityHandler;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;

public class CollectionValueHelper {

    private CollectionValueHelper() {
        // TODO Auto-generated constructor stub
    }
    
    public interface EnhancedPredicateInfo extends PredicateInfo {
        Class<?> getMethodArgType();
    }
    
    public static final String NAMESPACE = "http://localhost/test#";
    
    public static String getUri(String shortName) {
        return NAMESPACE+shortName.substring( 0,1 ).toLowerCase()+shortName.substring(1);
    }
    
    public static void assertSame(PredicateInfo expected, PredicateInfo actual) {
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
    
    public static final List<EnhancedPredicateInfo> createAllPredicateInfo() {

        List<EnhancedPredicateInfo> args = literalFunctions( new ArrayList<>(), "Bool", Boolean.class, Set.class);
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

        return args;
    }
    
    private static List<EnhancedPredicateInfo> literalFunctions(List<EnhancedPredicateInfo> args, String shortName, Class<?> valueType,
            Class<?> collectionType) {
        final TypeMapper typeMapper = TypeMapper.getInstance();
        final String namespace = CollectionValueHelper.getUri(shortName);
        args.add( makePredicateInfo( valueType, ActionType.SETTER, valueType, void.class, "add"+shortName,
                new LiteralHandler( typeMapper.getTypeByClass( valueType ) ), Collections.emptyList(), void.class,
                namespace, valueType ) );

        args.add( makePredicateInfo( collectionType, ActionType.GETTER, void.class, valueType, "get"+shortName,
                new LiteralHandler( typeMapper.getTypeByClass( valueType ) ), Collections.emptyList(), collectionType,
                namespace, collectionType ) );
        
        args.add( makePredicateInfo( valueType, ActionType.EXISTENTIAL, valueType, void.class, "has"+shortName,
                new LiteralHandler( typeMapper.getTypeByClass( Boolean.class ) ), Collections.emptyList(), Boolean.class,
                namespace, valueType ) );

        args.add( makePredicateInfo( valueType, ActionType.REMOVER, valueType, void.class, "remove"+shortName,
                new LiteralHandler( typeMapper.getTypeByClass( valueType ) ), Collections.emptyList(), void.class,
                namespace, valueType ) );
        return args;
    }
    
    private static List<EnhancedPredicateInfo> resourceFunctions(List<EnhancedPredicateInfo> args, String shortName, Class<?> valueType,
            Class<?> collectionType) {
        final String namespace = CollectionValueHelper.getUri(shortName);        
        args.add( makePredicateInfo( valueType, ActionType.SETTER, valueType, void.class, "add"+shortName,
                ResourceHandler.INSTANCE, Collections.emptyList(), void.class,
                namespace, valueType ) );

        args.add( makePredicateInfo( collectionType, ActionType.GETTER, void.class, valueType, "get"+shortName,
                ResourceHandler.INSTANCE, Collections.emptyList(), collectionType,
                namespace, collectionType ) );
        
        args.add( makePredicateInfo( valueType, ActionType.EXISTENTIAL, valueType, void.class, "has"+shortName,
                ResourceHandler.INSTANCE, Collections.emptyList(), Boolean.class,
                namespace, valueType ) );

        args.add( makePredicateInfo( valueType, ActionType.REMOVER, valueType, void.class, "remove"+shortName,
                ResourceHandler.INSTANCE, Collections.emptyList(), void.class,
                namespace, valueType ) );
        return args;
    }
    
    private static List<EnhancedPredicateInfo> uriFunctions(List<EnhancedPredicateInfo> args, String shortName, String collectionName, Class<?> collectionType) {
        final String namespace = CollectionValueHelper.getUri(shortName);
        args.add( makePredicateInfo( String.class, ActionType.SETTER, String.class, URI.class, "add"+shortName,
                UriHandler.INSTANCE, Collections.emptyList(), void.class,
                namespace, String.class ) );

        args.add( makePredicateInfo( collectionType, ActionType.GETTER, void.class, URI.class, "get"+collectionName,
                UriHandler.INSTANCE, Collections.emptyList(), collectionType,
                namespace, collectionType ) );
        
        args.add( makePredicateInfo( String.class, ActionType.EXISTENTIAL, URI.class, void.class, "has"+shortName,
                UriHandler.INSTANCE, Collections.emptyList(), Boolean.class,
                namespace, URI.class ) );

        args.add( makePredicateInfo( String.class, ActionType.REMOVER, URI.class, void.class, "remove"+shortName,
                UriHandler.INSTANCE, Collections.emptyList(), void.class,
                namespace, URI.class ) );
        return args;
    }
    
    private static List<EnhancedPredicateInfo> entFunctions(List<EnhancedPredicateInfo> args, String shortName, Class<?> valueType,
            Class<?> collectionType) {
        final String namespace = CollectionValueHelper.getUri(shortName);
        args.add( makePredicateInfo( valueType, ActionType.SETTER, valueType, void.class, "add"+shortName,
                new EntityHandler(valueType), Collections.emptyList(), void.class,
                namespace, valueType ) );

        args.add( makePredicateInfo( collectionType, ActionType.GETTER, void.class, valueType, "get"+shortName,
                new EntityHandler(valueType), Collections.emptyList(), collectionType,
                namespace, collectionType ) );
        
        args.add( makePredicateInfo( valueType, ActionType.EXISTENTIAL, valueType, void.class, "has"+shortName,
                new EntityHandler(valueType), Collections.emptyList(), Boolean.class,
                namespace, valueType ) );

        args.add( makePredicateInfo( valueType, ActionType.REMOVER, valueType, void.class, "remove"+shortName,
                new EntityHandler(valueType), Collections.emptyList(), void.class,
                namespace, valueType ) );
        return args;
    }
    


    public static EnhancedPredicateInfo makePredicateInfo(Class<?> methodArgType, ActionType actionType, Class<?> argumentType, Class<?> enclosedType,
            String methodName, ObjectHandler objectHandler, List<Method> postExec,
            Class<?> returnType, String uriString, Class<?> valueType){
        
        return new EnhancedPredicateInfo() {
            
            @Override
            public Class<?> getMethodArgType() {
                return methodArgType;
            }

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
            }
        };
    }
}
