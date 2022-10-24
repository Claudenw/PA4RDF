package org.xenei.jena.entities.impl.parser;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.MethodParser;
import org.xenei.jena.entities.impl.SubjectInfoImpl;

abstract public class BaseAbstractParserTest {
    protected MethodParser parser;
    protected SubjectInfoImpl subjectInfo;
    protected final Class<?> classUnderTest;

    protected BaseAbstractParserTest(final Class<?> classUnderTest) {
        this.classUnderTest = classUnderTest;
    }

    protected Map<String, Integer> countAdders(final Method[] methods) {
        final Map<String, Integer> addCount = new HashMap<>();
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

    @BeforeEach
    public void setUp() throws Exception {
        subjectInfo = new SubjectInfoImpl( classUnderTest );
        final Map<String, Integer> addCount = countAdders( classUnderTest.getMethods() );
        parser = new MethodParser( EntityManagerFactory.getEntityManager(), subjectInfo, addCount );
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    protected void validatePredicateInfo(final PredicateInfo pi, final String name, final ActionType type,
            final String var, final Class<?> clazz) {
        Assertions.assertNotNull( pi, name + " not parsed" );
        Assertions.assertEquals( type, pi.getActionType() );
        Assertions.assertEquals( name, pi.getMethodName() );
        Assertions.assertEquals( "http://example.com/", pi.getNamespace() );
        Assertions.assertEquals( "http://example.com/" + var, pi.getUriString() );
        Assertions.assertEquals( clazz, pi.getValueClass() );
    }
}
