package org.xenei.jena.entities.testing.bad;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;


public interface BadAddersInterface {

    @Subject(namespace = "http://example.com/")
    interface NullArgs {
        @Predicate
        void addX();
    }

    @Subject(namespace = "http://example.com/")
    interface TwoArgs {
        @Predicate
        void addX(String x, String y);
    }

}
