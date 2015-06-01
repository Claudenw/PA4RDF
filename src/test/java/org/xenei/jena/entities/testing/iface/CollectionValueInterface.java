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
package org.xenei.jena.entities.testing.iface;

import org.apache.jena.rdf.model.RDFNode;

import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;

@Subject( namespace = "http://localhost/test#" )
public interface CollectionValueInterface
{

	@Predicate
	void addBool( Boolean b );

	@Predicate
	void addChar( Character b );

	@Predicate
	void addDbl( Double b );

	@Predicate
	void addEnt( TestInterface b );

	@Predicate
	void addFlt( Float b );

	@Predicate
	void addInt( Integer b );

	@Predicate
	void addLng( Long b );

	@Predicate
	void addRDF( RDFNode b );

	@Predicate
	void addStr( String b );

	void addU( RDFNode b );

	@Predicate
	void addU( @URI String b );

	@Predicate
	void addU3( RDFNode b );

	void addU3( @URI String b );

	Set<Boolean> getBool();

	List<Character> getChar();

	Queue<Double> getDbl();

	Queue<TestInterface> getEnt();

	Set<Float> getFlt();

	Queue<Integer> getInt();

	List<Long> getLng();

	List<RDFNode> getRDF();

	Set<String> getStr();

	@Predicate( type = RDFNode.class )
	Set<RDFNode> getU();

	@Predicate( type = URI.class, name = "u" )
	List<String> getU2();

	@Predicate( type = RDFNode.class )
	Queue<RDFNode> getU3();

	@Predicate( type = URI.class, name = "u3" )
	Set<String> getU4();

	Boolean hasBool( Boolean b );

	Boolean hasChar( Character b );

	Boolean hasDbl( Double b );

	Boolean hasEnt( TestInterface b );

	Boolean hasFlt( Float b );

	Boolean hasInt( Integer b );

	Boolean hasLng( Long b );

	Boolean hasRDF( RDFNode b );

	Boolean hasStr( String b );

	Boolean hasU( RDFNode b );

	Boolean hasU( @URI String b );

	Boolean hasU3( RDFNode b );

	Boolean hasU3( @URI String b );

	void removeBool( Boolean b );

	void removeChar( Character b );

	void removeDbl( Double b );

	void removeEnt( TestInterface b );

	void removeFlt( Float b );

	void removeInt( Integer b );

	void removeLng( Long b );

	void removeRDF( RDFNode b );

	void removeStr( String b );

	void removeU( RDFNode b );

	void removeU( @URI String b );

	void removeU3( RDFNode b );

	void removeU3( @URI String b );

}
