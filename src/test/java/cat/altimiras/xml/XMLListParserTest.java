package cat.altimiras.xml;

import cat.altimiras.xml.pojo.ListPrimitivesObj;
import cat.altimiras.xml.pojo.ListTestObj;
import cat.altimiras.xml.pojo.ListTestObj2;
import cat.altimiras.xml.pojo.Nested4TestObj;
import cat.altimiras.xml.pojo.Nested5TestObj;
import cat.altimiras.xml.pojo.Nested6TestObj;
import cat.altimiras.xml.pojo.Nested7TestObj;
import cat.altimiras.xml.pojo.Nested8TestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class XMLListParserTest {

	@Test
	public void xmlListTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listTest.xml"), "UTF-8");
		XMLParser<ListTestObj> parser = new WoodStoxParserImpl<>(ListTestObj.class, ci);

		ListTestObj o = parser.parse(xml);

		assertEquals("111", o.getList().get(0).getElement1().trim());
		assertEquals("111", o.getList().get(1).getElement1().trim());
	}

	@Test
	public void xmlList2Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/list2Test.xml"), "UTF-8");
		XMLParser<ListTestObj> parser = new WoodStoxParserImpl<>(ListTestObj.class, ci);

		ListTestObj o = parser.parse(xml);

		assertEquals("111", o.getList().get(0).getElement1().trim());
		assertEquals("222", o.getList().get(1).getElement2().trim());
		assertEquals("111", o.getList().get(2).getElement1().trim());
		assertEquals("222", o.getList().get(2).getElement2().trim());
		assertEquals(3, o.getList().size());
	}

	@Test
	public void xmlListNestedTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested4TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listNestedTest.xml"), "UTF-8");
		XMLParser<Nested4TestObj> parser = new WoodStoxParserImpl<>(Nested4TestObj.class, ci);

		Nested4TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle());
		assertEquals("111", o.getSimpleElements().get(0).getElement1().trim());
		assertEquals("222", o.getSimpleElements().get(0).getElement2().trim());
	}

	@Test
	public void xmlListNestedSelfClosedTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested5TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listNestedselfClosedTest.xml"), "UTF-8");
		XMLParser<Nested5TestObj> parser = new WoodStoxParserImpl<>(Nested5TestObj.class, ci);

		Nested5TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle());
		assertEquals("111", o.getList().get(0).getElement1().trim());
		assertEquals("222", o.getList().get(1).getElement2().trim());
		assertEquals("333", o.getList().get(2).getElement2().trim());
		assertEquals("444", o.getList().get(3).getElement1().trim());
		assertEquals("555", o.getList().get(3).getElement2().trim());
	}

	@Test
	public void xmlListSelfClosedTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listSelfClosedTest.xml"), "UTF-8");
		XMLParser<ListTestObj> parser = new WoodStoxParserImpl<>(ListTestObj.class, ci);

		ListTestObj o = parser.parse(xml);

		assertEquals("111", o.getList().get(0).getElement1().trim());
		assertNull(o.getList().get(0).getElement2());
		assertEquals("222", o.getList().get(1).getElement2().trim());
		assertNull(o.getList().get(1).getElement1());
		assertEquals("333", o.getList().get(2).getElement1().trim());
		assertEquals("444", o.getList().get(2).getElement2().trim());
	}

	@Test
	public void xmlEmptyListTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/emptyListTest.xml"), "UTF-8");
		XMLParser<ListTestObj> parser = new WoodStoxParserImpl<>(ListTestObj.class, ci);

		ListTestObj o = parser.parse(xml);

		assertTrue(o.getList().isEmpty());
	}

	@Test
	public void xmlListPrimitivesTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListPrimitivesObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listPrimitives.xml"), "UTF-8");
		XMLParser<ListPrimitivesObj> parser = new WoodStoxParserImpl<>(ListPrimitivesObj.class, ci);

		ListPrimitivesObj o = parser.parse(xml);

		assertEquals(new Integer(8),o.getValues().get(0));
		assertEquals(new Integer(9), o.getValues().get(1));
	}

	@Test
	public void xmlListPrimitives2Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested7TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listPrimitives2.xml"), "UTF-8");
		XMLParser<Nested7TestObj> parser = new WoodStoxParserImpl<>(Nested7TestObj.class, ci);

		Nested7TestObj o = parser.parse(xml);

		assertEquals("hola", o.getField());
		assertEquals(new Integer(8),o.getValues().get(0));
		assertEquals(new Integer(9), o.getValues().get(1));
		assertEquals(new Integer(1), o.getAaa().getValues().get(0));
		assertEquals(new Integer(2), o.getAaa().getValues().get(1));
		assertEquals(new Integer(3), o.getBbb().getValues().get(0));
	}

	@Test
	public void xmlListPrimitivesNoWrapperTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListPrimitivesObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listPrimitivesNoWrapper.xml"), "UTF-8");

		XMLParser<ListPrimitivesObj> parser = new WoodStoxParserImpl<>(ListPrimitivesObj.class, ci);

		ListPrimitivesObj o = parser.parse(xml);

		assertEquals(new Integer(8),o.getValues().get(0));
		assertEquals(new Integer(9), o.getValues().get(1));
	}

	@Test
	public void xmlListPrimitives2NoWrapperTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested7TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listPrimitives2NoWrapper.xml"), "UTF-8");
		XMLParser<Nested7TestObj> parser = new WoodStoxParserImpl<>(Nested7TestObj.class, ci);

		Nested7TestObj o = parser.parse(xml);

		assertEquals("hola", o.getField());
		assertEquals(new Integer(8),o.getValues().get(0));
		assertEquals(new Integer(9), o.getValues().get(1));
		assertEquals(new Integer(1), o.getAaa().getValues().get(0));
		assertEquals(new Integer(2), o.getAaa().getValues().get(1));
		assertEquals(new Integer(3), o.getBbb().getValues().get(0));
	}

	@Test
	public void xmlListNoWrapperWithAttributesTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj2.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listPrimitivesNoWrapperWithAttributes.xml"), "UTF-8");
		XMLParser<ListTestObj2> parser = new WoodStoxParserImpl<>(ListTestObj2.class, ci);

		ListTestObj2 o = parser.parse(xml);

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

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listNoWrapper.xml"), "UTF-8");
		XMLParser<Nested8TestObj> parser = new WoodStoxParserImpl<>(Nested8TestObj.class, ci);

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
	}


}
