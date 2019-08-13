package cat.altimiras;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public abstract class Parser<T> {

	public final static String INCOMPLETE = "INCOMPLETE";

	/**
	 * Listeners for tags. Notified every time a tag is totally processed (on close </..> tag)
	 */
	protected Map<String, TagListener> listeners = null;

	/**
	 * Parses a unparsed to T
	 *
	 * @param content
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public abstract T parse(String content) throws IOException;

	public abstract T parse(String content, Charset charset) throws IOException;

	public abstract T parse(byte[] content) throws IOException;

	/**
	 * Register a TagListener to content tag
	 *
	 * @param tag
	 * @param listener
	 */
	//public abstract void register(String tag, TagListener listener);
	//@Override
	public void register(String tag, TagListener listener) {
		if (this.listeners == null) {
			this.listeners = new HashMap<>();
		}
		listeners.put(tag, listener);
	}
}
