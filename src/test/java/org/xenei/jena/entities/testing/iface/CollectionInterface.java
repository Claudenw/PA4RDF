package org.xenei.jena.entities.testing.iface;

import java.util.List;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

@Subject(namespace = "http://example.com/")
public interface CollectionInterface {
    @Predicate
    void addX(String x);

    List<String> getX();

    boolean hasX(String x);

    void removeX(String x);

}