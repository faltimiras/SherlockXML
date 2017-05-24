package cat.altimiras.xml;

import cat.altimiras.xml.exceptions.BufferOverflowException;
import cat.altimiras.xml.exceptions.InvalidXMLFormatException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static cat.altimiras.xml.Tag.TagType.CLOSE;
import static cat.altimiras.xml.Tag.TagType.OPEN;
import static cat.altimiras.xml.Tag.TagType.SELF_CLOSED;

public class XMLParserImpl<T> implements XMLParser<T> {

	/**
	 * Stack with tags opened and still not closed
	 */
	final private Stack<Context> contexts = new Stack<>();

	/**
	 * Listeners for tags. Notified every time a tag is totally processed (on close </..> tag)
	 */
	private Map<String, TagListener> listeners = null;

	/**
	 * Java object that is building from xml data.
	 */
	final private T obj;

	/**
	 * Java class name object hased. For performance comparation
	 */
	final private byte[] objClassNameBytes;

	/**
	 * Current tag that is being processed information
	 */
	private Context currentContext;

	/**
	 * Tag that is currently ignored as it is not mapped in the Java class
	 */
	private Tag ignoringTag = null;

	/**
	 * If object has to be populated has been found or not. Used if obj do not correspond to base tag.
	 */
	private boolean found = false;

	/**
	 * Contains field definitions and instances to populate fast objectes
	 */
	private ClassIntrospector<T> classIntrospector;

	final private TagParser tagParser;

