package cat.altimiras.xml;

import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XMLParserTest {

	final private int BUFFER_SIZE = 200;

	@Test(expected = NullPointerException.class)
	public void invalidNullStrInputTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class, ci, BUFFER_SIZE);
		SimpleTestObj o = parser.parse((String) null);
	}

	@Test(expected = NullPointerException.class)
	public void invalidNullByteInputTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class, ci, BUFFER_SIZE);
		SimpleTestObj o = parser.parse((byte[]) null);
	}

	@Test
	public void xmlSimpleTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleTest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class, ci, BUFFER_SIZE);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("111", o.getElement1());
		assertEquals("222", o.getElement2());
	}

	@Test
	public void xmlSimpleInlineTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleInlineTest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class, ci, BUFFER_SIZE);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("111", o.getElement1());
		assertEquals("222", o.getElement2());
	}

	public void invalidInputTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class, ci, BUFFER_SIZE);
		SimpleTestObj o = parser.parse("asdfasdfasdfasdf");
		assertNull(o);
	}

	@Test
	public void invalidEmptyInputTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class, ci, BUFFER_SIZE);
		SimpleTestObj o = parser.parse("");

		assertNull(o.getElement1());
		assertNull(o.getElement2());
	}
}
