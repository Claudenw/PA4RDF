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
 * A factory to create spans.
 *
 */
public class SpanFactory {

    /**
     * Construct a span from a starting position and an endpoint.
     * 
     * @param start
     *            The starting position.
     * @param end
     *            The endpoint
     * @return the new Span.
     */
    public static Span fromEnd(int start, int end) {
        return new SpanImpl( start, end - start + 1 );
    }

    /**
     * Create a span from a starting position and a length.
     * 
     * @param start
     *            the starting position.
     * @param length
     *            the length.
     * @return the new Span.
     */
    public static Span fromLength(int start, int length) {
        return new SpanImpl( start, length );
    }

    /**
     * An implementation of Span for factory use.
     *
     */
    private static class SpanImpl extends AbstractSpan {

        private final int start;
        private final int length;

        /**
         * Constructor using a starting position and a length. To construct
         * using a starting position and an endpoint use fromEnd().
         * 
         * @param start
         *            The starting position.
         * @param length
         *            The length.
         */
        SpanImpl(int start, int length) {
            Span.Util.checkIntAddLimit( start, length );
            if (length < 0) {
                throw new IndexOutOfBoundsException( "Length may not be less than zero: " + length );
            }
            this.start = start;
            this.length = length;
        }

        @Override
        public final int getStart() {
            return start;
        }

        @Override
        public final int getLength() {
            return length;
        }

        @Override
        public final int getEnd() {
            return Span.Util.calcEnd( this );
        }

    }
}
