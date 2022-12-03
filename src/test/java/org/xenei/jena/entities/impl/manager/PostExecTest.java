package org.xenei.jena.entities.impl.manager;

import org.apache.jena.rdf.model.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.testing.iface.SimpleInterfaceDefault;

public class PostExecTest extends BaseAbstractManagerTest {
    public PostExecTest() {
        super( SimpleInterfaceDefault.class );
    }

    @Test
    @Disabled
    public void testPostExecRuns() throws Exception {
        final Resource r = model.createResource();
        final SimpleInterfaceDefault o = manager.read( r, SimpleInterfaceDefault.class );

        o.setX( "foo" );
        o.getX();
        //Assertions.assertEquals( "foo", o.lastGetX );

    }

}
