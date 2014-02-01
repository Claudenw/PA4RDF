 [Examples](./index.html) -> this
 
Concrete Class Extending Annotated Interface Example
==========

This example show how the annotations work on an concrete class that extends an annotated 
interface.   Note that the entity manger creates an
instance of the abstract class.  This class is the similar to the [interface example](./interface.html)
except that it uses the List&lt;String&gt; return type for getAuthor() and with the addition of a cite() 
method that produces a cite entry for the book.

Book Interface
==========

    @Subject( namespace="http://example.com/PA4RDF#" )
    public interface BookI {

	    @Predicate
	    void setTitle( String title );
	    String getTitle();
	
	    @Predicate
	    void addAuthor( String author );
	    List<String> getAuthor();
	
	    @Predicate
	    void setPageCount( Integer int );
	    boolean hasPageCount();
	    int getPageCount();
    }

Book Class
==========

    public class Book implements BookI {

	    @Override
		@Predicate( impl = true )
		public public void setTitle( String title)
		{
			throw new EntityManagerRequiredException();
		}
	    
	    @Override
		@Predicate( impl = true )
		public String getTitle()
		{
			throw new EntityManagerRequiredException();
		}
	    
	    @Override
		@Predicate( impl = true )
	    public void addAuthor( String author )
		{
			throw new EntityManagerRequiredException();
		}
	    
	    @Override
		@Predicate( impl = true )
	    public List<String> getAuthor()	{
			throw new EntityManagerRequiredException();
		}
	    
	    @Override
		@Predicate( impl = true )
	    public void setPageCount( Integer int )
	    {
			throw new EntityManagerRequiredException();
		}
	    
	    @Override
		@Predicate( impl = true )  
	    public boolean hasPageCount()
	    {
			throw new EntityManagerRequiredException();
		}
	    
	    @Override
		@Predicate( impl = true )   
	    public int getPageCount()
		{
			throw new EntityManagerRequiredException();
		}
	    
	    public String cite() 
	    {
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
    Iterator<String> authors = book.getAuthors();
    while (authors.hasNext())
    {
        System.out.println( "  Author: "+authors.next() );
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
