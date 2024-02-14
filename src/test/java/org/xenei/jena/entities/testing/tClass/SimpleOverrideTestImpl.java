package org.xenei.jena.entities.testing.tClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.apache.jena.rdf.model.Resource;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.testing.iface.SimpleInterface;

public class SimpleOverrideTestImpl implements SimpleInterface, ResourceWrapper, List<String> {

    private final Resource resource;
    private final List<String> list;

    public SimpleOverrideTestImpl(final Resource resource) {
        this.resource = resource;
        list = new ArrayList<>();
    }

    @Override
    public String getX() {
        return "THE DATA: ";
    }

    public String output(final String txt) {
        list.add( txt );
        return txt;
    }

    @Override
    @Predicate(impl = true)
    public boolean hasX() {
        throw new IllegalStateException();
    }

    @Override
    @Predicate(impl = true)
    public void removeX() {
        throw new IllegalStateException();
    }

    @Override
    @Predicate(impl = true)
    public void setX(final String x) {
        throw new IllegalStateException();
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public void add(final int arg0, final String arg1) {
        list.add( arg0, arg1 );
    }

    @Override
    public boolean add(final String e) {
        return list.add( e );
    }

    @Override
    public boolean addAll(final Collection<? extends String> c) {
        return list.addAll( c );
    }

    @Override
    public boolean addAll(final int arg0, final Collection<? extends String> arg1) {
        return list.addAll( arg0, arg1 );
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean contains(final Object o) {
        return list.contains( o );
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return list.containsAll( c );
    }

    @Override
    public boolean equals(final Object o) {
        return list.equals( o );
    }

    @Override
    public void forEach(final Consumer<? super String> action) {
        list.forEach( action );
    }

    @Override
    public String get(final int index) {
        return list.get( index );
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public int indexOf(final Object o) {
        return list.indexOf( o );
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public Iterator<String> iterator() {
        return list.iterator();
    }

    @Override
    public int lastIndexOf(final Object o) {
        return list.lastIndexOf( o );
    }

    @Override
    public ListIterator<String> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<String> listIterator(final int index) {
        return list.listIterator( index );
    }

    @Override
    public Stream<String> parallelStream() {
        return list.parallelStream();
    }

    @Override
    public String remove(final int index) {
        return list.remove( index );
    }

    @Override
    public boolean remove(final Object o) {
        return list.remove( o );
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return list.removeAll( c );
    }

    @Override
    public boolean removeIf(final java.util.function.Predicate<? super String> filter) {
        return list.removeIf( filter );
    }

    @Override
    public void replaceAll(final UnaryOperator<String> operator) {
        list.replaceAll( operator );
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return list.retainAll( c );
    }

    @Override
    public String set(final int arg0, final String arg1) {
        return list.set( arg0, arg1 );
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void sort(final Comparator<? super String> c) {
        list.sort( c );
    }

    @Override
    public Spliterator<String> spliterator() {
        return list.spliterator();
    }

    @Override
    public Stream<String> stream() {
        return list.stream();
    }

    @Override
    public List<String> subList(final int arg0, final int arg1) {
        return list.subList( arg0, arg1 );
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(final IntFunction<T[]> generator) {
        return list.toArray( generator );
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return list.toArray( a );
    }

}
