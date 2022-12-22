package org.xenei.jena.entities.impl.method;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.jena.entities.EffectivePredicate;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.Action;
import org.xenei.jena.entities.impl.MethodParser;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.SubjectInfoImpl;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public class BaseMethodParserTest {

    static class ObservingStack extends Stack<Method> {
        private static final long serialVersionUID = 1L;
        public List<Method> seen = new ArrayList<>();

        @Override
        public Method push(final Method item) {
            seen.add( item );
            return super.push( item );
        }

    }

    ObservingStack parseStack;
    SubjectInfoImpl subjectInfo;
    Map<String, Integer> addCount;
    Logger log = LoggerFactory.getLogger( BaseMethodParser.class );
    BaseMethodParser baseMethodParser;

    void setup(final Class<?> clazz) {
        parseStack = new ObservingStack();
        subjectInfo = new SubjectInfoImpl( clazz );
        addCount = MethodParser.countAdders( clazz.getMethods() );
        baseMethodParser = new BaseMethodParser( parseStack, subjectInfo, addCount, log ) {
        };
    }

    @Test
    public void processAssociatedMethodsTest() throws Exception {
        setup( SimpleInterface.class );
        final Method method = SimpleInterface.class.getMethod( "getX" );
        final Action action = new Action( method );
        final PredicateInfo pi = new PredicateInfoImpl( new EffectivePredicate( method ), action );
        baseMethodParser.processAssociatedMethods( pi, action );
        Assertions.assertEquals( 4, parseStack.seen.size() );
    }

    @Test
    public void processAssociatedMethodsStackLimitTest() throws Exception {
        setup( SimpleInterface.class );
        Method method = SimpleInterface.class.getMethod( "removeX" );
        parseStack.add( method );
        method = SimpleInterface.class.getMethod( "getX" );
        final Action action = new Action( method );
        final PredicateInfo pi = new PredicateInfoImpl( new EffectivePredicate( method ), action );
        baseMethodParser.processAssociatedMethods( pi, action );
        Assertions.assertEquals( 3, parseStack.seen.size() );
    }

    @Test
    public void processAssociatedMethodsSubjectInfoLimitTest() throws Exception {
        setup( SimpleInterface.class );
        Method method = SimpleInterface.class.getMethod( "removeX" );
        PredicateInfo pi = new PredicateInfoImpl( new EffectivePredicate( method ), new Action( method ) );
        subjectInfo.add( method, pi );

        Assertions.assertNotNull( subjectInfo.getPredicateInfo( method ) );

        method = SimpleInterface.class.getMethod( "getX" );
        final Action action = new Action( method );
        pi = new PredicateInfoImpl( new EffectivePredicate( method ), action );
        baseMethodParser.processAssociatedMethods( pi, action );
        Assertions.assertEquals( 3, parseStack.seen.size() );
    }

    interface SetterMethods {
        void setX(String s);

        void addX(String s);

        void addX(int i);

        void removeX(String s);

        void addY(@URI String s);
    }

    @Test
    public void getSetterMethodsTest() throws Exception {
        setup( SetterMethods.class );
        List<Method> lst = baseMethodParser.getSetterMethods( "X" );
        Assertions.assertEquals( 3, lst.size() );
        lst = baseMethodParser.getSetterMethods( "x" );
        Assertions.assertEquals( 0, lst.size() );
    }

    @Test
    public void isMultiAddTest() {
        setup( SetterMethods.class );
        Assertions.assertTrue( baseMethodParser.isMultiAdd( "X" ) );
        Assertions.assertFalse( baseMethodParser.isMultiAdd( "Y" ) );
        Assertions.assertFalse( baseMethodParser.isMultiAdd( "x" ) );
        Assertions.assertFalse( baseMethodParser.isMultiAdd( "y" ) );
    }

}
