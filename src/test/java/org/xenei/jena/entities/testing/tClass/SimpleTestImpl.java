package org.xenei.jena.entities.testing.tClass;

import org.apache.jena.rdf.model.Resource;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public class SimpleTestImpl implements SimpleInterface, ResourceWrapper {

    private final Resource resource;

    public SimpleTestImpl(final Resource resource) {
        this.resource = resource;
    }

    @Override
    @Predicate(impl = true, postExec = "output")
    public String getX() {
        throw new IllegalStateException();
    }

    @SuppressWarnings("unused")
    public String output(final String txt) {
        System.out.println( txt );
        return txt;
    }

    @Override
    @Predicate(impl = true)
    public boolean hasX() {
        throw new IllegalStateException();
    }

    @Override
    @Predicate(impl = true)
    public void removeX() {
        throw new IllegalStateException();
    }

    @Override
    @Predicate(impl = true)
    public void setX(final String x) {
        throw new IllegalStateException();
    }

    @Override
    public Resource getResource() {
        return resource;
    }

}
