package org.xenei.pa4rdf.bean.impl.predicate.collectionValue;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.junit.Test;
import org.xenei.pa4rdf.bean.annotations.Predicate;
import org.xenei.pa4rdf.bean.annotations.URI;
import org.xenei.pa4rdf.bean.datatypes.CharacterDatatype;
import org.xenei.pa4rdf.bean.datatypes.Init;
import org.xenei.pa4rdf.bean.datatypes.LongDatatype;
import org.xenei.pa4rdf.bean.impl.ActionType;
import org.xenei.pa4rdf.bean.impl.EffectivePredicate;
import org.xenei.pa4rdf.bean.impl.predicate.AbstractPredicateTest;
import org.xenei.pa4rdf.bean.test.iface.TestInterface;

public abstract class AbstractCollectionValueInterfaceTest
		extends AbstractPredicateTest
{

	static
	{
		Init.registerTypes();
	}

	protected AbstractCollectionValueInterfaceTest(Class<?> underTest)
			throws NoSuchMethodException, SecurityException
	{
		super(underTest);
		builder.setNamespace("http://localhost/test#");

	}

	/*
	 * order Predicate : Getter Predicate : Other
	 * 
	 * Class method order with same name.
	 * 
	 * @Predicate public abstract void addBool(Boolean b);
	 * 
	 * @Predicate public abstract void addChar(Character b);
	 * 
	 * @Predicate public abstract void addDbl(Double b);
	 * 
	 * @Predicate public abstract void addEnt(TestInterface b);
	 * 
	 * @Predicate public abstract void addFlt(Float b);
	 * 
	 * @Predicate public abstract void addInt(Integer b);
	 * 
	 * @Predicate public abstract void addLng(Long b);
	 * 
	 * @Predicate public abstract void addRDF(RDFNode b);
	 * 
	 * @Predicate public abstract void addStr(String b);
	 * 
	 * @Predicate public abstract void addU(@URI String b);
	 * 
	 * @Predicate public abstract void addU3(RDFNode b);
	 * 
	 * @Predicate(type = RDFNode.class) public abstract Set<RDFNode> getU();
	 * 
	 * @Predicate(type = URI.class, name = "u") public abstract List<String>
	 * getU2();
	 * 
	 * @Predicate(type = RDFNode.class) public abstract Queue<RDFNode> getU3();
	 * 
	 * @Predicate(type = URI.class, name = "u3") public abstract Set<String>
	 * getU4();
	 * 
	 * 
	 */
	@Override
	public void processOrderTest()
			throws NoSuchMethodException, SecurityException
	{

		// bool
		EffectivePredicate base = new EffectivePredicate(
				interfaceClass.getMethod("addBool", Boolean.class));
		builder.setType(Boolean.class).setName("bool")
				.setInternalType(Literal.class)
				.setLiteralType(XSDDatatype.XSDboolean);

		Method mthd = interfaceClass.getMethod("getBool");
		EffectivePredicate othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.GETTER).setCollectionType(Set.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("hasBool", Boolean.class);
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.EXISTENTIAL)
				.setCollectionType(Predicate.UNSET.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("removeBool", Boolean.class);
		builder.setActionType(ActionType.REMOVER);
		othr = new EffectivePredicate(mthd).merge(base);
		assertSame(builder, othr, mthd);

		// char
		base = new EffectivePredicate(
				interfaceClass.getMethod("addChar", Character.class));
		builder.setType(Character.class).setName("char")
				.setLiteralType(CharacterDatatype.INSTANCE);

		mthd = interfaceClass.getMethod("getChar");
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.GETTER).setCollectionType(List.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("hasChar", Character.class);
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.EXISTENTIAL)
				.setCollectionType(Predicate.UNSET.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("removeChar", Character.class);
		builder.setActionType(ActionType.REMOVER);
		othr = new EffectivePredicate(mthd).merge(base);
		assertSame(builder, othr, mthd);

		// dbl
		base = new EffectivePredicate(
				interfaceClass.getMethod("addDbl", Double.class));
		builder.setType(Double.class).setName("dbl")
				.setLiteralType(XSDDatatype.XSDdouble);

		mthd = interfaceClass.getMethod("getDbl");
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.GETTER).setCollectionType(Queue.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("hasDbl", Double.class);
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.EXISTENTIAL)
				.setCollectionType(Predicate.UNSET.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("removeDbl", Double.class);
		builder.setActionType(ActionType.REMOVER);
		othr = new EffectivePredicate(mthd).merge(base);
		assertSame(builder, othr, mthd);

		// ent
		base = new EffectivePredicate(
				interfaceClass.getMethod("addEnt", TestInterface.class));
		builder.setType(TestInterface.class).setName("ent")
				.setInternalType(RDFNode.class).setLiteralType(null);

		mthd = interfaceClass.getMethod("getEnt");
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.GETTER).setCollectionType(Queue.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("hasEnt", TestInterface.class);
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.EXISTENTIAL)
				.setCollectionType(Predicate.UNSET.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("removeEnt", TestInterface.class);
		builder.setActionType(ActionType.REMOVER);
		othr = new EffectivePredicate(mthd).merge(base);
		assertSame(builder, othr, mthd);

		// flt
		base = new EffectivePredicate(
				interfaceClass.getMethod("addFlt", Float.class));
		builder.setType(Float.class).setName("flt")
				.setInternalType(Literal.class)
				.setLiteralType(XSDDatatype.XSDfloat);

		mthd = interfaceClass.getMethod("getFlt");
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.GETTER).setCollectionType(Set.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("hasFlt", Float.class);
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.EXISTENTIAL)
				.setCollectionType(Predicate.UNSET.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("removeFlt", Float.class);
		builder.setActionType(ActionType.REMOVER);
		othr = new EffectivePredicate(mthd).merge(base);
		assertSame(builder, othr, mthd);

		// int
		base = new EffectivePredicate(
				interfaceClass.getMethod("addInt", Integer.class));
		builder.setType(Integer.class).setName("int")
				.setLiteralType(XSDDatatype.XSDint);

		mthd = interfaceClass.getMethod("getInt");
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.GETTER).setCollectionType(Queue.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("hasInt", Integer.class);
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.EXISTENTIAL)
				.setCollectionType(Predicate.UNSET.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("removeInt", Integer.class);
		builder.setActionType(ActionType.REMOVER);
		othr = new EffectivePredicate(mthd).merge(base);
		assertSame(builder, othr, mthd);

		// lng
		base = new EffectivePredicate(
				interfaceClass.getMethod("addLng", Long.class));
		builder.setType(Long.class).setName("lng")
				.setLiteralType(LongDatatype.INSTANCE);

		mthd = interfaceClass.getMethod("getLng");
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.GETTER).setCollectionType(List.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("hasLng", Long.class);
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.EXISTENTIAL)
				.setCollectionType(Predicate.UNSET.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("removeLng", Long.class);
		builder.setActionType(ActionType.REMOVER);
		othr = new EffectivePredicate(mthd).merge(base);
		assertSame(builder, othr, mthd);

		// RDF
		base = new EffectivePredicate(
				interfaceClass.getMethod("addRDF", RDFNode.class));
		builder.setType(RDFNode.class).setName("rDF")
				.setInternalType(RDFNode.class).setLiteralType(null);

		mthd = interfaceClass.getMethod("getRDF");
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.GETTER).setCollectionType(List.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("hasRDF", RDFNode.class);
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.EXISTENTIAL)
				.setCollectionType(Predicate.UNSET.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("removeRDF", RDFNode.class);
		builder.setActionType(ActionType.REMOVER);
		othr = new EffectivePredicate(mthd).merge(base);
		assertSame(builder, othr, mthd);

		// str
		base = new EffectivePredicate(
				interfaceClass.getMethod("addStr", String.class));
		builder.setType(String.class).setName("str")
				.setInternalType(Literal.class)
				.setLiteralType(XSDDatatype.XSDstring);

		mthd = interfaceClass.getMethod("getStr");
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.GETTER).setCollectionType(Set.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("hasStr", String.class);
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.EXISTENTIAL)
				.setCollectionType(Predicate.UNSET.class);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("removeStr", String.class);
		builder.setActionType(ActionType.REMOVER);
		othr = new EffectivePredicate(mthd).merge(base);
		assertSame(builder, othr, mthd);

		// U
		base = new EffectivePredicate(interfaceClass.getMethod("getU"));
		builder.setType(RDFNode.class).setName("u")
				.setInternalType(RDFNode.class).setLiteralType(null)
				.setCollectionType(Set.class);

		mthd = interfaceClass.getMethod("addU", RDFNode.class);
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.SETTER);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("hasU", RDFNode.class);
		othr = new EffectivePredicate(mthd).merge(base);
		builder.setActionType(ActionType.EXISTENTIAL);
		assertSame(builder, othr, mthd);

		mthd = interfaceClass.getMethod("removeU", RDFNode.class);
		builder.setActionType(ActionType.REMOVER);
		othr = new EffectivePredicate(mthd).merge(base);
		assertSame(builder, othr, mthd);

		// U2
		base = new EffectivePredicate(interfaceClass.getMethod("getU2"));
		builder.setType(RDFNode.class).setCollectionType(ExtendedIterator.class)
				.setName("u2");

	}

	@Test
	public void testAddBool() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(XSDDatatype.XSDboolean).setType(Boolean.class)
				.setName("bool");
		updateAddBool();
		assertSame(builder, interfaceClass.getMethod("addBool", Boolean.class));
	}

	protected void updateAddBool()
	{
	}

	@Test
	public void testGetBool() throws Exception
	{
		builder.setActionType(ActionType.GETTER).setCollectionType(Set.class)
				.setName("bool");
		updateGetBool();
		assertSame(builder, interfaceClass.getMethod("getBool"));
	}

	protected void updateGetBool()
	{
	}

	@Test
	public void testHasBool() throws Exception
	{
		builder.setActionType(ActionType.EXISTENTIAL)
				.setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(XSDDatatype.XSDboolean).setType(Boolean.class)
				.setName("bool");
		updateHasBool();
		assertSame(builder, interfaceClass.getMethod("hasBool", Boolean.class));
	}

	protected void updateHasBool()
	{
	}

	@Test
	public void testRemoveBool() throws Exception
	{
		builder.setActionType(ActionType.REMOVER).setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(XSDDatatype.XSDboolean).setType(Boolean.class)
				.setName("bool");
		updateRemoveBool();
		assertSame(builder,
				interfaceClass.getMethod("removeBool", Boolean.class));
	}

	protected void updateRemoveBool()
	{
	}

	@Test
	public void testAddChar() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(CharacterDatatype.INSTANCE)
				.setType(Character.class).setName("char");
		updateAddChar();
		assertSame(builder,
				interfaceClass.getMethod("addChar", Character.class));
	}

	protected void updateAddChar()
	{
	}

	@Test
	public void testGetChar() throws Exception
	{
		builder.setActionType(ActionType.GETTER).setCollectionType(List.class)
				.setName("char");
		updateGetChar();
		assertSame(builder, interfaceClass.getMethod("getChar"));
	}

	protected void updateGetChar()
	{
	}

	@Test
	public void testHasChar() throws Exception
	{
		builder.setActionType(ActionType.EXISTENTIAL)
				.setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(CharacterDatatype.INSTANCE)
				.setType(Character.class).setName("char");
		updateHasChar();
		assertSame(builder,
				interfaceClass.getMethod("hasChar", Character.class));
	}

	protected void updateHasChar()
	{
	}

	@Test
	public void testRemoveChar() throws Exception
	{
		builder.setActionType(ActionType.REMOVER).setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(CharacterDatatype.INSTANCE)
				.setType(Character.class).setName("char");
		updateRemoveChar();
		assertSame(builder,
				interfaceClass.getMethod("removeChar", Character.class));
	}

	protected void updateRemoveChar()
	{
	}

	@Test
	public void testAddDbl() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(XSDDatatype.XSDdouble).setType(Double.class)
				.setName("dbl");
		updateAddDbl();
		assertSame(builder, interfaceClass.getMethod("addDbl", Double.class));
	}

	protected void updateAddDbl()
	{
	}

	@Test
	public void testGetDbl() throws Exception
	{
		builder.setActionType(ActionType.GETTER).setCollectionType(Queue.class)
				.setName("dbl");
		updateGetDbl();
		assertSame(builder, interfaceClass.getMethod("getDbl"));
	}

	protected void updateGetDbl()
	{
	}

	@Test
	public void testHasDbl() throws Exception
	{
		builder.setActionType(ActionType.EXISTENTIAL)
				.setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(XSDDatatype.XSDdouble).setType(Double.class)
				.setName("dbl");
		updateHasDbl();
		assertSame(builder, interfaceClass.getMethod("hasDbl", Double.class));
	}

	protected void updateHasDbl()
	{
	}

	@Test
	public void testRemoveDbl() throws Exception
	{
		builder.setActionType(ActionType.REMOVER).setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(XSDDatatype.XSDdouble).setType(Double.class)
				.setName("dbl");
		updateRemoveDbl();
		assertSame(builder,
				interfaceClass.getMethod("removeDbl", Double.class));
	}

	protected void updateRemoveDbl()
	{
	}

	@Test
	public void testAddEnt() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setInternalType(RDFNode.class)
				.setType(TestInterface.class)
				.setCollectionType(Predicate.UNSET.class).setName("ent");
		updateAddEnt();
		assertSame(builder,
				interfaceClass.getMethod("addEnt", TestInterface.class));
	}

	protected void updateAddEnt()
	{
	}

	@Test
	public void testGetEnt() throws Exception
	{
		builder.setActionType(ActionType.GETTER).setCollectionType(Queue.class)
				.setName("ent");
		updateGetEnt();
		assertSame(builder, interfaceClass.getMethod("getEnt"));
	}

	protected void updateGetEnt()
	{
	}

	@Test
	public void testHasEnt() throws Exception
	{
		builder.setActionType(ActionType.EXISTENTIAL)
				.setInternalType(RDFNode.class).setType(TestInterface.class)
				.setCollectionType(Predicate.UNSET.class).setName("ent");
		updateHasEnt();
		assertSame(builder,
				interfaceClass.getMethod("hasEnt", TestInterface.class));
	}

	protected void updateHasEnt()
	{
	}

	@Test
	public void testRemoveEnt() throws Exception
	{
		builder.setActionType(ActionType.REMOVER).setInternalType(RDFNode.class)
				.setType(TestInterface.class)
				.setCollectionType(Predicate.UNSET.class).setName("ent");
		updateRemoveEnt();
		assertSame(builder,
				interfaceClass.getMethod("removeEnt", TestInterface.class));
	}

	protected void updateRemoveEnt()
	{
	}

	@Test
	public void testAddFlt() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(XSDDatatype.XSDfloat).setType(Float.class)
				.setName("flt");
		updateAddFlt();
		assertSame(builder, interfaceClass.getMethod("addFlt", Float.class));
	}

	protected void updateAddFlt()
	{
	}

	@Test
	public void testGetFlt() throws Exception
	{
		builder.setActionType(ActionType.GETTER).setCollectionType(Set.class)
				.setName("flt");
		updateGetFlt();
		assertSame(builder, interfaceClass.getMethod("getFlt"));
	}

	protected void updateGetFlt()
	{
	}

	@Test
	public void testHasFlt() throws Exception
	{
		builder.setActionType(ActionType.EXISTENTIAL)
				.setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(XSDDatatype.XSDfloat).setType(Float.class)
				.setName("flt");
		updateHasFlt();
		assertSame(builder, interfaceClass.getMethod("hasFlt", Float.class));
	}

	protected void updateHasFlt()
	{
	}

	@Test
	public void testRemoveFlt() throws Exception
	{
		builder.setActionType(ActionType.REMOVER).setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(XSDDatatype.XSDfloat).setType(Float.class)
				.setName("flt");
		updateRemoveFlt();
		assertSame(builder, interfaceClass.getMethod("removeFlt", Float.class));
	}

	protected void updateRemoveFlt()
	{
	}

	@Test
	public void testAddInt() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setInternalType(Literal.class)
				.setLiteralType(XSDDatatype.XSDint)
				.setCollectionType(Predicate.UNSET.class).setType(Integer.class)
				.setName("int");
		updateAddInt();
		assertSame(builder, interfaceClass.getMethod("addInt", Integer.class));
	}

	protected void updateAddInt()
	{
	}

	@Test
	public void testGetInt() throws Exception
	{
		builder.setActionType(ActionType.GETTER).setCollectionType(Queue.class)
				.setName("int");
		updateGetInt();
		assertSame(builder, interfaceClass.getMethod("getInt"));
	}

	protected void updateGetInt()
	{
	}

	@Test
	public void testHasInt() throws Exception
	{
		builder.setActionType(ActionType.EXISTENTIAL)
				.setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(XSDDatatype.XSDint).setType(Integer.class)
				.setName("int");
		updateHasInt();
		assertSame(builder, interfaceClass.getMethod("hasInt", Integer.class));
	}

	protected void updateHasInt()
	{
	}

	@Test
	public void testRemoveInt() throws Exception
	{
		builder.setActionType(ActionType.REMOVER).setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(XSDDatatype.XSDint).setType(Integer.class)
				.setName("int");
		updateRemoveInt();
		assertSame(builder,
				interfaceClass.getMethod("removeInt", Integer.class));
	}

	protected void updateRemoveInt()
	{
	}

	@Test
	public void testAddLng() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(LongDatatype.INSTANCE).setType(Long.class)
				.setName("lng");
		updateAddLng();
		assertSame(builder, interfaceClass.getMethod("addLng", Long.class));
	}

	protected void updateAddLng()
	{
	}

	@Test
	public void testGetLng() throws Exception
	{
		builder.setActionType(ActionType.GETTER).setCollectionType(List.class)
				.setName("lng");
		updateGetLng();
		assertSame(builder, interfaceClass.getMethod("getLng"));
	}

	protected void updateGetLng()
	{
	}

	@Test
	public void testHasLng() throws Exception
	{
		builder.setActionType(ActionType.EXISTENTIAL)
				.setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(LongDatatype.INSTANCE).setType(Long.class)
				.setName("lng");
		updateHasLng();
		assertSame(builder, interfaceClass.getMethod("hasLng", Long.class));
	}

	protected void updateHasLng()
	{
	}

	@Test
	public void testRemoveLng() throws Exception
	{
		builder.setActionType(ActionType.REMOVER).setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(LongDatatype.INSTANCE).setType(Long.class)
				.setName("lng");
		updateRemoveLng();
		assertSame(builder, interfaceClass.getMethod("removeLng", Long.class));
	}

	protected void updateRemoveLng()
	{
	}

	@Test
	public void testAddRDF() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setInternalType(RDFNode.class)
				.setType(RDFNode.class).setCollectionType(Predicate.UNSET.class)
				.setName("rDF");
		updateAddRDF();
		assertSame(builder, interfaceClass.getMethod("addRDF", RDFNode.class));
	}

	protected void updateAddRDF()
	{
	}

	@Test
	public void testGetRDF() throws Exception
	{
		builder.setActionType(ActionType.GETTER).setCollectionType(List.class)
				.setName("rDF");
		updateGetRDF();
		assertSame(builder, interfaceClass.getMethod("getRDF"));
	}

	protected void updateGetRDF()
	{
	}

	@Test
	public void testHasRDF() throws Exception
	{
		builder.setActionType(ActionType.EXISTENTIAL)
				.setInternalType(RDFNode.class).setType(RDFNode.class)
				.setCollectionType(Predicate.UNSET.class).setName("rDF");
		updateHasRDF();
		assertSame(builder, interfaceClass.getMethod("hasRDF", RDFNode.class));
	}

	protected void updateHasRDF()
	{
	}

	@Test
	public void testRemoveRDF() throws Exception
	{
		builder.setActionType(ActionType.REMOVER).setInternalType(RDFNode.class)
				.setType(RDFNode.class).setCollectionType(Predicate.UNSET.class)
				.setName("rDF");
		updateRemoveRDF();
		assertSame(builder,
				interfaceClass.getMethod("removeRDF", RDFNode.class));
	}

	protected void updateRemoveRDF()
	{
	}

	@Test
	public void testAddStr() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(XSDDatatype.XSDstring).setType(String.class)
				.setName("str");
		updateAddStr();
		assertSame(builder, interfaceClass.getMethod("addStr", String.class));
	}

	protected void updateAddStr()
	{
	}

	@Test
	public void testGetStr() throws Exception
	{
		builder.setActionType(ActionType.GETTER).setCollectionType(Set.class)
				.setName("str");
		updateGetStr();
		assertSame(builder, interfaceClass.getMethod("getStr"));
	}

	protected void updateGetStr()
	{
	}

	@Test
	public void testHasStr() throws Exception
	{
		builder.setActionType(ActionType.EXISTENTIAL)
				.setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(XSDDatatype.XSDstring).setType(String.class)
				.setName("str");
		updateHasStr();
		assertSame(builder, interfaceClass.getMethod("hasStr", String.class));
	}

	protected void updateHasStr()
	{
	}

	@Test
	public void testRemoveStr() throws Exception
	{
		builder.setActionType(ActionType.REMOVER).setInternalType(Literal.class)
				.setCollectionType(Predicate.UNSET.class)
				.setLiteralType(XSDDatatype.XSDstring).setType(String.class)
				.setName("str");
		updateRemoveStr();
		assertSame(builder,
				interfaceClass.getMethod("removeStr", String.class));
	}

	protected void updateRemoveStr()
	{
	}

	@Test
	public void testAddU_R() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setInternalType(RDFNode.class)
				.setType(RDFNode.class).setCollectionType(Predicate.UNSET.class)
				.setName("u");
		updateAddU_R();
		assertSame(builder, interfaceClass.getMethod("addU", RDFNode.class));
	}

	protected void updateAddU_R()
	{
	}

	@Test
	public void testAddU_S() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setInternalType(RDFNode.class)
				.setType(URI.class).setCollectionType(Predicate.UNSET.class)
				.setName("u");
		updateAddU_S();
		assertSame(builder, interfaceClass.getMethod("addU", String.class));
	}

	protected void updateAddU_S()
	{
	}

	@Test
	public void testGetU() throws Exception
	{
		builder.setActionType(ActionType.GETTER).setCollectionType(Set.class)
				.setInternalType(RDFNode.class).setType(RDFNode.class)
				.setName("u");
		updateGetU();
		assertSame(builder, interfaceClass.getMethod("getU"));
	}

	protected void updateGetU()
	{
	}

	@Test
	public void testHasU_R() throws Exception
	{
		builder.setActionType(ActionType.EXISTENTIAL)
				.setInternalType(RDFNode.class).setType(RDFNode.class)
				.setCollectionType(Predicate.UNSET.class).setName("u");
		updateHasU_R();
		assertSame(builder, interfaceClass.getMethod("hasU", RDFNode.class));
	}

	protected void updateHasU_R()
	{
	}

	@Test
	public void testHasU_S() throws Exception
	{
		builder.setActionType(ActionType.EXISTENTIAL)
				.setInternalType(RDFNode.class).setType(URI.class)
				.setCollectionType(Predicate.UNSET.class).setName("u");
		updateHasU_S();
		assertSame(builder, interfaceClass.getMethod("hasU", String.class));
	}

	protected void updateHasU_S()
	{
	}

	@Test
	public void testRemoveU_S() throws Exception
	{
		builder.setActionType(ActionType.REMOVER).setInternalType(RDFNode.class)
				.setType(URI.class).setCollectionType(Predicate.UNSET.class)
				.setName("u");
		updateRemoveU_S();
		assertSame(builder, interfaceClass.getMethod("removeU", String.class));
	}

	protected void updateRemoveU_S()
	{
	}

	@Test
	public void testRemoveU_R() throws Exception
	{
		builder.setActionType(ActionType.REMOVER).setInternalType(RDFNode.class)
				.setType(RDFNode.class).setCollectionType(Predicate.UNSET.class)
				.setName("u");
		updateRemoveU_R();
		assertSame(builder, interfaceClass.getMethod("removeU", RDFNode.class));
	}

	protected void updateRemoveU_R()
	{
	}

	@Test
	public void testAddU3_R() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setInternalType(RDFNode.class)
				.setType(RDFNode.class).setCollectionType(Predicate.UNSET.class)
				.setName("u3");
		updateAddU3_R();
		assertSame(builder, interfaceClass.getMethod("addU3", RDFNode.class));
	}

	protected void updateAddU3_R()
	{
	}

	@Test
	public void testAddU3_S() throws Exception
	{
		builder.setActionType(ActionType.SETTER).setInternalType(RDFNode.class)
				.setType(URI.class).setCollectionType(Predicate.UNSET.class)
				.setName("u3");
		updateAddU3_S();
		assertSame(builder, interfaceClass.getMethod("addU3", String.class));
	}

	protected void updateAddU3_S()
	{
	}

	@Test
	public void testGetU3() throws Exception
	{
		builder.setActionType(ActionType.GETTER).setCollectionType(Queue.class)
				.setInternalType(RDFNode.class).setType(RDFNode.class)
				.setName("u3");
		updateGetU3();
		assertSame(builder, interfaceClass.getMethod("getU3"));
	}

	protected void updateGetU3()
	{
	}

	@Test
	public void testHasU3_R() throws Exception
	{
		builder.setActionType(ActionType.EXISTENTIAL)
				.setInternalType(RDFNode.class).setType(RDFNode.class)
				.setCollectionType(Predicate.UNSET.class).setName("u3");
		updateHasU3_R();
		assertSame(builder, interfaceClass.getMethod("hasU3", RDFNode.class));
	}

	protected void updateHasU3_R()
	{
	}

	@Test
	public void testHasU3_S() throws Exception
	{
		builder.setActionType(ActionType.EXISTENTIAL)
				.setInternalType(RDFNode.class).setType(URI.class)
				.setCollectionType(Predicate.UNSET.class).setName("u3");
		updateHasU3_S();
		assertSame(builder, interfaceClass.getMethod("hasU3", String.class));
	}

	protected void updateHasU3_S()
	{
	}

	@Test
	public void testRemoveU3_S() throws Exception
	{
		builder.setActionType(ActionType.REMOVER).setInternalType(RDFNode.class)
				.setType(URI.class).setCollectionType(Predicate.UNSET.class)
				.setName("u3");
		updateRemoveU3_S();
		assertSame(builder, interfaceClass.getMethod("removeU3", String.class));
	}

	protected void updateRemoveU3_S()
	{
	}

	@Test
	public void testRemoveU3_R() throws Exception
	{
		builder.setActionType(ActionType.REMOVER).setInternalType(RDFNode.class)
				.setType(RDFNode.class).setCollectionType(Predicate.UNSET.class)
				.setName("u3");
		updateRemoveU3_R();
		assertSame(builder,
				interfaceClass.getMethod("removeU3", RDFNode.class));
	}

	protected void updateRemoveU3_R()
	{
	}

	@Test
	public void testGetU4() throws Exception
	{
		builder.setActionType(ActionType.GETTER).setCollectionType(Set.class)
				.setInternalType(RDFNode.class).setType(URI.class)
				.setName("u3");
		updateGetU4();
		assertSame(builder, interfaceClass.getMethod("getU4"));
	}

	protected void updateGetU4()
	{
	}

}