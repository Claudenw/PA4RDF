package org.xenei.pa4rdf.bean.test.impl;

import java.util.Collection;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.junit.Ignore;
import org.xenei.pa4rdf.bean.test.iface.MultiValueInterface;
import org.xenei.pa4rdf.bean.test.iface.TestInterface;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.URI;
import org.xenei.pa4rdf.bean.exceptions.EntityFactoryRequiredException;

@Ignore( "IMpl has new unimplemented collection types -- specfifically arrays")
public class MultiValueImpl implements MultiValueInterface {

    @Override
    @Predicate(impl = true)
    public void addBool(final Boolean b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void addChar(final Character b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void addDbl(final Double b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void addEnt(final TestInterface b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void addFlt(final Float b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void addInt(final Integer b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void addLng(final Long b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void addRDF(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void addStr(final String b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void addU(final RDFNode b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void addU(@URI final String b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void addU3(final RDFNode b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void addU3(@URI final String b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public ExtendedIterator<Boolean> getBool() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public ExtendedIterator<Character> getChar() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public ExtendedIterator<Double> getDbl() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public ExtendedIterator<TestInterface> getEnt() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public ExtendedIterator<Float> getFlt() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public ExtendedIterator<Integer> getInt() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public ExtendedIterator<Long> getLng() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public ExtendedIterator<RDFNode> getRDF() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public ExtendedIterator<String> getStr() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public ExtendedIterator<RDFNode> getU() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public ExtendedIterator<String> getU2() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public ExtendedIterator<RDFNode> getU3() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public ExtendedIterator<String> getU4() {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean hasBool(final Boolean b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean hasChar(final Character b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean hasDbl(final Double b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean hasEnt(final TestInterface b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean hasFlt(final Float b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean hasInt(final Integer b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean hasLng(final Long b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean hasRDF(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean hasStr(final String b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean hasU(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean hasU(@URI final String b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean hasU3(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public Boolean hasU3(@URI final String b) {
        throw new EntityFactoryRequiredException();
    }

    @Override
    @Predicate(impl = true)
    public void removeBool(final Boolean b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeChar(final Character b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeDbl(final Double b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeEnt(final TestInterface b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeFlt(final Float b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeInt(final Integer b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeLng(final Long b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeRDF(final RDFNode b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeStr(final String b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeU(final RDFNode b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeU(@URI final String b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeU3(final RDFNode b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeU3(@URI final String b) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void setLst(Collection<String> s) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void setAry(String[] s) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void setAry2(String... s) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public Boolean hasLst(String s) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public Boolean hasAry(String s) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public Boolean hasAry2(String s) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeLst(String s) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeAry(String s) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public void removeAry2(String s) {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public Collection<String> getLst() {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public Collection<String> getAry() {
        throw new EntityFactoryRequiredException();

    }

    @Override
    @Predicate(impl = true)
    public Collection<String> getAry2() {
        throw new EntityFactoryRequiredException();

    }

}
