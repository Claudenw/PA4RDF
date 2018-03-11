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

/**
 * Abstract implementation of Span
 *
 */
public abstract class AbstractSpan implements Span {

    @Override
    public final boolean overlap(Span other) {
        return Span.Util.overlaps( this, other );
    }

    @Override
    public final boolean contains(int pos) {
        return Span.Util.contains( this, pos );
    }

    @Override
    public final String toString() {
        return Span.Util.toString( this );
    }
}