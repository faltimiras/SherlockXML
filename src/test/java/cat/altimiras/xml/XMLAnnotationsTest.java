package cat.altimiras.xml;


import cat.altimiras.xml.pojo.SimpleAnnotationTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMLAnnotationsTest {

	@Test
	public void xmlAnnotationTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleAnnotationTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleAnnotationTest.xml"), "UTF-8");
		XMLParser<SimpleAnnotationTestObj> parser = new WoodStoxParserImpl<>(SimpleAnnotationTestObj.class, ci);

		SimpleAnnotationTestObj o = parser.parse(xml);

		assertEquals("value", o.getField().trim());
		assertEquals(123, o.getValue());
	}
}
