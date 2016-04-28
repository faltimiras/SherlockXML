package cat.altimiras.xml;

import cat.altimiras.xml.pojo.*;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMAttributesParserTest {

    //@Test
    public void xmlSimpleAttributeTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleAttributeTest.xml"), "UTF-8");
        XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class);

        SimpleTestObj o = parser.parse(xml);

        assertEquals("111", o.getElement1());
    }

    //@Test
    public void xmlAttributesTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/attributesTest.xml"), "UTF-8");
        XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class);

        SimpleTestObj o = parser.parse(xml);

        assertEquals("111", o.getElement1());
        assertEquals("222", o.getElement2());
    }

    @Test
    public void xmlAttributes2Test() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/attributes2Test.xml"), "UTF-8");
        XMLParser<Nested3TestObj> parser = new XMLParserImpl<>(Nested3TestObj.class);

        Nested3TestObj o = parser.parse(xml);

        assertEquals("title", o.getTitle());
        assertEquals("111", o.getSimpleTestObj1().getElement1());
        assertEquals("222", o.getNestedTestObj().getSimpleTestObj().getElement1());
    }

}
