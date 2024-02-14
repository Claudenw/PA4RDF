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
import org.apache.jena.rdf.model.ResourceFactory;
import org.xenei.jena.entities.ObjectHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * An ObjectHandler that handles converting RDFNodes to URI strings and visa
 * versa
 */
public class UriHandler implements ObjectHandler {
    /**
     * Convert the object as a string to an RDFNode.
     *
     * @param obj
     *            The object to convert
     * @return The RDFNode with the object string value as the URI.
     */
    @Override
    public RDFNode createRDFNode(final Object obj) {
        return ResourceFactory.createResource( String.valueOf( obj ) );
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof UriHandler;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean isEmpty(final Object obj) {
        return (obj == null) || StringUtils.isBlank( String.valueOf( obj ) );
    }

    /**
     * Return the URI string for the RDFnode
     *
     * @param node
     *            the RDFNode
     * @return the URI fo the RDF node
     */
    @Override
    public Object parseObject(final RDFNode node) {
        return node.asResource().getURI();
    }

    @Override
    public String toString() {
        return "UriHandler";
    }
}