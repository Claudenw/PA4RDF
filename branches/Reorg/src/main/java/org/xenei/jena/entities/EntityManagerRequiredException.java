package org.xenei.jena.entities;

/**
 * A RuntimeException that indicates the class should have been constructed
 * by the EntityManager but wasn't.  Normally called in implemented @Predicate
 * annotated methods.
 */
public class EntityManagerRequiredException extends RuntimeException
{

	public EntityManagerRequiredException()
	{
		
	}

	public EntityManagerRequiredException( String message )
	{
		super(message);
	}

	public EntityManagerRequiredException( Throwable cause )
	{
		super(cause);
	}

	public EntityManagerRequiredException( String message, Throwable cause )
	{
		super(message, cause);
	}

	public EntityManagerRequiredException( String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace )
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
