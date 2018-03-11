package org.xenei.pa4rdf.bean;

import org.apache.jena.rdf.model.Resource;
import org.xenei.pa4rdf.bean.exceptions.MissingAnnotation;
import org.xenei.pa4rdf.bean.impl.FactoryImpl;

public interface EntityFactory
{

	static final EntityFactory INSTANCE = new FactoryImpl();

	static void reset()
	{
		INSTANCE.reset_();
	}

	static <T> T read(Resource r, Class<T> clazz) throws MissingAnnotation
	{
		return INSTANCE.makeInstance(r, clazz);
	}

	static <T> T read(ResourceWrapper r, Class<T> clazz)
			throws MissingAnnotation
	{
		return INSTANCE.makeInstance(r, clazz);
	}

	SubjectInfo getSubjectInfo(Class<?> clazz);

	<T> T makeInstance(Object source, Class<T> clazz) throws MissingAnnotation;

	/**
	 * Parse the class if necessary.
	 * 
	 * The first time the class is seen it is parsed, after that a cached
	 * version is returned.
	 * 
	 * @param clazz
	 * @return The SubjectInfo for the class.
	 * @throws MissingAnnotation
	 */
	SubjectInfo parse(final Class<?> clazz) throws MissingAnnotation;

	/**
	 * reset internals to initial state.
	 */
	public void reset_();
}
