package org.xenei.pa4rdf.bean.test.partialMagic;

import org.xenei.pa4rdf.bean.annotations.Subject;

@Subject(namespace = "http://example.com/test/", magic=true)
public interface PartialMagicA
{
	public int getA();

	public void setA(int a);

	public boolean hasA();
}
