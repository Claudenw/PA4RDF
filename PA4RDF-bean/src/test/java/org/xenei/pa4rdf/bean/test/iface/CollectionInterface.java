package org.xenei.pa4rdf.bean.test.iface;

import java.util.List;

import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;

@Subject(namespace = "http://example.com/")
public interface CollectionInterface {
    @Predicate
    void addX(String x);

    List<String> getX();

    boolean hasX(String x);

    void removeX(String x);

}