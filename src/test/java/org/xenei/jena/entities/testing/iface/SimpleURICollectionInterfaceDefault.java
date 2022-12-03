package org.xenei.jena.entities.testing.iface;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.List;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;

public interface SimpleURICollectionInterfaceDefault extends SimpleURICollectionInterface {

    @Override
    @Predicate(impl = true)
    default void addU(final RDFNode x) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addU(@URI final String x) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addU2(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addU2(@URI final String x) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default List<RDFNode> getU() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default ExtendedIterator<RDFNode> getU2() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default boolean hasU(@URI final String x) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean hasU2(@URI final String b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeU(@URI final String x) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeU2(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeU2(@URI final String b) {
        throw new EntityManagerRequiredException();
    }

}
