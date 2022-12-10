package org.xenei.jena.entities;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.jena.rdf.model.RDFNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;

public class EffectivePredicateTest {

    public static void assertValues(final EffectivePredicate ep, final boolean emptyIsNull, final boolean impl,
            final String literalType, final String name, final String namespace, final Class<?> rawType,
            final boolean upcase, final List<Method> postExec) {
        Assertions.assertEquals( emptyIsNull, ep.emptyIsNull(), "emptyIsNull error" );
        Assertions.assertEquals( impl, ep.impl(), "impl error" );
        Assertions.assertEquals( literalType, ep.literalType(), "litealType error" );
        Assertions.assertEquals( name, ep.name(), "nameError" );
        Assertions.assertEquals( namespace, ep.namespace(), "namespace error" );
        Assertions.assertEquals( rawType, ep.rawType(), "rawType error" );
        Assertions.assertEquals( upcase, ep.upcase(), "upcase error" );
        Assertions.assertEquals( postExec, ep.postExec(), "postExec error" );
    }

    /**
     *
     * @param ep
     * @param emptyIsNull
     * @param impl
     * @param literalType
     * @param name
     * @param namespace
     * @param rawtype
     * @param upcase
     */
    public static void assertValues(final EffectivePredicate ep, final boolean emptyIsNull, final boolean impl,
            final String literalType, final String name, final String namespace, final Class<?> rawType,
            final boolean upcase) {
        EffectivePredicateTest.assertValues( ep, emptyIsNull, impl, literalType, name, namespace, rawType, upcase,
                Collections.emptyList() );
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
    public void mergePredicateTest() {
        final EffectivePredicate ep = new EffectivePredicate();

        ep.merge( makePred( false, false, "", "", "http://example.com", "", null, false ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "", "http://example.com", null, false );

        ep.merge( makePred( false, false, "", "", "", "", Integer.class, false ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "", "http://example.com", Integer.class, false );

        ep.merge( makePred( true, false, "", "", "", "", null, false ) );
        EffectivePredicateTest.assertValues( ep, true, false, "", "", "http://example.com", Integer.class, false );

        ep.merge( makePred( false, true, "", "", "", "", null, false ) );
        EffectivePredicateTest.assertValues( ep, false, true, "", "", "http://example.com", Integer.class, false );

        ep.merge( makePred( false, false, "", "name", "", "", null, false ) );
        EffectivePredicateTest.assertValues( ep, false, true, "", "name", "http://example.com", Integer.class, false );

        ep.merge( makePred( false, false, "", "", "", "", null, true ) );
        EffectivePredicateTest.assertValues( ep, false, true, "", "Name", "http://example.com", Integer.class, true );

        ep.merge( makePred( false, false, "", "", "", "SomeMethod", null, false ) );
        EffectivePredicateTest.assertValues( ep, false, true, "", "name", "http://example.com", Integer.class, false );
    }

    @Test
    public void mergeEffectivePredicateTest() {
        EffectivePredicate tempEp;
        final EffectivePredicate ep = new EffectivePredicate();

        tempEp = new EffectivePredicate( makePred( false, false, "", "", "http://example.com", "", null, false ) );
        ep.merge( tempEp );
        EffectivePredicateTest.assertValues( ep, false, false, "", "", "http://example.com", null, false );

        tempEp = new EffectivePredicate( makePred( false, false, "", "", "", "", Integer.class, false ) );
        ep.merge( tempEp );
        EffectivePredicateTest.assertValues( ep, false, false, "", "", "http://example.com", Integer.class, false );

        tempEp = new EffectivePredicate( makePred( true, false, "", "", "", "", null, false ) );
        ep.merge( tempEp );
        EffectivePredicateTest.assertValues( ep, false, false, "", "", "http://example.com", Integer.class, false );

        tempEp = new EffectivePredicate( makePred( false, true, "", "", "", "", null, false ) );
        ep.merge( tempEp );
        EffectivePredicateTest.assertValues( ep, false, true, "", "", "http://example.com", Integer.class, false );

        tempEp = new EffectivePredicate( makePred( false, false, "", "name", "", "", null, false ) );
        ep.merge( tempEp );
        EffectivePredicateTest.assertValues( ep, false, true, "", "name", "http://example.com", Integer.class, false );

        tempEp = new EffectivePredicate( makePred( false, false, "", "", "", "", null, true ) );
        ep.merge( tempEp );
        EffectivePredicateTest.assertValues( ep, false, true, "", "Name", "http://example.com", Integer.class, true );

        tempEp = new EffectivePredicate( makePred( false, false, "", "", "", "SomeMethod", null, false ) );
        ep.merge( tempEp );
        EffectivePredicateTest.assertValues( ep, false, true, "", "name", "http://example.com", Integer.class, false );
    }

    @Test
    public void constructorFromMethodTests() throws Exception {
        EffectivePredicate ep = new EffectivePredicate( method( "getSimple" ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "simple", "", int.class, false );

        ep = new EffectivePredicate( method( "getEmptyIsNull" ) );
        EffectivePredicateTest.assertValues( ep, true, false, "", "emptyIsNull", "", boolean.class, false );

        ep = new EffectivePredicate( method( "getLiteralType" ) );
        EffectivePredicateTest.assertValues( ep, false, false, "Integer.class", "literalType", "", int.class,
                false );

        ep = new EffectivePredicate( method( "getName" ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "foo", "", int.class, false );

        ep = new EffectivePredicate( method( "getNamespace" ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "namespace", "http://example.com/", int.class,
                false );

        ep = new EffectivePredicate( method( "getPostExec" ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "postExec", "", int.class, false,
                Arrays.asList( TestingInterface.class.getMethod( "postExecFunc", int.class ) ) );

        ep = new EffectivePredicate( method( "getType" ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "type", "", Integer.class, false );

        ep = new EffectivePredicate( method( "getUpcase" ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "Upcase", "", int.class, true );

        ep = new EffectivePredicate( method( "isImpl" ) );
        EffectivePredicateTest.assertValues( ep, false, true, "", "impl", "", null, false );

        ep = new EffectivePredicate( TestingInterface.class.getMethod( "setUriParam", String.class ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "uriParam", "", URI.class, false );

        // with Subject
        ep = new EffectivePredicate( TestingInterface2.class.getMethod( "getSimple" ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "simple", "http://example.net", int.class,
                false );

        ep = new EffectivePredicate( TestingInterface2.class.getMethod( "getNamespace" ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "namespace", "http://example.com/", int.class,
                false );
    }

    @Test
    public void constructorFromPredicateTests() throws Exception {
        EffectivePredicate ep = new EffectivePredicate(
                makePred( false, false, "", "", "http://example.com", "", null, false ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "", "http://example.com", null, false );

        ep = new EffectivePredicate( makePred( false, false, "", "", "", "", Integer.class, false ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "", "", Integer.class, false );

        ep = new EffectivePredicate( makePred( true, false, "", "", "", "", null, false ) );
        EffectivePredicateTest.assertValues( ep, true, false, "", "", "", null, false );

        ep = new EffectivePredicate( makePred( false, true, "", "", "", "", null, false ) );
        EffectivePredicateTest.assertValues( ep, false, true, "", "", "", null, false );

        ep = new EffectivePredicate( makePred( false, false, "", "name", "", "", null, false ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "name", "", null, false );

        ep = new EffectivePredicate( makePred( false, false, "", "", "", "", null, true ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "", "", null, true );

        ep = new EffectivePredicate( makePred( false, false, "", "", "", "SomeMethod", null, false ) );
        EffectivePredicateTest.assertValues( ep, false, false, "", "", "", null, false );
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

        int setUriParam(@URI String uri);
    }

    @Subject(namespace = "http://example.net")
    interface TestingInterface2 {

        int getSimple();

        @Predicate(namespace = "http://example.com/")
        int getNamespace();
    }
}
