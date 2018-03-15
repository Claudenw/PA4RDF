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

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.xenei.pa4rdf.entityManager.impl.EntityManagerImpl;

/**
 * Factory to create EntityManagers.
 * 
 */
public class EntityManagerFactory {

    public static EntityManager create() {
        return create( DatasetFactory.create() );
    }

    public static EntityManager create(Model model) {
        return create( DatasetFactory.create( model ) );
    }

    public static EntityManager create(Dataset dataset) {
        return create( RDFConnectionFactory.connect( dataset ) );
    }

    public static EntityManager create(RDFConnection connection) {
        return new EntityManagerImpl( connection );
    }

}