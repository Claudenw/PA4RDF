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

/**
 * An enumeration of Action types.
 */
public enum ActionType
{
	 GETTER(), SETTER(), REMOVER(), EXISTENTIAL() ; 
	

	 /**
	  * Parse the action type from the function name.
	  * 
	  * Action types are defined from known function name prefixes.
	  * In general the function prefix will define the action type and the 
	  * remaining portion of the name will define the local name for the 
	  * predicate. @see @link{ org.xenei.jena.annotations.Predicate }
	  * 
	  * <table>
	  * <tr><th>Function prefix</th><th>ActionType</th></tr>
	  * <tr><td>get or is</td><td>GETTER</td></tr>
	  * <tr><td>set or add</td><td>SETTER</td></tr>
	  * <tr><td>remove</td><td>REMOVER</td></tr>
	  * <tr><td>has</td><td>EXISTENTIAL</td></tr>
	  * </table>
	  * 
	  * @param functionName
	  * @return ActionType
	  * @throws IllegalArgumentException if the function does not have an action type prefix.
	  */
	 public static ActionType parse(String functionName)
	 {
		 if (functionName.startsWith( "get") || functionName.startsWith( "is" ))
		 {
			 return GETTER;
		 }
		 if (functionName.startsWith( "set" ) || functionName.startsWith( "add" ))
		 {
			 return SETTER;
		 }
		 if (functionName.startsWith( "remove"))
		 {
			 return REMOVER;
		 }
		 if (functionName.startsWith( "has"))
		 {
			 return EXISTENTIAL;
		 }
		 throw new IllegalArgumentException( String.format( "%s is not an action type function", functionName));
		 
	 }
	 
	 /**
	  * Test to see if the function name is of this action type.
	  * @param functionName The name to test
	  * @return true if the function is of this type, false otherwise.
	  */
	 public boolean isA(String functionName)
	 {
		 if (functionName == null)
		 {
			 return false;
		 }
		 switch (this) {
			 case EXISTENTIAL:
				 return functionName.startsWith( "has");

			 case GETTER:
				 return functionName.startsWith( "get") || functionName.startsWith( "is" );
				 
			 case REMOVER:
				 return functionName.startsWith( "remove");
				 
			 case SETTER:
				 return functionName.startsWith( "set" ) || functionName.startsWith( "add" );
		 }
		return false;	 
	 }
	 
	 
	 /**
	  * Extract the local name portion of the function name/
	  * @param name The function name to extract the local portion from.
	  * @return The local name.
	  * @throws IllegalArgumentException fur unrecognized ActionType instances.
	  */
	 public String extractName( String name )
	 {
		 switch (this)
		 {
			 case GETTER:
				 if (name.startsWith( "get" ))
				 {
					 return name.substring( 3 );
				 }
				 return name.substring( 2 );
				 
			 case SETTER:
			 case EXISTENTIAL:
				 return name.substring( 3 );
				 
			 case REMOVER:
				 return name.substring( 6 );
		 }
		 throw new IllegalArgumentException( this.getClass().getName()+" does not seem to be a valid ActionType");
	 }

}
