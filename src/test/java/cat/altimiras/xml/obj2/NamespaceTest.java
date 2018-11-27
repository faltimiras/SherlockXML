package cat.altimiras.xml.obj2;

import cat.altimiras.xml.XMLParser;
import cat.altimiras.xml.obj.ClassIntrospector;
import cat.altimiras.xml.obj.WoodStoxObjParserImpl2;
import cat.altimiras.xml.pojo.Nested3TestObj;
import cat.altimiras.xml.pojo.Nested5TestObj;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NamespaceTest {

	@Test
	public void xmlNamespaceSimpleTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleNamespaceTest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new WoodStoxObjParserImpl2<>(SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("111", o.getElement1().trim());
		assertEquals("222", o.getElement2().trim());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlNamespaceAttributesTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested3TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/attributesNamespaceTest.xml"), "UTF-8");
		XMLParser<Nested3TestObj> parser = new WoodStoxObjParserImpl2<>(Nested3TestObj.class, ci);

		Nested3TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle().trim());
		assertEquals("111", o.getSimpleTestObj1().getElement1().trim());
		assertEquals("222", o.getNestedTestObj().getSimpleTestObj().getElement1().trim());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlNamespaceListSelfClosedTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested5TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listNestedselfClosedNamespaceTest.xml"), "UTF-8");
		XMLParser<Nested5TestObj> parser = new WoodStoxObjParserImpl2<>(Nested5TestObj.class, ci);

		Nested5TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle().trim());
		assertEquals("111", o.getList().get(0).getElement1().trim());
		assertEquals("222", o.getList().get(1).getElement2().trim());
		assertEquals("333", o.getList().get(2).getElement2().trim());
		assertEquals("444", o.getList().get(3).getElement1().trim());
		assertEquals("555", o.getList().get(3).getElement2().trim());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlNamespaComplexTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested5TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/namespacesComplexTest.xml"), "UTF-8");
		XMLParser<Nested5TestObj> parser = new WoodStoxObjParserImpl2<>(Nested5TestObj.class, ci);

		Nested5TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle().trim());
		assertEquals("111", o.getList().get(0).getElement1().trim());
		assertEquals("222", o.getList().get(1).getElement2().trim());
		assertEquals("333", o.getList().get(2).getElement2().trim());
		assertEquals("444", o.getList().get(3).getElement1().trim());
		assertEquals("555", o.getList().get(3).getElement2().trim());
		assertTrue(o.isIncomplete());
	}

}
