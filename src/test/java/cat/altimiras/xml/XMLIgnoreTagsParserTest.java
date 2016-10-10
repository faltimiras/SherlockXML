package cat.altimiras.xml;

import cat.altimiras.xml.pojo.Nested3TestObj;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMLIgnoreTagsParserTest {

    @Test
    public void xmlIncompleteTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/ignoreTagsTest.xml"), "UTF-8");
        XMLParser<SimpleTestObj> parser = new XMLParserImpl<>(SimpleTestObj.class);

        SimpleTestObj o = parser.parse(xml);

        assertEquals("111", o.getElement1());
        assertEquals("222", o.getElement2());
    }

}
