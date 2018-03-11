package org.xenei.pa4rdf.cache;

import java.lang.reflect.Method;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.xenei.pa4rdf.cache.model.CachingModel;
import org.xenei.pa4rdf.cache.model.ModelInterceptor;
import org.xenei.pa4rdf.cache.model.ModelInterceptor.Intercepted;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * A class that ensures that there is a hard reference between the resource and
 * its subject table.
 */
public class ResourceInterceptor implements MethodInterceptor {

    //
    private final Resource res;

    private final ModelInterceptor modelInterceptor;

    /**
     * Constructor.
     * 
     * @param modelInterceptor  The model interceptor to use.
     * @param res
     *            the resource
     */
    public ResourceInterceptor(ModelInterceptor modelInterceptor, Resource res) {
        this.modelInterceptor = modelInterceptor;
        this.res = res;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        final Object retval = method.invoke( res, args );
        if (retval instanceof Model && !(retval instanceof Intercepted)) {
            final Enhancer e = new Enhancer();
            e.setInterfaces( new Class[] { CachingModel.class, Intercepted.class } );
            e.setCallback( modelInterceptor );
            return e.create();
        }
        return modelInterceptor.wrapResource( retval );
    }

    // protected ModelCom mustHaveModel()
    // {
    // ModelCom model = getModelCom();
    // if (model == null) throw new HasNoModelException( this );
    // return model;
    // }

}