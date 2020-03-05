package cat.altimiras.xml.map;

import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import java.util.Map;

import static cat.altimiras.xml.XMLFactory.DEFAULT_INCOMPLETE_KEY_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BasicTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test(expected = NullPointerException.class)
	public void invalidNullStrInputTest() throws Exception {

		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);
		parser.parse((String) null);
	}

	@Test(expected = NullPointerException.class)
	public void invalidNullByteInputTest() throws Exception {

		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);
		byte[] nullArray = null;
		parser.parse(nullArray);
	}

	@Test
	public void simpleXml() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/simpleTest.xml"), "UTF-8");

		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("111", matryoshka.get("SimpleTestObj/element1").value());
		assertEquals("222", matryoshka.get("SimpleTestObj/element2").value());
	}


	@Test
	public void xmlSimpleInlineTest() throws Exception {


		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/simpleInlineTest.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("111", matryoshka.get("SimpleTestObj/element1").value());
		assertEquals("222", matryoshka.get("SimpleTestObj/element2").value());
	}

	@Test
	public void invalidInputTest() throws Exception {

		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);
		Map result = parser.parse("asdfasdfasdfasdf");
		assertTrue(result.isEmpty());
	}

	@Test
	public void invalidEmptyInputTest() throws Exception {

		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);
		Map result = parser.parse("");

		assertTrue(result.isEmpty());
	}
}