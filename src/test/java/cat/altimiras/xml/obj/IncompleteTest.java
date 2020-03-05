package cat.altimiras.xml.obj;

import cat.altimiras.Parser;
import cat.altimiras.xml.pojo.ListTestObj;
import cat.altimiras.xml.pojo.Nested3TestObj;
import cat.altimiras.xml.pojo.Nested6TestObj;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IncompleteTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test
	public void xmlIncompleteTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested3TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/incompleteXMLTest.xml"), "UTF-8");
		Parser<Nested3TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested3TestObj.class, ci);

		Nested3TestObj o = parser.parse(xml);

		assertEquals("111", o.getSimpleTestObj1().getElement1().trim());
	}

	@Test
	public void xmlIncomplete2Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested3TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/incompleteXML2Test.xml"), "UTF-8");
		Parser<Nested3TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested3TestObj.class, ci);

		Nested3TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle());
		assertEquals("111", o.getSimpleTestObj1().getElement1().trim());
		assertEquals("222", o.getNestedTestObj().getSimpleTestObj().getElement1().trim());
	}

	@Test
	public void xmlIncompleteListTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(ListTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/incompleteListXMLTest.xml"), "UTF-8");
		Parser<ListTestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, ListTestObj.class, ci);

		ListTestObj o = parser.parse(xml);

		assertEquals("111", o.getList().get(0).getElement1().trim());
		assertTrue(o.isIncomplete());
	}

	@Test
	public void xmlIncompleteList2Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested6TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/incompleteListXML2Test.xml"), "UTF-8");
		Parser<Nested6TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested6TestObj.class, ci);

		Nested6TestObj o = parser.parse(xml);

		assertEquals("lolo", o.getTitle().trim());
		assertEquals(2, o.getList().size());
		assertEquals("title", o.getList().get(0).getTitle().trim());
		assertEquals("111", o.getList().get(0).getList().get(0).getElement1().trim());
		assertEquals("222", o.getList().get(0).getList().get(1).getElement2().trim());
		assertEquals("333", o.getList().get(0).getList().get(2).getElement2().trim());
		assertEquals("444", o.getList().get(0).getList().get(3).getElement1().trim());
		assertEquals("555", o.getList().get(0).getList().get(3).getElement2().trim());
		assertEquals("666", o.getList().get(1).getList().get(0).getElement1().trim());
		assertEquals("777", o.getList().get(1).getList().get(1).getElement2().trim());
		assertTrue(o.isIncomplete());
	}


}
