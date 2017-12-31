package org.xenei.jena.entities.testing.impl;

import java.util.List;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.testing.iface.SimpleURICollectionInterface;

public class SimpleURICollectionImpl implements SimpleURICollectionInterface {

    @Override
    @Predicate(impl = true)
    public void addU(final RDFNode x) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void addU(@URI final String x) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void addU2(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void addU2(@URI final String x) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public List<RDFNode> getU() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public ExtendedIterator<RDFNode> getU2() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public boolean hasU(@URI final String x) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean hasU2(@URI final String b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeU(@URI final String x) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeU2(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeU2(@URI final String b) {
        throw new EntityManagerRequiredException();
    }

}
