package org.xenei.jena.entities;

/**
 * A RuntimeException that indicates the class should have been constructed by
 * the EntityManager but wasn't. Normally called in implemented @Predicate
 * annotated methods.
 */
public class EntityManagerRequiredException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -554457322254364401L;

    public EntityManagerRequiredException() {

    }
}
