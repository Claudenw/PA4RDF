package org.xenei.jena.entities.impl.predicate.impl;

import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.junit.Test;
import org.xenei.jena.entities.EntityManagerRequiredException;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.URI;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.predicate.AbstractPredicateTest;
import org.xenei.jena.entities.impl.predicate.REVIEW.REVIEW_AbstractCollectionURITest;
import org.xenei.jena.entities.testing.impl.SimpleURICollectionSubjectImpl;

public class SimpleURICollectionSubjectImplTest extends AbstractPredicateTest {
    public SimpleURICollectionSubjectImplTest() {
        super( SimpleURICollectionSubjectImpl.class );
        builder.setNamespace( "http://example.com/" )        
        .setImpl(  true  );

    }

    
    
    @Test
    public void testAddU_R() throws Exception {       
        builder.setActionType( ActionType.SETTER )
        .setName(  "u" )
        .setInternalType(  RDFNode.class )
        .setType(  RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "addU", RDFNode.class ) );
      
    }
    
    @Test
    public void testAddU_S() throws Exception {       
        builder.setActionType( ActionType.SETTER )
        .setName(  "u" )
        .setInternalType(  RDFNode.class )
        .setType(  URI.class );
        assertSame( builder, interfaceClass.getMethod( "addU", String.class ) );
      
    }
    
    @Test
    public void testAddU2_R() throws Exception {       
        builder.setActionType( ActionType.SETTER )
        .setName(  "u2" )
        .setInternalType(  RDFNode.class )
        .setType(  RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "addU2", RDFNode.class ) );
      
    }
    
    @Test
    public void testAddU2_S() throws Exception {       
        builder.setActionType( ActionType.SETTER )
        .setName(  "u2" )
        .setInternalType(  RDFNode.class )
        .setType( URI.class );
        assertSame( builder, interfaceClass.getMethod( "addU2", String.class ) );
      
    }
   
    @Test
    public  void testgetU() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setName( "u" )
        .setCollectionType( List.class );
        assertSame( builder, interfaceClass.getMethod( "getU" ) );
    }

    @Test
    public  void testgetU2() throws Exception {
        builder.setActionType( ActionType.GETTER )
        .setName( "u2" )
        .setInternalType( RDFNode.class )
        .setCollectionType( ExtendedIterator.class )
        .setType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "getU2" ) );
    }
    
    @Test
    public final void testHasU() throws Exception {
        builder.setActionType( ActionType.EXISTENTIAL )
        .setName( "u" )
        .setInternalType(  RDFNode.class )
        .setType(  URI.class );
        assertSame( builder, interfaceClass.getMethod( "hasU", String.class ) );
    } 


    @Test
    public final void testHasU2() throws Exception {
        builder.setActionType( ActionType.EXISTENTIAL )
        .setName( "u2" )
        .setInternalType(  RDFNode.class )
        .setType(  URI.class );
        assertSame( builder, interfaceClass.getMethod( "hasU2", String.class ) );
    } 

   
    @Test
    public final void testRemoveU() throws Exception {
        builder.setActionType( ActionType.REMOVER )
        .setName( "u" )
        .setInternalType(  RDFNode.class )
        .setType(  URI.class );
        assertSame( builder, interfaceClass.getMethod( "removeU", String.class ) );
    }

    @Test
    public final void testRemoveU2_R() throws Exception {
        builder.setActionType( ActionType.REMOVER )
        .setName( "u2" )
        .setInternalType(  RDFNode.class )
        .setType( RDFNode.class );
        assertSame( builder, interfaceClass.getMethod( "removeU2", RDFNode.class ) );
    }

    @Test
    public final void testRemoveU2_S() throws Exception {
        builder.setActionType( ActionType.REMOVER )
        .setName( "u2" )
        .setInternalType(  RDFNode.class )
        .setType( URI.class );
        assertSame( builder, interfaceClass.getMethod( "removeU2", String.class ) );
    }
}
