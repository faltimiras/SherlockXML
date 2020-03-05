package cat.altimiras;


public interface TagListener {

	/**
	 * If listener is registered for specific tag. When tag is closed. Listener is called with value.
	 *
	 * @param tag   called
	 * @param value
	 * @return true parsing must to stop otherwise continues
	 */
	boolean notify(String tag, Object value);

}
