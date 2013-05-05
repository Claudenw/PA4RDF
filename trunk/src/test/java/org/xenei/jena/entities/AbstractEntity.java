package org.xenei.jena.entities;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

@Subject( namespace = "http://example.com/" )
public abstract class AbstractEntity
{
	abstract public String getX();

	public String getY()
	{
		return "Y";
	}

	@Predicate
	abstract public void setX( String x );

}
