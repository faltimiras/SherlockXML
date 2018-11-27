package cat.altimiras.xml.obj;


import org.codehaus.stax2.XMLStreamReader2;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

class ListContext extends Context {

	protected Class clazz; //type of list objects
	protected boolean hasWrapper; //list is wrapped with a tag

	public ListContext(XMLStreamReader2 xmlStreamReader, ClassIntrospector classIntrospector, ArrayDeque<Context> contexts, Class clazz) {
		super(xmlStreamReader, classIntrospector, contexts, null, clazz);

		this.clazz = clazz;
		this.hasWrapper = !tag.equals(this.clazz.getSimpleName());
		this.object = new ArrayList<>();
	}

	@Override
	public Context close(XMLStreamReader2 xmlStreamReader) {
		this.contexts.pollFirst(); //remove myself from top of the stack
		if (!contexts.isEmpty()) {
			Context parent = contexts.peek();
			Field f = classIntrospector.getField((Class) parent.type, tag);
			parent.setToObj(f, this.object);
			//stop = notify(tag, currentContext.object);
			return parent;
		}
		return this;
	}

	@Override
	protected void setToObj(Field field, Object value) {
		((List) this.object).add(value);
	}

	@Override
	protected void setAttributes(XMLStreamReader2 xmlStreamReader) {
		//list don't have attributes, just ignore it.
	}
}
