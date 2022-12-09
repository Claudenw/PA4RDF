package org.xenei.jena.entities.impl.method;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.xenei.jena.entities.EffectivePredicate;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.PredicateInfoImpl;

class ImplMethodParser extends BaseMethodParser {

    public ImplMethodParser(final BaseMethodParser methodParser) {
        super( methodParser );
    }

    private static Method findAntecedentMethod(final Method method) {
        final Queue<Class<?>> queue = new LinkedList<>();
        final Set<Class<?>> seenSet = new HashSet<>();
        List<Class<?>> lst = Arrays.asList( method.getDeclaringClass().getInterfaces() );
        queue.addAll( lst );
        seenSet.addAll( lst );

        Class<?> iface = queue.poll();
        while (iface != null) {
            try {
                return iface.getMethod( method.getName(), method.getParameterTypes() );
            } catch (final NoSuchMethodException e) {
                lst = Arrays.asList( iface.getInterfaces() );
                lst.removeAll( seenSet );
                queue.addAll( lst );
                seenSet.addAll( lst );
                iface = queue.poll();
            }
        }
        return null;
    }
    
    void parse(final ActionType actionType, final Method method, final EffectivePredicate predicate)
            throws MissingAnnotation {

        Method antecedent = ImplMethodParser.findAntecedentMethod( method );
        if (antecedent == null) {
            return;
        }
        PredicateInfo antecedentPI = parse( antecedent );

        while ((antecedentPI == null) && (antecedent != null)) {
            
            antecedent = ImplMethodParser.findAntecedentMethod( antecedent );
            if (antecedent == null) {
                return;
            }
            antecedentPI = parse( antecedent );
        }
        PredicateInfoImpl pi = new PredicateInfoImpl( antecedentPI, method, predicate );
        subjectInfo.add( pi );
    }
}
