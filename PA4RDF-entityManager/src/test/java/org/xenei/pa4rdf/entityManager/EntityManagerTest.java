/*
 * Copyright 2012, XENEI.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xenei.pa4rdf.entityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

public class EntityManagerTest {

	private final Model model = ModelFactory.createDefaultModel();
	private EntityManager manager;

	@Before
	public void setup() {
		//PropertyConfigurator.configure( "./src/test/resources/log4j.properties" );
		model.removeAll();
		manager = EntityManagerFactory.create( model );       
	}

	@Test
	public void testInstanceSetter() throws Exception {
		final TestEntity testEntity = manager.makeInstance( ResourceFactory.createResource("http://example.com/res"), TestEntity.class );
		testEntity.setName( "phred");
		assertFalse( "Should have set a value in the test entity", testEntity.getResource().getModel().isEmpty());
		assertEquals( "phred", testEntity.getName() );
		assertTrue( "Should not have written to model yet", model.isEmpty());
	}


	@Test
	public void testInstanceSync() throws Exception {

		final TestEntity testEntity = manager.makeInstance( ResourceFactory.createResource("http://example.com/res"), TestEntity.class );
		testEntity.setName( "phred");
		assertFalse( "Should have set a value in the test entity", testEntity.getResource().getModel().isEmpty());
		assertEquals( "phred", testEntity.getName() );
		assertTrue( "Should not have written to model yet", model.isEmpty());
		manager.sync( testEntity );    
		assertFalse( "Model should not be empty", model.isEmpty() );
		final Property p = ResourceFactory.createProperty("http://example.com/test#name");
		final ResIterator iter = model.listResourcesWithProperty( p );
		assertTrue( "should have a resource", iter.hasNext());
		final Resource r = iter.next();
		assertEquals( "Should be the same resource", testEntity.getResource(), r);
		final Statement s = r.getProperty( p );
		assertEquals( "Should be 'phred'", "phred", s.getString());
	}

	

}
