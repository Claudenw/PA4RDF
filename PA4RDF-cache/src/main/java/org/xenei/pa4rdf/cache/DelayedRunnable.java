package org.xenei.pa4rdf.cache;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


public interface DelayedRunnable extends Delayed, Runnable {
    
    public static DelayedRunnable make( Runnable r )
    {
        if (r instanceof DelayedRunnable)
        {
            return (DelayedRunnable)r;
        }
        Impl impl = new Impl(r);
        if (r instanceof Delayed)
        {
            impl.expires = ((Delayed) r).getDelay( TimeUnit.MILLISECONDS );
        }
        return impl;
    }
    
    public static DelayedRunnable make( Runnable r, long expires )
    {
        if (r instanceof DelayedRunnable)
        {
            if (expires == ((DelayedRunnable)r).getDelay( TimeUnit.MILLISECONDS ))
            {
                return (DelayedRunnable)r;
            }
        }
        return new Impl(r, expires);
    }
    
    public class Impl implements DelayedRunnable {
   
        private Runnable r;
        private long expires;
        
        private Impl( Runnable r)
        {
            this(r,System.currentTimeMillis());
        }

        private Impl( Runnable r ,long expires)
        {
            this.r = r;
            this.expires = expires;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert( expires-System.currentTimeMillis(), TimeUnit.MILLISECONDS );
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(  getDelay( TimeUnit.MILLISECONDS), o.getDelay(  TimeUnit.MILLISECONDS ) );
        }

        @Override
        public void run() {
            r.run();
        }
        
        @Override
        public boolean equals( Object o )
        {
            if (o instanceof Impl)
            {
                return r.equals( ((Impl )o).equals(  r  ));
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return r.hashCode();
        }
                    
    }
}
