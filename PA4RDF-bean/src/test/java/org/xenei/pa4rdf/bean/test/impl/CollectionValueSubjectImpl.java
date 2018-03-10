package org.xenei.pa4rdf.bean.test.impl;

import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.jena.rdf.model.RDFNode;
import org.xenei.pa4rdf.bean.test.iface.TestInterface;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;
import org.xenei.pa4rdf.bean.annotations.URI;
import org.xenei.pa4rdf.bean.exceptions.EntityFactoryRequiredException;

@Subject(namespace = "http://localhost/test#")
public class CollectionValueSubjectImpl {

    @Predicate(impl = true)
    public void addBool(final Boolean b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void addChar(final Character b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void addDbl(final Double b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void addEnt(final TestInterface b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void addFlt(final Float b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void addInt(final Integer b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void addLng(final Long b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void addRDF(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void addStr(final String b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void addU(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void addU(@URI final String b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void addU3(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void addU3(@URI final String b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Set<Boolean> getBool() {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public List<Character> getChar() {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Queue<Double> getDbl() {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Queue<TestInterface> getEnt() {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Set<Float> getFlt() {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Queue<Integer> getInt() {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public List<Long> getLng() {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public List<RDFNode> getRDF() {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Set<String> getStr() {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true, type = RDFNode.class)
    public Set<RDFNode> getU() {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true, type = URI.class, name = "u")
    public List<String> getU2() {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true, type = RDFNode.class)
    public Queue<RDFNode> getU3() {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true, type = URI.class, name = "u3")
    public Set<String> getU4() {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Boolean hasBool(final Boolean b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Boolean hasChar(final Character b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Boolean hasDbl(final Double b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Boolean hasEnt(final TestInterface b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Boolean hasFlt(final Float b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Boolean hasInt(final Integer b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Boolean hasLng(final Long b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Boolean hasRDF(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Boolean hasStr(final String b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Boolean hasU(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Boolean hasU(@URI final String b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Boolean hasU3(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public Boolean hasU3(@URI final String b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void removeBool(final Boolean b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void removeChar(final Character b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void removeDbl(final Double b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void removeEnt(final TestInterface b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void removeFlt(final Float b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void removeInt(final Integer b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void removeLng(final Long b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void removeRDF(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void removeStr(final String b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void removeU(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void removeU(@URI final String b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void removeU3(final RDFNode b) {
        throw new EntityFactoryRequiredException();
    }

    @Predicate(impl = true)
    public void removeU3(@URI final String b) {
        throw new EntityFactoryRequiredException();
    }

}
