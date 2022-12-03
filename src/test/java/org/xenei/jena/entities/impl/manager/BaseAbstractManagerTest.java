package org.xenei.jena.entities.impl.manager;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.jena.entities.impl.SubjectInfoImpl;

abstract public class BaseAbstractManagerTest {
    protected SubjectInfoImpl subjectInfo;
    protected final Class<?> classUnderTest;
    protected static String NS = "http://localhost/test#";
    protected final EntityManager manager = new EntityManagerImpl();
    protected Model model;

    protected BaseAbstractManagerTest(final Class<?> classUnderTest) {
        this.classUnderTest = classUnderTest;
    }

    @BeforeEach
    public final void setup() {
        model = ModelFactory.createDefaultModel();
        subjectInfo = (SubjectInfoImpl) manager.getSubjectInfo( classUnderTest );
    }

    @AfterEach
    public final void teardown() {
        model.close();
    }

    @Test
    public void testGetResource() throws Exception {
        final Resource r = model.createResource();
        final Object o = manager.read( r, classUnderTest );
        Resource r2 = r;
        if (o instanceof ResourceWrapper) {
            final ResourceWrapper rw = (ResourceWrapper) o;
            r2 = rw.getResource();

        } else {
            r2 = (Resource) o;
        }
        Assertions.assertEquals( r, r2 );
    }

}
