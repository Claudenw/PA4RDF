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

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.xenei.jena.entities.impl.EntityManagerImpl;

/**
 * Factory to create EntityManagers.
 * 
 */
public class EntityManagerFactory
{
	
	public static EntityManager getEntityManager()
	{
		return getEntityManager( DatasetFactory.create());
	}

	public static EntityManager getEntityManager(boolean writeThrough)
	{
		return getEntityManager( DatasetFactory.create(), true);
	}

	public static EntityManager getEntityManager( Model model)
	{
		return getEntityManager( DatasetFactory.create( model ));
	}
	
	public static EntityManager getEntityManager( Model model, boolean writeThrough)
	{
		return getEntityManager( DatasetFactory.create( model ), writeThrough );
	}

	public static EntityManager getEntityManager( Dataset dataset)
	{
		return getEntityManager( RDFConnectionFactory.connect( dataset), true);
	}

	public static EntityManager getEntityManager( Dataset dataset, boolean writeThrough)
	{
		return new EntityManagerImpl( RDFConnectionFactory.connect( dataset), writeThrough);
	}

	public static EntityManager getEntityManager( RDFConnection connection)
	{
		return new EntityManagerImpl( connection, true);
	}
	
	public static EntityManager getEntityManager( RDFConnection connection, boolean writeThrough)
	{
		return new EntityManagerImpl( connection, writeThrough);
	}

	
}
