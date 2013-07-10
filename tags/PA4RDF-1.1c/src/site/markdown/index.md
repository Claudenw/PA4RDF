Persistence Annotation for RDF (PAR)
=====

Persistence Annotation for RDF (PAR) is a set of annotations and an entity manager that provides JPA like functionality on top of an RDF store while accounting for and exploiting the fundamental differences between graph storage and relational storage.  PAR introduces three (3) annotations that map a RDF triple (subject, predicate, object) to a Plain Old Java Object (POJO) using Java's dynamic proxy capabilities.

The interface is defined in generic terms but the only extant implementation is for the Apache Jena RDF implementation.

Maven
===

Group Id: org.xenei
Artifact Id: Jena-PA


Annotations
===

PA4RDF utilizes a total of 3 annotations to describe the mapping between Java and RDF Graphs.  In addition a total of 6 methods 

Supported Data Types
===

PAR will convert to/from any registered typed literal including the standard Java primitives and wrapped primitive types.

If the return type or parameter of an @Predicate annotated is another @Subject annotated class PAR will automatically create and map the class as appropriate.

For a complete discussion of data types and data type conversions see the [Data Types] wiki page.


License
===

PAR is licensed under the Apache 2 license.
