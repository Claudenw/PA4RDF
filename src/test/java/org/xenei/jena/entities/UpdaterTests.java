package org.xenei.jena.entities;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.junit.Before;
import org.junit.Test;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

import junit.framework.Assert;

public class UpdaterTests {
    Model model;
    EntityManager manager;

    public UpdaterTests() {
        model = ModelFactory.createDefaultModel();
    }

    public class DataSource {
        private int intValue;
        private double dblValue;
        private float fltValue;
        private long lngValue;
        private String strValue;

        public double getDblValue() {
            return dblValue;
        }

        public float getFltValue() {
            return fltValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public long getLngValue() {
            return lngValue;
        }

        public String getStrValue() {
            return strValue;
        }

        public void setDblValue(final double dblValue) {
            this.dblValue = dblValue;
        }

        public void setFltValue(final float fltValue) {
            this.fltValue = fltValue;
        }

        public void setIntValue(final int intValue) {
            this.intValue = intValue;
        }

        public void setLngValue(final long lngValue) {
            this.lngValue = lngValue;
        }

        public void setStrValue(final String strValue) {
            this.strValue = strValue;
        }
    }

    public interface DataSourceIface {
        public double getDblValue();

        public float getFltValue();

        public int getIntValue();

        public long getLngValue();

        public String getStrValue();

        public void setDblValue(double dblValue);

        public void setFltValue(float fltValue);

        public void setIntValue(int intValue);

        public void setLngValue(long lngValue);

        public void setStrValue(String strValue);
    }

    public class DataSourceIfaceImpl implements DataSourceIface {
        private int intValue;
        private double dblValue;
        private float fltValue;
        private long lngValue;
        private String strValue;

        @Override
        public double getDblValue() {
            return dblValue;
        }

        @Override
        public float getFltValue() {
            return fltValue;
        }

        @Override
        public int getIntValue() {
            return intValue;
        }

        @Override
        public long getLngValue() {
            return lngValue;
        }

        @Override
        public String getStrValue() {
            return strValue;
        }

        @Override
        public void setDblValue(final double dblValue) {
            this.dblValue = dblValue;
        }

        @Override
        public void setFltValue(final float fltValue) {
            this.fltValue = fltValue;
        }

        @Override
        public void setIntValue(final int intValue) {
            this.intValue = intValue;
        }

        @Override
        public void setLngValue(final long lngValue) {
            this.lngValue = lngValue;
        }

        @Override
        public void setStrValue(final String strValue) {
            this.strValue = strValue;
        }
    }

    @Subject(namespace = "http://example.com/test#")
    public interface DataSourceModelIface {
        @Predicate
        public double getDblValue();

        @Predicate
        public float getFltValue();

        @Predicate
        public int getIntValue();

        @Predicate
        public long getLngValue();

        @Predicate
        public String getStrValue();

        public void setDblValue(double dblValue);

        public void setFltValue(float fltValue);

        public void setIntValue(int intValue);

        public void setLngValue(long lngValue);

        public void setStrValue(String strValue);
    }

    @Before
    public void setup() {
        model.removeAll();
        manager = EntityManagerFactory.create( model );
    }

    @Test
    public void DataSourceModelTest() throws Exception {
        final DataSource ds1 = new DataSource();
        ds1.setIntValue( 1 );
        ds1.setDblValue( 2.1 );
        ds1.setFltValue( 3.2F );
        ds1.setLngValue( 4 );
        ds1.setStrValue( "5" );

        final Model m = ModelFactory.createDefaultModel();
        final Resource r = m.createResource( "http://example.com/test/resource" );
        final DataSourceModelIface ds2 = manager.read( r, DataSourceModelIface.class );
        manager.update( ds1, ds2 );

        Assert.assertEquals( 1, ds2.getIntValue() );
        Assert.assertEquals( 2.1, ds2.getDblValue() );
        Assert.assertEquals( 3.2F, ds2.getFltValue() );
        Assert.assertEquals( 4, ds2.getLngValue() );
        Assert.assertEquals( "5", ds2.getStrValue() );
    }

    @Test
    public void DataSourceModelTest2() throws Exception {
        final Model m = ModelFactory.createDefaultModel();
        final Resource r = m.createResource( "http://example.com/test/resource" );
        final DataSourceModelIface ds1 = manager.read( r, DataSourceModelIface.class );

        ds1.setIntValue( 1 );
        ds1.setDblValue( 2.1 );
        ds1.setFltValue( 3.2F );
        ds1.setLngValue( 4 );
        ds1.setStrValue( "5" );

        final DataSource ds2 = new DataSource();
        manager.update( ds1, ds2 );

        Assert.assertEquals( 1, ds2.getIntValue() );
        Assert.assertEquals( 2.1, ds2.getDblValue() );
        Assert.assertEquals( 3.2F, ds2.getFltValue() );
        Assert.assertEquals( 4, ds2.getLngValue() );
        Assert.assertEquals( "5", ds2.getStrValue() );
    }

    @Test
    public void testInterfaceUpdate() {
        final DataSource ds1 = new DataSource();
        ds1.setIntValue( 1 );
        ds1.setDblValue( 2.1 );
        ds1.setFltValue( 3.2F );
        ds1.setLngValue( 4 );
        ds1.setStrValue( "5" );

        final DataSourceIface ds2 = new DataSourceIfaceImpl();
        manager.update( ds1, ds2 );

        Assert.assertEquals( 1, ds2.getIntValue() );
        Assert.assertEquals( 2.1, ds2.getDblValue() );
        Assert.assertEquals( 3.2F, ds2.getFltValue() );
        Assert.assertEquals( 4, ds2.getLngValue() );
        Assert.assertEquals( "5", ds2.getStrValue() );
    }

    @Test
    public void testInterfaceUpdate2() {
        final DataSourceIface ds1 = new DataSourceIfaceImpl();
        ds1.setIntValue( 1 );
        ds1.setDblValue( 2.1 );
        ds1.setFltValue( 3.2F );
        ds1.setLngValue( 4 );
        ds1.setStrValue( "5" );

        final DataSource ds2 = new DataSource();
        manager.update( ds1, ds2 );

        Assert.assertEquals( 1, ds2.getIntValue() );
        Assert.assertEquals( 2.1, ds2.getDblValue() );
        Assert.assertEquals( 3.2F, ds2.getFltValue() );
        Assert.assertEquals( 4, ds2.getLngValue() );
        Assert.assertEquals( "5", ds2.getStrValue() );
    }

    @Test
    public void testStandardUpdate() {
        final DataSource ds1 = new DataSource();
        ds1.setIntValue( 1 );
        ds1.setDblValue( 2.1 );
        ds1.setFltValue( 3.2F );
        ds1.setLngValue( 4 );
        ds1.setStrValue( "5" );

        final DataSource ds2 = new DataSource();
        manager.update( ds1, ds2 );

        Assert.assertEquals( 1, ds2.getIntValue() );
        Assert.assertEquals( 2.1, ds2.getDblValue() );
        Assert.assertEquals( 3.2F, ds2.getFltValue() );
        Assert.assertEquals( 4, ds2.getLngValue() );
        Assert.assertEquals( "5", ds2.getStrValue() );
    }

}
