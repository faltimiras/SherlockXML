package cat.altimiras.xml;

import cat.altimiras.xml.pojo.*;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XMLParserTest {

    @Test(expected = NullPointerException.class)
    public void invalidNullStrInputTest() throws  Exception{
        XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class);
        SimpleTestObj o = parser.parse((String)null);
    }

    @Test(expected = NullPointerException.class)
    public void invalidNullByteInputTest() throws  Exception{
        XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class);
        SimpleTestObj o = parser.parse((byte[])null);
    }

    @Test
    public void xmlSimpleTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleTest.xml"), "UTF-8");
        XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class);

        SimpleTestObj o = parser.parse(xml);

        assertEquals("111", o.getElement1());
        assertEquals("222", o.getElement2());
    }

    @Test
    public void xmlSimpleInlineTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleInlineTest.xml"), "UTF-8");
        XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class);

        SimpleTestObj o = parser.parse(xml);

        assertEquals("111", o.getElement1());
        assertEquals("222", o.getElement2());
    }

    public void invalidInputTest() throws  Exception{
        XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class);
        SimpleTestObj o = parser.parse("asdfasdfasdfasdf");
        assertNull(o);
    }

    @Test
    public void invalidEmptyInputTest() throws  Exception{
        XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class);
        SimpleTestObj o = parser.parse("");

        assertNull(o.getElement1());
        assertNull(o.getElement2());
    }
}
