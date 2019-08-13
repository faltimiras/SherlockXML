package cat.altimiras.xml;

import cat.altimiras.Parser;
import cat.altimiras.matryoshka.Matryoshka;
import cat.altimiras.xml.matryoshka.WoodStoxMatryoshkaParserImpl;
import cat.altimiras.xml.obj.ClassIntrospector;
import cat.altimiras.xml.obj.WoodStoxObjParserImpl;
import org.codehaus.stax2.XMLInputFactory2;

import javax.xml.stream.XMLInputFactory;
import java.util.HashMap;
import java.util.Map;

public class XMLFactory {

	private static Map<String, ClassIntrospector> classesIntrospector = new HashMap<>();

	private static XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();

	public static void init(Class... classes) throws Exception {

		if (classes == null || classes.length == 0) {
			throw new IllegalArgumentException("init parameters can not be empty");
		}

		for (Class c : classes) {
			if (classesIntrospector.get(c.getCanonicalName()) == null) {
				ClassIntrospector classIntrospector = new ClassIntrospector(c);
				classesIntrospector.put(c.getCanonicalName(), classIntrospector);
			}
		}

		configure(MODE.PERFORMANCE, MODE.CDATA_SUPPORT);

	}

	public static void configure(MODE... modes) {
		for (MODE mode : modes) {
			mode.apply(xmlInputFactory);
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
	public static Parser getParser(Class c) throws Exception {

		if (c == null) {
			throw new IllegalArgumentException("Class can not be null");
		}

		ClassIntrospector classIntrospector = classesIntrospector.get(c.getCanonicalName());
		if (classIntrospector == null) {
			throw new IllegalArgumentException("XMLFactory has not been properly initialzed. Class:" + c.getCanonicalName() + ". Check init method");
		}
		return new WoodStoxObjParserImpl(xmlInputFactory, c, classIntrospector);
	}

	public static Parser<Matryoshka> getParser() {
		return new WoodStoxMatryoshkaParserImpl(xmlInputFactory);
	}

	static void reset() {
		classesIntrospector.clear();
	}


	public enum MODE {
		PERFORMANCE {
			public void apply(XMLInputFactory2 xmlInputFactory) {
				xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, false);
				xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
				xmlInputFactory.setProperty(XMLInputFactory2.P_REPORT_PROLOG_WHITESPACE, false);
				xmlInputFactory.setProperty(XMLInputFactory2.P_PRESERVE_LOCATION, false);
				xmlInputFactory.setProperty(XMLInputFactory2.P_INTERN_NAMES, true);
				xmlInputFactory.setProperty(XMLInputFactory2.P_INTERN_NS_URIS, true);
				xmlInputFactory.setProperty(XMLInputFactory2.P_LAZY_PARSING, true);
			}
		},
		CDATA_SUPPORT {
			public void apply(XMLInputFactory2 xmlInputFactory) {
				xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
			}
		}, DTD_VALIDATION {
			public void apply(XMLInputFactory2 xmlInputFactory) {
				xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, true);
			}
		};

		public abstract void apply(XMLInputFactory2 xmlInputFactory);
	}

}
