package cat.altimiras.json;

import cat.altimiras.Parser;
import cat.altimiras.json.matryoshka.JSONMatryoshkaParserImpl;
import cat.altimiras.matryoshka.Matryoshka;

public class JSONFactory {

	public static void init(Class... classes) throws Exception {
		throw new UnsupportedOperationException("Still not implemented");
	}

	public static Parser<Matryoshka> getParser() {
		return new JSONMatryoshkaParserImpl();
	}

	public static Parser getParser(Class clazz) {
		throw new UnsupportedOperationException("Still not implemented");
	}
}
