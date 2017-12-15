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

import org.apache.jena.rdf.model.RDFNode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;

/**
 * An class that mimics the Predicate annotation but allows processing to modify
 * the values.
 *
 * @see org.xenei.jena.entities.annotations.Predicate
 */
public class EffectivePredicate
{
	boolean upcase = false;
	String name = "";
	String namespace = "";
	String literalType = "";
	Class<?> type = null;
	Class<?> contained = RDFNode.class;
	boolean emptyIsNull = true;
	boolean impl = false;
	List<Method> postExec = null;

	public EffectivePredicate()
	{
	}

	public EffectivePredicate( final EffectivePredicate ep )
	{
		this();
		merge(ep);
	}

	public EffectivePredicate( final Method m )
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

			final Subject s = m.getDeclaringClass()
					.getAnnotation(Subject.class);
			if (s != null)
			{
				this.namespace = s.namespace();
			}
			Predicate p = m.getAnnotation(Predicate.class); 
			merge(p);
			if (p != null)
			{
				if (StringUtils.isNotBlank(p.postExec()))
				{
					String mName = p.postExec().trim();
					try
					{
						Method peMethod = null;
						Class<?> argType = null;
						final ActionType actionType = ActionType.parse(m.getName());
						switch (actionType)
						{
							case GETTER:
								argType = m.getReturnType();
								break;
								
							case SETTER:
							case EXISTENTIAL:
							case REMOVER:
								if (m.getParameterTypes().length != 1)
								{
									throw new RuntimeException( String.format( "%s does not have a single parameter", peMethod ));
								}
								argType = m.getParameterTypes()[0];
								break;
						}
						peMethod = m.getDeclaringClass().getMethod(mName, argType);
						if ( actionType==ActionType.GETTER && !argType.equals( peMethod.getReturnType()))
						{
						    throw new RuntimeException( String.format( "%s does not return its parameter type", peMethod ));
						}
                        addPostExec( peMethod );
					}
					catch (NoSuchMethodException e)
					{
						throw new RuntimeException( "Error parsing predicate annotation", e );
					}
					catch (SecurityException e)
					{
						throw new RuntimeException( "Error parsing predicate annotation", e );
					}
					catch (IllegalArgumentException e)
					{
						throw new RuntimeException( "Error parsing predicate annotation action type", e );
					}
				}
			}
			if (StringUtils.isBlank(name))
			{
				try
				{
					final ActionType actionType = ActionType.parse(m.getName());
					setName(actionType.extractName(m.getName()));
				}
				catch (final IllegalArgumentException e)
				{
					// expected when not an action method.
				}
			}
		}
	}

	public EffectivePredicate( final Predicate p )
	{
		this();
		merge(p);
	}
	
	public void addPostExec( Collection<Method> peMethods)
	{
		for (Method m : peMethods)
		{
			addPostExec( m );
		}
	}
	
	public void addPostExec( Method peMethod )
	{
		if (postExec == null)
		{
			postExec = new ArrayList<Method>();
		}
		if (! postExec.contains(peMethod))
		{
			postExec.add( peMethod );
		}
	}

	public boolean emptyIsNull()
	{
		return emptyIsNull;
	}

	public boolean impl()
	{
		return impl;
	}

	public boolean isTypeNotSet()
	{
		return type == null;
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
			setName(StringUtils.isBlank(predicate.name()) ? name : predicate
					.name());
			namespace = StringUtils.isBlank(predicate.namespace()) ? namespace
					: predicate.namespace();
			literalType = StringUtils.isBlank(predicate.literalType()) ? literalType
					: predicate.literalType();
			type = RDFNode.class.equals(predicate.type()) ? type : predicate
					.type();
			contained = contained==null?predicate.contained():contained;
			impl |= predicate.impl();
		}
		return this;
	}

	public EffectivePredicate merge( final Predicate predicate )
	{
		if (predicate != null)
		{
			upcase = predicate.upcase();
			setName(StringUtils.isBlank(predicate.name()) ? name : predicate
					.name());
			namespace = StringUtils.isBlank(predicate.namespace()) ? namespace
					: predicate.namespace();
			literalType = StringUtils.isBlank(predicate.literalType()) ? literalType
					: predicate.literalType();
			type = RDFNode.class.equals(predicate.type()) ? type : predicate
					.type();
			contained = predicate.contained();
			emptyIsNull = predicate.emptyIsNull();
			impl |= predicate.impl();
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

	public void setName( final String name )
	{
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

	public Class<?> type()
	{
		return type == null ? RDFNode.class : type;
	}

	public Class<?> contained()
	{
		return contained;// == null ? RDFNode.class : type;
	}
	
	public boolean upcase()
	{
		return upcase;
	}
	
	
}