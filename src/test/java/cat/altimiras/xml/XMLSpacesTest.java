package cat.altimiras.xml;

import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMLSpacesTest {

	final private int BUFFER_SIZE = 200;

	@Test
	public void trimStartTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/spacesStartTest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class, ci, BUFFER_SIZE);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("222", o.getElement2());
	}

	@Test
	public void trimEndTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/spacesEndTest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class, ci, BUFFER_SIZE);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("222", o.getElement2());
	}

	@Test
	public void trimMiddleTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/spacesMiddleTest.xml"), "UTF-8");
		XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class, ci, BUFFER_SIZE);

		SimpleTestObj o = parser.parse(xml);

		assertEquals("222", o.getElement2());
	}
}
