package org.xenei.jena.entities.impl.manager;

import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.testing.iface.MultiValueIntfDefault;

@Subject
public class MultiValueIntfTest extends AbstractMultiValueTest {

    public MultiValueIntfTest() {
        super( MultiValueIntfDefault.class );
    }

}
