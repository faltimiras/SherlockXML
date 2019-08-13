package cat.altimiras.xml.matryoshka;


import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;

import static org.junit.Assert.assertEquals;

public class CDATATest {

	@Test
	public void xmlSimpleCDATATest() throws Exception {

		XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);


		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/CDATATest.xml"), "UTF-8");
		WoodStoxMatryoshkaParserImpl parser = new WoodStoxMatryoshkaParserImpl(xmlInputFactory);

		Matryoshka o = parser.parse(xml);

		assertEquals("lolo <lo>A\n         </lo> lolo", o.get("/SimpleTestObj/element1").value());
		assertEquals("222", o.get("/SimpleTestObj/element2").value());
	}

}
