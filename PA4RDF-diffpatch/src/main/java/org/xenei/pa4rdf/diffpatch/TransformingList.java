/*
 * Copyright 2017, XENEI.com
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xenei.pa4rdf.diffpatch;

import java.util.AbstractList;
import java.util.List;
import java.util.function.Function;

/**
 * Class to that uses a function to transform a list to a list of a different
 * type of object.
 *
 * @param <F>
 *            the from object as found in the original list.
 * @param <T>
 *            the to object that this list holds.
 */
public class TransformingList<F, T> extends AbstractList<T> {
    final List<F> fromList;
    final Function<? super F, ? extends T> function;

    /**
     * Check that a reference is not null.
     * 
     * @param reference
     *            the reference to check.
     * @return the reference.
     * @throws NullPointerException
     *             if the reference is null.
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * Constructor.
     * 
     * @param fromList
     *            the original list.
     * @param function
     *            the function to transform objects.
     */
    public TransformingList(List<F> fromList, Function<? super F, ? extends T> function) {
        this.fromList = checkNotNull( fromList );
        this.function = checkNotNull( function );
    }

    @Override
    public T get(int index) {
        return function.apply( fromList.get( index ) );
    }

    @Override
    public int size() {
        return fromList.size();
    }
}
