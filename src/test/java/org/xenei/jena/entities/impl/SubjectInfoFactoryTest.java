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
    private boolean upcase;
    private boolean emptyIsNull;
    private boolean impl;
    private String literalType;

    private void reset() {
        upcase = false;
        emptyIsNull = false;
        impl = false;
        literalType = "";
    }

    @Test
    public void parseBadAddersTest() throws Exception {
        final SubjectInfoFactory factory = new SubjectInfoFactory();
        final SubjectInfo subjectInfo = factory.parse( BadAddersInterface.NullArgs.class );
        Assertions.assertThrows( IllegalArgumentException.class, () -> subjectInfo.getPredicateProperty( "addX" ) );

        final SubjectInfo subjectInfo2 = factory.parse( BadAddersInterface.TwoArgs.class );
        Assertions.assertThrows( IllegalArgumentException.class, () -> subjectInfo2.getPredicateProperty( "addX" ) );
    }

    @Test
    public void parseSingleValuePrimitiveInterface() throws Exception {
        namespace = "http://localhost/test#";
        final SubjectInfoFactory factory = new SubjectInfoFactory();
        final SubjectInfo subjectInfo = factory.parse( SingleValuePrimitiveInterface.class );
        Assertions.assertEquals( SingleValuePrimitiveInterface.class, subjectInfo.getImplementedClass() );

        reset();
        final String expectedHandler = PredicateInfoImplTest.createHandler( "string", "char" );
        final Property expectedProperty = ResourceFactory.createProperty( namespace, "char" );
        PredicateInfo pi = subjectInfo.getPredicateInfo( "getChar", char.class );
        PredicateInfoImplTest.assertValues( pi, ActionType.GETTER, char.class, "getChar", void.class,
                LiteralHandler.class, expectedHandler, expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), emptyIsNull, impl, literalType, "char", namespace,
                char.class, upcase );

        reset();
        pi = subjectInfo.getPredicateInfo( "setChar", char.class );
        PredicateInfoImplTest.assertValues( pi, ActionType.SETTER, void.class, "setChar", char.class,
                LiteralHandler.class, "VoidHandler", expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), emptyIsNull, impl, literalType, "char", namespace, null,
                upcase );

        reset();
        pi = subjectInfo.getPredicateInfo( "removeChar", void.class );
        PredicateInfoImplTest.assertValues( pi, ActionType.REMOVER, void.class, "removeChar", void.class,
                VoidHandler.class, "VoidHandler", expectedProperty, void.class );
        EffectivePredicateTest.assertValues( pi.getPredicate(), emptyIsNull, impl, literalType, "char", namespace,
                char.class, upcase );

        reset();

        /*
         *
         * char getChar();
         *
         * double getDbl();
         *
         * float getFlt();
         *
         * int getInt();
         *
         * long getLng();
         *
         * boolean isBool();
         *
         * void removeBool();
         *
         * void removeChar();
         *
         * void removeDbl();
         *
         * void removeFlt();
         *
         * void removeInt();
         *
         * void removeLng();
         *
         * @Predicate void setBool(boolean b);
         *
         * @Predicate void setChar(char b);
         *
         * @Predicate void setDbl(double b);
         *
         * @Predicate void setFlt(float b);
         *
         * @Predicate void setInt(int b);
         *
         * @Predicate void setLng(long b);
         *
         */

    }
}
