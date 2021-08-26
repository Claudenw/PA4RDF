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

import org.apache.commons.lang3.StringUtils;
import org.xenei.jena.entities.impl.ObjectHandler;

/**
 * An ObjectHandler that does not convert RDFNodes.
 */
public class ResourceHandler implements ObjectHandler {
    /**
     * Convert an object to an RDFNode.
     * 
     * @throws IllegalArgumentException
     *             if obj is not an instance of RDFNode
     */
    @Override
    public RDFNode createRDFNode(final Object obj) {
        if (obj instanceof RDFNode) {
            return (RDFNode) obj;
        }
        throw new IllegalArgumentException( String.format( "%s is not an RDFNode", obj ) );
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof ResourceHandler;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean isEmpty(final Object obj) {
        if ((obj != null) && (obj instanceof RDFNode)) {
            final RDFNode node = (RDFNode) obj;
            if (node.isLiteral()) {
                return StringUtils.isBlank( node.asLiteral().getLexicalForm() );
            }
            if (node.isURIResource()) {
                return StringUtils.isBlank( node.asResource().getURI() );
            }
            return false;
        }
        return true;
    }

    /**
     * Returns the argument
     * 
     * @param node
     *            The RDFNode to parse/return
     * @return The node parameter
     */
    @Override
    public Object parseObject(final RDFNode node) {
        return node;
    }

    @Override
    public String toString() {
        return "ResourceHandler";
    }
}