package cat.altimiras.xml.map;


import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import java.util.Map;

import static cat.altimiras.xml.XMLFactory.DEFAULT_INCOMPLETE_KEY_NAME;
import static org.junit.Assert.assertEquals;

public class CDATATest {

	@Test
	public void xmlSimpleCDATATest() throws Exception {

		XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/CDATATest.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("lolo <lo>A\n         </lo> lolo", matryoshka.get("/SimpleTestObj/element1").value());
		assertEquals("222", matryoshka.get("/SimpleTestObj/element2").value());
	}
}