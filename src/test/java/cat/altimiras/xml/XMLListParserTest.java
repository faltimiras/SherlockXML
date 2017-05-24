package cat.altimiras.xml;

import cat.altimiras.xml.pojo.ListTestObj;
import cat.altimiras.xml.pojo.Nested4TestObj;
import cat.altimiras.xml.pojo.Nested5TestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class XMLListParserTest {

	final private int BUFFER_SIZE = 200;

	@Test
	public void xmlListTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listTest.xml"), "UTF-8");
		XMLParser<ListTestObj> parser = new XMLParserImpl<>(ListTestObj.class, ci, BUFFER_SIZE);

		ListTestObj o = parser.parse(xml);

		assertEquals("111", o.getList().get(0).getElement1());
	}

	@Test
	public void xmlList2Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/list2Test.xml"), "UTF-8");
		XMLParser<ListTestObj> parser = new XMLParserImpl<>(ListTestObj.class, ci, BUFFER_SIZE);

		ListTestObj o = parser.parse(xml);

		assertEquals("111", o.getList().get(0).getElement1());
		assertEquals("222", o.getList().get(1).getElement2());
		assertEquals("111", o.getList().get(2).getElement1());
		assertEquals("222", o.getList().get(2).getElement2());
	}

	@Test
	public void xmlListNestedTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested4TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listNestedTest.xml"), "UTF-8");
		XMLParser<Nested4TestObj> parser = new XMLParserImpl<>(Nested4TestObj.class, ci, BUFFER_SIZE);

		Nested4TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle());
		assertEquals("111", o.getSimpleElements().get(0).getElement1());
		assertEquals("222", o.getSimpleElements().get(0).getElement2());
	}

	@Test
	public void xmlListNestedSelfClosedTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested5TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listNestedselfClosedTest.xml"), "UTF-8");
		XMLParser<Nested5TestObj> parser = new XMLParserImpl<>(Nested5TestObj.class, ci, BUFFER_SIZE);

		Nested5TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle());
		assertEquals("111", o.getList().get(0).getElement1());
		assertEquals("222", o.getList().get(1).getElement2());
		assertEquals("333", o.getList().get(2).getElement2());
		assertEquals("444", o.getList().get(3).getElement1());
		assertEquals("555", o.getList().get(3).getElement2());
	}

	@Test
	public void xmlListSelfClosedTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listSelfClosedTest.xml"), "UTF-8");
		XMLParser<ListTestObj> parser = new XMLParserImpl<>(ListTestObj.class, ci, BUFFER_SIZE);

		ListTestObj o = parser.parse(xml);

		assertEquals("111", o.getList().get(0).getElement1());
		assertNull(o.getList().get(0).getElement2());
		assertEquals("222", o.getList().get(1).getElement2());
		assertNull(o.getList().get(1).getElement1());
		assertEquals("333", o.getList().get(2).getElement1());
		assertEquals("444", o.getList().get(2).getElement2());
	}

	@Test
	public void xmlEmptyListTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/emptyListTest.xml"), "UTF-8");
		XMLParser<ListTestObj> parser = new XMLParserImpl<>(ListTestObj.class, ci, BUFFER_SIZE);

		ListTestObj o = parser.parse(xml);

		assertTrue(o.getList().isEmpty());
	}
}
