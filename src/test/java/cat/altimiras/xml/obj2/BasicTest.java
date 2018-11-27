package cat.altimiras.xml.obj2;

import cat.altimiras.xml.XMLParser;
import cat.altimiras.xml.obj.ClassIntrospector;
import cat.altimiras.xml.obj.WoodStoxObjParserImpl2;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class BasicTest {

	@Test(expected = NullPointerException.class)
	public void invalidNullStrInputTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		XMLParser<SimpleTestObj> parser = new WoodStoxObjParserImpl2<>(SimpleTestObj.class, ci);
		SimpleTestObj o = parser.parse((String) null);
	}

	@Test(expected = NullPointerException.class)
	public void invalidNullByteInputTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		XMLParser<SimpleTestObj> parser = new WoodStoxObjParserImpl2<>(SimpleTestObj.class, ci);
		byte[] nullArray = null;
		parser.parse(nullArray);
	}

	@Test
	public void xmlSimpleTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleTest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new WoodStoxObjParserImpl2<>(SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("111", o.getElement1().trim());
		assertEquals("222", o.getElement2().trim());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void noState() throws Exception {
		for(int i = 0 ; i < 5; i++ ){
			xmlSimpleTest();
		}
	}

	@Test
	public void xmlSimpleInlineTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleInlineTest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new WoodStoxObjParserImpl2<>(SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("111", o.getElement1().trim());
		assertEquals("222", o.getElement2().trim());
	}

	@Test
	public void invalidInputTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		XMLParser<SimpleTestObj> parser = new WoodStoxObjParserImpl2<>(SimpleTestObj.class, ci);
		SimpleTestObj o = parser.parse("asdfasdfasdfasdf");
		assertNull(o.getElement1());
	}

	@Test
	public void invalidEmptyInputTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		XMLParser<SimpleTestObj> parser = new WoodStoxObjParserImpl2<>(SimpleTestObj.class, ci);
		SimpleTestObj o = parser.parse("");

		assertNull(o.getElement1());
		assertNull(o.getElement2());
	}

}
