package cat.altimiras.xml;

import cat.altimiras.xml.exceptions.InvalidXMLFormatException;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WoodStoxParserImpl<T> implements XMLParser<T> {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	/**
	 * Listeners for tags. Notified every time a tag is totally processed (on close </..> tag)
	 */
	private Map<String, TagListener> listeners = null;

	/**
	 * Java object that is building from xml data.
	 */
	final private T obj;

	/**
	 * Contains field definitions and instances to populate fast objects
	 */
	final private ClassIntrospector<T> classIntrospector;

	private Context currentContext;

	/**
	 * Stack with tags opened and still not closed
	 */
	final private ArrayDeque<Context> contexts = new ArrayDeque<>();

	private boolean simpleElement = false; //inside simple element: int, float ...

	private Field currentField = null;

	final private int objHashCode;

	private boolean stop = false;


	public WoodStoxParserImpl(Class<T> typeArgumentClass, ClassIntrospector<T> classIntrospector) throws IllegalAccessException, InstantiationException {
		this.classIntrospector = classIntrospector;

		obj = typeArgumentClass.newInstance();
		objHashCode = obj.getClass().getSimpleName().hashCode();

		xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
	}

	@Override
	public T parse(String xml) throws InvalidXMLFormatException {

		if (xml == null) {
			throw new NullPointerException();
		}

		return parse(xml.getBytes());
	}

	@Override
	public T parse(byte[] xml) throws InvalidXMLFormatException {

		try {
			if (xml == null) {
				throw new NullPointerException();
			}

			InputStream xmlInputStream = new ByteArrayInputStream(xml);

			XMLStreamReader2 xmlStreamReader = (XMLStreamReader2) xmlInputFactory.createXMLStreamReader(xmlInputStream);
			while (xmlStreamReader.hasNext() && !stop) {
				String currentTagName;
				int eventType = xmlStreamReader.next();
				switch (eventType) {
					case XMLEvent.START_ELEMENT:

						currentTagName = xmlStreamReader.getName().getLocalPart();

						if (objHashCode != currentTagName.hashCode()) {

							if (currentContext == null) { //Object to parse is not the most outer element
								continue;
							}

							currentField = classIntrospector.getField(currentContext.object.getClass(), currentTagName);
						}

						if (currentField == null) {
							Object o;

							if (currentContext instanceof WoodStoxParserImpl.ListContext) {
								o = classIntrospector.getInstance(((ListContext) currentContext).clazz);
							}
							else {
								o = obj;
							}

							currentContext = new Context();
							currentContext.tag = currentTagName;
							currentContext.object = o;

							//attributes
							int attributeCount = xmlStreamReader.getAttributeCount();
							for (int i = 0; i < attributeCount; i++) {
								String attributeName = xmlStreamReader.getAttributeLocalName(i);
								Field f = classIntrospector.getField(currentContext.object.getClass(), attributeName);
								setToObj(currentContext.object, f, xmlStreamReader.getAttributeValue(i));
							}

							contexts.addFirst(currentContext);
							simpleElement = false;
						}
						else {

							if (currentField.getType().isAssignableFrom(String.class) ||
									currentField.getType().isAssignableFrom(Integer.TYPE) || currentField.getType().isAssignableFrom(Integer.class) ||
									currentField.getType().isAssignableFrom(Long.TYPE) || currentField.getType().isAssignableFrom(Long.class) ||
									currentField.getType().isAssignableFrom(Double.TYPE) || currentField.getType().isAssignableFrom(Double.class) ||
									currentField.getType().isAssignableFrom(Float.TYPE) || currentField.getType().isAssignableFrom(Float.class) ||
									currentField.getType().isAssignableFrom(Boolean.TYPE) || currentField.getType().isAssignableFrom(Boolean.class)) {
								simpleElement = true;
							}
							else if (currentField.getType().isAssignableFrom(ArrayList.class)) {
								ListContext context = new ListContext();
								context.tag = currentTagName;
								context.clazz = Class.forName((((ParameterizedType) currentField.getGenericType()).getActualTypeArguments()[0]).getTypeName());

								//initialize list
								List currentList = new ArrayList<>();
								currentField.setAccessible(true);
								currentField.set(currentContext.object, currentList);

								context.object = currentList;
								currentContext = context;

								contexts.addFirst(currentContext);

							}
							else {
								currentContext = new Context();
								currentContext.object = classIntrospector.getInstance(currentField.getType());
								currentContext.tag = currentTagName;

								//attributes
								int attributeCount = xmlStreamReader.getAttributeCount();
								for (int i = 0; i < attributeCount; i++) {
									String attributeName = xmlStreamReader.getAttributeLocalName(i);
									Field f = classIntrospector.getField(currentContext.object.getClass(), attributeName);
									setToObj(currentContext.object, f, xmlStreamReader.getAttributeValue(i));
								}

								contexts.addFirst(currentContext);
								simpleElement = false;

							}
						}
						break;
					case XMLEvent.CHARACTERS:

						String content = xmlStreamReader.getText();
						if (simpleElement) {
							setToObj(currentContext.object, currentField, content);
							stop = notify(currentField.getName(), content);
						}
						break;
					case XMLEvent.END_ELEMENT:

						currentTagName = xmlStreamReader.getName().getLocalPart();
						if (simpleElement) {
							simpleElement = false;
						}
						else {
							contexts.pollFirst();
							if (!contexts.isEmpty()) {
								Context parent = contexts.peek();
								Field f = classIntrospector.getField(parent.object.getClass(), currentTagName);
								setToObj(parent.object, f, currentContext.object);
								stop = notify(currentTagName, currentContext.object);
								currentContext = parent;
							}
						}

						break;
					default:
						//do nothing
						break;
				}
			}
		}

		catch (XMLStreamException e) {
			flushIncomplete();
		}
		catch (NullPointerException e) {
			throw e;
		}
		catch (RuntimeException | IllegalAccessException | ClassNotFoundException e) {
			throw new InvalidXMLFormatException("Impossible to parse XML. Msg:" + e.getMessage());
		}
		return obj;
	}

	@Override
	public void register(String tag, TagListener listener) {
		if (this.listeners == null) {
			this.listeners = new HashMap<>();
		}
		listeners.put(tag, listener);
	}

	/**
	 * Notify to a TagListener(if registered) when tag is closed
	 *
	 * @param tag
	 * @param value
	 *
	 * @return
	 */
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


	private void setToObj(Object obj, Field field, Object value) {

		try {
			if (obj instanceof List) {
				((List) obj).add(value);
			}
			else {
				if (field != null) {
					field.set(obj, convertTo(field, value));
				}
			}
		}
		catch (Exception e) {
			//ignore. If not exist, we just ignore it.
		}
	}

	private Object convertTo(Field field, Object value) {

		Class t = field.getType();

		if (t.isAssignableFrom(String.class)) {
			return value;
		}
		else if (t.isAssignableFrom(Integer.TYPE) || t.isAssignableFrom(Integer.class)) {
			return Integer.valueOf((String) value);
		}
		else if (t.isAssignableFrom(Long.TYPE) || t.isAssignableFrom(Long.class)) {
			return Long.valueOf((String) value);
		}
		else if (t.isAssignableFrom(Double.TYPE) || t.isAssignableFrom(Double.class)) {
			return Double.valueOf((String) value);
		}
		else if (t.isAssignableFrom(Float.TYPE) || t.isAssignableFrom(Float.class)) {
			return Float.valueOf((String) value);
		}
		else if (t.isAssignableFrom(Boolean.TYPE) || t.isAssignableFrom(Boolean.class)) {
			return Boolean.valueOf((String) value);
		}
		return value;
	}

	/**
	 * Flush to base object data is on the context but it could not be flushed due to XML is not correct and some tags hasn't been closed
	 */
	private void flushIncomplete() {

		Context nested = contexts.pollFirst();

		while (!contexts.isEmpty()) {
			Context current = contexts.pollFirst();
			Field field = classIntrospector.getField(current.object.getClass(), nested.tag);
			setToObj(current.object, field, nested.object);
			nested = current;
		}
	}

	private class Context {
		protected String tag;
		protected Object object;
	}

	private class ListContext extends Context {
		protected Class clazz;
	}
}