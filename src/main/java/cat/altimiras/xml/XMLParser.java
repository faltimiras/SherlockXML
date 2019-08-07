package cat.altimiras.xml;

import cat.altimiras.xml.exceptions.InvalidXMLFormatException;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

public interface XMLParser<T> {

	/**
	 * Parses a XML to T
	 *
	 * @param xml
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	T parse(String xml) throws InvalidXMLFormatException, CharacterCodingException;

	T parse(String xml, Charset charset) throws InvalidXMLFormatException, CharacterCodingException;

	T parse(byte[] xml) throws InvalidXMLFormatException, CharacterCodingException;

	/**
	 * Register a TagListener to xml tag
	 *
	 * @param tag
	 * @param listner
	 */
	void register(String tag, TagListener listner);
}
