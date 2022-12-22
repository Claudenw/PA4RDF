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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.slf4j.LoggerFactory;
import org.xenei.jena.entities.impl.method.BaseMethodParser;

/**
 * An implementation of the EntityManager interface.
 *
 */
public class MethodParser extends BaseMethodParser {

    /**
     * Constructor.
     *
     * @param subjectInfo
     *            The Subject Info that we are adding to.
     * @param addCount
     *            A maping of add types to counts.
     */
    public MethodParser(final SubjectInfoImpl subjectInfo) {
        super( new Stack<Method>(), subjectInfo,
                MethodParser.countAdders( subjectInfo.getImplementedClass().getMethods() ),
                LoggerFactory.getLogger( MethodParser.class ) );
    }

    public static Map<String, Integer> countAdders(final Method[] methods) {
        final Map<String, Integer> addCount = new HashMap<>();
        for (final Method m : methods) {
            if (ActionType.SETTER.isA( m.getName() ) && (m.getParameterCount() == 1)) {
                Integer i = addCount.get( m.getName() );
                if (i == null) {
                    i = 1;
                } else {
                    i = i + 1;
                }
                addCount.put( m.getName(), i );
            }
        }
        return addCount;
    }
}
