package org.xenei.jena.entities.impl.parser.simpleURI;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.testing.impl.SimpleURIInterfaceImpl;

public class SimpleURIInterfaceImplTest extends AbstractSimpleURIInterfaceTest {

    public SimpleURIInterfaceImplTest() throws Exception {
        super( SimpleURIInterfaceImpl.class );
    }
    
    @Override
    @Test
    public void testParseGetter() throws Exception {
        final PredicateInfo predicateInfo = parser.parse( getter );
        assertSame( PIMap.get( getter ), predicateInfo, getter );
        assertSame( getter );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterS ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( setterR ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( existential ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( remover ) );
    }


}
