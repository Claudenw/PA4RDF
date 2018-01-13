package org.xenei.jena.entities.impl.parser.impl;

import java.lang.reflect.Method;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.jena.entities.impl.TypeChecker;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.VoidHandler;
import org.xenei.jena.entities.impl.parser.AbstractMethodParserTest;
import org.xenei.jena.entities.testing.impl.SimpleSubjectImpl;

abstract public class AbstractSimpleImplTest extends AbstractMethodParserTest {



    protected final Method getter;
    protected final Method setter;
    protected final Method remover;
    protected final Method existential;

    protected final static LiteralHandler lh = new LiteralHandler( XSDDatatype.XSDstring );

    static {
        EntityManagerImpl.registerTypes();
    }

    protected AbstractSimpleImplTest(Class<? extends SimpleSubjectImpl> interfaceClass)
            throws NoSuchMethodException, SecurityException {
        super( interfaceClass );

        getter = interfaceClass.getMethod( "getX" );
        PIMap.put( getter, mockPredicateInfo( getter, "x", ActionType.GETTER, String.class, 0 ) );
        OMMap.put( getter, lh );

        setter = interfaceClass.getMethod( "setX", String.class );
        PIMap.put( setter, mockPredicateInfo( setter, "x", ActionType.SETTER, String.class, 0 ) );
        OMMap.put( setter, lh );

        remover = interfaceClass.getMethod( "removeX" );
        PIMap.put( remover, mockPredicateInfo( remover, "x", ActionType.REMOVER, Predicate.UNSET.class, 0 ) );
        OMMap.put( remover, VoidHandler.INSTANCE );

        existential = interfaceClass.getMethod( "hasX" );
        PIMap.put( existential, mockPredicateInfo( existential, "x", ActionType.EXISTENTIAL,
                TypeChecker.getPrimitiveClass( Boolean.class ), 0 ) );
        OMMap.put( existential, VoidHandler.INSTANCE );

        addCount.put( "setX", Integer.valueOf( 1 ) );

    }

    @Test
    public void testParseGetter() throws Exception {
        // getter changes output types
        OMMap.put( remover, lh );
        OMMap.put( existential, lh );
        final PredicateInfo predicateInfo = parser.parse( getter );
        assertSame( PIMap.get( getter ), predicateInfo, getter );
        assertSame( getter );
        assertSame( setter );
        assertSame( existential );
        assertSame( remover );

    }

    @Test
    public void testParseSetter() throws Exception {
        // getter changes output types
        OMMap.put( remover, lh );
        OMMap.put( existential, lh );
        final PredicateInfo predicateInfo = parser.parse( setter );
        assertSame( PIMap.get( setter ), predicateInfo, setter );
        assertSame( getter );
        assertSame( setter );
        assertSame( existential );
        assertSame( remover );
    }

    @Test
    public void testParseExistential() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( existential );
        assertSame( PIMap.get( existential ), predicateInfo, existential );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setter ) );
        assertSame( existential );
        Assert.assertNull( subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseRemover() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( remover );
        assertSame( PIMap.get( remover ), predicateInfo, remover );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( existential ) );
        assertSame( remover );
    }

    /*

    @Test
    public void testStandardGetter() throws Exception {
        final Method m = classUnderTest.getMethod( "getX" );
        final PredicateInfo pi = parser.parse( m );
        Assert.assertNotNull( "getX not parsed", pi );
        Assert.assertEquals( ActionType.GETTER, pi.getActionType() );
        Assert.assertEquals( "getX", pi.getMethodName() );
        Assert.assertEquals( "http://example.com/", pi.getNamespace() );
        Assert.assertEquals( "http://example.com/x", pi.getUriString() );
        Assert.assertEquals( String.class, pi.getValueClass() );
 //       checkContains( getGetAnnotations(), pi.getAnnotations() );
    }

    @Test
    public void testStandardHas() throws Exception {
        final Method m = classUnderTest.getMethod( "hasX" );
        final PredicateInfo pi = parser.parse( m );
        Assert.assertNotNull( "hasX not parsed", pi );
        Assert.assertEquals( ActionType.EXISTENTIAL, pi.getActionType() );
        Assert.assertEquals( "hasX", pi.getMethodName() );
        Assert.assertEquals( "http://example.com/", pi.getNamespace() );
        Assert.assertEquals( "http://example.com/x", pi.getUriString() );
        Assert.assertEquals( boolean.class, pi.getValueClass() );
//        checkContains( getHasAnnotations(), pi.getAnnotations() );

    }

    @Test
    public void testStandardRemove() throws Exception {
        final Method m = classUnderTest.getMethod( "removeX" );
        final PredicateInfo pi = parser.parse( m );
        Assert.assertNotNull( "removeX not parsed", pi );
        Assert.assertEquals( ActionType.REMOVER, pi.getActionType() );
        Assert.assertEquals( "removeX", pi.getMethodName() );
        Assert.assertEquals( "http://example.com/", pi.getNamespace() );
        Assert.assertEquals( "http://example.com/x", pi.getUriString() );
        Assert.assertEquals( null, pi.getValueClass() );
 //       checkContains( getRemoveAnnotations(), pi.getAnnotations() );

    }

    @Test
    public void testStandardSetter() throws Exception {
        final Method m = classUnderTest.getMethod( "setX", String.class );
        PredicateInfo pi = parser.parse( m );
        Assert.assertNotNull( "setX not parsed", pi );
        Assert.assertEquals( ActionType.SETTER, pi.getActionType() );
        Assert.assertEquals( "setX", pi.getMethodName() );
        Assert.assertEquals( "http://example.com/", pi.getNamespace() );
        Assert.assertEquals( "http://example.com/x", pi.getUriString() );
        Assert.assertEquals( String.class, pi.getValueClass() );
 //       checkContains( getSetAnnotations(), pi.getAnnotations() );

        pi = subjectInfo.getPredicateInfo( classUnderTest.getMethod( "getX" ) );
        Assert.assertNotNull( "getX not parsed", pi );
        Assert.assertEquals( ActionType.GETTER, pi.getActionType() );
        Assert.assertEquals( "getX", pi.getMethodName() );
        Assert.assertEquals( "http://example.com/", pi.getNamespace() );
        Assert.assertEquals( "http://example.com/x", pi.getUriString() );
        Assert.assertEquals( String.class, pi.getValueClass() );
//        checkContains( getGetAnnotations(), pi.getAnnotations() );

        pi = subjectInfo.getPredicateInfo( classUnderTest.getMethod( "hasX" ) );
        Assert.assertNotNull( "hasX not parsed", pi );
        Assert.assertEquals( ActionType.EXISTENTIAL, pi.getActionType() );
        Assert.assertEquals( "hasX", pi.getMethodName() );
        Assert.assertEquals( "http://example.com/", pi.getNamespace() );
        Assert.assertEquals( "http://example.com/x", pi.getUriString() );
        Assert.assertEquals( boolean.class, pi.getValueClass() );
//        checkContains( getHasAnnotations(), pi.getAnnotations() );

        pi = subjectInfo.getPredicateInfo( classUnderTest.getMethod( "removeX" ) );
        Assert.assertNotNull( "removeX not parsed", pi );
        Assert.assertEquals( ActionType.REMOVER, pi.getActionType() );
        Assert.assertEquals( "removeX", pi.getMethodName() );
        Assert.assertEquals( "http://example.com/", pi.getNamespace() );
        Assert.assertEquals( "http://example.com/x", pi.getUriString() );
        Assert.assertEquals( null, pi.getValueClass() );
//        checkContains( getRemoveAnnotations(), pi.getAnnotations() );

    }
     */
}
