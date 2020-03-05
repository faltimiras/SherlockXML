package cat.altimiras.json.map;

import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.util.Map;

import static cat.altimiras.json.JSONFactory.DEFAULT_INCOMPLETE_KEY_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NestedTest {

	@Test
	public void nested() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{ \"k\" : { \"k2\" : \"v\" }}");
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("v", matryoshka.get("k/k2").value());
		assertNull(matryoshka.get("k2").value());
	}

	@Test
	public void nested2() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{ \"k\" : { \"k2\" : {\"k3\" : 2 }}}");
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(2, matryoshka.get("k/k2/k3").value());
	}

	@Test
	public void nestedAfter() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{ \"k\" : { \"k2\" : true }, \"k1\" : 99}");
		Matryoshka matryoshka = new Matryoshka(result);

		assertTrue((Boolean) matryoshka.get("k/k2").value());
		assertNull(matryoshka.get("k2").value());
		assertEquals(99, matryoshka.get("k1").value());
	}

	@Test
	public void nestedBefore() throws Exception {

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse("{ \"k1\" : 99, \"k\" : { \"k2\" : true }}");
		Matryoshka matryoshka = new Matryoshka(result);

		assertTrue((Boolean) matryoshka.get("k/k2").value());
		assertNull(matryoshka.get("k2").value());
		assertEquals(99, matryoshka.get("k1").value());
	}

	@Test
	public void nestedMultiple() throws Exception {

		String json = IOUtils.toString(this.getClass().getResourceAsStream("/json/nested1.json"), "UTF-8");

		Map result = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME).parse(json);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("123", matryoshka.get("key2").value());
		assertEquals("value", matryoshka.get("key").value());
		assertEquals("lolo", matryoshka.get("nested2/n1").value());
		assertEquals(66, matryoshka.get("nested2/n2").value());
		assertEquals("lolo", matryoshka.get("nested1/n1").value());
		assertEquals(55, matryoshka.get("nested1/n2").value());
		assertEquals("b", matryoshka.get("nested3/a").value());
	}
}