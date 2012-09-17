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

import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;


import com.hp.hpl.jena.rdf.model.RDFNode;

@Subject( namespace="http://localhost/test#")
public abstract class SingleValueObjectTestClass  {
	
	public SingleValueObjectTestClass() {};

	@Predicate
	public abstract void setBool( Boolean b );
	public abstract Boolean isBool();
	public abstract void removeBool();
	
	@Predicate
	public abstract void setChar( Character b );
	public abstract Character getChar();
	public abstract void removeChar();	
	
	@Predicate
	public abstract void setDbl( Double b );
	public abstract Double getDbl();
	public abstract void removeDbl();
	
	@Predicate
	public abstract void setFlt( Float b );
	public abstract Float getFlt();
	public abstract void removeFlt();
	
	@Predicate
	public abstract void setLng( Long b );
	public abstract Long getLng();
	public abstract void removeLng();
	
	@Predicate
	public abstract void setInt( Integer b );
	public abstract Integer getInt();
	public abstract void removeInt();
	
	@Predicate
	public abstract void setStr( String b );
	public abstract String getStr();
	public abstract void removeStr();
	
	@Predicate
	public abstract void setRDF( RDFNode b );
	public abstract RDFNode getRDF();
	public abstract void removeRDF();
	
	@Predicate
	public abstract void setEnt( TestClass b );
	public abstract TestClass getEnt();
	public abstract void removeEnt();
	
	@Predicate
	public abstract void setU( @URI String b );
	public abstract void setU( RDFNode b );
	public abstract RDFNode getU();
	public abstract void removeU();
	@Predicate( type=URI.class, name="u" )
	public abstract String getU2();
	
	@Predicate
	public abstract void setSubPredicate( SubPredicate subPredicate );
	public abstract SubPredicate getSubPredicate();
	public abstract void removeSubPredicate();
	
	
	@Subject( namespace="http://localhost/test#")
	public static abstract class SubPredicate {
		public SubPredicate() {};
		@Predicate
		public abstract void setName( String name );
		public abstract String getName();
	}
	
}

