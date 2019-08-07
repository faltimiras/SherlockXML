package cat.altimiras.xml.parsed;


import cat.altimiras.Truffle;
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


		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/CDATATest.xml"), "UTF-8");
		WoodStoxTruffleParserImpl parser = new WoodStoxTruffleParserImpl(xmlInputFactory);

		Truffle o = parser.parse(xml);

		assertEquals("lolo <lo>A\n         </lo> lolo", o.get("/SimpleTestObj/element1").value());
		assertEquals("222", o.get("/SimpleTestObj/element2").value());
	}

}
