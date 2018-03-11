package org.xenei.pa4rdf.cache.model;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.xenei.pa4rdf.cache.DelayedRunnable;
import org.xenei.pa4rdf.cache.model.ModelInterceptor.Intercepted;

import net.sf.cglib.proxy.Enhancer;

public interface CachingModel extends Model {
    
    /**
     * Create an instance of the caching model.
     * @param entityManager the entity manager to use
     * @return the CachingModel in the entitiyManager.
     */
    public static CachingModel makeInstance(ModelInterceptor interceptor, ExecutorService executorService) {
        final Enhancer e = new Enhancer();
        e.setInterfaces( new Class[] { CachingModel.class, Intercepted.class } );
        e.setCallback( interceptor );
        CachingModel cModel = (CachingModel) e.create();        
        new ModelRefresh( cModel, executorService);
        return cModel;
    }

    /**
     * Synchronize the underlying CachingGraph.
     */
    public void sync();


    /**
     * Remove all statements from the underlying graph
     */
    public void clear();

    /**
     * Refresh all entities that are not currently being modified.
     */
    public void refresh();
    
    /**
     * Adopt the resource into this graph.
     * 
     * @param r
     *            the resource to adopt
     * @return the adopted resource.
     */
    public Resource adopt(Resource r);

    /**
     * Class to perform refresh on the model.
     * @author claude
     *
     */
    public static class ModelRefresh implements DelayedRunnable
    {       
        private WeakReference<CachingModel> sr;
        private long expires;
        private ExecutorService executorService;
        
        ModelRefresh( CachingModel model, ExecutorService executorService )
        {
            sr = new WeakReference<CachingModel>( model );            
            setExpires();
            this.executorService = executorService;
            executorService.execute(  this  );
        }
        
        private void setExpires()
        {
            expires = System.currentTimeMillis()+60000;
        }
        
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert( expires-System.currentTimeMillis(), TimeUnit.MILLISECONDS );
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare( getDelay( TimeUnit.MILLISECONDS), o.getDelay(  TimeUnit.MILLISECONDS ) );
        }

        @Override
        public void run() {
            CachingModel cm = sr.get();
            if (cm != null)
            {
                cm.refresh(); 
                // reset the expires time
                setExpires();
                // resubmit to caching service
                executorService.execute( this );
            }
        }           
    }
}
