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
package org.xenei.jena.entities.impl.handlers;

import org.apache.jena.rdf.model.RDFNode;

import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.ObjectHandler;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.exceptions.MissingAnnotationException;
import org.xenei.jena.entities.exceptions.NotInterfaceException;

/**
 * An ObjectHandler that creates EntityManager managed entities from
 * RDFResources and visa versa.
 *
 */
public class EntityHandler implements ObjectHandler {
    private final Class<?> valueClass;

    /**
     * Constructor.
     *
     * @param valueClass
     *            The Subject annotated class to create.
     */
    public EntityHandler(final Class<?> valueClass) {
        this.valueClass = valueClass;
    }

    /**
     * Create an RDFNode representation for the subject class.
     */
    @Override
    public RDFNode createRDFNode(final Object obj) {
        return ((ResourceWrapper) obj).getResource();
    }

    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof EntityHandler && valueClass.equals(((EntityHandler)o).valueClass));
    }

    @Override
    public int hashCode() {
        return valueClass.hashCode();
    }

    @Override
    public boolean isEmpty(final Object obj) {
        return obj == null;
    }

    /**
     * Use the entity manager to create an instance of the valueClass from the
     * resource. Effectively calls entityManager.read( node.asResource,
     * valueClass )
     *
     * @param node
     *            The RDFNode to wrap with the valueClass.
     * @return the instance of the valueClass.
     */
    @Override
    public Object parseObject(final RDFNode node) {
        try {
            return EntityManagerFactory.getEntityManager().read( node.asResource(), valueClass );
        } catch (final MissingAnnotationException | NotInterfaceException e) {
            throw new RuntimeException( e );
        }
    }

    @Override
    public String toString() {
        return "EntityHandler";
    }
}