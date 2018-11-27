package cat.altimiras.xml.obj;


import org.codehaus.stax2.XMLStreamReader2;

import java.lang.reflect.Field;
import java.util.ArrayDeque;

class IgnoreContext extends Context {

	public IgnoreContext(XMLStreamReader2 xmlStreamReader, ClassIntrospector classIntrospector, ArrayDeque<Context> contexts) {
		super(xmlStreamReader, classIntrospector, contexts, null, null);
	}

	@Override
	public Context close(XMLStreamReader2 xmlStreamReader) {
		String tag = xmlStreamReader.getName().getLocalPart();
		if (tag.hashCode()==this.tagHash) {
			contexts.pollFirst();
			return contexts.peek();
		}
		return this;
	}

	@Override
	protected void setToObj(Field field, Object value) {
		//nothing to do
	}

	@Override
	protected void setAttributes(XMLStreamReader2 xmlStreamReader) {
		//nothing to do
	}
}
