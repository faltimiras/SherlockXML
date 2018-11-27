package cat.altimiras.xml.obj;

import org.codehaus.stax2.XMLStreamReader2;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;


public class ContextFactory {
/*
	public static ArrayDeque<PrimitiveContext> primitiveContextsPool = new ArrayDeque<>(100);


	public static PrimitiveContext getPr(XMLStreamReader2 xmlStreamReader, ClassIntrospector classIntrospector, ArrayDeque<Context> contexts, Field field, Type type){
		if (primitiveContextsPool.isEmpty()){
			return new PrimitiveContext(xmlStreamReader, classIntrospector, contexts, field, type);
		}
		else {
			PrimitiveContext primitiveContext = primitiveContextsPool.getFirst();
			primitiveContext.tag = xmlStreamReader.getName().getLocalPart();
			primitiveContext.tagHash = primitiveContext.tag.hashCode();
			primitiveContext.classIntrospector = classIntrospector;
			primitiveContext.contexts = contexts;
			primitiveContext.type = type;
			primitiveContext.field = field;

			if (type != null) {
				//create the object. If it is a primitive, not neede. on setContent() sets the value
				if (ClassIntrospector.isPrimitive((Class) type)) {
					primitiveContext.object = null;
				}
				else {
					primitiveContext.object = classIntrospector.getInstance(type);
					primitiveContext.setAttributes(xmlStreamReader);
				}
			}


			contexts.addFirst(primitiveContext);

			return primitiveContext;


		}
	}


	public static void release(PrimitiveContext primitiveContext) {

		primitiveContextsPool.add(primitiveContext);
	}

*/


	private ContextFactory() {
	}

	public static Context get(XMLStreamReader2 xmlStreamReader, ClassIntrospector classIntrospector, ArrayDeque<Context> contexts) {
		String tag = xmlStreamReader.getName().getLocalPart();

		Context currentContext = contexts.peekFirst();

		if (currentContext instanceof ListPrimitiveContext) {

			Context bk = contexts.pollFirst();
			Context parent = contexts.peekFirst();
			Field field = classIntrospector.getField(parent.object.getClass(), tag);
			contexts.addFirst(bk);
			if (field != null
					&& !currentContext.tag.equals(tag)) { //if parent has current tag means no wrapped primitive list must be closed
				currentContext.close(xmlStreamReader);
				return getContext(xmlStreamReader, classIntrospector, contexts, field);
			}

			return new PrimitiveContext(xmlStreamReader, classIntrospector, contexts, null, currentContext.type);
		}


		if (currentContext instanceof ListContext) {


			ListContext currentListContext = (ListContext) currentContext;
			if (!tag.equals(currentListContext.clazz.getSimpleName())) { //If current tag is different form list's object type means that current list has finished.

				if (currentListContext.hasWrapper) {
					return new Context(xmlStreamReader, classIntrospector, contexts, null, currentListContext.clazz);
				}
				else {
					currentContext = currentContext.close(xmlStreamReader); // close current list

					Field field = classIntrospector.getField(currentContext.object.getClass(), tag);
					if (field == null) {
						//ignore object
						return new IgnoreContext(xmlStreamReader, classIntrospector, contexts);
					}
					else {
						return getContext(xmlStreamReader, classIntrospector, contexts, field);
					}
				}
			}
			else {
				return getContext(xmlStreamReader, classIntrospector, contexts, null, currentListContext.clazz);
			}

		}
		else {//parent is an object

			if (currentContext.object == null) {
				return getContext(xmlStreamReader, classIntrospector, contexts, null, currentContext.type);
			}
			else {
				if (tag.equals(currentContext.object.getClass().getSimpleName())) { //if element gonna create it is the same that we have at the moment means that we are in a list without wrapper
					currentContext = currentContext.close(xmlStreamReader); //close current context with completed object, and create a new one
					return getContext(xmlStreamReader, classIntrospector, contexts, null, ((ListContext) currentContext).clazz);
				}
				else {
					Field field = classIntrospector.getField(currentContext.object.getClass(), tag);
					if (field == null) {
						//ignore object
						return new IgnoreContext(xmlStreamReader, classIntrospector, contexts);
					}
					return getContext(xmlStreamReader, classIntrospector, contexts, field);
				}
			}
		}
	}

	private static Context getContext(XMLStreamReader2 xmlStreamReader, ClassIntrospector classIntrospector, ArrayDeque<Context> contexts, Field field) {
		if (ClassIntrospector.isList(field.getType())) {
			Class clazz = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
			return getListContext(xmlStreamReader, classIntrospector, contexts, field, clazz);
		}
		else {
			return getContext(xmlStreamReader, classIntrospector, contexts, field, field.getType());
		}
	}

	private static Context getListContext(XMLStreamReader2 xmlStreamReader, ClassIntrospector classIntrospector, ArrayDeque<Context> contexts, Field field, Type type) {

		Class clazz = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];

		if (ClassIntrospector.isPrimitive((Class) type)) {
			return new ListPrimitiveContext(xmlStreamReader, classIntrospector, contexts, (Class) type);
		}

		ListContext context = new ListContext(xmlStreamReader, classIntrospector, contexts, clazz);

		if (context.hasWrapper) {
			return context;
		}
		else {
			return getContext(xmlStreamReader, classIntrospector, contexts, field, clazz);
		}
	}

	private static Context getContext(XMLStreamReader2 xmlStreamReader, ClassIntrospector classIntrospector, ArrayDeque<Context> contexts, Field field, Type type) {

		if (ClassIntrospector.isPrimitive((Class) type)) {
			return new PrimitiveContext(xmlStreamReader, classIntrospector, contexts, field, type);
		}
		else {
			return new Context(xmlStreamReader, classIntrospector, contexts, field, type);
		}
	}
}
