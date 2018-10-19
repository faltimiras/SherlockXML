package cat.altimiras.xml.parsed;

import cat.altimiras.xml.XMLParser;
import cat.altimiras.xml.obj.ClassIntrospector;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValidationsTest {

	/**
	 * XML contains a dtd declaration to a not existing url. As validation is not enabled, everything works as expected
	 *
	 * @throws Exception
	 */
	@Test
	public void noValidatesDTD() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/withDTDNoExist.xml"), "UTF-8");
		XMLParser<Parsed> parser = new WoodStoxParsedParserImpl(false);

		Parsed o = parser.parse(xml);

		assertEquals("111", o.get("SimpleTestObj/element1").value());
		assertEquals("222", o.get("SimpleTestObj/element2").value());
	}

	/**
	 * XML contains a dtd declaration to a not existing url. As validation is enabled and can not be done, it can not parse it.
	 *
	 * @throws Exception
	 */
	@Test
	public void validatesDTD() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/withDTDNoExist.xml"), "UTF-8");
		XMLParser<Parsed> parser = new WoodStoxParsedParserImpl(true);

		Parsed o = parser.parse(xml);

		assertNull(o.get("SimpleTestObj/element1").value());
		assertNull(o.get("SimpleTestObj/element2").value());
	}
}
