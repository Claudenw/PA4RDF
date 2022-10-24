package org.xenei.jena.entities.testing.iface;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

@Subject(namespace = "http://example.com/")
public interface SimpleInterface {
    String getX();

    boolean hasX();

    void removeX();

    @Predicate
    void setX(String x);

}