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

import com.hp.hpl.jena.rdf.model.Resource;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

@Subject( namespace = "http://localhost/test#" )
public abstract class SingleValuePrimitiveTestClass implements Resource
{

	@Predicate
	public abstract void setBool( boolean b );

	public abstract boolean isBool();

	public abstract void removeBool();

	@Predicate
	public abstract void setChar( char b );

	public abstract char getChar();

	public abstract void removeChar();

	@Predicate
	public abstract void setDbl( double b );

	public abstract double getDbl();

	public abstract void removeDbl();

	@Predicate
	public abstract void setFlt( float b );

	public abstract float getFlt();

	public abstract void removeFlt();

	@Predicate
	public abstract void setLng( long b );

	public abstract long getLng();

	public abstract void removeLng();

	@Predicate
	public abstract void setInt( int b );

	public abstract int getInt();

	public abstract void removeInt();

}
