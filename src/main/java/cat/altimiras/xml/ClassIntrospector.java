package cat.altimiras.xml;


import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ClassIntrospector<T> {

	private Map<Long, Field> fields = new HashMap<>();

	public ClassIntrospector(Class clazz) {
		introspect(clazz);
	}

	public Field getField(Class clazz, String fieldName) {
		return fields.get(mergeHashCodes(fieldName, clazz));
	}

	public boolean hasField(Class clazz, String fieldName) {
		return fields.containsKey(mergeHashCodes(fieldName, clazz));
	}

	private void introspect(Class clazz) {

		if (fields.containsKey((long)clazz.hashCode())) {
			return;
		}
		else {
			//just keep class name to stop introspection if obj is repeated on obj tree. Clean after instrospection process
			fields.put((long)clazz.hashCode(), null);

			Field[] fs = clazz.getDeclaredFields();
			for (Field field : fs) {

				field.setAccessible(true);

				//check primitives or simple objects
				if (field.getType().isAssignableFrom(String.class)
						|| field.getType().isAssignableFrom(Integer.class)
						|| field.getType().isAssignableFrom(Integer.TYPE)
						|| field.getType().isAssignableFrom(Long.class)
						|| field.getType().isAssignableFrom(Long.TYPE)
						|| field.getType().isAssignableFrom(Double.class)
						|| field.getType().isAssignableFrom(Double.TYPE)
						|| field.getType().isAssignableFrom(Float.class)
						|| field.getType().isAssignableFrom(Float.TYPE)
						|| field.getType().isAssignableFrom(Boolean.class)
						|| field.getType().isAssignableFrom(Boolean.TYPE)) {
					fields.put(mergeHashCodes(field.getName(), clazz), field);
				}
				else if (field.getType().isAssignableFrom(List.class)) {
					fields.put(mergeHashCodes(field.getName(), clazz), field);
					introspect((Class) ((ParameterizedTypeImpl) field.getAnnotatedType().getType()).getActualTypeArguments()[0]);
				}
				else {
					//recursive introspection
					fields.put(mergeHashCodes(field.getName(), clazz), field);
					introspect(field.getType());
				}
			}
		}

		//cleans entries stores just to keep
		clean();
	}

	private long mergeHashCodes(Object a, Object b) {
		return (((long)a.hashCode()) << 32) | (b.hashCode() & 0xffffffffL);
	}

	private void clean() {

		if (fields != null && !fields.isEmpty()){

			Iterator it = fields.values().iterator();
			while (it.hasNext()){
				Object o = it.next();
				if (o == null) {
					it.remove();
				}
			}
		}
	}
}
