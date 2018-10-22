package cat.altimiras.xml;

import cat.altimiras.xml.obj.WoodStoxObjParserImpl;
import cat.altimiras.xml.parsed.WoodStoxParsedParserImpl;
import cat.altimiras.xml.pojo.Nested2TestObj;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class XMLFactoryTest {

	@Before
	public void setUp() {
		XMLFactory.reset();
	}

	@Test(expected = IllegalArgumentException.class)
	public void notInitFactory() throws Exception {
		XMLFactory.getParser(SimpleTestObj.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void notProperInitFactory() throws Exception {
		XMLFactory.init(Nested2TestObj.class);
		XMLFactory.getParser(SimpleTestObj.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyInitFactory() throws Exception {
		XMLFactory.init();
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullInitFactory() throws Exception {
		XMLFactory.init(null);
	}

	@Test
	public void initFactory() throws Exception {
		XMLFactory.init(SimpleTestObj.class);
		XMLParser p = XMLFactory.getParser(SimpleTestObj.class);

		assertNotNull(p);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullParamFactory() throws Exception {
		XMLFactory.init(SimpleTestObj.class);
		XMLFactory.getParser(null);
	}

	@Test
	public void objParseractory() throws Exception {
		XMLFactory.init(SimpleTestObj.class);
		XMLParser parser = XMLFactory.getParser(SimpleTestObj.class);
		assertTrue(parser instanceof WoodStoxObjParserImpl);
	}

	@Test
	public void parsedParseractory() throws Exception {
		XMLParser parser = XMLFactory.getParser();
		assertTrue(parser instanceof WoodStoxParsedParserImpl);
	}

}
