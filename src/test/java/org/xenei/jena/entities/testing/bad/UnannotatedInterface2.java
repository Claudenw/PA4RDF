package org.xenei.jena.entities.testing.bad;

import org.apache.jena.rdf.model.Model;

/**
 * A class that will fail.
 *
 * This should fail because getModel() does not have an implementation and this
 * does not extend Resource
 */
public interface UnannotatedInterface2 {
    Model getModel();

    String getZ();

    void setZ(String z);

}