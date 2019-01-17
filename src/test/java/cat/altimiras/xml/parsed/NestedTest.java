package cat.altimiras.xml.parsed;

import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NestedTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test
	public void xmlNestedTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nestedTest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl(xmlInputFactory); 

		Parsed o = parser.parse(xml);

		assertEquals("title", o.get("NestedTestObj/title").value());
		assertEquals("111", o.get("NestedTestObj/simpleTestObj/element1").value());
		assertEquals("222", o.get("/NestedTestObj/simpleTestObj/element2").value());
		assertNull(o.get("/NestedTestObj/simpleTestObj/element3").value());
		assertNull(o.get("/lolo/simpleTestObj/element2").value());
	}

	@Test
	public void xmlNested2Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nested2Test.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl(xmlInputFactory); 

		Parsed o = parser.parse(xml);

		assertEquals("111", o.get("Nested2TestObj/simpleTestObj1/element1").value());
		assertEquals("222", o.get("Nested2TestObj/simpleTestObj2/element2").value());
		assertEquals("title", o.get("/Nested2TestObj/title").value());
	}

	@Test
	public void xmlNested3Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nested3Test.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl(xmlInputFactory); 
		Parsed o = parser.parse(xml);

		assertEquals("111", o.get("Nested3TestObj/simpleTestObj1/element1").value());
		assertEquals("222", o.get("Nested3TestObj/nestedTestObj/simpleTestObj/element1").value());
		assertEquals("title", o.get("/Nested3TestObj/title").value());
	}

	@Test
	public void xmlNestedLoopTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nestedLoopTest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl(xmlInputFactory); 

		Parsed o = parser.parse(xml);

		assertEquals("1", o.get("NestedLoopTestObj/num").value());
		assertEquals("2", o.get("NestedLoopTestObj/nestedLoopTestObj/num").value());
		assertEquals("3", o.get("/NestedLoopTestObj/nestedLoopTestObj/nestedLoopTestObj/num").value());
		assertNull(o.get("/NestedLoopTestObj/nestedLoopTestObj/nestedLoopTestObj/nestedLoopTestObj").value());
	}

}
