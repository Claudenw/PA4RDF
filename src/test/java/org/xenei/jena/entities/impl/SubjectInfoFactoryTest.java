package org.xenei.jena.entities.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.testing.bad.BadAddersInterface;

public class SubjectInfoFactoryTest {

    @Test
    public void parseBadAddersTest() throws MissingAnnotation {
        final SubjectInfoFactory factory = new SubjectInfoFactory();
        final SubjectInfo subjectInfo = factory.parse( BadAddersInterface.NullArgs.class );
        Assertions.assertThrows( IllegalArgumentException.class, () -> subjectInfo.getPredicateProperty( "addX" ) );

        final SubjectInfo subjectInfo2 = factory.parse( BadAddersInterface.TwoArgs.class );
        Assertions.assertThrows( IllegalArgumentException.class, () -> subjectInfo2.getPredicateProperty( "addX" ) );
    }
}
