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

/**
 * An ObjectHandler that always returns null.
 */
public class VoidHandler extends AbstractObjectHandler {
    
    public static final VoidHandler INSTANCE = new VoidHandler();
    
    private VoidHandler() {
        
    }
    /**
     * Convert the object to null
     * 
     * @param obj
     *            The object to convert
     * @return null
     */
    @Override
    public RDFNode createRDFNode(final Object obj) {
        return null;
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof VoidHandler;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean isEmpty(final Object obj) {
        return true;
    }

    /**
     * convert the node to a null
     * 
     * @param node
     *            The node to convert.
     * @return null.
     */
    @Override
    public Object parseObject(final RDFNode node) {
        return null;
    }

    @Override
    public String toString() {
        return "VoidHandler";
    }

}