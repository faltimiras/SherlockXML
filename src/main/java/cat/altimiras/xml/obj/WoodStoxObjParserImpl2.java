package cat.altimiras.xml.obj;

import cat.altimiras.xml.TagListener;
import cat.altimiras.xml.XMLElement;
import cat.altimiras.xml.XMLParser;
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
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WoodStoxObjParserImpl2<T extends XMLElement> implements XMLParser<T> {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	/**
	 * Listeners for tags. Notified every time a tag is totally processed (on close </..> tag)
	 */
	private Map<String, TagListener> listeners = null;

	/**
	 * Java object that is building from xml parsed.
	 */
	private T obj;

	/**
	 * Contains field definitions and instances to populate fast objects
	 */
	final private ClassIntrospector<T> classIntrospector;

	private Context currentContext;

	/**
	 * Stack with tags opened and still not closed
	 */
	final private ArrayDeque<Context> contexts = new ArrayDeque<>();

	final private int objHashCode;

	private boolean stop = false;

	final private Class<T> typeArgumentClass;

	public WoodStoxObjParserImpl2(Class<T> typeArgumentClass, ClassIntrospector<T> classIntrospector, boolean validationEnabled) throws IllegalAccessException, InstantiationException {
		this.classIntrospector = classIntrospector;

		this.typeArgumentClass = typeArgumentClass;
		this.objHashCode = classIntrospector.getClassHashCode(typeArgumentClass);
		this.xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
		this.xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, validationEnabled);
	}

	public WoodStoxObjParserImpl2(Class<T> typeArgumentClass, ClassIntrospector<T> classIntrospector) throws IllegalAccessException, InstantiationException {
		this(typeArgumentClass, classIntrospector, false);
	}

	@Override
	public T parse(String xml) throws InvalidXMLFormatException, CharacterCodingException {

		if (xml == null) {
			throw new NullPointerException();
		}
		return parse(xml, Charset.forName("UTF-8"));
	}

	@Override
	public T parse(String xml, Charset charset) throws InvalidXMLFormatException, CharacterCodingException {

		if (xml == null) {
			throw new NullPointerException();
		}
		return parse(xml.getBytes(charset));
	}

	@Override
	public T parse(byte[] xml) throws InvalidXMLFormatException, CharacterCodingException {

		if (xml == null) {
			throw new NullPointerException();
		}

		InputStream xmlInputStream = new ByteArrayInputStream(xml);
		XMLStreamReader2 xmlStreamReader;

		try {
			this.obj = typeArgumentClass.newInstance();
			xmlStreamReader = (XMLStreamReader2) xmlInputFactory.createXMLStreamReader(xmlInputStream);
		}
		catch (XMLStreamException e) {
			throw new CharacterCodingException();
		}
		catch (Exception e){
			throw new  RuntimeException("Impossible to initialize obj");
		}

		try {

			boolean found = lookForStartTag(xmlStreamReader);
			if (!found) {
				//nothing found
				return obj;
			}
			else {
				//first context creation
				currentContext = new Context(xmlStreamReader, classIntrospector, contexts, obj);
			}

			process(xmlStreamReader);
			return obj;
		}
		catch (XMLStreamException e) {
			flushIncomplete();
			return obj;
		}
		catch (NullPointerException e) {
			throw e;
		}
		catch (Exception  e) {
			throw new InvalidXMLFormatException("Impossible to parse XML. Msg:" + e.getMessage());
		}
		finally {
			this.contexts.clear();
			this.currentContext = null;
			this.stop = false;
		}

	}

	/**
	 * Flush to base object parsed is on the context but it could not be flushed due to XML is not correct and some tags hasn't been closed
	 */
	private void flushIncomplete() throws CharacterCodingException {

		while (!contexts.isEmpty()) {
			Context current = contexts.pollFirst();
			if (current != null) {
				Context parent = contexts.peek();
				if (parent != null) {
					Field f = classIntrospector.getField((Class) parent.type, current.tag);
					parent.setToObj(f, current.object);
				}
			}
		}

		obj.markAsIncomplete();
	}

	private void process(XMLStreamReader2 xmlStreamReader) throws XMLStreamException {
		//starting to parse nested elements
		while (xmlStreamReader.hasNext() && !stop && !contexts.isEmpty()) {
			int eventType = xmlStreamReader.next();
			switch (eventType) {
				case XMLEvent.START_ELEMENT:
					currentContext = ContextFactory2.get(xmlStreamReader, classIntrospector, contexts);
					break;
				case XMLEvent.CHARACTERS:
					String content = xmlStreamReader.getText().trim();
					if (!content.isEmpty()) {
						currentContext.setContent(content);
					}
					break;
				case XMLEvent.END_ELEMENT:
					currentContext = currentContext.close(xmlStreamReader);
					break;
				default:
					//do nothing
					break;
			}
		}
	}

	private boolean lookForStartTag(XMLStreamReader2 xmlStreamReader) throws Exception{
		while (xmlStreamReader.hasNext() && !stop) {
			int eventType = xmlStreamReader.next();
			if (eventType == XMLEvent.START_ELEMENT) {

				if (objHashCode ==  xmlStreamReader.getName().getLocalPart().hashCode()) {
					return true; //start point found
				}
			}
		}
		return false;
	}

	@Override
	public void register(String tag, TagListener listner) {
//TODO
	}

}