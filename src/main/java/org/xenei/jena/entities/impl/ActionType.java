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
import java.util.Collection;
import java.util.Iterator;

/**
 * An enumeration of Action types.
 */
public enum ActionType {
    /**
     * Indicates a method that gets a value
     */
    GETTER("get", "is"),
    /**
     * Indicates a method that sets a value
     */
    SETTER("set", "add"),
    /**
     * Indicates a method that removes a value
     */
    REMOVER( "remove"),
    /**
     * Indicates a method that checks for the existence of a value
     */
    EXISTENTIAL( "has");
    
    private String[] prefixes;
    
    ActionType( String... prefixes)
    {
        this.prefixes = prefixes;
    }

    /**
     * Return true if the method is backed by multiple entries in the graph.
     * @param method the method to check
     * @return true if there are multiples.
     */
    public static boolean isMultiple(final Method method) {
        final ActionType at = ActionType.parse( method.getName() );
        switch (at) {
        case GETTER:
            return Iterator.class.isAssignableFrom( method.getReturnType() )
                    || Collection.class.isAssignableFrom( method.getReturnType() );

        case SETTER:
            return method.getName().startsWith( "set" );

        case EXISTENTIAL:
        case REMOVER:
            return method.getParameterTypes().length > 0;
        }
        throw new IllegalArgumentException( String.format( "%s is not an action type function", method ) );
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
        for (ActionType type :ActionType.values())
        {
            if (type.isA( functionName  ))                
            {
                return type;
            }
        }
        
        throw new IllegalArgumentException( String.format( "%s is not an action type function", functionName ) );

    }

    /**
     * Extract the local name portion of the function name.
     * 
     * @param name
     *            The function name to extract the local portion from.
     * @return The local name.
     * @throws IllegalArgumentException
     *             fur unrecognized ActionType instances.
     */
    public String extractName(final String name) {
        switch (this) {
        case GETTER:
            if (name.startsWith( "get" )) {
                return name.substring( 3 );
            }
            return name.substring( 2 );

        case SETTER:
        case EXISTENTIAL:
            return name.substring( 3 );

        case REMOVER:
            return name.substring( 6 );
        }
        throw new IllegalArgumentException( this.getClass().getName() + " does not seem to be a valid ActionType" );
    }

    /**
     * Create method names from the the name suffix of the function name.
     * 
     * @param nameSuffix
     *            the suffix for the name.
     * @return an array of potential method names.
     * @throws IllegalArgumentException
     *             fur unrecognized ActionType instances.
     */
    public String[] functionNames(final String nameSuffix) {
        String[] retval = new String[ prefixes.length];
        for (int i=0;i<prefixes.length;i++)
        {
            retval[i] = prefixes[i]+nameSuffix;
        }
        return retval;
    }
    
    /**
     * Test to see if the function name is of this action type.
     * 
     * @param functionName
     *            The name to test
     * @return true if the function is of this type, false otherwise.
     */
    public boolean isA(final String functionName) {
        if (functionName == null) {
            return false;
        }
        for (String pfx : prefixes)
        {
            if (functionName.startsWith( pfx ))
            {
                return true;
            }
        }
        return false;
    }

}
