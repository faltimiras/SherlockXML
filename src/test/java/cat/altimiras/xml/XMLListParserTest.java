package cat.altimiras.xml;

import cat.altimiras.xml.pojo.*;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

}
