package org.xenei.jena.entities;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

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
        final EntityManager mgr = new TestEntityManager();
        EntityManagerFactory.setEntityManager( mgr );
        Assertions.assertEquals( mgr, EntityManagerFactory.getEntityManager() );
    }

    @Test
    public void setNullTest() {
        Assertions.assertThrows( EntityManagerRequiredException.class,
                () -> EntityManagerFactory.setEntityManager( null ) );
    }
}
