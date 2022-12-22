package org.xenei.jena.entities.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.jena.entities.exceptions.NotInterfaceException;

public class ClassUtils {
    private static Logger LOG = LoggerFactory.getLogger( ClassUtils.class );

    /**
     * Recursive method used to find all classes in a given directory and
     * subdirs. Adapted from http://snippets.dzone.com/posts/show/4831 and
     * extended to support use of JAR files
     *
     * @param directory
     *            The base directory
     * @param packageName
     *            The package name for classes found inside the base directory
     * @return The classes
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Set<String> findClasses(final String directory, final String packageName) throws IOException {
        final Set<String> classes = new HashSet<>();
        if (directory.startsWith( "file:" ) && directory.contains( "!" )) {
            final String[] split = directory.split( "!" );
            final URL jar = new URL( split[0] );
            final ZipInputStream zip = new ZipInputStream( jar.openStream() );
            ZipEntry entry = null;
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.getName().endsWith( ".class" )) {
                    final String className = entry.getName().replaceAll( "[$].*", "" ).replaceAll( "[.]class", "" )
                            .replace( '/', '.' );
                    classes.add( className );
                }
            }
        }
        final File dir = new File( directory );
        if (!dir.exists()) {
            return classes;
        }
        final File[] files = dir.listFiles();
        for (final File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains( "." );
                classes.addAll( ClassUtils.findClasses( file.getAbsolutePath(), packageName + "." + file.getName() ) );
            } else if (file.getName().endsWith( ".class" )) {
                classes.add( packageName + '.' + file.getName().substring( 0, file.getName().length() - 6 ) );
            }
        }
        return classes;
    }

    /**
     * Scans all classes accessible from the context class loader which belong
     * to the given package and subpackages. Adapted from
     * http://snippets.dzone.com/posts/show/4831 and extended to support use of
     * JAR files
     *
     * @param packageName
     *            The base package or class name
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Collection<Class<?>> getClasses(final String packageName) {

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        final String path = packageName.replace( '.', '/' );
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources( path );
        } catch (final IOException e1) {
            ClassUtils.LOG.error( e1.toString() );
            return Collections.emptyList();
        }
        final Set<Class<?>> classes = new HashSet<>();
        if (resources.hasMoreElements()) {
            while (resources.hasMoreElements()) {
                final URL resource = resources.nextElement();
                try {
                    for (final String clazz : ClassUtils.findClasses( resource.getFile(), packageName )) {
                        try {
                            classes.add( Class.forName( clazz ) );
                        } catch (final ClassNotFoundException e) {
                            ClassUtils.LOG.warn( e.toString() );
                        }
                    }
                } catch (final IOException e) {
                    ClassUtils.LOG.warn( e.toString() );
                }
            }
        } else {
            // there are no resources at that path so see if it is a class
            try {
                classes.add( Class.forName( packageName ) );
            } catch (final ClassNotFoundException e) {
                ClassUtils.LOG.warn( "{} was neither a package name nor a class name", packageName );
            }
        }
        return classes;
    }

    public static boolean nullOrVoid(final Class<?> clazz) {
        return (clazz == null) || clazz.equals( void.class );
    }

    public static void validateInterface(final Class<?> clazz) throws NotInterfaceException {
        if (!clazz.isInterface()) {
            throw new NotInterfaceException( clazz );
        }
    }

    public static boolean isCollection(final Class<?> clazz) {
        return ((clazz != null)
                && (Iterator.class.isAssignableFrom( clazz ) || Collection.class.isAssignableFrom( clazz )))
                || clazz.isArray();
    }
}
