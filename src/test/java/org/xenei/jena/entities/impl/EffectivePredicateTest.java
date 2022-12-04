package org.xenei.jena.entities.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.jena.rdf.model.RDFNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;

public class EffectivePredicateTest {

    private void assertValues(final EffectivePredicate ep, final boolean emptyIsNull, final boolean impl,
            final String literalType, final String name, final String namespace, final Class<?> type,
            final boolean upcase, final List<Method> postExec) {
        Assertions.assertEquals( emptyIsNull, ep.emptyIsNull() );
        Assertions.assertEquals( impl, ep.impl() );
        Assertions.assertEquals( literalType, ep.literalType() );
        Assertions.assertEquals( name, ep.name() );
        Assertions.assertEquals( namespace, ep.namespace() );
        Assertions.assertEquals( type, ep.type() );
        Assertions.assertEquals( upcase, ep.upcase() );
        Assertions.assertEquals( postExec, ep.postExec() );
    }

    private void assertValues(final EffectivePredicate ep, final boolean emptyIsNull, final boolean impl,
            final String literalType, final String name, final String namespace, final Class<?> type,
            final boolean upcase) {
        assertValues( ep, emptyIsNull, impl, literalType, name, namespace, type, upcase, Collections.emptyList() );
    }

    private Predicate makePred(final boolean emptyIsNull, final boolean impl, final String literalType,
            final String name, final String namespace, final String postExec, final Class<?> type,
            final boolean upcase) {
        final Predicate pred = Mockito.mock( Predicate.class );
        Mockito.when( pred.emptyIsNull() ).thenReturn( emptyIsNull );
        Mockito.when( pred.impl() ).thenReturn( impl );
        Mockito.when( pred.literalType() ).thenReturn( literalType );
        Mockito.when( pred.name() ).thenReturn( name );
        Mockito.when( pred.namespace() ).thenReturn( namespace );
        Mockito.when( pred.postExec() ).thenReturn( postExec );
        Mockito.doReturn( type == null ? RDFNode.class : type ).when( pred ).type();
        Mockito.when( pred.upcase() ).thenReturn( upcase );
        return pred;
    }

    private Method method(final String name) throws NoSuchMethodException, SecurityException {
        return TestingInterface.class.getMethod( name );
    }

    @Test
    public void mergeTest() {
        final EffectivePredicate ep = new EffectivePredicate();

        ep.merge( makePred( false, false, "", "", "http://example.com", "", null, false ) );
        assertValues( ep, false, false, "", "", "http://example.com", RDFNode.class, false );

        ep.merge( makePred( false, false, "", "", "", "", Integer.class, false ) );
        assertValues( ep, false, false, "", "", "http://example.com", Integer.class, false );

        ep.merge( makePred( true, false, "", "", "", "", null, false ) );
        assertValues( ep, true, false, "", "", "http://example.com", Integer.class, false );

        ep.merge( makePred( false, true, "", "", "", "", null, false ) );
        assertValues( ep, false, true, "", "", "http://example.com", Integer.class, false );

        ep.merge( makePred( false, false, "", "name", "", "", null, false ) );
        assertValues( ep, false, true, "", "name", "http://example.com", Integer.class, false );

        ep.merge( makePred( false, false, "", "", "", "", null, true ) );
        assertValues( ep, false, true, "", "Name", "http://example.com", Integer.class, true );

        ep.merge( makePred( false, false, "", "", "", "SomeMethod", null, false ) );
        assertValues( ep, false, true, "", "name", "http://example.com", Integer.class, false );
    }

    @Test
    public void constructorFromMethodTests() throws Exception {
        EffectivePredicate ep = new EffectivePredicate( method( "getSimple" ) );
        assertValues( ep, true, false, "", "simple", "", RDFNode.class, false );

        ep = new EffectivePredicate( method( "getEmptyIsNull" ) );
        assertValues( ep, true, false, "", "emptyIsNull", "", RDFNode.class, false );

        ep = new EffectivePredicate( method( "getLiteralType" ) );
        assertValues( ep, false, false, "Integer.class", "literalType", "", RDFNode.class, false );

        ep = new EffectivePredicate( method( "getName" ) );
        assertValues( ep, false, false, "", "foo", "", RDFNode.class, false );

        ep = new EffectivePredicate( method( "getNamespace" ) );
        assertValues( ep, false, false, "", "namespace", "http://example.com/", RDFNode.class, false );

        ep = new EffectivePredicate( method( "getPostExec" ) );
        assertValues( ep, false, false, "", "postExec", "", RDFNode.class, false,
                Arrays.asList( TestingInterface.class.getMethod( "postExecFunc", int.class ) ) );

        ep = new EffectivePredicate( method( "getType" ) );
        assertValues( ep, false, false, "", "type", "", Integer.class, false );

        ep = new EffectivePredicate( method( "getUpcase" ) );
        assertValues( ep, false, false, "", "Upcase", "", RDFNode.class, true );

        ep = new EffectivePredicate( method( "isImpl" ) );
        assertValues( ep, false, true, "", "impl", "", RDFNode.class, false );

        ep = new EffectivePredicate( TestingInterface.class.getMethod( "getUriParam", String.class ) );
        assertValues( ep, true, false, "", "uriParam", "", URI.class, false );

    }
    @Test
    public void constructorFromPredicateTests() throws Exception {
        EffectivePredicate ep = new EffectivePredicate( makePred( false, false, "", "", "http://example.com", "", null, false ) );
        assertValues( ep, false, false, "", "", "http://example.com", RDFNode.class, false );

        ep = new EffectivePredicate( makePred( false, false, "", "", "", "", Integer.class, false ) );
        assertValues( ep, false, false, "", "", "", Integer.class, false );

        ep = new EffectivePredicate( makePred( true, false, "", "", "", "", null, false ) );
        assertValues( ep, true, false, "", "", "", RDFNode.class, false );

        ep = new EffectivePredicate( makePred( false, true, "", "", "", "", null, false ) );
        assertValues( ep, false, true, "", "", "", RDFNode.class, false );

        ep = new EffectivePredicate( makePred( false, false, "", "name", "", "", null, false ) );
        assertValues( ep, false, false, "", "name", "", RDFNode.class, false );

        ep = new EffectivePredicate( makePred( false, false, "", "", "", "", null, true ) );
        assertValues( ep, false, false, "", "", "", RDFNode.class, true );

        ep = new EffectivePredicate( makePred( false, false, "", "", "", "SomeMethod", null, false ) );
        assertValues( ep, false, false, "", "", "", RDFNode.class, false );
    }

    interface TestingInterface {
        int getSimple();

        @Predicate(emptyIsNull = true)
        boolean getEmptyIsNull();

        @Predicate(impl = true)
        default boolean isImpl() {
            return true;
        }

        @Predicate(literalType = "Integer.class")
        int getLiteralType();

        @Predicate(name = "foo")
        int getName();

        @Predicate(namespace = "http://example.com/")
        int getNamespace();

        @Predicate(postExec = "postExecFunc")
        int getPostExec();

        int postExecFunc(int i);

        @Predicate(type = Integer.class)
        int getType();

        @Predicate(upcase = true)
        int getUpcase();

        int getUriParam(@URI String uri);

    }
}
