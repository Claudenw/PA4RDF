package org.xenei.jena.entities.impl.parser.collectionValue;

import org.apache.jena.util.iterator.ExtendedIterator;
import org.xenei.jena.entities.testing.iface.MultiValueInterface;
import org.xenei.pa4rdf.bean.impl.ActionType;

public class MultiValueInterfaceTest extends AbstractCollectionValueInterfaceTest {

    public MultiValueInterfaceTest() throws NoSuchMethodException, SecurityException {
        this( MultiValueInterface.class );
    }

    protected MultiValueInterfaceTest(Class<?> underTest) throws NoSuchMethodException, SecurityException {
        super( underTest );
        PIMap.put( getBool, mockPredicateInfo( getBool, "bool", ActionType.GETTER, ExtendedIterator.class, 0 ) );
        PIMap.put( getChar, mockPredicateInfo( getChar, "char", ActionType.GETTER, ExtendedIterator.class, 0 ) );
        PIMap.put( getDbl, mockPredicateInfo( getDbl, "dbl", ActionType.GETTER, ExtendedIterator.class, 0 ) );
        PIMap.put( getEnt, mockPredicateInfo( getEnt, "ent", ActionType.GETTER, ExtendedIterator.class, 0 ) );
        PIMap.put( getFlt, mockPredicateInfo( getFlt, "flt", ActionType.GETTER, ExtendedIterator.class, 0 ) );
        PIMap.put( getInt, mockPredicateInfo( getInt, "int", ActionType.GETTER, ExtendedIterator.class, 0 ) );
        PIMap.put( getLng, mockPredicateInfo( getLng, "lng", ActionType.GETTER, ExtendedIterator.class, 0 ) );
        PIMap.put( getRDF, mockPredicateInfo( getRDF, "rDF", ActionType.GETTER, ExtendedIterator.class, 0 ) );
        PIMap.put( getStr, mockPredicateInfo( getStr, "str", ActionType.GETTER, ExtendedIterator.class, 0 ) );
        PIMap.put( getU, mockPredicateInfo( getU, "u", ActionType.GETTER, ExtendedIterator.class, 0 ) );
        PIMap.put( getU3, mockPredicateInfo( getU3, "u3", ActionType.GETTER, ExtendedIterator.class, 0 ) );
        PIMap.put( getU4, mockPredicateInfo( getU4, "u3", ActionType.GETTER, ExtendedIterator.class, 0 ) );
    }

}
