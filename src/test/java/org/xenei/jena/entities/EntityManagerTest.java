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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.exceptions.MissingAnnotationException;
import org.xenei.jena.entities.exceptions.NotInterfaceException;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.jena.entities.testing.bad.UnannotatedInterface;
import org.xenei.jena.entities.testing.iface.MultiValueInterface;
import org.xenei.jena.entities.testing.iface.CollectionValueInterface;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public class EntityManagerTest {

    private final EntityManager manager = new EntityManagerImpl();

    @Test
    public void testBasicParser() throws Exception {
        manager.getSubjectInfo( MultiValueInterface.class );
        manager.getSubjectInfo( CollectionValueInterface.class );
    }

    @Test
    public void testClassParser() throws Exception {
        manager.parseClasses(
                new String[] { MultiValueInterface.class.getName(), CollectionValueInterface.class.getName() } );
        SubjectInfo ci = manager.getSubjectInfo( MultiValueInterface.class );
        Assertions.assertNotNull( ci.getPredicateInfo( MultiValueInterface.class.getMethod( "getU" ) ) );
        ci = manager.getSubjectInfo( CollectionValueInterface.class );
        Assertions.assertNotNull( ci.getPredicateInfo( CollectionValueInterface.class.getMethod( "getU" ) ) );
    }

    @Test
    public void testPathParser() throws Exception {
        manager.parseClasses( new String[] { "org.xenei.jena.entities.testing.abst" } );
        SubjectInfo ci = manager.getSubjectInfo( MultiValueInterface.class );
        Assertions.assertNotNull( ci.getPredicateInfo( MultiValueInterface.class.getMethod( "getU" ) ) );
        ci = manager.getSubjectInfo( CollectionValueInterface.class );
        Assertions.assertNotNull( ci.getPredicateInfo( CollectionValueInterface.class.getMethod( "getU" ) ) );
    }

    @Test
    public void testPathParserWithBadClasses() throws Exception {
        final Model model = ModelFactory.createDefaultModel();
        Assertions.assertThrows( MissingAnnotationException.class,
                () -> manager.read( model.createResource(), SimpleInterface.class, UnannotatedInterface.class ) );
    }

    @Test
    public void testPathParserWithNonInterface() {
        final Model model = ModelFactory.createDefaultModel();
        Assertions.assertThrows( NotInterfaceException.class,
                () -> manager.read( model.createResource(), Integer.class, UnannotatedInterface.class ) );
        Assertions.assertThrows( NotInterfaceException.class,
                () -> manager.read( model.createResource(), SimpleInterface.class, Integer.class ) );

    }
}
