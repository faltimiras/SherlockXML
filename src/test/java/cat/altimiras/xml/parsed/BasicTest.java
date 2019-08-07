package cat.altimiras.xml.parsed;

import cat.altimiras.matrioshka.Matrioshka;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BasicTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test(expected = NullPointerException.class)
	public void invalidNullStrInputTest() throws Exception {

		WoodStoxMatrioshkaParserImpl parser = new WoodStoxMatrioshkaParserImpl(xmlInputFactory);
		parser.parse((String) null);
	}

	@Test(expected = NullPointerException.class)
	public void invalidNullByteInputTest() throws Exception {

		WoodStoxMatrioshkaParserImpl parser = new WoodStoxMatrioshkaParserImpl(xmlInputFactory);
		byte[] nullArray = null;
		parser.parse(nullArray);
	}

	@Test
	public void simpleXml() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleTest.xml"), "UTF-8");

		WoodStoxMatrioshkaParserImpl parser = new WoodStoxMatrioshkaParserImpl(xmlInputFactory);

		Matrioshka o = parser.parse(xml);

		assertEquals("111", o.get("SimpleTestObj/element1").value());
		assertEquals("222", o.get("SimpleTestObj/element2").value());
	}


	@Test
	public void xmlSimpleInlineTest() throws Exception {


		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleInlineTest.xml"), "UTF-8");
		WoodStoxMatrioshkaParserImpl parser = new WoodStoxMatrioshkaParserImpl(xmlInputFactory);

		Matrioshka o = parser.parse(xml);

		assertEquals("111", o.get("SimpleTestObj/element1").value());
		assertEquals("222", o.get("SimpleTestObj/element2").value());
	}

	@Test
	public void invalidInputTest() throws Exception {

		WoodStoxMatrioshkaParserImpl parser = new WoodStoxMatrioshkaParserImpl(xmlInputFactory);
		Matrioshka o = parser.parse("asdfasdfasdfasdf");
		assertNull(o.get("").value());
	}

	@Test
	public void invalidEmptyInputTest() throws Exception {

		WoodStoxMatrioshkaParserImpl parser = new WoodStoxMatrioshkaParserImpl(xmlInputFactory);
		Matrioshka o = parser.parse("");

		assertNull(o.get("").value());
	}
}