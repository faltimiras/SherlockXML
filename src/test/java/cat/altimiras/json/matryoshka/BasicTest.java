package cat.altimiras.json.matryoshka;

import cat.altimiras.matryoshka.Matryoshka;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BasicTest {

	@Test
	public void simple() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{ \"k\" : \"v\" }");
		assertEquals("v", matryoshka.get("k").value());
	}

	@Test
	public void simple2() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{\"k1\" : \"v1\", \"k2\" : \"v2\" }");
		assertEquals("v1", matryoshka.get("k1").value());
		assertEquals("v2", matryoshka.get("k2").value());
	}

	@Test
	public void ints() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{\"k1\" : 1, \"k2\" :2 }");
		assertEquals(1, matryoshka.get("k1").value());
		assertEquals(2, matryoshka.get("k2").value());
	}

	@Test
	public void floats() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{\"k1\" : 1.33, \"k2\" :2.69888 }");
		assertEquals(1.33f, matryoshka.get("k1").value());
		assertEquals(2.69888f, matryoshka.get("k2").value());
	}

	@Test
	public void booleans() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{\"k1\" : true, \"k2\" :false }");
		assertTrue((Boolean) matryoshka.get("k1").value());
		assertFalse((Boolean) matryoshka.get("k2").value());
	}

	@Test
	public void nulls() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{\"k1\" : true, \"k2\" : null }");
		assertTrue((Boolean) matryoshka.get("k1").value());
		assertNull(matryoshka.get("k2").value());
	}
}
