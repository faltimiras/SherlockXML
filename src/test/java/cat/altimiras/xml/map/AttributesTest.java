package cat.altimiras.xml.map;

import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import java.util.Map;

import static cat.altimiras.xml.XMLFactory.DEFAULT_INCOMPLETE_KEY_NAME;
import static org.junit.Assert.assertEquals;

public class AttributesTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test
	public void xmlSimpleAttributeTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/simpleAttributeTest.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("111", matryoshka.get("/SimpleTestObj/element1").value());
	}

	@Test
	public void xmlAttributesTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/attributesTest.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("11=1", matryoshka.get("/SimpleTestObj/element1").value());
		assertEquals("222", matryoshka.get("/SimpleTestObj/element2").value());
	}

	@Test
	public void xmlAttributes2Test() throws Exception {


		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/attributes2Test.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("111", matryoshka.get("Nested3TestObj/simpleTestObj1/element1").value());
		assertEquals("title", matryoshka.get("Nested3TestObj/title").value());
		assertEquals("222", matryoshka.get("Nested3TestObj/nestedTestObj/simpleTestObj/element1").value());
	}

}


