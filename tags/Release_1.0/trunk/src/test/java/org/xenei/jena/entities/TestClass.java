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

import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

@Subject( namespace = "http://localhost/test#" )
public abstract class TestClass
{

	@Predicate
	abstract public void setBar( String value );

	abstract public String getBar();

	abstract public void removeBar();

	@Predicate
	abstract public void addBaz( String str );

	abstract public ExtendedIterator<String> getBaz();

	abstract public void removeBaz( String str );

	@Predicate
	abstract public void setFlag( Boolean state );

	abstract public Boolean isFlag();

	abstract public void removeFlag();

}