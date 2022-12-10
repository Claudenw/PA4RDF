package org.xenei.jena.entities;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;

@TestInstance(Lifecycle.PER_CLASS)
public class EntityManagerFactoryTest {

    private EntityManager savedManager;

    @BeforeAll
    public void saveEntityManager() {
        savedManager = EntityManagerFactory.getEntityManager();
    }

    @AfterAll
    public void resetEntityManager() {
        EntityManagerFactory.setEntityManager( savedManager );
    }

    @Test
    public void defaultTest() {
        Assertions.assertNotNull( EntityManagerFactory.getEntityManager() );
    }

    @Test
    public void setTest() {
        final EntityManager mgr = Mockito.mock( EntityManager.class );
        EntityManagerFactory.setEntityManager( mgr );
        Assertions.assertEquals( mgr, EntityManagerFactory.getEntityManager() );
    }
}
