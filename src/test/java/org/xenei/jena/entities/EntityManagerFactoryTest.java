package org.xenei.jena.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
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
        assertNotNull( EntityManagerFactory.getEntityManager() );
    }
    
    @Test
    public void setTest() {
        EntityManager mgr = new TestEntityManager();
        EntityManagerFactory.setEntityManager( mgr );
        assertEquals( mgr, EntityManagerFactory.getEntityManager());
    }
    
    @Test
    public void setNullTest() {
        assertThrows( EntityManagerRequiredException.class, () -> EntityManagerFactory.setEntityManager( null ));
    }
}
