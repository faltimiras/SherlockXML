package cat.altimiras.xml;

import cat.altimiras.xml.pojo.*;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XMLListParserTest {

    @Test
    public void xmlListTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listTest.xml"), "UTF-8");
        XMLParser<ListTestObj> parser = new XMLParserImpl<>(ListTestObj.class);

        ListTestObj o = parser.parse(xml);

        assertEquals("111", o.getList().get(0).getElement1());
    }

    @Test
    public void xmlList2Test() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/list2Test.xml"), "UTF-8");
        XMLParser<ListTestObj> parser = new XMLParserImpl<>(ListTestObj.class);

        ListTestObj o = parser.parse(xml);

        assertEquals("111", o.getList().get(0).getElement1());
        assertEquals("222", o.getList().get(1).getElement2());
        assertEquals("111", o.getList().get(2).getElement1());
        assertEquals("222", o.getList().get(2).getElement2());
    }


    @Test
    public void xmlListNestedTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listNestedTest.xml"), "UTF-8");
        XMLParser<Nested4TestObj> parser = new XMLParserImpl<>(Nested4TestObj.class);

        Nested4TestObj o = parser.parse(xml);

        assertEquals("title", o.getTitle());
        assertEquals("111", o.getSimpleElements().get(0).getElement1());
        assertEquals("222", o.getSimpleElements().get(0).getElement2());
    }

    @Test
    public void xmlListNestedSelfClosedTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listNestedselfClosedTest.xml"), "UTF-8");
        XMLParser<Nested5TestObj> parser = new XMLParserImpl<>(Nested5TestObj.class);

        Nested5TestObj o = parser.parse(xml);

        assertEquals("title", o.getTitle());
        assertEquals("111", o.getList().get(0).getElement1());
        assertEquals("222", o.getList().get(1).getElement2());
        assertEquals("333", o.getList().get(2).getElement2());
        assertEquals("444", o.getList().get(3).getElement1());
        assertEquals("555", o.getList().get(3).getElement2());
    }

    @Test
    public void xmlListSelfClosedTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listSelfClosedTest.xml"), "UTF-8");
        XMLParser<ListTestObj> parser = new XMLParserImpl<>(ListTestObj.class);

        ListTestObj o = parser.parse(xml);

        assertEquals("111", o.getList().get(0).getElement1());
        assertNull(o.getList().get(0).getElement2());
        assertEquals("222", o.getList().get(1).getElement2());
        assertNull(o.getList().get(1).getElement1());
        assertEquals("333", o.getList().get(2).getElement1());
        assertEquals("444", o.getList().get(2).getElement2());
    }



}
