package cat.altimiras.xml;

import cat.altimiras.xml.pojo.Nested2TestObj;
import cat.altimiras.xml.pojo.Nested3TestObj;
import cat.altimiras.xml.pojo.Nested5TestObj;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMLNamespaceParserTest {


    @Test
    public void xmlNamespaceSimpleTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleNamespaceTest.xml"), "UTF-8");
        XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class);

        SimpleTestObj o = parser.parse(xml);

        assertEquals("111", o.getElement1());
        assertEquals("222", o.getElement2());
    }

    @Test
    public void xmlNamespaceAttributesTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/attributesNamespaceTest.xml"), "UTF-8");
        XMLParser<Nested3TestObj> parser = new XMLParserImpl<>(Nested3TestObj.class);

        Nested3TestObj o = parser.parse(xml);

        assertEquals("title", o.getTitle());
        assertEquals("111", o.getSimpleTestObj1().getElement1());
        assertEquals("222", o.getNestedTestObj().getSimpleTestObj().getElement1());
    }

    @Test
    public void xmlNamespaceListSelfClosedTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/listNestedselfClosedNamespaceTest.xml"), "UTF-8");
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
    public void xmlNamespaComplexTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/namespacesComplexTest.xml"), "UTF-8");
        XMLParser<Nested5TestObj> parser = new XMLParserImpl<>(Nested5TestObj.class);

        Nested5TestObj o = parser.parse(xml);

        assertEquals("title", o.getTitle());
        assertEquals("111", o.getList().get(0).getElement1());
        assertEquals("222", o.getList().get(1).getElement2());
        assertEquals("333", o.getList().get(2).getElement2());
        assertEquals("444", o.getList().get(3).getElement1());
        assertEquals("555", o.getList().get(3).getElement2());
    }

}
