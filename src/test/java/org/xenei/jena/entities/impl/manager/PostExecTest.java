package org.xenei.jena.entities.impl.manager;

import org.apache.jena.rdf.model.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.testing.impl.SimpleInterfaceImpl;

public class PostExecTest extends BaseAbstractManagerTest {
    public PostExecTest() {
        super( SimpleInterfaceImpl.class );
    }

    @Test
    public void testPostExecRuns() throws Exception {
        final Resource r = model.createResource();
        final SimpleInterfaceImpl o = manager.read( r, SimpleInterfaceImpl.class );

        o.setX( "foo" );
        o.getX();
        Assertions.assertEquals( "foo", o.lastGetX );

    }

}
