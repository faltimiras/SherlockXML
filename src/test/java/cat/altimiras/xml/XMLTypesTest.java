package cat.altimiras.xml;

import cat.altimiras.xml.pojo.TypeTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMLTypesTest {


	@Test
	public void xmlTypeTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(TypeTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/typeTest.xml"), "UTF-8");
		XMLParser<TypeTestObj> parser = new XMLParserImpl<>(TypeTestObj.class, ci);

		TypeTestObj o = parser.parse(xml);

		assertEquals("text", o.getText());
		assertEquals(new Integer(1), o.getIntegerNum());
		assertEquals(new Double(1.1), o.getDoubleNum());
		assertEquals(new Float(1.2), o.getFloatNum());
		assertEquals(new Long(2), o.getLongNum());
		assertEquals(true, o.getBooleanValue());

		assertEquals(1, o.getIntPrimitiveNum());
		assertEquals(1.1d, o.getDoublePrimitiveNum(), 0.0d);
		assertEquals(1.2f, o.getFloatPrimitiveNum(), 0.0f);
		assertEquals(2l, o.getLongPrimitiveNum());
		assertEquals(true, o.isBooleanPrimitiveValue());
	}


}
