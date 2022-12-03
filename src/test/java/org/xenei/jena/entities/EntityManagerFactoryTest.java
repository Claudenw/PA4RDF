package org.xenei.jena.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.xenei.jena.entities.annotations.Subject;

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
        EntityManager mgr = new EntityManager() {

            @Override
            public Subject getSubject(Class<?> clazz) {
                return null;
            }

            @Override
            public SubjectInfo getSubjectInfo(Class<?> clazz) {
                return null;
            }

            @Override
            public boolean isInstance(Object target, Class<?> clazz) {
                return false;
            }

            @Override
            public void parseClasses(String packageName) throws MissingAnnotation {}

            @Override
            public void parseClasses(String[] packageNames) throws MissingAnnotation {}

            @Override
            public <T> T make(Object source, Class<T> primaryClass, Class<?>... secondaryClasses)
                    throws MissingAnnotation {
                return null;
            }

            @Override
            public <T> T read(Object source, Class<T> primaryClass, Class<?>... secondaryClasses)
                    throws MissingAnnotation, IllegalArgumentException {
                return null;
            }

            @Override
            public <T> T addInstanceProperties(T source, Class<?> clazz) throws MissingAnnotation {
                return null;
            }

            @Override
            public Object update(Object source, Object target) {
                return null;
            }

            @Override
            public void reset() {}
        };
        EntityManagerFactory.setEntityManager( mgr );
        assertEquals( mgr, EntityManagerFactory.getEntityManager());
    }
    
    @Test
    public void setNullTest() {
        assertThrows( EntityManagerRequiredException.class, () -> EntityManagerFactory.setEntityManager( null ));
    }
}
