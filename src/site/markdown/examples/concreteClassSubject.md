Concrete Class Example
==========

This example show how the annotations work on a concrete class.  Note that the entity manger creates an
instance of the class.  This class is the similar to the [concrete class extending annotated interface 
example](./concreteClass.html) except that it directly annotates the methods.

Book Class
==========

	@Subject( namespace="http://example.com/PA4RDF#" )
    public class Book {
    
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
		@Predicate( impl = true, type=String.class )
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
