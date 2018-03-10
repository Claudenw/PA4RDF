package org.xenei.pa4rdf.bean.test.impl;

import org.apache.jena.rdf.model.RDFNode;
import org.xenei.pa4rdf.bean.test.iface.SingleValueObjectInterface;
import org.xenei.pa4rdf.bean.test.iface.TestInterface;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.URI;
import org.xenei.pa4rdf.bean.exceptions.EntityFactoryRequiredException;

public class SingleValueObjectImpl implements SingleValueObjectInterface {

    @Override
    @Predicate(impl = true)
    public Character getChar() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Double getDbl() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public TestInterface getEnt() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Float getFlt() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Integer getInt() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Long getLng() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public RDFNode getRDF() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public String getStr() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public SubPredicate getSubPredicate() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public RDFNode getU() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public String getU2() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean isBool() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeBool() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeChar() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeDbl() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeEnt() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeFlt() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeInt() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeLng() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeRDF() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeStr() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeSubPredicate() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeU() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setBool(final Boolean b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setChar(final Character b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setDbl(final Double b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setEnt(final TestInterface b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setFlt(final Float b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setInt(final Integer b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setLng(final Long b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setRDF(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setStr(final String b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setSubPredicate(final SubPredicate subPredicate) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setU(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setU(@URI final String b) {
        throw new EntityFactoryRequiredException();
    }

}
