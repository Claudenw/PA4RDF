package org.xenei.pa4rdf.bean.test.iface;

import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;

@Subject(namespace = "http://example.com/")
public interface SimpleInterface {
    String getX();

    boolean hasX();

    void removeX();

    @Predicate
    void setX(String x);

}