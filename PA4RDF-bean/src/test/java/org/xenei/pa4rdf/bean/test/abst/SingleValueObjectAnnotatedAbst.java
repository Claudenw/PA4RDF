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
package org.xenei.pa4rdf.bean.test.abst;

import org.apache.jena.rdf.model.RDFNode;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;
import org.xenei.pa4rdf.bean.annotations.URI;
import org.xenei.pa4rdf.bean.test.iface.TestInterface;

@Subject(namespace = "http://localhost/test#")
public abstract class SingleValueObjectAnnotatedAbst {

    @Subject(namespace = "http://localhost/test#")
    public static abstract class SubPredicate {
        public SubPredicate() {
        };

        public abstract String getName();

        @Predicate
        public abstract void setName(String name);
    };

    public abstract Character getChar();

    public abstract Double getDbl();

    public abstract TestInterface getEnt();

    public abstract Float getFlt();

    public abstract Integer getInt();

    public abstract Long getLng();

    public abstract RDFNode getRDF();

    public abstract String getStr();

    public abstract SubPredicate getSubPredicate();

    public abstract RDFNode getU();

    @Predicate(type = URI.class, name = "u")
    public abstract String getU2();

    public abstract Boolean isBool();

    public abstract void removeBool();

    public abstract void removeChar();

    public abstract void removeDbl();

    public abstract void removeEnt();

    public abstract void removeFlt();

    public abstract void removeInt();

    public abstract void removeLng();

    public abstract void removeRDF();

    public abstract void removeStr();

    public abstract void removeSubPredicate();

    public abstract void removeU();

    @Predicate
    public abstract void setBool(Boolean b);

    @Predicate
    public abstract void setChar(Character b);

    @Predicate
    public abstract void setDbl(Double b);

    @Predicate
    public abstract void setEnt(TestInterface b);

    @Predicate
    public abstract void setFlt(Float b);

    @Predicate
    public abstract void setInt(Integer b);

    @Predicate
    public abstract void setLng(Long b);

    @Predicate
    public abstract void setRDF(RDFNode b);

    @Predicate
    public abstract void setStr(String b);

    @Predicate
    public abstract void setSubPredicate(SubPredicate subPredicate);

    public abstract void setU(RDFNode b);

    @Predicate
    public abstract void setU(@URI String b);

}
