package org.xenei.pa4rdf.bean.test.impl;

import org.xenei.pa4rdf.bean.test.iface.SimpleInterface;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.exceptions.EntityFactoryRequiredException;

public class SimpleInterfaceImpl implements SimpleInterface {
    public String lastGetX;

    public SimpleInterfaceImpl() {
    }

    @Override
    @Predicate(impl = true, postExec = "postGetX")
    public String getX() {
        throw new EntityFactoryRequiredException();
    }

    public String postGetX(String s) {
        lastGetX = s;
        return s;
    }

    @Override
    @Predicate(impl = true)
    public boolean hasX() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeX() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setX(final String x) {
        throw new EntityFactoryRequiredException();
    }

}
