package org.xenei.jena.entities.impl.handlers;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.testing.iface.TwoValueSimpleInterface;

public class EntityHandlerTest implements HandlerTestInterface {
    EntityHandler handler;
    private RDFNode node;
    private TwoValueSimpleInterface instance;

    @BeforeAll
    public static void setupClass() {
        EntityManagerFactory.setEntityManager( null );
    }

    @AfterAll
    public static void teardownClass() {
        EntityManagerFactory.setEntityManager( null );
    }

    @BeforeEach
    public void setup() throws Exception {
        handler = new EntityHandler( TwoValueSimpleInterface.class );
        node = ResourceFactory.createResource();
        instance = EntityManagerFactory.getEntityManager().read( node, TwoValueSimpleInterface.class );
    }

    @Override
    @Test
    public void testCreateRDFNode() {
        final RDFNode n = handler.createRDFNode( instance );
        Assertions.assertNotNull( n );
        Assertions.assertEquals( node, n );
    }

    @Override
    @Test
    public void testIsEmpty() {
        Assertions.assertTrue( handler.isEmpty( null ) );
        Assertions.assertFalse( handler.isEmpty( instance ) );
    }

    @Override
    @Test
    public void testParseObject() {
        final Object o = handler.parseObject( node );
        Assertions.assertNotNull( o );
        Assertions.assertTrue( o instanceof TwoValueSimpleInterface );
        final TwoValueSimpleInterface a2 = (TwoValueSimpleInterface) o;
        Assertions.assertEquals( instance, a2 );
    }

    @Test
    public void testMissingAnnotationDuringRead() {
        final RDFNode node = ResourceFactory.createResource();
        final EntityManager em = EntityManagerFactory.getEntityManager();
        EntityManagerFactory.setEntityManager( null );
        final EntityManager mockEM = Mockito.mock( EntityManager.class );
        try {
            Mockito.when( mockEM.read( ArgumentMatchers.eq( node.asResource() ),
                    ArgumentMatchers.eq( TwoValueSimpleInterface.class ) ) ).thenThrow( MissingAnnotation.class );
            handler = new EntityHandler( TwoValueSimpleInterface.class );
            Assertions.assertThrows( RuntimeException.class, () -> handler.parseObject( node ) );
        } catch (IllegalArgumentException | MissingAnnotation e) {
            Assertions.fail( e.getMessage() );
        } finally {
            EntityManagerFactory.setEntityManager( em );
        }
    }
}
