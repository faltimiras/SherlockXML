package cat.altimiras.xml;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class XMLParserImpl<T> implements XMLParser<T> {

	/**
	 * Stack with tags opened and still not closed
	 */
	private Stack<Context> contexts = new Stack<>();

	/**
	 * Listeners for tags. Notified every time a tag is totally processed (on close </..> tag)
	 */
	private Map<String, TagListener> listeners = null;

	/**
	 * Java object that is building from xml data.
	 */
	private T obj = null;

	/**
	 * Current tag that is being processed information
	 */
	private Context currentContext;

	/**
	 * Tag that is currently ignored as it is not mapped in the Java class
	 */
	private Tag ignoringTag = null;

	private boolean found = false;

	private ClassIntrospector<T> classIntrospector;

	public XMLParserImpl(Class<T> typeArgumentClass, ClassIntrospector<T> classIntrospector) throws Exception {

		this.classIntrospector = classIntrospector;

		obj = typeArgumentClass.newInstance();

		currentContext = new Context();
		currentContext.object = obj;
		currentContext.tag = new Tag(obj.getClass().getSimpleName(), 0, TagType.OPEN);
		contexts.push(currentContext);
	}

	@Override
	public void register(String tag, TagListener listener) {
		if (this.listeners == null) {
			this.listeners = new HashMap<>();
		}
		listeners.put(tag, listener);
	}

	@Override
	public T parse(String xmlStr) throws InvalidXMLFormatException, NullPointerException {
		if (xmlStr == null) {
			throw new NullPointerException();
		}
		return parse(xmlStr.getBytes());
	}

	//TODO support comments <!-- -->

	@Override
	public T parse(byte[] xml) throws InvalidXMLFormatException, NullPointerException {

		if (xml == null) {
			throw new NullPointerException();
		}

		int cursor = 0;

		while (cursor < xml.length) {
			//extract tag to process
			Tag tag = getTag(xml, cursor);
			if (tag == null) {
				break;
			}
			cursor = tag.getEndPosition();

			//check if tag must be ignored
			if (checkIgnoreTag(tag)) {
				continue;
			}

			if (tag.name.equals(obj.getClass().getSimpleName())) {
				if (tag.isOpening()) {
					setAttributes(obj, tag); //if parent obj has attributes
					continue;
				}
				else {
					break; //object has completely processed. Finish
				}
			}

			//Start to process tag
			if (tag.type == TagType.CLOSE) {
				if (onCloseTag(contexts.pop(), tag, xml)) {
					break;
				}
			}
			else if (tag.type == TagType.SELF_CLOSED) {

				Boolean notify = onSelfClosedTag(contexts.peek(), tag);
				if (notify == null) {
					ignoringTag = tag;
				}
				else if (notify) {
					break;
				}
			}
			else { //is open tag
				Context context = onOpenTag(currentContext, tag);
				if (context == null) { //if context is null, tag must be ignored
					ignoringTag = tag;
				}
				else {
					currentContext = context;
					contexts.push(context);
				}
			}
		}

		//Base object is always on the context. If there are more, perhaps there are values pending ot set to base object
		if (contexts.size() > 1) {
			flushIncomplete();
		}

		return obj;
	}

	/**
	 * Flush to base object data is on the context but it can not be flushed due to XML is not correct and some tags hasn't been closed
	 */
	private void flushIncomplete() {

		Context nested = contexts.pop();

		while (!contexts.isEmpty()) {
			Context current = contexts.pop();
			setToObj(current.object, current.tag.name, nested.object);
			nested = current;
		}
	}

	/**
	 * Check if tags should be ignored  or not.
	 *
	 * @param tag
	 *
	 * @return true if should continue ignoring, false otherwise
	 */
	private boolean checkIgnoreTag(Tag tag) {

		//found obj we want
		if (!found && tag.name.equals(obj.getClass().getSimpleName())) {
			found = true;
			return false;
		}

		//skip tags at the beginning that don't care
		if (!found && !tag.name.equals(obj.getClass().getSimpleName())) {
			return true;
		}

		//if current tag is a closing and it is the same we are ignoring, stop ignoring and start to process next tag
		if (ignoringTag != null && ignoringTag.name.equals(tag.name) && tag.type == TagType.CLOSE) {
			ignoringTag = null;
			return true;
		}
		// if current ignoring tag is self-closed, just stop to ignore it. As it is self closed
		if (ignoringTag != null && ignoringTag.type == TagType.SELF_CLOSED) {
			ignoringTag = null;
			return false;
		}
		//Otherwise continue ignoring tags
		if (ignoringTag != null) {
			return true;
		}

		return false;
	}

	/**
	 * Sets value to fieldName on obj
	 *
	 * @param obj
	 * @param fieldName
	 * @param value
	 */
	private void setToObj(Object obj, String fieldName, Object value) {
		try {
			if (obj instanceof List) {
				((List) obj).add(value);
			}
			else {
				Field field = getField(obj, fieldName);
				field.set(obj, convertTo(field, value));
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
		else if (t.isAssignableFrom(Integer.TYPE) || field.getType().isAssignableFrom(Integer.class)) {
			return Integer.valueOf((String) value);
		}
		else if (t.isAssignableFrom(Long.TYPE) || field.getType().isAssignableFrom(Long.class)) {
			return Long.valueOf((String) value);
		}
		else if (t.isAssignableFrom(Double.TYPE) || field.getType().isAssignableFrom(Double.class)) {
			return Double.valueOf((String) value);
		}
		else if (t.isAssignableFrom(Float.TYPE) || field.getType().isAssignableFrom(Float.class)) {
			return Float.valueOf((String) value);
		}
		else if (t.isAssignableFrom(Boolean.TYPE) || field.getType().isAssignableFrom(Boolean.class)) {
			return Boolean.valueOf((String) value);
		}
		return value;
	}

	/**
	 * Set Tag attributes to object if names match
	 *
	 * @param obj
	 * @param tag
	 *
	 * @throws InvalidXMLFormatException
	 */
	private void setAttributes(Object obj, Tag tag) {
		if (tag.attributes != null) {
			for (Attribute attribute : tag.attributes) {
				setToObj(obj, attribute.name, attribute.value);
			}
		}
	}

	/**
	 * Self closed tag <tag/> management
	 *
	 * @param context actual tag context
	 * @param tag     new tag read
	 *
	 * @return
	 *
	 * @throws InvalidXMLFormatException
	 */
	private Boolean onSelfClosedTag(Context context, Tag tag) throws InvalidXMLFormatException {
		boolean stop;

		try {
			Field field = getField(currentContext.object, tag.name);

			Object object;
			if (field == null) {
				//si el tag nou no esta dins obj actual, es un nou obj
				if (currentContext instanceof XMLParserImpl.ListContext) {
					object = Class.forName(((ListContext) currentContext).className).newInstance();
				}
				else {
					//return null to ignore tag
					return null;
				}
			}
			else {

				if (field.getType().isAssignableFrom(List.class)) {
					object = new ArrayList<>();
				}
				else {
					object = Class.forName(field.getAnnotatedType().getType().getTypeName()).newInstance();
				}
			}

			//set attributes to object just created
			setAttributes(object, tag);
			setToObj(context.object, tag.name, object);

			stop = notify(tag.name, object);

		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new InvalidXMLFormatException(tag.name + " do not exist");
		}

		return stop;
	}

	/**
	 * Set Obj/list/field to proper object.
	 *
	 * @param context actual tag context
	 * @param tag     new tag read
	 * @param xml
	 *
	 * @return true, stop parsing otherwise false
	 *
	 * @throws InvalidXMLFormatException
	 */
	private Boolean onCloseTag(Context context, Tag tag, byte[] xml) {
		Tag open = context.tag;
		boolean stop;


		if (context instanceof XMLParserImpl.ObjectContext) { //object or list
			Context parent = contexts.peek();
			setToObj(parent.object, context.tag.name, context.object);
			currentContext = parent;

			stop = notify(tag.name, context.object);
		}
		else { //is String | Integer | Double | Long
			String content;
			if (tag.cdata) { //remove cdata beginning and end
				//<![CDATA[".length= 9
				// ]]>.length = 3
				String dirty = new String(xml, open.getEndPosition(), tag.getStartPosition() - open.getEndPosition()).trim();
				content = dirty.substring(9, dirty.length() - 3);
			}
			else {
				content = new String(xml, open.getEndPosition(), tag.getStartPosition() - open.getEndPosition()).trim();
			}

			setToObj(context.object, context.tag.name, content);
			currentContext = context;

			stop = notify(tag.name, content);
		}


		return stop;
	}

	/**
	 * Updates currentContext with new tag
	 *
	 * @param currentContext actual tag context
	 * @param tag            new tag read
	 *
	 * @return new context, or null if tag is unknown, so to ignore
	 *
	 * @throws InvalidXMLFormatException
	 */
	private Context onOpenTag(Context currentContext, Tag tag) throws InvalidXMLFormatException {
		Context context;

		Field field = getField(currentContext.object, tag.name);

		try {
			if (field == null) {
				//si el tag nou no esta dins obj actual, es un nou obj
				if (currentContext instanceof XMLParserImpl.ListContext) {
					context = new ObjectContext();
					((ObjectContext) context).object = Class.forName(((ListContext) currentContext).className).newInstance();

					//set attributes to object just created
					setAttributes(context.object, tag);
				}
				else {
					//return null to ignore the tag
					return null;
				}
			}
			else {

				if (field.getType().isAssignableFrom(ArrayList.class)) {
					context = new ListContext();

					((ListContext) context).className = (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]).getTypeName();
					//initialize list
					List currentList = new ArrayList<>();
					field.setAccessible(true);
					field.set(currentContext.object, currentList);

					context.object = currentList;
				}
				else if (field.getType().isAssignableFrom(String.class)
						|| field.getType().isAssignableFrom(Integer.class)
						|| field.getType().isAssignableFrom(Long.class)
						|| field.getType().isAssignableFrom(Double.class)) {
					context = new Context();
					context.object = currentContext.object;
				}
				else { //object
					context = new ObjectContext();
					context.object = Class.forName(field.getAnnotatedType().getType().getTypeName()).newInstance();

					//set attributes to object just created
					setAttributes(context.object, tag);

					setToObj(currentContext.object, tag.name, context.object);
				}
			}
		}
		catch (ClassNotFoundException e) {
			throw new InvalidXMLFormatException(String.format("No class for %s", field.getAnnotatedType().getType().getTypeName()));
		}
		catch (IllegalAccessException e) {
			throw new InvalidXMLFormatException(String.format("Error parsing xml", e.getMessage()));
		}
		catch (InstantiationException e) {
			throw new InvalidXMLFormatException(String.format("Error instantiation %s. Empty constructor needed.", field.getAnnotatedType().getType().getTypeName()));
		}

		context.tag = tag;
		return context;
	}

	/**
	 * Return field with name <name> if exist on obj. Null otherwise.
	 *
	 * @param obj
	 * @param name
	 *
	 * @return
	 */
	private Field getField(Object obj, String name) {
		return classIntrospector.getField(obj.getClass(), name);
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

	/**
	 * Looks for next tag on xml
	 *
	 * @param xml    xml content
	 * @param cursor until has been parsed
	 *
	 * @return Tag
	 */
	private Tag getTag(byte[] xml, int cursor) {

		TagType tagType = null;

		int startTag = cursor;
		int namespacePosition = 0;
		boolean in = false; //inside tag
		boolean inAtt = false; //there are attributes
		boolean space = false; //space processed on tag declaration
		boolean inCDATA = false; //processing a cdata
		boolean cdata = false; //contains cdata content

		while (cursor < xml.length) {

			if (xml[cursor] == '<' && cursor + 1 < xml.length && xml[cursor + 1] == '/' && !inCDATA) {
				startTag = cursor + 1;
				in = true;
				tagType = TagType.CLOSE;
			}
			else if (xml[cursor] == '<' && !inCDATA) {
				startTag = cursor;
				in = true;
				tagType = TagType.OPEN;
			}

			if (in && xml[cursor] == '>' && !inCDATA) {
				if (inAtt) {
					return getTagWithAttributes(Arrays.copyOfRange(xml, startTag + 1, cursor), cursor + 1, namespacePosition - startTag, TagType.OPEN, cdata);
				}
				else {
					return new Tag(xml, startTag + 1, cursor - startTag - 1, namespacePosition, cursor + 1, tagType, cdata);
				}
			}
			else if (in && xml[cursor] == '/' && cursor + 1 < xml.length && xml[cursor + 1] == '>' && !inCDATA) {
				if (tagType == TagType.OPEN) {
					if (inAtt) {
						return getTagWithAttributes(Arrays.copyOfRange(xml, startTag + 1, cursor), cursor + 1, namespacePosition - startTag, TagType.SELF_CLOSED, false);
					}
					else {
						return new Tag(xml, startTag + 1, cursor - startTag - 1, namespacePosition, cursor + 1, TagType.SELF_CLOSED, false); //cdata can not be in an attribute
					}
				}
				else {
					//return new Tag(new String(xml, startTag + 2, cursor - startTag - 1), cursor + 1, tagType);
					return new Tag(xml, startTag + 1, cursor - startTag - 1, namespacePosition, cursor + 1, tagType, cdata);
				}
			}
			else if (in && xml[cursor] == '=' && !inCDATA) { //if there is an equal is -> there is/are attributes
				inAtt = true;
			}
			else if (in && !space && xml[cursor] == ':') { // there is a namespace (: detected and no space has been processed
				namespacePosition = cursor;
			}
			else if (in && xml[cursor] == ' ' && !inCDATA) {
				space = true;
			}
			else if (in && xml[cursor] == '<' && !inCDATA) { //<![CDATA[. detection

				//check if cdata
				if (xml.length > cursor + 8  //"<![CDATA[".lenght= 9 ('<' already processed)
						&& xml[cursor + 1] == '!'
						&& xml[cursor + 2] == '['
						&& xml[cursor + 3] == 'C'
						&& xml[cursor + 4] == 'D'
						&& xml[cursor + 5] == 'A'
						&& xml[cursor + 6] == 'T'
						&& xml[cursor + 7] == 'A'
						&& xml[cursor + 8] == '['
						) {
					inCDATA = true;
					cdata = true;
					cursor += 8;
				}
			}
			else if (in && inCDATA && xml[cursor] == ']') { //out of cdata detection

				if (xml.length > cursor + 2  //"]]>".lenght= 3 (']' already processed)
						&& xml[cursor + 1] == ']'
						&& xml[cursor + 2] == '>'
						) {
					inCDATA = false;
					cursor += 2;
				}
			}

			cursor++;
		}
		return null;
	}

	private Tag getTagWithAttributes(byte[] tagDeclaration, int generalCursor, int namespacePos, TagType tagType, boolean cdata) {

		int cursor = 0; //skip first <
		int startAttName = 0;
		int startAttValue = 0;
		int quotes = 0;
		boolean endName = false;

		Tag tag = new Tag();
		tag.position = generalCursor;
		tag.attributes = new ArrayList<>(5);
		tag.type = tagType;
		tag.cdata = cdata;

		Attribute att = null;

		while (cursor < tagDeclaration.length) {

			if (!endName && tagDeclaration[cursor] == ' ') { //end of tag name

				if (namespacePos <= 0) { //if selfClosed with no namespace => namespace < 0
					tag.name = new String(tagDeclaration, 0, cursor);
				}
				else {
					tag.name = new String(tagDeclaration, namespacePos, cursor - namespacePos);
					tag.namespace = new String(tagDeclaration, 0, namespacePos - 1);
				}

				endName = true;
				startAttName = cursor;
			}
			else if (tagDeclaration[cursor] == '=' && quotes == 0) { //ends attribute name, starts attribute value and not inside attribute value
				att = new Attribute();
				att.name = new String(tagDeclaration, startAttName, cursor - startAttName).trim();
				startAttValue = cursor;
			}
			else if (att != null && tagDeclaration[cursor] == '"' && quotes == 1) { //find end of attribute value
				att.value = new String(tagDeclaration, startAttValue + 2, cursor - startAttValue - 2);
				tag.attributes.add(att);

				//reset counters for next Attribute
				att = null;
				quotes = 0;
				startAttName = cursor + 1; //skip ", +1
			}
			else if (tagDeclaration[cursor] == '"') {
				quotes++;
			}

			cursor++;
		}

		return tag;
	}

	private class Context {
		protected Tag tag;
		protected Object object;
	}

	private class ObjectContext extends Context {
	}

	private class ListContext extends ObjectContext {
		protected String className;
	}

	private class Attribute {
		private String name;
		private String value;
	}

	private enum TagType {OPEN, CLOSE, SELF_CLOSED}

	private class Tag {

		private String name;
		private int position;
		private TagType type;
		private List<Attribute> attributes;
		private String namespace;
		private boolean cdata;

		public Tag() {
		}

		public Tag(String name, int position, TagType type) {
			this.position = position;
			this.name = name.trim();
			this.type = type;
		}

		//return new Tag(new String(xml, startTag + 1, cursor - startTag - 1), cursor + 1, tagType);
		public Tag(byte[] xml, int startPos, int endPos, int namespacePos, int position, TagType type, boolean cdata) {
			this.position = position;
			this.type = type;
			this.cdata = cdata;
			if (namespacePos == 0) {
				this.name = new String(xml, startPos, endPos).trim();
			}
			else {
				int namespaceLengh = namespacePos - startPos + 1;
				this.name = new String(xml, startPos + namespaceLengh, endPos - namespaceLengh).trim();
				this.namespace = new String(xml, startPos, namespacePos - startPos).trim();
			}
		}

		public int getStartPosition() {
			if (namespace == null) {
				return position - name.length() - 3; // 3 = <(1= /(2)>(3)
			}
			else {
				return position - name.length() - 3 - namespace.length() - 1; // 3 = <(1= /(2)>(3)
			}
		}

		public int getEndPosition() {
			return position;
		}

		public boolean isOpening() {
			return type == TagType.OPEN || type == TagType.SELF_CLOSED;
		}

		public boolean isClosing() {
			return type == TagType.CLOSE || type == TagType.SELF_CLOSED;
		}

		public boolean hasNamespace() {
			return namespace != null;
		}
	}

}
