package cat.altimiras.xml.map;

import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import java.util.Map;

import static cat.altimiras.xml.XMLFactory.DEFAULT_INCOMPLETE_KEY_NAME;
import static org.junit.Assert.assertEquals;

public class SelfClosedTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test
	public void xmlSelfClosedTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/selfClosedTest.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("title", matryoshka.get("/Nested2TestObj/title").value());
		assertEquals("111", matryoshka.get("/Nested2TestObj/simpleTestObj1/element1").value());
		assertEquals("222", matryoshka.get("/Nested2TestObj/simpleTestObj2/element2").value());
	}
}