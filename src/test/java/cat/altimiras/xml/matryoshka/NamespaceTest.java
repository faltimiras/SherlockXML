package cat.altimiras.xml.matryoshka;

import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NamespaceTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test
	public void xmlNamespaceSimpleTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/simpleNamespaceTest.xml"), "UTF-8");
		WoodStoxMatryoshkaParserImpl parser = new WoodStoxMatryoshkaParserImpl(xmlInputFactory);

		Matryoshka o = parser.parse(xml);

		assertEquals("111", o.get("SimpleTestObj/element1").value());
		assertEquals("222", o.get("SimpleTestObj/element2").value());
	}

	@Test
	public void xmlNamespaceAttributesTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/attributesNamespaceTest.xml"), "UTF-8");
		WoodStoxMatryoshkaParserImpl parser = new WoodStoxMatryoshkaParserImpl(xmlInputFactory);

		Matryoshka o = parser.parse(xml);

		assertEquals("title", o.get("Nested3TestObj/title").value());
		assertEquals("111", o.get("Nested3TestObj/simpleTestObj1/element1").value());
		assertEquals("222", o.get("Nested3TestObj/nestedTestObj/simpleTestObj/element1").value());
	}

	@Test
	public void xmlNamespaceListSelfClosedTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/listNestedselfClosedNamespaceTest.xml"), "UTF-8");
		WoodStoxMatryoshkaParserImpl parser = new WoodStoxMatryoshkaParserImpl(xmlInputFactory);

		Matryoshka o = parser.parse(xml);

		assertEquals("111", o.get("Nested5TestObj/list").asList().get(0).get("simpleTestObj1/element1").value());
		assertEquals("222", o.get("Nested5TestObj/list").asList().get(1).get("simpleTestObj1/element2").value());
		assertEquals("333", o.get("Nested5TestObj/list").asList().get(2).get("simpleTestObj1/element2").value());
		assertEquals("444", o.get("Nested5TestObj/list").asList().get(3).get("simpleTestObj1/element1").value());
		assertEquals("555", o.get("Nested5TestObj/list").asList().get(3).get("simpleTestObj1/element2").value());
		assertEquals("title", o.get("Nested5TestObj/title").value());
	}

	@Test
	public void xmlNamespaComplexTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/namespacesComplexTest.xml"), "UTF-8");
		WoodStoxMatryoshkaParserImpl parser = new WoodStoxMatryoshkaParserImpl(xmlInputFactory);

		Matryoshka o = parser.parse(xml);

		assertEquals("111", o.get("Envelope/Body/Nested5TestObj/list").asList().get(0).get("simpleTestObj1/element1").value());
		assertEquals("222", o.get("Envelope/Body/Nested5TestObj/list").asList().get(1).get("simpleTestObj1/element2").value());
		assertEquals("333", o.get("Envelope/Body/Nested5TestObj/list").asList().get(2).get("simpleTestObj1/element2").value());
		assertEquals("444", o.get("Envelope/Body/Nested5TestObj/list").asList().get(3).get("simpleTestObj1/element1").value());
		assertEquals("555", o.get("Envelope/Body/Nested5TestObj/list").asList().get(3).get("simpleTestObj1/element2").value());
		assertEquals("title", o.get("Envelope/Body/Nested5TestObj/title").value());
		assertTrue((Boolean) o.getMetadata("INCOMPLETE"));
	}
}