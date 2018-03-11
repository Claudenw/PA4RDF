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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import org.apache.jena.sparql.core.Quad;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.WrappedIterator;

import difflib.Chunk;

/**
 * A class that implements a List of Quads by wrapping multiple Chunks of Quads.
 *
 */
public class QuadList implements List<Quad> {
    /**
     * The list of chunks.
     */
    protected final List<Chunk<Quad>> lst;

    /**
     * A Span that defines the start and end of this list relative to the start
     * of the list property.
     */
    protected Span span;

    /**
     * Constructor.
     */
    public QuadList() {
        this( new ArrayList<Chunk<Quad>>(), 0 );
    }

    /**
     * Constructor for SubList usage.
     * 
     * @param lst
     *            the list of Chunks to use.
     * @param first
     *            the index origin within the list for this list.
     */
    protected QuadList(final List<Chunk<Quad>> lst, final int first) {
        if (first < 0) {
            throw new IndexOutOfBoundsException( "first = " + first );
        }
        this.lst = lst;

        this.span = first == 0 ? new ListSpan() : new ListSpan() {

            @Override
            public int getStart() {
                return first;
            }
        };

    }

    /**
     * Verify that the index is in range of this list.
     * 
     * @param index
     *            the index to check.
     */
    protected void rangeCheck(final int index) {
        if (!span.contains( index )) {
            throw new IndexOutOfBoundsException( "Index '" + index + "' out of bounds for '" + span + "'" );
        }
    }

    @Override
    public boolean add(Quad e) {
        final Quad[] qa = { e };
        return addAll( Arrays.asList( qa ) );
    }

