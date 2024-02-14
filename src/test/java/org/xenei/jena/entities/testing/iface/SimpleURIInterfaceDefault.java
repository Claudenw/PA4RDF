package org.xenei.jena.entities.testing.iface;

import org.apache.jena.rdf.model.RDFNode;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;

public interface SimpleURIInterfaceDefault extends SimpleURIInterface {

    @Override
    @Predicate(impl = true)
    default RDFNode getU() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default boolean hasU() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeU() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setU(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setU(@URI final String b) {
        throw new EntityManagerRequiredException();
    }

}
