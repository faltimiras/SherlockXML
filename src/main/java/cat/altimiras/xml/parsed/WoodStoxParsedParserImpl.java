package cat.altimiras.xml.parsed;

import cat.altimiras.xml.TagListener;
import cat.altimiras.xml.XMLParser;
import cat.altimiras.xml.exceptions.InvalidXMLFormatException;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLInputFactory;
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

public class WoodStoxParsedParserImpl implements XMLParser<Parsed> {

	private final XMLInputFactory2 xmlInputFactory;

	private Context currentContext;


	/**
	 * Stack with tags opened and still not closed
	 */
	final private ArrayDeque<Context> contexts = new ArrayDeque<>();

	public WoodStoxParsedParserImpl() {

		xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
	}


	public Parsed parse(String xml) throws InvalidXMLFormatException, CharacterCodingException {
		if (xml == null) {
			throw new NullPointerException();
		}
		return parse(xml, Charset.forName("UTF-8"));
	}

	public Parsed parse(String xml, Charset charset) throws InvalidXMLFormatException, CharacterCodingException {
		if (xml == null) {
			throw new NullPointerException();
		}
		return parse(xml.getBytes(charset));
	}

	public Parsed parse(byte[] xml) throws InvalidXMLFormatException, CharacterCodingException {

		if (xml == null) {
			throw new NullPointerException();
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
			while (xmlStreamReader.hasNext()) {
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
			//flushIncomplete();
		}
		catch (NullPointerException e) {
			throw e;
		}
		catch (Exception e) {
			//TODO
			e.printStackTrace();
			throw new InvalidXMLFormatException("Impossible to parse XML. Msg:" + e.getMessage());
		}

		if (currentContext == null || currentContext.data == null) {
			return new Parsed();
		}
		Map<String, Object> p = new HashMap<>();
		p.put(currentContext.tag, currentContext.getContent());
		return new Parsed(currentContext.tag, p);
	}


	private void onOpenElement(XMLStreamReader2 xmlStreamReader) throws Exception {

		String currentTagName = xmlStreamReader.getName().getLocalPart();

		Context context = new Context();
		context.tag = currentTagName;
		context.data = new HashMap<>();

		setAttributes(xmlStreamReader, context);

		contexts.push(context);
		currentContext = context;
	}


	private void onContent(XMLStreamReader2 xmlStreamReader) throws Exception {
		String content = xmlStreamReader.getText().trim();
		if (!content.isEmpty()) {
			currentContext.value = content;
		}
	}

	private void onCloseElement(XMLStreamReader2 xmlStreamReader) throws Exception {

		String currentTagName = xmlStreamReader.getName().getLocalPart();

		contexts.pop(); //remove current
		Context context = contexts.peek();
		if (context != null) {

			if (context.isList) {

				//check if element gonna insert is same time of pervious one
				String key = ((Map.Entry)((Map)context.list.get(0)).entrySet().iterator().next()).getKey().toString();
				if (key.equals(currentTagName)) {
					Map<String, Object> element = new HashMap<>();
					element.put(currentTagName, currentContext.getContent());
					context.list.add(element);
				}
				else { //if not move current list to a tag element
					context.data.put(key, context.list);

					//add new element
					context.data.put(currentContext.tag, currentContext.getContent());
				}
			}
			else {

				Object previous = context.data.get(currentTagName);
				if (previous != null) {

					context.isList = true;
					//remove previous element before convert it to a list
					context.data.remove(currentTagName);

					context.list = new ArrayList();
					Map<String, Object> element = new HashMap<>();
					element.put(currentTagName, previous);
					context.list.add(element);
					Map<String, Object> element2 = new HashMap<>();
					element2.put(currentTagName, currentContext.getContent());
					context.list.add(element2);


				}
				else {
					context.data.put(currentContext.tag, currentContext.getContent());
				}
			}
			currentContext = context;
		}

	}

	public void register(String tag, TagListener listener) {

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
		protected boolean isList = false;
		protected String value;
		protected List list;
		protected Map<String, Object> data;
/*
		public Object getContent() {
			if (value != null) {
				return value;
			}
			if (data != null && !data.isEmpty()) {
				return data;
			}
			return list;
		}
*/
		public Object getContent() {
			if (value != null) {
				return value;
			}
			if (!data.isEmpty()) {

				if (list== null){
					return data;
				}
				else {
					String key = ((Map.Entry)((Map)currentContext.list.get(0)).entrySet().iterator().next()).getKey().toString();
					data.put(key,list);
					list = null;
					return data;
				}

				//return data;
			}
			return list;
		}



/*
		public Object getContent() {

			if (value != null) {
				return value;
			}

			//only map set
			if (data != null && value == null && (list == null || list.isEmpty())){
				return data;
			}

			//only list set
			if(list != null && data == null){
				return list;
			}

			//map and list are set
			if(list != null && !list.isEmpty() && data != null && !data.isEmpty()){
				Map<String, Object> p = new HashMap<>();
				String key = ((Map.Entry)((Map)currentContext.list.get(0)).entrySet().iterator().next()).getKey().toString();
				data.put(key, list);
				p.put(tag, data);
				return p;
			}

			return value;

		}
*/
	}

}
