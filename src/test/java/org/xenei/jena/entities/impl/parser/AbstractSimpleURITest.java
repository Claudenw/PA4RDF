package org.xenei.jena.entities.impl.parser;

import org.apache.jena.rdf.model.RDFNode;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;

public abstract class AbstractSimpleURITest extends BaseAbstractParserTest {
    protected AbstractSimpleURITest(final Class<?> classUnderTest) {
        super( classUnderTest );
    }

    @Test
    public void testStandardGetter() throws Exception {
        final Method m = classUnderTest.getMethod( "getU" );
        final PredicateInfo pi = parser.parse( m );
        validatePredicateInfo( pi, "getU", ActionType.GETTER, "u", RDFNode.class );
    }

    @Test
    public void testStandardHas() throws Exception {
        final Method m = classUnderTest.getMethod( "hasU" );
        final PredicateInfo pi = parser.parse( m );
        validatePredicateInfo( pi, "hasU", ActionType.EXISTENTIAL, "u", boolean.class );
    }

    @Test
    public void testStandardRemove() throws Exception {
        final Method m = classUnderTest.getMethod( "removeU" );
        final PredicateInfo pi = parser.parse( m );
        validatePredicateInfo( pi, "removeU", ActionType.REMOVER, "u", null );
    }

    @Test
    public void testStandardSetter() throws Exception {
        final Method m = classUnderTest.getMethod( "setU", String.class );
        PredicateInfo pi = parser.parse( m );
        validatePredicateInfo( pi, "setU", ActionType.SETTER, "u", String.class );
        Assertions.assertEquals( UriHandler.class, ((PredicateInfoImpl) pi).getObjectHandler().getClass() );

        pi = subjectInfo.getPredicateInfo( classUnderTest.getMethod( "setU", RDFNode.class ) );
        validatePredicateInfo( pi, "setU", ActionType.SETTER, "u", RDFNode.class );

        pi = subjectInfo.getPredicateInfo( classUnderTest.getMethod( "getU" ) );
        validatePredicateInfo( pi, "getU", ActionType.GETTER, "u", RDFNode.class );
        Assertions.assertEquals( ResourceHandler.class, ((PredicateInfoImpl) pi).getObjectHandler().getClass() );

        pi = subjectInfo.getPredicateInfo( classUnderTest.getMethod( "hasU" ) );
        validatePredicateInfo( pi, "hasU", ActionType.EXISTENTIAL, "u", boolean.class );

        pi = subjectInfo.getPredicateInfo( classUnderTest.getMethod( "removeU" ) );
        validatePredicateInfo( pi, "removeU", ActionType.REMOVER, "u", null );
    }
}
