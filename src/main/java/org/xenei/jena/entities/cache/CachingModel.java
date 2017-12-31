package org.xenei.jena.entities.cache;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.xenei.jena.entities.cache.ModelInterceptor.Intercepted;
import org.xenei.jena.entities.impl.EntityManagerImpl;

import net.sf.cglib.proxy.Enhancer;

public interface CachingModel extends Model {
    public static CachingModel makeInstance(EntityManagerImpl entityManager) {
        final ModelInterceptor interceptor = new ModelInterceptor( entityManager );
        final Enhancer e = new Enhancer();
        e.setInterfaces( new Class[] { CachingModel.class, Intercepted.class } );
        e.setCallback( interceptor );
        return (CachingModel) e.create();
    }

    /**
     * Synchronize the underlying CachingGraph.
     */
    public void sync();

    public void clear();

    /**
     * Adopt the resource into this graph.
     * 
     * @param r
     *            the resource to adopt
     * @return the adopted resource.
     */
    public Resource adopt(Resource r);

}
