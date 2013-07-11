 [Examples](./index.html) -> this
 
Abstract Class Example
==========

This example show how the annotations work on an abstract class.  Note that the entity manger creates an
instance of the abstract class.  This class is the similar to the [interface example](./interface.html)
except that it uses the List&lt;String&gt; return type for getAuthor() and with the addition of a cite() 
method that produces a cite entry for the book.

Book Class
==========

    @Subject( namespace="http://example.com/PA4RDF#" )
    public abstract class Book {

	    @Predicate
	    abstract void setTitle( String title );
	    abstract String getTitle();
	
	    @Predicate
	    abstract void addAuthor( String author );
	    abstract List<String> getAuthor();
	
	    @Predicate
	    abstract void setPageCount( Integer int );
	    abstract boolean hasPageCount();
	    abstract int getPageCount();
	    
	    public String cite() {
	      StringBuilder sb = new StringBuilder();
	      for (String author : getAuthor() )
	      {
	      	sb.append( author ).append( ", " );
	      }
	      sb.append( getTitle() ).append( "." );
	      if (book.hasPageCount())
	      {
	        sb.append(" pgs: ").append( getPageCount() );
	      }
	      return sb.toString();
	    }
    }

Example code setting the Book class
===================================

    Model model = ModelFactory.createModel();

    EntityManager entityManager = EntityManagerFactory.getEntityManager();
    Resource r = model.createResource( "http://example.com/mylibrary/book1" );

    Book book = EntityManager.read( r, Book.class );
    book.setTitle( "Java Generics and Collections" );
    book.addAuthor( "Maurice Naftalin" );
    book.addAuthor( "Philip Wadler" );
    book.setPageCount( 286 );

Result of example code
======================

The above code will result in the following graph:

    <http://example.com/mylibrary/book1> 
        <http://example.com/PA4RDF#title> "Java Generics and Collections" ;
        <http://example.com/PA4RDF#author> "Maurice Naftalin" ;
        <http://example.com/PA4RDF#author> "Philip Wadler" ;
        <http://example.com/PA4RDF#pageCount> "286"^^<xsd:integer> ;
    .


Example code reading the Book class
===================================

Assuming the book object has been constructed as in the first example.

    System.out.println( "Title: "+book.getTitle() );
    for (String author : book.getAuthor() )
    {
        System.out.println( "  Author: "+author );
    }
    if (book.hasPageCount())
    {
        System.out.println( "  Pages: "+book.getPageCount() );
    }
    System.out.println( book.cite() );

This will result in the following output:

    Title: Java Generics and Collections
      Author: Maurice Naftalin
      Author: Philip Wadler
      Pages: 286
    Maurice Naftalin, Philip Wadler, Java Generics and Collections. pgs: 286
