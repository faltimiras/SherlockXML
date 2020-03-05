package cat.altimiras.xml.map;

import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import java.util.Map;

import static cat.altimiras.xml.XMLFactory.DEFAULT_INCOMPLETE_KEY_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ListsTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test
	public void xmlListTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listTest.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(2, matryoshka.get("wrapperWeDontWant/ListTestObj/list").asList().size());
		assertEquals("111", matryoshka.get("wrapperWeDontWant/ListTestObj/list").asList().get(0).get("/SimpleTestObj/element1").value());
		assertEquals("111", matryoshka.get("wrapperWeDontWant/ListTestObj/list").asList().get(1).get("/SimpleTestObj/element1").value());
	}

	@Test
	public void xmlList2Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/list2Test.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(3, matryoshka.get("ListTestObj/list").asList().size());
		assertEquals("111", matryoshka.get("/ListTestObj/list").asList().get(0).get("/SimpleTestObj/element1").value());
		assertEquals("222", matryoshka.get("/ListTestObj/list").asList().get(1).get("/SimpleTestObj/element2").value());
		assertEquals("111", matryoshka.get("/ListTestObj/list").asList().get(2).get("/SimpleTestObj/element1").value());
		assertEquals("222", matryoshka.get("/ListTestObj/list").asList().get(2).get("/SimpleTestObj/element2").value());
	}

	@Test
	public void xmlListNestedTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listNestedTest.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(1, matryoshka.get("Nested4TestObj/simpleElements").asList().size());
		assertEquals("111", matryoshka.get("/Nested4TestObj/simpleElements").asList().get(0).get("/SimpleTestObj/element1").value());
		assertEquals("222", matryoshka.get("/Nested4TestObj/simpleElements").asList().get(0).get("/SimpleTestObj/element2").value());
		assertEquals("title", matryoshka.get("/Nested4TestObj/title").value());
	}

	@Test
	public void xmlListNestedSelfClosedTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listNestedselfClosedTest.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(4, matryoshka.get("Nested5TestObj/list").asList().size());
		assertEquals("111", matryoshka.get("/Nested5TestObj/list").asList().get(0).get("/simpleTestObj1/element1").value());
		assertEquals("222", matryoshka.get("/Nested5TestObj/list").asList().get(1).get("/simpleTestObj1/element2").value());
		assertEquals("333", matryoshka.get("/Nested5TestObj/list").asList().get(2).get("/simpleTestObj1/element2").value());
		assertEquals("444", matryoshka.get("/Nested5TestObj/list").asList().get(3).get("/simpleTestObj1/element1").value());
		assertEquals("555", matryoshka.get("/Nested5TestObj/list").asList().get(3).get("/simpleTestObj1/element2").value());
		assertEquals("title", matryoshka.get("/Nested5TestObj/title").value());
	}

	@Test
	public void xmlListSelfClosedTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listSelfClosedTest.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(3, matryoshka.get("/ListTestObj/list").asList().size());
		assertEquals("111", matryoshka.get("/ListTestObj/list").asList().get(0).get("/SimpleTestObj/element1").value());
		assertNull(matryoshka.get("/ListTestObj/list").asList().get(0).get("/SimpleTestObj/element2").value());
		assertEquals("222", matryoshka.get("/ListTestObj/list").asList().get(1).get("/SimpleTestObj/element2").value());
		assertNull(matryoshka.get("/ListTestObj/list").asList().get(1).get("/SimpleTestObj/element1").value());
		assertEquals("333", matryoshka.get("/ListTestObj/list").asList().get(2).get("/SimpleTestObj/element1").value());
		assertEquals("444", matryoshka.get("/ListTestObj/list").asList().get(2).get("/SimpleTestObj/element2").value());
	}

	@Test
	public void xmlEmptyListTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/emptyListTest.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertTrue(matryoshka.get("ListTestObj/list").asList().isEmpty());
	}

	@Test
	public void xmlListPrimitivesTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listPrimitives.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("8", matryoshka.get("ListPrimitivesObj/values").asList().get(0).get("val").value());
		assertEquals("9", matryoshka.get("ListPrimitivesObj/values").asList().get(1).get("val").value());
	}

	@Test
	public void xmlListPrimitives2Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listPrimitives2.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("hola", matryoshka.get("Nested7TestObj/field").value());
		assertEquals("8", matryoshka.get("Nested7TestObj/Values").asList().get(0).get("val").value());
		assertEquals("9", matryoshka.get("Nested7TestObj/Values").asList().get(1).get("val").value());
		assertEquals("1", matryoshka.get("Nested7TestObj/aaa/values").asList().get(0).get("val").value());
		assertEquals("2", matryoshka.get("Nested7TestObj/aaa/values").asList().get(1).get("val").value());
		assertEquals("3", matryoshka.get("Nested7TestObj/bbb/values").asList().get(0).get("val").value());
	}

	@Test
	public void xmlListPrimitivesNoWrapperTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listPrimitivesNoWrapper.xml"), "UTF-8");

		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("8", matryoshka.get("ListPrimitivesObj").asList().get(0).get("values").value());
		assertEquals("9", matryoshka.get("ListPrimitivesObj").asList().get(1).get("values").value());
	}

	@Test
	public void xmlListPrimitives2NoWrapperTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listPrimitives2NoWrapper.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("hola", matryoshka.get("Nested7TestObj/field").value());
		assertEquals("8", matryoshka.get("Nested7TestObj/Values").asList().get(0).get("Values").value());
		assertEquals("9", matryoshka.get("Nested7TestObj/Values").asList().get(1).get("Values").value());
		assertEquals("1", matryoshka.get("Nested7TestObj/aaa").asList().get(0).get("values").value());
		assertEquals("2", matryoshka.get("Nested7TestObj/aaa").asList().get(1).get("values").value());
		assertEquals("3", matryoshka.get("Nested7TestObj/bbb").asList().get(0).get("values").value());
	}

	@Test
	public void xmlListNoWrapperWithAttributesTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listNoWrapperWithAttributes.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("lolo", matryoshka.get("ListTestObj2/field").value());
		assertEquals(3, matryoshka.get("ListTestObj2/SimpleTestObj").asList().size());
		assertEquals("111", matryoshka.get("ListTestObj2/SimpleTestObj").asList().get(0).get("SimpleTestObj/element1").value());
		assertEquals("222", matryoshka.get("ListTestObj2/SimpleTestObj").asList().get(0).get("SimpleTestObj/element2").value());
		assertEquals("333", matryoshka.get("ListTestObj2/SimpleTestObj").asList().get(1).get("SimpleTestObj/element1").value());
		assertEquals("444", matryoshka.get("ListTestObj2/SimpleTestObj").asList().get(1).get("SimpleTestObj/element2").value());
		assertEquals("555", matryoshka.get("ListTestObj2/SimpleTestObj").asList().get(2).get("SimpleTestObj/element1").value());
		assertEquals("666", matryoshka.get("ListTestObj2/SimpleTestObj").asList().get(2).get("SimpleTestObj/element2").value());
	}

	@Test
	public void xmlListNoWrapperWithAttributesTest2() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listNoWrapperWithAttributes2.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("lolo", matryoshka.get("ListTestObj2/field").value());
		assertEquals(3, matryoshka.get("ListTestObj2/SimpleTestObj").asList().size());
		assertEquals("111", matryoshka.get("ListTestObj2/SimpleTestObj").asList().get(0).get("SimpleTestObj/element1").value());
		assertEquals("222", matryoshka.get("ListTestObj2/SimpleTestObj").asList().get(0).get("SimpleTestObj/element2").value());
		assertEquals("333", matryoshka.get("ListTestObj2/SimpleTestObj").asList().get(1).get("SimpleTestObj/element1").value());
		assertEquals("444", matryoshka.get("ListTestObj2/SimpleTestObj").asList().get(1).get("SimpleTestObj/element2").value());
		assertEquals("555", matryoshka.get("ListTestObj2/SimpleTestObj").asList().get(2).get("SimpleTestObj/element1").value());
		assertEquals("666", matryoshka.get("ListTestObj2/SimpleTestObj").asList().get(2).get("SimpleTestObj/element2").value());

	}

	@Test
	public void xmlListNoWrapper() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listNoWrapper.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("field", matryoshka.get("wrapperWeDontWant/Nested8TestObj/field").value());
		assertEquals(2, matryoshka.get("wrapperWeDontWant/Nested8TestObj/list").asList().size());
		assertEquals("lolo", matryoshka.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(0).get("Nested6TestObj/title").value());
		assertEquals(3, matryoshka.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(0).get("Nested6TestObj/Nested5TestObj").asList().size());
		assertEquals("111", matryoshka.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(0).get("Nested6TestObj/Nested5TestObj").asList().get(0).get("Nested5TestObj/title").value());
		assertEquals("222", matryoshka.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(0).get("Nested6TestObj/Nested5TestObj").asList().get(1).get("Nested5TestObj/title").value());
		assertEquals("333", matryoshka.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(0).get("Nested6TestObj/Nested5TestObj").asList().get(2).get("Nested5TestObj/title").value());
		assertEquals("lala", matryoshka.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(1).get("Nested6TestObj/title").value());
		assertEquals(3, matryoshka.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(1).get("Nested6TestObj/Nested5TestObj").asList().size());
		assertEquals("444", matryoshka.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(1).get("Nested6TestObj/Nested5TestObj").asList().get(0).get("Nested5TestObj/title").value());
		assertEquals("555", matryoshka.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(1).get("Nested6TestObj/Nested5TestObj").asList().get(1).get("Nested5TestObj/title").value());
		assertEquals("666", matryoshka.get("wrapperWeDontWant/Nested8TestObj/list").asList().get(1).get("Nested6TestObj/Nested5TestObj").asList().get(2).get("Nested5TestObj/title").value());
	}

	@Test
	public void xmlNoWrapperConsecutiveLists() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listsNoWrapperConsecutive.xml"), "UTF-8");

		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("11", matryoshka.get("Nested9TestObj/SimpleTestObj").asList().get(0).get("SimpleTestObj/element1").value());
		assertEquals("22", matryoshka.get("Nested9TestObj/SimpleTestObj").asList().get(1).get("SimpleTestObj/element2").value());
		assertEquals("33", matryoshka.get("Nested9TestObj/Simple2TestObj").asList().get(0).get("Simple2TestObj/element").value());
		assertEquals("44", matryoshka.get("Nested9TestObj/Simple2TestObj").asList().get(1).get("Simple2TestObj/element").value());
	}

	@Test
	public void xmlNestedNoWrapperConsecutiveLists() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/nestedListsNoWrapperConsecutive.xml"), "UTF-8");

		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("11", matryoshka.get("Nested10TestObj/nested9TestObj/SimpleTestObj").asList().get(0).get("SimpleTestObj/element1").value());
		assertEquals("22", matryoshka.get("Nested10TestObj/nested9TestObj/SimpleTestObj").asList().get(1).get("SimpleTestObj/element2").value());
		assertEquals("33", matryoshka.get("Nested10TestObj/nested9TestObj/Simple2TestObj").asList().get(0).get("Simple2TestObj/element").value());
		assertEquals("44", matryoshka.get("Nested10TestObj/nested9TestObj/Simple2TestObj").asList().get(1).get("Simple2TestObj/element").value());
	}

	@Test
	public void endList() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/endList.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(2, matryoshka.get("lalal/Nested9TestObj/SimpleTestObj").asList().size());
		assertEquals("11", matryoshka.get("lalal/Nested9TestObj/SimpleTestObj").asList().get(0).get("SimpleTestObj/element1").value());
		assertEquals("22", matryoshka.get("lalal/Nested9TestObj/SimpleTestObj").asList().get(1).get("SimpleTestObj/element2").value());
		assertNull(matryoshka.getMetadata("INCOMPLETE"));
	}

}

