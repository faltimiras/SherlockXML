package cat.altimiras.json.map;

import cat.altimiras.TagListener;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Map;

/**
 * MapDeserializer has state and JsonDeserializer impl object is shared by all threads/calls
 */
class MapDeserializerWrapper extends JsonDeserializer<Map> {

	final private String incompleteKeyName;
	private Map<String, TagListener> listeners;

	public MapDeserializerWrapper(Map<String, TagListener> listeners, String incompleteKeyName) {
		this.listeners = listeners;
		this.incompleteKeyName = incompleteKeyName;
	}

	public Map deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		return new MapDeserializer(listeners, incompleteKeyName).deserialize(jsonParser, deserializationContext);
	}

}

