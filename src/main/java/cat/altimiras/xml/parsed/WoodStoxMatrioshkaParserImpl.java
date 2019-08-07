package cat.altimiras.xml.parsed;

import cat.altimiras.matrioshka.Matrioshka;
import cat.altimiras.xml.TagListener;
import cat.altimiras.xml.XMLParser;
import cat.altimiras.xml.exceptions.InvalidXMLFormatException;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WoodStoxMatrioshkaParserImpl implements XMLParser<Matrioshka> {

	public final static String INCOMPLETE = "INCOMPLETE";

	private final XMLInputFactory2 xmlInputFactory;

	private Context currentContext;

	/**
	 * Listeners for tags. Notified every time a tag is totally processed (on close </..> tag)
	 */
	private Map<String, TagListener> listeners = null;

	/**
	 * Stack with tags opened and still not closed
	 */
	final private ArrayDeque<Context> contexts = new ArrayDeque<>();

	private boolean stop = false;

	public WoodStoxMatrioshkaParserImpl(XMLInputFactory2 xmlInputFactory) {
		this.xmlInputFactory = xmlInputFactory;
	}

	public Matrioshka parse(String xml) throws InvalidXMLFormatException, CharacterCodingException {
		if (xml == null) {
			throw new NullPointerException("xml can not be null");
		}
		return parse(xml, Charset.forName("UTF-8"));
	}

	public Matrioshka parse(String xml, Charset charset) throws InvalidXMLFormatException, CharacterCodingException {
		if (xml == null) {
			throw new NullPointerException("xml can not be null");
		}
		return parse(xml.getBytes(charset));
	}

	public Matrioshka parse(byte[] xml) throws InvalidXMLFormatException, CharacterCodingException {

		if (xml == null) {
			throw new NullPointerException("xml can not be null");
		}

		//cleans if had been a previous usage of this class
		contexts.clear();
		currentContext = null;

		InputStream xmlInputStream = new ByteArrayInputStream(xml);
		XMLStreamReader2 xmlStreamReader;

		try {
			xmlStreamReader = (XMLStreamReader2) xmlInputFactory.createXMLStreamReader(xmlInputStream);
		}
		catch (XMLStreamException e) {
			throw new CharacterCodingException();
		}

		try {
			while (xmlStreamReader.hasNext() && !stop) {
				int eventType = xmlStreamReader.next();
				switch (eventType) {
					case XMLEvent.START_ELEMENT:
						onOpenElement(xmlStreamReader);
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

			if (currentContext == null) {
				return new Matrioshka();
			}
			else {
				Matrioshka parsed = createMatrioshka();
				parsed.setMetadata(INCOMPLETE, true);
				return parsed;
			}

		}
		catch (NullPointerException e) {
			throw e;
		}
		catch (Exception e) {
			throw new InvalidXMLFormatException("Impossible to parse XML. Msg:" + e.getMessage());
		}
		finally {
			try {
				xmlInputStream.close();
				xmlStreamReader.close();
			}
			catch (Exception e) {
				//nothing to do
			}
		}

		if (currentContext == null || currentContext.data == null) {
			return new Matrioshka();
		}

		return createMatrioshka();
	}

	private Matrioshka createMatrioshka() {
		Map<String, Object> p = new HashMap<>();
		p.put(currentContext.tag, currentContext.getContent());
		return new Matrioshka(p);
	}

	private void flushIncomplete() {

		Context nested = contexts.pollFirst();

		while (!contexts.isEmpty()) {
			Context current = contexts.removeFirst();

			Object previous = current.data.get(nested.tag);
			if (previous == null) {
				current.data.put(nested.tag, nested.getContent());
			}
			else {
				//convert to list
				convertToList(nested.tag, current, previous, nested.getContent());
			}
			nested = current;
		}

		currentContext = nested;
	}

	private void onOpenElement(XMLStreamReader2 xmlStreamReader) throws Exception {

		String currentTagName = xmlStreamReader.getName().getLocalPart();
		Context context = new Context(currentTagName);
		setAttributes(xmlStreamReader, context);

		contexts.push(context);
		currentContext = context;
	}

	private void onContent(XMLStreamReader2 xmlStreamReader) throws Exception {
		String content = xmlStreamReader.getText().trim();
		if (!content.isEmpty()) {
			currentContext.value = content;
			stop = notify(currentContext.tag, content);
		}
	}

	private void onCloseElement(XMLStreamReader2 xmlStreamReader) throws Exception {

		String currentTagName = xmlStreamReader.getName().getLocalPart();


		contexts.removeFirst(); //remove current
		Context context = contexts.peekFirst();
		if (context != null) {

			if (context.isList) {

				//check if element gonna insert is the same type of previous one
				String key = context.getFirstElementName();
				if (key.equals(currentTagName)) {
					Element element = new Element(currentTagName, currentContext.getContent());
					context.list.add(element);
				}
				else { //if it isn't move current list to a tag element. This happens when there are 2 or mores lists unwrapped
					context.data.put(key, context.list);
					context.isList = false;
					//add new element
					context.data.put(currentContext.tag, currentContext.getContent());
				}
			}
			else {

				Object previous = context.data.get(currentTagName);
				if (previous != null) {
					convertToList(currentTagName, context, previous, currentContext.getContent());
				}
				else {
					context.data.put(currentContext.tag, currentContext.getContent());
				}
			}

			currentContext = context;

			stop = notify(currentTagName, currentContext.getContent());
		}
	}

	/**
	 * Converts data in a context to a list of data in same context
	 *
	 * @param currentTagName
	 * @param context
	 * @param previous
	 * @param content
	 */
	private void convertToList(String currentTagName, Context context, Object previous, Object content) {

		context.isList = true;

		//remove previous element before convert it to a list
		context.data.remove(currentTagName);

		context.list = new ArrayList();
		Element element = new Element(currentTagName, previous);
		context.list.add(element);
		Element element2 = new Element(currentTagName, content);
		context.list.add(element2);
	}

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

	private void setAttributes(XMLStreamReader2 xmlStreamReader, Context context) {
		int attributeCount = xmlStreamReader.getAttributeCount();

		for (int i = 0; i < attributeCount; i++) {
			String attName = xmlStreamReader.getAttributeLocalName(i);
			String attValue = xmlStreamReader.getAttributeValue(i);
			context.data.put(attName, attValue);
		}
	}

	private class Context {

		protected String tag;

		protected String value;

		protected boolean isList = false;
		protected List<Element> list;

		protected Element data;


		public Context(String tag) {
			this.tag = tag;
			this.data = new Element(tag);
		}

		public Object getContent() {
			if (value != null) {
				return value;
			}
			if (!data.isEmpty()) {

				if (list == null) {
					return data;
				}
				else {
					Map copy = new HashMap(data);
					copy.put(list.get(0).name, list);
					return copy;

				}
			}
			return list;
		}

		public String getFirstElementName() {
			return list == null ? null : list.get(0).name;
		}
	}

	private class Element extends HashMap<String, Object> {

		private String name;

		private Element(String name) {
			super();
			this.name = name;
		}

		private Element(String name, Object value) {
			this.name = name;
			put(name, value);
		}

	}
}