package org.xenei.jena.entities.impl.manager;

import org.junit.After;
import org.junit.Before;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.jena.entities.impl.SubjectInfoImpl;

abstract public class BaseAbstractManagerTest
{
	protected SubjectInfoImpl subjectInfo;
	protected final Class<?> classUnderTest;
	protected static String NS = "http://localhost/test#";
	protected final EntityManager manager = new EntityManagerImpl();

	protected BaseAbstractManagerTest( final Class<?> classUnderTest )
	{
		this.classUnderTest = classUnderTest;
	}

	@Before
	public void setup() throws MissingAnnotation
	{
		subjectInfo = (SubjectInfoImpl) manager.getSubjectInfo(classUnderTest);
	}

	@After
	public void teardown()
	{
	}

}
