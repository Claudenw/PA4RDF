package org.xenei.jena.entities.testing.impl;

import org.apache.jena.rdf.model.RDFNode;
import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.testing.iface.SimpleURIInterface;

public class SimpleURIInterfaceImpl implements SimpleURIInterface {

    @Override
    @Predicate(impl = true)
    public RDFNode getU() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public boolean hasU() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeU() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setU(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setU(@URI final String b) {
        throw new EntityManagerRequiredException();
    }

}
