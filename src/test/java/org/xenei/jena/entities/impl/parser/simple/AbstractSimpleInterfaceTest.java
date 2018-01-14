package org.xenei.jena.entities.impl.parser.simple;

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
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public abstract class AbstractSimpleInterfaceTest extends AbstractMethodParserTest {

    protected final Method getter;
    protected final Method setter;
    protected final Method remover;
    protected final Method existential;

    protected final static LiteralHandler lh = new LiteralHandler( XSDDatatype.XSDstring );

    static {
        EntityManagerImpl.registerTypes();
    }

    protected AbstractSimpleInterfaceTest(Class<?> interfaceClass)
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

}
