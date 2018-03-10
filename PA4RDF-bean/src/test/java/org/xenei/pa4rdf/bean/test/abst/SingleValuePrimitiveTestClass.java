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

import org.apache.jena.rdf.model.Resource;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;

@Subject(namespace = "http://localhost/test#")
public abstract class SingleValuePrimitiveTestClass implements Resource {

    public abstract char getChar();

    public abstract double getDbl();

    public abstract float getFlt();

    public abstract int getInt();

    public abstract long getLng();

    public abstract boolean isBool();

    public abstract void removeBool();

    public abstract void removeChar();

    public abstract void removeDbl();

    public abstract void removeFlt();

    public abstract void removeInt();

    public abstract void removeLng();

    @Predicate
    public abstract void setBool(boolean b);

    @Predicate
    public abstract void setChar(char b);

    @Predicate
    public abstract void setDbl(double b);

    @Predicate
    public abstract void setFlt(float b);

    @Predicate
    public abstract void setInt(int b);

    @Predicate
    public abstract void setLng(long b);

}
