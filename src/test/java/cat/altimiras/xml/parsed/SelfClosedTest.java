package cat.altimiras.xml.parsed;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelfClosedTest {

	@Test
	public void xmlSelfClosedTest() throws Exception {


		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/selfClosedTest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("title", o.get("/Nested2TestObj/title").value());
		assertEquals("111", o.get("/Nested2TestObj/simpleTestObj1/element1").value());
		assertEquals("222", o.get("/Nested2TestObj/simpleTestObj2/element2").value());
	}
}
