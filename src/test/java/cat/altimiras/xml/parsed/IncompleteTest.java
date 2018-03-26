package cat.altimiras.xml.parsed;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IncompleteTest {

	@Test
	public void xmlIncompleteTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/incompleteXMLTest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("111", o.get("Nested3TestObj/simpleTestObj1/element1").value());
		assertTrue(o.isIncomplete());
	}

	@Test
	public void xmlIncomplete2Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/incompleteXML2Test.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("title", o.get("Nested3TestObj/title").value());
		assertEquals("111", o.get("Nested3TestObj/simpleTestObj1/element1").value());
		assertEquals("222", o.get("Nested3TestObj/nestedTestObj/simpleTestObj/element1").value());
		assertTrue(o.isIncomplete());
	}

	@Test
	public void xmlIncompleteListTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/incompleteListXMLTest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);

		assertEquals("111", o.get("ListTestObj/list").asList().get(0).get("SimpleTestObj/element1").value());
		assertTrue(o.isIncomplete());
	}

	@Test
	public void xmlIncompleteList2Test() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/incompleteListXML2Test.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();

		Parsed o = parser.parse(xml);


		assertEquals("lolo", o.get("Nested6TestObj/title").value());
		assertEquals(2, o.get("Nested6TestObj/list").asList().size());
		assertEquals("111", o.get("Nested6TestObj/list").asList().get(0).get("Nested5TestObj/list/").asList().get(0).get("simpleTestObj1/element1").value());
		assertEquals("222", o.get("Nested6TestObj/list").asList().get(0).get("Nested5TestObj/list/").asList().get(1).get("simpleTestObj1/element2").value());
		assertEquals("333", o.get("Nested6TestObj/list").asList().get(0).get("Nested5TestObj/list/").asList().get(2).get("simpleTestObj1/element2").value());
		assertEquals("444", o.get("Nested6TestObj/list").asList().get(0).get("Nested5TestObj/list/").asList().get(3).get("simpleTestObj1/element1").value());
		assertEquals("555", o.get("Nested6TestObj/list").asList().get(0).get("Nested5TestObj/list/").asList().get(3).get("simpleTestObj1/element2").value());
		assertEquals("666", o.get("Nested6TestObj/list").asList().get(1).get("Nested5TestObj/list/").asList().get(0).get("simpleTestObj1/element1").value());
		assertEquals("777", o.get("Nested6TestObj/list").asList().get(1).get("Nested5TestObj/list/").asList().get(1).get("simpleTestObj1/element2").value());
		/*assertEquals("222", o.get("Nested6TestObj/list").asList().get(0).getList().get(1).getElement2().trim());
		assertEquals("333", o.get("Nested6TestObj/list").asList().get(0).getList().get(2).getElement2().trim());
		assertEquals("444", o.get("Nested6TestObj/list").asList().get(0).getList().get(3).getElement1().trim());
		assertEquals("555", o.get("Nested6TestObj/list").asList().get(0).getList().get(3).getElement2().trim());
		assertEquals("666", o.get("Nested6TestObj/list").asList().get(1).getList().get(0).getElement1().trim());
		assertEquals("777", o.get("Nested6TestObj/list").asList().get(1).getList().get(1).getElement2().trim());
		*/
		assertTrue(o.isIncomplete());

	}

}

/*
<pp:Nested6TestObj xmlns:pp="http://www.asdfasdf.com">
    <title>lolo</title>
    <list>
        <pp:Nested5TestObj>
            <po:list xmlns:po="http://www.asdfasdf.com">
                <po:simpleTestObj1 element1="111"/>
                <simpleTestObj1 element2="222"/>
                <po:simpleTestObj1 element2="333"/>
                <simpleTestObj1 element1="444" element2="555"/>
            </po:list>
            <title>title</title>
        </pp:Nested5TestObj>
        <pp:Nested5TestObj>
            <po:list xmlns:po="http://www.aaaasdfasdf.com">
                <po:simpleTestObj1 element1="666"/>
                <simpleTestObj1 element2="777"/>
 */
