package org.xenei.pa4rdf.bean.datatypes;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;

public class Init
{
	private static boolean inited = false;
	static
	{
		registerTypes();
	}

	/**
	 * Register the datatypes used by the entity manger specifically xsd:long :
	 * java.util.Long xsd:string : java.util.Character xsd:string :
	 * java.lang.char
	 * 
	 * and finally resetting xsd:string to java.lang.String
	 */
	public synchronized static void registerTypes()
	{
		if (!inited)
		{
			// handle the string types
			// preserve string class and put it back later.
			final RDFDatatype stype = TypeMapper.getInstance()
					.getTypeByClass(String.class);

			TypeMapper.getInstance()
					.registerDatatype(CharacterDatatype.INSTANCE);
			TypeMapper.getInstance().registerDatatype(CharDatatype.INSTANCE);
			TypeMapper.getInstance().registerDatatype(LongDatatype.INSTANCE);
			// put the string type back so that it is the registered type for
			// xsd:string
			TypeMapper.getInstance().registerDatatype(stype);
			inited = true;
		}
	}
}
