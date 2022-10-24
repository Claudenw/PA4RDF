package org.xenei.jena.entities.testing.impl;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

@Subject(namespace = "http://example.com/")
public class SimpleSubjectImpl {

    public SimpleSubjectImpl() {
    }

    @Predicate(impl = true)
    public String getX() {
        throw new EntityManagerRequiredException();
    }

    @Predicate(impl = true)
    public boolean hasX() {
        throw new EntityManagerRequiredException();
    }

    @Predicate(impl = true)
    public void removeX() {
        throw new EntityManagerRequiredException();
    }

    @Predicate(impl = true)
    public void setX(final String x) {
        throw new EntityManagerRequiredException();
    }

}
