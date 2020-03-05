package cat.altimiras.xml.map;

import cat.altimiras.Parser;
import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import java.util.Map;

import static cat.altimiras.xml.XMLFactory.DEFAULT_INCOMPLETE_KEY_NAME;
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

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/withDTDNoExist.xml"), "UTF-8");
		Parser<Map> parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("111", matryoshka.get("SimpleTestObj/element1").value());
		assertEquals("222", matryoshka.get("SimpleTestObj/element2").value());
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


		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/withDTDNoExist.xml"), "UTF-8");
		Parser<Map> parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertNull(matryoshka.get("SimpleTestObj/element1").value());
		assertNull(matryoshka.get("SimpleTestObj/element2").value());
	}
}
