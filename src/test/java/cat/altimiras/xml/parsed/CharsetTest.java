package cat.altimiras.xml.parsed;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class CharsetTest {

	@Test
	public void xmlSimpleUtf16Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/utf16Test.xml"), "UTF-16");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml, Charset.forName("UTF-16"));

		assertEquals("111", o.get("SimpleTestObj", "element1").value());
		assertEquals("222", o.get("SimpleTestObj", "element2").value());
	}

	@Test(expected = CharacterCodingException.class)
	public void xmlSimpleUtf16ErrorTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/utf16Test.xml"), "UTF-16");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		parser.parse(xml);

	}
}
