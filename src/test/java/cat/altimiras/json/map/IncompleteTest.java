package cat.altimiras.json.map;

import cat.altimiras.matryoshka.Matryoshka;
import org.junit.Test;

import java.util.Map;

import static cat.altimiras.json.JSONFactory.DEFAULT_INCOMPLETE_KEY_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IncompleteTest {

	@Test
	public void simple() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{\"k1\" : \"v1\", \"k2\" :  }");
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("v1", matryoshka.get("k1").value());
		assertTrue(result.containsKey(DEFAULT_INCOMPLETE_KEY_NAME));
	}

	@Test
	public void nested() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{ \"k\" : { \"k2\" : {\"k3\" : 2 , \"k4\" }}}");
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(2, matryoshka.get("k/k2/k3").value());
		assertTrue(result.containsKey(DEFAULT_INCOMPLETE_KEY_NAME));
	}

	@Test
	public void list() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{ \"k\" :[1,2, }");
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(2, matryoshka.get("k").asList().size());
		assertEquals(1, matryoshka.get("k").asList().get(0).value());
		assertEquals(2, matryoshka.get("k").asList().get(1).value());
		assertTrue(result.containsKey(DEFAULT_INCOMPLETE_KEY_NAME));
	}

	@Test
	public void listObjects() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{ \"k\" :[1,2, { \"k\" : \"v\" } }");
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(3, matryoshka.get("k").asList().size());
		assertEquals(1, matryoshka.get("k").asList().get(0).value());
		assertEquals(2, matryoshka.get("k").asList().get(1).value());
		assertEquals("v", matryoshka.get("k").asList().get(2).get("k").value());
		assertTrue(result.containsKey(DEFAULT_INCOMPLETE_KEY_NAME));
	}

	@Test
	public void listObjects2() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{ \"k\" :[1,2, { \"k\" : \"v\"}, 4 ");
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(4, matryoshka.get("k").asList().size());
		assertEquals(1, matryoshka.get("k").asList().get(0).value());
		assertEquals(2, matryoshka.get("k").asList().get(1).value());
		assertEquals("v", matryoshka.get("k").asList().get(2).get("k").value());
		assertEquals(4, matryoshka.get("k").asList().get(3).value());
		assertTrue(result.containsKey(DEFAULT_INCOMPLETE_KEY_NAME));
	}
}
