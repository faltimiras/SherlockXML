package cat.altimiras.xml.parsed;


import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CDATATest {

	@Test
	public void xmlSimpleCDATATest() throws Exception {


		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/CDATATest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("lolo <lo>A\n         </lo> lolo", o.get("/SimpleTestObj/element1").value());
		assertEquals("222", o.get("/SimpleTestObj/element2").value());
	}

}
