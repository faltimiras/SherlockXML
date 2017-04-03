package cat.altimiras.xml;

import cat.altimiras.xml.pojo.Nested2TestObj;
import cat.altimiras.xml.pojo.Nested3TestObj;
import cat.altimiras.xml.pojo.NestedTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMLNestedParserTest {

	@Test
	public void xmlNestedTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(NestedTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nestedTest.xml"), "UTF-8");
		XMLParser<NestedTestObj> parser = new XMLParserImpl<>(NestedTestObj.class, ci);

		NestedTestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle());
		assertEquals("111", o.getSimpleTestObj().getElement1());
		assertEquals("222", o.getSimpleTestObj().getElement2());
	}

	@Test
	public void xmlNested2Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested2TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nested2Test.xml"), "UTF-8");
		XMLParser<Nested2TestObj> parser = new XMLParserImpl<>(Nested2TestObj.class, ci);

		Nested2TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle());
		assertEquals("111", o.getSimpleTestObj1().getElement1());
		assertEquals("222", o.getSimpleTestObj2().getElement2());
	}

	@Test
	public void xmlNested3Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested3TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nested3Test.xml"), "UTF-8");
		XMLParser<Nested3TestObj> parser = new XMLParserImpl<>(Nested3TestObj.class, ci);

		Nested3TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle());
		assertEquals("111", o.getSimpleTestObj1().getElement1());
		assertEquals("222", o.getNestedTestObj().getSimpleTestObj().getElement1());
	}
}
