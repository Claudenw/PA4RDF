package org.xenei.jena.entities.impl.manager;

import com.hp.hpl.jena.rdf.model.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.jena.entities.testing.impl.SimpleInterfaceImpl;

public class PostExecTest extends BaseAbstractManagerTest
{
	public PostExecTest()
	{
		super( SimpleInterfaceImpl.class  );
	}
	
	@Test
	public void testPostExecRuns() throws Exception
	{
		final Resource r = model.createResource();
		final SimpleInterfaceImpl o = manager.read(r,  SimpleInterfaceImpl.class);
		
		o.setX( "foo" );
		o.getX();
		Assert.assertEquals( "foo", o.lastGetX );
		
	}

}