    @Override
    public void add(int index, Quad element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends Quad> c) {
        if (c.isEmpty()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final List<Quad> l = (c instanceof List) ? (List<Quad>) c : new ArrayList<Quad>( c );

        if (lst.isEmpty()) {
            return lst.add( new Chunk<Quad>( 0, l ) );
        } else {
            Chunk<Quad> chk = lst.get( lst.size() - 1 );
            final int offset = chk.getPosition() + chk.size();
            chk = new Chunk<Quad>( offset, l );
            return lst.add( new Chunk<Quad>( offset, l ) );
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends Quad> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        lst.clear();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Quad) {
            final Quad q = (Quad) o;
            final Iterator<Quad> iter = iterator();
            while (iter.hasNext()) {
                if (q.equals( iter.next() )) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (final Object o : c) {
            if (!contains( o )) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Quad get(int index) {
        int pos = index + span.getStart();
        if (!span.contains( pos )) {
            throw new IndexOutOfBoundsException( index + "<0" );
        }

        for (final Chunk<Quad> l : lst) {
            final ChunkSpan cSpan = new ChunkSpan( l );
            if (cSpan.contains( pos )) {
                pos = Span.Util.positionOf( cSpan, pos );
                return l.getLines().get( pos );
            }
        }
        throw new IllegalStateException( "Position " + index + " not found" );
    }

    @Override
    public int indexOf(Object o) {
        int idx;
        for (final Chunk<Quad> l : lst) {
            final ChunkSpan cSpan = new ChunkSpan( l );
            if (span.overlap( cSpan )) {
                int fromIndex = 0;
                int toIndex = cSpan.getLength();
                if (span.getStart() > cSpan.getStart()) {
                    fromIndex = span.getStart() - cSpan.getStart();
                }
                if (span.getEnd() < cSpan.getEnd()) {
                    toIndex -= (cSpan.getEnd() - span.getEnd());
                }
                final List<Quad> lq = l.getLines().subList( fromIndex, toIndex );
                idx = lq.indexOf( o );
                if (idx > -1) {
                    idx += fromIndex;
                    return idx - span.getStart();
                }
            }
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return lst.isEmpty();
    }

    @Override
    public Iterator<Quad> iterator() {
        return listIterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = lst.size(); i > 0; i--) {
            final Chunk<Quad> chk = lst.get( i - 1 );
            final ChunkSpan cSpan = new ChunkSpan( chk );
            if (span.overlap( cSpan )) {
                int fromIndex = 0;
                int toIndex = cSpan.getLength();
                if (span.getStart() > cSpan.getStart()) {
                    fromIndex = span.getStart() - cSpan.getStart();
                }
                if (span.getEnd() < cSpan.getEnd()) {
                    toIndex -= (cSpan.getEnd() - span.getEnd());
                }
                final List<Quad> lq = chk.getLines().subList( fromIndex, toIndex );
                int idx = lq.lastIndexOf( o );
                if (idx > -1) {
                    idx += fromIndex;
                    return idx - span.getStart();
                }
            }
        }
        return -1;
    }

    @Override
    public ListIterator<Quad> listIterator() {
        return new QuadIterator();
    }

    @Override
    public ListIterator<Quad> listIterator(int index) {
        return new QuadIterator( index );
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Quad remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Quad set(int index, Quad element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        if (lst.isEmpty()) {
            return 0;
        }
        return span.getLength();
    }

    @Override
    public List<Quad> subList(int fromIndex, int toIndex) {
        return new SubList( lst, fromIndex, toIndex );
    }

    @Override
    public Object[] toArray() {
        final Quad[] retval = new Quad[size()];
        for (int i = 0; i < size(); i++) {
            retval[i] = get( i );
        }
        return retval;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size()) {
            return (T[]) toArray();
        }
        for (int i = 0; i < size(); i++) {
            a[i] = (T) get( i );
        }
        return a;
    }

    /**
     * A ListIterator over the QuadList.
     *
     */
    private class QuadIterator implements ListIterator<Quad> {

        private int idx;
        private ListIterator<Quad> iter;

        /**
         * Constructor
         * 
         * @param index
         *            the offset for the start of the iterator.
         */
        protected QuadIterator(int index) {
            this.idx = -1;
            iter = null;
            if (index < span.getLength()) {
                final int cIdx = index + span.getStart();
                for (int i = 0; i < lst.size(); i++) {
                    final Chunk<Quad> cnk = lst.get( i );
                    final ChunkSpan cSpan = new ChunkSpan( cnk );
                    if (cSpan.contains( cIdx )) {
                        this.iter = cnk.getLines().listIterator( cIdx );
                        this.idx = i;
                        return;
                    }
                }
            }
        }

        /**
         * Constructor. Starts at position 0.
         */
        protected QuadIterator() {
            this( 0 );
        }

        @Override
        public void forEachRemaining(Consumer<? super Quad> action) {
            if (iter == null) {
                return;
            }
            final ExtendedIterator<Quad> eIter = WrappedIterator.create( iter );
            for (int i = idx + 1; i < lst.size(); i++) {
                eIter.andThen( lst.get( i ).getLines().listIterator() );
            }

        }

        @Override
        public boolean hasNext() {
            if (iter == null) {
                return false;
            }
            if (iter.hasNext()) {
                return true;
            }
            idx++;
            iter = idx < lst.size() ? lst.get( idx ).getLines().listIterator() : null;
            return hasNext();
        }

        @Override
        public Quad next() {
            if (hasNext()) {
                return iter.next();
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException( "remove() is not supported" );
        }

        @Override
        public void set(Quad obj) {
            throw new UnsupportedOperationException( "set() is not supported" );
        }

        @Override
        public void add(Quad obj) {
            throw new UnsupportedOperationException( "add() is not supported" );
        }

        @Override
        public boolean hasPrevious() {

            if (iter != null) {
                if (iter.hasPrevious()) {
                    return true;
                }
            }

            if (lst.isEmpty()) {
                return false;
            }
            for (int i = idx; i > 0; i--) {
                final Chunk<Quad> chk = lst.get( i );
                if (!chk.getLines().isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int nextIndex() {
            if (iter == null) {
                return size();
            }
            if (iter.hasNext()) {
                final Chunk<Quad> chk = lst.get( idx );
                return chk.getPosition() + iter.nextIndex();
            }
            if (idx + 1 < lst.size()) {
                final Chunk<Quad> chk = lst.get( idx + 1 );
                chk.getPosition();
            }
            return size();
        }

        @Override
        public Quad previous() {
            if (hasPrevious()) {
                if (iter != null) {
                    return iter.previous();
                }

                for (int i = idx; i > 0; i--) {
                    final Chunk<Quad> chk = lst.get( i );
                    if (!chk.getLines().isEmpty()) {
                        iter = lst.get( i ).getLines().listIterator();
                        idx = i;
                        while (iter.hasNext()) {
                            iter.next();
                        }
                        return iter.previous();
                    }
                }
            }
            throw new NoSuchElementException();
        }

        @Override
        public int previousIndex() {
            if (iter != null) {
                if (iter.hasPrevious()) {
                    return iter.previousIndex();
                }
            }

            if (lst.isEmpty()) {
                return -1;
            }
            for (int i = idx; i > 0; i--) {
                final Chunk<Quad> chk = lst.get( i );
                if (!chk.getLines().isEmpty()) {
                    return chk.getPosition() + chk.size() - 1;
                }
            }
            return -1;
        }

    }

    /**
     * SubList for a QuadList.
     * 
     * The quad list with boundaries.
     *
     */
    protected static class SubList extends QuadList {

        /**
         * Constructor.
         * 
         * List is [fromIndex, toIndex).
         * 
         * @param parent
         *            the list of Chunk<Quads> for this sublist.
         * @param fromIndex
         *            the index to start at inclusive.
         * @param toIndex
         *            the index to end at exclusive.
         */
        protected SubList(final List<Chunk<Quad>> parent, final int fromIndex, final int toIndex) {
            super( parent, fromIndex );
            if (toIndex > span.getEnd()) {
                throw new IndexOutOfBoundsException( "toIndex = " + toIndex );
            }
            if (fromIndex > toIndex) {
                throw new IllegalArgumentException( "fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")" );
            }
            this.span = SpanFactory.fromLength( span.getStart(), toIndex - fromIndex );
        }

        @Override
        public void add(final int index, final Quad obj) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Quad remove(final int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(final Collection<? extends Quad> coll) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(final int index, final Collection<? extends Quad> coll) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Quad set(final int index, final Quad obj) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Quad> subList(final int fromIndexInclusive, final int toIndexExclusive) {
            return new SubList( lst, fromIndexInclusive + span.getStart(), toIndexExclusive + span.getStart() );
        }

        // protected void checkModCount() {
        // if (parent.modCount != expectedModCount) {
        // throw new ConcurrentModificationException();
        // }
        // }
    }

    /**
     * A span that that uses the list lenght as its terminus length. This span
     * violates the standard span contract in that it is mutable as long as the
     * list is mutable.
     */
    private class ListSpan extends AbstractSpan {

        @Override
        public int getStart() {
            return 0;
        }

        @Override
        public int getLength() {
            return Span.Util.calcLength( this );
        }

        @Override
        public int getEnd() {
            return lst.get( lst.size() - 1 ).last();
        }
    }

    /**
     * A span that is calculated from a Chunk.
     *
     */
    private class ChunkSpan extends AbstractSpan {
        private final Chunk<?> chunk;

        /**
         * Constructor.
         * 
         * @param chunk
         *            the chunk to create a span from.
         */
        private ChunkSpan(Chunk<?> chunk) {
            this.chunk = chunk;
        }

        @Override
        public int getStart() {
            return chunk.getPosition();
        }

        @Override
        public int getLength() {
            return Span.Util.calcLength( span );
        }

        @Override
        public int getEnd() {
            return chunk.last();
        }
    }
}
