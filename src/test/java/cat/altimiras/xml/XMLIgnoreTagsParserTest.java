package cat.altimiras.xml;

import cat.altimiras.xml.pojo.Nested4TestObj;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMLIgnoreTagsParserTest {

	@Test
	public void xmlIgnoreTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/ignoreTagsTest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new WoodStoxParserImpl<>(SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("111", o.getElement1().trim());
		assertEquals("222", o.getElement2().trim());
	}

	/**
	 * Ignored element is the first of the object
	 *
	 * @throws Exception
	 */
	@Test
	public void xmlIgnore2Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/ignoreTags2Test.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new WoodStoxParserImpl<>(SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("111", o.getElement1().trim());
		assertEquals("222", o.getElement2().trim());
	}

	@Test
	public void xmlIgnoreSelfClosedTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested4TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/ignoreSelfClosedTagTest.xml"), "UTF-8");
		XMLParser<Nested4TestObj> parser = new WoodStoxParserImpl<>(Nested4TestObj.class, ci);

		Nested4TestObj o = parser.parse(xml);

		assertEquals("111", o.getSimpleElements().get(0).getElement1().trim());
		assertEquals("222", o.getSimpleElements().get(0).getElement2().trim());
		assertEquals("title", o.getTitle());
	}

	@Test
	public void xmlIgnoreTagsBeginningTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/ignoreBeginningTest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new WoodStoxParserImpl<>(SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("111", o.getElement1().trim());
		assertEquals("222", o.getElement2().trim());
	}

}
