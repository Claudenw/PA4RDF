package org.xenei.jena.entities.impl.manager.simple;

import java.lang.reflect.Method;

import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.jena.entities.impl.ObjectHandler;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.TypeChecker;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.VoidHandler;
import org.xenei.jena.entities.impl.manager.BaseAbstractManagerTest;
import org.xenei.jena.entities.impl.parser.AbstractMethodParserTest;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public abstract class AbstractSimpleInterfaceTest extends BaseAbstractManagerTest
{

    protected AbstractSimpleInterfaceTest(Class<?> interfaceClass)
            throws NoSuchMethodException, SecurityException {
        super( interfaceClass );

        NS="http://example.com/";
        
    }

    @Test
    public void testParsed() throws Exception {
        
        LiteralHandler lh = new LiteralHandler( XSDDatatype.XSDstring);
        
       PredicateInfoImpl pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "getX", String.class );
       final ObjectHandler handler = new LiteralHandler( TypeMapper.getInstance().getTypeByClass( String.class ) );

       Assert.assertEquals( "getX", pi.getMethodName() );
       Assert.assertEquals( handler, pi.getObjectHandler( manager ) );
       Assert.assertEquals( String.class, pi.getValueClass() );
       Assert.assertEquals( BaseAbstractManagerTest.NS + "x", pi.getUriString() );
       Assert.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

       pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "setX", String.class );
       Assert.assertEquals( "setX", pi.getMethodName() );
       Assert.assertEquals( handler, pi.getObjectHandler( manager ) );
       Assert.assertEquals( String.class, pi.getValueClass() );
       Assert.assertEquals( BaseAbstractManagerTest.NS + "x", pi.getUriString() );
       Assert.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

       pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "removeX", null );
       Assert.assertEquals( "removeX", pi.getMethodName() );
       Assert.assertEquals( lh, pi.getObjectHandler( manager ) );
       Assert.assertEquals( Predicate.UNSET.class, pi.getValueClass() );
       Assert.assertEquals( BaseAbstractManagerTest.NS + "x", pi.getUriString() );
       Assert.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );

       final Class<?> c = (Class<?>) Boolean.class.getField( "TYPE" ).get( null );

       pi = (PredicateInfoImpl) subjectInfo.getPredicateInfo( "hasX", Predicate.UNSET.class );
       Assert.assertEquals( "hasX", pi.getMethodName() );
       Assert.assertEquals( lh, pi.getObjectHandler( manager ) );
       Assert.assertEquals( c, pi.getValueClass() );
       Assert.assertEquals( BaseAbstractManagerTest.NS + "x", pi.getUriString() );
       Assert.assertEquals( BaseAbstractManagerTest.NS, pi.getNamespace() );


   }

}
