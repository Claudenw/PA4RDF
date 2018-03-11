package org.xenei.pa4rdf.bean.impl.predicate.singleValue;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Ignore;
import org.junit.Test;
import org.xenei.pa4rdf.bean.annotations.URI;
import org.xenei.pa4rdf.bean.impl.ActionType;
import org.xenei.pa4rdf.bean.test.iface.SingleValueObjectInterface;
import org.xenei.pa4rdf.bean.test.impl.SingleValueObjectImpl;

public class SingleValueObjectImplTest extends AbstractSingleValueObjectTest
{

	public SingleValueObjectImplTest()
	{
		super(SingleValueObjectImpl.class);
		builder.setImpl(true).setNamespace("");
	}

	@Override
	protected void updateGetU2()
	{
		builder.setInternalType(Literal.class)
				.setLiteralType(XSDDatatype.XSDstring).setType(String.class)
				.setName("u2");
	}

	@Test
	@Ignore("Predicate merging not quite right")
	public void testMergeOverride() throws Exception
	{

		builder.setActionType(ActionType.GETTER).setName("u")
				.setInternalType(RDFNode.class).setType(URI.class)
				.setNamespace("http://localhost/test#");

		assertOverride(builder, SingleValueObjectImpl.class.getMethod("getU2"),
				SingleValueObjectInterface.class.getMethod("getU2"));

	}

}
