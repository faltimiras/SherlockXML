package cat.altimiras.json;

import cat.altimiras.Parser;
import cat.altimiras.json.map.JSONMapParserImpl;

import java.util.Map;

public class JSONFactory {

	final static public String DEFAULT_INCOMPLETE_KEY_NAME = "_is_incomplete";

	public static void init(Class... classes) throws Exception {
		throw new UnsupportedOperationException("Still not implemented");
	}

	public static Parser<Map> getParser(String incompleteKeyName) {
		return new JSONMapParserImpl(incompleteKeyName);
	}

	public static Parser<Map> getParser() {
		return new JSONMapParserImpl(DEFAULT_INCOMPLETE_KEY_NAME);
	}

	public static Parser getParser(Class clazz) {
		throw new UnsupportedOperationException("Still not implemented");
	}
}
