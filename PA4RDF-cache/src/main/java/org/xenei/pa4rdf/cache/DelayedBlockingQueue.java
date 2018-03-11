package org.xenei.pa4rdf.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.jena.util.iterator.WrappedIterator;




public class DelayedBlockingQueue extends DelayQueue<DelayedRunnable>  {
    
    public BlockingQueue<Runnable> asRunnableQueue() {
        return new BlockingQueue<Runnable>(){
            DelayedBlockingQueue dbq = DelayedBlockingQueue.this;
            public boolean add(Runnable e) {
                return dbq.add( DelayedRunnable.make( e ));
            }

            
            private List<DelayedRunnable> makeList( Collection<? extends Runnable> coll)
            {
               return coll.stream().map( r -> DelayedRunnable.make( r ) ).collect( Collectors.toList() ) ;
            }
            
            
            public boolean addAll(Collection<? extends Runnable> arg0) {
                return dbq.addAll(makeList( arg0 ) );
            }

            public void clear() {
                dbq.clear();
            }

            public boolean contains(Object o) {
                if (o instanceof Runnable) { 
                    return dbq.contains( DelayedRunnable.make( (Runnable)o ) );
                } 
                return false;
            }

            public boolean containsAll(Collection<?> arg0) {
                List<DelayedRunnable> lst = new ArrayList<DelayedRunnable>();
                for (Object o : arg0)
                {
                    if (o instanceof Runnable)
                    {
                        lst.add(  DelayedRunnable.make(  (Runnable)o ) );
                    }
                    else {
                        return false;
                    }
                }
            
                return dbq.containsAll( lst );
            }

            public int drainTo(Collection<? super Runnable> c, int maxElements) {
                return dbq.drainTo( c, maxElements );
            }

            public int drainTo(Collection<? super Runnable> c) {
                return dbq.drainTo( c );
            }

            public Runnable element() {
                return dbq.element();
            }                

            public void forEach(Consumer<? super Runnable> arg0) {
                dbq.forEach( arg0 );
            }
           
            public boolean isEmpty() {
                return dbq.isEmpty();
            }

            public Iterator<Runnable> iterator() {
                return WrappedIterator.create( dbq.iterator() ).mapWith( dr -> (Runnable)dr );
            }

            public boolean offer(Runnable e, long timeout, TimeUnit unit) throws InterruptedException {
                return dbq.offer( DelayedRunnable.make( e ), timeout, unit );
            }

            public boolean offer(Runnable e) {
                return dbq.offer( DelayedRunnable.make( e ) );
            }

//            public Stream<Runnable> parallelStream() {
//                return dbq.parallelStream().map(  dr -> (Runnable)dr );
//            }

            public Runnable peek() {
                return dbq.peek();
            }

            public Runnable poll() {
                return dbq.poll();
            }

            public Runnable poll(long timeout, TimeUnit unit) throws InterruptedException {
                return dbq.poll( timeout, unit );
            }

            public void put(Runnable e) throws InterruptedException {
                dbq.put( DelayedRunnable.make(e) );
            }

            public int remainingCapacity() {
                return dbq.remainingCapacity();
            }

            public Runnable remove() {
                return dbq.remove();
            }

            public boolean remove(Object o) {
                if (o instanceof Runnable)
                {
                    return dbq.remove( DelayedRunnable.make(  (Runnable)o) );
                }
                return false;
            }

            public boolean removeAll(Collection<?> arg0) {
                List<DelayedRunnable> lst = new ArrayList<DelayedRunnable>();
                for (Object o : arg0)
                {
                    if (o instanceof Runnable)
                    {
                        lst.add(  DelayedRunnable.make(  (Runnable)o ) );
                    }
                    
                }               
                return dbq.removeAll( lst );
            }

//            public boolean removeIf(java.util.function.Predicate<? super Runnable> arg0) {
//                return dbq.removeIf( arg0 );
//            }

            public boolean retainAll(Collection<?> arg0) {
                return dbq.retainAll( arg0 );
            }

            public int size() {
                return dbq.size();
            }
           
//            public Stream<Runnable> stream() {
//                return dbq.stream().map(  dr -> (Runnable) dr );
//            }

            public Runnable take() throws InterruptedException {
                return dbq.take();
            }

            public Object[] toArray() {
                return dbq.toArray();
            }

            public <T> T[] toArray(T[] arg0) {
                return dbq.toArray( arg0 );
            }
            
    };
    
    
    
}
    
}
