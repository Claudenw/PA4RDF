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

import com.hp.hpl.jena.rdf.model.Resource;

import com.hp.hpl.jena.rdf.model.RDFNode;

@Subject( namespace="http://localhost/test#")
public abstract class SingleValueMixedTypeTestClass implements Resource {
	
	@Predicate
	public abstract void setBool( boolean b );
	public abstract Boolean isBool();
	
	@Predicate
	public abstract void setChar( char b );
	public abstract Character getChar();	
	
	@Predicate
	public abstract void setDbl( double b );
	public abstract Double getDbl();
	
	@Predicate
	public abstract void setFlt( float b );
	public abstract Float getFlt();
	
	@Predicate
	public abstract void setLng( long b );
	public abstract Long getLng();
	
	@Predicate
	public abstract void setInt( int b );
	public abstract Integer getInt();
	
	@Predicate
	public abstract void setB( Boolean b );
	public abstract boolean isB();
	
	@Predicate
	public abstract void setC( Character b );
	public abstract char getC();	
	
	@Predicate
	public abstract void setD( Double b );
	public abstract double getD();
	
	@Predicate
	public abstract void setF( Float b );
	public abstract float getF();
	
	@Predicate
	public abstract void setL( Long b );
	public abstract long getL();
	
	@Predicate
	public abstract void setI( Integer b );
	public abstract int getI();

	@Predicate
	public abstract void setU( @URI String b );
	public abstract RDFNode getU();
	
	@Predicate
	public abstract void setU2( RDFNode n );
	@Predicate( type=URI.class )
	public abstract String getU2();
}
