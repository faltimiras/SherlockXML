package cat.altimiras.xml.pojo;


import cat.altimiras.xml.XMLElement;

import java.util.List;

public class ListTestObj2 extends XMLElement {
	private List<SimpleTestObj> SimpleTestObj;

	private String field;

	public List<SimpleTestObj> getList() {
		return SimpleTestObj;
	}

	public String getField() {
		return field;
	}
}
