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

package org.xenei.jena.entities.exceptions;

/**
 * Thrown when an annotation is required but not found.
 */
public class NotInterfaceException extends Exception {

    private static final long serialVersionUID = -5874216486380814195L;

    private final Class<?> clazz;

    public NotInterfaceException(final Class<?> clazz) {
        super( String.format( "%s is not an interface", clazz ) );
        this.clazz = clazz;
    }

    public Class<?> getNonInterface() {
        return clazz;
    }

}
