Interface Example
==========

This example show how the annotations work on an interface.  Note that the entity manger creates an
instance of the interface and no direct coding for the Book interface is ever done.

Book Interface
==========

    @Subject( namespace="http://example.com/PA4RDF#" )
    public interface Book {

	    @Predicate
	    void setTitle( String title );
	    String getTitle();
	
	    @Predicate
	    void addAuthor( String author );
	    ExtendedIterator<String> getAuthor();
	
	    @Predicate
	    void setPageCount( Integer int );
	    boolean hasPageCount();
	    int getPageCount();
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

This will result in the following output:

    Title: Java Generics and Collections
      Author: Maurice Naftalin
      Author: Philip Wadler
      Pages: 286
