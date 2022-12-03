package org.xenei.jena.entities;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.Test;

public class ResourceWrapperTest {
    
    Model m = ModelFactory.createDefaultModel();

    @Test
    public void resourceTest() {
        Resource r = m.createResource();
        assertEquals(r, ResourceWrapper.getResource( r ));
    }
    
    @Test
    public void wrapperTest() {
        Resource r = m.createResource();
        ResourceWrapper wrapper = new ResourceWrapper() {

            @Override
            public Resource getResource() {
                return r;
            }
        };
        assertEquals( r, ResourceWrapper.getResource( wrapper ));
    }
    
    @Test
    public void notResourceTest() {
        
        Long l = Long.valueOf(1L);
        assertThrows( IllegalArgumentException.class, () -> ResourceWrapper.getResource( l ) );
    }
}
