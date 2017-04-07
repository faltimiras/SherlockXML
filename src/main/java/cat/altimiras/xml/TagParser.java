package cat.altimiras.xml;

import java.util.ArrayList;
import java.util.List;

class TagParser {

	/**
	 * Buffer to keep bytes until becomes an tag name, attribute name or attribute value
	 */
	private byte[] buffer = new byte[300]; //this is a hard limit. tag name, attributes names and attributes values can not be longer than 300.

	/**
	 * Position until data is valid on buffer
	 */
	private int bufferCursor = 0;

	protected Tag getTag(byte[] xml, int cursor) {

		Tag.TagType tagType = null;
		boolean in = false; //inside tag
		boolean inCDATA = false; //processing a cdata
		boolean cdata = false; //tag contains cdata content
		int quotes = 0;

		Attribute attribute = null;
		List<Attribute> attributes = null;

		String name = null;
		String namespace = null;

		bufferCursor = 0;

		int contentCursor = 0;
		int startContentCursor = 0;
		int endContentCursor = 0;
		boolean contentFound = false;

		byte val;
		while (cursor < xml.length) {

			val = xml[cursor];

			if (in) {
				if (val == '>') { //detecting close tag definition
					if (name == null) {
						name = new String(buffer, 0, bufferCursor);
					}
					return new Tag(name, namespace, cursor + 1, tagType, attributes, cdata, startContentCursor,  endContentCursor);
				}
				if (val == '/' && cursor + 1 < xml.length && xml[cursor + 1] == '>') { //detecting close tag
					//name = new String(buffer, 1, bufferCursor);
					if (name == null) {
						name = new String(buffer, 0, bufferCursor);
					}
					return new Tag(name, namespace, cursor + 1, Tag.TagType.SELF_CLOSED, attributes, cdata, 0 ,0);
				}

				//parsing characters in tag definition
				if (val == ' ' && quotes != 1) {
					if (name == null) { //first space, until here tag name defined
						name = new String(buffer, 0, bufferCursor);
						bufferCursor = 0;
						quotes = 0;
					}
				}
				else if (val == '"') {
					quotes++;

					if (attribute != null && quotes == 2) {
						attribute.value = new String(buffer, 0, bufferCursor);
						bufferCursor = 0;
						attributes.add(attribute);
						attribute = null;
						quotes = 0;
					}
				}
				else if (val == '=' && quotes == 0) { //attribute detected and it is not inside defining attribute name or value

					if (attributes == null) {
						attributes = new ArrayList<>(5);
					}

					attribute = new Attribute();
					attribute.name = new String(buffer, 0, bufferCursor);
					bufferCursor = 0;

					quotes = 0;

				}
				else if (val == ':') { //namespace
					if (namespace == null) {
						namespace = new String(buffer, 0, bufferCursor);
						bufferCursor = 0;
					}
				}
				else if (val == '/') {
					//do nothing, just to not store it in the buffer
				}
				else { //store on buffer (only non special characters)
					buffer[bufferCursor] = val;
					bufferCursor++;
				}
			}
			else {
				//not in tag definition. On content definition
				if (!inCDATA) {
					if (val == '\n') {
						contentCursor++;
						cursor++;
						continue;
					} else if (val == ' ') { //discard spaces between
						contentCursor++;
						cursor++;
						continue;
					} else {
						if (contentFound) { //content found previously
							if (contentCursor != 0) {
								endContentCursor= contentCursor;
								contentCursor = 0;
							}
						} else {
							startContentCursor = contentCursor;
							contentCursor = 0;
							contentFound = true;
						}

					}

					//detecting tag definitions
					if (val == '<' && cursor + 1 < xml.length && xml[cursor + 1] == '/') { //check is closing tag definition
						//startTag = cursor + 1;
						in = true;
						tagType = Tag.TagType.CLOSE;
						cursor++;
						continue;
					}

					if (val == '<') { //check for open tag definition
						//startTag = cursor;
						in = true;
						tagType = Tag.TagType.OPEN;

						//check if it is CDATA (only checking 3, for performace
						if (xml.length > cursor + 2  //"<![CDATA[".lenght= 9 ('<' already processed)
								&& xml[cursor + 1] == '!'
								&& xml[cursor + 2] == '['
								&& xml[cursor + 3] == 'C'
								&& xml[cursor + 4] == 'D'
								&& xml[cursor + 5] == 'A'
								&& xml[cursor + 6] == 'T'
								&& xml[cursor + 7] == 'A'
								&& xml[cursor + 8] == '['
								) {
							inCDATA = true;
							cursor += 9;
							in = false;
							continue;
						}
					}
				}
				else { //detect end CDATA
					if (val == ']') {
						if (xml.length > cursor + 2  //"]]>".lenght= 3 (']' already processed)
								&& xml[cursor + 1] == ']'
								&& xml[cursor + 2] == '>'
								) {
							inCDATA = false;
							cdata = true;
							cursor += 3;
							continue;
						}
					}
				}
			}
			cursor++;
		}
		return null;
	}
}