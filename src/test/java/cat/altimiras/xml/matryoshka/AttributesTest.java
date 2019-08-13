package cat.altimiras.xml.matryoshka;

import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;

import static org.junit.Assert.assertEquals;

public class AttributesTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test
	public void xmlSimpleAttributeTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/simpleAttributeTest.xml"), "UTF-8");
		WoodStoxMatryoshkaParserImpl parser = new WoodStoxMatryoshkaParserImpl(xmlInputFactory);

		Matryoshka o = parser.parse(xml);

		assertEquals("111", o.get("/SimpleTestObj/element1").value());
	}

	@Test
	public void xmlAttributesTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/attributesTest.xml"), "UTF-8");
		WoodStoxMatryoshkaParserImpl parser = new WoodStoxMatryoshkaParserImpl(xmlInputFactory);
		Matryoshka o = parser.parse(xml);

		assertEquals("11=1", o.get("/SimpleTestObj/element1").value());
		assertEquals("222", o.get("/SimpleTestObj/element2").value());
	}

	@Test
	public void xmlAttributes2Test() throws Exception {


		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/attributes2Test.xml"), "UTF-8");
		WoodStoxMatryoshkaParserImpl parser = new WoodStoxMatryoshkaParserImpl(xmlInputFactory);

		Matryoshka o = parser.parse(xml);

		assertEquals("111", o.get("Nested3TestObj/simpleTestObj1/element1").value());
		assertEquals("title", o.get("Nested3TestObj/title").value());
		assertEquals("222", o.get("Nested3TestObj/nestedTestObj/simpleTestObj/element1").value());
	}

}


