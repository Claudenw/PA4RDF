package org.xenei.jena.entities.impl.parser.simple;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.pa4rdf.bean.PredicateInfo;
import org.xenei.pa4rdf.bean.impl.ActionType;
import org.xenei.pa4rdf.bean.test.impl.SimpleInterfaceImpl;

public class SimpleInterfaceImplTest extends AbstractSimpleInterfaceTest {

    public SimpleInterfaceImplTest() throws Exception {
        super( SimpleInterfaceImpl.class );
        PIMap.put( getter, mockPredicateInfo( getter, "x", ActionType.GETTER, String.class, 1 ) );
    }
    
    @Override
    @Test
    public void testParseGetter() throws Exception {
        // getter changes output types
        final PredicateInfo predicateInfo = parser.parse( getter );
        assertSame( PIMap.get( getter ), predicateInfo, getter );
        assertSame( getter );
        Assert.assertNull( subjectInfo.getPredicateInfo( setter ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( existential ) );
        Assert.assertNull( subjectInfo.getPredicateInfo( remover ) );
    }

}
