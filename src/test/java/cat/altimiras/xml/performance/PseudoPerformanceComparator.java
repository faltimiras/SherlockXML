package cat.altimiras.xml.performance;


import cat.altimiras.xml.XMLParser;
import cat.altimiras.xml.obj.ClassIntrospector;
import cat.altimiras.xml.obj.ContextFactory;
import cat.altimiras.xml.obj.WoodStoxObjParserImpl;
import cat.altimiras.xml.obj.WoodStoxObjParserImpl2;
import cat.altimiras.xml.obj.WoodStoxObjParserImpl3;
import cat.altimiras.xml.parsed.Parsed;
import cat.altimiras.xml.parsed.WoodStoxParsedParserImpl;
import cat.altimiras.xml.pojo.ListTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Ignore
public class PseudoPerformanceComparator {

	private static final int LOOPS = 1000;

	//THIS IS NOT A REAL PERFORMANCE TEST!!
	@Test
	public void parseBigList() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/performance/bigListTest.xml"), "UTF-8");

		ClassIntrospector c = new ClassIntrospector(ListTestObj.class);

		long ini = System.currentTimeMillis();
		XMLParser<ListTestObj> parser = new WoodStoxObjParserImpl<>(ListTestObj.class, c);
		for (int i = 0; i < LOOPS; i++) {
			ListTestObj o = parser.parse(xml);

			assertEquals(2394, o.getList().size());
		}
		long end = System.currentTimeMillis();

		System.out.println("Diff:" + (end - ini));

	}

	@Test
	public void tenTimes() throws Exception {

		//Thread.sleep(20000);

		for (int i = 0; i < 10; i++) {
			parseBigList();
		}

	}

	public void parseParsedBigList() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/performance/bigListTest.xml"), "UTF-8");

		long ini = System.currentTimeMillis();
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl();
		for (int i = 0; i < LOOPS; i++) {
			Parsed o = parser.parse(xml);

			//assertEquals(2394, o.getList().size());

		}
		long end = System.currentTimeMillis();

		System.out.println("Diff:" + (end - ini));

	}

	//THIS IS NOT A REAL PERFORMANCE TEST!!
	//@Test
	public void parseBigListIgnore() throws Exception {

		byte[] xml = IOUtils.toString(this.getClass().getResourceAsStream("/performance/bigListIgnoreTest.xml"), "UTF-8").getBytes();

		ClassIntrospector c = new ClassIntrospector(ListTestObj.class);

		long ini = System.currentTimeMillis();
		XMLParser<ListTestObj> parser = new WoodStoxObjParserImpl<>(ListTestObj.class, c);
		for (int i = 0; i < LOOPS; i++) {
			ListTestObj o = parser.parse(xml);

			assertEquals(4896, o.getList().size());
		}
		long end = System.currentTimeMillis();

		System.out.println("Diff:" + (end - ini));
	}

	//THIS IS NOT A REAL PERFORMANCE TEST!!
	@Test
	public void parseBigListAttIgnore() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/performance/bigListAttTest.xml"), "UTF-8");

		ClassIntrospector c = new ClassIntrospector(ListTestObj.class);

		long ini = System.currentTimeMillis();
		XMLParser<ListTestObj> parser = new WoodStoxObjParserImpl<>(ListTestObj.class, c);
		for (int i = 0; i < LOOPS; i++) {
			ListTestObj o = parser.parse(xml);

			assertEquals(6138, o.getList().size());
		}
		long end = System.currentTimeMillis();

		System.out.println("Diff:" + (end - ini));
	}



	@Test
	public void tenTimesParsed() throws Exception {

		//Thread.sleep(20000);

		for (int i = 0; i < 10; i++) {
			parseParsedBigList();
		}
	}
}
