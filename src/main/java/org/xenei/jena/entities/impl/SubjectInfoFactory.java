package org.xenei.jena.entities.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.annotations.Predicate;

public class SubjectInfoFactory {
    private static Logger LOG = LoggerFactory.getLogger( SubjectInfoFactory.class );
    private final Map<Class<?>, SubjectInfoImpl> classInfo = new HashMap<>();
   
    private EntityManager entityManager;
    
    public SubjectInfoFactory(EntityManager entityManager) {
        this.entityManager=entityManager;
        try {
            parse( ResourceWrapper.class );
        } catch (final MissingAnnotation e) {
            throw new RuntimeException( e );
        }
    }
    
    public void clear() {
        classInfo.clear();
        try {
            parse( ResourceWrapper.class );
        } catch (final MissingAnnotation e) {
            throw new RuntimeException( e );
        }
    }
    /**
     * Parse the class if necessary.
     *
     * The first time the class is seen it is parsed, after that a cached
     * version is returned.
     *
     * @param clazz
     * @return The SubjectInfo for the class.
     * @throws MissingAnnotation
     */
    public SubjectInfoImpl parse(final Class<?> clazz) throws MissingAnnotation {
        SubjectInfoImpl subjectInfo = classInfo.get( clazz );

        if (subjectInfo == null) {
            LOG.info( "Parsing {}", clazz );
            subjectInfo = new SubjectInfoImpl( clazz );

            final MethodParser parser = new MethodParser( entityManager, subjectInfo, countAdders( clazz.getMethods() ) );

            boolean foundAnnotation = false;
            final List<Method> annotated = new ArrayList<>();
            for (final Method method : clazz.getMethods()) {
                try {
                    final ActionType actionType = ActionType.parse( method.getName() );
                    if (method.getAnnotation( Predicate.class ) != null) {
                        foundAnnotation = true;
                        if (ActionType.GETTER == actionType) {
                            parser.parse( method );
                        } else {
                            annotated.add( method );
                        }
                    }
                } catch (final IllegalArgumentException expected) {
                    // not an action type ignore method
                }

            }
            if (!foundAnnotation) {
                throw new MissingAnnotation( "No annotated methods in " + clazz.getCanonicalName() );
            }

            for (final Method method : annotated) {
                parser.parse( method );
            }
            classInfo.put( clazz, subjectInfo );
        }
        return subjectInfo;
    }

    private boolean isAdd(final Method m) {
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
    
    private Map<String, Integer> countAdders(final Method[] methods) {
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
}
