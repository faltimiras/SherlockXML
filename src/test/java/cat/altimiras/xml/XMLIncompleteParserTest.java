package cat.altimiras.xml;

import cat.altimiras.xml.pojo.Nested3TestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMLIncompleteParserTest {

    @Test
    public void xmlIncompleteTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/incompleteXMLTest.xml"), "UTF-8");
        XMLParser<Nested3TestObj> parser = new XMLParserImpl<>(Nested3TestObj.class);

        Nested3TestObj o = parser.parse(xml);

        assertEquals("111", o.getSimpleTestObj1().getElement1());
    }

    @Test
    public void xmlIncomplete2Test() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/incompleteXML2Test.xml"), "UTF-8");
        XMLParser<Nested3TestObj> parser = new XMLParserImpl<>(Nested3TestObj.class);

        Nested3TestObj o = parser.parse(xml);

        assertEquals("title", o.getTitle());
        assertEquals("111", o.getSimpleTestObj1().getElement1());
        assertEquals("222", o.getNestedTestObj().getSimpleTestObj().getElement1());
    }
}
