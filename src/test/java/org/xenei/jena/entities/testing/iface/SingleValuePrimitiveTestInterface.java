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

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;

@Subject(namespace = "http://localhost/test#")
public interface SingleValuePrimitiveTestInterface {

    char getChar();

    double getDbl();

    float getFlt();

    int getInt();

    long getLng();

    boolean isBool();

    void removeBool();

    void removeChar();

    void removeDbl();

    void removeFlt();

    void removeInt();

    void removeLng();

    @Predicate
    void setBool(boolean b);

    @Predicate
    void setChar(char b);

    @Predicate
    void setDbl(double b);

    @Predicate
    void setFlt(float b);

    @Predicate
    void setInt(int b);

    @Predicate
    void setLng(long b);

}
