package org.xenei.jena.entities.impl.parser;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.testing.impl.SimpleSubjectImpl;

public class SimpleSubjectImplTest extends AbstractSimpleTest {
    public SimpleSubjectImplTest() {
        super( SimpleSubjectImpl.class );
    }

    @Override
    protected Class<?>[] getGetAnnotations() {
        return new Class<?>[] { Predicate.class };
    }

    @Override
    protected Class<?>[] getHasAnnotations() {
        return new Class<?>[] { Predicate.class };
    }

    @Override
    protected Class<?>[] getRemoveAnnotations() {
        return new Class<?>[] { Predicate.class };
    }

    @Override
    protected Class<?>[] getSetAnnotations() {
        return new Class<?>[] { Predicate.class };
    }
}
