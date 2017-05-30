package cat.altimiras.xml;


import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMLCDATATest {

	@Test
	public void xmlSimpleCDATATest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/CDATATest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new WoodStoxParserImpl<>(SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("lolo <lo>A\n         </lo> lolo", o.getElement1().trim());
		assertEquals("222", o.getElement2().trim());
	}

}
