package cat.altimiras.xml.performance;


import cat.altimiras.xml.ClassIntrospector;
import cat.altimiras.xml.XMLParser;
import cat.altimiras.xml.XMLParserImpl;
import cat.altimiras.xml.pojo.ListTestObj;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.util.Arrays;

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

		byte[] xml = IOUtils.toByteArray(this.getClass().getResourceAsStream("/performance/bigListIgnoreTest.xml"));

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

	@Test
	public void tenTimes() throws Exception {
		for (int i = 0; i<10 ; i++){
			parseBigListIgnore();
		}
	}


	@Test
	public void dasfasdf() throws Exception {

		byte[] s = IOUtils.toString(this.getClass().getResourceAsStream("/performance/bigListAttTest.xml"), "UTF-8").getBytes();

		byte[] a ="sdasdfasdfasdfasdfasdf".getBytes();
		byte[] b = "3sdasdfasdfasdfasdfasdf".getBytes();

		String ab = "sdasdfasdfasdfasdfasdf";
		String bb = "asdfasdfasdfasdfasdf";

		long ini = System.currentTimeMillis();


		for (int i = 0; i < 1000000; i++) {

			//Arrays.equals(a,b);
			ab.equals(bb);

		/*
			byte[] t = new byte[50];
			 System.arraycopy(s,200,t,0,50);
			String a = new String(t);
*/
/*
			for(int ii = 0; ii <50; ii++){
				char[] t = new char[50];
				t[ii]= (char)s[200+ii];
				String.copyValueOf(t);
			}
*/
		}
		long end = System.currentTimeMillis();

		System.out.println("Diff:" + (end - ini));

	}
}
