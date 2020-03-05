package cat.altimiras.xml.obj;

import cat.altimiras.Parser;
import cat.altimiras.xml.pojo.SimpleTestObj;
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

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/withDTDNoExist.xml"), "UTF-8");
		Parser<SimpleTestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("111", o.getElement1().trim());
		assertEquals("222", o.getElement2().trim());
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

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/withDTDNoExist.xml"), "UTF-8");
		Parser<SimpleTestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml);

		assertNull(o.getElement1());
		assertNull(o.getElement2());
	}
}
