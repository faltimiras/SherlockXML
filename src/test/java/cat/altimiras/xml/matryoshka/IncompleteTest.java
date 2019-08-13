package cat.altimiras.xml.matryoshka;

import cat.altimiras.matryoshka.Matryoshka;
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

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/incompleteXMLTest.xml"), "UTF-8");
		WoodStoxMatryoshkaParserImpl parser = new WoodStoxMatryoshkaParserImpl(xmlInputFactory);

		Matryoshka o = parser.parse(xml);

		assertEquals("111", o.get("Nested3TestObj/simpleTestObj1/element1").value());
		assertTrue((Boolean) o.getMetadata("INCOMPLETE"));
	}

	@Test
	public void xmlIncomplete2Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/incompleteXML2Test.xml"), "UTF-8");
		WoodStoxMatryoshkaParserImpl parser = new WoodStoxMatryoshkaParserImpl(xmlInputFactory);

		Matryoshka o = parser.parse(xml);

		assertEquals("title", o.get("Nested3TestObj/title").value());
		assertEquals("111", o.get("Nested3TestObj/simpleTestObj1/element1").value());
		assertEquals("222", o.get("Nested3TestObj/nestedTestObj/simpleTestObj/element1").value());
		assertTrue((Boolean) o.getMetadata("INCOMPLETE"));
	}

	@Test
	public void xmlIncompleteListTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/incompleteListXMLTest.xml"), "UTF-8");
		WoodStoxMatryoshkaParserImpl parser = new WoodStoxMatryoshkaParserImpl(xmlInputFactory);

		Matryoshka o = parser.parse(xml);

		assertEquals("111", o.get("ListTestObj/list").asList().get(0).get("SimpleTestObj/element1").value());
		assertTrue((Boolean) o.getMetadata("INCOMPLETE"));
	}

	@Test
	public void xmlIncompleteList2Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/incompleteListXML2Test.xml"), "UTF-8");
		WoodStoxMatryoshkaParserImpl parser = new WoodStoxMatryoshkaParserImpl(xmlInputFactory);

		Matryoshka o = parser.parse(xml);

		assertEquals("lolo", o.get("Nested6TestObj/title").value());
		assertEquals(2, o.get("Nested6TestObj/list").asList().size());
		assertEquals("111", o.get("Nested6TestObj/list").asList().get(0).get("Nested5TestObj/list/").asList().get(0).get("simpleTestObj1/element1").value());
		assertEquals("222", o.get("Nested6TestObj/list").asList().get(0).get("Nested5TestObj/list/").asList().get(1).get("simpleTestObj1/element2").value());
		assertEquals("333", o.get("Nested6TestObj/list").asList().get(0).get("Nested5TestObj/list/").asList().get(2).get("simpleTestObj1/element2").value());
		assertEquals("444", o.get("Nested6TestObj/list").asList().get(0).get("Nested5TestObj/list/").asList().get(3).get("simpleTestObj1/element1").value());
		assertEquals("555", o.get("Nested6TestObj/list").asList().get(0).get("Nested5TestObj/list/").asList().get(3).get("simpleTestObj1/element2").value());
		assertEquals("666", o.get("Nested6TestObj/list").asList().get(1).get("Nested5TestObj/list/").asList().get(0).get("simpleTestObj1/element1").value());
		assertEquals("777", o.get("Nested6TestObj/list").asList().get(1).get("Nested5TestObj/list/").asList().get(1).get("simpleTestObj1/element2").value());
		assertTrue((Boolean) o.getMetadata("INCOMPLETE"));
	}
}