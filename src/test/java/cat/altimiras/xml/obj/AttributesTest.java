package cat.altimiras.xml.obj;

import cat.altimiras.xml.XMLParser;
import cat.altimiras.xml.pojo.Nested3TestObj;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AttributesTest {

	@Test
	public void xmlSimpleAttributeTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleAttributeTest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new WoodStoxObjParserImpl<>(SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("111", o.getElement1());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlAttributesTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/attributesTest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new WoodStoxObjParserImpl<>(SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("11=1", o.getElement1());
		assertEquals("222", o.getElement2());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlAttributes2Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested3TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/attributes2Test.xml"), "UTF-8");
		XMLParser<Nested3TestObj> parser = new WoodStoxObjParserImpl<>(Nested3TestObj.class, ci);

		Nested3TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle().trim());
		assertEquals("111", o.getSimpleTestObj1().getElement1().trim());
		assertEquals("222", o.getNestedTestObj().getSimpleTestObj().getElement1().trim());
		assertFalse(o.isIncomplete());
	}

}
