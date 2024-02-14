package org.xenei.jena.entities.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.exceptions.MissingAnnotationException;
import org.xenei.jena.entities.exceptions.NotInterfaceException;
import org.apache.jena.atlas.lib.CacheSet;

import org.apache.jena.atlas.lib.cache.CacheSimple;
import org.apache.jena.atlas.lib.cache.CacheSetImpl;

public class SubjectInfoFactory {
    private static Logger LOG = LoggerFactory.getLogger( SubjectInfoFactory.class );
    private final Map<Class<?>, SubjectInfoImpl> classInfo = new HashMap<>();
    private final CacheSet<Class<?>> notAnnotated = new CacheSetImpl<>( new CacheSimple<Class<?>, Object>( 50 ) );

    public SubjectInfoFactory() {
    }

    public void clear() {
        classInfo.clear();
    }

    /**
     * Parse the class if necessary.
     *
     * The first time the class is seen it is parsed, after that a cached
     * version is returned.
     *
     * @param clazz
     * @return The SubjectInfo for the class.
     * @throws MissingAnnotationException
     * @throws NotInterfaceException
     */
    public SubjectInfoImpl parse(final Class<?> clazz) throws MissingAnnotationException {
        SubjectInfoImpl subjectInfo = classInfo.get( clazz );

        if ((subjectInfo == null) && !notAnnotated.contains( clazz )) {
            SubjectInfoFactory.LOG.info( "Parsing {}", clazz );
            subjectInfo = new SubjectInfoImpl( clazz );
            final MethodParser parser = new MethodParser( subjectInfo );

            boolean foundAnnotation = false;
            final List<Method> annotated = new ArrayList<>();
            // parse all the setter annotated methods, parse other annotated methods later

            for (final Method method : clazz.getMethods()) {
                try {
                    if (method.getAnnotation( Predicate.class ) != null) {
                        foundAnnotation = true;
                        if (ActionType.SETTER.isA( method.getName() )) {
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
                notAnnotated.add( clazz );
                SubjectInfoFactory.LOG.debug( "caching {}", clazz );
                return null;
            }
            // parse the reminder
            for (final Method method : annotated) {
                parser.parse( method );
            }
            classInfo.put( clazz, subjectInfo );
        }
        return subjectInfo;
    }
}
