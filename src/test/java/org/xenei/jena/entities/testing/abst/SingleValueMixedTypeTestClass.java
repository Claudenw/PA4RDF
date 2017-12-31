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
package org.xenei.jena.entities.testing.abst;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;

@Subject(namespace = "http://localhost/test#")
public abstract class SingleValueMixedTypeTestClass implements Resource {

    public abstract char getC();

    public abstract Character getChar();

    public abstract double getD();

    public abstract Double getDbl();

    public abstract float getF();

    public abstract Float getFlt();

    public abstract int getI();

    public abstract Integer getInt();

    public abstract long getL();

    public abstract Long getLng();

    public abstract RDFNode getU();

    @Predicate(type = URI.class)
    public abstract String getU2();

    public abstract boolean isB();

    public abstract Boolean isBool();

    @Predicate
    public abstract void setB(Boolean b);

    @Predicate
    public abstract void setBool(boolean b);

    @Predicate
    public abstract void setC(Character b);

    @Predicate
    public abstract void setChar(char b);

    @Predicate
    public abstract void setD(Double b);

    @Predicate
    public abstract void setDbl(double b);

    @Predicate
    public abstract void setF(Float b);

    @Predicate
    public abstract void setFlt(float b);

    @Predicate
    public abstract void setI(Integer b);

    @Predicate
    public abstract void setInt(int b);

    @Predicate
    public abstract void setL(Long b);

    @Predicate
    public abstract void setLng(long b);

    @Predicate
    public abstract void setU(@URI String b);

    @Predicate
    public abstract void setU2(RDFNode n);
}
