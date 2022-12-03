package org.xenei.jena.entities.impl.parser;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.testing.iface.SimpleInterfaceDefault;

public class SimpleInterfaceImplTest extends AbstractSimpleTest {
    public SimpleInterfaceImplTest() {
        super( SimpleInterfaceDefault.class );
    }

    @Test
    public void testPostExec() throws Exception {
        final Method m = classUnderTest.getMethod( "getX" );
        final PredicateInfo pi = parser.parse( m );
        Assertions.assertNotNull( pi, "getX not parsed" );
        Assertions.assertEquals( ActionType.GETTER, pi.getActionType() );
        Assertions.assertEquals( "getX", pi.getMethodName() );
        Assertions.assertEquals( "http://example.com/", pi.getNamespace() );
        Assertions.assertEquals( "http://example.com/x", pi.getUriString() );
        Assertions.assertEquals( String.class, pi.getValueClass() );
        Assertions.assertFalse( pi.getPostExec().isEmpty() );

    }
}
