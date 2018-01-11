package org.xenei.jena.entities.impl.parser.REVIEW;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.jena.entities.impl.MethodParser;
import org.xenei.jena.entities.impl.SubjectInfoImpl;

abstract public class REVIEW_BaseAbstractParserTest {
    protected MethodParser parser;
    protected SubjectInfoImpl subjectInfo;
    protected final Class<?> classUnderTest;
    protected EntityManagerImpl entityManager;

    protected REVIEW_BaseAbstractParserTest(final Class<?> classUnderTest) {
        this.classUnderTest = classUnderTest;
    }

    protected Map<String, Integer> countAdders(final Method[] methods) {
        final Map<String, Integer> addCount = new HashMap<String, Integer>();
        for (final Method m : methods) {
            if (isAdd( m )) {
                Integer i = addCount.get( m.getName() );
                if (i == null) {
                    i = 1;
                } else {
                    i = i + 1;
                }
                addCount.put( m.getName(), i );
            }
        }
        return addCount;
    }

    protected boolean isAdd(final Method m) {
        try {
            if (ActionType.parse( m.getName() ) == ActionType.SETTER) {
                final Class<?> parms[] = m.getParameterTypes();
                return (parms != null) && (parms.length == 1);
            }
        } catch (final IllegalArgumentException expected) {
            // do nothing
        }
        return false;
    }

    @Before
    public void setUp() throws Exception {
        subjectInfo = new SubjectInfoImpl( classUnderTest );
        final Map<String, Integer> addCount = countAdders( classUnderTest.getMethods() );
        entityManager = (EntityManagerImpl) EntityManagerFactory.create();
        parser = new MethodParser( entityManager, subjectInfo, addCount );
    }

    @After
    public void tearDown() throws Exception {
    }
}
