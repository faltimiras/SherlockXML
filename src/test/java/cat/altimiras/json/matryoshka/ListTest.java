package cat.altimiras.json.matryoshka;

import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ListTest {

	@Test
	public void simple() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{ \"k\" :[1,2,3] }");

		assertEquals(3, matryoshka.get("k").asList().size());
		assertEquals(1, matryoshka.get("k").asList().get(0).value());
		assertEquals(2, matryoshka.get("k").asList().get(1).value());
		assertEquals(3, matryoshka.get("k").asList().get(2).value());
	}


	@Test
	public void simpleTypes() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{ \"k\" :[\"1\",2, false] }");

		assertEquals(3, matryoshka.get("k").asList().size());
		assertEquals("1", matryoshka.get("k").asList().get(0).value());
		assertEquals(2, matryoshka.get("k").asList().get(1).value());
		assertEquals(false, matryoshka.get("k").asList().get(2).value());
	}


	@Test
	public void multipleLists() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{ \"k\" :[1,2,3], \"k2\" :[true, false, true] }");

		assertEquals(3, matryoshka.get("k").asList().size());
		assertEquals(1, matryoshka.get("k").asList().get(0).value());
		assertEquals(2, matryoshka.get("k").asList().get(1).value());
		assertEquals(3, matryoshka.get("k").asList().get(2).value());

		assertEquals(3, matryoshka.get("k2").asList().size());
		assertEquals(true, matryoshka.get("k2").asList().get(0).value());
		assertEquals(false, matryoshka.get("k2").asList().get(1).value());
		assertEquals(true, matryoshka.get("k2").asList().get(2).value());
	}

	@Test
	public void multipleListsAfter() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{ \"k\" :[1,2,3], \"k2\" :[true, false, true], \"k3\" : \"aaa\" }");

		assertEquals(3, matryoshka.get("k").asList().size());
		assertEquals(1, matryoshka.get("k").asList().get(0).value());
		assertEquals(2, matryoshka.get("k").asList().get(1).value());
		assertEquals(3, matryoshka.get("k").asList().get(2).value());

		assertEquals(3, matryoshka.get("k2").asList().size());
		assertEquals(true, matryoshka.get("k2").asList().get(0).value());
		assertEquals(false, matryoshka.get("k2").asList().get(1).value());
		assertEquals(true, matryoshka.get("k2").asList().get(2).value());

		assertEquals("aaa", matryoshka.get("k3").value());
	}

	@Test
	public void multipleListsBefore() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{ \"k3\" : \"aaa\", \"k\" :[1,2,3], \"k2\" :[true, false, true] }");

		assertEquals(3, matryoshka.get("k").asList().size());
		assertEquals(1, matryoshka.get("k").asList().get(0).value());
		assertEquals(2, matryoshka.get("k").asList().get(1).value());
		assertEquals(3, matryoshka.get("k").asList().get(2).value());

		assertEquals(3, matryoshka.get("k2").asList().size());
		assertEquals(true, matryoshka.get("k2").asList().get(0).value());
		assertEquals(false, matryoshka.get("k2").asList().get(1).value());
		assertEquals(true, matryoshka.get("k2").asList().get(2).value());

		assertEquals("aaa", matryoshka.get("k3").value());
	}


	@Test
	public void nestedList1() throws Exception {

		String json = IOUtils.toString(this.getClass().getResourceAsStream("/json/nestedList1.json"), "UTF-8");
		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse(json);

		assertEquals(4, matryoshka.get("key").asList().size());
		assertEquals("lolo", matryoshka.get("key").asList().get(0).get("n1").value());
		assertEquals(55, matryoshka.get("key").asList().get(0).get("n2").value());
		assertEquals("lala", matryoshka.get("key").asList().get(1).value());
		assertEquals("lele", matryoshka.get("key").asList().get(2).get("n1").value());
		assertEquals(66, matryoshka.get("key").asList().get(2).get("n2").value());
		assertEquals(false, matryoshka.get("key").asList().get(3).get("a").value());
		assertFalse((Boolean) matryoshka.get("key2").value());
	}

}
