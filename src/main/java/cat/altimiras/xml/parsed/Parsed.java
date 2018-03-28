package cat.altimiras.xml.parsed;

import cat.altimiras.xml.XMLElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Parsed extends XMLElement {

	private final static String SEPARATOR = "/";

	private Map<String, Object> data;

	private String value;

	Parsed() {
	}

	Parsed(Object content) throws Exception {
		if (content instanceof Map) {
			this.data = (Map) content;
		}
		else if (content instanceof String) {
			this.value = (String) content;
		}
		else {
			throw new RuntimeException("Content can be only a Map or a String");
		}
	}

	Parsed(String value) {
		this.value = value;
	}

	Parsed(Map<String, Object> data) {
		this.data = data;
	}

	public Result get(String path) {
		if (path == null) {
			return null;
		}
		return get(path.split(SEPARATOR));
	}

	public Result get(String... parts) {

		if (parts == null || parts.length == 0) {
			return null;
		}

		//support to / or not at the beginning of the path expression
		int start = 0;
		if (parts[0].isEmpty() && parts.length > 1) {
			start = 1;
		}
		return new Result( getRec(data, parts, start));
	}

	private Object getRec(Map<String, Object> p, String[] path, int pos) {

		if (p == null || path == null || path.length == 0) {
			return value;
		}

		if (path.length - 1 == pos) {
			return p.get(path[pos]);
		}
		else {
			if (p.get(path[pos]) instanceof String) {
				return p.get(path[pos]);
			}
			else {
				return getRec((Map) p.get(path[pos]), path, ++pos);
			}
		}
	}

	public class Result {

		final private Object content;

		Result(Object o) {
			this.content = o;
		}

		public ParsedList asList() {

			if (this.content == null) {
				return new ParsedList();
			}

			if (this.content instanceof List) {
				return new ParsedList((List) this.content);
			}
			else {
				List list = new ArrayList(1);
				list.add(this.content);
				return new ParsedList(list);
			}
		}

		public Object value() {
			return content;
		}

		public Parsed asParsed() throws Exception {
			if (this.content instanceof Map) {
				return new Parsed((Map) this.content);
			}
			if (this.content instanceof String) {
				return new Parsed((String) this.content);
			}

			throw new Exception("Can not get as Parsed. Hint: try with asList()");
		}
	}

	public class ParsedIterator implements Iterator<Parsed> {

		final private Iterator<Map> it;

		ParsedIterator(Iterator<Map> it) {
			this.it = it;
		}

		@Override
		public boolean hasNext() {
			if (it == null) {
				return false;
			}
			return it.hasNext();
		}

		@Override
		public Parsed next() {
			if (it == null) {
				return null;
			}
			Map m = it.next();
			return new Parsed(m);
		}
	}

	public class EmptyParsedIterator implements Iterator {

		EmptyParsedIterator() {
		}

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public Object next() {
			throw new NoSuchElementException();
		}
	}

	public class ParsedList {

		final private List rawList;

		ParsedList() {
			this.rawList = null;
		}

		ParsedList(List rawList) {
			this.rawList = rawList;
		}

		public int size() {
			return rawList.size();
		}

		public boolean isEmpty() {
			if (this.rawList == null) {
				return true;
			}
			return rawList.isEmpty();
		}

		public Iterator<Parsed> iterator() {
			if (this.rawList == null || rawList.isEmpty()) {
				return new EmptyParsedIterator();
			}
			return new ParsedIterator(this.rawList.iterator());
		}

		public Parsed get(int index) throws Exception {
			return new Parsed(this.rawList.get(index));
		}
	}
}
