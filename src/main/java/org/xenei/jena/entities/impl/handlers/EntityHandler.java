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

import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.ObjectHandler;
import org.xenei.jena.entities.ResourceWrapper;

/**
 * An ObjectHandler that creates EntityManager managed entities from
 * RDFResources and visa versa.
 *
 */
public class EntityHandler implements ObjectHandler {
    private final Class<?> valueClass;
    private final EntityManager entityManager;

    /**
     * Constructor.
     *
     * @param entityManager
     *            The EntityManager to use.
     * @param valueClass
     *            The Subject annotated class to create.
     */
    public EntityHandler(final EntityManager entityManager, final Class<?> valueClass) {
        this.entityManager = entityManager;
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
        return o instanceof EntityHandler;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
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
            return entityManager.read( node.asResource(), valueClass );
        } catch (final MissingAnnotation e) {
            throw new RuntimeException( e );
        }
    }

    @Override
    public String toString() {
        return "EntityHandler";
    }
}