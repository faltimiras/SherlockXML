package cat.altimiras.json.map;

import cat.altimiras.TagListener;
import cat.altimiras.matryoshka.Matryoshka;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.util.Map;

import static cat.altimiras.json.JSONFactory.DEFAULT_INCOMPLETE_KEY_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TagListenerTest {

	@Test
	public void stop() throws Exception {

		String json = IOUtils.toString(this.getClass().getResourceAsStream("/json/nested1.json"), "UTF-8");
		TagListener objListener = mock(TagListener.class);
		when(objListener.notify(eq("key"), any())).thenReturn(true);

		JSONMapParserImpl matryoshkaParser = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME);
		matryoshkaParser.register("key", objListener);

		Map result = matryoshkaParser.parse(json);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("value", matryoshka.get("key").value());
		assertNull(matryoshka.get("key2").value());
	}

	@Test
	public void notStop() throws Exception {

		String json = IOUtils.toString(this.getClass().getResourceAsStream("/json/nested1.json"), "UTF-8");
		TagListener objListener = mock(TagListener.class);
		when(objListener.notify(eq("key"), any())).thenReturn(false);

		JSONMapParserImpl matryoshkaParser = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME);
		matryoshkaParser.register("key", objListener);

		Map result = matryoshkaParser.parse(json);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("123", matryoshka.get("key2").value());
		assertEquals("value", matryoshka.get("key").value());
		assertEquals("lolo", matryoshka.get("nested2/n1").value());
		assertEquals(66, matryoshka.get("nested2/n2").value());
		assertEquals("lolo", matryoshka.get("nested1/n1").value());
		assertEquals(55, matryoshka.get("nested1/n2").value());
		assertEquals("b", matryoshka.get("nested3/a").value());
	}

	@Test
	public void stopList() throws Exception {

		String json = IOUtils.toString(this.getClass().getResourceAsStream("/json/nestedList1.json"), "UTF-8");

		TagListener objListener = mock(TagListener.class);
		when(objListener.notify(eq("key"), any())).thenReturn(true);

		JSONMapParserImpl matryoshkaParser = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME);
		matryoshkaParser.register("key", objListener);

		Map result = matryoshkaParser.parse(json);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(4, matryoshka.get("key").asList().size());
		assertEquals("lolo", matryoshka.get("key").asList().get(0).get("n1").value());
		assertEquals(55, matryoshka.get("key").asList().get(0).get("n2").value());
		assertEquals("lala", matryoshka.get("key").asList().get(1).value());
		assertEquals("lele", matryoshka.get("key").asList().get(2).get("n1").value());
		assertEquals(66, matryoshka.get("key").asList().get(2).get("n2").value());
		assertEquals(false, matryoshka.get("key").asList().get(3).get("a").value());
		assertNull(matryoshka.get("key2").value());
	}

	@Test
	public void stopElementList() throws Exception {

		String json = IOUtils.toString(this.getClass().getResourceAsStream("/json/nestedList1.json"), "UTF-8");

		TagListener objListener = mock(TagListener.class);
		when(objListener.notify(eq("n1"), any())).thenReturn(true);

		JSONMapParserImpl matryoshkaParser = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME);
		matryoshkaParser.register("n1", objListener);

		Map result = matryoshkaParser.parse(json);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(1, matryoshka.get("key").asList().size());
		assertEquals("lolo", matryoshka.get("key").asList().get(0).get("n1").value());
		assertNull(matryoshka.get("key").asList().get(0).get("n2").value());
		assertNull(matryoshka.get("key2").value());
	}

	@Test
	public void notStopList() throws Exception {

		String json = IOUtils.toString(this.getClass().getResourceAsStream("/json/nestedList1.json"), "UTF-8");

		TagListener objListener = mock(TagListener.class);
		when(objListener.notify(eq("key"), any())).thenReturn(false);

		JSONMapParserImpl matryoshkaParser = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME);
		matryoshkaParser.register("key", objListener);

		Map result = matryoshkaParser.parse(json);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals(4, matryoshka.get("key").asList().size());
		assertEquals("lolo", matryoshka.get("key").asList().get(0).get("n1").value());
		assertEquals(55, matryoshka.get("key").asList().get(0).get("n2").value());
		assertEquals("lala", matryoshka.get("key").asList().get(1).value());
		assertEquals("lele", matryoshka.get("key").asList().get(2).get("n1").value());
		assertEquals(66, matryoshka.get("key").asList().get(2).get("n2").value());
		assertEquals(false, matryoshka.get("key").asList().get(3).get("a").value());
		assertFalse((Boolean) matryoshka.get("key2").value());
	}

	@Test
	public void stopObject() throws Exception {

		String json = IOUtils.toString(this.getClass().getResourceAsStream("/json/nested1.json"), "UTF-8");
		TagListener objListener = mock(TagListener.class);
		when(objListener.notify(eq("nested2"), any())).thenReturn(true);

		JSONMapParserImpl matryoshkaParser = new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME);
		matryoshkaParser.register("nested2", objListener);

		Map result = matryoshkaParser.parse(json);
		Matryoshka matryoshka = new Matryoshka(result);

		assertEquals("123", matryoshka.get("key2").value());
		assertEquals("value", matryoshka.get("key").value());
		assertEquals("lolo", matryoshka.get("nested2/n1").value());
		assertEquals(66, matryoshka.get("nested2/n2").value());
		assertEquals("lolo", matryoshka.get("nested1/n1").value());
		assertEquals(55, matryoshka.get("nested1/n2").value());
		assertNull(matryoshka.get("nested3/a").value());
	}
}