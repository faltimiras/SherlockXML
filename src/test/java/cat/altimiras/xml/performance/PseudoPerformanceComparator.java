package cat.altimiras.xml.performance;


import cat.altimiras.xml.ClassIntrospector;
import cat.altimiras.xml.XMLParser;
import cat.altimiras.xml.XMLParserImpl;
import cat.altimiras.xml.pojo.ListTestObj;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PseudoPerformanceComparator {

	private static final int LOOPS = 1000;

    //THIS IS NOT A REAL PERFORMANCE TEST!!
	//@Test
	public void parseBigList() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/performance/bigListTest.xml"), "UTF-8");

		ClassIntrospector c = new ClassIntrospector(ListTestObj.class);

		long ini = System.currentTimeMillis();
		XMLParser<ListTestObj> parser = new XMLParserImpl<>(ListTestObj.class, c);
		for (int i = 0; i < LOOPS; i++) {
			ListTestObj o = parser.parse(xml);

			assertEquals(2394, o.getList().size());
		}
		long end = System.currentTimeMillis();

		System.out.println("Diff:" + (end - ini));

	}

    //THIS IS NOT A REAL PERFORMANCE TEST!!
	@Test
	public void parseBigListIgnore() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/performance/bigListIgnoreTest.xml"), "UTF-8");

		ClassIntrospector c = new ClassIntrospector(ListTestObj.class);

		long ini = System.currentTimeMillis();
		XMLParser<ListTestObj> parser = new XMLParserImpl<>(ListTestObj.class, c);
		for (int i = 0; i < LOOPS; i++) {
			ListTestObj o = parser.parse(xml);

			assertEquals(4896, o.getList().size());
		}
		long end = System.currentTimeMillis();

		System.out.println("Diff:" + (end - ini));
	}

	//THIS IS NOT A REAL PERFORMANCE TEST!!
	//@Test
	public void parseBigListAttIgnore() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/performance/bigListAttTest.xml"), "UTF-8");

		ClassIntrospector c = new ClassIntrospector(ListTestObj.class);

		long ini = System.currentTimeMillis();
		XMLParser<ListTestObj> parser = new XMLParserImpl<>(ListTestObj.class, c);
		for (int i = 0; i < LOOPS; i++) {
			ListTestObj o = parser.parse(xml);

			assertEquals(6138, o.getList().size());
		}
		long end = System.currentTimeMillis();

		System.out.println("Diff:" + (end - ini));
	}
}
