package cat.altimiras.json.map;

import cat.altimiras.Parser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

public class JSONMapParserImpl extends Parser<Map> {

	private final String incompleteKeyName;
	private ObjectMapper objectMapper = new ObjectMapper();
	private boolean init = false;

	public JSONMapParserImpl(String incompleteKeyName) {
		Objects.nonNull(incompleteKeyName);
		this.incompleteKeyName = incompleteKeyName;
	}

	@Override
	public Map parse(String json) throws IOException {
		if (!init) {
			configure();
		}
		return objectMapper.readValue(json, Map.class);
	}

	@Override
	public Map parse(String json, Charset charset) throws IOException {
		if (!init) {
			configure();
		}
		return objectMapper.readValue(json.getBytes(charset), Map.class);
	}

	@Override
	public Map parse(byte[] json) throws IOException {
		if (!init) {
			configure();
		}
		return objectMapper.readValue(json, Map.class);
	}

	private void configure() {
		if (!init) {
			SimpleModule module = new SimpleModule();
			module.addDeserializer(Map.class, new MapDeserializerWrapper(listeners, incompleteKeyName));
			objectMapper.registerModule(module);
			init = true;
		} else {
			throw new IllegalArgumentException("Not allowed to configure after use");
		}
	}

}
