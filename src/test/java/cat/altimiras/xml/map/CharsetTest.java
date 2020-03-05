package cat.altimiras.xml.map;

import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.Map;

import static cat.altimiras.xml.XMLFactory.DEFAULT_INCOMPLETE_KEY_NAME;
import static org.junit.Assert.assertEquals;

public class CharsetTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test
	public void xmlSimpleUtf16Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/utf16Test.xml"), "UTF-16");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml, Charset.forName("UTF-16"));
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("111", matryoshka.get("SimpleTestObj", "element1").value());
		assertEquals("222", matryoshka.get("SimpleTestObj", "element2").value());
	}

	@Test(expected = CharacterCodingException.class)
	public void xmlSimpleUtf16ErrorTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/utf16Test.xml"), "UTF-16");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		parser.parse(xml);

	}
}
