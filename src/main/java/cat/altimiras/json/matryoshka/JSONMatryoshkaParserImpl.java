package cat.altimiras.json.matryoshka;

import cat.altimiras.Parser;
import cat.altimiras.matryoshka.Matryoshka;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.nio.charset.Charset;

public class JSONMatryoshkaParserImpl extends Parser<Matryoshka> {

	private ObjectMapper objectMapper = new ObjectMapper();

	private boolean init = false;

	@Override
	public Matryoshka parse(String json) throws IOException {
		if (!init) {
			configure();
		}
		return objectMapper.readValue(json, Matryoshka.class);
	}

	@Override
	public Matryoshka parse(String json, Charset charset) throws IOException {
		if (!init) {
			configure();
		}
		return objectMapper.readValue(json.getBytes(charset), Matryoshka.class);
	}

	@Override
	public Matryoshka parse(byte[] json) throws IOException {
		if (!init) {
			configure();
		}
		return objectMapper.readValue(json, Matryoshka.class);
	}

	private void configure() {
		if (!init) {
			SimpleModule module = new SimpleModule();
			module.addDeserializer(Matryoshka.class, new MatryoshkaDeserializerWrapper(listeners));
			objectMapper.registerModule(module);
			init = true;
		} else {
			throw new IllegalArgumentException("Not allowed to configure after use");
		}
	}

}
