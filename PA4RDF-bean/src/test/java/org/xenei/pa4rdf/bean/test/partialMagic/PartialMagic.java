package org.xenei.pa4rdf.bean.test.partialMagic;

import org.xenei.pa4rdf.bean.annotations.Subject;

@Subject(namespace = "http://example.com/test/")
public interface PartialMagic extends PartialMagicA, PartialMagicB
{
}
