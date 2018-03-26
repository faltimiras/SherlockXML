package cat.altimiras.xml.obj;


import cat.altimiras.xml.XMLParser;
import cat.altimiras.xml.pojo.NestedAnnotationTestObj;
import cat.altimiras.xml.pojo.SimpleAnnotationTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AnnotationsTest {

	@Test
	public void xmlAnnotationTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleAnnotationTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleAnnotationTest.xml"), "UTF-8");
		XMLParser<SimpleAnnotationTestObj> parser = new WoodStoxObjParserImpl<>(SimpleAnnotationTestObj.class, ci);

		SimpleAnnotationTestObj o = parser.parse(xml);

		assertEquals("value", o.getField().trim());
		assertEquals(123, o.getValue());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlNestedAnnotationTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(NestedAnnotationTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nestedAnnotationTest.xml"), "UTF-8");
		XMLParser<NestedAnnotationTestObj> parser = new WoodStoxObjParserImpl<>(NestedAnnotationTestObj.class, ci);

		NestedAnnotationTestObj o = parser.parse(xml);

		assertEquals("value", o.getObject().getField());
		assertEquals(123, o.getObject().getValue());
		assertEquals("aaa", o.getList().get(0).getField());
		assertEquals(111, o.getList().get(0).getValue());
		assertEquals("bbb", o.getList().get(1).getField());
		assertEquals(222, o.getList().get(1).getValue());
		assertFalse(o.isIncomplete());
	}
}
