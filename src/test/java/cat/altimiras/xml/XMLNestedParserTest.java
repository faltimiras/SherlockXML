package cat.altimiras.xml;

import cat.altimiras.xml.pojo.Nested2TestObj;
import cat.altimiras.xml.pojo.Nested3TestObj;
import cat.altimiras.xml.pojo.NestedLoopTestObj;
import cat.altimiras.xml.pojo.NestedTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class XMLNestedParserTest {

	@Test
	public void xmlNestedTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(NestedTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nestedTest.xml"), "UTF-8");
		XMLParser<NestedTestObj> parser = new WoodStoxParserImpl<>(NestedTestObj.class, ci);

		NestedTestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle().trim());
		assertEquals("111", o.getSimpleTestObj().getElement1().trim());
		assertEquals("222", o.getSimpleTestObj().getElement2().trim());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlNested2Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested2TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nested2Test.xml"), "UTF-8");
		XMLParser<Nested2TestObj> parser = new WoodStoxParserImpl<>(Nested2TestObj.class, ci);

		Nested2TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle().trim());
		assertEquals("111", o.getSimpleTestObj1().getElement1().trim());
		assertEquals("222", o.getSimpleTestObj2().getElement2().trim());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlNested3Test() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested3TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nested3Test.xml"), "UTF-8");
		XMLParser<Nested3TestObj> parser = new WoodStoxParserImpl<>(Nested3TestObj.class, ci);

		Nested3TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle().trim());
		assertEquals("111", o.getSimpleTestObj1().getElement1().trim());
		assertEquals("222", o.getNestedTestObj().getSimpleTestObj().getElement1().trim());
		assertFalse(o.isIncomplete());
	}

	@Test
	public void xmlNestedLoopTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(NestedLoopTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nestedLoopTest.xml"), "UTF-8");
		XMLParser<NestedLoopTestObj> parser = new WoodStoxParserImpl<>(NestedLoopTestObj.class, ci);

		NestedLoopTestObj o = parser.parse(xml);

		assertEquals(1, o.getNum());
		assertEquals(2, o.getNestedLoopTestObj().getNum());
		assertEquals(3, o.getNestedLoopTestObj().getNestedLoopTestObj().getNum());
		assertNull(o.getNestedLoopTestObj().getNestedLoopTestObj().getNestedLoopTestObj());
		assertFalse(o.isIncomplete());

	}
}
