package cat.altimiras.xml;

import java.util.HashMap;
import java.util.Map;

public class XMLFactory {

	private static Map<String, ClassIntrospector> classesIntrospector = new HashMap<>();

	public static void init(Class... classes) throws Exception {

		if (classes == null || classes.length == 0) {
			throw new IllegalArgumentException("init parameters can not be empty");
		}

		for (Class c : classes) {
			if (classesIntrospector.get(c.getSimpleName()) == null) {
				ClassIntrospector classIntrospector = new ClassIntrospector(c);
				classesIntrospector.put(c.getSimpleName(), classIntrospector);
			}
		}
	}

	/**
	 * Get a parser for class c, with user defined buffer size
	 *
	 * @param c
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static XMLParser getParser(Class c) throws Exception {

		if (c == null){
			throw new IllegalArgumentException("Class can not be null");
		}

		ClassIntrospector classIntrospector = classesIntrospector.get(c.getSimpleName());
		if (classIntrospector == null) {
			throw new IllegalArgumentException("XMLFactory has not been properly initialzed. Class:" + c.getSimpleName() + ". Check init method");
		}
		return new WoodStoxParserImpl(c, classIntrospector);
	}

	static void reset() {
		classesIntrospector.clear();
	}

}
