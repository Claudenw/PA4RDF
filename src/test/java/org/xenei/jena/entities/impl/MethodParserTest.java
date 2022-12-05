package org.xenei.jena.entities.impl;

import java.util.Map;
import java.util.TreeMap;

import org.apache.jena.datatypes.xsd.impl.XSDBaseNumericType;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.ObjectHandler;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;

public class MethodParserTest {

    @BeforeAll
    public static void setupClass() {
        EntityManagerFactory.setEntityManager( Mockito.mock( EntityManager.class ) );
    }

    @AfterAll
    public static void teardownClass() {
        EntityManagerFactory.setEntityManager( null );
    }

    private final SubjectInfoImpl subjectInfo = new SubjectInfoImpl( ImplementedClass.class );
    private final Map<String, Integer> addCount = new TreeMap<>();

    private final MethodParser methodParser = new MethodParser( subjectInfo, addCount );

    private void assertValues(final PredicateInfo pi, final ActionType actionType, final Class<?> concreteType,
            final String methodName, final ObjectHandler handler, final Property property, final Class<?> valueClass) {
        Assertions.assertEquals( actionType, pi.getActionType() );
        Assertions.assertEquals( methodName, pi.getMethodName() );
        Assertions.assertEquals( handler.getClass(), pi.getObjectHandler().getClass() );
        Assertions.assertEquals( handler.toString(), pi.getObjectHandler().toString() );
        Assertions.assertEquals( property, pi.getProperty() );
        Assertions.assertEquals( valueClass, pi.getValueClass() );
    }

    @Test
    public void parseTest() throws NoSuchMethodException, SecurityException, MissingAnnotation {
        final PredicateInfo pi = methodParser.parse( ImplementedClass.class.getDeclaredMethod( "getX" ) );
        assertValues( pi, ActionType.GETTER, int.class, "getX",
                new LiteralHandler( new XSDBaseNumericType( "int", Integer.class ) ),
                ResourceFactory.createProperty( "x" ), int.class );
        Assertions.fail( "Incomplete" );
    }

    interface ImplementedClass {
        int getX();
    }
}
