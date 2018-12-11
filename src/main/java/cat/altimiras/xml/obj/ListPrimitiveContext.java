package cat.altimiras.xml.obj;


import org.codehaus.stax2.XMLStreamReader2;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

class ListPrimitiveContext extends Context {

	public ListPrimitiveContext(XMLStreamReader2 xmlStreamReader, ClassIntrospector classIntrospector, ArrayDeque<Context> contexts, Class clazz) {
		super(xmlStreamReader, classIntrospector, contexts, null, clazz);
		this.object = new ArrayList<>();
	}

	@Override
	public Context close(XMLStreamReader2 xmlStreamReader) {
		String tag = xmlStreamReader.getName().getLocalPart();
		if (tag.hashCode() != this.tagHash) {
			Context parent;

			Context primitiveListContext = this.contexts.pollFirst();
			parent = contexts.peek();
			Field f = classIntrospector.getField((Class) parent.type, primitiveListContext.tag);
			parent.setToObj(f, this.object);
			//stop = notify(tag, currentContext.object);

			if (tag.hashCode()==parent.tagHash) { //
				return parent.close(xmlStreamReader);
			}

			return parent;
		}
		return this;
	}

	@Override
	protected void setToObj(Field field, Object value) {
		((List) this.object).add(convertTo((Class) type, value));
	}

	@Override
	protected void setAttributes(XMLStreamReader2 xmlStreamReader) {
		//list don't have attributes, just ignore it.
	}

	@Override
	public boolean setContent(String content) {
		boolean stop = false;
		((List) this.object).add(convertTo((Class) type, content));
		return stop;
	}
}
