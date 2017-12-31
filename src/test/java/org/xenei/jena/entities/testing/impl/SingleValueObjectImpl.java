package org.xenei.jena.entities.testing.impl;

import org.apache.jena.rdf.model.RDFNode;
import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.testing.iface.SingleValueObjectInterface;
import org.xenei.jena.entities.testing.iface.TestInterface;

public class SingleValueObjectImpl implements SingleValueObjectInterface {

    @Override
    @Predicate(impl = true)
    public Character getChar() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Double getDbl() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public TestInterface getEnt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Float getFlt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Integer getInt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Long getLng() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public RDFNode getRDF() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public String getStr() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public SubPredicate getSubPredicate() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public RDFNode getU() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public String getU2() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean isBool() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeBool() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeChar() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeDbl() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeEnt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeFlt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeInt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeLng() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeRDF() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeStr() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeSubPredicate() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeU() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setBool(final Boolean b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setChar(final Character b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setDbl(final Double b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setEnt(final TestInterface b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setFlt(final Float b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setInt(final Integer b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setLng(final Long b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setRDF(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setStr(final String b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setSubPredicate(final SubPredicate subPredicate) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setU(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void setU(@URI final String b) {
        throw new EntityManagerRequiredException();
    }

}
