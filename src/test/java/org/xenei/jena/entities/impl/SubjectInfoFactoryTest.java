package org.xenei.jena.entities.impl;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xenei.jena.entities.EffectivePredicateTest;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.PredicateInfoImplTest;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.exceptions.MissingAnnotationException;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.jena.entities.impl.handlers.VoidHandler;
import org.xenei.jena.entities.testing.bad.BadAddersInterface;
import org.xenei.jena.entities.testing.iface.SingleValuePrimitiveInterface;

public class SubjectInfoFactoryTest {

    private String namespace = "http://localhost/test#";

    @Test
    public void parseBadAddersTest() throws Exception {
        final SubjectInfoFactory factory = new SubjectInfoFactory();
        final SubjectInfo subjectInfo = factory.parse( BadAddersInterface.NullArgs.class );
        Assertions.assertNull( subjectInfo.getPredicateProperty( "addX" ) );

        final SubjectInfo subjectInfo2 = factory.parse( BadAddersInterface.TwoArgs.class );
        Assertions.assertNull( subjectInfo2.getPredicateProperty( "addX" ) );
    }

    @Test
    public void parseSingleValuePrimitiveInterface() throws Exception {
        namespace = "http://localhost/test#";
        final SubjectInfoFactory factory = new SubjectInfoFactory();
        final SubjectInfo subjectInfo = factory.parse( SingleValuePrimitiveInterface.class );
        Assertions.assertEquals( SingleValuePrimitiveInterface.class, subjectInfo.getImplementedClass() );

        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "getChar", char.class ) );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "setChar", char.class ));
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "removeChar", void.class ));
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "hasChar", void.class ) );
        
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "getDbl", double.class ) );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "setDbl", double.class ));
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "removeDbl", void.class ));
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "hasDbl", void.class ) );
        
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "getFlt", float.class ) );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "setFlt", float.class ));
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "removeFlt", void.class ));
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "hasFlt", void.class ) );
       
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "getInt", int.class ) );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "setInt", int.class ));
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "removeInt", void.class ));
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "hasInt", void.class ) );

        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "getLng", long.class ) );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "setLng", long.class ));
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "removeLng", void.class ));
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "hasLng", void.class ) );

        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "isBool", void.class ) );
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "setBool", boolean.class ));
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "removeBool", void.class ));
        Assertions.assertNotNull( subjectInfo.getPredicateInfo( "hasBool", void.class ) );
    }
}
