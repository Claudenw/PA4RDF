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

import org.xenei.jena.entities.annotations.Predicate;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public abstract class TestPropertyRenamingInterface implements Resource {

		@Predicate( name="foo", namespace="http://localhost/different#" )
		public abstract void addFoomer( String key );
		public abstract ExtendedIterator<String> getFoomer();
		public abstract void removeFoomer( String key );
		
		@Predicate( name="foo2", namespace="http://localhost/different#" )
		public abstract void setFoomer2( String example );
		public abstract String getFoomer2();
		public abstract void removeFoomer2();
		
		@Predicate( name="foo3", namespace="http://localhost/different#" )
		public abstract void setFoomer3( Boolean example );
		public abstract Boolean isFoomer3();
		public abstract void removeFoomer3();

}
