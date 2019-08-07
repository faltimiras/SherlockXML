package cat.altimiras.xml.parsed;

import cat.altimiras.Truffle;
import cat.altimiras.xml.XMLParser;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;

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

		XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
		xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/withDTDNoExist.xml"), "UTF-8");
		XMLParser<Truffle> parser = new WoodStoxTruffleParserImpl(xmlInputFactory);

		Truffle o = parser.parse(xml);

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

		XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
		xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, true);


		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/withDTDNoExist.xml"), "UTF-8");
		XMLParser<Truffle> parser = new WoodStoxTruffleParserImpl(xmlInputFactory);

		Truffle o = parser.parse(xml);

		assertNull(o.get("SimpleTestObj/element1").value());
		assertNull(o.get("SimpleTestObj/element2").value());
	}
}
