package org.xenei.jena.entities.impl.parser;

import java.lang.reflect.Method;
import java.util.List;

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
import org.xenei.jena.entities.testing.iface.CollectionInterface;

public abstract class AbstractCollectionInterfaceTest extends AbstractMethodParserTest {
    protected final Method getter;
    protected final Method setter;
    protected final Method remover;
    protected final Method existential;

    protected final static LiteralHandler lh = new LiteralHandler( XSDDatatype.XSDstring );

    static {
        EntityManagerImpl.registerTypes();
    }

    protected AbstractCollectionInterfaceTest(final Class<? extends CollectionInterface> interfaceClass) throws NoSuchMethodException, SecurityException {
        super( interfaceClass );
        
        getter = interfaceClass.getMethod( "getX" );
        PIMap.put( getter, mockPredicateInfo( getter, "x", ActionType.GETTER, List.class, 0 ) );
        OMMap.put( getter, lh );

        setter = interfaceClass.getMethod( "addX", String.class );
        PIMap.put( setter, mockPredicateInfo( setter, "x", ActionType.SETTER, String.class, 0 ) );
        OMMap.put( setter, lh );

        remover = interfaceClass.getMethod( "removeX", String.class );
        PIMap.put( remover, mockPredicateInfo( remover, "x", ActionType.REMOVER, String.class, 0 ) );
        OMMap.put( remover, lh );

        existential = interfaceClass.getMethod( "hasX", String.class );
        PIMap.put( existential, mockPredicateInfo( existential, "x", ActionType.EXISTENTIAL,
                TypeChecker.getPrimitiveClass( Boolean.class ), 0 ) );
        OMMap.put( existential, lh );

        addCount.put( "addX", Integer.valueOf( 1 ) );
    }
    
    @Test
    public void testParseGetter() throws Exception {
        // getter changes output types
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
