package org.xenei.jena.entities.testing.iface;

import org.apache.jena.rdf.model.RDFNode;

import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;

public interface CollectionValueInterfaceDefault extends CollectionValueInterface {

    @Override
    @Predicate(impl = true)
    default void addBool(final Boolean b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addChar(final Character b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addDbl(final Double b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addEnt(final TestInterface b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addFlt(final Float b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addInt(final Integer b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addLng(final Long b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addRDF(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addStr(final String b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addU(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addU(@URI final String b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addU3(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void addU3(@URI final String b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Set<Boolean> getBool() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default List<Character> getChar() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Queue<Double> getDbl() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Queue<TestInterface> getEnt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Set<Float> getFlt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Queue<Integer> getInt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default List<Long> getLng() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default List<RDFNode> getRDF() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Set<String> getStr() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true, type = RDFNode.class)
    default Set<RDFNode> getU() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true, type = URI.class, name = "u")
    default List<String> getU2() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true, type = RDFNode.class)
    default Queue<RDFNode> getU3() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true, type = URI.class, name = "u3")
    default Set<String> getU4() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean hasBool(final Boolean b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean hasChar(final Character b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean hasDbl(final Double b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean hasEnt(final TestInterface b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean hasFlt(final Float b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean hasInt(final Integer b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean hasLng(final Long b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean hasRDF(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean hasStr(final String b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean hasU(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean hasU(@URI final String b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean hasU3(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean hasU3(@URI final String b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeBool(final Boolean b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeChar(final Character b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeDbl(final Double b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeEnt(final TestInterface b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeFlt(final Float b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeInt(final Integer b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeLng(final Long b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeRDF(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeStr(final String b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeU(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeU(@URI final String b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeU3(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeU3(@URI final String b) {
        throw new EntityManagerRequiredException();
    }

}
