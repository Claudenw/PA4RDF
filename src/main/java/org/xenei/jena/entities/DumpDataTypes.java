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
package org.xenei.jena.entities;

import org.xenei.jena.entities.impl.PredicateInfoImpl;

public class DumpDataTypes {

    /**
     * Dump the registered data types.
     *
     * Prints a list of registered data types.
     *
     * arg1 = Output format The format is a String.format format string for two
     * (2) string inputs. The first format parameter is the URI of the data
     * type, the second format parameter is the class name. The default value is
     * "%s | %s". To reverse the display use "%2$s | %1$s".
     *
     * arg2 = null class string This is the string to pring if the java class is
     * not defined. If the nullCLassString is null registered data types without
     * classes will not be printed.
     *
     * @param args
     *            the arguments
     * @see org.xenei.jena.entities.impl.PredicateInfoImpl#dataTypeDump(String,
     *      String)
     */
    public static void main(final String[] args) {
        String fmt = "%s | %s";
        String nullString = null;
        if (args.length > 0) {
            fmt = args[0];
        }

        if (args.length > 1) {
            nullString = args[1];
        }
        for (final String s : PredicateInfoImpl.dataTypeDump( fmt, nullString )) {
            System.out.println( s );
        }

    }
}
