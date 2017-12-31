package org.xenei.jena.entities.cache;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.xenei.jena.entities.impl.EntityManagerImpl;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * A class that ensures that there is a hard reference between the resource and
 * its subject table.
 */
public class ModelInterceptor implements MethodInterceptor {
    public interface Intercepted {
    };

    private final CachingGraph graph;
    private final Model model;
    private final Map<Byte, List<SoftReference<Resource>>> resourceCache;

    private static final Method SYNC;
    private static final Method ADOPT;
    private static final Method CLEAR;

    static {

        try {
            SYNC = CachingModel.class.getMethod( "sync" );
            ADOPT = CachingModel.class.getMethod( "adopt", Resource.class );
            CLEAR = CachingModel.class.getMethod( "clear" );
        } catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalStateException( e );
        }
    }

    /**
     * Constructor.
     * 
     * @param res
     *            the resource
     * @param entityManagerImpl
     *            TODO
     */
    public ModelInterceptor(EntityManagerImpl entityManager) {
        this.graph = new CachingGraph( entityManager );
        this.model = ModelFactory.createModelForGraph( graph );
        this.resourceCache = new HashMap<Byte, List<SoftReference<Resource>>>();

    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (CLEAR.equals( method )) {
            graph.clear();
            return null;
        }

        if (SYNC.equals( method )) {
            graph.sync( false );
            return null;
        }

        if (ADOPT.equals( method )) {
            Resource r = (Resource) args[0];
            if (r.isURIResource()) {
                r = model.createResource( r.getURI() );
            }

            else if (r.isAnon()) {
                r = model.createResource( r.getId() );
            } else {
                throw new IllegalStateException( "Must Resource be URI or Anon" );
            }

            return getResourceFromCache( calcBloomFilter( r ), r );
        }

        return wrapResource( method.invoke( model, args ) );

    }

    public Object wrapResource(Object retval) {
        if (retval instanceof Resource && !(retval instanceof Intercepted)) {
            final Resource r = (Resource) retval;
            final Class<?> clazz = retval.getClass();
            final SubjectTable tbl = graph.getTable( r.asNode() );
            final ResourceInterceptor interceptor = new ResourceInterceptor( this, tbl, r );
            final Enhancer e = new Enhancer();
            final List<Class<?>> lst = new ArrayList<Class<?>>( Arrays.asList( clazz.getInterfaces() ) );
            lst.add( Intercepted.class );
            e.setInterfaces( lst.toArray( new Class[lst.size()] ) );
            e.setCallback( interceptor );
            return e.create();
        }
        return retval;
    }

    private synchronized Resource getResourceFromCache(Byte b, Resource r) {
        List<SoftReference<Resource>> lst = resourceCache.get( b );
        if (lst != null) {
            final Iterator<SoftReference<Resource>> iter = lst.iterator();
            while (iter.hasNext()) {
                final SoftReference<Resource> wr = iter.next();
                final Resource r2 = wr.get();
                if (r2 == null) {
                    iter.remove();
                } else {
                    if (r2.equals( r )) {
                        return r2;
                    }
                }
            }
        } else {
            lst = new ArrayList<SoftReference<Resource>>();
            resourceCache.put( b, lst );
        }

        final Resource retval = (Resource) wrapResource( r );
        // if (r.isAnon())
        // {
        // retval = createResource( r.getId());
        // } else {
        // retval = createResource( r.getURI());
        // }
        lst.add( new SoftReference<Resource>( retval ) );
        return retval;
    }

    private Byte calcBloomFilter(Resource r) {
        String name = null;
        if (r.isAnon()) {
            name = r.getId().toString();
        } else {
            name = r.getURI();
        }

        short first = (short) (name.hashCode() & 0xFFFF);
        final short second = (short) (((name.hashCode() & 0xFFFF0000) >> 16) & 0xFFFF);
        byte b = 0;
        for (int i = 0; i < 3; i++) {
            b |= (1 << Math.abs( first % (short) 8 ));
            first += second;
        }
        return Byte.valueOf( b );
    }

}