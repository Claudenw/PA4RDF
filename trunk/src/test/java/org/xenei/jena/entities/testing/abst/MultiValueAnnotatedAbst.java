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
package org.xenei.jena.entities.testing.abst;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.testing.iface.TestInterface;

@Subject( namespace = "http://localhost/test#" )
public abstract class MultiValueAnnotatedAbst
{

	@Predicate
	public abstract void addBool( Boolean b );

	@Predicate
	public abstract void addChar( Character b );

	@Predicate
	public abstract void addDbl( Double b );

	@Predicate
	public abstract void addEnt( TestInterface b );

	@Predicate
	public abstract void addFlt( Float b );

	@Predicate
	public abstract void addInt( Integer b );

	@Predicate
	public abstract void addLng( Long b );

	@Predicate
	public abstract void addRDF( RDFNode b );

	@Predicate
	public abstract void addStr( String b );

	public abstract void addU( RDFNode b );

	@Predicate
	public abstract void addU( @URI String b );

	@Predicate
	public abstract void addU3( RDFNode b );

	public abstract void addU3( @URI String b );

	public abstract ExtendedIterator<Boolean> getBool();

	public abstract ExtendedIterator<Character> getChar();

	public abstract ExtendedIterator<Double> getDbl();

	public abstract ExtendedIterator<TestInterface> getEnt();

	public abstract ExtendedIterator<Float> getFlt();

	public abstract ExtendedIterator<Integer> getInt();

	public abstract ExtendedIterator<Long> getLng();

	public abstract ExtendedIterator<RDFNode> getRDF();

	public abstract ExtendedIterator<String> getStr();

	@Predicate( type = RDFNode.class )
	public abstract ExtendedIterator<RDFNode> getU();

	@Predicate( type = URI.class, name = "u" )
	public abstract ExtendedIterator<String> getU2();

	@Predicate( type = RDFNode.class )
	public abstract ExtendedIterator<RDFNode> getU3();

	@Predicate( type = URI.class, name = "u3" )
	public abstract ExtendedIterator<String> getU4();

	public abstract Boolean hasBool( Boolean b );

	public abstract Boolean hasChar( Character b );

	public abstract Boolean hasDbl( Double b );

	public abstract Boolean hasEnt( TestInterface b );

	public abstract Boolean hasFlt( Float b );

	public abstract Boolean hasInt( Integer b );

	public abstract Boolean hasLng( Long b );

	public abstract Boolean hasRDF( RDFNode b );

	public abstract Boolean hasStr( String b );

	public abstract Boolean hasU( RDFNode b );

	public abstract Boolean hasU( @URI String b );

	public abstract Boolean hasU3( RDFNode b );

	public abstract Boolean hasU3( @URI String b );

	public abstract void removeBool( Boolean b );

	public abstract void removeChar( Character b );

	public abstract void removeDbl( Double b );

	public abstract void removeEnt( TestInterface b );

	public abstract void removeFlt( Float b );

	public abstract void removeInt( Integer b );

	public abstract void removeLng( Long b );

	public abstract void removeRDF( RDFNode b );

	public abstract void removeStr( String b );

	public abstract void removeU( RDFNode b );

	public abstract void removeU( @URI String b );

	public abstract void removeU3( RDFNode b );

	public abstract void removeU3( @URI String b );

}
