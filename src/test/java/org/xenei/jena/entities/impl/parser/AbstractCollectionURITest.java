package org.xenei.jena.entities.impl.parser;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;

public abstract class AbstractCollectionURITest extends BaseAbstractParserTest {

    protected AbstractCollectionURITest(final Class<?> classUnderTest) {
        super( classUnderTest );
    }

    @Test
    public void testStandardGetter() throws Exception {
        final Method m = classUnderTest.getMethod( "getU" );
        final PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );
        validatePredicateInfo( pi, "getU", ActionType.GETTER, "u", List.class );
        Assertions.assertEquals( ResourceHandler.class, pi.getObjectHandler().getClass() );
    }

    @Test
    public void testStandardGetter2() throws Exception {
        final Method m = classUnderTest.getMethod( "getU2" );
        final PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );
        validatePredicateInfo( pi, "getU2", ActionType.GETTER, "u2", ExtendedIterator.class );
        Assertions.assertEquals( ResourceHandler.class, pi.getObjectHandler().getClass() );
    }

    @Test
    public void testStandardHas() throws Exception {
        final Method m = classUnderTest.getMethod( "hasU", String.class );
        final PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );
        validatePredicateInfo( pi, "hasU", ActionType.EXISTENTIAL, "u", String.class );
        Assertions.assertEquals( UriHandler.class, pi.getObjectHandler().getClass() );
    }

    @Test
    public void testStandardHas2() throws Exception {
        final Method m = classUnderTest.getMethod( "hasU2", String.class );
        final PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );
        validatePredicateInfo( pi, "hasU2", ActionType.EXISTENTIAL, "u2", String.class );
        Assertions.assertEquals( UriHandler.class, pi.getObjectHandler().getClass() );
    }

    @Test
    public void testStandardRemove() throws Exception {
        final Method m = classUnderTest.getMethod( "removeU", String.class );
        final PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );
        validatePredicateInfo( pi, "removeU", ActionType.REMOVER, "u", String.class );
        Assertions.assertEquals( UriHandler.class, pi.getObjectHandler().getClass() );
    }

    @Test
    public void testStandardRemove2() throws Exception {
        final Method m = classUnderTest.getMethod( "removeU2", String.class );
        final PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );
        validatePredicateInfo( pi, "removeU2", ActionType.REMOVER, "u2", String.class );
        Assertions.assertEquals( UriHandler.class, pi.getObjectHandler().getClass() );
    }

    @Test
    public void testStandardSetter() throws Exception {
        final Method m = classUnderTest.getMethod( "addU", String.class );
        PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );
        validatePredicateInfo( pi, "addU", ActionType.SETTER, "u", String.class );
        Assertions.assertEquals( UriHandler.class, pi.getObjectHandler().getClass() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( classUnderTest.getMethod( "addU", RDFNode.class ) );
        validatePredicateInfo( pi, "addU", ActionType.SETTER, "u", RDFNode.class );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( classUnderTest.getMethod( "getU" ) );
        validatePredicateInfo( pi, "getU", ActionType.GETTER, "u", List.class );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( classUnderTest.getMethod( "hasU", String.class ) );
        validatePredicateInfo( pi, "hasU", ActionType.EXISTENTIAL, "u", String.class );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( classUnderTest.getMethod( "removeU", String.class ) );
        validatePredicateInfo( pi, "removeU", ActionType.REMOVER, "u", String.class );
    }

    @Test
    public void testStandardSetter2() throws Exception {
        final Method m = classUnderTest.getMethod( "addU2", RDFNode.class );
        PredicateInfoImpl pi = (PredicateInfoImpl) parser.parse( m );
        validatePredicateInfo( pi, "addU2", ActionType.SETTER, "u2", RDFNode.class );
        Assertions.assertEquals( ResourceHandler.class, pi.getObjectHandler().getClass() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( classUnderTest.getMethod( "addU2", String.class ) );
        validatePredicateInfo( pi, "addU2", ActionType.SETTER, "u2", String.class );
        Assertions.assertEquals( UriHandler.class, pi.getObjectHandler().getClass() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( classUnderTest.getMethod( "getU2" ) );
        validatePredicateInfo( pi, "getU2", ActionType.GETTER, "u2", ExtendedIterator.class );
        Assertions.assertEquals( ResourceHandler.class, pi.getObjectHandler().getClass() );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( classUnderTest.getMethod( "hasU2", String.class ) );
        validatePredicateInfo( pi, "hasU2", ActionType.EXISTENTIAL, "u2", String.class );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( classUnderTest.getMethod( "removeU2", RDFNode.class ) );
        validatePredicateInfo( pi, "removeU2", ActionType.REMOVER, "u2", RDFNode.class );

        pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( classUnderTest.getMethod( "removeU2", String.class ) );
        validatePredicateInfo( pi, "removeU2", ActionType.REMOVER, "u2", String.class );
        Assertions.assertEquals( UriHandler.class, pi.getObjectHandler().getClass() );
    }
}
