package cat.altimiras.xml;


import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMLCDATATest {

	@Test
	public void xmlSimpleCDATATest() throws Exception {
		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/CDATATest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("lolo <br> lolo", o.getElement1());
		assertEquals("222", o.getElement2());
	}

}
