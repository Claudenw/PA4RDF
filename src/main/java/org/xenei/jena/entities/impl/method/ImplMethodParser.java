package org.xenei.jena.entities.impl.method;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.xenei.jena.entities.EffectivePredicate;
import org.xenei.jena.entities.EntityManagerFactory;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.exceptions.MissingAnnotationException;
import org.xenei.jena.entities.impl.Action;
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

    void parse(final Action action, final EffectivePredicate predicate) throws MissingAnnotationException {

        Method antecedent = action.method;
        PredicateInfo antecedentPI = null;

        while ((antecedentPI == null) && (antecedent != null)) {

            antecedent = ImplMethodParser.findAntecedentMethod( antecedent );
            if (antecedent == null) {
                return;
            }
            final SubjectInfo info = EntityManagerFactory.getEntityManager()
                    .getSubjectInfo( antecedent.getDeclaringClass() );
            antecedentPI = info.getPredicateInfo( antecedent );
        }

        final PredicateInfoImpl pi = new PredicateInfoImpl( predicate.merge( antecedentPI.getPredicate() ), action );
        subjectInfo.add( action.method, pi );
        switch (action.actionType) {
        case GETTER:
        case SETTER:
            processAssociatedMethods( pi, action );
            break;
        default:
            break;
        }
    }
}
