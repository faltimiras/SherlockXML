package cat.altimiras.xml.obj;

import cat.altimiras.Parser;
import cat.altimiras.xml.pojo.Nested2TestObj;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SelfClosedTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	@Test
	public void xmlSelfClosedTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested2TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/selfClosedTest.xml"), "UTF-8");
		Parser<Nested2TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested2TestObj.class, ci);

		Nested2TestObj o = parser.parse(xml);

		assertEquals("title", o.getTitle().trim());
		assertEquals("111", o.getSimpleTestObj1().getElement1().trim());
		assertEquals("222", o.getSimpleTestObj2().getElement2().trim());
		assertFalse(o.isIncomplete());
	}
}
