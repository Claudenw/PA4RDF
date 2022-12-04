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

import org.apache.jena.rdf.model.Property;

import java.lang.reflect.Method;
import java.util.List;

import org.xenei.jena.entities.impl.ActionType;

/**
 * Information about the predicate.
 *
 * This class should contain enough information to make translation from graph
 * to instance variables possible.
 */
public interface PredicateInfo {

    /**
     * @return the action type for the predicate.
     */
    public ActionType getActionType();

    /**
     * Get the method name that this predicate info is describing.
     *
     * @return the method name
     */
    public String getMethodName();

    /**
     * Get the namespace URI for the predicate.
     *
     * @return The namespace URI.
     */
    public String getNamespace();

    /**
     * Get the predicate as a property.
     *
     * @return The property for the predicate.
     */
    public Property getProperty();

    /**
     * Get the predicate URI as a string.
     *
     * @return The URI for the predicate.
     */
    public String getUriString();

    /**
     * get the value class.
     *
     * The value class is the class that is returned (for a getter type method)
     * or the class type of the argument (for a setter type method).
     *
     * @return The class
     */
    public Class<?> getValueClass();

    /**
     * List of methods to execute after the exec.
     *
     * @return The list of methods to execute after exec.
     */
    public List<Method> getPostExec();
    
    /**
     * return the object handler for the predicate info type.
     * @return the ObjectHandler.
     */
    public ObjectHandler getObjectHandler();
}