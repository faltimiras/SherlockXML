package cat.altimiras.xml.parsed;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParsedTest {


	@Test
	public void simpleValue() throws Exception {

		Map<String, Object> content = new HashMap<>();
		content.put("value", "123456789");

		Parsed parsed = new Parsed(content);

		assertEquals("123456789", parsed.get("value").value());
		assertEquals("123456789", parsed.get("/value").value());
		assertEquals(1, parsed.get("value").asList().size());
		assertEquals("123456789", parsed.get("value").asList().get(0).get("").value());
	}

	@Test
	public void object() throws Exception {

		Map<String, Object> obj = new HashMap<>();
		obj.put("value1", "111");
		obj.put("value2", "222");

		Map<String, Object> content = new HashMap<>();
		content.put("obj", obj);
		content.put("field1", "aaa");
		content.put("field2", "bbb");

		Map<String, Object> root = new HashMap<>();
		root.put("root", content);

		Parsed parsed = new Parsed(root);

		assertEquals("111", parsed.get("root/obj/value1").value());
		assertEquals("222", parsed.get("/root/obj/value2").value());
		assertEquals("aaa", parsed.get("root/field1").value());
		assertEquals("bbb", parsed.get("root/field2").value());

		assertEquals("111", parsed.get("root/obj/value1").asList().get(0).get("").value());
		assertEquals("222", parsed.get("/root").asList().get(0).get("/obj/value2").value());
		assertEquals("222", parsed.get("/root").asList().get(0).get("/obj/").asParsed().get("value2").value());

		assertEquals("aaa", parsed.get("root").asParsed().get("field1").value());
		assertEquals("bbb", parsed.get("root/field2").value());
	}

	@Test
	public void list() throws Exception {

		Map<String, Object> e1 = new HashMap<>(1);
		e1.put("key", "111");
		Map<String, Object> e2 = new HashMap<>(1);
		e2.put("key", "222");

		Map<String, Object> content = new HashMap<>();
		content.put("list", Arrays.asList(e1, e2));
		content.put("field1", "aaa");
		content.put("field2", "bbb");

		Map<String, Object> root = new HashMap<>();
		root.put("root", content);

		Parsed parsed = new Parsed(root);

		assertEquals(2, parsed.get("root/list").asList().size());
		assertEquals("111", parsed.get("root/list").asList().get(0).get("key").value());
		assertEquals("222", parsed.get("root/list").asList().get(1).get("key").value());
		assertEquals("aaa", parsed.get("root/field1").value());
		assertEquals("bbb", parsed.get("root/field2").value());

		assertTrue(parsed.get("root/list").value() instanceof List);
	}

	@Test(expected = Exception.class)
	public void asParsedList() throws Exception {
		Map<String, Object> e1 = new HashMap<>(1);
		e1.put("key", "111");

		Map<String, Object> content = new HashMap<>();
		content.put("list", Arrays.asList(e1));

		Map<String, Object> root = new HashMap<>();
		root.put("root", content);

		Parsed parsed = new Parsed(root);

		parsed.get("root/list").asParsed();
	}

	@Test
	public void parsedIterator() throws Exception {

		Map<String, Object> e1 = new HashMap<>(1);
		e1.put("key", "111");
		Map<String, Object> e2 = new HashMap<>(1);
		e2.put("key", "222");

		Map<String, Object> content = new HashMap<>();
		content.put("list", Arrays.asList(e1, e2));

		Map<String, Object> root = new HashMap<>();
		root.put("root", content);

		Parsed parsed = new Parsed(root);

		Iterator<Parsed> iterator = parsed.get("root/list").asList().iterator();
		assertTrue(iterator.hasNext());
		assertEquals("111", iterator.next().get("key").value());
		assertTrue(iterator.hasNext());
		assertEquals("222", iterator.next().get("key").value());
		assertFalse(iterator.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void emptyIterator() throws Exception {

		Map<String, Object> content = new HashMap<>();
		content.put("list", new ArrayList<>());

		Map<String, Object> root = new HashMap<>();
		root.put("root", content);

		Parsed parsed = new Parsed(root);

		Iterator<Parsed> iterator = parsed.get("root/list").asList().iterator();
		assertFalse(iterator.hasNext());
		assertFalse(iterator.hasNext());

		iterator.next();
	}
}
