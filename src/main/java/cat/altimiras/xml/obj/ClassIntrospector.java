package cat.altimiras.xml.obj;


import cat.altimiras.xml.XMLElement;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassIntrospector<T> {

	private Map<Long, Field> fields = new HashMap<>();

	private Map<Integer, XMLElement> instancesByClass = new HashMap<>();
	private Map<Integer, XMLElement> instancesByName = new HashMap<>();

	public ClassIntrospector(Class clazz) throws Exception {
		introspect(clazz);
	}

	public Field getField(Class clazz, String fieldName) {
		return fields.get(mergeHashCodes(fieldName, clazz));
	}

	public boolean hasField(Class clazz, String fieldName) {
		return fields.containsKey(mergeHashCodes(fieldName, clazz));
	}

	/**
	 * Get an new instance clonning the base instance created on construction time.
	 *
	 * @param clazz
	 *
	 * @return
	 */
	public XMLElement getInstance(Type clazz) {
		try {
			XMLElement base = instancesByClass.get(clazz.hashCode());
			if (base != null) {
				return (XMLElement) base.clone();
			}
		}
		catch (CloneNotSupportedException e) {
			//should never happen. Validation done on construction.
		}
		return null;
	}

	/**
	 * Get an new instance clonning the base instance created on construction time.
	 *
	 * @param className
	 *
	 * @return
	 */
	public XMLElement getInstance(String className) {
		try {
			XMLElement base = instancesByName.get(className.hashCode());
			if (base != null) {
				return (XMLElement) base.clone();
			}
		}
		catch (CloneNotSupportedException e) {
			//should never happen. Validation done on construction.
		}
		return null;
	}

	public int getClassHashCode(Class clazz) {
		XmlRootElement root = (XmlRootElement) clazz.getAnnotation(XmlRootElement.class);
		int hash;
		if (root == null) {
			hash = clazz.getSimpleName().hashCode();
		}
		else {
			hash = root.name().hashCode();
		}
		return hash;
	}

	private void introspect(Class clazz) throws Exception {

		if (!clazz.getGenericSuperclass().equals(XMLElement.class)) {
			throw new Exception("All classes MUST extend XMLElement. " + clazz.getName() + " do not.");
		}

		if (instancesByClass.containsKey(clazz.hashCode())) {
			return;
		}
		else {

			Integer hash = getClassHashCode(clazz);
			XMLElement element = (XMLElement) Class.forName(clazz.getName()).newInstance();
			instancesByClass.put(clazz.hashCode(), element);
			instancesByName.put(hash, element);

			Field[] fs = clazz.getDeclaredFields();
			for (Field field : fs) {

				javax.xml.bind.annotation.XmlElement fieldAnnotation = field.getAnnotation(XmlElement.class);
				String fieldName;
				if (fieldAnnotation == null) {
					fieldName = field.getName();
				}
				else {
					fieldName = fieldAnnotation.name();
				}


				field.setAccessible(true);

				//check primitives or simple objects
				if (isPrimitive(field.getType())) {
					fields.put(mergeHashCodes(fieldName, clazz), field);
				}
				else if (field.getType().isAssignableFrom(List.class)) {
					fields.put(mergeHashCodes(fieldName, clazz), field);
					if (!isPrimitive((Class) ((ParameterizedTypeImpl) field.getAnnotatedType().getType()).getActualTypeArguments()[0])) {
						introspect((Class) ((ParameterizedTypeImpl) field.getAnnotatedType().getType()).getActualTypeArguments()[0]);
					}
				}
				else {
					//recursive introspection
					fields.put(mergeHashCodes(fieldName, clazz), field);
					introspect(field.getType());
				}
			}
		}
	}

	private long mergeHashCodes(Object a, Object b) {
		return (((long) a.hashCode()) << 32) | (b.hashCode() & 0xffffffffL);
	}

	public static boolean isPrimitive(Class type) {
		return type.isAssignableFrom(String.class)
				|| type.isAssignableFrom(Integer.class)
				|| type.isAssignableFrom(Integer.TYPE)
				|| type.isAssignableFrom(Long.class)
				|| type.isAssignableFrom(Long.TYPE)
				|| type.isAssignableFrom(Double.class)
				|| type.isAssignableFrom(Double.TYPE)
				|| type.isAssignableFrom(Float.class)
				|| type.isAssignableFrom(Float.TYPE)
				|| type.isAssignableFrom(Boolean.class)
				|| type.isAssignableFrom(Boolean.TYPE);
	}

	public static boolean isList(Class type) {
		return type.isAssignableFrom(ArrayList.class);
	}
}
