package cat.altimiras.xml;

import cat.altimiras.xml.pojo.SimpleTestObj;
import cat.altimiras.xml.pojo.TypeTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XMLTypesTest {


    @Test
    public void xmlTypeTest() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/typeTest.xml"), "UTF-8");
        XMLParser<TypeTestObj> parser = new XMLParserImpl<>(TypeTestObj.class);

        TypeTestObj o = parser.parse(xml);

        assertEquals("text", o.getText());
        assertEquals(new Integer(1), o.getIntegerNum());
        assertEquals(new Double(1.1), o.getDoubleNum());
        assertEquals(new Float(1.2), o.getFloatNum());
        assertEquals(new Long(2), o.getLongNum());

    }


}
