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
package org.xenei.jena.entities.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * An enumeration of Action types.
 */
public enum ActionType {
    /**
     * Indicates a method that gets a value
     */
    GETTER(Arrays.asList( "get" )),
    /**
     * Indicates a method that sets a value
     */
    SETTER(Arrays.asList( "set", "add" )),
    /**
     * Indicates a method that removes a value
     */
    REMOVER(Arrays.asList( "remove" )),
    /**
     * Indicates a method that checks for the existance of a value
     */
    EXISTENTIAL(Arrays.asList( "has", "is" ));

    private final List<String> prefixes;

    private ActionType(final List<String> prefixes) {
        this.prefixes = prefixes;
    }

    public static boolean isMultiple(final Method m) {
        final ActionType at = ActionType.parse( m.getName() );
        switch (at) {
        case GETTER:
            return Iterator.class.isAssignableFrom( m.getReturnType() )
                    || Collection.class.isAssignableFrom( m.getReturnType() ) || m.getReturnType().isArray();

        case SETTER:
            return m.getName().startsWith( "add" );

        case EXISTENTIAL:
        case REMOVER:
            return m.getParameterTypes().length > 0;
        }
        throw new IllegalArgumentException( String.format( "%s is not an action type function", m ) );
    }

    /**
     * Parse the action type from the function name.
     *
     * Action types are defined from known function name prefixes. In general
     * the function prefix will define the action type and the remaining portion
     * of the name will define the local name for the predicate. @see @link{
     * org.xenei.jena.annotations.Predicate }
     *
     * <table summary="List of function prefixes and ActionType mappings">
     * <tr>
     * <th>Function prefix</th>
     * <th>ActionType</th>
     * </tr>
     * <tr>
     * <td>get or is</td>
     * <td>GETTER</td>
     * </tr>
     * <tr>
     * <td>set or add</td>
     * <td>SETTER</td>
     * </tr>
     * <tr>
     * <td>remove</td>
     * <td>REMOVER</td>
     * </tr>
     * <tr>
     * <td>has</td>
     * <td>EXISTENTIAL</td>
     * </tr>
     * </table>
     *
     * @param functionName
     *            The name of the function being parsed.
     * @return ActionType
     * @throws IllegalArgumentException
     *             if the function does not have an action type prefix.
     */
    public static ActionType parse(final String functionName) {
        for (final ActionType action : ActionType.values()) {
            if (action.prefixes.stream().anyMatch( (prefix) -> functionName.startsWith( prefix ) )) {
                return action;
            }
        }
        throw new IllegalArgumentException( String.format( "%s is not an action type function", functionName ) );
    }

    /**
     * Extract the local name portion of the function name/
     *
     * @param functionName
     *            The function name to extract the local portion from.
     * @return The local name.
     * @throws IllegalArgumentException
     *             fur unrecognized ActionType instances.
     */
    public String extractName(final String functionName) {
        if (isA( functionName )) {
            final Predicate<String> predicate = new Predicate<>() {
                @Override
                public boolean test(final String prefix) {
                    return functionName.startsWith( prefix );
                }
            };
            final Optional<String> prefix = prefixes.stream().filter( predicate ).findFirst();
            if (prefix.isPresent()) {
                return functionName.substring( prefix.get().length() );
            }
        }
        throw new IllegalArgumentException( functionName + " is not an ActionType" );
    }

    public Stream<String> createNames(final String name) {
        return prefixes.stream().map( t -> t + name );
    }

    /**
     * Test to see if the function name is of this action type.
     *
     * @param functionName
     *            The name to test
     * @return true if the function is of this type, false otherwise.
     */
    public boolean isA(final String functionName) {
        return StringUtils.isNotBlank( functionName )
                ? prefixes.stream().anyMatch( (prefix) -> functionName.startsWith( prefix ) )
                : false;
    }

    public Class<?> predicateClass(final Method m) {
        switch (this) {
        case EXISTENTIAL:
        case REMOVER:
        case SETTER:
            return m.getParameterTypes().length == 0 ? null : m.getParameterTypes()[0];

        case GETTER:
        default:
            return m.getReturnType();

        }
    }

}
