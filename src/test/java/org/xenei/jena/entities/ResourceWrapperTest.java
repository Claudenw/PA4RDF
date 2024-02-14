package org.xenei.jena.entities;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResourceWrapperTest {

    Model m = ModelFactory.createDefaultModel();

    @Test
    public void resourceTest() {
        final Resource r = m.createResource();
        Assertions.assertEquals( r, ResourceWrapper.getResource( r ) );
    }

    @Test
    public void wrapperTest() {
        final Resource r = m.createResource();
        final ResourceWrapper wrapper = new ResourceWrapper() {

            @Override
            public Resource getResource() {
                return r;
            }
        };
        Assertions.assertEquals( r, ResourceWrapper.getResource( wrapper ) );
    }

    @Test
    public void notResourceTest() {

        final Long l = Long.valueOf( 1L );
        Assertions.assertThrows( IllegalArgumentException.class, () -> ResourceWrapper.getResource( l ) );
    }
}
