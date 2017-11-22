package cat.altimiras.xml;

import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharsetTest {

	@Test
	public void xmlSimpleUtf16Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/utf16Test.xml"), "UTF-16");
		XMLParser<SimpleTestObj> parser = new WoodStoxParserImpl<>(SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml, Charset.forName("UTF-16"));

		assertEquals("111", o.getElement1().trim());
		assertEquals("222", o.getElement2().trim());
	}

	@Test(expected = CharacterCodingException.class)
	public void xmlSimpleUtf16ErrorTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/utf16Test.xml"), "UTF-16");
		XMLParser<SimpleTestObj> parser = new WoodStoxParserImpl<>(SimpleTestObj.class, ci);

		parser.parse(xml);

	}

	@Test()
	public void noXml() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		XMLParser<SimpleTestObj> parser = new WoodStoxParserImpl<>(SimpleTestObj.class, ci);

		SimpleTestObj o =parser.parse("asdfasdf");

		assertNull(o.getElement1());
		assertNull(o.getElement2());
	}

}
