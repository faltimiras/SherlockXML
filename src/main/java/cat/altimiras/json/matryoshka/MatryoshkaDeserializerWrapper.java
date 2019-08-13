package cat.altimiras.json.matryoshka;

import cat.altimiras.TagListener;
import cat.altimiras.matryoshka.Matryoshka;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Map;

/**
 * MatryoshkaDeserializer has state and JsonDeserializer impl object is shared by all threads/calls
 */
class MatryoshkaDeserializerWrapper extends JsonDeserializer<Matryoshka> {

	private Map<String, TagListener> listeners = null;

	public MatryoshkaDeserializerWrapper(Map<String, TagListener> listeners) {
		this.listeners = listeners;
	}

	public Matryoshka deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		return new MatryoshkaDeserializer(listeners).deserialize(jsonParser, deserializationContext);
	}

}

