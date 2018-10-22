package cat.altimiras.xml.obj;

import cat.altimiras.xml.XMLParser;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class CharsetTest {

	@Test
	public void xmlSimpleUtf16Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/utf16Test.xml"), "UTF-16");
		XMLParser<SimpleTestObj> parser = new WoodStoxObjParserImpl<>(SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml, Charset.forName("UTF-16"));

		assertEquals("111", o.getElement1().trim());
		assertEquals("222", o.getElement2().trim());
	}

	@Test(expected = CharacterCodingException.class)
	public void xmlSimpleUtf16ErrorTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/utf16Test.xml"), "UTF-16");
		XMLParser<SimpleTestObj> parser = new WoodStoxObjParserImpl<>(SimpleTestObj.class, ci);

		parser.parse(xml);

	}
}
