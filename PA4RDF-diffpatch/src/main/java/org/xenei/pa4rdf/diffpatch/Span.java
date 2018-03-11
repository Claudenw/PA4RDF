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
 * Describes a span of data.
 * 
 * Includes the starting point, ending point, length and methods to determine if
 * points are within the span or spans overlap.
 *
 */
public interface Span {
    /**
     * Starting position
     * 
     * @return start position
     */
    int getStart();

    /**
     * Length of the span.
     * 
     * @return the length of the span.
     */
    int getLength();

    /**
     * Ending position of span
     * 
     * @return end position
     */
    int getEnd();

    /**
     * Return true if the spans share any positions.
     * 
     * @param other
     *            The other span
     * @return true if overlap
     */
    boolean overlap(Span other);

    /**
     * Return true if this span contains the position.
     * 
     * @param pos
     *            the position to check for.
     * @return true if start &lt;= pos &lt;= end
     */
    boolean contains(int pos);

    /**
     * A class of static methods providing reference implementations
     * implementing specific methods in the Span interface as well as some
     * helper methods used within the Span implementation.
     *
     */
    public static class Util {
        /**
         * Return true if the spans share any positions with ourselves or any
         * parent. Standard implementation of Span.hasOverlap
         * 
         * @param one
         *            the first span
         * @param other
         *            The other span
         * @return true if there is an overlap
         */
        public static boolean overlaps(Span one, Span other) {
            if (one.getEnd() < other.getStart() || one.getStart() > other.getEnd()) {
                return false;
            }

            return true;
        }

        /**
         * Returns true if the span contains the position.
         * 
         * @param span
         *            The span
         * @param pos
         *            The position
         * @return true if the span contains the position.
         */
        public static boolean contains(Span span, int pos) {
            return span.getStart() <= pos && span.getEnd() >= pos;
        }

        /**
         * A method to calculate the end of a span from the start and length.
         * Intended to be used by span implementations that store start and
         * length.
         * 
         * @param span
         *            The span to calculate end for
         * @return The end position of the span
         */
        public static int calcEnd(Span span) {
            return span.getStart() + span.getLength() - 1;
        }

        /**
         * A method to calculate the length of a span from the start and end.
         * Intended to be used by span implementations that stoare start and
         * end.
         * 
         * @param span
         *            The span to calculate end for
         * @return The end position of the span
         */
        public static int calcLength(Span span) {
            return span.getEnd() - span.getStart() + 1;
        }

        /**
         * Return the longer of the 2 spans. If the spans are the same length
         * returns other.
         * 
         * @param one
         *            The first span.
         * @param other
         *            The other span.
         * @return The longer of the two spans.
         */
        public static Span longest(Span one, Span other) {
            return one.getLength() > other.getLength() ? one : other;
        }

        /**
         * Return the shorter of the 2 spans. If the spans are the same length
         * returns one.
         * 
         * @param one
         *            The first span.
         * @param other
         *            The other span.
         * @return The shorter of the two spans.
         */
        public static Span shortest(Span one, Span other) {
            return one.getLength() > other.getLength() ? other : one;
        }

        /**
         * Check for over flow when calculating end position.
         * 
         * @param start
         *            The starting position
         * @param increment
         *            the length
         * @return the long value created by adding start and increment.
         * @throws IllegalArgumentException
         *             if the result if not within [Long.MIN_VALUE,
         *             Long.MAX_VALUE]
         */
        public static long checkIntAddLimit(long start, long increment) {
            if (increment == 0) {
                return start;
            }
            if (increment < 0) {
                // this really subtracts
                if (Integer.MIN_VALUE - increment > start) {
                    throw new IllegalArgumentException( String.format( "Start (%s) - length (%s) < Long.MIN_VALUE (%s)",
                            start, increment, Integer.MIN_VALUE ) );

                }
            } else {
                if ((Integer.MAX_VALUE - increment) < start) {
                    throw new IllegalArgumentException( String.format( "Length (%s) + Start (%s) > Long.MAX_VALUE (%s)",
                            increment, start, Integer.MAX_VALUE ) );
                }
            }
            return start + increment;
        }

        /**
         * create the default string for the span.
         * 
         * @param span
         *            The span to get the string for
         * @return The printable string
         */
        public static String toString(Span span) {
            return String.format( "%s[%s,%s]", span.getClass().getName(), span.getStart(),
                    span.getLength() > 0 ? span.getEnd() : "-empty-" );

        }

        /**
         * Given a span returns the position in absolute positioning. (e.g.
         * pos-span.start )
         * 
         * @param span
         *            the span to calcualte with.
         * @param pos
         *            the position.
         * @return the absolute position of the span.
         */
        public static int positionOf(Span span, int pos) {
            return pos - span.getStart();
        }

    }

}
