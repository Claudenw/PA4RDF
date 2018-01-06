package org.xenei.jena.entities.impl;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.impl.handlers.ListHandler;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.testing.iface.SimpleURICollectionInterface;

public abstract class AbstractSimpleURICollectionTest extends AbstractMethodParserTest {

 
    protected final Method getter;
    protected final Method setterS;
    protected final Method setterR;
    protected final Method remover;
    protected final Method existential;

    protected final Method getter2;
    protected final Method setterR2;
    protected final Method setterS2;
    protected final Method removerS2;
    protected final Method removerR2;
    protected final Method existential2;

    protected AbstractSimpleURICollectionTest(Class<? extends SimpleURICollectionInterface> interfaceClass)
            throws NoSuchMethodException, SecurityException {
        super( interfaceClass );
        EffectivePredicate pred;
        getter = SimpleURICollectionInterface.class.getMethod( "getU" );
        PIMap.put( getter, mockPredicateInfo( getter, "u", ActionType.GETTER, List.class, 1, 0 ) );
        OMMap.put( getter, new ListHandler(ResourceHandler.INSTANCE, List.class) );
        
        setterR = SimpleURICollectionInterface.class.getMethod( "addU", RDFNode.class );
        PIMap.put( setterR, mockPredicateInfo( setterR, "u", ActionType.SETTER, RDFNode.class, 0, 0 ) );
        OMMap.put( setterR, ResourceHandler.INSTANCE );
        
        setterS = SimpleURICollectionInterface.class.getMethod( "addU", String.class );
        PIMap.put( setterS, mockPredicateInfo( setterS, "u", ActionType.SETTER, String.class, 1, 0 ) );
        OMMap.put( setterS, UriHandler.INSTANCE );
        
        remover = SimpleURICollectionInterface.class.getMethod( "removeU", String.class );
        PIMap.put( remover, mockPredicateInfo( remover, "u", ActionType.REMOVER, String.class, 0, 0 ) );
        OMMap.put( remover, UriHandler.INSTANCE );
 
        existential = SimpleURICollectionInterface.class.getMethod( "hasU", String.class );
        PIMap.put( existential, mockPredicateInfo( existential, "u", ActionType.EXISTENTIAL, String.class, 0, 0 ) );
        OMMap.put( existential, UriHandler.INSTANCE );

        getter2 = SimpleURICollectionInterface.class.getMethod( "getU2" );
        PIMap.put( getter2, mockPredicateInfo( getter2, "u2", ActionType.GETTER, ExtendedIterator.class, 1, 0 ) );
        OMMap.put( getter2, new ListHandler( ResourceHandler.INSTANCE, ExtendedIterator.class) );
        
        setterR2 = SimpleURICollectionInterface.class.getMethod( "addU2", RDFNode.class );
        PIMap.put( setterR2, mockPredicateInfo( setterR2, "u2", ActionType.SETTER, RDFNode.class, 0, 0 ) );
        OMMap.put( setterR2, ResourceHandler.INSTANCE );
        
        setterS2 = SimpleURICollectionInterface.class.getMethod( "addU2", String.class );
        PIMap.put( setterS2, mockPredicateInfo( setterS2, "u2", ActionType.SETTER, String.class, 0, 0 ) );
        OMMap.put( setterS2, UriHandler.INSTANCE );
        
        removerS2 = SimpleURICollectionInterface.class.getMethod( "removeU2", String.class );
        PIMap.put( removerS2, mockPredicateInfo( removerS2, "u2", ActionType.REMOVER, String.class, 0, 0 ) );
        OMMap.put( removerS2, UriHandler.INSTANCE );

        removerR2 = SimpleURICollectionInterface.class.getMethod( "removeU2", RDFNode.class );
        PIMap.put( removerR2, mockPredicateInfo( removerR2, "u2", ActionType.REMOVER, RDFNode.class, 0, 0 ) );
        OMMap.put( removerR2, ResourceHandler.INSTANCE );

        existential2 = SimpleURICollectionInterface.class.getMethod( "hasU2", String.class );
        PIMap.put( existential2, mockPredicateInfo( existential2, "u2", ActionType.EXISTENTIAL, String.class, 0, 0 ) );
        OMMap.put( existential2, UriHandler.INSTANCE );
        
        addCount.put( "addU", Integer.valueOf( 2 ) );
        addCount.put( "addU2", Integer.valueOf( 2 ) );
       
    }

