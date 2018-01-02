package org.xenei.jena.entities.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.PredicateInfo;

public abstract class AbstractMethodParserTest {

    protected final EntityManagerImpl entityManager = (EntityManagerImpl) EntityManagerFactory.create();
    protected final Map<String, Integer> addCount = new HashMap<String, Integer>();
    protected final Map<Method, PredicateInfo> PIMap = new HashMap<Method, PredicateInfo>();

    protected void assertSame(PredicateInfo expected, PredicateInfo actual) {
        Assert.assertNotNull( "Missing predicate info " + expected.getActionType(), actual );
        Assert.assertEquals( expected.getActionType(), actual.getActionType() );
        Assert.assertEquals( expected.getMethodName(), actual.getMethodName() );
        Assert.assertEquals( "found unexpected annotations", expected.getAnnotations().size(),
                actual.getAnnotations().size() );
        Assert.assertEquals( expected.getNamespace(), actual.getNamespace() );
        Assert.assertEquals( "found unexpected postExec", expected.getPostExec().size(), actual.getPostExec().size() );
        Assert.assertEquals( expected.getProperty(), actual.getProperty() );
        Assert.assertEquals( expected.getUriString(), actual.getUriString() );
        Assert.assertEquals( expected.getValueClass(), actual.getValueClass() );
    }

    @SuppressWarnings("unchecked")
    protected PredicateInfo mockPredicateInfo(Method m, String shortName, ActionType type, @SuppressWarnings("rawtypes") Class valueClass,
            int annotationCount, int execCount) {
        final PredicateInfo pi = mock( PredicateInfo.class );
        when( pi.getNamespace() ).thenReturn( "http://example.com/" );
        when( pi.getUriString() ).thenReturn( "http://example.com/" + shortName );
        when( pi.getProperty() ).thenReturn( ResourceFactory.createProperty( "http://example.com/" + shortName ) );
        when( pi.getActionType() ).thenReturn( type );
        when( pi.getMethodName() ).thenReturn( m.getName() );
        when( pi.getValueClass() ).thenReturn( valueClass );
        final List<Annotation> ann = new ArrayList<Annotation>();
        for (int i = 0; i < annotationCount; i++) {
            ann.add( mock( Annotation.class ) );
        }
        when( pi.getAnnotations() ).thenReturn( ann );

        final List<Method> mthd = new ArrayList<Method>();
        for (int i = 0; i < execCount; i++) {
            mthd.add( m );
        }
        when( pi.getPostExec() ).thenReturn( mthd );
        return pi;
    }
}
