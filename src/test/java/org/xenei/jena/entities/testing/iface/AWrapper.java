package org.xenei.jena.entities.testing.iface;

import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

@Subject(namespace = "http://example.com/")
public interface AWrapper extends ResourceWrapper {
    String getX();

    String getZ();

    @Predicate
    void setX(String x);

    @Predicate(emptyIsNull = true)
    void setZ(String z);

}