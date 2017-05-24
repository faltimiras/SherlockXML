package cat.altimiras.xml.pojo;

import cat.altimiras.xml.XMLElement;

import java.util.List;

public class Nested6TestObj extends XMLElement {
	private String title;
	private SimpleTestObj simpleTestObj;
	private List<Nested5TestObj> list;

	public String getTitle() {
		return title;
	}

	public SimpleTestObj getSimpleTestObj() {
		return simpleTestObj;
	}

	public List<Nested5TestObj> getList() {
		return list;
	}
}
