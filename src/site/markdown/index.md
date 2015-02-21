Persistence Annotation for RDF (PA4RDF)
=====

Persistence Annotation for RDF (PA4RDF) is a set of annotations and an entity manager that provides JPA like functionality on top of an RDF store while accounting for and exploiting the fundamental differences between graph storage and relational storage.  PA4RDF introduces three (3) annotations that map a RDF triple (subject, predicate, object) to a Plain Old Java Object (POJO) using Java's dynamic proxy capabilities.

PA4RDF was originally intended to annotate an interface that defined the Java access to the underlying 
properties of a subject.  The over arching goal was to read and write the data directly into the graph 
without copying in and out of java objects.  This goal removes the synchronization issues found in most
persistence frameworks.

However as larger projects were developed, using interfaces became unwieldly and additions were made 
to allow concrete implementations to be overridden by the PA4RDF functionality.  For a complete
discussion of the use of PA4RDF annotations against interface, abstract and concrete classes see
the [Usage Strategies](./usageStrategies.html) page.

The interface is defined in generic terms but the only extant implementation is for the Apache Jena RDF implementation.

Maven
===

    <dependency>
        <groupId>org.xenei</groupId>
        <artifactId>PA4RDF</artifactId>
	</dependency>

Snapshots: [https://oss.sonatype.org/content/repositories/snapshots/org/xenei/PA4RDF]

Releases: [https://oss.sonatype.org/content/repositories/releases/org/xenei/PA4RDF]

Annotations
===

PA4RDF utilizes a total of 3 annotations to describe the mapping between Java and RDF Graphs.  In addition a total of 6 methods 

Supported Data Types
===

PA4RDF will convert to/from any registered typed literal including the standard Java primitives and wrapped primitive types.

If the return type or parameter of an @Predicate annotated is another @Subject annotated class PA4RDF will automatically create and map the class as appropriate.

For a complete discussion of data types and data type conversions see the [Data Types](./dataTypes.html) page.


License
===

PA4RDF is licensed under the Apache 2 license.
