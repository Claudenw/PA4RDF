package org.xenei.pa4rdf.bean.exceptions;

/**
 * A RuntimeException that indicates the class should have been constructed by
 * the EntityFactory but wasn't. Normally called in implemented @Predicate
 * annotated methods.
 */
public class EntityFactoryRequiredException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -554457322254364401L;

    public EntityFactoryRequiredException() {

    }

    public EntityFactoryRequiredException(final String message) {
        super( message );
    }

    public EntityFactoryRequiredException(final String message, final Throwable cause) {
        super( message, cause );
    }

    public EntityFactoryRequiredException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super( message, cause, enableSuppression, writableStackTrace );
    }

    public EntityFactoryRequiredException(final Throwable cause) {
        super( cause );
    }

}
