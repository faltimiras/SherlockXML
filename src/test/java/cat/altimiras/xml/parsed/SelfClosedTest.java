package cat.altimiras.xml.parsed;

import cat.altimiras.Truffle;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;

import static org.junit.Assert.assertEquals;

public class SelfClosedTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test
	public void xmlSelfClosedTest() throws Exception {


		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/selfClosedTest.xml"), "UTF-8");
		WoodStoxTruffleParserImpl parser = new WoodStoxTruffleParserImpl(xmlInputFactory);

		Truffle o = parser.parse(xml);

		assertEquals("title", o.get("/Nested2TestObj/title").value());
		assertEquals("111", o.get("/Nested2TestObj/simpleTestObj1/element1").value());
		assertEquals("222", o.get("/Nested2TestObj/simpleTestObj2/element2").value());
	}
}
