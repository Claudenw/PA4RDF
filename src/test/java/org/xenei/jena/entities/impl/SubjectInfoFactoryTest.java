package org.xenei.jena.entities.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.testing.bad.BadAddersInterface;

public class SubjectInfoFactoryTest {

    @Test
    public void parseBadAddersTest() throws MissingAnnotation {
        SubjectInfoFactory factory = new SubjectInfoFactory(  EntityManagerFactory.getEntityManager() );
        final SubjectInfo subjectInfo = factory.parse( BadAddersInterface.NullArgs.class );
        assertThrows( IllegalArgumentException.class, () -> subjectInfo.getPredicateProperty( "addX" ));

        final SubjectInfo subjectInfo2 = factory.parse( BadAddersInterface.TwoArgs.class );
        assertThrows( IllegalArgumentException.class, () -> subjectInfo2.getPredicateProperty( "addX" ));
    }
}
