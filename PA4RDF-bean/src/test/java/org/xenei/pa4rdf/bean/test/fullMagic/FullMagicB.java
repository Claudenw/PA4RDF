package org.xenei.pa4rdf.bean.test.fullMagic;

import org.xenei.pa4rdf.bean.annotations.Subject;

@Subject(namespace = "http://example.com/test/", magic = true)
public interface FullMagicB
{
	public String getB();

	public void setB(String a);

	public boolean hasB();
}
