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
import java.lang.reflect.Type;
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
		objHashCode =  classIntrospector.getClassHashCode(typeArgumentClass); //obj.getClass().getSimpleName().hashCode();
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
				int eventType = xmlStreamReader.next();
				switch (eventType) {
					case XMLEvent.START_ELEMENT:
						onStartElement(xmlStreamReader);
						break;
					case XMLEvent.CHARACTERS:
						onContent(xmlStreamReader);
						break;
					case XMLEvent.END_ELEMENT:
						onCloseElement(xmlStreamReader);
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

	private void onCloseElement(XMLStreamReader2 xmlStreamReader) {
		String currentTagName = xmlStreamReader.getName().getLocalPart();

		//if it is a simple element or a list of simple elements just mark the end
		if (simpleElement) {
			simpleElement = false;
		}
		else {

			if (currentContext instanceof WoodStoxParserImpl.ListContext) {

				if (!currentTagName.equals(currentContext.tag)) {
					//when it is a list of primitives and  current tag(the closing one) is not the closing list tag, just ignore current closing tag.
					if (((ListContext) currentContext).isPrimitive) {
						return;
					}
					else {
						//when it is a list of obj. Current tag (closing one) is not the closing list tag, so obj context must be removed.
						if (!((ListContext) currentContext).hasWrapper) {
							apply(currentContext.tag);
						}

					}
				}
			}
			apply(currentTagName);
		}
	}

	private void apply(String tag){
		contexts.pollFirst();
		if (!contexts.isEmpty()) {
			Context parent = contexts.peek();
			Field f = classIntrospector.getField(parent.object.getClass(), tag);
			setToObj(parent.object, f, currentContext.object);
			stop = notify(tag, currentContext.object);
			currentContext = parent;
		}
	}

	private void onContent(XMLStreamReader2 xmlStreamReader) {
		String content = xmlStreamReader.getText();

		//Set content in the current object
		if (simpleElement) {
			setToObj(currentContext.object, currentField, content);
			stop = notify(currentField.getName(), content);
		}
		else if (currentContext instanceof WoodStoxParserImpl.ListContext) {
			if (!content.trim().isEmpty()) {
				setToObj(currentContext.object, currentField, convertTo(((ListContext) currentContext).clazz, content));
			}
		}
	}

	private void onStartElement(XMLStreamReader2 xmlStreamReader) throws ClassNotFoundException, IllegalAccessException {
		String currentTagName = xmlStreamReader.getName().getLocalPart();

		if (objHashCode != currentTagName.hashCode()) {
			if (currentContext == null) { //Object to parse is not the most outer element
				return;
			}

			currentField = classIntrospector.getField(currentContext.object.getClass(), currentTagName);
		}


		if (currentField == null) {
			atFirstElement(xmlStreamReader, currentTagName);
		}
		else {

			//if current field is a primitive type is not needed to create a context, at onContent value will be set
			if (ClassIntrospector.isPrimitive(currentField.getType())) {
				simpleElement = true;
			}
			//if current field is a list context list must be created with the list where elements will be added
			else if (ClassIntrospector.isList(currentField.getType())) {
				ListContext context = createCurrentListContext(currentTagName, currentContext.object);

				//when is not a primitive list and is not wrapped (current tag and current list class are not the same. first list object context must be created
				if (!context.isPrimitive && !context.hasWrapper) {

					createCurrentContext(currentTagName, classIntrospector.getInstance(((ListContext) currentContext).clazz));
					setAttributes(xmlStreamReader, currentContext.object);

				}
			}
			//if field it is an object, create the context with a new instantiation of his class
			else {
				createCurrentContext(currentTagName, classIntrospector.getInstance(currentField.getType()));

				//attributes
				setAttributes(xmlStreamReader, currentContext.object);
				simpleElement = false;
			}
		}
	}

	private ListContext createCurrentListContext(String currentTagName, Object o) throws IllegalAccessException, ClassNotFoundException {

		//initialize list and set it to the object
		List currentList;
		currentField.setAccessible(true);

		if (currentField.get(o) == null) {
			currentList = new ArrayList<>();
			currentField.set(o, currentList);
		}
		else {
			currentList = (List) currentField.get(o);
		}

		Type type = ((ParameterizedType) currentField.getGenericType()).getActualTypeArguments()[0];

		//create list context
		ListContext listContext = new ListContext();
		listContext.tag = currentTagName;
		listContext.object = currentList;

		listContext.clazz = Class.forName(type.getTypeName());
		listContext.isPrimitive = ClassIntrospector.isPrimitive((Class) type);
		listContext.hasWrapper = !currentTagName.equals(listContext.clazz.getSimpleName());

		currentContext = listContext;
		contexts.addFirst(currentContext);

		return listContext;
	}

	private void atFirstElement(XMLStreamReader2 xmlStreamReader, String currentTagName) {
		Object o;
		if (currentContext instanceof WoodStoxParserImpl.ListContext) {
			//if list of primitives is not needed to create a new context, primitives values will be added to the list directly
			if (((ListContext) currentContext).isPrimitive) {
				return;
			}
			o = classIntrospector.getInstance(((ListContext) currentContext).clazz);
		}
		else {
			o = obj;
		}

		//create new context
		createCurrentContext(currentTagName, o);

		setAttributes(xmlStreamReader, o);
		simpleElement = false;
	}

	private Context createCurrentContext(String tagName, Object o) {
		currentContext = new Context();
		currentContext.object = o;
		currentContext.tag = tagName;

		contexts.addFirst(currentContext);

		return currentContext;
	}

	private void setAttributes(XMLStreamReader2 xmlStreamReader, Object o) {
		//attributes
		int attributeCount = xmlStreamReader.getAttributeCount();
		for (int i = 0; i < attributeCount; i++) {
			String attributeName = xmlStreamReader.getAttributeLocalName(i);
			Field f = classIntrospector.getField(o.getClass(), attributeName);
			setToObj(o, f, xmlStreamReader.getAttributeValue(i));
		}
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
					field.set(obj, convertTo(field.getType(), value));
				}
			}
		}
		catch (Exception e) {
			//ignore. If not exist, we just ignore it.
		}
	}

	private Object convertTo(Class t, Object value) {

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
		protected Class clazz; //type of list objects
		protected boolean isPrimitive; //contains primitive objects
		protected boolean hasWrapper; //list is wrapped with a tag
	}
}