	public XMLParserImpl(Class<T> typeArgumentClass, ClassIntrospector<T> classIntrospector, int tagBufferSize) throws IllegalAccessException, InstantiationException {

		this.classIntrospector = classIntrospector;

		this.tagParser = new TagParser(tagBufferSize);

		obj = typeArgumentClass.newInstance();
		objClassNameBytes = obj.getClass().getSimpleName().getBytes();

		currentContext = new Context();
		currentContext.object = obj;
		currentContext.tag = new Tag(obj.getClass().getSimpleName().getBytes(), null, 0, OPEN, null, false, 0, 0);
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
	public T parse(String xmlStr) throws InvalidXMLFormatException, BufferOverflowException {
		if (xmlStr == null) {
			throw new NullPointerException();
		}
		return parse(xmlStr.getBytes());
	}

	@Override
	public T parse(byte[] xml) throws InvalidXMLFormatException, BufferOverflowException {

		if (xml == null) {
			throw new NullPointerException();
		}

		int cursor = 0;

		while (cursor < xml.length) {

			//extract tag to process
			Tag tag = tagParser.getTag(xml, cursor);
			if (tag == null) {
				break;
			}
			cursor = tag.getEndPosition();

			//check if tag must be ignored
			if (checkIgnoreTag(tag)) {
				continue;
			}

			if (Arrays.equals(tag.name,objClassNameBytes)) { //obj.getClass().getSimpleName().hashCode();
				if (tag.isOpening()) {
					setAttributes(obj, tag); //if parent obj has attributes
					continue;
				}
				else {
					break; //object has completely processed. Finish
				}
			}

			//Start to process tag
			if (tag.type == CLOSE) {
				if (!contexts.isEmpty()) {
					if (onCloseTag(contexts.pop(), tag, xml)) {
						break;
					}
				}
			}
			else if (tag.type == SELF_CLOSED) {

				if (!contexts.isEmpty()) {
					Boolean notify = onSelfClosedTag(contexts.peek(), tag);
					if (notify == null) {
						ignoringTag = tag;
					}
					else if (notify) {
						break;
					}
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
	 * Flush to base object data is on the context but it could not be flushed due to XML is not correct and some tags hasn't been closed
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
		int tagNameHash = tag.name.hashCode();
		if (!found && Arrays.equals(tag.name,objClassNameBytes)) { //obj.getClass().getSimpleName().hashCode();
			found = true;
			return false;
		}

		//skip tags at the beginning that don't care
		if (!found && !Arrays.equals(tag.name,objClassNameBytes)) { //obj.getClass().getSimpleName().hashCode();
			return true;
		}

		//if current tag is a closing and it is the same we are ignoring, stop ignoring and start to process next tag
		if (ignoringTag != null && Arrays.equals(tag.name,ignoringTag.name)  && tag.type == CLOSE) {
			ignoringTag = null;
			return true;
		}
		// if current ignoring tag is self-closed, just stop to ignore it. As it is self closed
		if (ignoringTag != null && ignoringTag.type == SELF_CLOSED) {
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
	private void setToObj(Object obj, byte[] fieldName, Object value) {
		try {
			if (obj instanceof List) {
				((List) obj).add(value);
			}
			else {
				if (fieldName != null) {
					Field field = classIntrospector.getField(obj.getClass(), fieldName);
					if (field != null) {
						field.set(obj, convertTo(field, value));
					}
				}
			}
		}
		catch (Exception e) {
			//ignore. If not exist, we just ignore it.
			//FIXME
			e.printStackTrace();
		}
	}

	/**
	 * Sets value to field on obj
	 *
	 * @param obj
	 * @param field
	 * @param value
	 */
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

	private Object convertTo(Field field, Object valueRaw) {

		Class t = field.getType();


		if (t.isAssignableFrom(String.class)) {
			return new String(new String((byte[])valueRaw));
		}
		else if (t.isAssignableFrom(Integer.TYPE) || t.isAssignableFrom(Integer.class)) {
			return Integer.valueOf((String) new String(new String((byte[])valueRaw)));
		}
		else if (t.isAssignableFrom(Long.TYPE) || t.isAssignableFrom(Long.class)) {
			return Long.valueOf((String) new String(new String((byte[])valueRaw)));
		}
		else if (t.isAssignableFrom(Double.TYPE) || t.isAssignableFrom(Double.class)) {
			return Double.valueOf((String) new String(new String((byte[])valueRaw)));
		}
		else if (t.isAssignableFrom(Float.TYPE) || t.isAssignableFrom(Float.class)) {
			return Float.valueOf((String) new String(new String((byte[])valueRaw)));
		}
		else if (t.isAssignableFrom(Boolean.TYPE) || t.isAssignableFrom(Boolean.class)) {
			return Boolean.valueOf((String) new String(new String((byte[])valueRaw)));
		}
		return valueRaw;
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
			for (int i = 0; i < tag.attributes.size(); i++) {
				setToObj(obj, tag.attributes.get(i).name, tag.attributes.get(i).value);
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

		Field field = classIntrospector.getField(context.object.getClass(), tag.name);

		Object object;
		if (field == null) {
			if (currentContext instanceof XMLParserImpl.ListContext) {
				object = classIntrospector.getInstance(((ListContext) currentContext).className);

				//set attributes to object just created
				setAttributes(object, tag);
				setToObj(context.object, field, object);
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
				object = classIntrospector.getInstance(field.getAnnotatedType().getType());

				//set attributes to object just created
				setAttributes(object, tag);
			}

			setToObj(context.object, field, object);
		}
		stop = notify(tag.name, object);
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
			byte[] content;
			int end = tag.getStartPosition() - tag.getEndContentOffset();
			int start = open.getEndPosition() + tag.getStartContentOffset();
			if (tag.cdata) { //remove cdata beginning and end
				//<![CDATA[".length= 9
				// ]]>.length = 3
				content = new byte[end - start - 12];
				System.arraycopy(xml,start + 9, content, 0,end - start - 12);
				//content = new String(xml, start + 9, end - start - 12); //-9 -3 = -12
			}
			else {
				//content = new String(xml, start, end - start);
				content = new byte[end - start];
				System.arraycopy(xml,start, content, 0,end - start);
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

		Field field = classIntrospector.getField(currentContext.object.getClass(), tag.name);

		try {
			if (field == null) {
				if (currentContext instanceof XMLParserImpl.ListContext) {
					context = new ObjectContext();
					((ObjectContext) context).object = classIntrospector.getInstance(((ListContext) currentContext).className);

					//set attributes to object just created
					setAttributes(context.object, tag);
				}
				else {
					//return null to ignore the tag
					return null;
				}
			}
			else {
				Class t = field.getType();
				if (t.isAssignableFrom(ArrayList.class)) {
					context = new ListContext();

					((ListContext) context).className = (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]).getTypeName().getBytes();
					//initialize list
					List currentList = new ArrayList<>();
					field.setAccessible(true);
					field.set(currentContext.object, currentList);

					context.object = currentList;
				}
				else if (t.isAssignableFrom(String.class)
						|| t.isAssignableFrom(Integer.class)
						|| t.isAssignableFrom(Long.class)
						|| t.isAssignableFrom(Double.class)) {
					context = new Context();
					context.object = currentContext.object;
				}
				else { //object
					context = new ObjectContext();
					context.object = classIntrospector.getInstance(field.getAnnotatedType().getType());

					//set attributes to object just created
					setAttributes(context.object, tag);

					setToObj(currentContext.object, field, context.object);
				}
			}
		}
		catch (IllegalAccessException e) {
			throw new InvalidXMLFormatException(String.format("Error parsing xml", e.getMessage()));
		}
		context.tag = tag;
		return context;
	}

	/**
	 * Notify to a TagListener(if registered) when tag is closed
	 *
	 * @param tag
	 * @param value
	 *
	 * @return
	 */
	private boolean notify(byte[] tag, Object value) {

		if (listeners == null) {
			return false;
		}

		TagListener listener = listeners.get(tag);
		if (listener != null) {
			return listener.notify(new String(tag), value);
		}
		return false; //to continue
	}

	private class Context {
		protected Tag tag;
		protected Object object;
	}

	private class ObjectContext extends Context {
	}

	private class ListContext extends ObjectContext {
		protected byte[] className;
	}
}