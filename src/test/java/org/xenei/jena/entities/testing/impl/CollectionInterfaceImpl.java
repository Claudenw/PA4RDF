package org.xenei.jena.entities.testing.impl;

import java.util.List;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.testing.iface.CollectionInterface;

public class CollectionInterfaceImpl implements CollectionInterface {

    @Override
    @Predicate(impl = true)
    public void addX(final String x) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public List<String> getX() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public boolean hasX(final String x) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeX(final String x) {
        throw new EntityManagerRequiredException();
    }

}
