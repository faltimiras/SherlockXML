package cat.altimiras.xml.obj;


import cat.altimiras.Parser;
import cat.altimiras.xml.pojo.SimpleTestObj;
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

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/CDATATest.xml"), "UTF-8");
		Parser<SimpleTestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("lolo <lo>A\n         </lo> lolo", o.getElement1().trim());
		assertEquals("222", o.getElement2().trim());
	}

}
