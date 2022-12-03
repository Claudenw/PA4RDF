package org.xenei.jena.entities.testing.iface;

import org.apache.jena.rdf.model.RDFNode;

import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;

public interface SingleValueObjectInterfaceDefault extends SingleValueObjectInterface {

    @Override
    @Predicate(impl = true)
    default Character getChar() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Double getDbl() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default TestInterface getEnt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Float getFlt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Integer getInt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Long getLng() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default RDFNode getRDF() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default String getStr() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default SubPredicate getSubPredicate() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default RDFNode getU() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default String getU2() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default Boolean isBool() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeBool() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeChar() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeDbl() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeEnt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeFlt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeInt() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeLng() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeRDF() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeStr() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeSubPredicate() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void removeU() {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setBool(final Boolean b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setChar(final Character b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setDbl(final Double b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setEnt(final TestInterface b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setFlt(final Float b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setInt(final Integer b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setLng(final Long b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setRDF(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setStr(final String b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setSubPredicate(final SubPredicate subPredicate) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setU(final RDFNode b) {
        throw new EntityManagerRequiredException();
    }

    @Override
    @Predicate(impl = true)
    default void setU(@URI final String b) {
        throw new EntityManagerRequiredException();
    }

}
