package cat.altimiras.xml;


import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public class ClassIntrospector<T> {

	private Map<Long, Field> fields = new HashMap<>();

    private Map<Integer, XMLElement> instancesByClass = new HashMap<>();
    private Map<Integer, XMLElement> instancesByName = new HashMap<>();

	public ClassIntrospector(Class clazz) throws Exception{
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
     * @param clazz
     * @return
     */
    public XMLElement getInstance(Type clazz){
        try {
            XMLElement base = instancesByClass.get(clazz.hashCode());
            if (base != null) {
                return (XMLElement)base.clone();
            }
        } catch (CloneNotSupportedException e){
            //should never happen. Validation done on construction.
        }
        return null;
    }

    /**
     * Get an new instance clonning the base instance created on construction time.
     * @param className
     * @return
     */
    public XMLElement getInstance(String className){
        try {
            XMLElement base = instancesByName.get(className.hashCode());
            if (base != null) {
                return (XMLElement)base.clone();
            }
        } catch (CloneNotSupportedException e){
            //should never happen. Validation done on construction.
        }
        return null;
    }

	private void introspect(Class clazz) throws Exception{

        if (!clazz.getGenericSuperclass().equals(XMLElement.class)){
            throw new Exception("All classes MUST extend XMLElement. " + clazz.getName() + " do not." );
        }

		if (instancesByClass.containsKey(clazz.hashCode())) {
			return;
		}
		else {
            XMLElement element = (XMLElement)Class.forName(clazz.getName()).newInstance();
            instancesByClass.put(clazz.hashCode(), element);
            instancesByName.put(clazz.getName().hashCode(), element);

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
	}

	private long mergeHashCodes(Object a, Object b) {
		return (((long)a.hashCode()) << 32) | (b.hashCode() & 0xffffffffL);
	}
}
