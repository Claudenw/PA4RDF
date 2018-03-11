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

	/**
     * Get the SubjectInfo for the class.
     *
     * @param clazz
     *            The class to get SubjectInfo for.
     * @return The SubjectInfo
     * @throws IllegalArgumentException
     *             if clazz is not properly annotated with Subject annotations.
     */
	public SubjectInfo getSubjectInfo(Class<?> clazz);

	  /**
   * Read an instance of clazz from source.
   *
   * Does not verify that the resource passes the isInstance() check.
   *
   * @see #isInstance(Object, Class)
   * @param source
   *            Must either implement Resource or ResourceWrapper interfaces.
   * @param class
   *            The class of the object to be returned.
   * @param <T>
   *            the instance type to return.
   * @return primaryClass instance that also implements ResourceWrapper.
   * @throws MissingAnnotation
   *             if any of the classes do not have Subject annotations.
   * @throws IllegalArgumentException
   *             if source implements neither Resource nor ResourceWrapper.
   */
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
	public SubjectInfo parse(final Class<?> clazz) throws MissingAnnotation;

	/**
	 * reset internals to initial state.
	 */
	public void reset_();
}
