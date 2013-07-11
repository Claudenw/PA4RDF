package org.xenei.jena.entities;

/**
 * A RuntimeException that indicates the class should have been constructed
 * by the EntityManager but wasn't. Normally called in implemented @Predicate
 * annotated methods.
 */
public class EntityManagerRequiredException extends RuntimeException
{

	public EntityManagerRequiredException()
	{

	}

	public EntityManagerRequiredException( final String message )
	{
		super(message);
	}

	public EntityManagerRequiredException( final String message,
			final Throwable cause )
	{
		super(message, cause);
	}

	public EntityManagerRequiredException( final String message,
			final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace )
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EntityManagerRequiredException( final Throwable cause )
	{
		super(cause);
	}

}
