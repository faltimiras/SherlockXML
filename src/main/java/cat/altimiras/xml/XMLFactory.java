package cat.altimiras.xml;

public class XMLFactory {

    public static XMLParser getParser(Class c) throws Exception{
        return new XMLParserImpl<>(c.getClass());
    }

}
