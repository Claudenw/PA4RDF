Book Class
==========

    @Subject( namespace="http://example.com/PAR#" )
    public class Book {

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
        <http://example.com/PAR#title> "Java Generics and Collections" ;
        <http://example.com/PAR#author> "Maurice Naftalin" ;
        <http://example.com/PAR#author> "Philip Wadler" ;
        <http://example.com/PAR#pageCount> "286"^^<xsd:integer> ;
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
      Page: 286
