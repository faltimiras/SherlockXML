package cat.altimiras.xml;

import cat.altimiras.xml.pojo.Nested2TestObj;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

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

	@Test(expected = IllegalArgumentException.class)
	public void badBufferParamFactory() throws Exception {
		XMLFactory.getParser(SimpleTestObj.class, 100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void negativeBufferParamFactory() throws Exception {
		XMLFactory.getParser(SimpleTestObj.class, -100);
	}
}
