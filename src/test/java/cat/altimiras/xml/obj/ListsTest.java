package cat.altimiras.xml.obj;

import cat.altimiras.Parser;
import cat.altimiras.xml.pojo.ListPrimitivesObj;
import cat.altimiras.xml.pojo.ListTestObj;
import cat.altimiras.xml.pojo.ListTestObj2;
import cat.altimiras.xml.pojo.Nested10TestObj;
import cat.altimiras.xml.pojo.Nested4TestObj;
import cat.altimiras.xml.pojo.Nested5TestObj;
import cat.altimiras.xml.pojo.Nested7TestObj;
import cat.altimiras.xml.pojo.Nested8TestObj;
import cat.altimiras.xml.pojo.Nested9TestObj;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ListsTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test
	public void xmlListTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listTest.xml"), "UTF-8");
		Parser<ListTestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, ListTestObj.class, ci);

		ListTestObj o = parser.parse(xml);

		assertEquals("111", o.getList().get(0).getElement1().trim());
		assertEquals("111", o.getList().get(1).getElement1().trim());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlList2Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/list2Test.xml"), "UTF-8");
		Parser<ListTestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, ListTestObj.class, ci);

		ListTestObj o = parser.parse(xml);

		assertEquals("111", o.getList().get(0).getElement1().trim());
		assertEquals("222", o.getList().get(1).getElement2().trim());
		assertEquals("111", o.getList().get(2).getElement1().trim());
		assertEquals("222", o.getList().get(2).getElement2().trim());
		assertEquals(3, o.getList().size());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlListNestedTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested4TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listNestedTest.xml"), "UTF-8");
		Parser<Nested4TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested4TestObj.class, ci);

		Nested4TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle());
		assertEquals("111", o.getSimpleElements().get(0).getElement1().trim());
		assertEquals("222", o.getSimpleElements().get(0).getElement2().trim());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlListNestedSelfClosedTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested5TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listNestedselfClosedTest.xml"), "UTF-8");
		Parser<Nested5TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested5TestObj.class, ci);

		Nested5TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle());
		assertEquals("111", o.getList().get(0).getElement1().trim());
		assertEquals("222", o.getList().get(1).getElement2().trim());
		assertEquals("333", o.getList().get(2).getElement2().trim());
		assertEquals("444", o.getList().get(3).getElement1().trim());
		assertEquals("555", o.getList().get(3).getElement2().trim());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlListSelfClosedTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listSelfClosedTest.xml"), "UTF-8");
		Parser<ListTestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, ListTestObj.class, ci);

		ListTestObj o = parser.parse(xml);

		assertEquals("111", o.getList().get(0).getElement1().trim());
		assertNull(o.getList().get(0).getElement2());
		assertEquals("222", o.getList().get(1).getElement2().trim());
		assertNull(o.getList().get(1).getElement1());
		assertEquals("333", o.getList().get(2).getElement1().trim());
		assertEquals("444", o.getList().get(2).getElement2().trim());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlEmptyListTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/emptyListTest.xml"), "UTF-8");
		Parser<ListTestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, ListTestObj.class, ci);

		ListTestObj o = parser.parse(xml);

		assertTrue(o.getList().isEmpty());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlListPrimitivesTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListPrimitivesObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listPrimitives.xml"), "UTF-8");
		Parser<ListPrimitivesObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, ListPrimitivesObj.class, ci);

		ListPrimitivesObj o = parser.parse(xml);

		assertEquals(new Integer(8), o.getValues().get(0));
		assertEquals(new Integer(9), o.getValues().get(1));
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlListPrimitives2Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested7TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listPrimitives2.xml"), "UTF-8");
		Parser<Nested7TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested7TestObj.class, ci);

		Nested7TestObj o = parser.parse(xml);

		assertEquals("hola", o.getField());
		assertEquals(new Integer(8), o.getValues().get(0));
		assertEquals(new Integer(9), o.getValues().get(1));
		assertEquals(new Integer(1), o.getAaa().getValues().get(0));
		assertEquals(new Integer(2), o.getAaa().getValues().get(1));
		assertEquals(new Integer(3), o.getBbb().getValues().get(0));
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlListPrimitivesNoWrapperTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListPrimitivesObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listPrimitivesNoWrapper.xml"), "UTF-8");

		Parser<ListPrimitivesObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, ListPrimitivesObj.class, ci);

		ListPrimitivesObj o = parser.parse(xml);

		assertEquals(new Integer(8), o.getValues().get(0));
		assertEquals(new Integer(9), o.getValues().get(1));
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlListPrimitives2NoWrapperTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested7TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listPrimitives2NoWrapper.xml"), "UTF-8");
		Parser<Nested7TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested7TestObj.class, ci);

		Nested7TestObj o = parser.parse(xml);

		assertEquals("hola", o.getField());
		assertEquals(new Integer(8), o.getValues().get(0));
		assertEquals(new Integer(9), o.getValues().get(1));
		assertEquals(new Integer(1), o.getAaa().getValues().get(0));
		assertEquals(new Integer(2), o.getAaa().getValues().get(1));
		assertEquals(new Integer(3), o.getBbb().getValues().get(0));
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlListNoWrapperWithAttributesTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj2.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listNoWrapperWithAttributes.xml"), "UTF-8");
		Parser<ListTestObj2> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, ListTestObj2.class, ci);

		ListTestObj2 o = parser.parse(xml);

		assertEquals("lolo", o.getField());
		assertEquals("111", o.getList().get(0).getElement1().trim());
		assertEquals("222", o.getList().get(0).getElement2().trim());
		assertEquals("333", o.getList().get(1).getElement1().trim());
		assertEquals("444", o.getList().get(1).getElement2().trim());
		assertEquals("555", o.getList().get(2).getElement1().trim());
		assertEquals("666", o.getList().get(2).getElement2().trim());
		assertEquals(3, o.getList().size());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlListNoWrapperWithAttributesTest2() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj2.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listNoWrapperWithAttributes2.xml"), "UTF-8");
		Parser<ListTestObj2> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, ListTestObj2.class, ci);

		ListTestObj2 o = parser.parse(xml);

		assertEquals("lolo", o.getField());
		assertEquals("111", o.getList().get(0).getElement1().trim());
		assertEquals("222", o.getList().get(0).getElement2().trim());
		assertEquals("333", o.getList().get(1).getElement1().trim());
		assertEquals("444", o.getList().get(1).getElement2().trim());
		assertEquals("555", o.getList().get(2).getElement1().trim());
		assertEquals("666", o.getList().get(2).getElement2().trim());
		assertEquals(3, o.getList().size());

	}

	@Test
	public void xmlListNoWrapper() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested8TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listNoWrapper.xml"), "UTF-8");
		Parser<Nested8TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested8TestObj.class, ci);

		Nested8TestObj o = parser.parse(xml);

		assertEquals("field", o.getField());
		assertEquals("lolo", o.getList().get(0).getTitle());
		assertEquals("111", o.getList().get(0).getList().get(0).getTitle());
		assertEquals("222", o.getList().get(0).getList().get(1).getTitle());
		assertEquals("333", o.getList().get(0).getList().get(2).getTitle());
		assertEquals("lala", o.getList().get(1).getTitle());
		assertEquals("444", o.getList().get(1).getList().get(0).getTitle());
		assertEquals("555", o.getList().get(1).getList().get(1).getTitle());
		assertEquals("666", o.getList().get(1).getList().get(2).getTitle());
		assertEquals(2, o.getList().size());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlNoWrapperConsecutiveLists1() throws Exception {
		xmlNoWrapperConsecutiveLists("/xml/listsNoWrapperConsecutive.xml");
	}

	public void xmlNoWrapperConsecutiveLists(String file) throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested9TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream(file), "UTF-8");
		Parser<Nested9TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested9TestObj.class, ci);

		Nested9TestObj o = parser.parse(xml);

		assertEquals(2, o.getSimpleTestObj().size());
		assertEquals("11", o.getSimpleTestObj().get(0).getElement1());
		assertNull(o.getSimpleTestObj().get(0).getElement2());
		assertNull(o.getSimpleTestObj().get(1).getElement1());
		assertEquals("22", o.getSimpleTestObj().get(1).getElement2());

		assertEquals(2, o.getSimple2TestObj().size());
		assertEquals("33", o.getSimple2TestObj().get(0).getElement());
		assertEquals("44", o.getSimple2TestObj().get(1).getElement());

		assertEquals("asa", o.getField());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlNoWrapperConsecutiveLists2() throws Exception {
		xmlNoWrapperConsecutiveLists("/xml/listsNoWrapperConsecutive2.xml");
	}

	@Test
	public void xmlNoWrapperConsecutiveLists3() throws Exception {
		xmlNoWrapperConsecutiveLists("/xml/listsNoWrapperConsecutive3.xml");
	}

	@Test
	public void xmlNestedNoWrapperConsecutiveLists() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested10TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/nestedListsNoWrapperConsecutive.xml"), "UTF-8");
		Parser<Nested10TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested10TestObj.class, ci);

		Nested10TestObj o = parser.parse(xml);

		assertEquals("asa", o.getNested9TestObj().getField());
		assertEquals(2, o.getNested9TestObj().getSimpleTestObj().size());
		assertEquals("11", o.getNested9TestObj().getSimpleTestObj().get(0).getElement1());
		assertNull(o.getNested9TestObj().getSimpleTestObj().get(0).getElement2());
		assertNull(o.getNested9TestObj().getSimpleTestObj().get(1).getElement1());
		assertEquals("22", o.getNested9TestObj().getSimpleTestObj().get(1).getElement2());

		assertEquals(2, o.getNested9TestObj().getSimple2TestObj().size());
		assertEquals("33", o.getNested9TestObj().getSimple2TestObj().get(0).getElement());
		assertEquals("44", o.getNested9TestObj().getSimple2TestObj().get(1).getElement());

		assertNull(o.getNested6TestObj());

		assertFalse(o.isIncomplete());
	}

	@Test
	public void endList() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested9TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/endList.xml"), "UTF-8");
		Parser<Nested9TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested9TestObj.class, ci);

		Nested9TestObj o = parser.parse(xml);

		assertEquals(2, o.getSimpleTestObj().size());
		assertEquals("11", o.getSimpleTestObj().get(0).getElement1());
		assertEquals("22", o.getSimpleTestObj().get(1).getElement2());
		assertNull(o.getSimple2TestObj());

		assertFalse(o.isIncomplete());
	}

}
