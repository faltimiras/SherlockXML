package cat.altimiras.xml;


import cat.altimiras.xml.pojo.NestedAnnotationTestObj;
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

	@Test
	public void xmlNestedAnnotationTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(NestedAnnotationTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nestedAnnotationTest.xml"), "UTF-8");
		XMLParser<NestedAnnotationTestObj> parser = new WoodStoxParserImpl<>(NestedAnnotationTestObj.class, ci);

		NestedAnnotationTestObj o = parser.parse(xml);

		assertEquals("value", o.getObject().getField());
		assertEquals(123, o.getObject().getValue());
		assertEquals("aaa", o.getList().get(0).getField());
		assertEquals(111, o.getList().get(0).getValue());
		assertEquals("bbb", o.getList().get(1).getField());
		assertEquals(222, o.getList().get(1).getValue());
	}
}
