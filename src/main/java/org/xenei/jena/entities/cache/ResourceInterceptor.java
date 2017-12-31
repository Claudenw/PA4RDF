package org.xenei.jena.entities.cache;

import java.lang.reflect.Method;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.xenei.jena.entities.cache.ModelInterceptor.Intercepted;

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

    // private final static Method MUST_HAVE_MODEL;
    //
    // static {
    // try
    // {
    // MUST_HAVE_MODEL = ResourceImpl.class.getMethod("mustHaveModel");
    // } catch (NoSuchMethodException | SecurityException e)
    // {
    // throw new IllegalStateException( e );
    // }
    // }

    /**
     * Constructor.
     * 
     * @param res
     *            the resource
     * @param entityManagerImpl
     *            TODO
     */
    public ResourceInterceptor(ModelInterceptor modelInterceptor, SubjectTable tbl, Resource res) {
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