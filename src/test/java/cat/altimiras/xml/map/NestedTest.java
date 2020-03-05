package cat.altimiras.xml.map;

import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import java.util.Map;

import static cat.altimiras.xml.XMLFactory.DEFAULT_INCOMPLETE_KEY_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NestedTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test
	public void xmlNestedTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/nestedTest.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("title", matryoshka.get("NestedTestObj/title").value());
		assertEquals("111", matryoshka.get("NestedTestObj/simpleTestObj/element1").value());
		assertEquals("222", matryoshka.get("/NestedTestObj/simpleTestObj/element2").value());
		assertNull(matryoshka.get("/NestedTestObj/simpleTestObj/element3").value());
		assertNull(matryoshka.get("/lolo/simpleTestObj/element2").value());
	}

	@Test
	public void xmlNested2Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/nested2Test.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("111", matryoshka.get("Nested2TestObj/simpleTestObj1/element1").value());
		assertEquals("222", matryoshka.get("Nested2TestObj/simpleTestObj2/element2").value());
		assertEquals("title", matryoshka.get("/Nested2TestObj/title").value());
	}

	@Test
	public void xmlNested3Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/nested3Test.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("111", matryoshka.get("Nested3TestObj/simpleTestObj1/element1").value());
		assertEquals("222", matryoshka.get("Nested3TestObj/nestedTestObj/simpleTestObj/element1").value());
		assertEquals("title", matryoshka.get("/Nested3TestObj/title").value());
	}

	@Test
	public void xmlNestedLoopTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/nestedLoopTest.xml"), "UTF-8");
		WoodStoxMapParserImpl parser = new WoodStoxMapParserImpl(xmlInputFactory, DEFAULT_INCOMPLETE_KEY_NAME);

		Map result = parser.parse(xml);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("1", matryoshka.get("NestedLoopTestObj/num").value());
		assertEquals("2", matryoshka.get("NestedLoopTestObj/nestedLoopTestObj/num").value());
		assertEquals("3", matryoshka.get("/NestedLoopTestObj/nestedLoopTestObj/nestedLoopTestObj/num").value());
		assertNull(matryoshka.get("/NestedLoopTestObj/nestedLoopTestObj/nestedLoopTestObj/nestedLoopTestObj").value());
	}

}
