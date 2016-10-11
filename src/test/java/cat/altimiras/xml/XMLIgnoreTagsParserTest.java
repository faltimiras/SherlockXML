package cat.altimiras.xml;

import cat.altimiras.xml.pojo.Nested3TestObj;
import cat.altimiras.xml.pojo.Nested4TestObj;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMLIgnoreTagsParserTest {

    @Test
    public void xmlIgnoreTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/ignoreTagsTest.xml"), "UTF-8");
        XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class);

        SimpleTestObj o = parser.parse(xml);

        assertEquals("111", o.getElement1());
        assertEquals("222", o.getElement2());
    }

    @Test
    public void xmlIgnoreSelfClosedTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/ignoreSelfClosedTagTest.xml"), "UTF-8");
        XMLParser<Nested4TestObj> parser = new XMLParserImpl<>(Nested4TestObj.class);

        Nested4TestObj o = parser.parse(xml);

        assertEquals("111", o.getSimpleElements().get(0).getElement1());
        assertEquals("222", o.getSimpleElements().get(0).getElement2());
        assertEquals("title", o.getTitle());
    }

    @Test
    public void xmlIgnoreTagsBeginningTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/ignoreBeginningTest.xml"), "UTF-8");
        XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class);

        SimpleTestObj o = parser.parse(xml);

        assertEquals("111", o.getElement1());
        assertEquals("222", o.getElement2());
    }

}
