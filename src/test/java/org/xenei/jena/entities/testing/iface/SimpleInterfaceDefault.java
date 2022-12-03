package org.xenei.jena.entities.testing.iface;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;

public interface SimpleInterfaceDefault extends SimpleInterface {

    @Override
    @Predicate(impl = true, postExec = "postGetX")
    default String getX() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default boolean hasX() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeX() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setX(final String x) {
        throw new EntityManagerRequiredException();
    }

}
