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
import org.apache.jena.rdf.model.Resource;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.annotations.URI;

@Subject(namespace = "http://localhost/test#")
public interface SingleValueMixedTypeTestInterface {

    char getC();

    Character getChar();

    double getD();

    Double getDbl();

    float getF();

    Float getFlt();

    int getI();

    Integer getInt();

    long getL();

    Long getLng();

    RDFNode getU();

    @Predicate(type = URI.class)
    String getU2();

    boolean isB();

    Boolean isBool();

    @Predicate
    void setB(Boolean b);

    @Predicate
    void setBool(boolean b);

    @Predicate
    void setC(Character b);

    @Predicate
    void setChar(char b);

    @Predicate
    void setD(Double b);

    @Predicate
    void setDbl(double b);

    @Predicate
    void setF(Float b);

    @Predicate
    void setFlt(float b);

    @Predicate
    void setI(Integer b);

    @Predicate
    void setInt(int b);

    @Predicate
    void setL(Long b);

    @Predicate
    void setLng(long b);

    @Predicate
    void setU(@URI String b);

    @Predicate
    void setU2(RDFNode n);
}
