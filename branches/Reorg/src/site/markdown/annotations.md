Annotations
=====

Persistence Annotation for RDF introduces three (3) annotations:

** @Subject
@Subject denotes a class or interface as mapping to an RDF Subject.  Any subject in the triple store can be so mapped.  This annotation takes two (2) arguments:
* namespace (required) defines the default namespace for the @Predicate annotations.
* types (optional) a list of uri strings that define types that the RDF Property underlying the Subject must have to be considered.

** @Predicate
 
@Predicate denotes a method of an @Subject annotated class or interface as mapping to an RDF Predicate.  The values of @Predicate annotated methods are the objects in the resulting triples.
* emptyIsNull (optional) a boolean that, if true, indicates that empty strings should be treated as nulls.  Empty string values will not be inserted into the graph and, if read from the graph, will be returned as nulls.
* literalType (optional) a string representation of the URI for the RDF (XML) Type of the literal.  If both literalType and type are specified the literalType takes precedence.  See [Data Types](./dataTypes.html) for more information.  
* name (optional) the local name, the local or fully qualified predicate name. if not specified the name of the function with the action prefix (is, get, has, etc.) removed.  if a simple name the namespace property of this annotation or the namespace of the enclosing @Subject will be used.  if a fully qualified name then the namespace property is not required.  
* namespace (optional) The namespace for the underlying property.  If not specified the enclosing @Subject namespace will be used.
* type (optional) This is the Java class that is returned from getter methods.  In general it is not required except where collections or iterators are being retrieved.  See [Data Types](./dataTypes.html) for more information. 
* upcase (optional) a boolean value that indicates that the first character of the local property name should be upper cased or not.  By default the local names are lower case.

When used on a non abstract class (as opposed to an abstract class or interface), the @Predicate annotation indicates that the method is implementing a predicate annotated abstract method.

** @URI
@URI denotes a string method parameter that should be treated as a URI as opposed to a normal string value.  May be used as an annotation on a parameter or as a type in an @Predicate annotation to indicate that the returned string must be a URI value.



Methods Processed
===

In addition to the standard setX(), getX(), and isX() methods PAR introduces four (4) more that exploit the RDF storage model.  The complete set of annotatable methods are: 

Setters
---

setX() Set a single value.  This method implies that only one X value may exist for the subject.

addX() Add another value.  This method implies that the subject may have multiple X values.

removeX() Remove the predicate.  This method removes the predicate/value pair as opposed to setting the value to null as setX(null) would do. 


Getters
---

getX() get the value for single values or an instance of an iterator or collection of values for multiple value subjects.  Three types of Collection are supported: List, Set and Queue. 
- If the value is a primitive type and there is no predicate a NullPointerException is thrown.
- If the value is an Object type and there is no predicate null is returned.
- If there are multiple values an arbitrary value is selected and returned.

isX() for boolean values returns true if the value of the predicate is true, false otherwise.  If there are multiple values an arbitrary value is selected.

hasX() returns true if there is a value of X for the subject.  This is an existential method and returns True if the predicate exists, false otherwise.  


Supported Data Types
===

PAR will convert to/from any registered typed literal including the standard Java primitives and wrapped primitive types.

If the return type or parameter of an @Predicate annotated is another @Subject annotated class PAR will automatically create and map the class as appropriate.

For a complete discussion of data types and data type conversions see the [Data Types](./dataTypes.html) page.

