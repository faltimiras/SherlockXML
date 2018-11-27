package cat.altimiras.xml.obj2;


import cat.altimiras.xml.XMLParser;
import cat.altimiras.xml.obj.ClassIntrospector;
import cat.altimiras.xml.obj.WoodStoxObjParserImpl2;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CDATATest {

	@Test
	public void xmlSimpleCDATATest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/CDATATest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new WoodStoxObjParserImpl2<>(SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("lolo <lo>A\n         </lo> lolo", o.getElement1().trim());
		assertEquals("222", o.getElement2().trim());
	}

}
