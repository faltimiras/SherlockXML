package cat.altimiras.xml.pojo;

import cat.altimiras.xml.XMLElement;

import java.util.List;

public class Nested4TestObj extends XMLElement {
	private String title;
	private List<SimpleTestObj> simpleElements;

	public String getTitle() {
		return title;
	}

	public List<SimpleTestObj> getSimpleElements() {
		return simpleElements;
	}
}
