package org.xenei.jena.entities;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.impl.EntityManagerImpl;

public class UpdaterTests
{
	EntityManager manager = new EntityManagerImpl();
	
	@Test
	public void testStandardUpdate()
	{
		DataSource ds1 = new DataSource();
		ds1.setIntValue(1);
		ds1.setDblValue(2.1);
		ds1.setFltValue(3.2F);
		ds1.setLngValue(4);
		ds1.setStrValue("5");
		
		DataSource ds2 = new DataSource();
		manager.update(ds1, ds2);
		
		Assert.assertEquals(1, ds2.getIntValue());
		Assert.assertEquals(2.1, ds2.getDblValue());
		Assert.assertEquals(3.2F, ds2.getFltValue());
		Assert.assertEquals(4, ds2.getLngValue());
		Assert.assertEquals("5", ds2.getStrValue());
	}
	
	@Test
	public void testInterfaceUpdate()
	{
		DataSource ds1 = new DataSource();
		ds1.setIntValue(1);
		ds1.setDblValue(2.1);
		ds1.setFltValue(3.2F);
		ds1.setLngValue(4);
		ds1.setStrValue("5");
		
		DataSourceIface ds2 = new DataSourceIfaceImpl();
		manager.update(ds1, ds2);
		
		Assert.assertEquals(1, ds2.getIntValue());
		Assert.assertEquals(2.1, ds2.getDblValue());
		Assert.assertEquals(3.2F, ds2.getFltValue());
		Assert.assertEquals(4, ds2.getLngValue());
		Assert.assertEquals("5", ds2.getStrValue());
	}
	
	
	@Test
	public void testInterfaceUpdate2()
	{
		DataSourceIface ds1 = new DataSourceIfaceImpl();
		ds1.setIntValue(1);
		ds1.setDblValue(2.1);
		ds1.setFltValue(3.2F);
		ds1.setLngValue(4);
		ds1.setStrValue("5");
		
		DataSource ds2 = new DataSource();
		manager.update(ds1, ds2);
		
		Assert.assertEquals(1, ds2.getIntValue());
		Assert.assertEquals(2.1, ds2.getDblValue());
		Assert.assertEquals(3.2F, ds2.getFltValue());
		Assert.assertEquals(4, ds2.getLngValue());
		Assert.assertEquals("5", ds2.getStrValue());
	}
	
	@Test
	public void DataSourceModelTest() {
		DataSource ds1 = new DataSource();
		ds1.setIntValue(1);
		ds1.setDblValue(2.1);
		ds1.setFltValue(3.2F);
		ds1.setLngValue(4);
		ds1.setStrValue("5");
		
		Model m = ModelFactory.createDefaultModel();
		Resource r = m.createResource( "http://example.com/test/resource");
		DataSourceModelIface ds2 = manager.read( r , DataSourceModelIface.class );
		manager.update(ds1, ds2);
		
		m.write(System.out, "TURTLE");
		
		Assert.assertEquals(1, ds2.getIntValue());
		Assert.assertEquals(2.1, ds2.getDblValue());
		Assert.assertEquals(3.2F, ds2.getFltValue());
		Assert.assertEquals(4, ds2.getLngValue());
		Assert.assertEquals("5", ds2.getStrValue());
	}
	
	@Test
	public void DataSourceModelTest2() {
		Model m = ModelFactory.createDefaultModel();
		Resource r = m.createResource( "http://example.com/test/resource");
		DataSourceModelIface ds1 = manager.read( r , DataSourceModelIface.class );
	
		ds1.setIntValue(1);
		ds1.setDblValue(2.1);
		ds1.setFltValue(3.2F);
		ds1.setLngValue(4);
		ds1.setStrValue("5");
		
		DataSource ds2 = new DataSource();
		manager.update(ds1, ds2);
		
		Assert.assertEquals(1, ds2.getIntValue());
		Assert.assertEquals(2.1, ds2.getDblValue());
		Assert.assertEquals(3.2F, ds2.getFltValue());
		Assert.assertEquals(4, ds2.getLngValue());
		Assert.assertEquals("5", ds2.getStrValue());
	}
	
	public interface DataSourceIface {
		public int getIntValue();
		public void setIntValue( int intValue );
		public double getDblValue();
		public void setDblValue( double dblValue );
		public float getFltValue();
		public void setFltValue( float fltValue );
		public long getLngValue();
		public void setLngValue( long lngValue );
		public String getStrValue();
		public void setStrValue( String strValue );
	}
	
	@Subject( namespace = "http://example.com/test#" )
	public interface DataSourceModelIface {
		@Predicate
		public int getIntValue();
		public void setIntValue( int intValue );
		@Predicate
		public double getDblValue();
		public void setDblValue( double dblValue );
		@Predicate
		public float getFltValue();
		public void setFltValue( float fltValue );
		@Predicate
		public long getLngValue();
		public void setLngValue( long lngValue );
		@Predicate
		public String getStrValue();
		public void setStrValue( String strValue );
	}
	
	public class DataSourceIfaceImpl implements DataSourceIface {
		private int intValue;
		private double dblValue;
		private float fltValue;
		private long lngValue;
		private String strValue;
		
		public int getIntValue()
		{
			return intValue;
		}
		public void setIntValue( int intValue )
		{
			this.intValue = intValue;
		}
		public double getDblValue()
		{
			return dblValue;
		}
		public void setDblValue( double dblValue )
		{
			this.dblValue = dblValue;
		}
		public float getFltValue()
		{
			return fltValue;
		}
		public void setFltValue( float fltValue )
		{
			this.fltValue = fltValue;
		}
		public long getLngValue()
		{
			return lngValue;
		}
		public void setLngValue( long lngValue )
		{
			this.lngValue = lngValue;
		}
		public String getStrValue()
		{
			return strValue;
		}
		public void setStrValue( String strValue )
		{
			this.strValue = strValue;
		}	
	}
	
	public class DataSource {
		private int intValue;
		private double dblValue;
		private float fltValue;
		private long lngValue;
		private String strValue;
		
		public int getIntValue()
		{
			return intValue;
		}
		public void setIntValue( int intValue )
		{
			this.intValue = intValue;
		}
		public double getDblValue()
		{
			return dblValue;
		}
		public void setDblValue( double dblValue )
		{
			this.dblValue = dblValue;
		}
		public float getFltValue()
		{
			return fltValue;
		}
		public void setFltValue( float fltValue )
		{
			this.fltValue = fltValue;
		}
		public long getLngValue()
		{
			return lngValue;
		}
		public void setLngValue( long lngValue )
		{
			this.lngValue = lngValue;
		}
		public String getStrValue()
		{
			return strValue;
		}
		public void setStrValue( String strValue )
		{
			this.strValue = strValue;
		}
	}
	
}
