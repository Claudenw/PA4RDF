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

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;

@Subject( namespace = "http://localhost/test#" )
public abstract class MultiValueObjectTestClass
{

	@Predicate
	public abstract void addBool( Boolean b );

	public abstract ExtendedIterator<Boolean> getBool();

	public abstract Boolean hasBool( Boolean b );

	public abstract void removeBool( Boolean b );

	@Predicate
	public abstract void addChar( Character b );

	public abstract ExtendedIterator<Character> getChar();

	public abstract Boolean hasChar( Character b );

	public abstract void removeChar( Character b );

	@Predicate
	public abstract void addDbl( Double b );

	public abstract ExtendedIterator<Double> getDbl();

	public abstract Boolean hasDbl( Double b );

	public abstract void removeDbl( Double b );

	@Predicate
	public abstract void addFlt( Float b );

	public abstract ExtendedIterator<Float> getFlt();

	public abstract Boolean hasFlt( Float b );

	public abstract void removeFlt( Float b );

	@Predicate
	public abstract void addLng( Long b );

	public abstract ExtendedIterator<Long> getLng();

	public abstract Boolean hasLng( Long b );

	public abstract void removeLng( Long b );

	@Predicate
	public abstract void addInt( Integer b );

	public abstract ExtendedIterator<Integer> getInt();

	public abstract Boolean hasInt( Integer b );

	public abstract void removeInt( Integer b );

	@Predicate
	public abstract void addStr( String b );

	public abstract ExtendedIterator<String> getStr();

	public abstract Boolean hasStr( String b );

	public abstract void removeStr( String b );

	@Predicate
	public abstract void addRDF( RDFNode b );

	public abstract ExtendedIterator<RDFNode> getRDF();

	public abstract Boolean hasRDF( RDFNode b );

	public abstract void removeRDF( RDFNode b );

	@Predicate
	public abstract void addEnt( TestClass b );

	public abstract ExtendedIterator<TestClass> getEnt();

	public abstract Boolean hasEnt( TestClass b );

	public abstract void removeEnt( TestClass b );

	@Predicate
	public abstract void addU( @URI String b );

	public abstract void addU( RDFNode b );

	@Predicate( type = RDFNode.class )
	public abstract ExtendedIterator<RDFNode> getU();

	public abstract void removeU( @URI String b );

	public abstract void removeU( RDFNode b );

	public abstract Boolean hasU( @URI String b );

	public abstract Boolean hasU( RDFNode b );

	@Predicate( type = URI.class, name = "u" )
	public abstract ExtendedIterator<String> getU2();

	@Predicate
	public abstract void addU3( RDFNode b );

	@Predicate( type = RDFNode.class )
	public abstract ExtendedIterator<RDFNode> getU3();

	public abstract void removeU3( @URI String b );

	public abstract void removeU3( RDFNode b );

	public abstract Boolean hasU3( @URI String b );

	public abstract Boolean hasU3( RDFNode b );

	@Predicate( type = URI.class, name = "u3" )
	public abstract ExtendedIterator<String> getU4();

	public abstract void addU3( @URI String b );

}
