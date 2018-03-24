package cat.altimiras.xml.parsed;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AttributesTest {

	@Test
	public void xmlSimpleAttributeTest() throws Exception {


		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleAttributeTest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("111", o.get("/SimpleTestObj/element1").value());
	}

	@Test
	public void xmlAttributesTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/attributesTest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();
		Parsed o = parser.parse(xml);

		assertEquals("11=1",  o.get("/SimpleTestObj/element1").value());
		assertEquals("222",  o.get("/SimpleTestObj/element2").value());
	}

	@Test
	public void xmlAttributes2Test() throws Exception {


		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/attributes2Test.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("111", o.get("Nested3TestObj/simpleTestObj1/element1").value());
		assertEquals("title",o.get("Nested3TestObj/title").value());
		assertEquals("222", o.get("Nested3TestObj/nestedTestObj/simpleTestObj/element1").value());
	}

}


