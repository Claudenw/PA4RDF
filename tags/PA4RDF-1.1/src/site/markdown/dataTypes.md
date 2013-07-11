
# Data Types #

PA4RDF will convert to/from any registered typed literal including the standard Java primitives and wrapped primitive types.

If the return type or parameter of an @Predicate annotated is another @Subject annotated class PA4RDF will automatically create and map the class as appropriate.

## Data Type Mapping ##

To perform the conversion the following steps are taken:

The RDFDatatype associated with the return type is looked up using the TypeMapper.  If found the return type value is created using the the RDFDatatype.  

Otherwise: if the return type class is annotated with @Subject the class is automatically created by the entity manager.

Otherwise: if the return type is an instance of RDFNode the graph node is returned.

Otherwise: if the return type is the URI class the URI of the graph node is returned. 

Otherwise: null is returned.


### Default Java Type to Data Type Mapping ###

Java Type | RDF Type
--------- | --------
 byte[] | http://www.w3.org/2001/XMLSchema#base64Binary 
 char | http://www.w3.org/2001/XMLSchema#string 
 com.hp.hpl.jena.datatypes.xsd.XSDDateTime | http://www.w3.org/2001/XMLSchema#dateTime 
 com.hp.hpl.jena.datatypes.xsd.XSDDuration | http://www.w3.org/2001/XMLSchema#duration 
 java.lang.Boolean | http://www.w3.org/2001/XMLSchema#boolean 
 java.lang.Byte | http://www.w3.org/2001/XMLSchema#byte 
 java.lang.Double | http://www.w3.org/2001/XMLSchema#double 
 java.lang.Float | http://www.w3.org/2001/XMLSchema#float 
 java.lang.Integer | http://www.w3.org/2001/XMLSchema#int 
 java.lang.Long | http://www.w3.org/2001/XMLSchema#long 
 java.lang.Short | http://www.w3.org/2001/XMLSchema#short 
 java.lang.String | http://www.w3.org/2001/XMLSchema#normalizedString 
 java.math.BigDecimal | http://www.w3.org/2001/XMLSchema#decimal 
 java.math.BigInteger | http://www.w3.org/2001/XMLSchema#integer 
 java.net.URI | http://www.w3.org/2001/XMLSchema#anyURI 

Thus is a @Property annotated setter method takes a Float argument the data type as stored in the graph will be http://www.w3.org/2001/XMLSchema#float.

### Default Data Type to Java Mapping ###

RDF Type | Java Type
-------- | ---------
 http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral |   
 http://www.w3.org/2001/XMLSchema#ENTITY |   
 http://www.w3.org/2001/XMLSchema#ID |   
 http://www.w3.org/2001/XMLSchema#IDREF |   
 http://www.w3.org/2001/XMLSchema#NCName |   
 http://www.w3.org/2001/XMLSchema#NMTOKEN |   
 http://www.w3.org/2001/XMLSchema#NOTATION |   
 http://www.w3.org/2001/XMLSchema#Name |   
 http://www.w3.org/2001/XMLSchema#QName |   
 http://www.w3.org/2001/XMLSchema#anySimpleType |   
 http://www.w3.org/2001/XMLSchema#anyURI | java.net.URI 
 http://www.w3.org/2001/XMLSchema#base64Binary | byte[] 
 http://www.w3.org/2001/XMLSchema#boolean | java.lang.Boolean 
 http://www.w3.org/2001/XMLSchema#byte | java.lang.Byte 
 http://www.w3.org/2001/XMLSchema#date |   
 http://www.w3.org/2001/XMLSchema#dateTime | com.hp.hpl.jena.datatypes.xsd.XSDDateTime 
 http://www.w3.org/2001/XMLSchema#decimal | java.math.BigDecimal 
 http://www.w3.org/2001/XMLSchema#double | java.lang.Double 
 http://www.w3.org/2001/XMLSchema#duration | com.hp.hpl.jena.datatypes.xsd.XSDDuration 
 http://www.w3.org/2001/XMLSchema#float | java.lang.Float 
 http://www.w3.org/2001/XMLSchema#gDay |   
 http://www.w3.org/2001/XMLSchema#gMonth |   
 http://www.w3.org/2001/XMLSchema#gMonthDay |   
 http://www.w3.org/2001/XMLSchema#gYear |   
 http://www.w3.org/2001/XMLSchema#gYearMonth |   
 http://www.w3.org/2001/XMLSchema#hexBinary |   
 http://www.w3.org/2001/XMLSchema#int | java.lang.Integer 
 http://www.w3.org/2001/XMLSchema#integer | java.math.BigInteger 
 http://www.w3.org/2001/XMLSchema#language |   
 http://www.w3.org/2001/XMLSchema#long | java.lang.Long 
 http://www.w3.org/2001/XMLSchema#negativeInteger |   
 http://www.w3.org/2001/XMLSchema#nonNegativeInteger |   
 http://www.w3.org/2001/XMLSchema#nonPositiveInteger |   
 http://www.w3.org/2001/XMLSchema#normalizedString | java.lang.String 
 http://www.w3.org/2001/XMLSchema#positiveInteger |   
 http://www.w3.org/2001/XMLSchema#short | java.lang.Short 
 http://www.w3.org/2001/XMLSchema#string | char 
 http://www.w3.org/2001/XMLSchema#time |   
 http://www.w3.org/2001/XMLSchema#token |   
 http://www.w3.org/2001/XMLSchema#unsignedByte |   
 http://www.w3.org/2001/XMLSchema#unsignedInt |   
 http://www.w3.org/2001/XMLSchema#unsignedLong |   
 http://www.w3.org/2001/XMLSchema#unsignedShort |   

## Primitive Data Types ##

Primitive data types are automatically converted to their non-primitive form as necessary.
One caveat: if the return type of a @Property annotated method is a primitive and the data element does not exist in the graph a NullPointerException is thrown.

## Adding Additional Data Types ##

Additional data types may be registered using the standard Jena process:

1 Create a class that extends com.hp.hpl.jena.datatypes.RDFDatatype.
2 Register the class with the com.hp.hpl.jena.datatypes.TypeMapper.

<pre>
com.hp.hpl.jena.datatypes. rtype = new MyDatatype();
com.hp.hpl.jena.datatypes.TypeMapper.getInstance().registerDatatype(rtype);
</pre>

## Displaying Registered Data Types ##


Registered data types may be displayed using org.xenei.jena.entities.DumpDataTypes

#### Arguments ####

1. Output format. The format is a String.format format string for two (2) string inputs. The default is "%s | %s".  To reverse the display use "%2$s | %1$s".
* The first format parameter is the URI of the data type, 
* The second format parameter is the class name.
2. null class string. This is the string to print if the java class is not defined.If the nullCLassString is null registered data types without classes will not be printed.

