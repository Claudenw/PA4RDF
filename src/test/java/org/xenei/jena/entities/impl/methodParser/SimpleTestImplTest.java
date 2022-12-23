package org.xenei.jena.entities.impl.methodParser;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.EffectivePredicateTest;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.PredicateInfoImplTest;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.MethodParser;
import org.xenei.jena.entities.impl.SubjectInfoImpl;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.testing.tClass.SimpleTestImpl;

public class SimpleTestImplTest {
    private Model model;
    public SimpleTestImpl impl;

    final String namespace = "http://example.com/";
    final SubjectInfoImpl subjectInfo = new SubjectInfoImpl( SimpleTestImpl.class );
    final MethodParser methodParser = new MethodParser( subjectInfo );

    @Test
    public void implParsingTest() throws Exception {
        final Property expectedProperty = ResourceFactory.createProperty( namespace, "x" );
        final Method method = SimpleTestImpl.class.getMethod( "setX", String.class );
        final PredicateInfo pi = methodParser.parse( method );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( SimpleTestImpl.class.getMethod( "removeX" ) ) );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( SimpleTestImpl.class.getMethod( "getX" ) ) );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( SimpleTestImpl.class.getMethod( "hasX" ) ) );
        final String expectedHandler = PredicateInfoImplTest.createHandler( "string", String.class.toString() );
        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "setX", String.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), false, true, "", "x", namespace, String.class, false );
    }

    @Test
    public void x() throws Exception {
        System.out.println( SimpleTestImpl.class.getAnnotation( Subject.class ) );
        for (final AnnotatedType o : SimpleTestImpl.class.getAnnotatedInterfaces()) {

            System.out.println( o.getType() );
        }
        System.out.format( "Annotated Superclass %s", SimpleTestImpl.class.getAnnotatedSuperclass().getType() );
    }

}
