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
package org.xenei.jena.entities;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.testing.iface.SimpleInterface;
import org.xenei.jena.entities.testing.tClass.SimpleTestImpl;

public class SimpleTestImplTests {

    private SimpleInterface tc;
    private Model model;
    private SimpleTestImpl simpleTest;

    @BeforeEach
    public void setup() throws Exception {
        model = ModelFactory.createDefaultModel();
        final Resource r = model.createResource( "http://localhost/SimpleTestImplTests" );
        simpleTest = new SimpleTestImpl( r );
        final EntityManager manager = EntityManagerFactory.getEntityManager();
        tc = manager.read( simpleTest, SimpleInterface.class, ResourceWrapper.class );
    }

    @AfterEach
    public void teardown() {
        model.close();
    }

    @Test
    public void testX() {
        Assertions.assertFalse( tc.hasX() );
        tc.setX( "hello" );
        Assertions.assertTrue( tc.hasX() );
        Assertions.assertEquals( "hello", tc.getX() );
        tc.setX( "world" );
        Assertions.assertEquals( "world", tc.getX() );
        tc.removeX();
        Assertions.assertFalse( tc.hasX() );
        Assertions.assertNull( tc.getX() );
        Assertions.assertEquals( 3, simpleTest.size() );
        Assertions.assertEquals( "hello", simpleTest.get( 0 ) );
        Assertions.assertEquals( "world", simpleTest.get( 1 ) );
        Assertions.assertNull( simpleTest.get( 2 ) );
    }

}
