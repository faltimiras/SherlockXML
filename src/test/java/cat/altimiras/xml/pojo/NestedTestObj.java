package cat.altimiras.xml.pojo;

import cat.altimiras.xml.XMLElement;

public class NestedTestObj extends XMLElement {
	private String title;
	private SimpleTestObj simpleTestObj;

	public String getTitle() {
		return title;
	}

	public SimpleTestObj getSimpleTestObj() {
		return simpleTestObj;
	}

}
