package org.xenei.jena.entities.impl.datatype;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.impl.EntityManagerImpl;

/**
 * A set of tests to ensure that the EntityManager does not inject any errors into the TypeManager
 */
public class TypeManagerTest
{

	private TypeMapper mapper;

	@BeforeEach
	public void setUp() throws Exception
	{
		TypeMapper.reset();
		mapper = TypeMapper.getInstance();
	}

	@Test
	public void testClassesNotChange()
	{
		Map<Class<?>,RDFDatatype> localTypeMap = new HashMap<Class<?>,RDFDatatype>();

		// collect the list of datatypes
		for (final Iterator<RDFDatatype> iter = mapper.listTypes(); iter
				.hasNext();)
		{
			final RDFDatatype dt = iter.next();
			if ((dt.getJavaClass() != null))
			{
				RDFDatatype dt2 = mapper.getTypeByClass( dt.getJavaClass() );
				System.out.println( dt.getJavaClass()+": "+dt2.getURI() );
				localTypeMap.put( dt.getJavaClass(), dt2);
			}
		}
		EntityManagerImpl.registerTypes();

		// check the list of datatypes
		for (final Iterator<RDFDatatype> iter = mapper.listTypes(); iter
				.hasNext();)
		{
			final RDFDatatype dt = iter.next();
			if ((dt.getJavaClass() != null))
			{
				RDFDatatype dt2 = mapper.getTypeByClass( dt.getJavaClass() );
				RDFDatatype dt3 = localTypeMap.get( dt.getJavaClass() );
				Assertions.assertNotNull( dt3 );
				Assertions.assertEquals( dt3.getURI(), dt2.getURI() );
				Assertions.assertEquals( dt3.getJavaClass(), dt2.getJavaClass() );
			}
		}
	}

	@Test
	public void testURIsNotChange()
	{
		Map<String,RDFDatatype> localTypeMap = new HashMap<String,RDFDatatype>();

		// collect the list of datatypes
		for (final Iterator<RDFDatatype> iter = mapper.listTypes(); iter
				.hasNext();)
		{
			final RDFDatatype dt = iter.next();
			if ((dt.getJavaClass() != null))
			{
				RDFDatatype dt2 = mapper.getTypeByName( dt.getURI() );
				System.out.println( dt.getURI()+": "+dt2.getJavaClass() );
				localTypeMap.put( dt.getURI(), dt2);
			}
		}
		EntityManagerImpl.registerTypes();

		// check the list of datatypes
		for (final Iterator<RDFDatatype> iter = mapper.listTypes(); iter
				.hasNext();)
		{
			final RDFDatatype dt = iter.next();
			if ((dt.getJavaClass() != null))
			{
				RDFDatatype dt2 = mapper.getTypeByName( dt.getURI() );
				RDFDatatype dt3 = localTypeMap.get( dt.getURI() );
				Assertions.assertNotNull( dt3 );
				Assertions.assertEquals( dt3.getURI(), dt2.getURI() );
				Assertions.assertEquals( dt3.getJavaClass(), dt2.getJavaClass() );
			}
		}
	}
}
