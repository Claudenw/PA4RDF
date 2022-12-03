package org.xenei.jena.entities.testing.iface;

import java.util.List;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;

public interface CollectionInterfaceDefault extends CollectionInterface {

    @Override
    @Predicate(impl = true)
    default void addX(final String x) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default List<String> getX() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default boolean hasX(final String x) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeX(final String x) {
        throw new EntityManagerRequiredException();
    }

}
