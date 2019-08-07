package cat.altimiras.xml.performance;


import cat.altimiras.xml.XMLParser;
import cat.altimiras.xml.obj.ClassIntrospector;
import cat.altimiras.xml.obj.WoodStoxObjParserImpl;
import cat.altimiras.xml.parsed.WoodStoxTruffleParserImpl;
import cat.altimiras.xml.pojo.ListTestObj;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;

@Ignore
public class PseudoPerformanceComparator {

	private static final int LOOPS = 1000;

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	//THIS IS NOT A REAL PERFORMANCE TEST!!
	@Test
	public void parseBigList() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/performance/bigListTest.xml"), "UTF-8");

		ClassIntrospector c = new ClassIntrospector(ListTestObj.class);

		long ini = System.currentTimeMillis();
		XMLParser<ListTestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, ListTestObj.class, c);
		for (int i = 0; i < LOOPS; i++) {
			parser.parse(xml);
		}
		long end = System.currentTimeMillis();

		System.out.println("Diff:" + (end - ini));

	}

	//THIS IS NOT A REAL PERFORMANCE TEST!!
	@Test
	public void parseParsedBigList() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/performance/bigListTest.xml"), "UTF-8");

		long ini = System.currentTimeMillis();
		WoodStoxTruffleParserImpl parser = new WoodStoxTruffleParserImpl(xmlInputFactory);
		for (int i = 0; i < LOOPS; i++) {
			parser.parse(xml);
		}
		long end = System.currentTimeMillis();

		System.out.println("Diff:" + (end - ini));

	}

	//THIS IS NOT A REAL PERFORMANCE TEST!!
	@Test
	public void parseBigListIgnore() throws Exception {

		byte[] xml = IOUtils.toString(this.getClass().getResourceAsStream("/performance/bigListIgnoreTest.xml"), "UTF-8").getBytes();

		ClassIntrospector c = new ClassIntrospector(ListTestObj.class);

		long ini = System.currentTimeMillis();
		XMLParser<ListTestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, ListTestObj.class, c);
		for (int i = 0; i < LOOPS; i++) {
			parser.parse(xml);
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
		XMLParser<ListTestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, ListTestObj.class, c);
		for (int i = 0; i < LOOPS; i++) {
			parser.parse(xml);
		}
		long end = System.currentTimeMillis();

		System.out.println("Diff:" + (end - ini));
	}

	//THIS IS NOT A REAL PERFORMANCE TEST!!
	@Test
	public void tenTimes() throws Exception {

		xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, false);
		xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		xmlInputFactory.setProperty(XMLInputFactory2.P_REPORT_PROLOG_WHITESPACE, false);
		xmlInputFactory.setProperty(XMLInputFactory2.P_PRESERVE_LOCATION, false);
		xmlInputFactory.setProperty(XMLInputFactory2.P_INTERN_NAMES, true);
		xmlInputFactory.setProperty(XMLInputFactory2.P_INTERN_NS_URIS, true);

		for (int i = 0; i < 10; i++) {
			parseBigList();
		}
	}

	//THIS IS NOT A REAL PERFORMANCE TEST!!
	@Test
	public void tenTimesParsed() throws Exception {

		xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, false);
		xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		xmlInputFactory.setProperty(XMLInputFactory2.P_REPORT_PROLOG_WHITESPACE, false);
		xmlInputFactory.setProperty(XMLInputFactory2.P_PRESERVE_LOCATION, false);
		xmlInputFactory.setProperty(XMLInputFactory2.P_INTERN_NAMES, true);
		xmlInputFactory.setProperty(XMLInputFactory2.P_INTERN_NS_URIS, true);

		for (int i = 0; i < 10; i++) {
			parseParsedBigList();
		}
	}
}
