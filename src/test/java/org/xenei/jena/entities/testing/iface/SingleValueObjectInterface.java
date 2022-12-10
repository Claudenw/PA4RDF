/*
 * Copyright 2012, XENEI.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xenei.jena.entities.testing.iface;

import org.apache.jena.rdf.model.RDFNode;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;

@Subject(namespace = "http://localhost/test#")
public interface SingleValueObjectInterface {

    @Subject(namespace = "http://localhost/test#")
    public interface SubPredicate {
        String getName();

        @Predicate
        void setName(String name);
    };

    Character getChar();

    Double getDbl();

    TestInterface getEnt();

    Float getFlt();

    Integer getInt();

    Long getLng();

    RDFNode getRDF();

    String getStr();

    SubPredicate getSubPredicate();

    RDFNode getU();

    @Predicate(type = URI.class, name = "u")
    String getU2();

    Boolean isBool();

    void removeBool();

    void removeChar();

    void removeDbl();

    void removeEnt();

    void removeFlt();

    void removeInt();

    void removeLng();

    void removeRDF();

    void removeStr();

    void removeSubPredicate();

    void removeU();

    Boolean hasBool();

    Boolean hasChar();

    Boolean hasDbl();

    Boolean hasEnt();

    Boolean hasFlt();

    Boolean hasInt();

    Boolean hasLng();

    Boolean hasRDF();

    Boolean hasStr();

    Boolean hasSubPredicate();

    Boolean hasU();

    @Predicate
    void setBool(Boolean b);

    @Predicate
    void setChar(Character b);

    @Predicate
    void setDbl(Double b);

    @Predicate
    void setEnt(TestInterface b);

    @Predicate
    void setFlt(Float b);

    @Predicate
    void setInt(Integer b);

    @Predicate
    void setLng(Long b);

    @Predicate
    void setRDF(RDFNode b);

    @Predicate
    void setStr(String b);

    @Predicate
    void setSubPredicate(SubPredicate subPredicate);

    void setU(RDFNode b);

    @Predicate
    void setU(@URI String b);

}
