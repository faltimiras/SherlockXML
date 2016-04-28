package cat.altimiras.xml;

public interface XMLParser<T> {

    /**
     * Parses a XML to T
     *
     * @param xml
     * @return
     * @throws Exception
     */
    T parse(String xml) throws InvalidXMLFormatException, NullPointerException;

    T parse(byte[] xml) throws InvalidXMLFormatException, NullPointerException;

    /**
     * Register a TagListener to xml tag
     *
     * @param tag
     * @param listner
     */
    void register(String tag, TagListener listner);
}
