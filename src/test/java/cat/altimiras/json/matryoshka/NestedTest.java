package cat.altimiras.json.matryoshka;

import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NestedTest {

	@Test
	public void nested() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{ \"k\" : { \"k2\" : \"v\" }}");
		assertEquals("v", matryoshka.get("k/k2").value());
		assertNull(matryoshka.get("k2").value());
	}

	@Test
	public void nested2() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{ \"k\" : { \"k2\" : {\"k3\" : 2 }}}");
		assertEquals(2, matryoshka.get("k/k2/k3").value());
	}

	@Test
	public void nestedAfter() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{ \"k\" : { \"k2\" : true }, \"k1\" : 99}");
		assertTrue((Boolean) matryoshka.get("k/k2").value());
		assertNull(matryoshka.get("k2").value());
		assertEquals(99, matryoshka.get("k1").value());
	}

	@Test
	public void nestedBefore() throws Exception {

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse("{ \"k1\" : 99, \"k\" : { \"k2\" : true }}");
		assertTrue((Boolean) matryoshka.get("k/k2").value());
		assertNull(matryoshka.get("k2").value());
		assertEquals(99, matryoshka.get("k1").value());
	}

	@Test
	public void nestedMultiple() throws Exception {

		String json = IOUtils.toString(this.getClass().getResourceAsStream("/json/nested1.json"), "UTF-8");

		Matryoshka matryoshka = new JSONMatryoshkaParserImpl().parse(json);
		assertEquals("123", matryoshka.get("key2").value());
		assertEquals("value", matryoshka.get("key").value());
		assertEquals("lolo", matryoshka.get("nested2/n1").value());
		assertEquals(66, matryoshka.get("nested2/n2").value());
		assertEquals("lolo", matryoshka.get("nested1/n1").value());
		assertEquals(55, matryoshka.get("nested1/n2").value());
		assertEquals("b", matryoshka.get("nested3/a").value());
	}

}
