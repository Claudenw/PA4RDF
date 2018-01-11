package org.xenei.jena.entities.impl.parser;

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
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.jena.entities.impl.MethodParser;
import org.xenei.jena.entities.impl.ObjectHandler;
import org.xenei.jena.entities.impl.PredicateInfoImpl;
import org.xenei.jena.entities.impl.SubjectInfoImpl;

public abstract class AbstractMethodParserTest {

    protected final EntityManagerImpl entityManager = (EntityManagerImpl) EntityManagerFactory.create();
    protected final Map<String, Integer> addCount = new HashMap<String, Integer>();
    protected final Map<Method, PredicateInfo> PIMap = new HashMap<Method, PredicateInfo>();
    protected final Map<Method, ObjectHandler> OMMap = new HashMap<Method, ObjectHandler>();
    protected final SubjectInfoImpl subjectInfo;
    protected final MethodParser parser;
    protected String NS = "http://example.com/";

    static {
        EntityManagerImpl.registerTypes();
    }

    protected AbstractMethodParserTest(Class<?> interfaceClass) {
        subjectInfo = new SubjectInfoImpl( interfaceClass );
        parser = new MethodParser( entityManager, subjectInfo, addCount );
    }

    protected void assertSame(Method method) {
        assertSame( PIMap.get( method ), subjectInfo.getPredicateInfo( method ), method );
    }

    protected void assertSame(PredicateInfo expected, PredicateInfo actual, Method method) {
        Assert.assertNotNull( method.toGenericString()+": Missing actual predicate info " + expected.getActionType(), actual );
        Assert.assertNotNull( method.toGenericString()+": Missing expected predicate info " + expected.getActionType(), expected );
        Assert.assertEquals( method.toGenericString()+": Wrong action type", expected.getActionType(), actual.getActionType() );
        Assert.assertEquals( method.toGenericString()+": Wrong method name", expected.getMethodName(), actual.getMethodName() );
        Assert.assertEquals( method.toGenericString()+": Wrong namespace", expected.getNamespace(), actual.getNamespace() );
        Assert.assertEquals( method.toGenericString()+": Found unexpected postExec", expected.getPostExec().size(), actual.getPostExec().size() );
        Assert.assertEquals( method.toGenericString()+": Wrong property", expected.getProperty(), actual.getProperty() );
        Assert.assertEquals( method.toGenericString()+": Wrong URI string", expected.getUriString(), actual.getUriString() );
        Assert.assertEquals( method.toGenericString()+": Wrong value class", expected.getValueClass(), actual.getValueClass() );

        Assert.assertEquals( method.toGenericString()+": Wrong object handler\n", OMMap.get( method ),
                ((PredicateInfoImpl) actual).getObjectHandler( (EntityManager) null ) );
    }

    @SuppressWarnings("unchecked")
    protected PredicateInfo mockPredicateInfo(Method m, String shortName, ActionType type,
            @SuppressWarnings("rawtypes") Class valueClass, int execCount) {
        final PredicateInfo pi = mock( PredicateInfo.class );
        when( pi.getNamespace() ).thenReturn( NS );
        when( pi.getUriString() ).thenReturn( NS + shortName );
        when( pi.getProperty() ).thenReturn( ResourceFactory.createProperty( NS + shortName ) );
        when( pi.getActionType() ).thenReturn( type );
        when( pi.getMethodName() ).thenReturn( m.getName() );
        when( pi.getValueClass() ).thenReturn( valueClass );
        new ArrayList<Annotation>();

        final List<Method> mthd = new ArrayList<Method>();
        for (int i = 0; i < execCount; i++) {
            mthd.add( m );
        }
        when( pi.getPostExec() ).thenReturn( mthd );
        return pi;
    }
}
