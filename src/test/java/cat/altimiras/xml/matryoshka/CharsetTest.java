package cat.altimiras.xml.matryoshka;

import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class CharsetTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test
	public void xmlSimpleUtf16Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/utf16Test.xml"), "UTF-16");
		WoodStoxMatryoshkaParserImpl parser = new WoodStoxMatryoshkaParserImpl(xmlInputFactory);

		Matryoshka o = parser.parse(xml, Charset.forName("UTF-16"));

		assertEquals("111", o.get("SimpleTestObj", "element1").value());
		assertEquals("222", o.get("SimpleTestObj", "element2").value());
	}

	@Test(expected = CharacterCodingException.class)
	public void xmlSimpleUtf16ErrorTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/utf16Test.xml"), "UTF-16");
		WoodStoxMatryoshkaParserImpl parser = new WoodStoxMatryoshkaParserImpl(xmlInputFactory);

		parser.parse(xml);

	}
}
