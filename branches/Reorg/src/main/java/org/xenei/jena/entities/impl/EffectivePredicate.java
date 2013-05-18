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

import com.hp.hpl.jena.rdf.model.RDFNode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;

/**
 * An class that mimics the Predicate annotation but allows processing to modify
 * the values.
 * 
 * @see {@link org.xenei.jena.entity.annotations.Precicate}
 */
class EffectivePredicate
{
	boolean upcase = false;
	String name = "";
	String namespace = "";
	String literalType = "";
	Class<?> type = null;
	boolean emptyIsNull = true;
	boolean impl = false;

	public EffectivePredicate()
	{
	}
	
	public EffectivePredicate( Method m )
	{
		if (m != null)
		{
			
			if (m.getParameterTypes().length > 0)
			{
			
				for (final Annotation a : m.getParameterAnnotations()[0])
				{
					if (a instanceof URI)
					{
						this.type = URI.class;
					}
				}
			}
			
			Subject s =m.getDeclaringClass().getAnnotation( Subject.class);
			if (s != null)
			{
				this.namespace = s.namespace();
			}
			merge( m.getAnnotation(Predicate.class));
			if (StringUtils.isBlank( name ))
			{
				try {
				ActionType actionType  = ActionType.parse(m.getName());
				setName( actionType.extractName( m.getName() ));	
				}
				catch (IllegalArgumentException e)
				{
					// expected when not an action method.
				}
			}
		}
	}
	
	public void setName(String name) {
		if (StringUtils.isNotBlank(name))
		{
			final String s = name.substring(0, 1);
			if (upcase())
			{
				this.name = name.replaceFirst(s, s.toUpperCase());
			}
			else
			{
				this.name = name.replaceFirst(s, s.toLowerCase());
			}
		}
	}

	public boolean emptyIsNull()
	{
		return emptyIsNull;
	}

	public String literalType()
	{
		return literalType;
	}

	public EffectivePredicate merge( final EffectivePredicate predicate )
	{
		if (predicate != null)
		{
			upcase = predicate.upcase();
			setName(  StringUtils.isBlank(predicate.name()) ? name : predicate
					.name() );
			namespace = StringUtils.isBlank(predicate.namespace()) ? namespace
					: predicate.namespace();
			literalType = StringUtils.isBlank(predicate.literalType()) ? literalType
					: predicate.literalType();
			type = RDFNode.class.equals(predicate.type()) ? type : predicate
					.type();
		}
		return this;
	}

	public EffectivePredicate merge( final Predicate predicate )
	{
		if (predicate != null)
		{
			upcase = predicate.upcase();
			setName( StringUtils.isBlank(predicate.name()) ? name : predicate
					.name() );
			namespace = StringUtils.isBlank(predicate.namespace()) ? namespace
					: predicate.namespace();
			literalType = StringUtils.isBlank(predicate.literalType()) ? literalType
					: predicate.literalType();
			type = RDFNode.class.equals(predicate.type()) ? type : predicate
					.type();
			emptyIsNull = predicate.emptyIsNull();
			System.out.println( predicate.type() );
		}
		return this;
	}

	public String name()
	{
		return name;
	}

	public String namespace()
	{
		return namespace;
	}

	public Class<?> type()
	{
		return type==null?RDFNode.class:type;
	}

	public boolean isTypeNotSet()
	{
		return type == null;
	}

	
	public boolean upcase()
	{
		return upcase;
	}
	
	public boolean impl()
	{
		return impl;
	}

}