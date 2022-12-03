package org.xenei.jena.entities;

import org.xenei.jena.entities.annotations.Subject;

public class TestEntityManager implements EntityManager {

    @Override
    public Subject getSubject(Class<?> clazz) {
        return null;
    }

    @Override
    public SubjectInfo getSubjectInfo(Class<?> clazz) {
        return null;
    }

    @Override
    public boolean isInstance(Object target, Class<?> clazz) {
        return false;
    }

    @Override
    public void parseClasses(String packageName) throws MissingAnnotation {
    }

    @Override
    public void parseClasses(String[] packageNames) throws MissingAnnotation {
    }

    @Override
    public <T> T make(Object source, Class<T> primaryClass, Class<?>... secondaryClasses) throws MissingAnnotation {
        return null;
    }

    @Override
    public <T> T read(Object source, Class<T> primaryClass, Class<?>... secondaryClasses)
            throws MissingAnnotation, IllegalArgumentException {
        return null;
    }

    @Override
    public <T> T addInstanceProperties(T source, Class<?> clazz) throws MissingAnnotation {
        return null;
    }

    @Override
    public Object update(Object source, Object target) {
        return null;
    }

    @Override
    public void reset() {
    }
}
