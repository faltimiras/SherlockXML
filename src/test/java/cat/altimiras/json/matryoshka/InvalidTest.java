package cat.altimiras.json.matryoshka;

import cat.altimiras.matryoshka.Matryoshka;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InvalidTest {

	@Test
	public void simple() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{\"k1\" : \"v1\", \"k2\" :  }");
		assertEquals("v1", matryoshka.get("k1").value());
	}

	@Test
	public void nested() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{ \"k\" : { \"k2\" : {\"k3\" : 2 , \"k4\" }}}");
		assertEquals(2, matryoshka.get("k/k2/k3").value());
	}

	@Test
	public void list() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{ \"k\" :[1,2, }");

		assertEquals(2, matryoshka.get("k").asList().size());
		assertEquals(1, matryoshka.get("k").asList().get(0).value());
		assertEquals(2, matryoshka.get("k").asList().get(1).value());
	}

	@Test
	public void listObjects() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{ \"k\" :[1,2, { \"k\" : \"v\" } }");

		assertEquals(3, matryoshka.get("k").asList().size());
		assertEquals(1, matryoshka.get("k").asList().get(0).value());
		assertEquals(2, matryoshka.get("k").asList().get(1).value());
		assertEquals("v", matryoshka.get("k").asList().get(2).get("k").value());
	}

	@Test
	public void listObjects2() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{ \"k\" :[1,2, { \"k\" : \"v\"}, 4 ");

		assertEquals(4, matryoshka.get("k").asList().size());
		assertEquals(1, matryoshka.get("k").asList().get(0).value());
		assertEquals(2, matryoshka.get("k").asList().get(1).value());
		assertEquals("v", matryoshka.get("k").asList().get(2).get("k").value());
		assertEquals(4, matryoshka.get("k").asList().get(3).value());
	}
}
