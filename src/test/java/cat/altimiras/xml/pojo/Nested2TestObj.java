package cat.altimiras.xml.pojo;


import cat.altimiras.xml.XMLElement;

public class Nested2TestObj extends XMLElement {
	private String title;
	private SimpleTestObj simpleTestObj1;
	private SimpleTestObj simpleTestObj2;

	public String getTitle() {
		return title;
	}

	public SimpleTestObj getSimpleTestObj2() {
		return simpleTestObj2;
	}

	public SimpleTestObj getSimpleTestObj1() {
		return simpleTestObj1;
	}

}
