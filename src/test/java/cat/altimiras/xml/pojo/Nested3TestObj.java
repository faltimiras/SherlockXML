package cat.altimiras.xml.pojo;


import cat.altimiras.xml.XMLElement;

public class Nested3TestObj extends XMLElement {
	private String title;
	private NestedTestObj nestedTestObj;
	private SimpleTestObj simpleTestObj1;

	public String getTitle() {
		return title;
	}

	public NestedTestObj getNestedTestObj() {
		return nestedTestObj;
	}

	public SimpleTestObj getSimpleTestObj1() {
		return simpleTestObj1;
	}

}
