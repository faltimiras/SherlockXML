package cat.altimiras.xml.obj;


import org.codehaus.stax2.XMLStreamReader2;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayDeque;

class Context {

	protected ArrayDeque<Context> contexts;

	protected ClassIntrospector classIntrospector;

	protected String tag;

	protected int tagHash;

	protected Object object;

	/**
	 * Field to set this context to his parent
	 */
	protected Field field;

	protected Type type;

	protected boolean primitive = false;

	/**
	 * Creation of most outsiding element
	 *
	 * @param xmlStreamReader
	 * @param classIntrospector
	 * @param contexts
	 * @param o
	 */
	public Context(XMLStreamReader2 xmlStreamReader, ClassIntrospector classIntrospector, ArrayDeque<Context> contexts, Object o) {
		this.tag = xmlStreamReader.getName().getLocalPart();
		this.tagHash = this.tag.hashCode();
		this.classIntrospector = classIntrospector;
		this.contexts = contexts;
		this.object = o;
		this.type = o.getClass();

		setAttributes(xmlStreamReader);
		contexts.addFirst(this);

	}

	/**
	 * Context for "internal" new tags. Most outsiding tag is created by other construcotr, then context is never empty
	 *
	 * @param xmlStreamReader
	 * @param classIntrospector
	 * @param contexts
	 */
	public Context(XMLStreamReader2 xmlStreamReader, ClassIntrospector classIntrospector, ArrayDeque<Context> contexts, Field field, Type type) {
		this.tag = xmlStreamReader.getName().getLocalPart();
		this.tagHash = this.tag.hashCode();
		this.classIntrospector = classIntrospector;
		this.contexts = contexts;
		this.type = type;
		this.field = field;

		if (type != null) {
			//create the object. If it is a primitive, not needed. on setContent() sets the value
			if (ClassIntrospector.isPrimitive((Class) type)) {
				this.object = null;
			}
			else {
				this.object = classIntrospector.getInstance(type);
				setAttributes(xmlStreamReader);
			}
		}

		contexts.addFirst(this);
	}


	public boolean setContent(String content) {

		boolean stop = false;

		setToObj(field, content);
		//stop = notify(field.getName(), content);
		return stop;
	}

	public Context close(XMLStreamReader2 xmlStreamReader) {

		if (primitive) {
			primitive = false;

		}
		else {
			contexts.pollFirst();
			if (!contexts.isEmpty()) {
				Context parent = contexts.peek();
				Field f = classIntrospector.getField((Class) parent.type, tag);
				parent.setToObj(f, this.object);
				//stop = notify(tag, currentContext.object);
				return parent;
			}
		}
		return this;

	}

	protected void setAttributes(XMLStreamReader2 xmlStreamReader) {
		//attributes
		try {
			int attributeCount = xmlStreamReader.getAttributeCount();
			for (int i = 0; i < attributeCount; i++) {
				String attributeName = xmlStreamReader.getAttributeLocalName(i);
				Field f = classIntrospector.getField((Class) this.type, attributeName);
				if (f != null) {
					f.set(this.object, convertTo(f.getType(), xmlStreamReader.getAttributeValue(i)));
				}
			}
		}
		catch (Exception e) {
			//ignore. If not exist, we just ignore it.
		}
	}

	protected void setToObj(Field field, Object value) {
		try {
			field.set(this.object, convertTo(field.getType(), value));
		}
		catch (Exception e) {
			//ignore. If not exist, we just ignore it.
		}
	}

	protected Object convertTo(Class t, Object value) {

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
	 * Notify to a TagListener(if registered) when tag is closed
	 *
	 * @param tag
	 * @param value
	 *
	 * @return
	 */
	protected boolean notify(String tag, Object value) {
/*
		if (listeners == null) {
			return false;
		}

		TagListener listener = listeners.get(tag);
		if (listener != null) {
			return listener.notify(tag, value);
		}
		return false; //to continue
		*/
		return false;
	}


}
