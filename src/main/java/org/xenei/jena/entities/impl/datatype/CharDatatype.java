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
package org.xenei.jena.entities.impl.datatype;

import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.xsd.impl.XSDBaseStringType;

/**
 * An instance of XSDBaseStringType that converts char.class objects into single
 * character strings and back again.
 */
public class CharDatatype extends XSDBaseStringType {

    public static final CharDatatype INSTANCE = new CharDatatype();

    private CharDatatype() {
        super( "string", char.class );
    }

    /**
     * Parse the single character string into a char.class object.
     *
     * @param lexical
     *            The single character string
     * @throws DatatypeFormatException
     *             if lexical is more than one character
     */
    @Override
    public Object parseValidated(final String lexical) {
        final String val = lexical.trim();
        if (val.length() != 1) {
            throw new DatatypeFormatException( lexical, this, "more than 1 character" );
        }

        return Character.valueOf( val.charAt( 0 ) ).charValue();
    }

}