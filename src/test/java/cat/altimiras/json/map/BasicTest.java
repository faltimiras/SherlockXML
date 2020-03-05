package cat.altimiras.json.map;

import cat.altimiras.matryoshka.Matryoshka;
import org.junit.Test;

import java.util.Map;

import static cat.altimiras.json.JSONFactory.DEFAULT_INCOMPLETE_KEY_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BasicTest {

	@Test
	public void simple() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{ \"k\" : \"v\" }");
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("v", matryoshka.get("k").value());
		assertFalse(result.containsKey(DEFAULT_INCOMPLETE_KEY_NAME));
	}

	@Test
	public void simple2() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{\"k1\" : \"v1\", \"k2\" : \"v2\" }");
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("v1", matryoshka.get("k1").value());
		assertEquals("v2", matryoshka.get("k2").value());
		assertFalse(result.containsKey(DEFAULT_INCOMPLETE_KEY_NAME));
	}

	@Test
	public void ints() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{\"k1\" : 1, \"k2\" :2 }");
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(1, matryoshka.get("k1").value());
		assertEquals(2, matryoshka.get("k2").value());
		assertFalse(result.containsKey(DEFAULT_INCOMPLETE_KEY_NAME));
	}

	@Test
	public void floats() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{\"k1\" : 1.33, \"k2\" :2.69888 }");
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(1.33f, matryoshka.get("k1").value());
		assertEquals(2.69888f, matryoshka.get("k2").value());
		assertFalse(result.containsKey(DEFAULT_INCOMPLETE_KEY_NAME));
	}

	@Test
	public void booleans() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{\"k1\" : true, \"k2\" :false }");
		Matryoshka matryoshka = new Matryoshka(result);

		assertTrue((Boolean) matryoshka.get("k1").value());
		assertFalse((Boolean) matryoshka.get("k2").value());
		assertFalse(result.containsKey(DEFAULT_INCOMPLETE_KEY_NAME));
	}

	@Test
	public void nulls() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{\"k1\" : true, \"k2\" : null }");
		Matryoshka matryoshka = new Matryoshka(result);

		assertTrue((Boolean) matryoshka.get("k1").value());
		assertNull(matryoshka.get("k2").value());
		assertFalse(result.containsKey(DEFAULT_INCOMPLETE_KEY_NAME));
	}
}