    @Test
    public void testParseGetter() throws Exception {

        final PredicateInfo predicateInfo = parser.parse( getter );
        assertSame( PIMap.get( getter ), predicateInfo,  getter );
        assertSame( getter );
        assertSame( setterS  );
        assertSame(  setterR );
        assertSame(  existential );
        assertSame(  remover );
    }

    @Test
    public void testParseSetterS() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( setterS );
        assertSame( PIMap.get( setterS ), predicateInfo, setterS );
        assertSame( getter );
        assertSame( setterS  );
        assertSame(  setterR );
        assertSame(  existential );
        assertSame(  remover );
    }

    @Test
    public void testParseSetterR() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( setterR );
        assertSame( PIMap.get( setterR ), predicateInfo, setterR );
        assertSame( getter );
        assertSame( setterS  );
        assertSame(  setterR );
        assertSame(  existential );
        assertSame(  remover );
    }

    @Test
    public void testParseExistential() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( existential );
        assertSame( PIMap.get( existential ), predicateInfo, existential );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterS ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterR ) );
        assertSame(  existential  );
        Assert.assertNull( subjectInfo.getPredicateInfo( remover ) );
    }

    @Test
    public void testParseRemover() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( remover );
        assertSame( PIMap.get( remover ), predicateInfo, remover );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterS ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterR ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( existential ) );
        assertSame( remover  );
    }

    @Test
    public void testParseGetter2() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getter2 );
        assertSame( PIMap.get( getter2 ), predicateInfo, getter2 );
        assertSame(  getter2  );
        assertSame(  setterR2  );
        assertSame( setterS2  );
        assertSame(  existential2  );
        assertSame( removerR2  );
        assertSame(  removerS2  );
    }

    @Test
    public void testParseSetterR2() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( setterR2 );
        assertSame( PIMap.get( setterR2 ), predicateInfo, setterR2 );
        assertSame(  getter2  );
        assertSame(  setterR2  );
        assertSame( setterS2  );
        assertSame(  existential2  );
        assertSame( removerR2  );
        assertSame(  removerS2  );
    }

    @Test
    public void testParseSetterS2() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( setterS2 );
        assertSame( PIMap.get( setterS2 ), predicateInfo, setterS2 );
        assertSame(  getter2  );
        assertSame(  setterR2  );
        assertSame( setterS2  );
        assertSame(  existential2  );
        assertSame( removerR2  );
        assertSame(  removerS2  );

    }

    @Test
    public void testParseExistential2() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( existential2 );
        assertSame( PIMap.get( existential2 ), predicateInfo, existential2 );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter2 ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterR2 ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterS2 ) );
        assertSame(  existential2 );
        Assert.assertNull( subjectInfo.getPredicateInfo( removerR2 ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( removerS2 ) );
    }

    @Test
    public void testParseRemoverR2() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removerR2 );
        assertSame( PIMap.get( removerR2 ), predicateInfo, removerR2 );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter2 ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterR2 ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterS2 ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( existential2 ) );
        assertSame( removerR2 );
        Assert.assertNull( subjectInfo.getPredicateInfo( removerS2 ) );
    }

    @Test
    public void testParseRemoverS2() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( removerS2 );
        assertSame( PIMap.get( removerS2 ), predicateInfo, removerS2 );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter2 ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterR2 ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterS2 ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( existential2 ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( removerR2 ) );
        assertSame( removerS2  );
    }

}
