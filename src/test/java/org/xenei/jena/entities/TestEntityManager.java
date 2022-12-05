package org.xenei.jena.entities;

import org.xenei.jena.entities.annotations.Subject;

public class TestEntityManager implements EntityManager {

    @Override
    public Subject getSubject(final Class<?> clazz) {
        return null;
    }

    @Override
    public SubjectInfo getSubjectInfo(final Class<?> clazz) {
        return null;
    }

    @Override
    public boolean isInstance(final Object target, final Class<?> clazz) {
        return false;
    }

    @Override
    public void parseClasses(final String packageName) throws MissingAnnotation {
    }

    @Override
    public void parseClasses(final String[] packageNames) throws MissingAnnotation {
    }

    @Override
    public <T> T make(final Object source, final Class<T> primaryClass, final Class<?>... secondaryClasses)
            throws MissingAnnotation {
        return null;
    }

    @Override
    public <T> T read(final Object source, final Class<T> primaryClass, final Class<?>... secondaryClasses)
            throws MissingAnnotation, IllegalArgumentException {
        return null;
    }

    @Override
    public <T> T addInstanceProperties(final T source, final Class<?> clazz) throws MissingAnnotation {
        return null;
    }

    @Override
    public Object update(final Object source, final Object target) {
        return null;
    }

    @Override
    public void reset() {
    }
}
