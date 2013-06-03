Usage Strategies
=====

PA4RDF was originally intended to annotate an interface that defined the Java access to the underlying 
properties of a subject.  The overarching goal was to read and write the data directly into the graph 
without copying in and out of java objects.  This goal removes the synchronization issues found in most
persistence frameworks.

However as larger projects were developed, using interfaces became unwieldly and additions were made 
to allow concrete implementations to be overridden by the PA4RDF functionality. 

Interface And Abstract Class Annotations
===

The original usage for PA4DRF was to annotate an interface and the use the dynamic proxy to produce a 
concrete implementation of the interface.  This strategy works equally well with interfaces and 
abstract classes.  However once the abstract class become too complex it becomes difficult to maintain
the code and the logic.

When annotating interfaces and abstract classes the @Subject annotation must be used to identify that
the interface/class is to be handled by the PA4RDF entity manager.  

Any method that should be handled by the EntityManger should be annotated with a @Predicate annotation with the following exceptions:

* Annotating a getter (e.g. getX or isX) method will automatically include the setters (i.e. setX or addX, and removeX) and the existential method hasX.

* Annotating a getter that return an RDFNode type will automatically include the setters, and existential methods that have @URI annotated parameters.

* Annotating a setter (e.g. addX or setX) method will automatically include the getter (i.e. getX), removeX and the existential method hasX.

* Annotating a setter that accepts an RDFNode type will automatically include the setters, getters, and existential methods that have @URI annotated parameters.

* Annotating a setter that accepts a String parameter that is annotated with @URI will automatically include the setters, getters, and existential methods that have RDFNode parameters.

Methods are processed in groups based on whether methods are single or multiValue.

Single value methods are:

* valueClass getX() -- where the valueClass is not an ExtendedIterator or Collection class.

* boolean isX() -- where the valueClass is a boolean.

* void setX( valueClass )

* boolean hasX()

* void removeX()

Multivalue methods are:

* ExtendedIterator&lt;valueClass&gt; getX()

* Colllection&lt;valueClass&gt; getX()

* void addX( valueClass )

* boolean hasX( valueClass )

* void removeX( valueClass )
 
If a getter or setter for a group is annotated with @Predicate all other members of the group will be 
processed as if they were annotated as well.

Concrete Class Annotations
===

A concrete class may extend an annotated interface or abstract class, these classes must not have
the @Subject annotation.  The overridden methods must be annotated with @Predicate( impl=true ) annotations.  Methods that are annotated
in this way should be written as follows so that if the class is used without being loaded by the 
entity manager an exception will be thrown:

	@Override
	@Predicate( impl = true )
	public String getX()
	{
		throw new EntityManagerRequiredException();
	}

This strategy allows for the creation of concrete classes that still retrieve data from the graph as expected for interface and abstract classes.  

A concrete class be annotated with @Subject.  In this case all methods that are to be intercepted must be annotated with full @Predicate annotations as would be used in an interface and must include the 
impl=true setting as shown below:

	@Predicate( impl = true )
	public String getX()
	{
		throw new EntityManagerRequiredException();
	}  

As with the above, this strategy will throw an exception if the method is not used within a entity manager. 

Future Directions
===

There is a plan to add a concrete annotation that works more like the standard JPA annotations in that
values will be loaded into the java object.  However, this will be done utilizing graph listeners and that will automatically update the data in the object when the data in the graph changes.  To facilitate this all setters will be intercepted by the entity manager and the data will be updated in the graph.  The object will listen for changes in the graph and will update the local version of the data when those changes are discovered.  Getters will access the local version of the data.


