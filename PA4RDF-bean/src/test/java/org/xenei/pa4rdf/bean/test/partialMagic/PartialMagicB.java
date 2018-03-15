package org.xenei.pa4rdf.bean.test.partialMagic;

import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.Subject;

@Subject(namespace = "http://example.com/test/")
public interface PartialMagicB
{
	public String getB();

	@Predicate
	public void setB(String a);

	public boolean hasB();
}