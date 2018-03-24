package cat.altimiras.xml.parsed;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ListsTest {

	@Test
	public void xmlListTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listTest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals(2, o.get("wrapperWeDontWant/ListTestObj/list").asList().size());
		assertEquals("111", o.get("wrapperWeDontWant/ListTestObj/list").asList().get(0).get("/SimpleTestObj/element1").value());
		assertEquals("111", o.get("wrapperWeDontWant/ListTestObj/list").asList().get(1).get("/SimpleTestObj/element1").value());
	}

	@Test
	public void xmlList2Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/list2Test.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals(3, o.get("ListTestObj/list").asList().size());
		assertEquals("111", o.get("/ListTestObj/list").asList().get(0).get("/SimpleTestObj/element1").value());
		assertEquals("222", o.get("/ListTestObj/list").asList().get(1).get("/SimpleTestObj/element2").value());
		assertEquals("111", o.get("/ListTestObj/list").asList().get(2).get("/SimpleTestObj/element1").value());
		assertEquals("222", o.get("/ListTestObj/list").asList().get(2).get("/SimpleTestObj/element2").value());
	}

	@Test
	public void xmlListNestedTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listNestedTest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals(1, o.get("Nested4TestObj/simpleElements").asList().size());
		assertEquals("111", o.get("/Nested4TestObj/simpleElements").asList().get(0).get("/SimpleTestObj/element1").value());
		assertEquals("222", o.get("/Nested4TestObj/simpleElements").asList().get(0).get("/SimpleTestObj/element2").value());
		assertEquals("title", o.get("/Nested4TestObj/title").value());
	}

	@Test
	public void xmlListNestedSelfClosedTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listNestedselfClosedTest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals(4, o.get("Nested5TestObj/list").asList().size());
		assertEquals("111", o.get("/Nested5TestObj/list").asList().get(0).get("/simpleTestObj1/element1").value());
		assertEquals("222", o.get("/Nested5TestObj/list").asList().get(1).get("/simpleTestObj1/element2").value());
		assertEquals("333", o.get("/Nested5TestObj/list").asList().get(2).get("/simpleTestObj1/element2").value());
		assertEquals("444", o.get("/Nested5TestObj/list").asList().get(3).get("/simpleTestObj1/element1").value());
		assertEquals("555", o.get("/Nested5TestObj/list").asList().get(3).get("/simpleTestObj1/element2").value());
		assertEquals("title", o.get("/Nested5TestObj/title").value());
	}

	@Test
	public void xmlListSelfClosedTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listSelfClosedTest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals(3, o.get("/ListTestObj/list").asList().size());
		assertEquals("111", o.get("/ListTestObj/list").asList().get(0).get("/SimpleTestObj/element1").value());
		assertNull(o.get("/ListTestObj/list").asList().get(0).get("/SimpleTestObj/element2").value());
		assertEquals("222", o.get("/ListTestObj/list").asList().get(1).get("/SimpleTestObj/element2").value());
		assertNull(o.get("/ListTestObj/list").asList().get(1).get("/SimpleTestObj/element1").value());
		assertEquals("333", o.get("/ListTestObj/list").asList().get(2).get("/SimpleTestObj/element1").value());
		assertEquals("444", o.get("/ListTestObj/list").asList().get(2).get("/SimpleTestObj/element2").value());
	}

	@Test
	public void xmlEmptyListTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/emptyListTest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertTrue(o.get("ListTestObj/list").asList().isEmpty());
	}

	@Test
	public void xmlListPrimitivesTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listPrimitives.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("8", o.get("ListPrimitivesObj/values").asList().get(0).get("val").value());
		assertEquals("9", o.get("ListPrimitivesObj/values").asList().get(1).get("val").value());
	}

	@Test
	public void xmlListPrimitives2Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listPrimitives2.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("hola", o.get("Nested7TestObj/field").value());
		assertEquals("8", o.get("Nested7TestObj/Values").asList().get(0).get("val").value());
		assertEquals("9", o.get("Nested7TestObj/Values").asList().get(1).get("val").value());
		assertEquals("1", o.get("Nested7TestObj/aaa/values").asList().get(0).get("val").value());
		assertEquals("2", o.get("Nested7TestObj/aaa/values").asList().get(1).get("val").value());
		assertEquals("3", o.get("Nested7TestObj/bbb/values").asList().get(0).get("val").value());
	}

	@Test
	public void xmlListPrimitivesNoWrapperTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listPrimitivesNoWrapper.xml"), "UTF-8");

		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("8", o.get("ListPrimitivesObj").asList().get(0).get("values").value());
		assertEquals("9", o.get("ListPrimitivesObj").asList().get(1).get("values").value());
	}

	@Test
	public void xmlListPrimitives2NoWrapperTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listPrimitives2NoWrapper.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("hola", o.get("Nested7TestObj/field").value());
		assertEquals("8", o.get("Nested7TestObj/Values").asList().get(0).get("Values").value());
		assertEquals("9", o.get("Nested7TestObj/Values").asList().get(1).get("Values").value());
		assertEquals("1", o.get("Nested7TestObj/aaa").asList().get(0).get("values").value());
		assertEquals("2", o.get("Nested7TestObj/aaa").asList().get(1).get("values").value());
		assertEquals("3", o.get("Nested7TestObj/bbb").asList().get(0).get("values").value());
	}

	@Test
	public void xmlListNoWrapperWithAttributesTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listNoWrapperWithAttributes.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("lolo", o.get("ListTestObj2/field").value());
		assertEquals(3, o.get("ListTestObj2/SimpleTestObj").asList().size());
		assertEquals("111", o.get("ListTestObj2/SimpleTestObj").asList().get(0).get("SimpleTestObj/element1").value());
		assertEquals("222", o.get("ListTestObj2/SimpleTestObj").asList().get(0).get("SimpleTestObj/element2").value());
		assertEquals("333", o.get("ListTestObj2/SimpleTestObj").asList().get(1).get("SimpleTestObj/element1").value());
		assertEquals("444", o.get("ListTestObj2/SimpleTestObj").asList().get(1).get("SimpleTestObj/element2").value());
		assertEquals("555", o.get("ListTestObj2/SimpleTestObj").asList().get(2).get("SimpleTestObj/element1").value());
		assertEquals("666", o.get("ListTestObj2/SimpleTestObj").asList().get(2).get("SimpleTestObj/element2").value());
	}

	@Test
	public void xmlListNoWrapperWithAttributesTest2() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listNoWrapperWithAttributes2.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("lolo", o.get("ListTestObj2/field").value());
		assertEquals(3, o.get("ListTestObj2/SimpleTestObj").asList().size());
		assertEquals("111", o.get("ListTestObj2/SimpleTestObj").asList().get(0).get("SimpleTestObj/element1").value());
		assertEquals("222", o.get("ListTestObj2/SimpleTestObj").asList().get(0).get("SimpleTestObj/element2").value());
		assertEquals("333", o.get("ListTestObj2/SimpleTestObj").asList().get(1).get("SimpleTestObj/element1").value());
		assertEquals("444", o.get("ListTestObj2/SimpleTestObj").asList().get(1).get("SimpleTestObj/element2").value());
		assertEquals("555", o.get("ListTestObj2/SimpleTestObj").asList().get(2).get("SimpleTestObj/element1").value());
		assertEquals("666", o.get("ListTestObj2/SimpleTestObj").asList().get(2).get("SimpleTestObj/element2").value());

	}

	@Test
	public void xmlListNoWrapper() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listNoWrapper.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("field", o.get("wrapperWeDontWant/Nested8TestObj/field").value());
		assertEquals(2, o.get("wrapperWeDontWant/Nested8TestObj/list").asList().size());
		assertEquals("lolo", o.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(0).get("Nested6TestObj/title").value());
		assertEquals(3, o.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(0).get("Nested6TestObj/Nested5TestObj").asList().size());
		assertEquals("111", o.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(0).get("Nested6TestObj/Nested5TestObj").asList().get(0).get("Nested5TestObj/title").value());
		assertEquals("222", o.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(0).get("Nested6TestObj/Nested5TestObj").asList().get(1).get("Nested5TestObj/title").value());
		assertEquals("333", o.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(0).get("Nested6TestObj/Nested5TestObj").asList().get(2).get("Nested5TestObj/title").value());
		assertEquals("lala", o.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(1).get("Nested6TestObj/title").value());
		assertEquals(3, o.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(1).get("Nested6TestObj/Nested5TestObj").asList().size());
		assertEquals("444", o.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(1).get("Nested6TestObj/Nested5TestObj").asList().get(0).get("Nested5TestObj/title").value());
		assertEquals("555", o.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(1).get("Nested6TestObj/Nested5TestObj").asList().get(1).get("Nested5TestObj/title").value());
		assertEquals("666", o.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(1).get("Nested6TestObj/Nested5TestObj").asList().get(2).get("Nested5TestObj/title").value());
	}
}

