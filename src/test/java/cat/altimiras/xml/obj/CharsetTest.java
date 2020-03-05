package cat.altimiras.xml.obj;

import cat.altimiras.Parser;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class CharsetTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test
	public void xmlSimpleUtf16Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/utf16Test.xml"), "UTF-16");
		Parser<SimpleTestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, SimpleTestObj.class, ci);

		SimpleTestObj o = parser.parse(xml, Charset.forName("UTF-16"));

		assertEquals("111", o.getElement1().trim());
		assertEquals("222", o.getElement2().trim());
	}

	@Test(expected = CharacterCodingException.class)
	public void xmlSimpleUtf16ErrorTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/utf16Test.xml"), "UTF-16");
		Parser<SimpleTestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, SimpleTestObj.class, ci);

		parser.parse(xml);

	}
}
