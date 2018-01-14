package org.xenei.jena.entities.impl.parser.simpleURI;

import java.lang.reflect.Method;

import org.apache.jena.rdf.model.RDFNode;
import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.jena.entities.impl.TypeChecker;
import org.xenei.jena.entities.impl.handlers.ResourceHandler;
import org.xenei.jena.entities.impl.handlers.UriHandler;
import org.xenei.jena.entities.impl.handlers.VoidHandler;
import org.xenei.jena.entities.impl.parser.AbstractMethodParserTest;

public abstract class AbstractSimpleURIInterfaceTest extends AbstractMethodParserTest {

    protected final Method getter;
    protected final Method setterS;
    protected final Method setterR;
    protected final Method remover;
    protected final Method existential;

    /*
     * setter string is annotated with predicate 
     */
    protected AbstractSimpleURIInterfaceTest(Class<?> interfaceClass) throws NoSuchMethodException, SecurityException {
        super( interfaceClass );

        getter = interfaceClass.getMethod( "getU" );
        PIMap.put( getter, mockPredicateInfo( getter, "u", ActionType.GETTER, RDFNode.class, 0 ) );
       
        setterR = interfaceClass.getMethod( "setU", RDFNode.class );
        PIMap.put( setterR, mockPredicateInfo( setterR, "u", ActionType.SETTER, RDFNode.class, 0 ) );
       
        setterS = interfaceClass.getMethod( "setU", String.class );
        PIMap.put( setterS, mockPredicateInfo( setterS, "u", ActionType.SETTER, String.class, 0 ) );
       
        remover = interfaceClass.getMethod( "removeU" );
        PIMap.put( remover, mockPredicateInfo( remover, "u", ActionType.REMOVER, Predicate.UNSET.class, 0 ) );
       
        existential = interfaceClass.getMethod( "hasU" );
        PIMap.put( existential, mockPredicateInfo( existential, "u", ActionType.EXISTENTIAL,
                TypeChecker.getPrimitiveClass( Boolean.class ), 0 ) );
       
        addCount.put( "setU", Integer.valueOf( 2 ) );
    }

    @Test
    public void testParseGetter() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getter );
        assertSame( PIMap.get( getter ), predicateInfo, getter );
        assertSame( getter );
        assertSame( setterS );
        assertSame( setterR );
        assertSame( existential );
        assertSame( remover );

    }

    @Test
    public void testParseSetterS() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( setterS );
        assertSame( PIMap.get( setterS ), predicateInfo, setterS );
        assertSame( getter );
        assertSame( setterS );
        assertSame( setterR );
        assertSame( existential );
        assertSame( remover );
    }

    @Test
    public void testParseSetterR() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( setterR );
        assertSame( PIMap.get( setterR ), predicateInfo, setterR );
        assertSame( getter );
        assertSame( setterS );
        assertSame( setterR );
        assertSame( existential );
        assertSame( remover );
    }

    @Test
    public void testParseExistential() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( existential );
        assertSame( PIMap.get( existential ), predicateInfo, existential );
        Assert.assertNull( subjectInfo.getPredicateInfo( getter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterS ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterR ) );
        assertSame( existential );
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
        assertSame( remover );
    }

}
