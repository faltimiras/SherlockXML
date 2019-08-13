package cat.altimiras.json.matryoshka;

import cat.altimiras.TagListener;
import cat.altimiras.matryoshka.Matryoshka;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cat.altimiras.Parser.INCOMPLETE;

class MatryoshkaDeserializer extends JsonDeserializer<Matryoshka> {

	final private ArrayDeque<Context> contexts = new ArrayDeque<>();
	/**
	 * Listeners for element. Notified every time a tag is totally processed (on close } element)
	 */
	private Map<String, TagListener> listeners = null;
	private Context currentContext;
	private String currentKey;
	/**
	 * Stack with tags opened and still not closed
	 */

	private boolean stop = false;

	public MatryoshkaDeserializer(Map<String, TagListener> listeners) {
		this.listeners = listeners;
	}

	public Matryoshka deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

		if (jsonParser.currentTokenId() != 1) {
			throw new IllegalArgumentException("Not a json");
		}

		try {
			Context parent = null;
			JsonToken token = jsonParser.nextToken();
			while (token != null && !stop) {
				int eventType = token.id();
				switch (eventType) {
					case 1: // {
						if (currentContext != null) {
							contexts.push(currentContext);
							currentContext = null;
						}
						break;
					case 2: // }
						parent = contexts.pollFirst();
						if (parent != null) {
							parent.add(parent.lastKey, currentContext.data);

							if (!parent.isList) {
								stop = notify(parent.lastKey, currentContext.data);
							}

							currentContext = parent;
						}
						break;
					case 3: // [
						currentContext.isList = true;
						currentContext.list = new ArrayList<>();
						break;
					case 4: // ]
						parent = contexts.pollFirst();
						if (parent != null) {
							stop = notify(parent.lastKey, currentContext.list);
							parent.add(parent.lastKey, currentContext.list);
							currentContext = parent;
						} else {
							currentContext.isList = false;
							stop = notify(currentContext.lastKey, currentContext.list);
							currentContext.add(currentContext.lastKey, currentContext.list);

						}
						break;
					case 5: //field name, the key
						currentKey = jsonParser.currentName();
						if (currentContext == null) {
							currentContext = new Context(currentKey);
						} else {
							currentContext.lastKey = currentKey;
						}
						break;
					case 6: //String value
						addValue(jsonParser.getValueAsString());
						break;
					case 7: //int value
						addValue(jsonParser.getIntValue());
						break;
					case 8: // float value
						addValue(jsonParser.getFloatValue());
						break;
					case 9: // true
						addValue(true);
						break;
					case 10: //false
						addValue(false);
						break;
					default:
						//do nothing
						break;
				}

				token = jsonParser.nextToken();
			}
			return createMatryoshka(false);
		} catch (Exception e) {
			return createMatryoshka(true);
		}
	}

	private boolean notify(String tag, Object value) {

		if (listeners == null) {
			return false;
		}

		TagListener listener = listeners.get(tag);
		if (listener != null) {
			return listener.notify(tag, value);
		}
		return false; //to continue
	}

	private void addValue(Object value) {

		if (currentContext == null) {
			currentContext = new Context(currentKey);
		}
		currentContext.add(currentKey, value);
		stop = notify(currentKey, value);

		currentKey = null;
	}

	private Matryoshka createMatryoshka(boolean incomplete) {

		flush();

		if (currentContext.data == null || currentContext.data.isEmpty()) {
			return null;
		} else {
			Matryoshka matryoshka = new Matryoshka(currentContext.data);
			if (incomplete) {
				matryoshka.setMetadata(INCOMPLETE, true);
			}
			return matryoshka;
		}
	}

	private void flush() {

		while (!contexts.isEmpty()) {
			Context parent = contexts.removeFirst();

			parent.add(parent.lastKey, currentContext.data);
			currentContext = parent;
		}

		if (currentContext.isList) {
			currentContext.isList = false;
			currentContext.add(currentContext.lastKey, currentContext.list);
		}
	}

	private class Context {

		protected String lastKey;

		protected HashMap<String, Object> data;
		protected boolean isList = false;
		protected List<Object> list;

		Context(String key) {
			this.lastKey = key;
			this.data = new HashMap<>();
		}

		void add(String key, Object value) {
			if (isList) {
				list.add(value);
			} else {
				data.put(key, value);
			}
		}
	}
}

