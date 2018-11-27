package cat.altimiras.xml.obj;


import org.codehaus.stax2.XMLStreamReader2;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayDeque;

class PrimitiveContext extends Context {

	public PrimitiveContext(XMLStreamReader2 xmlStreamReader, ClassIntrospector classIntrospector, ArrayDeque<Context> contexts, Field field, Type type) {
		super(xmlStreamReader, classIntrospector, contexts, field, type);
	}

	@Override
	public boolean setContent(String content) {

		boolean stop = false;

		this.object = content;
		return stop;
	}

	@Override
	public Context close(XMLStreamReader2 xmlStreamReader) {
		this.contexts.pollFirst(); //remove myself from top of the stack
		if (!contexts.isEmpty()) {
			Context parent = contexts.peek();
			Field f = classIntrospector.getField((Class) parent.type, tag);
			parent.setToObj(f, this.object);
			//stop = notify(tag, currentContext.object);
			//ContextFactory.release(c);
			return parent;
		}
		return this;
	}

	@Override
	protected void setAttributes(XMLStreamReader2 xmlStreamReader) {
		//nothing to do
	}


}
