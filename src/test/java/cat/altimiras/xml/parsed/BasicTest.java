package cat.altimiras.xml.parsed;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class BasicTest {

	@Test(expected = NullPointerException.class)
	public void invalidNullStrInputTest() throws Exception {

		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();
		parser.parse((String) null);
	}

	@Test(expected = NullPointerException.class)
	public void invalidNullByteInputTest() throws Exception {

		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();
		byte[] nullArray = null;
		parser.parse(nullArray);
	}

	@Test
	public void simpleXml() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleTest.xml"), "UTF-8");

		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("111", o.get("SimpleTestObj/element1").value());
		assertEquals("222", o.get("SimpleTestObj/element2").value());
	}



	@Test
	public void xmlSimpleInlineTest() throws Exception {


		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleInlineTest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("111", o.get("SimpleTestObj/element1").value());
		assertEquals("222", o.get("SimpleTestObj/element2").value());
	}

	@Test
	public void invalidInputTest() throws Exception {

		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();
		Parsed o = parser.parse("asdfasdfasdfasdf");
		assertNull(o.data);
	}

	@Test
	public void invalidEmptyInputTest() throws Exception {

		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();
		Parsed o = parser.parse("");

		assertNull(o.data);
	}

}
